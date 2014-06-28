package com.velisphere.toucan;
 

import java.io.IOException;
import java.util.LinkedList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.GetResponse;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
 
@Path("/messages")
public class Messages  {
 
	/**
	@GET
	@Path("/{param}")
	public Response getMsg(@PathParam("param") String msg) {
 
		String output = "Jersey says : " + msg;
 
		return Response.status(200).entity(output).build();
 
	} 
	**/
	
	@GET
	@Path("/xml/{param}")
	@Produces({ MediaType.APPLICATION_XML })
	public Todo getXML(@PathParam("param") String endpointID) {
	    Todo todo = new Todo();
	    todo.setSummary("VeliSphere Web Service Version 0.1");
	    //todo.setDescription("First Sphere");
	    
	    //AMQP handling from here
	    
	    ConfigHandler conf = new ConfigHandler();
	    conf.loadParamChangesAsXML();
	    
	    String QUEUE_NAME = endpointID;

		try {
	    ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost(ServerParameters.bunny_ip);
	    Connection connection;
			connection = factory.newConnection();
		
	    Channel channel = connection.createChannel();

	    //channel.queueDeclare(QUEUE_NAME, false, false, false, null);
	    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
		
	    GetResponse response = channel.basicGet(QUEUE_NAME, true);
	    
	    LinkedList<String> test = new LinkedList<String>();
	    
	    while (response != null){
	        AMQP.BasicProperties props = response.getProps();
	        byte[] body = response.getBody();
	        String message = new String(body);
	        System.out.println("RECEIVED: " + message);
	        test.add(message);
	        test.add(MessagePack.extractProperty(message, "PR9"));
	        long deliveryTag = response.getEnvelope().getDeliveryTag();
	        response = channel.basicGet(QUEUE_NAME, true);
	    }
	    
	    todo.setDescription(test);
	    
		}
	     
	    
	    
	    /**
	    QueueingConsumer consumer = new QueueingConsumer(channel);
	    channel.basicConsume(QUEUE_NAME, true, consumer);
	    
	    
	    RabbitAdmin   getQueueProperties(queue.getName());
        messageCount = Integer.parseInt(props.get("QUEUE_MESSAGE_COUNT").toString());
        System.out.println(queue.getName() + " has " + messageCount + " messages");
		
	   while (!Thread.currentThread().isInterrupted()){
	    QueueingConsumer.Delivery delivery = consumer.nextDelivery();
	    String message = new String(delivery.getBody());
	    System.out.println(" [x] Received '" + message + "'");
	  
	   
	   }
	   
	   
	   }catch(InterruptedException e){
	       Thread.currentThread().interrupt();
	       
	   }
	   **/
		 catch (IOException | ShutdownSignalException | ConsumerCancelledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 	 return todo;
	  }

	
	@GET
	@Path("/json/{param}")
	@Produces({ MediaType.TEXT_PLAIN })
	public String getJSON(@PathParam("param") String endpointID) {
	    Todo todo = new Todo();
	    todo.setSummary("VeliSphere Web Service Version 0.1");
	    //todo.setDescription("First Sphere");
	    
	    //AMQP handling from here
	    
	    ConfigHandler conf = new ConfigHandler();
	    conf.loadParamChangesAsXML();
	    
	    String QUEUE_NAME = endpointID;

	    String test = new String();
	    
		try {
	    ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost(ServerParameters.bunny_ip);
	    Connection connection;
			connection = factory.newConnection();
		
	    Channel channel = connection.createChannel();

	    //channel.queueDeclare(QUEUE_NAME, false, false, false, null);
	    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
		
	    GetResponse response = channel.basicGet(QUEUE_NAME, true);
	    
	    
	    
	    while (response != null){
	        AMQP.BasicProperties props = response.getProps();
	        byte[] body = response.getBody();
	        String message = new String(body);
	        System.out.println("RECEIVED: " + message);
	        test = test+","+message;
	        //test.add(MessagePack.extractProperty(message, "PR9"));
	        long deliveryTag = response.getEnvelope().getDeliveryTag();
	        response = channel.basicGet(QUEUE_NAME, true);
	    }
	    
	    
	    if (test.length() > 0) test = "[" + test.substring(1) +"]";
	    //todo.setDescription(test);
	    
		}
	     
	    
	    		 catch (IOException | ShutdownSignalException | ConsumerCancelledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 	 return test;
	  }

	
	
	 // This can be used to test the integration with the browser
	
	@GET
	@Path("/text")
	  @Produces({ MediaType.TEXT_XML })
	  public Todo getHTML() {
	    Todo todo = new Todo();
	    todo.setSummary("VeliSphere Web Service Version 0.1");
	    //todo.setDescription("First Sphere");
	    return todo;
	  }
	 

	 
 
}