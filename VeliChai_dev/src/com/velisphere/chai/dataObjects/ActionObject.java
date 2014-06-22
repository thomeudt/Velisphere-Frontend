package com.velisphere.chai.dataObjects;

public class ActionObject {

	String actionID;
	String sensorID;
	String actorID;
	String payload;
	String propertyID;
	
	public ActionObject(String actionID, String sensorID, String actorID, String payload, String propertyID){
		this.actionID = actionID;
		this.sensorID = sensorID;
		this.actorID = actorID;
		this.payload = payload;
		this.propertyID = propertyID;
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
	
	public String getPropertyID(){
		return propertyID;
	}
	
}
