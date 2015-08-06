package com.velisphere.blender.dataObjects;



public class EndpointData
{
	public String endpointId;
	public String endpointName;
	public String endpointclassId;
	public String endpointProvDate;
	public String endpointState;
	
	
	public String getEndpointState() {
		return endpointState;
	}

	public void setEndpointState(String endpointState) {
		this.endpointState = endpointState;
	}

	public String getId(){
		return endpointId;
	}
	
	public String getName(){
		return endpointName;
	}
	
	public String getEpcId(){
		return endpointclassId;
	}
	
	public String getEndpointProvDate(){
		return endpointProvDate;
	}
	
	public void setId(String endpointID){
		this.endpointId = endpointID;
	}
	
	public void setName(String endpointName){
		this.endpointName = endpointName;
	}
	
	public void  setEpcId(String endpointclassId){
		this.endpointclassId = endpointclassId;
	}
	
	public void  setEndpointProvDate(String endpointProvDate){
		this.endpointProvDate = endpointProvDate;
	}
}
