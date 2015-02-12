package com.velisphere.tigerspice.client.logic.controllers;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.allen_sauer.gwt.dnd.client.drop.AbstractPositioningDropController;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;
import com.allen_sauer.gwt.dnd.client.util.DOMUtil;
import com.allen_sauer.gwt.dnd.client.util.WidgetLocation;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.velisphere.tigerspice.client.event.DraggedToCanvasEvent;
import com.velisphere.tigerspice.client.event.EventUtils;
import com.velisphere.tigerspice.client.event.LinkedInCanvasP2LEvent;
import com.velisphere.tigerspice.client.event.LinkedInCanvasP2PEvent;
import com.velisphere.tigerspice.client.logic.draggables.LogicCheck;
import com.velisphere.tigerspice.client.logic.draggables.PhysicalItem;
import com.velisphere.tigerspice.client.logic.draggables.LinkCreator;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.util.DOMUtil;
import com.allen_sauer.gwt.dnd.client.util.DragClientBundle;
import com.allen_sauer.gwt.dnd.client.util.WidgetLocation;

import java.util.ArrayList;

/**
 * A {@link DropController} which allows a draggable widget to be placed at specific (absolute)
 * locations on an {@link com.google.gwt.user.client.ui.AbsolutePanel} drop target.
 */
public class InCanvasLinkDropController extends SimpleDropController {
	
	PhysicalItem dropTarget;
	LogicCheck genericDropTarget;
	
	public InCanvasLinkDropController(PhysicalItem dropTarget) {
		super(dropTarget);
		this.dropTarget = dropTarget;
	}
	
	public InCanvasLinkDropController(LogicCheck genericDropTarget) {
		super(genericDropTarget);
		this.genericDropTarget = genericDropTarget;
	}
	
	
	@Override
	public void onPreviewDrop(DragContext context) throws VetoDragException
	{
		
		
		if (dropTarget != null)
		{
			if (dropTarget.getIsSensor() == 1) {
			      throw new VetoDragException();
			    }
			
		}
		
	}
	
	
	@Override
	public void onDrop(DragContext context)
	{
	
		if (dropTarget != null)
		{
			if (dropTarget.getIsActor() == 1)
			{
				super.onDrop(context);
				RootPanel.get().add(new HTML("DropController says onDrop"));
				LinkCreator linkCreator = (LinkCreator) context.draggable;
			    RootPanel.get().add(new HTML(linkCreator.getSource().getContentRepresentation() + " was dropped on " + dropTarget.getContentRepresentation()));
			    EventUtils.EVENT_BUS.fireEvent(new LinkedInCanvasP2PEvent(linkCreator.getSource(), dropTarget));
			}
			
		}
		
		if (genericDropTarget != null)
		{
			super.onDrop(context);
			RootPanel.get().add(new HTML("DropController says onGENERICDrop"));
			LinkCreator linkCreator = (LinkCreator) context.draggable;
			 EventUtils.EVENT_BUS.fireEvent(new LinkedInCanvasP2LEvent(linkCreator.getSource(), genericDropTarget));
			
		}
		
		
		
		
		
			
			
	}
	
	

}
	

