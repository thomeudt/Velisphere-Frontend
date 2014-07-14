package com.velisphere.toucanTest;



import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URI;
import java.net.UnknownHostException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.*;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;





public class ProvisioningClient {


	public static void main(String[] args) {
		InetAddress ip;
		
		Client client = ClientBuilder.newClient();

		try {
			ip = InetAddress.getLocalHost();
			System.out.println("Current IP address : " + ip.getHostAddress());
			 
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
	 
			byte[] mac = network.getHardwareAddress();
			
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));		
			}
			System.out.println("Current Mac Address: " + sb.toString());
	 
		
		
			
			WebTarget target = client.target( "http://localhost:8080/Toucan/rest/provisioning/put" );

			//Response response = target.path( "endpoint" ).path( sb.toString() ).request().put( Entity.text("f67528e4-80f7-4832-a5fd-3082bd4e7385") );
			Response response = target.path( "endpoint" ).path( "Phi2" ).request().put( Entity.text("9555b1d6-aa24-4dd5-afef-bc7c95897e34") );
				
		
			System.out.println (response);
			System.out.println (response.readEntity(String.class));
			


		} catch (UnknownHostException e) {
				System.out.println("Unknown Host " + e);
			e.printStackTrace();
		} catch (SocketException e) {
				System.out.println("Socket Error " + e);
			e.printStackTrace();
		}
		 
		
		
		

		
	
		
		
	}
}
