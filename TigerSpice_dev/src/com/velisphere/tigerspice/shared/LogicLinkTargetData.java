package com.velisphere.tigerspice.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class LogicLinkTargetData implements IsSerializable {

	private String targetEndpointID;
	private String checkpathID;
	private String checkpathName;
	
	
	
	public void setCheckpathID(String checkpathID){
		this.checkpathID = checkpathID;
	}
	
	public void setCheckpathName(String checkpathName){
		this.checkpathName = checkpathName;
	}
	
	
	public void setTargetEndpointID(String targetEndpointID){
		this.targetEndpointID = targetEndpointID;
	}
	
	public String getCheckpathID(){
		return this.checkpathID;
	}
	
	public String getCheckpathName(){
		return this.checkpathName;
	}
	
	public String getTargetEndpointID(){
		return this.targetEndpointID;
	}
}
