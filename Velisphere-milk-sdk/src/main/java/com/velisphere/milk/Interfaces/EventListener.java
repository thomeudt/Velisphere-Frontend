package com.velisphere.milk.Interfaces;

public interface EventListener {

	public void isAliveRequested();
	public void allPropertiesRequested();
	public void newInboundMessage(String message);
	
}