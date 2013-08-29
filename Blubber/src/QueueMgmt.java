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
import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


public class QueueMgmt {
    
	public static void deleteQueue(String queueName) throws IOException{
	
	ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(ServerParameters.bunny_ip);
    Connection connection;
		connection = factory.newConnection();
	
    Channel channel = connection.createChannel();

    channel.queueDelete(queueName);
    
    channel.close();
    connection.close();
    
    
    System.out.println(" [!] Queue deleted..."+queueName);
	}

	public static void bindQueueFanout(String queueName, String exchangeName) throws IOException{
		  
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(ServerParameters.bunny_ip);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		    
		
		channel.exchangeDeclare(exchangeName, "fanout");
	    channel.queueBind(queueName, exchangeName, "");
	    
	    channel.close();
	    connection.close();
	    
	
	
	}


}
