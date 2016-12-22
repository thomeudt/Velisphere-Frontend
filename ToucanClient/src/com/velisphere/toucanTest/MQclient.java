package com.velisphere.toucanTest;



import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
		
		String endpointID = "f217be48-3cb1-49ac-a737-95c3396e3ba2";
		String secret = "v9OLVQlNJBQTt8NVmwAx3nb3QrYblNJOX0L741UpQP+nr7SdsLUHuz2WU/d46w4kX/eU/CKJGx1rRjEff6UzkQ==";
		String propertyID = "PR6";
		String fakeSensorValue = "888";
		
		
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
			
			
			Map<String,Object> submittableMessage = new HashMap<String,Object>();
			
			
			HashMap<String, String> passwordMap = new HashMap<String, String>();
			passwordMap.put("password", new String(pwHash));
			HashMap<String, String> messageMap = new HashMap<String, String>();
			messageMap.put(hMAC, messagePackJSON);
			
			submittableMessage.put(new String(pwHash), messageMap);
			
			
			
						
			MessageFabrik messageFabrik = new MessageFabrik(submittableMessage);
			
			
			// Message sending via HTTP POST
			
			Client client = ClientBuilder.newClient();
			
			
			
			
			
			WebTarget target = client.target( "http://localhost:8082/rest/message/post/" );
		
				

			for(int x = 1; x < 50; x = x+1) {
			
			Response response = target.path( "pushjson" ).path(endpointID).request().post( Entity.text(messageFabrik.getJsonString()) );
			
			
				System.out.println ("Reponse from server: " + response);
				System.out.println ("Returned data: " + response.readEntity(String.class));
				
			}
		
			// System.out.println(" [x] Sent '" + messagePackText + "'");


				// and validate by getting back again
				
				target = client.target( "http://localhost:8082/rest/message/post" );
				Response getResponse = target.path("requestjson").path(endpointID).request().post(Entity.text(new String(pwHash)));
				System.out.println ("Reponse from server: " + getResponse);
				System.out.println ("Returned json: " + getResponse.readEntity(String.class));
				
				
		}


	
	
}


class MqForJSON{
	public String password;
	public HashMap<String, String> message;
}
