package com.velisphere.tigerspice.client.analytics;

import java.util.LinkedList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.velisphere.tigerspice.shared.ActionData;
import com.velisphere.tigerspice.shared.AnalyticsRawData;
import com.velisphere.tigerspice.shared.FileData;
import com.velisphere.tigerspice.shared.GeoLocationData;
import com.velisphere.tigerspice.shared.TableRowData;



public interface AnalyticsServiceAsync {
	void getEndpointLog(String EndpointID, String PropertyID, AsyncCallback<LinkedList<AnalyticsRawData>> callback);
	void getLastEndpointLogTime(String endpointID, AsyncCallback<String> callback);
	void getEndpointLogCount(String endpointID, AsyncCallback<String> callback);
	void getActionLogCount(String endpointID, AsyncCallback<String> callback);
	void getActionExecutedLog(String EndpointID, String PropertyID, AsyncCallback<LinkedList<AnalyticsRawData>> callback);
	void getEndpointLogAsFile(LinkedList<TableRowData> table, AsyncCallback<String> callback);
	void getEndpointLogChartAsFile(String htmlData, AsyncCallback<String> callback);
	void getCurrentSensorState(String endpointID, String propertyID, AsyncCallback<AnalyticsRawData> callback);
	void getCurrentActorState(String endpointID, String propertyID, AsyncCallback<AnalyticsRawData> callback);
	void getActionNameForActionID(String actionID, AsyncCallback<String> callback);
	void getAllGeoLocations(String userID, AsyncCallback<LinkedList<GeoLocationData>> callback);
	void getAllGeoLocationTrails(String userID, AsyncCallback<LinkedList<GeoLocationData>> callback);
	void getGeoLocationTrailSingleEndpoint(String userID, String endpointID, AsyncCallback<LinkedList<GeoLocationData>> callback);
	void getGeoLocationTrailSphere(String userID, String sphereID, AsyncCallback<LinkedList<GeoLocationData>> callback);
	void getGeoLocationSingleEndpoint(String userID, String endpointID, AsyncCallback<LinkedList<GeoLocationData>> callback);
	void getGeoLocationSphere(String userID, String sphereID, AsyncCallback<LinkedList<GeoLocationData>> callback);
	void getLastActionExecuted(String actionID, AsyncCallback<ActionData> callback);
	void getAllFileData(String endpointID, AsyncCallback<LinkedList<FileData>> callback);
}

