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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.AMQP.BasicProperties;

public class AMQPUnpack implements Runnable {


	QueueingConsumer.Delivery delivery;
	AMQPUnpack(QueueingConsumer.Delivery d) { delivery = d; }


	ExecutorService inspector = Executors.newFixedThreadPool(ServerParameters.threadpoolSize); // create thread pool for message inspection


	public void run() {


		String message = new String(delivery.getBody());
		BasicProperties props = delivery.getProperties(); // not yet used

		/*
		 *  Here the inspection of the message is being triggered.
		 *  Inspect only messages of type REG
		 */


		String messagetype;
		try {
			messagetype = MessagePack.extractProperty(message, "TYPE");
			if(messagetype.equals("REG")) {

				Thread inspectionThread;
				inspectionThread = new Thread(new messageInspect(message), "inspector");
				inspector.execute(inspectionThread);
				Imdb.writeLog("null", message, ServerParameters.controllerQueueName, "null");
				
				
			}


		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		inspector.shutdown(); // close thread pool for message inspection
		return; // close the current thread
	}






}
