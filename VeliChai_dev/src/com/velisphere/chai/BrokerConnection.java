package com.velisphere.chai;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class BrokerConnection {

	public static ConnectionFactory factory;
	public static Channel channel;
	static Connection rxConnection;
	static Connection txConnection;
	
	public static void establishConnection(){
		factory = new ConnectionFactory();
		factory.setHost(ServerParameters.bunny_ip);
	    
	    try {
	    	txConnection = factory.newConnection();
	    	rxConnection = factory.newConnection();
	    	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Channel establishRxChannel() throws IOException {
		
		
	    return channel = rxConnection.createChannel();
		
	}
	
	public Channel establishTxChannel() throws IOException {
		
		
	    return channel = rxConnection.createChannel();
		
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
