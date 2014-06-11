package com.velisphere.tigerspice.client.analytics;

import java.io.IOException;
import java.util.LinkedList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.velisphere.tigerspice.shared.EndpointLogData;


	@RemoteServiceRelativePath("verticaAnalytics")
	public interface AnalyticsService extends RemoteService {
		LinkedList<EndpointLogData> getEndpointLog(String endpointID, String propertyID);
		String getEndpointLogAsFile(String endpointID, String propertyID) throws IOException;
	}

	
	
