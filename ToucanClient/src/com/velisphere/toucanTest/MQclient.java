package com.velisphere.toucanTest;



import java.sql.Timestamp;
import java.util.HashMap;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.velisphere.fs.sdk.MessageFabrik;
import com.velisphere.fs.sdk.config.ConfigData;
import com.velisphere.fs.sdk.security.HashTool;





public class MQclient {


	public static void main(String[] args) {

		HashMap<String, String> message = new HashMap<String, String>();
		
		message.put("7244bc76-8385-4bb5-b242-18cfa97d473b", "AAA");
		
			

			message.put("TYPE", "REG");
			java.util.Date date = new java.util.Date();
			Timestamp timeStamp = new Timestamp(date.getTime());
			message.put("TIMESTAMP", timeStamp.toString());
			message.put("EPID", "1234");

			MessageFabrik innerMessageFactory = new MessageFabrik(message);
			String messagePackJSON = innerMessageFactory.getJsonString();

			String hMAC = HashTool.getHmacSha1(messagePackJSON, "here should come the secret");

			System.out.println("Using secret: "+ ConfigData.secret);

			System.out.println("Using endpointID: "+ ConfigData.epid);
			
			
			HashMap<String, String> submittableMessage = new HashMap<String, String>();

			submittableMessage.put(hMAC, messagePackJSON);

			MessageFabrik outerMessageFactory = new MessageFabrik(
					submittableMessage);
			String submittableJSON = outerMessageFactory.getJsonString();

			System.out.println("HMAC:" + hMAC);
			System.out.println("Submittable:" + submittableJSON);

			// Message sending via HTTP POST
			
			Client client = ClientBuilder.newClient();
			
			
			WebTarget target = client.target( "http://localhost:8082/rest/message/post/" );
		
				
				Response response = target.path( "json" ).request().post( Entity.text(submittableJSON) );
	
			
				System.out.println ("Reponse from server: " + response);
				System.out.println ("Returned data: " + response.readEntity(String.class));
				
			
		
			// System.out.println(" [x] Sent '" + messagePackText + "'");


				// and validate by getting back again
				
				target = client.target( "http://localhost:8082/rest/message/get" );
				response = target.path("json").path("1234").request().get();
				System.out.println ("Reponse from server: " + response);
				System.out.println ("Returned json: " + response.readEntity(String.class));
				
				
		}

	
}
