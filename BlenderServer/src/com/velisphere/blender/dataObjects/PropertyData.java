package com.velisphere.blender.dataObjects;



public class PropertyData
{
	public String propertyId;
	public String propertyName;
	public String propertyclassName;
	public String propertyclassId;
	public String endpointclassId;
	public byte isActor;
	public byte isSensor;
	public byte isConfigurable;
	public byte status;
	public String unitOfMeasure;
	public String currentValue;
	public String lastUpdate;
	public String triggeredBySensor;
	public String processedByAction;

	
	public String getTriggeredBySensor() {
		return triggeredBySensor;
	}

	public void setTriggeredBySensor(String triggeredBySensor) {
		this.triggeredBySensor = triggeredBySensor;
	}

	public String getProcessedByAction() {
		return processedByAction;
	}

	public void setProcessedByAction(String processedByAction) {
		this.processedByAction = processedByAction;
	}

	
	
	public String getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(String currentValue) {
		this.currentValue = currentValue;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	
	public String getUnitOfMeasure() {
		return unitOfMeasure;
	}

	public void setUnitOfMeasure(String unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}

	public String getPropertyName(){
		return propertyName;
	}
	
	public String getPropertyId(){
		return propertyId;
	}
	
	public String getPropertyclassId(){
		return propertyclassId;
	}
	
	
	public String getEndpointclassId(){
		return endpointclassId;
	}
	
	public byte getIsConfigurable(){
		return isConfigurable;
	}
	
	public byte getIsActor(){
		return isActor;
	}
	
	public byte getIsSensor(){
		return isSensor;
	}
	
	public byte getStatus(){
		return status;
	}
	
	public String getPropertyclassName(){
		return propertyclassName;
	}
	 
	
}
