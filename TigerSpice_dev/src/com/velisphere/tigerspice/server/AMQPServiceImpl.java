/*******************************************************************************
 * CONFIDENTIAL INFORMATION
 *  __________________
 *  
 *   Copyright (C) 2014 Thorsten Meudt / Connected Things Lab
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
package com.velisphere.tigerspice.server;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.HashMap;

import org.voltdb.client.ClientResponse;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.velisphere.tigerspice.client.amqp.AMQPService;

@SuppressWarnings("serial")
public class AMQPServiceImpl extends RemoteServiceServlet implements
		AMQPService {

	@Override
	public String sendGetAllProperties(String endpointID) {
		
		HashMap<String, String> messageHash = new HashMap<String, String>();
				
		messageHash.put("getAllProperties", "1");
		
		try {
			sendHashTable(messageHash, endpointID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "OK";
	}
	
	@Override
	public String sendIsAliveRequest(String endpointID) {
		// TODO Auto-generated method stub
		
		VoltConnector voltCon = new VoltConnector();
		
		try {
			voltCon.openDatabase();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			ClientResponse bleUpdateCheckState = voltCon.montanaClient
					.callProcedure("SRV_UpdateEndpointState", endpointID, "UNKNOWN");
		} catch (IOException | ProcCallException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
					
		HashMap<String, String> messageHash = new HashMap<String, String>();
		messageHash.put("getIsAlive", "1");
		
		try {
			sendHashTable(messageHash, endpointID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "OK";
		
		
	}


	
	private void sendHashTable(HashMap<String, String> message,
			String queue_name) throws Exception {

		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost(ServerParameters.rabbit_ip);
		connectionFactory.setUsername("dummy");
		connectionFactory.setPassword("dummy");

		connectionFactory.setVirtualHost("hClients");
		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();

		// System.out.println("QUEUE DEFINED AS....."+queue_name+"...");
		channel.queueDeclare(ServerParameters.my_queue_name, false, false, false, null);
		

		// implemented reply queue to allow tracing the sender for callback

		BasicProperties props = new BasicProperties.Builder()
				.replyTo(ServerParameters.my_queue_name).deliveryMode(2)
				.build();

		message.put("TYPE", "CTL");
		java.util.Date date = new java.util.Date();
		Timestamp timeStamp = new Timestamp(date.getTime());
		message.put("TIMESTAMP", timeStamp.toString());
		message.put("EPID", ServerParameters.my_queue_name);

		MessageFabrik messageFactory = new MessageFabrik(message);
		String messagePackText = messageFactory.getJsonString();

		System.out.println(messagePackText);

		channel.basicPublish("", queue_name, props,
				messagePackText.getBytes());

		// System.out.println(" [x] Sent '" + messagePackText + "'");

		channel.close();
		connection.close();
	}
	
	

	
	private class MessageFabrik {

		
		String jsonString;
		
		public MessageFabrik(Object object)
		{
		
			ObjectMapper mapper = new ObjectMapper();
			
			System.out.println("Intake: " + object.toString());
		 
			jsonString = new String();
			try {
				jsonString = mapper.writeValueAsString(object);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
			System.out.println("JSON generiert: " + jsonString);
					
		}

		
		
		public String extractProperty(String jsonInput, String propertyID) throws JsonProcessingException, IOException 
		{

			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = mapper.getFactory();
			JsonParser jp = factory.createParser(jsonInput);
			String foundValue = new String();

			while (jp.nextToken() != JsonToken.END_OBJECT) {

				String fieldname = jp.getCurrentName();
				if (propertyID.equals(fieldname)) {
					jp.nextToken();
					foundValue = jp.getText(); 
				}
			}

			jp.close();		 

			return foundValue;  
		}



		public String getJsonString() {
			return jsonString;
		}

			
		


	}


	
	
}
