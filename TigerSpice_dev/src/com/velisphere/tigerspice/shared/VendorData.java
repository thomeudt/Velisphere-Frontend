package com.velisphere.tigerspice.shared;

import java.util.List;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Image;

public class VendorData implements IsSerializable
{
	public String vendorID;
	public String vendorName;
	public String vendorPath;
	public String vendorImageURL;
		
	public String getName(){
		return vendorName;
	}
	
	public String getId(){
		return vendorID;
	}
	
	public String getPath(){
		return vendorPath;
	}
	
	public String getImageURL(){
		return vendorImageURL;
	}
	
	public void setName(String vendorName){
		this.vendorName = vendorName;
	}
	
	public void setPath(String vendorPath){
		this.vendorPath = vendorPath;
	}
	
	public void setImageURL( String vendorImage){
		this.vendorImageURL = vendorImage;
	}
}
