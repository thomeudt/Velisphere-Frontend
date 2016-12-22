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
import com.velisphere.tigerspice.client.event.LinkedInCanvasL2PEvent;
import com.velisphere.tigerspice.client.event.LinkedInCanvasP2LEvent;
import com.velisphere.tigerspice.client.event.LinkedInCanvasP2PEvent;
import com.velisphere.tigerspice.client.logic.draggables.LogicCheck;
import com.velisphere.tigerspice.client.logic.draggables.LogicCheckAnd;
import com.velisphere.tigerspice.client.logic.draggables.LogicCheckOr;
import com.velisphere.tigerspice.client.logic.draggables.LinkCreator;
import com.velisphere.tigerspice.client.logic.draggables.PhysicalItem;
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
	
	PhysicalItem phyicalDropTarget;
	LogicCheck logicDropTarget;
	
	public InCanvasLinkDropController(PhysicalItem physicalDropTarget) {
		super(physicalDropTarget);
		this.phyicalDropTarget = physicalDropTarget;
	}
	
	public InCanvasLinkDropController(LogicCheck logicDropTarget) {
		super(logicDropTarget);
		this.logicDropTarget = logicDropTarget;
	}
	
	
	@Override
	public void onPreviewDrop(DragContext context) throws VetoDragException
	{
		
		
		if (phyicalDropTarget != null)
		{
			if (phyicalDropTarget.getIsSensor() == 1) {
			      throw new VetoDragException();
			    }
			
		}
		
		LinkCreator linkCreator = (LinkCreator) context.draggable;
	    if(linkCreator.isLogic())
		{
			LogicCheck logicCheck = (LogicCheck) linkCreator.getSource();
			
			if (logicCheck.getSourceCount() == 0)
			{
				  throw new VetoDragException();
			}
		}
		
	}
	
	
	@Override
	public void onDrop(DragContext context)
	{
	
		if (phyicalDropTarget != null)
		{
			if (phyicalDropTarget.getIsActor() == 1)
			{
				super.onDrop(context);
				LinkCreator linkCreator = (LinkCreator) context.draggable;
			    if(linkCreator.isLogic())
			    {
			    	EventUtils.RESETTABLE_EVENT_BUS.fireEvent(new LinkedInCanvasL2PEvent((LogicCheck) linkCreator.getSource(), phyicalDropTarget));
			    }
			    else
			    {
			    	EventUtils.RESETTABLE_EVENT_BUS.fireEvent(new LinkedInCanvasP2PEvent((PhysicalItem) linkCreator.getSource(), phyicalDropTarget));	
			    }
				
				
			}
			
		}
		
		if (logicDropTarget != null)
		{
			super.onDrop(context);
			
			
			LinkCreator linkCreator = (LinkCreator) context.draggable;
			 EventUtils.RESETTABLE_EVENT_BUS.fireEvent(new LinkedInCanvasP2LEvent((PhysicalItem) linkCreator.getSource(), logicDropTarget));
			
		}
		
		
		
		
		
			
			
	}
	
	

}
	

