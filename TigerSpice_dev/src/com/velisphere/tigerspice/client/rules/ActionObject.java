package com.velisphere.tigerspice.client.rules;

public class ActionObject {

	public String actionID;
	public String sensorEndpointID;
	public String actionName;
	public String endpointName;
	public String endpointID;
	public String endpointClassID;
	public String propertyName;
	public String propertyID;
	public int settingSourceIndex;
	public String manualValue;
	public int validValueIndex;
	public int propertyIdIndex;
	
	
	public ActionObject(String actionID, String actionName, String endpointName, String endpointID, String endpointClassID, String propertyName, String propertyID, int settingSourceIndex, String manualValue, int validValueIndex, int propertyIdIndex, String sensorEndpointID){

		this.actionID = actionID;
		this.actionName = actionName;
		this.sensorEndpointID = sensorEndpointID;
		this.endpointName = endpointName;
		this.endpointID = endpointID;
		this.endpointClassID = endpointClassID;
		this.propertyName = propertyName;
		this.propertyID = propertyID;
		this.settingSourceIndex = settingSourceIndex;
		this.manualValue = manualValue;
		this.validValueIndex = validValueIndex;
		this.propertyIdIndex = propertyIdIndex;
		
	}

}
