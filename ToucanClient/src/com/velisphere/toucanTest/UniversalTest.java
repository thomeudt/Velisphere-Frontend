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

public class UniversalTest {
	public static void main(String[] args) {
		InetAddress ip;
		
		Client client = ClientBuilder.newClient();

		// get user name
		
		String userid = "b9816c0a-1c2c-42da-9bc7-d9dfc51baafc";
		WebTarget target = client.target( "http://localhost:8080/ToucanServer/rest/users/get" );
		Response response = target.path( "user" ).path("name").path(userid).request().get();
		System.out.println ("Reponse from server: " + response);
		System.out.println ("Returned user id: " + response.readEntity(String.class));
		
		// get endpoints json
		
		target = client.target( "http://localhost:8080/ToucanServer/rest/users/get" );
		response = target.path( "user" ).path("endpoints").path(userid).request().get();
		System.out.println ("Reponse from server: " + response);
		System.out.println ("Returned json: " + response.readEntity(String.class));

		// get spheres json
		
		target = client.target( "http://localhost:8080/ToucanServer/rest/users/get" );
		response = target.path( "user" ).path("spheres").path(userid).request().get();
		System.out.println ("Reponse from server: " + response);
		System.out.println ("Returned json: " + response.readEntity(String.class));
		
	}
}
