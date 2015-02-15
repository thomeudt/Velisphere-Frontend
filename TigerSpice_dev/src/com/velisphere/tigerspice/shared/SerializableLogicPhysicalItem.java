package com.velisphere.tigerspice.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SerializableLogicPhysicalItem  implements IsSerializable {

	String id;
	String endpointName;
    String content;	
    String propertyID;
	String propertyClassID;
  	String endpointClassID;
  	String endpointID;
  	byte isActor;
  	byte isSensor;
  	int xPos;
  	int yPos;
  	
  
	public String getEndpointName() {
		return endpointName;
	}
	public void setEndpointName(String endpointName) {
		this.endpointName = endpointName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPropertyID() {
		return propertyID;
	}
	public void setPropertyID(String propertyID) {
		this.propertyID = propertyID;
	}
	public String getPropertyClassID() {
		return propertyClassID;
	}
	public void setPropertyClassID(String propertyClassID) {
		this.propertyClassID = propertyClassID;
	}
	public String getEndpointClassID() {
		return endpointClassID;
	}
	public void setEndpointClassID(String endpointClassID) {
		this.endpointClassID = endpointClassID;
	}
	public String getEndpointID() {
		return endpointID;
	}
	public void setEndpointID(String endpointID) {
		this.endpointID = endpointID;
	}
	public byte getIsActor() {
		return isActor;
	}
	public void setIsActor(byte isActor) {
		this.isActor = isActor;
	}
	public byte getIsSensor() {
		return isSensor;
	}
	public void setIsSensor(byte isSensor) {
		this.isSensor = isSensor;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public int getxPos() {
		return xPos;
	}
	public void setxPos(int xPos) {
		this.xPos = xPos;
	}
	public int getyPos() {
		return yPos;
	}
	public void setyPos(int yPos) {
		this.yPos = yPos;
	}
	
}
