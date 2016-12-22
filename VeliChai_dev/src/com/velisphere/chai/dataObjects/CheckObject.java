package com.velisphere.chai.dataObjects;

public class CheckObject {

	String checkID;
	String checkValue;
	boolean hit;
	
	
	public CheckObject(String checkID, String checkValue, boolean hit){
		this.checkID = checkID;
		this.checkValue = checkValue;
		this.hit = hit;
	
	}
	
	public String getCheckID(){
		return this.checkID;
	}
	
	public String getCheckValue(){
		return this.checkValue;
	}
	
	public boolean getHit(){
		return this.hit;
	}
	
	
}
