package com.velisphere.toucanTest;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class AuthenticationClient {
	public static void main(String[] args) {
		InetAddress ip;
		
		Client client = ClientBuilder.newClient();

		String username = "a";
		String password = "a";
			
			WebTarget target = client.target( "http://16.1.1.82/Toucan/rest/authentication/put" );

			//Response response = target.path( "endpoint" ).path( sb.toString() ).request().put( Entity.text("f67528e4-80f7-4832-a5fd-3082bd4e7385") );
			
			
			
			Response response = target.path( "user" ).path(username).request().put( Entity.text(password) );
			System.out.println("User tried to log in: " + username);	
		
			System.out.println ("Reponse from server: " + response);
			System.out.println ("Returned user id: " + response.readEntity(String.class));
			


		
	}
}
