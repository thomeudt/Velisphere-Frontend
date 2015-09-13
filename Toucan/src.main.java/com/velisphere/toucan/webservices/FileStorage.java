package com.velisphere.toucan.webservices;

import java.awt.Image;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.activation.FileDataSource;
import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.ContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.MultiPart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.velisphere.toucan.vertica.Uploads;
import com.velisphere.toucan.volt.MessageValidator;

@Path("/files")
public class FileStorage {

	private static final String SAVE_FOLDER = "/home/thorsten/filestorage/";

	@POST
	@Path("/post/binary/upload/{filetype}/{endpointid}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response postFile(
			@PathParam("filetype") String fileType,
			@PathParam("endpointid") String endpointID,
			@FormDataParam("file") InputStream fileInputString,
			@FormDataParam("file") FormDataContentDisposition fileInputDetails,
			@FormDataParam("hMAC") String hMAC)
			throws Exception {

		
		// security handling
		
		if(MessageValidator.validateHmac(hMAC, endpointID, endpointID)==true)
			{
			
			// file handling
			
			System.out.println(" [IN] Filetype received is " + fileType);


			System.out.println(" [IN] HMAC received is " + hMAC);

			
			String randomFileName = UUID.randomUUID().toString() + "." + fileType;

			String originalFileName = fileInputDetails.getFileName();

			// validate if size is within 1MB limit, if not, return negative
			// response

			if (fileInputDetails.getSize() > 1048576) {
				return Response.status(413).entity("Rejected. Size limit exceeded")
						.build();
			} else {
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

				// Add entry to Vertica

				java.util.Date date = new java.util.Date();

				String uploadID = String.valueOf(UUID.randomUUID());
				
				Uploads uploads = new Uploads();
				uploads.uploadFile(uploadID,
						randomFileName, fileType, originalFileName, endpointID,
						String.valueOf(new Timestamp(date.getTime())));
				
				return Response.status(200).entity(uploadID).build();

			}

			
			}
			else
				{

				System.out.println(" [IN] hMAC does not match - potential security breach.");

				return Response.status(500).build();
				}

		
		
	}

	
	@GET
	@Path("/get/binary/download/{filetype}/{endpointid}/{json}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.MULTIPART_FORM_DATA)
	public Response getFile(
			@PathParam("filetype") String fileType,
			@PathParam("endpointid") String endpointID,
			@PathParam("json") String jSON)
			throws Exception {

		System.out.println(" [IN] Request received: Download File, JSON: " + jSON);

		
		// parse JSON
		
		String[] fileIdAndHmac = new String[2];
		
		ObjectMapper mapper = new ObjectMapper();
		
		fileIdAndHmac = mapper.readValue(jSON, String[].class);


		String hMAC = fileIdAndHmac[1];
		String fileID = fileIdAndHmac[0];
		
		// security handling
		
		if(MessageValidator.validateHmac(hMAC, fileID, endpointID)==true)
			{
			
			// file handling
			
			System.out.println(" [IN] Filetype received is " + fileType);


			System.out.println(" [IN] HMAC received is " + hMAC);

			
				// Get path from Vertica


				Uploads uploads = new Uploads();
				String [] nameArray = uploads.downloadFile(fileID, endpointID);
				String fileName = nameArray[0];
				String origName = nameArray[1];
				String fullPath = SAVE_FOLDER + fileName;
			
				
				// load file from disk and return
				
				File downloadableFile = new File(fullPath);
				
				FileDataSource file = new FileDataSource(downloadableFile); 
				
				
				MultiPart multiPart = new MultiPart(). 
				bodyPart(new BodyPart(file, new MediaType("application", "octet-stream"))); 
				
				ContentDisposition contentDisposition = ContentDisposition.type("attachment")
					    .fileName(origName).creationDate(new Date()).build();

				
				return Response.ok(multiPart, MediaType.MULTIPART_FORM_DATA).header("Content-Disposition", contentDisposition).build(); 
				
			
			}
			else
				{

				System.out.println(" [IN] hMAC does not match - potential security breach.");

				return Response.status(500).build();
				}

		
		
	}

	
	
	
}
