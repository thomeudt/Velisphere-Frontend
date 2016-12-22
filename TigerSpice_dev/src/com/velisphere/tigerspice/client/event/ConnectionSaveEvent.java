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

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.logic.draggables.PhysicalItem;

public class ConnectionSaveEvent extends GwtEvent<ConnectionSaveEventHandler> {


		public static Type<ConnectionSaveEventHandler> TYPE = new Type<ConnectionSaveEventHandler>();
		PhysicalItem source;
		PhysicalItem target;
		Widget connector;
		
		
		public ConnectionSaveEvent(PhysicalItem source, PhysicalItem target, Widget connector)
		{
			this.source = source;
			this.target = target;
			this.connector = connector;
			
		}
		
		  @Override
		public Type<ConnectionSaveEventHandler> getAssociatedType() {
		    return TYPE;
		}

		@Override
		protected void dispatch(ConnectionSaveEventHandler handler) {
		    handler.onConnectionSave(this);
		}
		
	
		
		public PhysicalItem getSource()
		{
			return this.source;
		}
		
		public PhysicalItem getTarget(){
			return this.target;
		}

		public Widget getConnector(){
			return this.connector;
		}

}
