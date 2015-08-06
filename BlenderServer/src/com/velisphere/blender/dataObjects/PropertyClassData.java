package com.velisphere.blender.dataObjects;



public class PropertyClassData
{
	public String propertyClassId;
	public String propertyClassName;
	public String propertyClassDatatype;
	public String propertyClassUnit;
	
	
	public String getId(){
		return propertyClassId;
	}
	
	public String getName(){
		return propertyClassName;
	}
	
	public String getDatatype(){
		return propertyClassDatatype;
	}
	
	public String getUnit(){
		return propertyClassUnit;
	}
	

}
