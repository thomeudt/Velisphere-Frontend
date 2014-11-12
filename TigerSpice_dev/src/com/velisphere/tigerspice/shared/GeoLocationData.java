package com.velisphere.tigerspice.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GeoLocationData implements IsSerializable{
	String userID;
	String endpointID;
	String endpointName;
	String endpointClassID;
	String propertyClassID;
	String value;
	
	
	public void setUserID(String userID){
		this.userID = userID;
	}
	
	public void setEndpointID(String endpointID){
		this.endpointID = endpointID;
	}
	
	public void setEndpointName(String endpointName){
		this.endpointName = endpointName;
	}
	
	public void setEndpointClassID(String endpointClassID){
		this.endpointClassID = endpointClassID;
	}
	
	public void setPropertyClassID(String propertyClassID){
		this.propertyClassID = propertyClassID;
	}
	
	public void setValue(String value){
		this.value = value;
	}
	
	public String getUserID(){
		return this.userID;
	}
	
	public String getEndpointID(){
		return this.endpointID;
	}
	
	public String getEndpointName(){
		return this.endpointName;
	}
	
	public String getEndpointClassID(){
		return this.endpointClassID;
	}
	
	public String getPropertyClassID(){
		return this.propertyClassID;
	}
	
	public String getValue(){
		return this.value;
	}
	
	
	
	
	
}
