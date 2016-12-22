package com.velisphere.toucanTest.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;


public class ConfigFileAccess {
	public static void saveParamChangesAsXML(String secret, String endpointID) {
	    try {
	        Properties props = new Properties();
	        props.setProperty("Secret Key", secret);
	        props.setProperty("Endpoint ID", endpointID);
	        
	        
	        File f = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "velisphere_config.xml");
	        System.out.println(System.getProperty("user.dir"));
	        OutputStream out = new FileOutputStream( f );
	        props.storeToXML(out, "This file contains Velisphere authentication information. Do not overwrite!");
	    }
	    catch (Exception e ) {
	        e.printStackTrace();
	    }
	}
	
}