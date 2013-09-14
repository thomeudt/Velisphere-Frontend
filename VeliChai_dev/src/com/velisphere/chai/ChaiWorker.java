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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class ChaiWorker {

	public static ExecutorService receiver;

	static ObjectMapper mapper = new ObjectMapper(); // object mapper for
	// jackson json parser -
	// create once, reuse
	// for performance

	public static JsonFactory factory = mapper.getFactory();

	public static void main(String[] args) throws IOException {

		/*
		 * Show startup message
		 */

		System.out.println();
		System.out.println("*     * VeliChai v0.0.5 - VeliSphere Controller");
		System.out
				.println(" *   *  Copyright (C) 2013 Thorsten Meudt. All rights reserved.");
		System.out.println("  * *   ");
		System.out
				.println("   *    VeliChai is part of the VeliSphere IoTS ecosystem.");
		System.out.println();

		/*
		 * Load Config File and set config variables
		 */

		ConfigHandler cfh = new ConfigHandler();
		cfh.loadParamChangesAsXML();

		/*
		 * 
		 * Creating the connection to Rabbit
		 */

		BrokerConnection.establishConnection();

		// Open the IMDB Database

		System.out
				.println(" [IN] Selected VoltDB: " + ServerParameters.volt_ip);
		BusinessLogicEngine.openDatabase();

		System.out.println(" [OK] Connection Successful.");
		System.out.println(" [OK] Waiting for messages on queue: "
				+ ServerParameters.controllerQueueName
				+ ". To exit press CTRL+C");

		
		ExecutorService unpacker = Executors
				.newFixedThreadPool(ServerParameters.threadpoolSize);

		for(int i = 0; i<ServerParameters.threadpoolSize; i++){
				Thread unpackingThread;
				unpackingThread = new Thread(new AMQPUnpack(),
						"unpacker");
			    unpacker.execute(unpackingThread);
		}
		
		
	}
}
