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
import com.github.gwtbootstrap.client.ui.Button;
import com.google.common.eventbus.EventBus;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.logic.draggables.DraggableButton;
import com.velisphere.tigerspice.client.logic.draggables.LinkCreator;
import com.velisphere.tigerspice.client.logic.draggables.LogicCheckAnd;
import com.velisphere.tigerspice.client.logic.draggables.LogicCheckOr;
import com.velisphere.tigerspice.client.logic.draggables.PhysicalItem;

public class DraggedInCanvasEvent extends GwtEvent<DraggedInCanvasEventHandler> {

		
		public static Type<DraggedInCanvasEventHandler> TYPE = new Type<DraggedInCanvasEventHandler>();
		
		DragContext context;
		PhysicalItem canvasLabel;
		LogicCheckAnd logicCheckAnd;
		LogicCheckOr logicCheckOr;
		DraggableButton button;
		
		
		public DraggedInCanvasEvent(DragContext context, PhysicalItem canvasLabel)
		{
			this.canvasLabel = canvasLabel;
			this.context = context;
		}
		
		public DraggedInCanvasEvent(DragContext context, LogicCheckAnd logicCheckAnd) {
			this.logicCheckAnd = logicCheckAnd;
			this.context = context;
		}
		
		public DraggedInCanvasEvent(DragContext context, LogicCheckOr logicCheckOr) {
			this.logicCheckOr = logicCheckOr;
			this.context = context;
		}

		public DraggedInCanvasEvent(DragContext context, DraggableButton button) {
			this.button = button;
			this.context = context;
		}
		
		@Override
		public Type<DraggedInCanvasEventHandler> getAssociatedType() {
		    return TYPE;
		}

		@Override
		protected void dispatch(DraggedInCanvasEventHandler handler) {
		    handler.onDraggedInCanvas(this);
		}
		
		public PhysicalItem getCanvasLabel()
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
		
		public DraggableButton getButton()
		{
			return this.button;
		}
		
		public DragContext getContext()
		{
			return this.context;
		}
		

		
		
}
