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

public class InCanvasMoveDropController extends AbsolutePositionDropController {

	AbsolutePanel dropTarget;
	
	public InCanvasMoveDropController(AbsolutePanel dropTarget) {
		super(dropTarget);
		this.dropTarget = dropTarget;
	}
	
	@Override
	public void onDrop(DragContext context)
	{
		super.onDrop(context);
		
		RootPanel.get().add(new HTML("DropController says onDrop"));
		
		WidgetLocation dropTargetLocation = new WidgetLocation(dropTarget, null);
		int dropTargetOffsetX = dropTargetLocation.getLeft()
		        + DOMUtil.getBorderLeft(dropTarget.getElement());
		int dropTargetOffsetY = dropTargetLocation.getTop() + DOMUtil.getBorderTop(dropTarget.getElement());
		
		 CanvasLabel current = (CanvasLabel) context.selectedWidgets.get(0);
		
		RootPanel.get().add(new HTML(current.content + " WAS DROPPED at " + (context.desiredDraggableX - dropTargetOffsetX) + " / " + (context.desiredDraggableY - dropTargetOffsetY)));
		
			
	}
	
	

}