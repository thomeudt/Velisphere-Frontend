package com.velisphere.chai.engines;

import java.sql.Timestamp;
import java.util.UUID;

import org.voltdb.types.TimestampType;

// unused
public class LoggerEngine {

	public static void awriteEndpointLog(String EPID, String property, String entry) throws Exception {

		/*
		 * Write log to database.
		 */

		UUID entryID = UUID.randomUUID();
		String entryIDString = entryID.toString();

		// TODO: change to a meaningful log table!

		// TODO: Add entry that deletes log entries older than n days for this endpoint before writing new log
		

		//BusinessLogicEngine.montanaClient.callProcedure("LGE_InsertEndpointPropertyLog", EPID, entryIDString, property, entry);
		// now only logging to VeliSphere Mart
		
		
		//VelisphereMart.insertPropertyLog(entryIDString, EPID, property, entry);
		//BusinessLogicEngine.montanaClient.callProcedure("LGE_InsertEndpointPropertyLog", "A", "B", "C", "D", "E");
	}

	
	
}
