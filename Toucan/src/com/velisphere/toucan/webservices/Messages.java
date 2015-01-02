package com.velisphere.toucan.webservices;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
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
import com.velisphere.toucan.ConfigHandler;
import com.velisphere.toucan.Todo;
import com.velisphere.toucan.amqp.MessagePack;
import com.velisphere.toucan.amqp.Send;
import com.velisphere.toucan.amqp.ServerParameters;


@Path("/messages")
public class Messages {

	@GET
	@Path("/get/xml/{param}")
	@Produces({ MediaType.APPLICATION_XML })
	public Todo getXML(@PathParam("param") String endpointID) {
		Todo todo = new Todo();
		todo.setSummary("VeliSphere Web Service Version 0.1");
		// todo.setDescription("First Sphere");

		// AMQP handling from here

		ConfigHandler conf = new ConfigHandler();
		conf.loadParamChangesAsXML();

		String QUEUE_NAME = endpointID;

		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(ServerParameters.bunny_ip);
			Connection connection;
			connection = factory.newConnection();

			Channel channel = connection.createChannel();

			// channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			System.out
					.println(" [*] Waiting for messages. To exit press CTRL+C");

			GetResponse response = channel.basicGet(QUEUE_NAME, true);

			LinkedList<String> test = new LinkedList<String>();

			while (response != null) {
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

		} catch (IOException | ShutdownSignalException
				| ConsumerCancelledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return todo;
	}

	@GET
	@Path("/get/json/{param}")
	@Produces({ MediaType.TEXT_PLAIN })
	public String getJSON(@PathParam("param") String endpointID) {
		Todo todo = new Todo();
		todo.setSummary("VeliSphere Web Service Version 0.1");
		// todo.setDescription("First Sphere");

		// AMQP handling from here

		ConfigHandler conf = new ConfigHandler();
		conf.loadParamChangesAsXML();

		String QUEUE_NAME = endpointID;

		String test = new String();

		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(ServerParameters.bunny_ip);
			factory.setUsername("guest");
			factory.setPassword("guest");
			
			Connection connection;
			connection = factory.newConnection();

			
			Channel channel = connection.createChannel();

			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			System.out
					.println(" [*] Waiting for messages. To exit press CTRL+C");

			//String queueName = channel.queueDeclare();

			channel.queueBind(QUEUE_NAME, "xClients", "EX");
			
			
			GetResponse response = channel.basicGet(QUEUE_NAME, true);

			while (response != null) {
				AMQP.BasicProperties props = response.getProps();
				byte[] body = response.getBody();
				String message = new String(body);
				System.out.println("RECEIVED: " + message);
				test = test + "," + message;
				// test.add(MessagePack.extractProperty(message, "PR9"));
				long deliveryTag = response.getEnvelope().getDeliveryTag();
				response = channel.basicGet(QUEUE_NAME, true);
			}

			if (test.length() > 0)
				test = "[" + test.substring(1) + "]";
			// todo.setDescription(test);

		}

		catch (IOException | ShutdownSignalException
				| ConsumerCancelledException e) {
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
		// todo.setDescription("First Sphere");
		return todo;
	}
	
	
	@PUT
	@Path( "/put/text/{user}" )
	@Consumes( MediaType.TEXT_PLAIN )
	public Response postPlainTextMessage( @PathParam( "content" ) String user, String message ) throws Exception {

	
	System.out.printf( "%s sendet ‘%s’%n", user, message );
	HashMap<String,String>outboundMessageMap = new HashMap<String, String>();
	outboundMessageMap.put("A", message);
	String targetEPID = new String("EX");

	Send.sendHashTable(outboundMessageMap, targetEPID, "test");
	return Response.noContent().build();

	}
	

}