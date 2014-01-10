package com.velisphere.tigerspice.client.helper;


import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.ResettableEventBus;
import com.google.gwt.event.shared.SimpleEventBus;









public class EventUtils {
	 
	public static EventBus EVENT_BUS = GWT.create(SimpleEventBus.class);
	
	
}
