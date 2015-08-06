package com.velisphere.blender.dataObjects;



public class VendorData
{
	public String vendorID;
	public String vendorName;
	public String vendorPath;
	public String vendorImageURL;
		
	public String getName(){
		return vendorName;
	}
	
	public String getVendorID(){
		return vendorID;
	}
	
	public String getVendorPath(){
		return vendorPath;
	}
	
	public String getVendorImageURL(){
		return vendorImageURL;
	}
	
	public void setVendorName(String vendorName){
		this.vendorName = vendorName;
	}
	
	public void setVendorPath(String vendorPath){
		this.vendorPath = vendorPath;
	}
	
	public void setVendorImageURL( String vendorImage){
		this.vendorImageURL = vendorImage;
	}
	
}
