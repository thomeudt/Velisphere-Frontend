package com.velisphere.tigerspice.client.logic.widgets;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.allen_sauer.gwt.dnd.client.util.DOMUtil;
import com.allen_sauer.gwt.dnd.client.util.WidgetLocation;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.velisphere.tigerspice.client.event.DraggedToCanvasEvent;
import com.velisphere.tigerspice.client.event.EventUtils;
import com.velisphere.tigerspice.client.event.FilterAppliedEvent;

public class CanvasDropController extends AbsolutePositionDropController {

	AbsolutePanel dropTarget;
	
	public CanvasDropController(AbsolutePanel dropTarget) {
		super(dropTarget);
		this.dropTarget = dropTarget;
	}
	
	@Override
	public void onDrop(DragContext context)
	{
		//super.onDrop(context);
		WidgetLocation dropTargetLocation = new WidgetLocation(dropTarget, null);
		int dropTargetOffsetX = dropTargetLocation.getLeft()
		        + DOMUtil.getBorderLeft(dropTarget.getElement());
		int dropTargetOffsetY = dropTargetLocation.getTop() + DOMUtil.getBorderTop(dropTarget.getElement());
		RootPanel.get().add(new HTML("IT WAS DROPPED at " + (context.desiredDraggableX - dropTargetOffsetX) + " / " + (context.desiredDraggableY - dropTargetOffsetY)));
		
		int targetX = context.desiredDraggableX - dropTargetOffsetX;
		int targetY = context.desiredDraggableY - dropTargetOffsetY;
		EventUtils.EVENT_BUS.fireEvent(new DraggedToCanvasEvent(context, targetX, targetY));	
	}

}
