package com.velisphere.tigerspice.client.propertyclasses;

import java.util.Vector;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.PropertyClassData;

@RemoteServiceRelativePath("voltPropertyClass")
public interface PropertyClassService extends RemoteService {

	PropertyClassData getPropertyClassForPropertyClassID(String propertyClassID);

}
