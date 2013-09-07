package com.velisphere.montana.manager;

import org.voltdb.*;
import org.voltdb.client.*;

public class PreLoad {

	 public static void main(String[] args) throws Exception {

	        /*
	         * Instantiate a client and connect to the database.
	         */
	        org.voltdb.client.Client preLoader;
	        preLoader = ClientFactory.createClient();
	        preLoader.createConnection("16.1.1.149");

	        /*
	         * Load the database.
	         */
	        preLoader.callProcedure("USER.insert", "1000", "thmeu", "thorsten@thorsten-meudt.de");
	        preLoader.callProcedure("USER.insert", "1001", "utmeu", "ute_lechner@yahoo.de");
	        preLoader.callProcedure("USER.insert", "1002", "lcmeu", "charlotte@thorsten-meudt.de");
	        
	        System.out.println("Users loaded");
	        
	        preLoader.callProcedure("ENDPOINTCLASS.insert", "1000", "Blubber Messenger Client Account");
	        preLoader.callProcedure("ENDPOINTCLASS.insert", "1001", "Generic Universal Sensor / Actor");
	        preLoader.callProcedure("ENDPOINTCLASS.insert", "1002", "Generic Setter/Getter Application");
	        
	        System.out.println("Endpointclasses loaded");
	        
	        preLoader.callProcedure("ENDPOINT.insert", "1000", "Thorstens Blubber Account", "1000");
	        preLoader.callProcedure("ENDPOINT.insert", "1001", "RaspberryPi im Arbeitszimmer", "1001");
	        preLoader.callProcedure("ENDPOINT.insert", "1002", "Utes Blubber Account", "1000");
	        preLoader.callProcedure("ENDPOINT.insert", "1003", "Homecontroller App", "1002");
	        
	        System.out.println("Endpoints loaded");
	        
	        preLoader.callProcedure("ENDPOINT_USER_LINK.insert", "1000", "1000", "1000");
	        preLoader.callProcedure("ENDPOINT_USER_LINK.insert", "1001", "1001", "1000");
	        preLoader.callProcedure("ENDPOINT_USER_LINK.insert", "1002", "1002", "1001");
	        preLoader.callProcedure("ENDPOINT_USER_LINK.insert", "1003", "1003", "1000");
	        
	        System.out.println("Endpoints and Users linked");
	        
	        preLoader.callProcedure("PROPERTYCLASS.insert", "1000", "Text Message", "String", "");
	        preLoader.callProcedure("PROPERTYCLASS.insert", "1001", "Generic Digital Port", "Byte", "");
	        preLoader.callProcedure("PROPERTYCLASS.insert", "1002", "Brightness", "Double", "lux");
	        preLoader.callProcedure("PROPERTYCLASS.insert", "1003", "On Off Switch", "Byte", "");
	        preLoader.callProcedure("PROPERTYCLASS.insert", "1004", "On Off Indicator", "Byte", "");
	        
	        System.out.println("Property Classes loaded");
	        
	        preLoader.callProcedure("PROPERTY.insert", "1000", "Switch 1", "1003", "1003");
	        preLoader.callProcedure("PROPERTY.insert", "1001", "Switch 2", "1003", "1003");
	        preLoader.callProcedure("PROPERTY.insert", "1002", "Light 1 On Indicator", "1004", "1003");
	        preLoader.callProcedure("PROPERTY.insert", "1003", "Digital Port 6", "1001", "1001");
	        preLoader.callProcedure("PROPERTY.insert", "1004", "Switch 3", "1003", "1003");
	        
	        System.out.println("Properties loaded");
	        
	        preLoader.callProcedure("CHECK.insert", "1000", "1001", "1003", "1", "=", "0", "0"); // Checks if relay on RPI is on
	        preLoader.callProcedure("CHECK.insert", "1001", "1003", "1000", "1", "=", "0", "0"); // Checks if button "Lights" on home controller is pressed
	        preLoader.callProcedure("CHECK.insert", "1002", "1003", "1001", "1", "=", "0", "0"); // Checks if button "Lightsoff" on home controller is pressed
	        preLoader.callProcedure("CHECK.insert", "1003", "1003", "1004", "1", "=", "0", "0"); // Checks if button "Lightsoff" on home controller is pressed
	        
	        System.out.println("Checks loaded");
	        
	        preLoader.callProcedure("MULTICHECK.insert", "1000", "AND", "0", "0");
	        preLoader.callProcedure("MULTICHECK.insert", "1001", "AND", "0", "0");
	        preLoader.callProcedure("MULTICHECK.insert", "1002", "AND", "0", "0");
	        	 
	        System.out.println("Multichecks loaded");
	        
	        preLoader.callProcedure("MULTICHECK_CHECK_LINK.insert", "1000", "1000", "1001");
	        preLoader.callProcedure("MULTICHECK_CHECK_LINK.insert", "1001", "1000", "1002");
	        preLoader.callProcedure("MULTICHECK_CHECK_LINK.insert", "1002", "1001", "1001");
	        preLoader.callProcedure("MULTICHECK_CHECK_LINK.insert", "1003", "1001", "1003");
	        
	        System.out.println("Multichecks and Checks linked");
	        
	        preLoader.callProcedure("MULTICHECK_MULTICHECK_LINK.insert", "1000", "1002", "1000");
	        preLoader.callProcedure("MULTICHECK_MULTICHECK_LINK.insert", "1001", "1002", "1001");
	        
	        System.out.println("Multichecks and Multichecks linked");
	        
	        System.out.println("Done!");
	        
	        /*
	         * Retrieve the message.
	         */
	 }
	
}
