package com.velisphere.chai;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.ShutdownSignalException;

public class AMQPUnpack implements Runnable {


	QueueingConsumer.Delivery delivery;
	AMQPUnpack(QueueingConsumer.Delivery d) { delivery = d; }

	 
	 ExecutorService inspector = Executors.newFixedThreadPool(ServerParameters.threadpoolSize); // create thread pool for message inspection
	 	
	 
	public void run() {
		
		
			String message = new String(delivery.getBody());
		    BasicProperties props = delivery.getProperties();
		    
		    
		    // System.out.println(" [RX] Received from "+ props.getReplyTo() + " the message: '" + message + "'");
		    
		    /*
		     *  Here the inspection of the message is being triggered.
		     *  Inspect only messages of type REG
		     */
		    
		    // MessagePack mp = new MessagePack();
		    // String messagetype = mp.extractProperty(message, "TYPE"); changed to static for performance
		    
		    String messagetype;
			try {
				messagetype = MessagePack.extractProperty(message, "TYPE");
			    if(messagetype.equals("REG")) {
			    	
			        // messageInspect mI = new messageInspect(); made static

			        // messageInspect.inspectAMQP(message);
			    	
			    	Thread inspectionThread;
					
					inspectionThread = new Thread(new messageInspect(message), "inspector");
					
				 	inspector.execute(inspectionThread);
				 	
				 	
				 	ImdbLog.writeLog("null", message, "controller", "null");
				 
					
					// ChaiWorker.listener.execute(inspectionThread);
					
					// inspectionThread.start();
		    	
			
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
	    return;
	}
	
	

	
	
	
}
