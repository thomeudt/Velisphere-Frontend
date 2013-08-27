package com.velisphere.chai;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;



public class ConfigHandler {


	/**
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

	
	/**
	 * Load the preferences
	 * @return 
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
	
    ServerParameters.ldapUrl = props.getProperty("LDAP URL");
    ServerParameters.ldapPrincipal = props.getProperty("LDAP Principal");
    ServerParameters.ldapPassword = props.getProperty("LDAP PW");
    ServerParameters.controllerQueueName = props.getProperty("Controller Queue Name");
    ServerParameters.bunny_ip = props.getProperty("VeliBunny AMPQ Broker IP");
    ServerParameters.threadpoolSize = Integer.parseInt(props.getProperty("Threadpool Size"));
    
    System.out.println(" [IN] Reading Configuration");
    System.out.println(" [IN] Selected VeliBunny AMQP Broker: "+ ServerParameters.bunny_ip);
    System.out.println(" [IN] Threadpool Size: "+ ServerParameters.threadpoolSize);
    
	}

}
