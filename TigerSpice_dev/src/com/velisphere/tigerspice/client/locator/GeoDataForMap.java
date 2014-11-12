package com.velisphere.tigerspice.client.locator;

public class GeoDataForMap {

	private String endpointID;
	private String endpointName;
	private String lat;
	private String lon;
	
	public void setEndpointID(String endpointID){
		this.endpointID = endpointID;
	}
	
	public void setEndpointName(String endpointName){
		this.endpointName = endpointName;
	}
	
	public void setLat(String lat){
		this.lat = lat;
	}
	
	public void setLon(String lon){
		this.lon = lon;
	}
	
	public String getEndpointID()
	{
		return this.endpointID;
	}
	
	public String getEndpointName()
	{
		return this.endpointName;
	}
	
	public String getLat()
	{
		return this.lat;
	}
	
	public String getLon()
	{
		return this.lon;
	}
	
}
