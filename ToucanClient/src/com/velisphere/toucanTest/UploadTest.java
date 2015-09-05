package com.velisphere.toucanTest;

import java.io.File;
import java.net.InetAddress;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

public class UploadTest {
	public static void main(String[] args) {
		
		Client client = ClientBuilder.newClient();

		
		WebTarget target = client.target( "http://connectedthingslab.com:8080/BlenderServer/rest/config/get/general" );
		Response response = target.path("TOUCAN").request().get();
		
		String toucanIP = response.readEntity(String.class);
		
		InetAddress ip;
		
		
		// get user name
		
		String userid = "3f82b42b-7d77-4e97-8e92-becdc1bf797d";
		String endpointID = "9e48c4eb-4ac1-4512-ac53-b86b1dd130aa";
		String endpointClassID = "EPC1";
		String propertyClassID = "PC1";
		String vendorID = "65eb271e-d6c3-470a-8d62-6e40b9f930e7";
		String propertyID = "aa4bae92-3fda-44ca-ad28-f65931ef10a0";		
				
		
		// upload image
		
		client.register(MultiPartFeature.class);
		 
	    final MultiPart multiPart = new MultiPart();
	 
	    // large file
	    //FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("file", new File("/home/thorsten/Downloads/stockvault-internet161600.jpg"), MediaType.APPLICATION_OCTET_STREAM_TYPE);
	    
	    // small file
	    
	    FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("file",
	            new File("/home/thorsten/Downloads/SF14_DATS004_100p NEW.pdf"), MediaType.APPLICATION_OCTET_STREAM_TYPE);
	    
	    multiPart.bodyPart(fileDataBodyPart);
	    
	    String fileType = "PDF";
	    
	    final Response res = client.target("http://"+toucanIP+"/rest/files/put/binary").path(fileType).path(endpointID).request().put(Entity.entity(multiPart, MediaType.MULTIPART_FORM_DATA_TYPE));
	 
	    System.out.println("Response: "+res.readEntity(String.class));
		
	}
}
