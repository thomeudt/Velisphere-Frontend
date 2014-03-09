/*******************************************************************************
 * CONFIDENTIAL INFORMATION
 *  __________________
 *  
 *   Copyright (C) 2014 Thorsten Meudt / Connected Things Lab
 *   All Rights Reserved.
 *  
 *  NOTICE:  All information contained herein is, and remains
 *  the property of Thorsten Meudt and its suppliers,
 *  if any.  The intellectual and technical concepts contained
 *  herein are proprietary to Thorsten Meudt
 *  and its suppliers and may be covered by Patents,
 *  patents in process, and are protected by trade secret or copyright law.
 *  Dissemination of this information or reproduction of this material
 *  is strictly forbidden unless prior written permission is obtained
 *  from Thorsten Meudt.
 ******************************************************************************/
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
