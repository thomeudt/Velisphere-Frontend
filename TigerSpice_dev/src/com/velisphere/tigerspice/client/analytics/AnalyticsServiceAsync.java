package com.velisphere.tigerspice.client.analytics;

import java.util.LinkedList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.velisphere.tigerspice.shared.AnalyticsRawData;
import com.velisphere.tigerspice.shared.TableRowData;



public interface AnalyticsServiceAsync {
	void getEndpointLog(String EndpointID, String PropertyID, AsyncCallback<LinkedList<AnalyticsRawData>> callback);
	void getActionExecutedLog(String EndpointID, String PropertyID, AsyncCallback<LinkedList<AnalyticsRawData>> callback);
	void getEndpointLogAsFile(LinkedList<TableRowData> table, AsyncCallback<String> callback);
	void getEndpointLogChartAsFile(String htmlData, AsyncCallback<String> callback);
}

