package com.velisphere.toucan.webservices;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.soap.MessageFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
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

	@SuppressWarnings("unchecked")
	@POST
	@Path("/post/pushjson/{endpointid}")
	@Consumes(MediaType.TEXT_PLAIN)
	public Response postJSON(@PathParam("endpointid") String endpointid,
			String message) throws Exception {

		// Receive incoming message and create JACKSON Object Mapper
		System.out.println("Parsing " + message);
		ObjectMapper mapper = new ObjectMapper();
	
		// This map will take from JSON the password for RabbitMQ and the enclosed message
		Map<String, Object> passwordAndMessageObject = new HashMap<String, Object>();

		// Parse XML and put into Map
		passwordAndMessageObject = mapper.readValue(message, Map.class);

		// Get entry - as per definition, we never receive more than one entry, so we do not need to iterate
		Map.Entry<String, Object> entry = passwordAndMessageObject.entrySet()
				.iterator().next();
		Map<String, String> messageMap = new HashMap<String, String>();
		
		// Get the value from the entry - this is the messageMap that will be sent to the controller via RabbitMQ
		messageMap = (Map<String, String>) entry.getValue();
		
		// Get the key from the entry - this is the password we will use to log on to RabbitMQ
		String password = entry.getKey();
		MessageFabrik messageFabrik = new MessageFabrik(messageMap);

		AMQPServer.sendJSON(endpointid, password,
				messageFabrik.getJsonString(), ServerParameters.my_queue_name,
				"REG");
		return Response.noContent().build();

	}

	@POST
	@Path("/post/requestjson/{endpointid}")
	@Produces({ MediaType.APPLICATION_JSON })
	public LinkedList<String> getJSON(@PathParam("endpointid") String endpointID, String password) {
		
		// TODO implement password authentication exactly as in the POST method!!!
		

		// AMQP handling from here

		System.out.println("EndpointID is " + endpointID);

		//String submittableJSON = null;

		LinkedList<String> messageList = new LinkedList<String>();

		String QUEUE_NAME = endpointID;

		try {

			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(ServerParameters.rabbit_ip);
			factory.setUsername(endpointID);
			factory.setPassword(password);
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

			System.out.println(" [IN] AMQP connection to RabbitMQ established");
			System.out.println(" [IN] Requesting messages from Queue (GET)...");
			
			//QueueingConsumer consumer = new QueueingConsumer(channel);

			boolean empty = false;

			while (empty == false) {
				GetResponse response = channel.basicGet(QUEUE_NAME, true);
				if (response == null) {
					empty = true;
				} else {
					byte[] messageBody = response.getBody();
					messageList.add(new String(messageBody));
					System.out.println("Getting: " + new String(messageBody));
				}

			}

			System.out.println(" [IN] Getting messages completed!");
			
			
			/*
			 * MessageFabrik messageFactory = new MessageFabrik( messageList);
			 * submittableJSON = messageFactory.getJsonString();
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


}