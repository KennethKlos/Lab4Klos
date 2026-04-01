import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/*
  Project: Lab 4
  Purpose Details: Sends flat file and JSON pizza messages
  Course: IST 242
  Author: Ken Klos
  Date Developed:
  Last Date Changed:
  Rev:
 */
public class PizzaSender {

    private static final String FLAT_QUEUE = "pizzaFlatQueue";
    private static final String JSON_QUEUE = "pizzaJsonQueue";

    public static void main(String[] args) throws Exception {

        // Create pizza object
        Pizza pizza = new Pizza("Large", "Thin Crust", "Pepperoni", 12.99);

        // Flat file message
        String flatMessage = pizza.toString();

        // JSON message
        ObjectMapper mapper = new ObjectMapper();
        String jsonMessage = mapper.writeValueAsString(pizza);

        // RabbitMQ setup
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // Create queues
        channel.queueDeclare(FLAT_QUEUE, false, false, false, null);
        channel.queueDeclare(JSON_QUEUE, false, false, false, null);

        // Send flat file message
        channel.basicPublish("", FLAT_QUEUE, null, flatMessage.getBytes("UTF-8"));
        System.out.println("Sent flat file message: " + flatMessage);

        // Send JSON message
        channel.basicPublish("", JSON_QUEUE, null, jsonMessage.getBytes("UTF-8"));
        System.out.println("Sent JSON message: " + jsonMessage);

        channel.close();
        connection.close();
    }
}
