package com.velisphere.tigerspice.client.analytics;

import java.io.IOException;
import java.util.LinkedList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.velisphere.tigerspice.shared.AnalyticsRawData;
import com.velisphere.tigerspice.shared.TableRowData;

	@RemoteServiceRelativePath("verticaAnalytics")
	public interface AnalyticsService extends RemoteService {
		LinkedList<AnalyticsRawData> getEndpointLog(String endpointID, String propertyID);
		LinkedList<AnalyticsRawData> getActionExecutedLog(String endpointID, String propertyID);
		String getEndpointLogAsFile(LinkedList<TableRowData> table) throws IOException;
		String getEndpointLogChartAsFile (String htmlData);
	}

	
	
