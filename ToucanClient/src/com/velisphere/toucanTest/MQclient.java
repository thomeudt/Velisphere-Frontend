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
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;





public class MQclient {


	public static void main(String[] args) {
		Client client = ClientBuilder.newClient();

		
		WebTarget target = client.target( "http://localhost:8080/Toucan/rest/messages/put/" );

		Response response = target.path( "text" ).path( "chris" ).request().put( Entity.text("Hey Chris") );
		
	
		System.out.println (response);


		Client client2 = ClientBuilder.newClient();
		HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("veliadmin2014", "4GfQ2xgIwVsJ9g3wZIQE");
		client2.register(feature);
		WebTarget target2 = client2.target( "http://h2209363.stratoserver.net:15672/api/users/jil" );
		Response response2 = target2.request().put( Entity.json("{\"password\":\"secret\",\"tags\":\"administrator\"}") );
		
	
		System.out.println (response2);

		WebTarget target3 = client2.target( "http://h2209363.stratoserver.net:15672/api/permissions/hClients/jil" );
		Response response3 = target3.request().put( Entity.json("{\"configure\":\".*\",\"write\":\".*\",\"read\":\"EA\"}") );
		
		
		System.out.println (response3);
	
		
		
	}
}
