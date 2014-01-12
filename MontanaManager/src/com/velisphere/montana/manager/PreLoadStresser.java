package com.velisphere.montana.manager;

import org.voltdb.*;
import org.voltdb.client.*;

public class PreLoadStresser {

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
	        
	         
	        	        
	        preLoader.callProcedure("CHECK.insert", "C1", "E1", "PR1", "1", "=", "0", "0", "Switch 1 on"); // Checks if switch 1 is on
	        preLoader.callProcedure("CHECK.insert", "C2", "E1", "PR2", "1", "=", "0", "0", "Switch 2 on"); // Checks if switch 2 is on
	        preLoader.callProcedure("CHECK.insert", "C3", "E1", "PR3", "1", "=", "0", "0", "Switch 3 on"); // Checks if switch 3 is on
	        preLoader.callProcedure("CHECK.insert", "C4", "E1", "PR4", "1", "=", "0", "0", "Switch 4 on"); // Checks if switch 4 is on
	        preLoader.callProcedure("CHECK.insert", "C5", "E1", "PR5", "1", "=", "0", "0", "Switch 5 on"); // Checks if switch 5 is on
	        
	        System.out.println("Checks loaded");
	        
	        preLoader.callProcedure("MULTICHECK.insert", "MC1", "AND", "0", "0");
	        preLoader.callProcedure("MULTICHECK.insert", "MC2", "AND", "0", "0");
	        preLoader.callProcedure("MULTICHECK.insert", "MC3", "AND", "0", "0");
	        preLoader.callProcedure("MULTICHECK.insert", "MC4", "AND", "0", "0");
	        preLoader.callProcedure("MULTICHECK.insert", "MC5", "AND", "0", "0");
	        	        	        	 
	        System.out.println("Multichecks loaded");
	        
	        preLoader.callProcedure("MULTICHECK_CHECK_LINK.insert", "1000", "MC1", "C1");
	        preLoader.callProcedure("MULTICHECK_CHECK_LINK.insert", "1001", "MC1", "C2");
	        preLoader.callProcedure("MULTICHECK_CHECK_LINK.insert", "1002", "MC2", "C3");
	        preLoader.callProcedure("MULTICHECK_CHECK_LINK.insert", "1003", "MC2", "C4");
	        
	        
	        System.out.println("Multichecks and Checks linked");
	        
	        preLoader.callProcedure("MULTICHECK_MULTICHECK_LINK.insert", "1000", "MC3", "MC1");
	        preLoader.callProcedure("MULTICHECK_MULTICHECK_LINK.insert", "1001", "MC3", "MC2");
	        preLoader.callProcedure("MULTICHECK_MULTICHECK_LINK.insert", "1002", "MC4", "MC3");
	        preLoader.callProcedure("MULTICHECK_MULTICHECK_LINK.insert", "1003", "MC4", "MC2");
	        
	        System.out.println("Multichecks and Multichecks linked");
	        
        
	        preLoader.callProcedure("CHECKPATH.insert", "CP1"); // Checpath für Button 1 und 2gedrückt
	        preLoader.callProcedure("CHECKPATH.insert", "CP2"); // Checkpath Button 3 und 4 gedrückt
	        preLoader.callProcedure("CHECKPATH.insert", "CP3"); // Checkpath Button 5 gedrückt
	        preLoader.callProcedure("CHECKPATH.insert", "CP4"); // Checkpath Button 1,2,3,4 gedrückt
	        preLoader.callProcedure("CHECKPATH.insert", "CP5"); // Checkpath Button 1,2,3,4 gedrückt
	        preLoader.callProcedure("CHECKPATH.insert", "CP6"); // Checkpath Button 1,2,3,4 gedrückt
	        
	        System.out.println("Checkpaths loaded");

	        preLoader.callProcedure("CHECKPATH_CHECK_LINK.insert", "1000", "CP1", "C1"); 
	        preLoader.callProcedure("CHECKPATH_CHECK_LINK.insert", "1001", "CP1", "C2");
	        preLoader.callProcedure("CHECKPATH_CHECK_LINK.insert", "1002", "CP2", "C3");
	        preLoader.callProcedure("CHECKPATH_CHECK_LINK.insert", "1003", "CP2", "C4");
	        preLoader.callProcedure("CHECKPATH_CHECK_LINK.insert", "1004", "CP3", "C5");
	        preLoader.callProcedure("CHECKPATH_CHECK_LINK.insert", "1005", "CP4", "C1");
	        preLoader.callProcedure("CHECKPATH_CHECK_LINK.insert", "1006", "CP4", "C2");
	        preLoader.callProcedure("CHECKPATH_CHECK_LINK.insert", "1007", "CP4", "C3");
	        preLoader.callProcedure("CHECKPATH_CHECK_LINK.insert", "1008", "CP4", "C4");
	        preLoader.callProcedure("CHECKPATH_CHECK_LINK.insert", "1009", "CP5", "C1");
	        preLoader.callProcedure("CHECKPATH_CHECK_LINK.insert", "1010", "CP5", "C2");
	        preLoader.callProcedure("CHECKPATH_CHECK_LINK.insert", "1011", "CP5", "C3");
	        preLoader.callProcedure("CHECKPATH_CHECK_LINK.insert", "1012", "CP5", "C4");
	        preLoader.callProcedure("CHECKPATH_CHECK_LINK.insert", "1013", "CP6", "C1");
	        preLoader.callProcedure("CHECKPATH_CHECK_LINK.insert", "1014", "CP6", "C2");
	        preLoader.callProcedure("CHECKPATH_CHECK_LINK.insert", "1015", "CP6", "C3");
	        preLoader.callProcedure("CHECKPATH_CHECK_LINK.insert", "1016", "CP6", "C4");
	        	        
	        	        
	        System.out.println("Checkpaths and Checks linked");

