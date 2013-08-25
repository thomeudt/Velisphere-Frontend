package com.example.blubbermobile;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;



public class Send {

  

  public static void main(String message, String queue_name, String myQueueName) throws Exception {
      	      
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(ServerParameters.bunny_ip);
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    
    
    System.out.println("QUEUE DEFINED AS....."+queue_name+"...");  
    if (queue_name.equals("controller"))
    {
    	boolean durable = true;
    	channel.queueDeclare(queue_name, durable, false, false, null);
    }
    else{
    	channel.queueDeclare(queue_name, false, false, false, null);	
    }
    
    
    
    message = "[" + myQueueName + "] " + message;
    channel.basicPublish("", queue_name, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
    System.out.println(" [x] Sent '" + message + "'");
    
    channel.close();
    connection.close();
  }
}