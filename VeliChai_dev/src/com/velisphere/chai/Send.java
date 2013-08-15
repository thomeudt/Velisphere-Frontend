package com.velisphere.chai;

import org.json.JSONObject;
import org.json.JSONStringer;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;



public class Send {

  public static void main(String message, String queue_name) throws Exception {
      	      
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(ServerParameters.bunny_ip);
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.queueDeclare(queue_name, false, false, false, null);
    
    message = "[" + "Redirect" + "] " + message;
    channel.basicPublish("", queue_name, null, message.getBytes());
    System.out.println(" [x] Sent '" + message + "'");
    
    channel.close();
    connection.close();
  }
  
  public static void sendJson (String jsonContainer, String queue_name) throws Exception {
	      
	    ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost(ServerParameters.bunny_ip);
	    Connection connection = factory.newConnection();
	    Channel channel = connection.createChannel();

	    channel.queueDeclare(queue_name, false, false, false, null);
	    
	    
	    channel.basicPublish("", queue_name, null, jsonContainer.getBytes());
	    System.out.println(" [x] Sent '" + jsonContainer + "'");
	    
	    channel.close();
	    connection.close();
	  }
}