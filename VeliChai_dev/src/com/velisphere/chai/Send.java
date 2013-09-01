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

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.rabbitmq.client.Channel;



public class Send implements Runnable {

	String message = new String();
	String queue_name = new String();
	Send(String m, String q) { message = m; queue_name = q; }

	public static void main(String message, String queue_name) throws Exception {
		BrokerConnection bc = new BrokerConnection();
		Channel channel = bc.establishTxChannel();  
		channel.queueDeclare(queue_name, false, false, false, null);
		
		
		
		message = "[" + "via controller" + "] " + message;
		// channel.basicPublish("", queue_name, null, message.getBytes());
		
		MessageProperties mp = new MessageProperties();
		bc.veliTemplate.send(queue_name, new Message(message.getBytes(), mp));
		
		
		
		System.out.println(message);
		channel.close();
	}

	public static void sendJson (String jsonContainer, String queue_name) throws Exception {
		CachingConnectionFactory factory = new CachingConnectionFactory();
		factory.setHost(ServerParameters.bunny_ip);
		Connection connection = factory.createConnection();
		Channel channel = connection.createChannel(true);

		channel.queueDeclare(queue_name, false, false, false, null);


		channel.basicPublish("", queue_name, null, jsonContainer.getBytes());
		System.out.println(" [x] Sent '" + jsonContainer + "'");

		channel.close();
		connection.close();
	}

	@Override
	public void run() {
		// Not used yet, need to evaluate if sending as a thread makes a lot of sense from a performance point of view

		Connection connection;
		try {
			connection = BrokerConnection.factory.createConnection();
			Channel channel = connection.createChannel(true);
			channel.queueDeclare(queue_name, false, false, false, null);
			message = "[" + "via controller" + "] " + message;
			channel.basicPublish("", queue_name, null, message.getBytes());
			System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
			channel.close();
			connection.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
