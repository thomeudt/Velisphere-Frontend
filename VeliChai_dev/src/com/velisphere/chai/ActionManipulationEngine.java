package com.velisphere.chai;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import org.voltdb.VoltTable;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

public class ActionManipulationEngine {

	
	public static HashSet<String> getActionItems(String ruleID) throws NoConnectionsException, IOException, ProcCallException
	{
				
		HashSet<String> actionsFound = new HashSet<String>();
		final ClientResponse findActionsForRuleID = BusinessLogicEngine.montanaClient
				.callProcedure("AME_ActionsForRule", ruleID);
		
		final VoltTable findActionsForRuleIDResults[] = findActionsForRuleID
				.getResults();
		
		VoltTable actionsForRuleID = findActionsForRuleIDResults[0];
		// check if any rows have been returned
		while (actionsForRuleID.advanceRow()) {
			{
				actionsFound.add(actionsForRuleID.getString("ACTIONID"));
			}
		}
		
		return actionsFound;
	}
	
	public static void executeActionItems(String actionID, HashMap<String, String> inboundMessageMap) throws Exception
	{
				
		
		final ClientResponse findActionDetails = BusinessLogicEngine.montanaClient
				.callProcedure("AME_DetailsForAction", actionID);
		
		final VoltTable findActionDetailsResults[] = findActionDetails
				.getResults();
		
		VoltTable actionDetails = findActionDetailsResults[0];
		// check if any rows have been returned
		
		HashMap<String,String> outboundMessageMap = new HashMap<String, String>();
		while (actionDetails.advanceRow()) {
			{
				System.out.println(actionDetails.toFormattedString());
				outboundMessageMap.put(actionDetails.getString("OUTBOUNDPROPERTYID"), inboundMessageMap.get(actionDetails.getString("INBOUNDPROPERTYID")));
				System.out.println("Outboundmap: " + outboundMessageMap);
				Send.sendHashTable(outboundMessageMap, inboundMessageMap.get(actionDetails.getString("TGTEPIDFROMINBOUNDPROP")), inboundMessageMap.get("EPID"));

			}
		}
		
		
	}


}
