package com.velisphere.toucan.amqp;

public interface EventListener {

	public void isAliveRequested();
	public void allPropertiesRequested();
	public void newInboundMessage(String message);
}
