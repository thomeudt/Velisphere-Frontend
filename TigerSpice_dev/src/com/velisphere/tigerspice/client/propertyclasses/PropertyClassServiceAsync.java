package com.velisphere.tigerspice.client.propertyclasses;

import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.PropertyClassData;

public interface PropertyClassServiceAsync {
	
	void getPropertyClassForPropertyClassID(String propertyClassID, AsyncCallback<PropertyClassData> callback);
	
	
}
