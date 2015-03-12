package com.velisphere.tigerspice.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ActionData implements IsSerializable {

	private String actionID;
	private String targetEndpointID;
	
	
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
}
