package com.example.blubbermobile;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Route {

	
	public static void sendToDirectExchange(String message, String exchange_name, String routingKey) throws Exception {
	      
	    ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost(ServerParameters.bunny_ip);
	    Connection connection = factory.newConnection();
	    Channel channel = connection.createChannel();

	    channel.exchangeDeclare(exchange_name, "direct");
	  	   
	    channel.basicPublish(exchange_name, routingKey, null, message.getBytes());
	    System.out.println(" [x] Sent '" + message + "'");
	    
	    channel.close();
	    connection.close();
	  }
	
	
}
