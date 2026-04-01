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
public class PizzaReceiver {

    private final static String FLAT_QUEUE = "pizzaFlatQueue";
    private final static String JSON_QUEUE = "pizzaJsonQueue";

    public static void main(String[] argv) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(FLAT_QUEUE, false, false, false, null);
        channel.queueDeclare(JSON_QUEUE, false, false, false, null);

        System.out.println("Waiting for messages...");

        DeliverCallback flatDeliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("Received flat file message: " + message);
        };

        DeliverCallback jsonDeliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);

            ObjectMapper mapper = new ObjectMapper();
            Pizza pizza = mapper.readValue(message, Pizza.class);

            System.out.println("Received JSON message: " + message);
            System.out.println("Pizza object from JSON: " + pizza);
        };

        channel.basicConsume(FLAT_QUEUE, true, flatDeliverCallback, consumerTag -> { });
        channel.basicConsume(JSON_QUEUE, true, jsonDeliverCallback, consumerTag -> { });
    }
}