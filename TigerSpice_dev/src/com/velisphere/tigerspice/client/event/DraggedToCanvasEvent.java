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

public class DraggedToCanvasEvent extends GwtEvent<DraggedToCanvasEventHandler> {

		
		public static Type<DraggedToCanvasEventHandler> TYPE = new Type<DraggedToCanvasEventHandler>();
		
		DragContext context;
		int targetX;
		int targetY;
	
		public DraggedToCanvasEvent(DragContext context, int targetX, int targetY)
		{
			this.context = context;
			this.targetX = targetX;
			this.targetY = targetY;
			
		}
		
		@Override
		public Type<DraggedToCanvasEventHandler> getAssociatedType() {
		    return TYPE;
		}

		@Override
		protected void dispatch(DraggedToCanvasEventHandler handler) {
		    handler.onDraggedToCanvas(this);
		}
		
		public DragContext getContext()
		{
			return this.context;
		}
		
		public int getTargetX(){
			return this.targetX;
		}
		
		public int getTargetY(){
			return this.targetY;
		}
		
}
