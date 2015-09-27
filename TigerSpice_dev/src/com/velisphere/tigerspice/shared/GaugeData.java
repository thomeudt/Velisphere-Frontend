package com.velisphere.tigerspice.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GaugeData implements IsSerializable {


	private String endpointID;
	private String propertyID;
	
	public String getEndpointID() {
		return endpointID;
	}
	public void setEndpointID(String endpointID) {
		this.endpointID = endpointID;
	}
	public String getPropertyID() {
		return propertyID;
	}
	public void setPropertyID(String propertyID) {
		this.propertyID = propertyID;
	}

}
