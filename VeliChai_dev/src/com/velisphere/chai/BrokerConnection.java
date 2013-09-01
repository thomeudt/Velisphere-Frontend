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









import com.rabbitmq.client.Channel;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
// import com.rabbitmq.client.Connection;
// import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class BrokerConnection {

	// this class is used to provide connectivity to the AMQP broker
	// it is a utility class
	// needs a lot of cleanup!

	
//	
	
	public static CachingConnectionFactory factory;
	public static Channel channel;
	public static AmqpTemplate veliTemplate;
	static Connection rxConnection;
	static Connection txConnection;

	
	
	
	
	public static void establishConnection(){
		factory = new CachingConnectionFactory();
		factory.setHost(ServerParameters.bunny_ip);

		factory.setUsername("guest");
		factory.setPassword("guest");
				
		veliTemplate = new RabbitTemplate(factory);
		
		txConnection = factory.createConnection();
		rxConnection = factory.createConnection();

	}

	public Channel establishRxChannel() throws IOException {

		return channel = rxConnection.createChannel(true);

	}

	public Channel establishTxChannel() throws IOException {

		return channel = txConnection.createChannel(true);

	}



	public static void closeFactory(){

		txConnection.close();
		rxConnection.close();
	}



}
