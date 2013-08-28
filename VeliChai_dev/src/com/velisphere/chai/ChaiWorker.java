package com.velisphere.chai;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ChaiWorker {

	static ObjectMapper mapper = new ObjectMapper(); // object mapper for jackson json parser - create once, reuse for performance
	public static JsonFactory factory = mapper.getFactory();


	public static void main(String[] args) throws IOException {

		/*
		 * Show startup message
		 */

		System.out.println();
		System.out.println("*     * VeliChai v0.0.2 - VeliSphere Controller");
		System.out.println(" *   *  Copyright (C) 2013 Thorsten Meudt. All rights reserved.");
		System.out.println("  * *   ");
		System.out.println("   *    VeliChai is part of the VeliSphere IoTS ecosystem.");
		System.out.println();



		/*
		 * Load Config File and set config variables
		 */


		ConfigHandler cfh = new ConfigHandler();
		cfh.loadParamChangesAsXML();

		/*
		 * 
		 * Creating the connection to Rabbit
		 * 
		 */

		BrokerConnection.establishConnection();

		// Open the IMDB Logger Database

		ImdbLog.openDatabase();

		/*
		 * Start the listening service
		 */
		// {


		// open as many listener threads as defined in threadpool size in config file.
		
		int numworkers = ServerParameters.threadpoolSize; 

		ExecutorService receiver = Executors.newFixedThreadPool(ServerParameters.threadpoolSize);
		
		Recv[] receiverThread = new Recv[numworkers];
		for (int i = 0; i < numworkers; i++) {
			receiverThread[i] = new Recv(i);
			receiver.execute(receiverThread[i]);
            
        }
		
	}	
}
