package com.velisphere.chai;



import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;



public class Broadcast {

  public static void broadcastToExchange(String message, String exchange_name) throws Exception {
      	      
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(ServerParameters.bunny_ip);
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.exchangeDeclare(exchange_name, "fanout");
    // channel.queueDeclare(queue_name, false, false, false, null);
    
   
    channel.basicPublish(exchange_name, "", null, message.getBytes());
    System.out.println(" [x] Sent '" + message + "'");
    
    channel.close();
    connection.close();
  }
  
  
}