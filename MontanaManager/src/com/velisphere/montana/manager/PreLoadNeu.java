package com.velisphere.montana.manager;

import org.voltdb.*;
import org.voltdb.client.*;

public class PreLoadNeu {

	 public static void main(String[] args) throws Exception {

	        /*
	         * Instantiate a client and connect to the database.
	         */
	        org.voltdb.client.Client preLoader;
	        preLoader = ClientFactory.createClient();
	        //public static String volt_ip = "16.1.1.149"; // for local db
	    	//public static String volt_ip = "ec2-54-200-17-56.us-west-2.compute.amazonaws.com"; // for aws db
	        
	        preLoader.createConnection("16.1.1.110");
	        //preLoader.createConnection("ec2-54-186-8-25.us-west-2.compute.amazonaws.com");
	        
	        /*
	         * Load the database.
	         */
	        
	        preLoader.callProcedure("SPHERE.insert", "1000", "Home Tübingen", "0");
	        preLoader.callProcedure("SPHERE.insert", "1001", "My Car", "0");
	       
	        
	        System.out.println("Spheres loaded");
	        
	        
	        preLoader.callProcedure("USER.insert", "1000", "thmeu", "thorsten@thorsten-meudt.de", "asa");
	        preLoader.callProcedure("USER.insert", "1001", "utmeu", "ute_lechner@yahoo.de", "asa");
	        preLoader.callProcedure("USER.insert", "1002", "lcmeu", "charlotte@thorsten-meudt.de", "asa");
	        
	        System.out.println("Users loaded");
	        
	        preLoader.callProcedure("ENDPOINTCLASS.insert", "EPC1", "Blubber Messenger Client Account");
	        preLoader.callProcedure("ENDPOINTCLASS.insert", "EPC2", "Generic Universal Sensor / Actor");
	        preLoader.callProcedure("ENDPOINTCLASS.insert", "EPC3", "Generic Setter/Getter Application");
	        
	        System.out.println("Endpointclasses loaded");
	        
	        preLoader.callProcedure("ENDPOINT.insert", "E1", "PiPhidget", "EPC2");
	        preLoader.callProcedure("ENDPOINT.insert", "E2", "Blubber Thorsten", "EPC1");
	        preLoader.callProcedure("ENDPOINT.insert", "E3", "Blubber Ute", "EPC1");
	        preLoader.callProcedure("ENDPOINT.insert", "E4", "Reference Home Controller App", "EPC3");
	        
	        System.out.println("Endpoints loaded");
	        
	        preLoader.callProcedure("ENDPOINT_USER_LINK.insert", "1000", "E1", "1000");
	        preLoader.callProcedure("ENDPOINT_USER_LINK.insert", "1001", "E2", "1000");
	        preLoader.callProcedure("ENDPOINT_USER_LINK.insert", "1002", "E3", "1001");
	        
	        System.out.println("Endpoints and Spheres linked");
	        
	        preLoader.callProcedure("ENDPOINT_SPHERE_LINK.insert", "1000", "E1", "1000");
	        preLoader.callProcedure("ENDPOINT_SPHERE_LINK.insert", "1001", "E2", "1000");
	        preLoader.callProcedure("ENDPOINT_SPHERE_LINK.insert", "1002", "E3", "1001");
	        
	        System.out.println("Endpoints and Users linked");
	        
	        preLoader.callProcedure("PROPERTYCLASS.insert", "PC1", "Text", "String", "");
	        preLoader.callProcedure("PROPERTYCLASS.insert", "PC2", "Generic Digital Port", "Byte", "");
	        preLoader.callProcedure("PROPERTYCLASS.insert", "PC3", "Brightness", "Double", "lux");
	        preLoader.callProcedure("PROPERTYCLASS.insert", "PC4", "On Off Switch", "Byte", "");
	        preLoader.callProcedure("PROPERTYCLASS.insert", "PC5", "On Off Indicator", "Byte", "");
	        preLoader.callProcedure("PROPERTYCLASS.insert", "PC6", "Touch", "Double", "");
	        preLoader.callProcedure("PROPERTYCLASS.insert", "PC7", "Rotation", "Double", "");
	        preLoader.callProcedure("PROPERTYCLASS.insert", "PC8", "Pressure", "Double", "");
	        	        
	        System.out.println("Property Classes loaded");
	        
	        preLoader.callProcedure("PROPERTY.insert", "PR1", "Switch 1", "PC4", "EPC3", 0, 1, 0, 0);
	        preLoader.callProcedure("PROPERTY.insert", "PR2", "Switch 2", "PC4", "EPC3", 0, 1, 0, 0);
	        preLoader.callProcedure("PROPERTY.insert", "PR3", "Switch 3", "PC4", "EPC3", 0, 1, 0, 0);
	        preLoader.callProcedure("PROPERTY.insert", "PR4", "Switch 4", "PC4", "EPC3", 0, 1, 0, 0);
	        preLoader.callProcedure("PROPERTY.insert", "PR5", "Switch 5", "PC4", "EPC3", 0, 1, 0, 0);
	        preLoader.callProcedure("PROPERTY.insert", "PR6", "To Field", "PC1", "EPC1", 0, 1, 0, 0);
	        preLoader.callProcedure("PROPERTY.insert", "PR7", "Send Content", "PC1", "EPC1", 1, 0, 0, 0);
	        preLoader.callProcedure("PROPERTY.insert", "PR8", "Send Request", "PC1", "EPC1", 1, 0, 0, 0);
	        preLoader.callProcedure("PROPERTY.insert", "PR9", "Receive Content", "PC1", "EPC1", 1, 0, 0, 0);
	        preLoader.callProcedure("PROPERTY.insert", "PR10", "Measured Pressure", "PC8", "EPC2", 0, 1, 0, 0);
	        preLoader.callProcedure("PROPERTY.insert", "PR11", "Measured Rotation", "PC7", "EPC2", 0, 1, 0, 0);
	        preLoader.callProcedure("PROPERTY.insert", "PR12", "Measured Touch", "PC6", "EPC2", 0, 1, 0, 0);
	        preLoader.callProcedure("PROPERTY.insert", "PR13", "Measured Brightness", "PC3", "EPC2", 0, 1, 0, 0);
	        	        	        
	        System.out.println("Properties loaded");
	        
	        	        
	        
	        preLoader.callProcedure("CHECK.insert", "C1", "E1", "PR1", "1", "=", "0", "0", "Switch 1 on", "CP1"); // Checks if switch 1 is on
	        preLoader.callProcedure("CHECKSTATE.insert", "C1", 0, "CP1");
	        preLoader.callProcedure("CHECK.insert", "C2", "E1", "PR2", "1", "=", "0", "0", "Switch 2 on", "CP1"); // Checks if switch 2 is on
	        preLoader.callProcedure("CHECKSTATE.insert", "C2", 0, "CP1");
	        preLoader.callProcedure("CHECK.insert", "C3", "E1", "PR3", "1", "=", "0", "0", "Switch 3 on", "CP1"); // Checks if switch 3 is on
	        preLoader.callProcedure("CHECKSTATE.insert", "C3", 0, "CP1");
	        preLoader.callProcedure("CHECK.insert", "C4", "E1", "PR4", "1", "=", "0", "0", "Switch 4 on", "CP1"); // Checks if switch 4 is on
	        preLoader.callProcedure("CHECKSTATE.insert", "C4", 0, "CP1");
	        preLoader.callProcedure("CHECK.insert", "C5", "E1", "PR5", "1", "=", "0", "0", "Switch 5 on", "CP1"); // Checks if switch 5 is on
	        preLoader.callProcedure("CHECKSTATE.insert", "C5", 0, "CP1");
	        preLoader.callProcedure("CHECK.insert", "C6", "E2", "PR8", "1", "=", "0", "0", "Send Request is Triggered", "CP2"); // Checks if Send Request is Set
	        preLoader.callProcedure("CHECKSTATE.insert", "C6", 0, "CP2");
	        preLoader.callProcedure("CHECK.insert", "C7", "E3", "PR8", "1", "=", "0", "0", "Send Request is Triggered", "CP2"); // Checks if Send Request is Set
	        preLoader.callProcedure("CHECKSTATE.insert", "C7", 0, "CP2");
	        preLoader.callProcedure("CHECK.insert", "C8", "E1", "PR1", "0", ">", "0", "0", "Switch 1 is greater than 0", "CP1"); // Checks if switch 1 is on
	        preLoader.callProcedure("CHECKSTATE.insert", "C8", 0, "CP1");
	        preLoader.callProcedure("CHECK.insert", "C9", "E1", "PR1", "5", "<", "0", "0", "Switch 1 is less than 5", "CP1"); // Checks if switch 1 is on
	        preLoader.callProcedure("CHECKSTATE.insert", "C9", 0, "CP1");
	        
	        /* megastress
	       */
	        int i = 0;
	        
	        while (i<5){
	        	preLoader.callProcedure("CHECK.insert", "CA"+i, "E1"+i, "PR1", "1", "=", "0", "0", "Switch 1 on", "CP1"+i); // Checks if switch 1 is on
	        	preLoader.callProcedure("CHECKPATH.insert", "CP1"+i, "Dummy "+i, ""); // Checkpath Button 1,2,3,4 gedrückt UNUSED
	        	preLoader.callProcedure("CHECKSTATE.insert", "CA"+i, 0, "CP1"+i);
	        	preLoader.callProcedure("MULTICHECK.insert", "MC5"+i, "AND", "0", "0", "Test 5", "CP1"+i);
	        	preLoader.callProcedure("MULTICHECK_CHECK_LINK.insert", "1000"+i, "MC1"+i, "C1"+i, "CP1"+i);
	        	System.out.println("MASSCHECK " + i);
	        	i = i + 1;
	        }
	        
	        int e = 0;
	        
	        while (e<2){
	        	preLoader.callProcedure("CHECK.insert", "CX"+e, "E1", "PR1", "1", "=", "0", "0", "Switch 1 on", "CP1"); // Checks if switch 1 is on
	        	preLoader.callProcedure("CHECKSTATE.insert", "CX"+e, 0, "CP1");
	        	System.out.println("MASS CP CHECK " + e);
	        	e = e + 1;
	        }
	        
	        /**/
	        
	        System.out.println("Checks loaded");
	        
	        preLoader.callProcedure("MULTICHECK.insert", "MC1", "AND", "0", "0", "Test 1", "CP1");
	        preLoader.callProcedure("MULTICHECK.insert", "MC2", "AND", "0", "0", "Test 2", "CP1");
	        preLoader.callProcedure("MULTICHECK.insert", "MC3", "AND", "0", "0", "Test 3", "CP1");
	        preLoader.callProcedure("MULTICHECK.insert", "MC4", "AND", "0", "0", "Test 4", "CP1");
	        preLoader.callProcedure("MULTICHECK.insert", "MC5", "AND", "0", "0", "Test 5", "CP1");
	        	        	        	 
	        System.out.println("Multichecks loaded");
	        
	        preLoader.callProcedure("MULTICHECK_CHECK_LINK.insert", "1000", "MC1", "C1", "CP1");
	        preLoader.callProcedure("MULTICHECK_CHECK_LINK.insert", "1001", "MC1", "C2", "CP1");
	        preLoader.callProcedure("MULTICHECK_CHECK_LINK.insert", "1002", "MC2", "C3", "CP1");
	        preLoader.callProcedure("MULTICHECK_CHECK_LINK.insert", "1003", "MC2", "C4", "CP1");
	        
	        
	        System.out.println("Multichecks and Checks linked");
	        
	        preLoader.callProcedure("MULTICHECK_MULTICHECK_LINK.insert", "1000", "MC3", "MC1", "CP1");
	        preLoader.callProcedure("MULTICHECK_MULTICHECK_LINK.insert", "1001", "MC3", "MC2", "CP1");
	        preLoader.callProcedure("MULTICHECK_MULTICHECK_LINK.insert", "1002", "MC4", "MC3", "CP1");
	        preLoader.callProcedure("MULTICHECK_MULTICHECK_LINK.insert", "1003", "MC4", "MC2", "CP1");
	        
	        System.out.println("Multichecks and Multichecks linked");
	        
        
	        preLoader.callProcedure("CHECKPATH.insert", "CP1", "Dummy 1", ""); // Checpath für alle Buttons
	        preLoader.callProcedure("CHECKPATH.insert", "CP2", "Dummy 2", ""); // Checkpath Messenger
	        preLoader.callProcedure("CHECKPATH.insert", "CP3", "Dummy 3", ""); // Checkpath Button 5 gedrückt UNUSED
	        preLoader.callProcedure("CHECKPATH.insert", "CP4", "Dummy 4", ""); // Checkpath Button 1,2,3,4 gedrückt UNUSED
	        preLoader.callProcedure("CHECKPATH.insert", "CP5", "Dummy 5", ""); // Checkpath Button 1,2,3,4 gedrückt UNUSED
	        preLoader.callProcedure("CHECKPATH.insert", "CP6", "Dummy 5", ""); // Checkpath Button 1,2,3,4 gedrückt UNUSED
	        
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
	        
	        preLoader.callProcedure("RULE.insert", "R1", "Button 1 gedrückt", "C1", "", "CP1"); 
	        preLoader.callProcedure("RULE.insert", "R2", "Button 1 und 2 gedrückt", "", "MC1", "CP1");
	        preLoader.callProcedure("RULE.insert", "R3", "Button 3 und 4 gedrückt", "", "MC2", "CP1");
	        preLoader.callProcedure("RULE.insert", "R4", "Button 1,2, 3 und 4 gedrückt", "", "MC3", "CP1");
	        preLoader.callProcedure("RULE.insert", "R5", "Chat von Thorsten", "C6", "", "CP2");	           
	        preLoader.callProcedure("RULE.insert", "R6", "Chat von Ute", "C7", "", "CP2");
	        
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
