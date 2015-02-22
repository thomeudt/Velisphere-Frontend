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

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.google.common.eventbus.EventBus;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.logic.draggables.LogicCheck;
import com.velisphere.tigerspice.client.logic.draggables.LinkCreator;
import com.velisphere.tigerspice.client.logic.draggables.PhysicalItem;

public class LinkedInCanvasP2LEvent extends GwtEvent<LinkedInCanvasP2LEventHandler> {

		
		public static Type<LinkedInCanvasP2LEventHandler> TYPE = new Type<LinkedInCanvasP2LEventHandler>();
		
		PhysicalItem source;
		LogicCheck target;
		
		public LinkedInCanvasP2LEvent(PhysicalItem source, LogicCheck target)
		{
			this.source = source;
			this.target = target;
			
		}
		
		@Override
		public Type<LinkedInCanvasP2LEventHandler> getAssociatedType() {
		    return TYPE;
		}

		@Override
		protected void dispatch(LinkedInCanvasP2LEventHandler handler) {
		    handler.onLinkedInCanvas(this);
		}
		
		public PhysicalItem getSource()
		{
			return this.source;
		}
		
		public LogicCheck getTarget(){
			return this.target;
		}
		
		
}