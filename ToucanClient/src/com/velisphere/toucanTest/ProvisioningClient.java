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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.velisphere.toucanTest.config.ConfigFileAccess;
import com.velisphere.toucanTest.config.SecretData;





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
	 
		
		
			
			WebTarget target = client.target( "http://localhost:8080/ToucanServer/rest/provisioning/put" );

			//Response response = target.path( "endpoint" ).path( sb.toString() ).request().put( Entity.text("f67528e4-80f7-4832-a5fd-3082bd4e7385") );
			
			String identifier = "MSG2"; 
			
			Response response = target.path( "endpoint" ).path( identifier).request().put( Entity.text("363a5307-bcd3-4b97-acfc-06da47411355") );
			System.out.println("Search for identifier: " + identifier);	
		
			System.out.println (response);
			String jsonInput = response.readEntity(String.class);
			System.out.println (jsonInput);
			
			ObjectMapper mapper = new ObjectMapper();

			SecretData secretData = new SecretData();
			try {
				secretData = mapper.readValue(jsonInput, SecretData.class);			
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			ConfigFileAccess.saveParamChangesAsXML(secretData.getSecret(), secretData.getEpid());


		} catch (UnknownHostException e) {
				System.out.println("Unknown Host " + e);
			e.printStackTrace();
		} catch (SocketException e) {
				System.out.println("Socket Error " + e);
			e.printStackTrace();
		}
		 
		
		
		

		
	
		
		
	}
}
