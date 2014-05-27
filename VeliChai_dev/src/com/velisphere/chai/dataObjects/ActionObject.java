package com.velisphere.chai.dataObjects;

public class ActionObject {

	String actionID;
	String sensorID;
	String actorID;
	String payload;
	
	public ActionObject(String actionID, String sensorID, String actorID, String payload){
		this.actionID = actionID;
		this.sensorID = sensorID;
		this.actorID = actorID;
		this.payload = payload;
	}
	
	public String getActionID(){
		return actionID;
	}
	
	public String getSensorID(){
		return sensorID;
	}
	
	public String getActorID(){
		return actorID;
	}
	
	public String getPayload(){
		return payload;
	}
}
