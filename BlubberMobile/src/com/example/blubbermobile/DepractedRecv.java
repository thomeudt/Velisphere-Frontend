package com.example.blubbermobile;
import java.io.IOException;

import android.widget.EditText;
import android.app.Activity;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;



public class DepractedRecv implements Runnable {

	
	
    public  void run() {


    //String QUEUE_NAME = ServerParameters.my_queue_name;

    	String QUEUE_NAME = "";
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
    
    boolean admFilterResult = filterMqttAdmMessage(message);   
    if(admFilterResult==false){

    	//
    	MainActivity a = new MainActivity();
    	
    	//a.updateHistory(message);
    	
    }
    else
    {
    	//MainScreen.updateHistory("FILTER");
    }
   }
   }catch(InterruptedException e){
       Thread.currentThread().interrupt();
       
   }
	 catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
  }
    
    
    

	public static boolean filterMqttAdmMessage(String message){
		
    	
    	return false;
		
	}
	
    
}
