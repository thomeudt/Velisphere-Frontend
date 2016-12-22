package ChatExample;

import java.util.ArrayList;
import java.util.List;

public class EventInitiator {
	
	    List<EventListener> listeners = new ArrayList<EventListener>();

	    public void addListener(EventListener toAdd) {
	        listeners.add(toAdd);
	    }

	    public void requestIsAlive() {
	        System.out.println(" [IN] Is Alive Requested");

	        // Notify
	        for (EventListener cl : listeners)
	            cl.isAliveRequested();
	    }
	    
	    public void requestAllProperties() {
	    	System.out.println(" [IN] All Properties Requested");

	        // Notify
	        for (EventListener cl : listeners)
	            cl.allPropertiesRequested();
	    }
	    
	    public void newInboundMessage(String message) {
	    	System.out.println(" [IN] New Inbound Message");

	        // Notify
	        for (EventListener cl : listeners)
	            cl.newInboundMessage(message);
	    }

	
}
