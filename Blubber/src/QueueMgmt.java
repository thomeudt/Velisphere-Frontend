import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


public class QueueMgmt {
    
	public static void deleteQueue(String queueName) throws IOException{
	
	ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(ServerParameters.bunny_ip);
    Connection connection;
		connection = factory.newConnection();
	
    Channel channel = connection.createChannel();

    channel.queueDelete(queueName);
    
    channel.close();
    connection.close();
    
    
    System.out.println(" [!] Queue deleted..."+queueName);
	}

	public static void bindQueueFanout(String queueName, String exchangeName) throws IOException{
		  
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(ServerParameters.bunny_ip);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		    
		
		channel.exchangeDeclare(exchangeName, "fanout");
	    channel.queueBind(queueName, exchangeName, "");
	    
	    channel.close();
	    connection.close();
	    
	
	
	}


}
