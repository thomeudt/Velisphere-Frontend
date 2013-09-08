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
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class Recv implements Runnable {

	private int workerNumber;

    Recv(int number) {
        workerNumber = number;
    }

	
	/*
	 *  This class contains routines that are triggered when the
	 *  worker is started
	 */

	public  void run() {
		  
		/*
		 *  This is the listener method. It constantly monitors the controller queue for new messages
		 *  and sends them for inspection or triggers certain administrative actions.
		 *  
		 *  
		 */

		ExecutorService unpacker = Executors.newFixedThreadPool(ServerParameters.threadpoolSize*32); // create thread pool for message unpacking
		//ExecutorService unpacker = Executors.newFixedThreadPool(1); // create thread pool for message unpacking
		
		try {

			BrokerConnection bc = new BrokerConnection();
			Channel channel = bc.establishRxChannel();

			// Prefetch of 100 has proven to be a good choice for performance reasons, but this needs further evaluation


			int prefetchCount = 100;
			channel.basicQos(prefetchCount);

			System.out.println(" [OK] Connection Successful.");
			System.out.println(" [OK] Waiting for messages on queue: " + ServerParameters.controllerQueueName + ". To exit press CTRL+C");

			QueueingConsumer consumer = new QueueingConsumer(channel);

			// Message acknowledgment is needed to make sure no messages are lost
			
			boolean autoAck = false;
			channel.basicConsume(ServerParameters.controllerQueueName, autoAck, consumer);

			while (!Thread.currentThread().isInterrupted()){

				QueueingConsumer.Delivery delivery;
				try{
					delivery = consumer.nextDelivery();
					Thread unpackingThread;
					unpackingThread = new Thread(new AMQPUnpack(delivery), "unpacker");
					// unpacker.execute(unpackingThread);
					ChaiWorker.receiver.execute(unpackingThread);
					channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false); // here we ack receipt of the message
					
				} catch (ShutdownSignalException | ConsumerCancelledException
						| InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			channel.close(); // close channel

			unpacker.shutdown(); // close thread pool for message unpacking
		}
		catch (IOException | ShutdownSignalException | ConsumerCancelledException e) {
			// TODO Auto-generated catch block
			System.out.println("*** Connection error. Conntection to Broker could not be established.");
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block

			// e.printStackTrace();
		}
		return; // close current thread
	}

}
