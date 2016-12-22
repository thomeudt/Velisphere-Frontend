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
package com.velisphere.tigerspice.client.event;

import com.google.common.eventbus.EventBus;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class JSONreadyEvent extends GwtEvent<JSONreadyEventHandler> {

		String json;
		
		public JSONreadyEvent() {
			super();
			
		}
	
		public JSONreadyEvent(String json) {
			super();
			this.json = json;
		}

		
		public String getJson() {
			return json;
		}


		public static Type<JSONreadyEventHandler> TYPE = new Type<JSONreadyEventHandler>();

		  @Override
		public Type<JSONreadyEventHandler> getAssociatedType() {
		    return TYPE;
		}

		@Override
		protected void dispatch(JSONreadyEventHandler handler) {
		    handler.onJSONready(this);
		}
}
