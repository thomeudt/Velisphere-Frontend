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
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.velisphere.chai.broker.BrokerConnection;
import com.velisphere.chai.engines.BusinessLogicEngine;
import com.velisphere.chai.messageUtils.AMQPUnpack;

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
		System.out.println("    * *    VeliSphere Secure Worker Node (chai) v0.8.5b");
		System.out.println("    * * *  Copyright (C) 2015 Thorsten Meudt/Connected Things Lab. All rights reserved.");
		System.out.println("**   *    ");
		System.out.println("  * *   ");
		System.out.println("   *       VeliSphere Worker Node is part of the VeliSphere IoTS ecosystem.");
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

		try {
			BrokerConnection.establishConnection();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(" [OK] Connection to AMQP Broker successful.");
		
		// Open the IMDB Database

		//System.out.println(" [IN] Selected VoltDB: " + ServerParameters.volt_ip);
		BusinessLogicEngine.openDatabase();
		VelisphereMart.connect();

		System.out.println(" [IN] Connecting completed.");
		System.out.println(" [IN] Waiting for messages on queue: "
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
