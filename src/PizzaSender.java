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
public class PizzaSender {

    private final static String FLAT_QUEUE = "pizzaFlatQueue";
    private final static String JSON_QUEUE = "pizzaJsonQueue";

    public static void main(String[] args) throws Exception {

        Pizza pizza = new Pizza("Large", "Thin Crust", "Pepperoni", 12.99);

        String flatMessage = pizza.toString();

        ObjectMapper mapper = new ObjectMapper();
        String jsonMessage = mapper.writeValueAsString(pizza);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(FLAT_QUEUE, false, false, false, null);
        channel.queueDeclare(JSON_QUEUE, false, false, false, null);

        channel.basicPublish("", FLAT_QUEUE, null, flatMessage.getBytes("UTF-8"));
        System.out.println("Sent flat file message: " + flatMessage);

        channel.basicPublish("", JSON_QUEUE, null, jsonMessage.getBytes("UTF-8"));
        System.out.println("Sent JSON message: " + jsonMessage);

        channel.close();
        connection.close();
    }
}