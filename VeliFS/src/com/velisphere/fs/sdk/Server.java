package com.velisphere.fs.sdk;
/*******************************************************************************
 * CONFIDENTIAL INFORMATION
 *  __________________
 *  
 *   Copyright (C) 2013 Thorsten Meudt 
 *   All Rights Reserved.
 *  
 *  NOTICE:  All information contained herein is, and remains
 *  the property of Thorsten Meudt and its suppliers,
 *  if any.  The intellectual and technical concepts contained
 *  herein are proprietary to Thorsten Meudt
 *  and its suppliers and may be covered by Patents,
 *  patents in process, and are protected by trade secret or copyright law.
 *  Dissemination of this information or reproduction of this material
 *  is strictly forbidden unless prior written permission is obtained
 *  from Thorsten Meudt.
 ******************************************************************************/
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.client.AMQP.BasicProperties;


public class Server implements Runnable {

	
	
    public  void run() {

    String QUEUE_NAME = ServerParameters.my_queue_name;

	try {
	
		
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(ServerParameters.bunny_ip);
		factory.setUsername("dummy");
		factory.setPassword("dummy");
		
		factory.setVirtualHost("hClients");
		Connection connection = factory.newConnection();

		
		
		
    Channel channel = connection.createChannel();

    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
     
    QueueingConsumer consumer = new QueueingConsumer(channel);
    channel.basicConsume(QUEUE_NAME, true, consumer);
	
   while (!Thread.currentThread().isInterrupted()){
    QueueingConsumer.Delivery delivery = consumer.nextDelivery();
    String message = new String(delivery.getBody());
    System.out.println(" [x] Received '" + message + "'");
    
    boolean admFilterResult = filterMqttAdmMessage(message);   
    if(admFilterResult==false){
    
    	String addText = MessageFabrik.extractProperty(message, "PR9");
    	
    }
    
   }
   }catch(InterruptedException e){
       Thread.currentThread().interrupt();
       
   }
	 catch (IOException | ShutdownSignalException | ConsumerCancelledException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
  }
    
    
    public static boolean filterMqttAdmMessage(String message){
		
    	
    	return false;
		
	}
	
    public static void sendHashTable(HashMap<String, String> message,
			String queue_name) throws Exception {

		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost(ServerParameters.bunny_ip);
		connectionFactory.setUsername("dummy");
		connectionFactory.setPassword("dummy");
		
		connectionFactory.setVirtualHost("hController");
		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();

		// System.out.println("QUEUE DEFINED AS....."+queue_name+"...");
		if (queue_name.equals("controller")) {
			boolean durable = true;
			channel.queueDeclare(queue_name, durable, false, false, null);
		} else {
			channel.queueDeclare(queue_name, false, false, false, null);
		}

		// implemented reply queue to allow tracing the sender for callback

		BasicProperties props = new BasicProperties.Builder()
		.replyTo(ServerParameters.my_queue_name).deliveryMode(2)
		.build();

			
		
		message.put("TYPE", "REG");
		java.util.Date date= new java.util.Date();
		Timestamp timeStamp = new Timestamp(date.getTime());
		message.put("TIMESTAMP", timeStamp.toString());
		message.put("EPID", ServerParameters.my_queue_name);
		
						
		MessageFabrik messageFactory = new MessageFabrik(message);
		String messagePackText = messageFactory.getJsonString();
		
		System.out.println(messagePackText);

		channel.basicPublish("", "controller", props,
				messagePackText.getBytes());

		// System.out.println(" [x] Sent '" + messagePackText + "'");

		channel.close();
		connection.close();
	}

    
}
