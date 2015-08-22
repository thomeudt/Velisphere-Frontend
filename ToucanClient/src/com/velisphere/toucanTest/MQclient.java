package com.velisphere.toucanTest;



import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
		
		String endpointID = "99539bd2-e643-45d6-b731-e68fe0e27401";
		String secret = "QyTZga/nXRVXAiA+zm2AGB3/vyEEyRkN9SI6L6jnZwPVjRKOVFCAA1ngamWtvhuDeOrZxjQA9JD+knHNHj7ASQ==";
		String propertyID = "c04a3711-84ea-4eaf-a4d7-bd2ba2f4b62d";
		String fakeSensorValue = "777";
		
		
		// build password for rabbitmq by hashing secret
		StringBuffer pwHash = null;
		MessageDigest sh;
			try {
				sh = MessageDigest.getInstance("SHA-512");
			  sh.update(secret.getBytes());
		        //Get the hash's bytes
			  
			

				 pwHash = new StringBuffer();
		            for (byte b : sh.digest()) pwHash.append(Integer.toHexString(0xff & b));
				
			
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
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
			System.out.println("Using password: "+ new String(pwHash));
			
			
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
		
				
				Response response = target.path( "json" ).path(endpointID).path(new String(pwHash)).request().post( Entity.text(submittableJSON) );
	
			
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
