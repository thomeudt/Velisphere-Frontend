package com.velisphere.toucan.webservices;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;







import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.GetResponse;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
import com.velisphere.toucan.amqp.VoltConnector;
import com.velisphere.toucan.volt.PasswordChecker;
import com.velisphere.toucan.volt.UserData;



@Path("/authentication")
public class Authenticator {

	
	@PUT
	@Path( "/put/user/{username}" )
	@Consumes( MediaType.TEXT_PLAIN )
	public Response postPlainTextMessage( 
			@PathParam( "username" ) String username,
			String password ) 
					throws Exception {

	PasswordChecker checker = new PasswordChecker();
	
	UserData user = checker.loginServer(username, password);
	
		
	System.out.println("User ID " + user.userID + " validated for User Name: " + username);
		
	return Response.ok(user.userID).build();
	//return Response.noContent().build();

	}
	

}