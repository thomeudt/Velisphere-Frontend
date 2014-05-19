/*******************************************************************************
 * CONFIDENTIAL INFORMATION
 *  __________________
 *  
 *   Copyright (C) 2014 Thorsten Meudt / Connected Things Lab
 *   All Rights Reserved.
 *  
 *  NOTICE:  All information contained herein is, and remains
 *  the property of Thorsten Meudt and its suppliers,
 *  if any.  The intellectual and technical concepts contained
 *  herein are proprietary to Thorsten Meudt
 *  and its suppliers and may be covered by Patents,
 *  patents in process, and are protected by trade secret or copyright law.
 *  Dissemination of this information or reproduction of this material
 *  is strictly forbidden unless prior written permission is obtained
 *  from Thorsten Meudt.
 ******************************************************************************/
package com.velisphere.tigerspice.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.UUID;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mindrot.BCrypt;
import org.voltdb.VoltTable;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.ClientStats;
import org.voltdb.client.ClientStatsContext;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.velisphere.tigerspice.server.ServerParameters;
import com.velisphere.tigerspice.client.helper.HelperService;
import com.velisphere.tigerspice.client.users.LoginService;
import com.velisphere.tigerspice.shared.EPCData;
import com.velisphere.tigerspice.shared.MontanaStatsData;
import com.velisphere.tigerspice.shared.UserData;

@SuppressWarnings("serial")
public class HelperServiceImpl extends RemoteServiceServlet implements HelperService
{
   
	static ClientStatsContext montanaStatsContext;
	
	public String autoConfMontana()
	{
		
		Properties props = new Properties();
		InputStream is = null;

		// First try loading from the current directory
		try {
			File f = new File("montanaconf.xml");
			is = new FileInputStream( f );
		}
		catch ( Exception e ) { is = null; }

		try {
			if ( is == null ) {
				// Try loading from classpath
				is = getClass().getResourceAsStream("montanaconf.xml");
			}

			// Try loading properties from the file (if found)
			props.loadFromXML( is );
		}
		catch ( Exception e ) { }

	
		ServerParameters.volt_ip = props.getProperty("Volt IP");

		System.out.println(" [IN] Reading Configuration");
		System.out.println(" [IN] Selected VoltDB: "+ ServerParameters.volt_ip);
		
		
		VoltConnector voltCon = new VoltConnector();
    	
		try {
			voltCon.openDatabase();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		montanaStatsContext = voltCon.montanaClient.createStatsContext();
				
		return montanaStatsContext.toString();
		
	}
 
    @Override
    public MontanaStatsData getMontanaStats()
    {
                //validate username and password
                 
                //store the user/session id
    	
    	MontanaStatsData stats = new MontanaStatsData();
    	VoltConnector voltCon = new VoltConnector();

		try {
			voltCon.openDatabase();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
	
		
			ClientResponse findCheck;
			try {
				findCheck = voltCon.montanaClient
						.callProcedure("@Statistics",
				                  "IOSTATS",1);
				
				final VoltTable findCheckResults[] = findCheck.getResults();

				VoltTable result = findCheckResults[0];
				// check if any rows have been returned

				result.advanceRow();
					
						// extract the value in column checkid
						
						stats.bytesRead = result.getLong("BYTES_READ");
						stats.bytesWritten = result.getLong("BYTES_WRITTEN");
						stats.connectionHostname = result.getString("CONNECTION_HOSTNAME");
						stats.connectionId = result.getLong("CONNECTION_ID");
						stats.hostId = result.getLong("HOST_ID");
						stats.hostname = result.getString("HOSTNAME");
						stats.messagesRead = result.getLong("MESSAGES_READ");
						stats.messagesWritten = result.getLong("MESSAGES_WRITTEN");
						stats.timestamp = result.getLong("TIMESTAMP");
						stats.IP = ServerParameters.volt_ip;
						
						
				
			} catch (IOException | ProcCallException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			
					
				
		

		try {
			voltCon.closeDatabase();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

				 
        return stats;
    }
 
     
}
