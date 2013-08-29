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



import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;



public class Broadcast {

	public static void broadcastToExchange(String message, String exchange_name) throws Exception {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(ServerParameters.bunny_ip);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(exchange_name, "fanout");
		channel.basicPublish(exchange_name, "", null, message.getBytes());
		System.out.println(" [x] Sent '" + message + "'");
		channel.close();
		connection.close();
	}


}
