package com.velisphere.chai;

import java.sql.Timestamp;
import java.util.UUID;

import org.voltdb.types.TimestampType;


public class LoggerEngine {

	public static void writeEndpointLog(String EPID, String property, String entry) throws Exception {

		/*
		 * Write log to database.
		 */

		UUID entryID = UUID.randomUUID();
		String entryIDString = entryID.toString();

		// TODO: change to a meaningful log table!

		
		BusinessLogicEngine.montanaClient.callProcedure("LGE_InsertEndpointPropertyLog", entryIDString, EPID, property, entry, System.currentTimeMillis());
		
	}

	
	
}