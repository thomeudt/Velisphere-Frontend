package com.velisphere.fs.sdk.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;


public class ConfigFileAccess {
	
	public void saveParamChangesAsXML(String secret, String endpointID) {
	    try {
	        Properties props = new Properties();
	        props.setProperty("Secret Key", secret);
	        props.setProperty("Endpoint ID", endpointID);
	        
	        
	        File f = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "velisphere_config.xml");
	        OutputStream out = new FileOutputStream( f );
	        props.storeToXML(out, "This file contains Velisphere authentication information. Do not overwrite!");
	    }
	    catch (Exception e ) {
	        e.printStackTrace();
	    }
	}

	
	/**
	 * Load the preferences
	 * @return 
	 */
	
	public static void loadParamChangesAsXML(){
	
		System.out.println(" [IN] Loading config from " + System.getProperty("user.dir") + System.getProperty("file.separator") + "velisphere_config.xml");
		
	Properties props = new Properties();
    InputStream is = null;
 
   
    try {
        File f = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "velisphere_config.xml");
        is = new FileInputStream( f );
        props.loadFromXML( is );
    }
    catch ( Exception e ) { is = null; }
 
      
    
    ConfigData.secret = props.getProperty("Secret Key");
    ConfigData.epid = props.getProperty("Endpoint ID");
    
	}


}
