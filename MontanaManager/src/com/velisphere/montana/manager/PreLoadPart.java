package com.velisphere.montana.manager;

import org.voltdb.*;
import org.voltdb.client.*;

public class PreLoadPart {

	 public static void main(String[] args) throws Exception {

	        /*
	         * Instantiate a client and connect to the database.
	         */
	        org.voltdb.client.Client preLoader;
	        preLoader = ClientFactory.createClient();
	     // public static String volt_ip = "16.1.1.149"; // for local db
	    	// public static String volt_ip = "ec2-54-200-208-195.us-west-2.compute.amazonaws.com"; // for aws db
	        
	        preLoader.createConnection("16.1.1.149");

	        /*
	         * Load the database.
	         */
	        
	        	        
	        preLoader.callProcedure("ENDPOINT_USER_LINK.insert", "1007", "E1", "6014efca-5d35-45cf-ab87-38e6ec966495");
	        preLoader.callProcedure("ENDPOINT_USER_LINK.insert", "1006", "E2", "6014efca-5d35-45cf-ab87-38e6ec966495");
	        preLoader.callProcedure("ENDPOINT_USER_LINK.insert", "1008", "E3", "6014efca-5d35-45cf-ab87-38e6ec966495");
	        
	        
	        
	        System.out.println("Endpoints and Users linked");
	        
	        
	        System.out.println("Done!");
	        
	        /*
	         * Retrieve the message.
	         */
	
	 }
	
}
