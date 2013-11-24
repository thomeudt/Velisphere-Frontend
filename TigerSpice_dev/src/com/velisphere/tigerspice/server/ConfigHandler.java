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
package com.velisphere.tigerspice.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;



public class ConfigHandler {


	/*
	 * 
	 * Save the preferences
	 * 
	 */

	public void saveParamChangesAsXML(String url, String principal, String password, String controllerQueueName, String veliBunnyIP) {
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
				is = getClass().getResourceAsStream("./chaiconf.xml");
			}

			// Try loading properties from the file (if found)
			props.loadFromXML( is );
		}
		catch ( Exception e ) { }

		
		ServerParameters.volt_ip = props.getProperty("Volt IP");

		System.out.println(" [IN] Reading Configuration");
		System.out.println(" [IN] Selected VoltDB: "+ ServerParameters.volt_ip);

	}

}
