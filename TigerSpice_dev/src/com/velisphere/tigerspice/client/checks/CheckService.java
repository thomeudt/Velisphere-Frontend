package com.velisphere.tigerspice.client.checks;

import java.util.Vector;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.velisphere.tigerspice.shared.CheckData;
import com.velisphere.tigerspice.shared.EndpointData;


@RemoteServiceRelativePath("voltCheck")
public interface CheckService extends RemoteService {
		Vector<CheckData> getChecksForEndpointID(String endpointID);
		String addNewCheck(String endpointID, String propertyID, String checkValue, String operator, String name);
		String updateCheck(String checkID, String name, String checkValue, String operator);
		String deleteCheck(String checkID);
		Vector<CheckData> getChecksForUserID(String userID);

}
