package com.velisphere.fs;

import com.velisphere.milk.interfaces.EventInitiator;
import com.velisphere.milk.interfaces.EventListener;




public class FsEventInitiator implements EventInitiator {
	
	  
	    @Override
		public void addListener(EventListener toAdd) {
	        listeners.add(toAdd);
	    }

		@Override
		public void requestIsAlive() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void requestAllProperties() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void newInboundMessage(String message) {
			// TODO Auto-generated method stub
			System.out.println(" [IN] New Inbound Message");

	        // Notify
	        for (EventListener cl : listeners)
	            cl.newInboundMessage(message);

			
		}

		
}
