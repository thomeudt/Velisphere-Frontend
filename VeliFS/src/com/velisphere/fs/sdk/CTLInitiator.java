package com.velisphere.fs.sdk;

import java.util.ArrayList;
import java.util.List;

public class CTLInitiator {
	
	    List<CTLListener> listeners = new ArrayList<CTLListener>();

	    public void addListener(CTLListener toAdd) {
	        listeners.add(toAdd);
	    }

	    public void requestIsAlive() {
	        System.out.println(" [IN] Is Alive Requested");

	        // Notify
	        for (CTLListener cl : listeners)
	            cl.isAliveRequested();
	    }
	    
	    public void requestAllProperties() {
	    	System.out.println(" [IN] All Properties Requested");

	        // Notify
	        for (CTLListener cl : listeners)
	            cl.allPropertiesRequested();
	    }

	
}
