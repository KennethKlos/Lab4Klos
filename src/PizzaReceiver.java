import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

/**
 * Project: Lab 4
 * Purpose Details: Receives flat file and JSON pizza messages
 * Course: IST 242
 * Author: Ken Klos
 * Date Developed:
 * Last Date Changed:
 * Rev:
 */
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;

public class PizzaReceiver {

    private final static String FLAT_QUEUE = "pizzaFlatQueue";
    private final static String JSON_QUEUE = "pizzaJsonQueue";
    private final static String SECRET_KEY = "secret key";

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(FLAT_QUEUE, false, false, false, null);
        channel.queueDeclare(JSON_QUEUE, false, false, false, null);

        System.out.println("Waiting for messages...");

        DeliverCallback flatCallback = (consumerTag, delivery) -> {
            try {
                String payload = new String(delivery.getBody(), "UTF-8");

                String[] parts = payload.split("\\|\\|HMAC\\|\\|");
                String receivedMessage = parts[0];
                String receivedHmac = parts[1];

                String generatedHmac = HmacUtil.generateHMAC(receivedMessage, SECRET_KEY);

                System.out.println("\n========== RECEIVER FLAT ==========");
                System.out.println("Message received:   " + receivedMessage);
                System.out.println("HMAC received:      " + receivedHmac);
                System.out.println("HMAC generated:     " + generatedHmac);

                if (receivedHmac.equals(generatedHmac)) {
                    System.out.println("Integrity Check: PASSED");
                } else {
                    System.out.println("Integrity Check: FAILED");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        DeliverCallback jsonCallback = (consumerTag, delivery) -> {
            try {
                String payload = new String(delivery.getBody(), "UTF-8");

                String[] parts = payload.split("\\|\\|HMAC\\|\\|");
                String receivedMessage = parts[0];
                String receivedHmac = parts[1];

                String generatedHmac = HmacUtil.generateHMAC(receivedMessage, SECRET_KEY);

                System.out.println("\n========== RECEIVER JSON ==========");
                System.out.println("Message received:   " + receivedMessage);
                System.out.println("HMAC received:      " + receivedHmac);
                System.out.println("HMAC generated:     " + generatedHmac);

                if (receivedHmac.equals(generatedHmac)) {
                    System.out.println("Integrity Check: PASSED");
                } else {
                    System.out.println("Integrity Check: FAILED");
                }

                ObjectMapper mapper = new ObjectMapper();
                Pizza pizza = mapper.readValue(receivedMessage, Pizza.class);
                System.out.println("JSON converted back to Pizza object: " + pizza);

            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        channel.basicConsume(FLAT_QUEUE, true, flatCallback, consumerTag -> { });
        channel.basicConsume(JSON_QUEUE, true, jsonCallback, consumerTag -> { });
    }
}