package com.velisphere.tigerspice.client.logic.controllers;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.allen_sauer.gwt.dnd.client.util.DOMUtil;
import com.allen_sauer.gwt.dnd.client.util.WidgetLocation;
import com.github.gwtbootstrap.client.ui.Button;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.velisphere.tigerspice.client.event.DraggedInCanvasEvent;
import com.velisphere.tigerspice.client.event.DraggedToCanvasEvent;
import com.velisphere.tigerspice.client.event.EventUtils;
import com.velisphere.tigerspice.client.event.LinkedInCanvasP2PEvent;
import com.velisphere.tigerspice.client.logic.draggables.DraggableButton;
import com.velisphere.tigerspice.client.logic.draggables.LogicCheckAnd;
import com.velisphere.tigerspice.client.logic.draggables.LogicCheckOr;
import com.velisphere.tigerspice.client.logic.draggables.PhysicalItem;

public class InCanvasDragDropController extends AbsolutePositionDropController {

	AbsolutePanel dropTarget;
	
	public InCanvasDragDropController(AbsolutePanel dropTarget) {
		super(dropTarget);
		this.dropTarget = dropTarget;
	}
	
	@Override
	public void onDrop(DragContext context)
	{
		super.onDrop(context);
		
		
		WidgetLocation dropTargetLocation = new WidgetLocation(dropTarget, null);
		int dropTargetOffsetX = dropTargetLocation.getLeft()
		        + DOMUtil.getBorderLeft(dropTarget.getElement());
		int dropTargetOffsetY = dropTargetLocation.getTop() + DOMUtil.getBorderTop(dropTarget.getElement());
		
		
		if (context.selectedWidgets.get(0).getClass() == PhysicalItem.class)
		{
			PhysicalItem canvasLabel = (PhysicalItem) context.selectedWidgets.get(0);
			EventUtils.RESETTABLE_EVENT_BUS.fireEvent(new DraggedInCanvasEvent(context, canvasLabel));
		}
		else if (context.selectedWidgets.get(0).getClass() == LogicCheckAnd.class)
		{
			LogicCheckAnd logicCheckAnd = (LogicCheckAnd) context.selectedWidgets.get(0);
			EventUtils.RESETTABLE_EVENT_BUS.fireEvent(new DraggedInCanvasEvent(context, logicCheckAnd));
		}
		else if (context.selectedWidgets.get(0).getClass() == LogicCheckOr.class)
		{
			LogicCheckOr logicCheckOr = (LogicCheckOr) context.selectedWidgets.get(0);
			EventUtils.RESETTABLE_EVENT_BUS.fireEvent(new DraggedInCanvasEvent(context, logicCheckOr));
		}
		else if (context.selectedWidgets.get(0).getClass() == DraggableButton.class)
		{
			DraggableButton button = (DraggableButton) context.selectedWidgets.get(0);
			EventUtils.RESETTABLE_EVENT_BUS.fireEvent(new DraggedInCanvasEvent(context, button));
		}
		
		
		
		
			
	}
	
	

}