	        preLoader.callProcedure("CHECKPATH_MULTICHECK_LINK.insert", "1000", "CP1", "MC1");
	        preLoader.callProcedure("CHECKPATH_MULTICHECK_LINK.insert", "1001", "CP2", "MC2");
	        preLoader.callProcedure("CHECKPATH_MULTICHECK_LINK.insert", "1002", "CP4", "MC1");
	        preLoader.callProcedure("CHECKPATH_MULTICHECK_LINK.insert", "1003", "CP4", "MC2");
	        preLoader.callProcedure("CHECKPATH_MULTICHECK_LINK.insert", "1004", "CP4", "MC3");
	        preLoader.callProcedure("CHECKPATH_MULTICHECK_LINK.insert", "1005", "CP5", "MC1");
	        preLoader.callProcedure("CHECKPATH_MULTICHECK_LINK.insert", "1006", "CP5", "MC2");
	        preLoader.callProcedure("CHECKPATH_MULTICHECK_LINK.insert", "1007", "CP5", "MC3");
	        preLoader.callProcedure("CHECKPATH_MULTICHECK_LINK.insert", "1008", "CP5", "MC4");
	        preLoader.callProcedure("CHECKPATH_MULTICHECK_LINK.insert", "1009", "CP6", "MC1");
	        preLoader.callProcedure("CHECKPATH_MULTICHECK_LINK.insert", "1010", "CP6", "MC2");
	        preLoader.callProcedure("CHECKPATH_MULTICHECK_LINK.insert", "1011", "CP6", "MC3");
	        preLoader.callProcedure("CHECKPATH_MULTICHECK_LINK.insert", "1012", "CP6", "MC4");
	        preLoader.callProcedure("CHECKPATH_MULTICHECK_LINK.insert", "1013", "CP6", "MC5");
	        	        
	        System.out.println("Checkpaths and MultiChecks linked");
	        
	        preLoader.callProcedure("RULE.insert", "R1", "Button 1 gedrückt", "C1", ""); 
	        preLoader.callProcedure("RULE.insert", "R2", "Button 1 und 2 gedrückt", "", "MC1");
	        preLoader.callProcedure("RULE.insert", "R3", "Button 3 und 4 gedrückt", "", "MC2");
	        preLoader.callProcedure("RULE.insert", "R4", "Button 1,2, 3 und 4 gedrückt", "", "MC3");
	        preLoader.callProcedure("RULE.insert", "R5", "Chat von Thorsten", "C6", "");	           
	        preLoader.callProcedure("RULE.insert", "R6", "Chat von Ute", "C7", "");
	        
	        System.out.println("Rules loaded");
	 	        
	        preLoader.callProcedure("ACTION.insert", "A1", "Forward Chat to Zielendpoint", "", "PR6", 0); 
	        preLoader.callProcedure("ACTION.insert", "A2", "Licht an im Arbeitszimmer", "E2", "", 0);
	        
	        System.out.println("Actions loaded");
	        	        	        
	        preLoader.callProcedure("OUTBOUNDPROPERTYACTION.insert", "OPA1", "PR9", "PR7", "", "", "A1");
	        preLoader.callProcedure("OUTBOUNDPROPERTYACTION.insert", "OPA2", "PR9", "", "", "LICHT AN", "A2");
	        
	        System.out.println("Outbound Property Actions loaded");
	        	        
	        preLoader.callProcedure("RULE_ACTION_LINK.insert", "1000", "R5", "A1");
	        preLoader.callProcedure("RULE_ACTION_LINK.insert", "1001", "R1", "A2");
	        preLoader.callProcedure("RULE_ACTION_LINK.insert", "1002", "R6", "A1");
	        
	        System.out.println("Outbound Rules and Actions linked");
	        
	        
	        
	        System.out.println("Done!");
	        
	        /*
	         * Retrieve the message.
	         */
	
	 }
	
}
