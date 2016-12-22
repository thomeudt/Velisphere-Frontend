package com.velisphere.tigerspice.server;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashSet;

import org.voltdb.VoltTable;
import org.voltdb.client.ClientFactory;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import com.velisphere.tigerspice.client.GreetingService;
import com.velisphere.tigerspice.shared.FieldVerifier;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {

	public static org.voltdb.client.Client montanaClient;
	
	public String greetServer(String input) throws IllegalArgumentException {
        // Verify that the input is valid. 
        if (!FieldVerifier.isValidName(input)) {
                // If the input is not valid, throw an IllegalArgumentException back to
                // the client.
                throw new IllegalArgumentException(
                                "Name must be at least 4 characters long");
        }

        String serverInfo = getServletContext().getServerInfo();
        String userAgent = getThreadLocalRequest().getHeader("User-Agent");

        // Escape data from the client to avoid cross-site script vulnerabilities.
        input = escapeHtml(input);
        userAgent = escapeHtml(userAgent);

        try {
			lookupRulesForCheckID("C1");
		} catch (NoConnectionsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProcCallException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return "Hello, " + input + "!<br><br>I am running " + serverInfo
                        + ".<br><br>It looks like you are using:<br>" + userAgent;
}
	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}
	
	
	
	public static void openDatabase() throws UnknownHostException, IOException {
		/*
		 * Instantiate a client and connect to the database.
		 */

		montanaClient = ClientFactory.createClient();
		montanaClient.createConnection(ServerParameters.volt_ip);
		System.out.println(" [IN] Connected to VoltDB on address: "
		+ ServerParameters.volt_ip);
	}
	
	
	private HashSet<String> lookupRulesForCheckID(String checkID)
			throws NoConnectionsException, IOException, ProcCallException {

		openDatabase();
		
		HashSet<String> ruleIDs = new HashSet<String>();

		final ClientResponse findRulesForCheckIDResponse = montanaClient
				.callProcedure("FindRulesForCheckID", checkID);
		if (findRulesForCheckIDResponse.getStatus() != ClientResponse.SUCCESS) {
			System.err.println(findRulesForCheckIDResponse.getStatusString());
		}
		final VoltTable findRulesForCheckIDResults[] = findRulesForCheckIDResponse
				.getResults();
		if (findRulesForCheckIDResults.length == 0) {
			// System.out.printf("Not valid match found!\n");
		}

		VoltTable result = findRulesForCheckIDResults[0];
		// check if any rows have been returned
		while (result.advanceRow()) {
			{

				// extract the value in column checkid
				ruleIDs.add(result.getString("RULEID"));
				// System.out.println(result.getString("RULEID"));
			}
		}

		return ruleIDs;
	}
	
	
}
