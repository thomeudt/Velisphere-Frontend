package com.velisphere.fs;
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
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.AMQP.BasicProperties;

import java.io.StringWriter;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

public class Send {

	public static void main(String message, String queue_name) throws Exception {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(ServerParameters.bunny_ip);
		factory.setUsername("dummy");
		factory.setPassword("dummy");
		factory.setVirtualHost("hController");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		// System.out.println("QUEUE DEFINED AS....."+queue_name+"...");
		if (queue_name.equals("controller")) {
			boolean durable = true;
			channel.queueDeclare(queue_name, false, false, false, null);
		} else {
			channel.queueDeclare(queue_name, false, false, false, null);
		}

		// implemented reply queue to allow tracing the sender for callback

		BasicProperties props = new BasicProperties.Builder()
		.replyTo(ServerParameters.my_queue_name).deliveryMode(2)
		.build();

		message = "[" + ServerParameters.my_queue_name + "] " + message;

		
		JSONObject messagePack = new JSONObject();
		JsonTools tooler = new JsonTools();

		messagePack = tooler.addArgument(messagePack,
				ServerParameters.my_queue_name, "EPID");
		messagePack = tooler.addArgument(messagePack, queue_name, "0");
		messagePack = tooler.addArgument(messagePack, message, "1");
		messagePack = tooler.addArgument(messagePack, null, "TIMESTAMP");
		messagePack = tooler.addArgument(messagePack, "REG", "TYPE");

		StringWriter out = new StringWriter();
		
		messagePack.writeJSONString(out);

		String messagePackText = out.toString();

		System.out.println(messagePack.toJSONString());
		
		channel.basicPublish("", "controller", props,
				messagePackText.getBytes());
		// channel.basicPublish("", "sana", props, messagePackText.getBytes());

		// System.out.println(" [x] Sent '" + message + "'");

		channel.close();
		connection.close();
	}

	public static void sendHashTable(HashMap<String, String> message,
			String queue_name) throws Exception {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(ServerParameters.bunny_ip);
		factory.setUsername("dummy");
		factory.setPassword("dummy");
		
		factory.setVirtualHost("hController");
		Connection connection = factory.newConnection();
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
		
						
		String messagePackText = MessagePack.buildMessagePack(message);
		
		System.out.println(messagePackText);

		channel.basicPublish("", "controller", props,
				messagePackText.getBytes());

		// System.out.println(" [x] Sent '" + messagePackText + "'");

		channel.close();
		connection.close();
	}

}
