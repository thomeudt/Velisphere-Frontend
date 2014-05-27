package com.velisphere.chai.dataObjects;

public class MulticheckObject {

	String multicheckID;
	String checkValue;
	boolean hit;
	
	
	public MulticheckObject(String multicheckID, String checkValue, boolean hit){
		this.multicheckID = multicheckID;
		this.checkValue = checkValue;
		this.hit = hit;
	
	}
	
	public String getMulticheckID(){
		return this.multicheckID;
	}
	
	public String getCheckValue(){
		return this.checkValue;
	}
	
	public boolean getHit(){
		return this.hit;
	}
	
	
}
