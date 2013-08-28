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
				ImdbLog.writeLog("null", message, "controller", "null");
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
