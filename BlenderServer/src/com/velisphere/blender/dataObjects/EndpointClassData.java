package com.velisphere.blender.dataObjects;



public class EndpointClassData
{
	public String endpointclassID;
	public String endpointclassName;
	public String endpointclassPath;
	public String endpointclassImageURL;
	public String vendorID;
		
	public String getEndpointclassName(){
		return endpointclassName;
	}
	
	
	
	public String getVendorID(){
		return vendorID;
	}

	public String getEndpointclassPath(){
		return endpointclassPath;
	}
	
	public String getEndpointclassImageURL(){
		return endpointclassImageURL;
	}
	
	public void setName(String endpointclassName){
		this.endpointclassName = endpointclassName;
	}
	
	public void setEndpointclassPath(String endpointclassPath){
		this.endpointclassPath = endpointclassPath;
	}
	
	public void setEndpointclassImageURL( String endpointclassImage){
		this.endpointclassImageURL = endpointclassImage;
	}
	
	public void setVendorID( String vendorID){
		this.vendorID = vendorID;
	}
}
