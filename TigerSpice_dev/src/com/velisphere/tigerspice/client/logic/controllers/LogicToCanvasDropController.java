package com.velisphere.tigerspice.client.logic.controllers;

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
import com.velisphere.tigerspice.client.logic.draggables.ExplorerLabel;
import com.velisphere.tigerspice.client.logic.draggables.LogicCheckAnd;
import com.velisphere.tigerspice.client.logic.draggables.LogicCheckOr;

public class LogicToCanvasDropController extends AbsolutePositionDropController {

	AbsolutePanel dropTarget;

	public LogicToCanvasDropController(AbsolutePanel dropTarget) {
		super(dropTarget);
		this.dropTarget = dropTarget;
	}

	@Override
	public void onDrop(DragContext context) {
		// super.onDrop(context);
		RootPanel.get().add(new HTML("DropController says onDrop"));

		WidgetLocation dropTargetLocation = new WidgetLocation(dropTarget, null);
		int dropTargetOffsetX = dropTargetLocation.getLeft()
				+ DOMUtil.getBorderLeft(dropTarget.getElement());
		int dropTargetOffsetY = dropTargetLocation.getTop()
				+ DOMUtil.getBorderTop(dropTarget.getElement());


		int targetX = context.desiredDraggableX - dropTargetOffsetX;
		int targetY = context.desiredDraggableY - dropTargetOffsetY;
		
		if (context.draggable.getClass().getName() == "com.velisphere.tigerspice.client.logic.draggables.LogicCheckAnd")
		{
			LogicDragController logicDragController = (LogicDragController) context.dragController;
			logicDragController.getExplorer().addLogicCheckAnd();
		}
		
		if (context.draggable.getClass().getName() == "com.velisphere.tigerspice.client.logic.draggables.LogicCheckOr")
		{
			LogicDragController logicDragController = (LogicDragController) context.dragController;
			logicDragController.getExplorer().addLogicCheckOr();
		}
			
		
		EventUtils.EVENT_BUS.fireEvent(new DraggedToCanvasEvent(context,
				targetX, targetY));

	}

}
