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
	        
	         preLoader.createConnection("16.1.1.110");
	        //preLoader.createConnection("ec2-54-186-85-248.us-west-2.compute.amazonaws.com");
	        
	        /*
	         * Load the database.
	         */
	        
	        String userID = "7d0670b8-e93d-41fe-80e9-98b7625ed668";
	        	        
	        preLoader.callProcedure("ENDPOINT_USER_LINK.insert", "1007", "E1", userID);
	        preLoader.callProcedure("ENDPOINT_USER_LINK.insert", "1006", "E2", userID);
	        preLoader.callProcedure("ENDPOINT_USER_LINK.insert", "1008", "E3", userID);
	        preLoader.callProcedure("ENDPOINT_USER_LINK.insert", "1009", "E4", userID);
	        
	        
	        
	        System.out.println("Endpoints and Users linked");
	        
	        
	        System.out.println("Done!");
	        
	        /*
	         * Retrieve the message.
	         */
	
	 }
	
}
