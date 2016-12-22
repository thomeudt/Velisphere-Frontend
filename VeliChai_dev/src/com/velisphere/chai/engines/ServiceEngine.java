package com.velisphere.chai.engines;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.voltdb.VoltTable;
import org.voltdb.client.ClientResponse;

import com.velisphere.chai.broker.Send;
import com.velisphere.chai.dataObjects.ActionObject;

public class ServiceEngine {
	
	
	public static void setEndpointState(String endpointID, String endpointState) throws Exception
	{
				
		ClientResponse bleUpdateCheckState = BusinessLogicEngine.montanaClient
				.callProcedure("SRV_UpdateEndpointState", endpointID, endpointState);
		
		System.out.println(bleUpdateCheckState.getStatus());
				
	}

	

}
