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
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
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

	private static Thread t;
	private CTLInitiator ctlInitiator;
	
	public Server(CTLInitiator ctlInitiator){
		this.ctlInitiator = ctlInitiator;
		
	}

	public void run() {

		String QUEUE_NAME = ServerParameters.my_queue_name;

		try {

			
		
			
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(ServerParameters.bunny_ip);
			factory.setUsername("dummy");
			factory.setPassword("dummy");
			factory.setVirtualHost("hClients");
			factory.setPort(5671);
			factory.useSslProtocol();
			
			Connection connection = null;
			
			try {
				connection = factory.newConnection();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			Channel channel = connection.createChannel();

			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			
			System.out.println(" [IN] Completed...");
			System.out
					.println(" [IN] Waiting for messages. To exit press CTRL+C");

			QueueingConsumer consumer = new QueueingConsumer(channel);
			channel.basicConsume(QUEUE_NAME, true, consumer);

			
			
			while (!Thread.currentThread().isInterrupted()) {
				QueueingConsumer.Delivery delivery = consumer.nextDelivery();
				String message = new String(delivery.getBody());
				System.out.println(" [x] Received '" + message + "'");

			
				
				String msgGetAllProperties = MessageFabrik.extractProperty(message,
							"getAllProperties");
				
				if (msgGetAllProperties.equals("1")) ctlInitiator.requestAllProperties();
				
				String msgIsAliveRequest = MessageFabrik.extractProperty(message,
						"getIsAlive");
			
				if (msgIsAliveRequest.equals("1")) ctlInitiator.requestIsAlive();;
				
					
			}
			
			
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			System.out.println(" [ER] Server has shut down. Reason: "
					+ e.getMessage());
		} catch (IOException | ShutdownSignalException
				| ConsumerCancelledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(" [ER] Server has shut down. Reason: "
					+ e.getMessage());
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
		java.util.Date date = new java.util.Date();
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

	public static void startServer(String endpointID, CTLInitiator ctlInitiator) {

		// set queue name for (rabbitMQ)
		ServerParameters.my_queue_name = endpointID;

		/*
		 * Show startup message
		 */

		System.out.println();
		System.out.println("    * *    VeliSphere SDK Server v0.1 (vanilla)");
		System.out
				.println("    * * *  Copyright (C) 2015 Thorsten Meudt/Connected Things Lab. All rights reserved.");
		System.out.println("**   *    ");
		System.out.println("  * *   ");
		System.out
				.println("   *       VeliSphere SDK Server is part of the VeliSphere IoTS ecosystem.");
		System.out.println();
		System.out.println();
		System.out.println(" [IN] Starting server...");

		t = new Thread(new Server(ctlInitiator), "listener");
		t.start();

	}

}
