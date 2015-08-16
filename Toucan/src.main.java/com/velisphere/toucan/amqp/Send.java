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
package com.velisphere.toucan.amqp;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.AMQP.BasicProperties;


public class Send {

	String message = new String();
	String queue_name = new String();

	Send(String m, String q) {
		message = m;
		queue_name = q;
	}

	
	
	
	public static void sendHashTable(HashMap<String, String> message,
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
	


}
