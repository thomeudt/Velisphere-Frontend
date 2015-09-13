package com.velisphere.toucanTest;

import java.io.File;
import java.net.InetAddress;
import java.util.HashMap;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.velisphere.fs.sdk.MessageFabrik;
import com.velisphere.fs.sdk.security.HashTool;

public class UploadTest {
	public static void main(String[] args) {
		
		Client client = ClientBuilder.newClient();

		
		WebTarget target = client.target( "http://connectedthingslab.com:8080/BlenderServer/rest/config/get/general" );
		Response response = target.path("TOUCAN").request().get();
		
		String toucanIP = response.readEntity(String.class);
		
		
		String endpointID = "08c7f657-d091-4a2d-bcbd-3ce832ae545c";
		String secret = "XrbFLA7al1fR/nEgdzHCxRE1ucz8qTekcOutSe8Qcw/eHMDuQec8mIw8JAIFR2fQUBT0g8LtxpAskvXN+gwFgQ==";
				
		
		// upload image
		
		client.register(MultiPartFeature.class);
		 
	    MultiPart multiPart = new MultiPart();
	 
	    // large file
	    //FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("file", new File("/home/thorsten/Downloads/stockvault-internet161600.jpg"), MediaType.APPLICATION_OCTET_STREAM_TYPE);
	    
	    // small file
	    
	    FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("file",
	            new File("/home/thorsten/Downloads/SF14_DATS004_100p NEW.pdf"), MediaType.APPLICATION_OCTET_STREAM_TYPE);
	    
	    multiPart.bodyPart(fileDataBodyPart);
	    
	    String fileType = "PDF";
	    
	    // calculate hMAC
	    
		String hMacEndpointID = HashTool.getHmacSha1(endpointID, secret);


	    FormDataBodyPart hMACBodyPart = new FormDataBodyPart("hMAC", hMacEndpointID, MediaType.TEXT_PLAIN_TYPE);
	    multiPart.bodyPart(hMACBodyPart);
		
	
	    
	    //final Response res = client.target("http://"+toucanIP+"/rest/files/put/binary").path(fileType).path(endpointID).request().put(Entity.entity(multiPart, MediaType.MULTIPART_FORM_DATA_TYPE));
	 
		final Response res = client.target("http://"+toucanIP+"/rest/files/post/binary/upload").path(fileType).path(endpointID).request().post(Entity.entity(multiPart, MediaType.MULTIPART_FORM_DATA));
		
		String uploadID = res.readEntity(String.class);
		
	    System.out.println("Response: "+ uploadID);
	    
	    
	    // now download that file to test
	    
	    String hMacUploadID = HashTool.getHmacSha1(uploadID, secret);
	    
	    String[] fileIdAndHmac = new String[2];
				
		fileIdAndHmac[1] = hMacUploadID;
		fileIdAndHmac[0] = uploadID;
	    
	    
	    MessageFabrik messageFabrik = new MessageFabrik(fileIdAndHmac);

		
	    
	    
	    
	    
	    final Response resCheck = client.target("http://"+toucanIP+"/rest/files/get/binary/download").path(fileType).path(endpointID).path(messageFabrik.getJsonString()).request().get();
		System.out.println ("Reponse from server: " + resCheck);
		System.out.println ("Returned json: " + resCheck.readEntity(String.class));
	    
		
	}
}
