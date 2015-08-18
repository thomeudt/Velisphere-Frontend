package com.velisphere.toucan.webservices;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import com.velisphere.fs.sdk.MessageFabrik;
import com.velisphere.toucan.amqp.AMQPServer;
import com.velisphere.toucan.amqp.ServerParameters;
import com.velisphere.toucan.xmlRootElements.MessageElement;


@Path("/message")
public class Message {




 

	

	@POST
	@Path( "/post/json" )
	@Consumes( MediaType.TEXT_PLAIN )
	public Response postJSON( String message ) throws Exception {

	
	System.out.println( "Message is " + message );


	AMQPServer.sendJSON(message, ServerParameters.my_queue_name, "REG");
	return Response.noContent().build();

	}

	
	
	


	@GET
	@Path("/get/json/{endpointid}")
	@Produces({ MediaType.APPLICATION_JSON })
	public LinkedList<String> getJSON(@PathParam("endpointid") String endpointID) {
		MessageElement todo = new MessageElement();
		todo.setSummary("VeliSphere Web Service Version 0.1");
		// todo.setDescription("First Sphere");

		// AMQP handling from here

		
		System.out.println("EndpointID is "+ endpointID);
		
		String submittableJSON = null;
	
		LinkedList<String> messageList = new LinkedList<String>();
		
		

		String QUEUE_NAME = endpointID;

		
		try {

			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(ServerParameters.rabbit_ip);
			factory.setUsername("dummy");
			factory.setPassword("dummy");
			factory.setVirtualHost("hClients");
			factory.setPort(5671);
			factory.useSslProtocol();

			Connection connection = null;

			try {
				connection = factory.newConnection();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Channel channel = connection.createChannel();

			channel.queueDeclare(QUEUE_NAME, false, false, false, null);

			System.out.println(" [IN] Server Startup Completed.");
			System.out
					.println(" [IN] Waiting for messages. To exit press CTRL+C");

			QueueingConsumer consumer = new QueueingConsumer(channel);
			
			
			boolean empty = false;
			
			while (empty==false)
			{
				GetResponse response = channel.basicGet(QUEUE_NAME, true);
				if(response == null)
				{
					empty = true;
				}
				else
				{
					byte[] messageBody = response.getBody();
					messageList.add(new String(messageBody));		
				}
				
				
			}
			
						
			
			/*
			MessageFabrik messageFactory = new MessageFabrik(
					messageList);
			 submittableJSON = messageFactory.getJsonString();
*/
		}

		catch (IOException | ShutdownSignalException
				| ConsumerCancelledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return messageList;
	}

	// This can be used to test the integration with the browser

	@GET
	@Path("/text")
	@Produces({ MediaType.TEXT_XML })
	public MessageElement getHTML() {
		MessageElement todo = new MessageElement();
		todo.setSummary("VeliSphere Web Service Version 0.1");
		// todo.setDescription("First Sphere");
		return todo;
	}
	
	
	@PUT
	@Path( "/put/text/{user}" )
	@Consumes( MediaType.TEXT_PLAIN )
	public Response postPlainTextMessage( @PathParam( "content" ) String user, String message ) throws Exception {

	
	System.out.printf( "%s sendet �%s�%n", user, message );
	HashMap<String,String>outboundMessageMap = new HashMap<String, String>();
	outboundMessageMap.put("A", message);
	String targetEPID = new String("EX");

	AMQPServer.sendHashTable(outboundMessageMap, targetEPID, "REG");
	return Response.noContent().build();

	}
	

}