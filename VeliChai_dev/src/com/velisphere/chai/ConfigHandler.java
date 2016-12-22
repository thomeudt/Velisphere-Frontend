/*******************************************************************************
 * CONFIDENTIAL INFORMATION
 *  __________________
 *  
 *   Copyright (C) 2013 Thorsten Meudt 
 *   All Rights Reserved.
 *  
 *  NOTICE:  All information contained herein is, and remains
 *  the property of Thorsten Meudt and its suppliers,
 *  if any.  The intellectual and technical concepts contained
 *  herein are proprietary to Thorsten Meudt
 *  and its suppliers and may be covered by Patents,
 *  patents in process, and are protected by trade secret or copyright law.
 *  Dissemination of this information or reproduction of this material
 *  is strictly forbidden unless prior written permission is obtained
 *  from Thorsten Meudt.
 ******************************************************************************/
package com.velisphere.chai;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;




public class ConfigHandler {


	/*
	 * 
	 * Save the preferences
	 * 
	 */

	public static void saveParamChangesAsXML(String url, String principal, String password, String controllerQueueName, String veliBunnyIP) {
		try {
			Properties props = new Properties();
			props.setProperty("LDAP URL", url);
			props.setProperty("LDAP Principal", principal);
			props.setProperty("LDAP PW", password);
			props.setProperty("Controller Queue Name", controllerQueueName);
			props.setProperty("VeliBunny AMPQ Broker IP", veliBunnyIP);

			File f = new File("chaiconf.xml");
			OutputStream out = new FileOutputStream( f );
			props.storeToXML(out, "This file contains chai configuration information. Do not overwrite!");
		}
		catch (Exception e ) {
			e.printStackTrace();
		}
	}


	/*
	 * Load the preferences
	 * 
	 */

	public void loadParamChangesAsXML(){

		Properties props = new Properties();
		InputStream is = null;

		// First try loading from the current directory
		try {
			File f = new File("chaiconf.xml");
			is = new FileInputStream( f );
		}
		catch ( Exception e ) { is = null; }

		try {
			if ( is == null ) {
				// Try loading from classpath
				is = getClass().getResourceAsStream("chaiconf.xml");
			}

			// Try loading properties from the file (if found)
			props.loadFromXML( is );
		}
		catch ( Exception e ) { }

		ServerParameters.controllerQueueName = props.getProperty("Controller Queue Name");
		ServerParameters.threadpoolSize = Integer.parseInt(props.getProperty("Threadpool Size"));
		
		System.out.println(" [IN] Requesting Configuration From BlenderServer");
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target( "http://www.connectedthingslab.com:8080/BlenderServer/rest/config/get/general" );
		Response response = target.path("RABBIT").request().get();
		ServerParameters.bunny_ip = response.readEntity(String.class);
		System.out.println(" [IN] BlenderServer provided Rabbit IP: " + ServerParameters.bunny_ip);
		response = target.path("VOLT").request().get();
		ServerParameters.volt_ip = response.readEntity(String.class);
		System.out.println(" [IN] BlenderServer provided Volt IP: " + ServerParameters.volt_ip);
		response = target.path("VERTICA").request().get();
		ServerParameters.vertica_ip = response.readEntity(String.class);
		System.out.println(" [IN] BlenderServer provided Vertica IP: " + ServerParameters.vertica_ip);
		System.out.println(" [IN] Threadpool Size set in chaiconf.xml: "+ ServerParameters.threadpoolSize);

		
		
	}

}
