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
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


public class BrokerConnection {

	// this class is used to provide connectivity to the AMQP broker
	// it is a utility class
	// needs a lot of cleanup!

	
	// CachingConnectionFactory connectionFactory = new CachingConnectionFactory("somehost");
	
	public static Channel channel;
	static Connection rxConnection;
	static Connection txConnection;

	public static void establishConnection(){
		
		ConnectionFactory txFactory;
		ConnectionFactory rxFactory;
		txFactory = new ConnectionFactory();
		txFactory.setHost(ServerParameters.bunny_ip);
		txFactory.setVirtualHost("hClients");
		txFactory.setUsername("dummy");
		txFactory.setPassword("dummy");

		rxFactory = new ConnectionFactory();
		rxFactory.setHost(ServerParameters.bunny_ip);
		rxFactory.setVirtualHost("hController");
		rxFactory.setUsername("dummy");
		rxFactory.setPassword("dummy");


		
		try {
			txConnection = txFactory.newConnection();
			rxConnection = rxFactory.newConnection();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Channel establishRxChannel() throws IOException {

		
		return channel = rxConnection.createChannel();

	}

	public Channel establishTxChannel() throws IOException {

		
		return channel = txConnection.createChannel();

	}



	public static void closeFactory(){

		try {
			txConnection.close();
			rxConnection.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



}
