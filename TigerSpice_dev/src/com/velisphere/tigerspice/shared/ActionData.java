package com.velisphere.tigerspice.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ActionData implements IsSerializable {

	private String actionID;
	private String targetEndpointID;
	private String payload;
	private String timestamp;
	
	
	public void setActionID(String actionID){
		this.actionID = actionID;
	}
	
	public void setTargetEndpointID(String targetEndpointID){
		this.targetEndpointID = targetEndpointID;
	}
	
	public String getActionID(){
		return this.actionID;
	}
	
	public String getTargetEndpointID(){
		return this.targetEndpointID;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}
