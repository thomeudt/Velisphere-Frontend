package com.velisphere.mailService;


import com.velisphere.fs.sdk.CTLInitiator;
import com.velisphere.fs.sdk.Server;


public class SimpleMessageWorker 
{
    public static void main( String[] args )
    {
        System.out.println( "Starting VeliSphere Simple Mail Service" );
        
     // Activate Event Responders

     		CTLEventResponder eventResponder = new CTLEventResponder();
     		
     		CTLInitiator initiator = new CTLInitiator();
     		
     		initiator.addListener(eventResponder);
     		

     		// Start Server

     		Server.startServer(initiator);

        
    }
}
