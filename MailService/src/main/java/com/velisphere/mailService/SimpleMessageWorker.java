package com.velisphere.mailService;


import com.velisphere.fs.sdk.CTLInitiator;
import com.velisphere.fs.sdk.Server;


public class SimpleMessageWorker 
{
    public static void main( String[] args )
    {
        System.out.println( " [MS] Starting VeliSphere Simple Mail Service v0.1" );
        
     // Activate Event Responders

     		CTLEventResponder eventResponder = new CTLEventResponder();
     		
     		CTLInitiator initiator = new CTLInitiator();
     		
     		initiator.addListener(eventResponder);
     		

     		// Start Server

     		Server.startServer(initiator);

        
    }
}
