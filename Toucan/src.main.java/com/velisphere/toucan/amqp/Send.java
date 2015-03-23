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
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.velisphere.toucan.ConfigHandler;

public class Send {

	String message = new String();
	String queue_name = new String();

	Send(String m, String q) {
		message = m;
		queue_name = q;
	}

	
	
	
	public static void sendHashTable(HashMap<String, String> message,
			String targetQueueName, String senderQueueName) throws Exception {

		ConfigHandler conf = new ConfigHandler();
		conf.loadParamChangesAsXML();
		
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(ServerParameters.bunny_ip);
		factory.setUsername("guest");
		factory.setPassword("guest");
		

		Connection connection;
		connection = factory.newConnection();

		
		Channel channel = connection.createChannel();

		//String queueName = channel.queueDeclare().getQueue();
        //channel.queueBind(queueName, "xClients", targetQueueName);

		ObjectMapper mapper = new ObjectMapper();
		StringWriter writer = new StringWriter();
		HashMap<String, String> messageMap = new HashMap<String, String>();
		messageMap.put("SECTOK", null);
		messageMap.put("TIMESTAMP", null);
		messageMap.put("TYPE", "REG");
		messageMap.put("EPID", senderQueueName);
		messageMap.putAll(message);
		
		mapper.writeValue(writer, messageMap);
		
		//System.out.println("Target: " + targetQueueName);

		channel.basicPublish("xClients", targetQueueName, null,
				writer.toString().getBytes());

		// System.out.println(" [x] Sent '" + writer.toString() + "' from " + senderQueueName + " to " + targetQueueName);

		channel.close();
		
	}
	


}
