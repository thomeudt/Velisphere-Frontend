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
import com.velisphere.tigerspice.client.logic.draggables.CanvasLabel;
import com.velisphere.tigerspice.client.logic.draggables.LinkCreator;
import com.velisphere.tigerspice.client.logic.draggables.LogicCheckAnd;
import com.velisphere.tigerspice.client.logic.draggables.LogicCheckOr;

public class DraggedInCanvasEvent extends GwtEvent<DraggedInCanvasEventHandler> {

		
		public static Type<DraggedInCanvasEventHandler> TYPE = new Type<DraggedInCanvasEventHandler>();
		
		CanvasLabel canvasLabel;
		LogicCheckAnd logicCheckAnd;
		LogicCheckOr logicCheckOr;
		
		
		public DraggedInCanvasEvent(CanvasLabel canvasLabel)
		{
			this.canvasLabel = canvasLabel;
	
			
		}
		
		public DraggedInCanvasEvent(LogicCheckAnd logicCheckAnd) {
			this.logicCheckAnd = logicCheckAnd;
		}
		
		public DraggedInCanvasEvent(LogicCheckOr logicCheckOr) {
			this.logicCheckOr = logicCheckOr;
		}

		@Override
		public Type<DraggedInCanvasEventHandler> getAssociatedType() {
		    return TYPE;
		}

		@Override
		protected void dispatch(DraggedInCanvasEventHandler handler) {
		    handler.onDraggedInCanvas(this);
		}
		
		public CanvasLabel getCanvasLabel()
		{
			return this.canvasLabel;
		}
		
		public LogicCheckAnd getLogicCheckAnd()
		{
			return this.logicCheckAnd;
		}
		
		public LogicCheckOr getLogicCheckOr()
		{
			return this.logicCheckOr;
		}
		
		
		
}
