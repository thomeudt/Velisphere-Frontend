package com.velisphere.tigerspice.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ActionObject  implements IsSerializable{

	public String actionID;
	public String sensorEndpointID;
	public String actionName;
	public String endpointName;
	public String endpointID;
	public String endpointClassID;
	public String propertyName;
	public String propertyID;
	public String valueFromInboundPropertyID;
	public String manualValue;
	public String validValueIndex;
	public String propertyIdIntake;
	
	public ActionObject() {
	}
	
	public ActionObject(String actionID, String actionName, String endpointName, String endpointID, String endpointClassID, String propertyName, String propertyID, String settingSourceIndex, String manualValue, String validValueIndex, String propertyIdIntake, String sensorEndpointID){

		this.actionID = actionID;
		this.actionName = actionName;
		this.sensorEndpointID = sensorEndpointID;
		this.endpointName = endpointName;
		this.endpointID = endpointID;
		this.endpointClassID = endpointClassID;
		this.propertyName = propertyName;
		this.propertyID = propertyID;
		this.valueFromInboundPropertyID = settingSourceIndex;
		this.manualValue = manualValue;
		this.validValueIndex = validValueIndex;
		this.propertyIdIntake = propertyIdIntake;
		
	}

}
