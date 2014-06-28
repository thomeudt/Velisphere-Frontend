package com.velisphere.toucanTest;



import java.io.IOException;
import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.*;





public class client {


	public static void main(String[] args) {
		Client client = ClientBuilder.newClient();

		
		WebTarget target = client.target( "http://localhost:8080/Toucan/rest/messages/put/" );

		Response response = target.path( "text" ).path( "chris" ).request().put( Entity.text("Hey Chris") );
		
	
		System.out.println (response);

		
	}
}
