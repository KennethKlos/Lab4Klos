import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Project: Lab 4
 * Purpose Details: Sends flat file and JSON pizza messages
 * Course: IST 242
 * Author: Ken Klos
 * Date Developed:
 * Last Date Changed:
 * Rev:
 */
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class PizzaSender {

    private final static String FLAT_QUEUE = "pizzaFlatQueue";
    private final static String JSON_QUEUE = "pizzaJsonQueue";
    private final static String SECRET_KEY = "secret key";

    public static void main(String[] args) throws Exception {

        Pizza pizza = new Pizza("Large", "Thin Crust", "Pepperoni", 12.99);

        String flatMessage = pizza.toString();

        ObjectMapper mapper = new ObjectMapper();
        String jsonMessage = mapper.writeValueAsString(pizza);

        String flatHmac = HmacUtil.generateHMAC(flatMessage, SECRET_KEY);
        String jsonHmac = HmacUtil.generateHMAC(jsonMessage, SECRET_KEY);

        String flatPayload = flatMessage + "||HMAC||" + flatHmac;
        String jsonPayload = jsonMessage + "||HMAC||" + jsonHmac;

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(FLAT_QUEUE, false, false, false, null);
        channel.queueDeclare(JSON_QUEUE, false, false, false, null);

        channel.basicPublish("", FLAT_QUEUE, null, flatPayload.getBytes("UTF-8"));
        System.out.println("========== SENDER FLAT ==========");
        System.out.println("Message sent: " + flatMessage);
        System.out.println("HMAC sent:    " + flatHmac);

        channel.basicPublish("", JSON_QUEUE, null, jsonPayload.getBytes("UTF-8"));
        System.out.println("========== SENDER JSON ==========");
        System.out.println("Message sent: " + jsonMessage);
        System.out.println("HMAC sent:    " + jsonHmac);

        channel.close();
        connection.close();
    }
}