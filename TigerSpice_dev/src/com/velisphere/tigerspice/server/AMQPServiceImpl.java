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
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.HashMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.voltdb.VoltTable;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonMappingException;
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
			sendHashTable(messageHash, endpointID, "CTL");
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
			sendHashTable(messageHash, endpointID, "CTL");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "OK";
		
		
	}
	
	
	@Override
	public String sendConfigMessage(String endpointID, String propertyID, String value) {
					
	HashMap<String, String> messageHash = new HashMap<String, String>();
	messageHash.put(propertyID, value);
	
	try {
		sendHashTable(messageHash, endpointID, "CNF");
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
	return "OK";
	
	}
	
	@Override
	public String sendRegMessage(String endpointID, String propertyID, String value) {
					
	HashMap<String, String> messageHash = new HashMap<String, String>();
	messageHash.put(propertyID, value);
	
	try {
		sendHashTable(messageHash, endpointID, "REG");
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
	return "OK";
	
	}


	
	private void sendHashTable(HashMap<String, String> message,
			String queue_name, String messageType) throws Exception {

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

		
		/*
		message.put("TYPE", messageType);
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
		*/
		
		
		
		HashMap<String, String> messageMap = new HashMap<String, String>();
		messageMap.put("SECTOK", null);
		messageMap.put("TIMESTAMP", null);
		messageMap.put("TYPE", messageType);
		messageMap.put("EPID", ServerParameters.my_queue_name);
		messageMap.putAll(message);
	
				
		String messagePackJSON = MessageFabrik.buildMessagePack(messageMap);
		
		String hMAC = HashTool.getHmacSha1(messagePackJSON, MessageValidator.getSecretFromMontana(queue_name));

		HashMap<String, String> submittableMessage = new HashMap<String, String>();
		
		submittableMessage.put(hMAC, messagePackJSON);
				
		String submittableJSON = MessageFabrik.buildMessagePack(submittableMessage);
		
		System.out.println("HMAC:" + hMAC);
		System.out.println("Submittable:" + submittableJSON);
		System.out.println("Target Queue:" + queue_name);
		

		channel.basicPublish("", queue_name, null,
				submittableJSON.getBytes());

		channel.close();
	}
	
	

	
	private static class MessageFabrik {
		
		
		static ObjectMapper mapper = new ObjectMapper(); // object mapper for
		
		public static JsonFactory factory = mapper.getFactory();
		
		
		public String extractProperty(String jsonInput, String propertyID) throws JsonProcessingException, IOException 
		{

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

		public HashMap<String, String> extractKeyPropertyPairs(String jsonInput) throws JsonProcessingException, IOException 
		{

			JsonParser jp = factory.createParser(jsonInput);
			HashMap<String, String> foundMap = new HashMap<String, String>();

			while (jp.nextToken() != JsonToken.END_OBJECT) {

				String fieldname = jp.getCurrentName();
				foundMap.put(jp.getCurrentName(), jp.getText());
			}

			jp.close();		 

			return foundMap;  
		}
		
		public static  String buildMessagePack(Object object)
		{
		
					
			ObjectMapper mapper = new ObjectMapper();
			StringWriter writer = new StringWriter();
			try {
				mapper.writeValue(writer, object);
			} catch (JsonGenerationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return writer.toString();
		
		}

		public String[] parseOuterJSON(String messageBody) throws IOException
		{
		
			System.out.println("Parsing outer: " + messageBody);
			JsonParser jp = factory.createParser(messageBody);
			
			String[] hMACandPayload = new String[2];
			
			while (jp.nextToken() != JsonToken.END_OBJECT) {

				
				hMACandPayload[0] = jp.getCurrentName();
				hMACandPayload[1] = jp.getText();
			}

			jp.close();		 
			

			return hMACandPayload;  

		}

		
	}
	
	
	private static class MessageValidator {

		
		public static boolean validateHmac(String receivedHMAC, String payload, String endpointID) throws NoConnectionsException, IOException, ProcCallException
		{
			

			String secret = null;
			
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

			
			ClientResponse findSecret = voltCon.montanaClient
					.callProcedure("SEC_SelectSecretForEndpointID", endpointID);

			final VoltTable findSecretResults[] = findSecret.getResults();

			VoltTable result = findSecretResults[0];

			while (result.advanceRow()) {
					
					secret = result
							.getString("SECRET");
					
			}

			System.out.println("Endpoint ID: " + endpointID );
			System.out.println("Secret in DB: " + secret );
			
			String calculatedHmac = HashTool.getHmacSha1(payload, secret);
			
			System.out.println("Calculated HMAC: " + calculatedHmac + " <> Received HMAC: " + receivedHMAC);
			
			boolean validationOK = false;
			
			if (calculatedHmac.equals(receivedHMAC)) validationOK = true;
			
			return validationOK;

		}
		
		
		public static String getSecretFromMontana(String endpointID) throws NoConnectionsException, IOException, ProcCallException
		{
			

			String secret = null;
			
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

			
			ClientResponse findSecret = voltCon.montanaClient
					.callProcedure("SEC_SelectSecretForEndpointID", endpointID);

			final VoltTable findSecretResults[] = findSecret.getResults();

			VoltTable result = findSecretResults[0];

			while (result.advanceRow()) {
					
					secret = result
							.getString("SECRET");
					
			}

			System.out.println("Secret in DB: " + secret );
			
					
			return secret;
		
		}
	
	}
	
	private static class HashTool {
		
		public static String getHmacSha1(String value, String key) {
	        try {
	            // Get an hmac_sha1 key from the raw key bytes
	            byte[] keyBytes = key.getBytes();           
	            SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");

	            // Get an hmac_sha1 Mac instance and initialize with the signing key
	            Mac mac = Mac.getInstance("HmacSHA1");
	            mac.init(signingKey);

	            // Compute the hmac on input data bytes
	            byte[] rawHmac = mac.doFinal(value.getBytes());

	            // Convert raw bytes to Hex
	            byte[] hexBytes = new Hex().encode(rawHmac);

	            //  Covert array of Hex bytes to a String
	            return new String(hexBytes, "UTF-8");
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	    }
	
	}
	
}
