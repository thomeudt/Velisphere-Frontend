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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.velisphere.fs.actors.SimFunctions;
import com.velisphere.fs.sdk.config.ConfigData;
import com.velisphere.fs.sdk.config.ConfigFileAccess;
import com.velisphere.fs.sdk.security.HashTool;

import flightsim.simconnect.config.ConfigurationNotFoundException;


public class Server implements Runnable {

	private static Thread t;
	private CTLInitiator ctlInitiator;
	
	public Server(CTLInitiator ctlInitiator){
		this.ctlInitiator = ctlInitiator;
		
	}

	public void run() {

		String QUEUE_NAME = ConfigData.epid;

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
			
			System.out.println(" [IN] Server Startup Completed.");
			System.out
					.println(" [IN] Waiting for messages. To exit press CTRL+C");

			QueueingConsumer consumer = new QueueingConsumer(channel);
			channel.basicConsume(QUEUE_NAME, true, consumer);

			
			
			while (!Thread.currentThread().isInterrupted()) {
				QueueingConsumer.Delivery delivery = consumer.nextDelivery();
				String message = new String(delivery.getBody());
				String msgType = MessageFabrik.extractProperty(message, "TYPE");
				System.out.println(" [IN] Received " + msgType+ " message: '" + message + "'");

				
				if (msgType.equals("CTL"))
					{
						processCtlMessage(message);
					}
					
				
				if (msgType.equals("CNF"))
					{
					System.out.println(" [IN] Processing of CNF message started");
					String msgSetFlightNumber = MessageFabrik.extractProperty(message,
							"0d5af911-2bff-4bf8-b1f3-132c5671930e");
						if (!msgSetFlightNumber.isEmpty())
						{
							SimFunctions.setFlightNumber(msgSetFlightNumber);
						}

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
		} catch (ConfigurationNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private void processCtlMessage(String message) throws JsonProcessingException, IOException
	{
		String msgGetAllProperties = MessageFabrik.extractProperty(message,
				"getAllProperties");

			if (msgGetAllProperties.equals("1")) ctlInitiator.requestAllProperties();

			String msgIsAliveRequest = MessageFabrik.extractProperty(message,
			"getIsAlive");

			if (msgIsAliveRequest.equals("1")) ctlInitiator.requestIsAlive();

		
	}
	
	private void processCnfMessage(String message) throws JsonProcessingException, IOException, ConfigurationNotFoundException
	{
		
		System.out.println(" [IN] Processing of CNF message started");
		String msgSetFlightNumber = MessageFabrik.extractProperty(message,
				"0d5af911-2bff-4bf8-b1f3-132c5671930e");
			if (!msgSetFlightNumber.isEmpty())
			{
				SimFunctions.setFlightNumber(msgSetFlightNumber);
			}

		
	}
	
	
	


	public static void sendHashTable(HashMap<String, String> message,
			String queue_name, String type) throws Exception {

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

	public static void startServer(CTLInitiator ctlInitiator) {


		/*
		 * Show startup message
		 */

		System.out.println();
		System.out.println("    * *    VeliSphere SDK Server v0.2.2 / AMQP (vanilla)");
		System.out
				.println("    * * *  Copyright (C) 2015 Thorsten Meudt/Connected Things Lab. All rights reserved.");
		System.out.println("**   *    ");
		System.out.println("  * *   ");
		System.out
				.println("   *       VeliSphere SDK Server is part of the VeliSphere IoTS ecosystem.");
		System.out.println();
		System.out.println();
		System.out.println(" [IN] Starting server...");
		
		// load config data
		
		ConfigFileAccess.loadParamChangesAsXML();
				
		System.out.println(" [IN] Endpoint ID: " + ConfigData.epid);
		System.out.println(" [IN] Secret: " + ConfigData.secret);
				

		t = new Thread(new Server(ctlInitiator), "listener");
		t.start();

	}

}
