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
package com.velisphere.toucan;

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

	
	/*
	 * Load the preferences
	 * 
	 */

	public void loadParamChangesAsXML(){

		Properties props = new Properties();
		InputStream is = null;

		// First try loading from the current directory
		try {
			File f = new File("toucanconf.xml");
			is = new FileInputStream( f );
		}
		catch ( Exception e ) { is = null; }

		try {
			if ( is == null ) {
				// Try loading from classpath
				is = getClass().getResourceAsStream("toucanconf.xml");
			}

			// Try loading properties from the file (if found)
			props.loadFromXML( is );
		}
		catch ( Exception e ) { }

		ServerParameters.bunny_ip = props.getProperty("VeliBunny AMPQ Broker IP");

		System.out.println(" [IN] Reading Configuration");
		System.out.println(" [IN] Selected RabbitMQ AMQP Broker: "+ ServerParameters.bunny_ip);

	}

}
