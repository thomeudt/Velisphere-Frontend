package PhidgetsExample;

import java.util.ArrayList;
import java.util.List;

import com.velisphere.milk.interfaces.EventInitiator;
import com.velisphere.milk.interfaces.EventListener;

public class PhidgetEventInitiator implements EventInitiator{
	
	    List<EventListener> listeners = new ArrayList<EventListener>();

	    
	    @Override
		public void addListener(
				EventListener toAdd) {

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
