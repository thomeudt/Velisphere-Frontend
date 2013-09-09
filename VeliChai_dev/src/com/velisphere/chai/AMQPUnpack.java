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

public class AMQPUnpack implements Runnable{


	
	
	
	QueueingConsumer.Delivery delivery;
	AMQPUnpack(QueueingConsumer.Delivery d) { delivery = d; }


	ExecutorService inspector = Executors.newFixedThreadPool((ServerParameters.threadpoolSize)); // create thread pool for message inspection
	// ExecutorService inspector = Executors.newFixedThreadPool(1); // create thread pool for message inspection

	public void run() {
		
		BrokerConnection bc = new BrokerConnection();
		Channel channel = null;
		try {
			channel = bc.establishRxChannel();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int prefetchCount = 1;
		try {
			channel.basicQos(prefetchCount);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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

		ExecutorService unpacker = Executors
				.newFixedThreadPool(ServerParameters.threadpoolSize/16);

		while (true) {
			try {
				delivery = consumer.nextDelivery();
				Thread unpackingThread;
				unpackingThread = new Thread(new AMQPUnpack(delivery),
						"unpacker");
				 unpacker.execute(unpackingThread);
				
				// AMQPUnpack unPacker = new AMQPUnpack(delivery);
				// unPacker.run();
				
				channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false); 
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
						inspectionThread = new Thread(new MessageInspect(message), "inspector");
						inspector.execute(inspectionThread);
						//ChaiWorker.receiver.execute(inspectionThread);
						// MessageInspect mI = new MessageInspect(message);
						// mI.run();
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

			
			} catch (ShutdownSignalException | ConsumerCancelledException
					| InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				channel.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return; // close the current thread
		}

	}






}
