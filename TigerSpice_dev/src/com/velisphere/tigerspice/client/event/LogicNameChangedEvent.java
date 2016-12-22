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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.velisphere.tigerspice.client.logic.draggables.PhysicalItem;

public class LogicNameChangedEvent extends GwtEvent<LogicNameChangedEventHandler> {

	String name;	
	
		public static Type<LogicNameChangedEventHandler> TYPE = new Type<LogicNameChangedEventHandler>();
		
		public LogicNameChangedEvent(String name)
		{
			this.name = name;
			
		}

		  @Override
		public Type<LogicNameChangedEventHandler> getAssociatedType() {
		    return TYPE;
		}

		@Override
		protected void dispatch(LogicNameChangedEventHandler handler) {
		    handler.onLogicNameChanged(this);
		}

		public String getName() {
			return name;
		}
		
		
}
