package com.velisphere.toucan.dataObjects;



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
