package chai;
import java.io.IOException;

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
    		
    

	try {
    ConnectionFactory factory = new ConnectionFactory();
    
    
    factory.setHost(ServerParameters.bunny_ip);
    
    Connection connection;
		connection = factory.newConnection();
	
    Channel channel = connection.createChannel();

    // we do some simple QoS here to avoid overloading a single controller
    // if previous task has not been completed, it will be moved directly to the next controller available
    
    int prefetchCount = 1;
    channel.basicQos(prefetchCount);
    
    // declare the controller queue and make it durable, so no queues are lost
    boolean durable = true;
    channel.queueDeclare(ServerParameters.controllerQueueName, durable, false, false, null);
    
    System.out.println(" [*] Waiting for messages on queue " + ServerParameters.controllerQueueName + ". To exit press CTRL+C");
    
    QueueingConsumer consumer = new QueueingConsumer(channel);

    
    // The controller always needs to acknowledge a task completed
    boolean autoAck = false; 
    channel.basicConsume(ServerParameters.controllerQueueName, autoAck, consumer);
	
   while (!Thread.currentThread().isInterrupted()){
    QueueingConsumer.Delivery delivery = consumer.nextDelivery();
    String message = new String(delivery.getBody());
    BasicProperties props = delivery.getProperties();
    
    
    System.out.println(" [x] Received from "+ props.getReplyTo() + " the message: '" + message + "'");
    
    /*
     *  Here the inspection of the message is being triggered.
     */
    
    messageInspect mI = new messageInspect();
    mI.inspectAMQP(message);
    
    ImdbLog logger = new ImdbLog();
    logger.writeLog("", message, "", "");
    // Here we acknowledge completion of the task
    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
    
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
    
}
