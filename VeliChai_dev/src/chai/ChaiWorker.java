package chai;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class ChaiWorker {
	
	
	public static void main(String[] args) {
		
		/*
		 * Show startup message
		 */
		
		System.out.println();
		System.out.println("*     * VeliChai v0.0.1 - VeliSphere Controller");
		System.out.println(" *   *  Copyright (C) 2013 Thorsten Meudt. All rights reserved.");
		System.out.println("  * *   ");
		System.out.println("   *    VeliChai is part of the VeliSphere IoTS ecosystem.");
		System.out.println();
		
		
		
		/**
		 * Load Config File and set config variables
		 */
		
				
		ConfigHandler cfh = new ConfigHandler();
		cfh.loadParamChangesAsXML();
	
		
		/*
		 * Start the listening service
		 */
		
		
		Thread listenerThread;
		listenerThread = new Thread(new Recv(), "listener");
		listenerThread.start();
	
}	
}
