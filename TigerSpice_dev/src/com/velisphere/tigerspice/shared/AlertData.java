package com.velisphere.tigerspice.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AlertData implements IsSerializable{
	
	
	String endpointID;
	String checkpathID;
	String alertName;
	String userID;
	String property;
	String operator;
	String threshold;
	String type;
	String recipient;
	String text;
	String alertID;
	
	public String getAlertID() {
		return alertID;
	}
	public void setAlertID(String alertID) {
		this.alertID = alertID;
	}
	public String getEndpointID() {
		return endpointID;
	}
	public void setEndpointID(String endpointID) {
		this.endpointID = endpointID;
	}
	public String getCheckpathID() {
		return checkpathID;
	}
	public void setCheckpathID(String checkpathID) {
		this.checkpathID = checkpathID;
	}
	public String getAlertName() {
		return alertName;
	}
	public void setAlertName(String alertName) {
		this.alertName = alertName;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getThreshold() {
		return threshold;
	}
	public void setThreshold(String threshold) {
		this.threshold = threshold;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRecipient() {
		return recipient;
	}
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

}
