package com.velisphere.tigerspice.client.helper;

import com.google.common.eventbus.EventBus;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class SessionVerifiedEvent extends GwtEvent<SessionVerifiedEventHandler> {

		
		
	
		
		
		public static Type<SessionVerifiedEventHandler> TYPE = new Type<SessionVerifiedEventHandler>();

		  @Override
		public Type<SessionVerifiedEventHandler> getAssociatedType() {
		    return TYPE;
		}

		@Override
		protected void dispatch(SessionVerifiedEventHandler handler) {
		    handler.onSessionVerified(this);
		}
}