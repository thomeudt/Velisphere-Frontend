package com.velisphere.toucan.dataObjects;



public class EndpointClassData
{
	public String endpointclassID;
	public String endpointclassName;
	public String endpointclassPath;
	public String endpointclassImageURL;
	public String vendorID;
		
	public String getName(){
		return endpointclassName;
	}
	
	public String getId(){
		return endpointclassID;
	}

	
	public String getVendorId(){
		return vendorID;
	}

	public String getPath(){
		return endpointclassPath;
	}
	
	public String getImageURL(){
		return endpointclassImageURL;
	}
	
	public void setName(String endpointclassName){
		this.endpointclassName = endpointclassName;
	}
	
	public void setPath(String endpointclassPath){
		this.endpointclassPath = endpointclassPath;
	}
	
	public void setImageURL( String endpointclassImage){
		this.endpointclassImageURL = endpointclassImage;
	}
	
	public void setVendorID( String vendorID){
		this.vendorID = vendorID;
	}
}
