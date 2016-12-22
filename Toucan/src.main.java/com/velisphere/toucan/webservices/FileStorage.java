package com.velisphere.toucan.webservices;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;





@Path("/files")
public class FileStorage {
	
	private static final String SAVE_FOLDER = "/home/thorsten/filestorage/";	
	
	
	@PUT
	@Path( "/put/binary" )
	@Consumes( MediaType.MULTIPART_FORM_DATA )
	public Response postPlainTextMessage( 
			@FormDataParam("file") InputStream fileInputString, 
			@FormDataParam("file") FormDataContentDisposition fileInputDetails) throws Exception {

		
		/*
		,
		@FormDataParam("file") InputStream fileInputString,
	     @FormDataParam("file") FormDataContentDisposition fileInputDetails
		*/
		
		//InputStream fileInputString = null;
	    //FormDataContentDisposition fileInputDetails = null;
	    //String fileLocation = SAVE_FOLDER + fileInputDetails.getFileName();
		
		String randomFileName = UUID.randomUUID().toString();
		
		
		
		String fileLocation = SAVE_FOLDER + randomFileName;
		
		String status = null;
	    NumberFormat myFormat = NumberFormat.getInstance();
	    myFormat.setGroupingUsed(true);
	     
	    // Save the file
	    try {
	     OutputStream out = new FileOutputStream(new File(fileLocation));
	     byte[] buffer = new byte[1024];
	     int bytes = 0;
	     long file_size = 0;
	     while ((bytes = fileInputString.read(buffer)) != -1) {
	      out.write(buffer, 0, bytes);
	      file_size += bytes;
	     }
	     out.flush(); 
	     out.close();
	             
	   
	     ;
	    } catch (IOException ex) {
	      ex.printStackTrace();
	    }
	 
	    return Response.status(200).entity(randomFileName).build();

	
	}
	
	
		

}


