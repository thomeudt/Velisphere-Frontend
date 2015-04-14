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
		
		String userid = "3f82b42b-7d77-4e97-8e92-becdc1bf797d";
		String endpointID = "E3";
		String endpointClassID = "EPC1";
		String propertyClassID = "PC1";
		String vendorID = "65eb271e-d6c3-470a-8d62-6e40b9f930e7";
		String propertyID = "PR1";
		
				
				
		WebTarget target = client.target( "http://localhost:8080/ToucanServer/rest/user/get" );
		Response response = target.path("name").path(userid).request().get();
		System.out.println ("Reponse from server: " + response);
		System.out.println ("Returned user id: " + response.readEntity(String.class));
		
		// get endpoints json
		
		target = client.target( "http://localhost:8080/ToucanServer/rest/user/get" );
		response = target.path("endpoints").path(userid).request().get();
		System.out.println ("Reponse from server: " + response);
		System.out.println ("Returned json: " + response.readEntity(String.class));

		// get spheres json
		
		target = client.target( "http://localhost:8080/ToucanServer/rest/user/get" );
		response = target.path("spheres").path(userid).request().get();
		System.out.println ("Reponse from server: " + response);
		System.out.println ("Returned json: " + response.readEntity(String.class));
		
		// get single endpoint json
		
		target = client.target( "http://localhost:8080/ToucanServer/rest/endpoint/get" );
		response = target.path("general").path(endpointID).request().get();
		System.out.println ("Reponse from server: " + response);
		System.out.println ("Returned json: " + response.readEntity(String.class));
		
		// get single analytics / last log entry
		
		target = client.target( "http://localhost:8080/ToucanServer/rest/analytics/get" );
		response = target.path("lastendpointlogtime").path(endpointID).request().get();
		System.out.println ("Reponse from server: " + response);
		System.out.println ("Returned json: " + response.readEntity(String.class));
		
		// get endpoint class details
		
		target = client.target( "http://localhost:8080/ToucanServer/rest/endpointclass/get" );
		response = target.path("general").path(endpointClassID).request().get();
		System.out.println ("Reponse from server: " + response);
		System.out.println ("Returned json: " + response.readEntity(String.class));
				
		// get property class details
		
		target = client.target( "http://localhost:8080/ToucanServer/rest/propertyclass/get" );
		response = target.path("general").path(propertyClassID).request().get();
		System.out.println ("Reponse from server: " + response);
		System.out.println ("Returned json: " + response.readEntity(String.class));

		
		// get property class details

		target = client.target( "http://localhost:8080/ToucanServer/rest/vendor/get" );
		response = target.path("general").path(vendorID).request().get();
		System.out.println ("Reponse from server: " + response);
		System.out.println ("Returned json: " + response.readEntity(String.class));

		// get property class details

		target = client.target( "http://localhost:8080/ToucanServer/rest/property/get" );
		response = target.path("general").path(propertyID).request().get();
		System.out.println ("Reponse from server: " + response);
		System.out.println ("Returned json: " + response.readEntity(String.class));

		
	}
}
