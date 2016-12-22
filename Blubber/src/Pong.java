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
import java.io.IOException;

import ChatExample.MainScreen;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class Pong implements Runnable {

	// Testing class - responds to sending queue with exactly the same message
	
	
    public  void run() {

    String QUEUE_NAME = ServerParameters.my_queue_name;

	try {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(ServerParameters.bunny_ip);
    Connection connection;
		connection = factory.newConnection();
	
    Channel channel = connection.createChannel();

    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
     
    QueueingConsumer consumer = new QueueingConsumer(channel);
    channel.basicConsume(QUEUE_NAME, true, consumer);
	
   while (!Thread.currentThread().isInterrupted()){
    QueueingConsumer.Delivery delivery = consumer.nextDelivery();
    String message = new String(delivery.getBody());
    System.out.println(" [x] Received '" + message + "'");
    SendOld.main("PONG:" + message, "adam");
    
    boolean admFilterResult = filterMqttAdmMessage(message);   
    if(admFilterResult==false){
    MainScreen.updateHistory(message);
    }
    else
    {
    	MainScreen.updateHistory("FILTER");
    }
   }
   }catch(InterruptedException e){
       Thread.currentThread().interrupt();
       
   }
	 catch (IOException | ShutdownSignalException | ConsumerCancelledException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
  }
    
    
    public static boolean filterMqttAdmMessage(String message){
		
    	
    	return false;
		
	}
	
    
}
