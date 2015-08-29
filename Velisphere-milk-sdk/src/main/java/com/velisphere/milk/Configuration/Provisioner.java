package com.velisphere.milk.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class Provisioner {
	
	public static String triggerProvisioning(String identifier)
	{
		
		Client client = ClientBuilder.newClient();

		System.out.println(" [IN] Requesting Server Configuration from Blender...");
		
		WebTarget target = client
				.target("http://connectedthingslab.com:8080/BlenderServer/rest/config/get/general");
		Response response = target.path("TOUCAN").request().get();

		System.out.println(" [IN] Blender responded with: "+ response);

		String toucanIP = response.readEntity(String.class);


		System.out.println(" [IN] Sending provisioning request to Toucan...");

		
		target = client.target("http://" + toucanIP
				+ "/rest/provisioning/put");

		response = target.path("endpoint").path(identifier).request()
				.put(Entity.text("EPC1"));
		

		System.out.println(response);
		String jsonInput = response.readEntity(String.class);
		System.out.println(jsonInput);

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

		System.out.println(" [IN] Toucan provisioned with Endpoint ID " + secretData.getEpid() + " and secret " + secretData.getSecret());
		
		ConfigFileAccess.saveParamChangesAsXML(secretData.getSecret(),
				secretData.getEpid(), true);

		return ("OK");
		
	}
	
	public static boolean isProvisioned()
	{
		Properties props = new Properties();
		InputStream is = null;

		try {
			File f = new File(System.getProperty("user.dir")
					+ System.getProperty("file.separator")
					+ "velisphere_config.xml");
			is = new FileInputStream(f);
			props.loadFromXML(is);
		} catch (Exception e) {
			is = null;
		}
		
		if(props.getProperty("isProvisioned") == "true")
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}
	

}
