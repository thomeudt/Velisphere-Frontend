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
		
		String endpointID = "596bf410-9b99-4acc-9d2b-202f3e315c65";
		String secret = "Pr03JIRthrRMqEr8lBI7VIATQ6vfmM+jcNmUen6nXzjyf4W16Pp/xv8DvIUwDmzBEqe8bcpyNGLHrrGg5LElLg==";
		String propertyID = "c04a3711-84ea-4eaf-a4d7-bd2ba2f4b62d";
		String fakeSensorValue = "777";
		
		
		message.put(propertyID, fakeSensorValue);
		
			

			message.put("TYPE", "REG");
			java.util.Date date = new java.util.Date();
			Timestamp timeStamp = new Timestamp(date.getTime());
		
			message.put("TIMESTAMP", timeStamp.toString());
			message.put("EPID", endpointID);

			MessageFabrik innerMessageFactory = new MessageFabrik(message);
			String messagePackJSON = innerMessageFactory.getJsonString();

			String hMAC = HashTool.getHmacSha1(messagePackJSON, secret);

			System.out.println("Using secret: "+ secret);

			System.out.println("Using endpointID: "+ endpointID);
			
			
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
				response = target.path("json").path(endpointID).request().get();
				System.out.println ("Reponse from server: " + response);
				System.out.println ("Returned json: " + response.readEntity(String.class));
				
				
		}

	
}
