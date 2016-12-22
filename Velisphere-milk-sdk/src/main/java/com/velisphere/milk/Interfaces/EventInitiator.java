package com.velisphere.milk.Interfaces;

import java.util.ArrayList;
import java.util.List;

public interface EventInitiator {

    List<EventListener> listeners = new ArrayList<EventListener>();

    public void addListener(EventListener toAdd);
    public void requestIsAlive();
    public void requestAllProperties();    
    public void newInboundMessage(String message);

	
}
