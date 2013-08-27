package com.velisphere.chai;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class Recv implements Runnable {

	
	/**
	 *  This class contains routines that are triggered when the
	 *  worker is started
	 */
	
	
	
    public  void run() {


    	/**
    	 *  This is the listener method. It constantly monitors the controller queue for new messages
    	 *  and sends them for inspection or triggers certain administrative actions.
    	 *  
    	 *  
    	 */
    		
    ExecutorService unpacker = Executors.newFixedThreadPool(ServerParameters.threadpoolSize); // create thread pool for message unpacking
    
    	

	try {
    
	BrokerConnection bc = new BrokerConnection();
	Channel channel = bc.establishRxChannel();
		

    // we do some simple QoS here to avoid overloading a single controller
    // if previous task has not been completed, it will be moved directly to the next controller available
    
    int prefetchCount = 10;
    channel.basicQos(prefetchCount);
    
    // declare the controller queue and make it durable, so no queues are lost
    // boolean durable = true;
    // channel.queueDeclare(ServerParameters.controllerQueueName, durable, false, false, null);
    
    System.out.println(" [OK] Connection Successful.");
    System.out.println(" [OK] Waiting for messages on queue: " + ServerParameters.controllerQueueName + ". To exit press CTRL+C");
    
    QueueingConsumer consumer = new QueueingConsumer(channel);

    
    // The controller always needs to acknowledge a task completed
   
    
    // boolean autoAck = false;
    
    // temporarily changed to see if prefetch improves perf
    boolean autoAck = true;
    channel.basicConsume(ServerParameters.controllerQueueName, autoAck, consumer);
	
    while (!Thread.currentThread().isInterrupted()){
    
    	
    	QueueingConsumer.Delivery delivery;
	try{
    	delivery = consumer.nextDelivery();
    	Thread unpackingThread;
		
		unpackingThread = new Thread(new AMQPUnpack(delivery), "unpacker");
		
	 	unpacker.execute(unpackingThread);
	 	
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
	
	return;
  }
    
    
}
