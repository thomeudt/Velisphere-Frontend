package com.velisphere.tigerspice.client.analytics;

import java.io.IOException;
import java.util.LinkedList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.velisphere.tigerspice.shared.AnalyticsRawData;
import com.velisphere.tigerspice.shared.GeoLocationData;
import com.velisphere.tigerspice.shared.TableRowData;

	@RemoteServiceRelativePath("verticaAnalytics")
	public interface AnalyticsService extends RemoteService {
		LinkedList<AnalyticsRawData> getEndpointLog(String endpointID, String propertyID);
		String getLastEndpointLogTime(String endpointID);
		String getEndpointLogCount(String endpointID);
		String getActionLogCount(String endpointID);
		LinkedList<AnalyticsRawData> getActionExecutedLog(String endpointID, String propertyID);
		String getEndpointLogAsFile(LinkedList<TableRowData> table) throws IOException;
		String getEndpointLogChartAsFile (String htmlData);
		AnalyticsRawData getCurrentSensorState(String endpointID, String propertyID);
		AnalyticsRawData getCurrentActorState(String endpointID, String propertyID);
		String getActionNameForActionID(String actionID);
		LinkedList<GeoLocationData> getAllGeoLocations(String userID);
		LinkedList<GeoLocationData> getAllGeoLocationTrails(String userID);
	}

	
	
