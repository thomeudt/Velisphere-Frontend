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
import com.velisphere.tigerspice.client.logic.widgets.draggables.CanvasLabel;
import com.velisphere.tigerspice.client.logic.widgets.draggables.LinkCreator;

public class LinkedInCanvasEvent extends GwtEvent<LinkedInCanvasEventHandler> {

		
		public static Type<LinkedInCanvasEventHandler> TYPE = new Type<LinkedInCanvasEventHandler>();
		
		CanvasLabel source;
		CanvasLabel target;
		
		public LinkedInCanvasEvent(CanvasLabel source, CanvasLabel target)
		{
			this.source = source;
			this.target = target;
			
		}
		
		@Override
		public Type<LinkedInCanvasEventHandler> getAssociatedType() {
		    return TYPE;
		}

		@Override
		protected void dispatch(LinkedInCanvasEventHandler handler) {
		    handler.onLinkedInCanvas(this);
		}
		
		public CanvasLabel getSource()
		{
			return this.source;
		}
		
		public CanvasLabel getTarget(){
			return this.target;
		}
		
		
}
