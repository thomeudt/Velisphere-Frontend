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
	        
	        System.out.println("Endpoints loaded");
	        
	        preLoader.callProcedure("ENDPOINT_USER_LINK.insert", "1000", "1000", "1000");
	        preLoader.callProcedure("ENDPOINT_USER_LINK.insert", "1001", "1001", "1000");
	        preLoader.callProcedure("ENDPOINT_USER_LINK.insert", "1002", "1002", "1001");
	        
	        System.out.println("Endpoints and Users linked");
	        
	        preLoader.callProcedure("PROPERTY.insert", "1000", "Text Message", "String", "");
	        preLoader.callProcedure("PROPERTY.insert", "1001", "Two State Relay State", "Byte", "");
	        preLoader.callProcedure("PROPERTY.insert", "1002", "Brightness", "Double", "lux");
	        
	        System.out.println("Properties loaded");
	        
	        System.out.println("Done!");
	        
	        /*
	         * Retrieve the message.
	         */
	 }
	
}
