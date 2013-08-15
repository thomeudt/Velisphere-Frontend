package com.velisphere.chai;

import org.hsqldb_voltpatches.Types;
import org.voltdb.*;
import org.voltdb.client.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

public class ImdbLog {

    public void writeLog(String exchangeName, String message, String queueName, String routingKey) throws Exception {

        /*
         * Instantiate a client and connect to the database.
         */
        org.voltdb.client.Client montanaClient;
        montanaClient = ClientFactory.createClient();
        montanaClient.createConnection("16.1.1.149");

        /*
         * Write log to database.
         */
        
                
            
   	    UUID identifier = UUID.randomUUID();
   	    String identifierString = identifier.toString();
        montanaClient.callProcedure("Insert", exchangeName, message, queueName, routingKey, identifierString);
        

    }
}