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
package com.velisphere.chai;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.AMQP.BasicProperties;

public class Send implements Runnable {

	String message = new String();
	String queue_name = new String();

	Send(String m, String q) {
		message = m;
		queue_name = q;
	}

	public static void main(String message, String queue_name) throws Exception {
		BrokerConnection bc = new BrokerConnection();
		Channel channel = bc.establishTxChannel();
		channel.queueDeclare(queue_name, false, false, false, null);
		message = "[" + "via controller" + "] " + message;
		channel.basicPublish("", queue_name, null, message.getBytes());
		channel.close();
	}
	
	
	public static void sendHashTable(HashMap<String, String> message,
			String targetQueueName, String senderQueueName) throws Exception {

		BrokerConnection bc = new BrokerConnection();
		Channel channel = bc.establishTxChannel();
		channel.queueDeclare(senderQueueName, false, false, false, null);


		ObjectMapper mapper = new ObjectMapper();
		StringWriter writer = new StringWriter();
		HashMap<String, String> messageMap = new HashMap<String, String>();
		messageMap.put("SECTOK", null);
		messageMap.put("TIMESTAMP", null);
		messageMap.put("TYPE", "REG");
		messageMap.put("EPID", senderQueueName);
		messageMap.putAll(message);
		mapper.writeValue(writer, messageMap);
		

		channel.basicPublish("", targetQueueName, null,
				writer.toString().getBytes());

		// System.out.println(" [x] Sent '" + writer.toString() + "' from " + senderQueueName + " to " + targetQueueName);

		channel.close();
		
	}
	

	public static void sendJson(String jsonContainer, String queue_name)
			throws Exception {
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

	@Override
	public void run() {
		// Not used yet, need to evaluate if sending as a thread makes a lot of
		// sense from a performance point of view

		Connection connection;
		try {
			connection = BrokerConnection.factory.newConnection();
			Channel channel = connection.createChannel();
			channel.queueDeclare(queue_name, false, false, false, null);
			message = "[" + "via controller" + "] " + message;
			channel.basicPublish("", queue_name, null, message.getBytes());
			channel.close();
			connection.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
