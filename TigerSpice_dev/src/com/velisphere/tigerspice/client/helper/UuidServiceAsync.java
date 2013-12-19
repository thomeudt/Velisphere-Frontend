package com.velisphere.tigerspice.client.helper;

import java.util.HashSet;
import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.velisphere.tigerspice.shared.EPCData;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.PropertyData;
import com.velisphere.tigerspice.shared.UserData;

public interface UuidServiceAsync {
	void getUuid(AsyncCallback<String> callback);
	
	
}
