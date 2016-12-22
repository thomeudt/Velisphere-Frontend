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

public class PingRequestClient {
	public static void main(String[] args) {
		InetAddress ip;
		
		Client client = ClientBuilder.newClient();

		String endpointID = "b0954534-4396-4d49-912c-8b6b99f6743c";
		
			
			WebTarget target = client.target( "http://localhost:8080/ToucanServer/rest/endpoint/put/" );

			//Response response = target.path( "endpoint" ).path( sb.toString() ).request().put( Entity.text("f67528e4-80f7-4832-a5fd-3082bd4e7385") );
			
			
			
			Response response = target.path( "isalive" ).request().put( Entity.text(endpointID) );
			System.out.println("Pinging: " + endpointID);	
		
			System.out.println ("Reponse from server: " + response);
			System.out.println ("Returned user id: " + response.readEntity(String.class));
			


		
	}
}
