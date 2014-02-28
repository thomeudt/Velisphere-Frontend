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
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.client.AMQP.BasicProperties;

public class AMQPUnpack implements Runnable {



	


	public void run() {

		BrokerConnection bc = new BrokerConnection();
		Channel channel;
		
		try {
			channel = bc.establishRxChannel();
		


			// Prefetch of 100 has proven to be a good choice for performance
			// reasons, but this needs further evaluation

			int prefetchCount = 1;
			try {
				channel.basicQos(prefetchCount);
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			QueueingConsumer consumer = new QueueingConsumer(channel);

			// Message acknowledgment is needed to make sure no messages are lost

			boolean autoAck = false;
			try {
				channel.basicConsume(ServerParameters.controllerQueueName, autoAck,
							consumer);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
			/*
			 * Start the listening service
			 */

			QueueingConsumer.Delivery delivery;


			while (true) {
				try {
					delivery = consumer.nextDelivery();

					String message = new String(delivery.getBody());
					BasicProperties props = delivery.getProperties(); // not yet used

					/*
					 *  Here the inspection of the message is being triggered.
					 *  Inspect only messages of type REG
					 */


					ExecutorService inspector = Executors.newFixedThreadPool(ServerParameters.threadpoolSize/4); // create thread pool for message inspection
					String messagetype;
					messagetype = MessagePack.extractProperty(message, "TYPE");
					if(messagetype.equals("REG")) {
						Thread inspectionThread;
						inspectionThread = new Thread(new MessageInspect(message), "inspector");
						inspector.execute(inspectionThread);
						//BusinessLogicEngine.writeLog("null", message, ServerParameters.controllerQueueName, "null");
						channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
					 
					}
					inspector.shutdown(); // close thread pool for message inspection
					} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

					}
			
		} catch (IOException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();

			return; // close the current thread
		}
	}
}