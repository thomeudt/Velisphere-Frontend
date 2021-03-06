package com.velisphere.toucan.amqp;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.HashMap;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.velisphere.fs.sdk.MessageFabrik;
import com.velisphere.fs.sdk.config.ConfigData;
import com.velisphere.fs.sdk.config.ConfigFileAccess;
import com.velisphere.fs.sdk.security.HashTool;
import com.velisphere.fs.sdk.security.MessageValidator;

public class AMQPServer implements Runnable {

	private static Thread t;
	private EventInitiator eventInitiator;

	public AMQPServer(EventInitiator eventInitiator) {
		this.eventInitiator = eventInitiator;

	}

	public void run() {

		// wait until parameters from BlenderServer are available


		System.out.println(" [IN] Waiting for Configuration Information from BlenderServer...");
		
		while(ServerParameters.rabbit_ip==null)
		{
			// do nothing
		}
		
		
		String QUEUE_NAME = ServerParameters.my_queue_name;

		try {

			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(ServerParameters.rabbit_ip);
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

			System.out.println(" [IN] Server Startup Completed.");
			System.out
					.println(" [IN] Waiting for messages. To exit press CTRL+C");

			QueueingConsumer consumer = new QueueingConsumer(channel);
			channel.basicConsume(QUEUE_NAME, true, consumer);

			while (!Thread.currentThread().isInterrupted()) {
				QueueingConsumer.Delivery delivery = consumer.nextDelivery();
				String messageBody = new String(delivery.getBody());

				System.out.println(" [IN] Message JSON:" + messageBody);

				String[] hMACandPayload = new String[2];
				boolean validationResult = false;

				// parse outer JSON

				try {
					hMACandPayload = MessageFabrik.parseOuterJSON(messageBody);

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				validationResult = MessageValidator.validateHmac(
						hMACandPayload[0], hMACandPayload[1], ConfigData.epid);

				if (validationResult) {
					String msgGetAllProperties = MessageFabrik.extractProperty(
							hMACandPayload[1], "getAllProperties");

					if (msgGetAllProperties.equals("1"))
						eventInitiator.requestAllProperties();

					String msgIsAliveRequest = MessageFabrik.extractProperty(
							hMACandPayload[1], "getIsAlive");

					if (msgIsAliveRequest.equals("1"))
						eventInitiator.requestIsAlive();

					String displayMessage = MessageFabrik.extractProperty(
							hMACandPayload[1], "PR9");

					System.out
							.println(" [IN] Got new inbound message. Content: "
									+ displayMessage);

					eventInitiator.newInboundMessage(displayMessage);

				}
				
				else
				{
					System.out.println(" [IN] Message rejected - HMAC not matching. Possibly an attempted security breach.");
					
					//TODO write notification of security breach into database
				}
				

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
			String queue_name, String type) throws Exception {

		
		//TODO this is not yet updated with new RabbitMQ login mechanism, see sendJSON
		
		
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost(ServerParameters.rabbit_ip);
		connectionFactory.setUsername("dummy");
		connectionFactory.setPassword("dummy");
		connectionFactory.setVirtualHost("hClients");
		connectionFactory.setPort(5671);
		connectionFactory.useSslProtocol();

		
		

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
				.replyTo(ConfigData.epid).deliveryMode(2)
				.build();

		message.put("TYPE", type);
		java.util.Date date = new java.util.Date();
		Timestamp timeStamp = new Timestamp(date.getTime());
		message.put("TIMESTAMP", timeStamp.toString());
		message.put("EPID", ConfigData.epid);

		MessageFabrik innerMessageFactory = new MessageFabrik(message);
		String messagePackJSON = innerMessageFactory.getJsonString();

		String hMAC = HashTool.getHmacSha1(messagePackJSON, ConfigData.secret);

		System.out.println("Using secret: "+ ConfigData.secret);

		System.out.println("Using endpointID: "+ ConfigData.epid);
		
		
		HashMap<String, String> submittableMessage = new HashMap<String, String>();

		submittableMessage.put(hMAC, messagePackJSON);

		MessageFabrik outerMessageFactory = new MessageFabrik(
				submittableMessage);
		String submittableJSON = outerMessageFactory.getJsonString();

		System.out.println("HMAC:" + hMAC);
		System.out.println("Submittable:" + submittableJSON);

		channel.basicPublish("", "controller", props,
				submittableJSON.getBytes());

		// System.out.println(" [x] Sent '" + messagePackText + "'");

		channel.close();
		connection.close();
	}

	public static void sendJSON(String username, String password, String message,
			String queue_name, String type) throws Exception {

		
		
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost(ServerParameters.rabbit_ip);
		connectionFactory.setUsername(username);
		connectionFactory.setPassword(password);
		connectionFactory.setPort(5671);
		connectionFactory.useSslProtocol();

		
		

		
		connectionFactory.setVirtualHost("hController");
		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();

/*
		if (queue_name.equals("controller")) {
			boolean durable = true;
			channel.queueDeclare(queue_name, durable, false, false, null);
		} else {
			channel.queueDeclare(queue_name, false, false, false, null);
		}

		// implemented reply queue to allow tracing the sender for callback
*/
		BasicProperties props = new BasicProperties.Builder()
				.replyTo(ConfigData.epid).deliveryMode(2)
				.build();


		channel.basicPublish("", "controller", props,
				message.getBytes());

		// System.out.println(" [x] Sent '" + messagePackText + "'");

		channel.close();
		connection.close();
	}

	
	
	public static void startServer(EventInitiator eventInitiator) {

		/*
		 * Show startup message
		 */

		System.out.println();
		System.out
				.println("    * *    VeliSphere SDK Server v0.2a / AMQP (vanilla)");
		System.out
				.println("    * * *  Copyright (C) 2015 Thorsten Meudt/Connected Things Lab. All rights reserved.");
		System.out.println("**   *    ");
		System.out.println("  * *   ");
		System.out
				.println("   *       VeliSphere SDK Server is part of the VeliSphere IoTS ecosystem.");
		System.out.println();
		System.out.println();
		System.out.println(" [IN] This product includes GeoLite data created by MaxMind, available from http://www.maxmind.com");
		System.out.println();
		System.out.println();
		System.out.println(" [IN] Starting server...");

		// load config data

		ConfigFileAccess.loadParamChangesAsXML();

		System.out.println(" [IN] Endpoint ID: " + ConfigData.epid);
		System.out.println(" [IN] Secret: " + ConfigData.secret);

		t = new Thread(new AMQPServer(eventInitiator), "listener");
		t.start();

	}

}
