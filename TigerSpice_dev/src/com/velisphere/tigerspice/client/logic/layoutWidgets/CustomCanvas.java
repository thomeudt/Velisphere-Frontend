package com.velisphere.tigerspice.client.logic.layoutWidgets;

import java.util.Iterator;
import java.util.LinkedList;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.allen_sauer.gwt.dnd.client.util.WidgetLocation;
import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.orange.links.client.event.TieLinkEvent;
import com.orange.links.client.event.TieLinkHandler;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.endpointclasses.EPCService;
import com.velisphere.tigerspice.client.endpointclasses.EPCServiceAsync;
import com.velisphere.tigerspice.client.event.DraggedInCanvasEvent;
import com.velisphere.tigerspice.client.event.DraggedInCanvasEventHandler;
import com.velisphere.tigerspice.client.event.DraggedToCanvasEvent;
import com.velisphere.tigerspice.client.event.DraggedToCanvasEventHandler;
import com.velisphere.tigerspice.client.event.EventUtils;
import com.velisphere.tigerspice.client.event.LinkedInCanvasEvent;
import com.velisphere.tigerspice.client.event.LinkedInCanvasEventHandler;
import com.velisphere.tigerspice.client.logic.controllers.InCanvasLinkDropController;
import com.velisphere.tigerspice.client.logic.controllers.InCanvasDragDropController;
import com.velisphere.tigerspice.client.logic.controllers.ListToCanvasDropController;
import com.velisphere.tigerspice.client.logic.controllers.LogicToCanvasDropController;
import com.velisphere.tigerspice.client.logic.draggables.CanvasLabel;
import com.velisphere.tigerspice.client.logic.draggables.ExplorerLabel;
import com.velisphere.tigerspice.client.logic.draggables.LinkCreator;
import com.velisphere.tigerspice.client.logic.draggables.LogicCheckAnd;
import com.velisphere.tigerspice.client.logic.draggables.LogicCheckOr;

public class CustomCanvas extends Composite {

	Context2d context;
	Canvas canvas;
	
	ListToCanvasDropController listToCanvasDropController;
	InCanvasDragDropController inCanvasMoveDropController;
	LogicToCanvasDropController logicToCanvasDropController;
	PickupDragController dragController;
	PickupDragController linkDragController;

	LinkedList<LinkedPair<CanvasLabel, CanvasLabel>> linkedPairs;
	
	AbsolutePanel logicPanel;
	
	public CustomCanvas()
	{
		
		linkedPairs = new LinkedList<LinkedPair<CanvasLabel, CanvasLabel>>();
		
		logicPanel = new AbsolutePanel();
		
		logicPanel.setWidth("100%");
		logicPanel.setHeight("400px");
		initWidget(logicPanel);
		
		dragController = new PickupDragController(logicPanel, false);
		linkDragController = new PickupDragController(logicPanel, false);
		listToCanvasDropController = new ListToCanvasDropController(logicPanel);
		logicToCanvasDropController = new LogicToCanvasDropController(logicPanel);
		
		// call all event listeners
		setDraggedToCanvasEventListener();
		setLinkedInCanvasEventListener();
		setDraggedInCanvasEventListener();

		
		logicPanel.addAttachHandler(new AttachEvent.Handler() {
			
			@Override
			public void onAttachOrDetach(AttachEvent event) {
				// TODO Auto-generated method stub
			
				if (event.isAttached())
				{
					canvas = Canvas.createIfSupported();
					logicPanel.add(canvas);
					
					
					canvas.setWidth("100%");
					canvas.setHeight("100%");
					RootPanel.get().add(new HTML ("WIDTH OFFSET " + logicPanel.getOffsetWidth()));
					canvas.setCoordinateSpaceWidth(logicPanel.getOffsetWidth());
					canvas.setCoordinateSpaceHeight(400);

				
								
					
					canvas.addStyleName("wellsilver");
					
					logicPanel.add(new HTML("<b>Logic Canvas</b> Drag sensors and actors here to build your logic."), 5, 5);
					
					context = canvas.getContext2d();
				
					/*
					context.setFillStyle(CssColor.make("grey"));
					context.beginPath();
					context.setLineWidth(5);
					context.arc(50, 50,35, 0, 320);
					context.closePath();
					context.fill();
					*/
					

					
				}
				
			}
		});
		
	}

	public DropController getListToCanvasDropController()
	{
		
		return this.listToCanvasDropController;
	}
	
	public DropController getLogicToCanvasDropController()
	{
		
		return this.logicToCanvasDropController;
	}
	
	
	private void setDraggedToCanvasEventListener() {
		
		inCanvasMoveDropController = new InCanvasDragDropController(logicPanel);
		dragController.registerDropController(inCanvasMoveDropController);
		
		HandlerRegistration draggedToCanvasHandler;
		draggedToCanvasHandler = EventUtils.EVENT_BUS.addHandler(
				DraggedToCanvasEvent.TYPE, new DraggedToCanvasEventHandler() {

					@Override
					public void onDraggedToCanvas(
							DraggedToCanvasEvent draggedToCanvasEvent) {

						
						// TODO Auto-generated method stub

						
						if (draggedToCanvasEvent.getContext().selectedWidgets.get(0) instanceof ExplorerLabel)
						{
							addCanvasLabel(draggedToCanvasEvent);
						} else 
							if (draggedToCanvasEvent.getContext().selectedWidgets.get(0) instanceof LogicCheckAnd)
							{
								// do something
								
								LogicCheckAnd logicCheckAnd = (LogicCheckAnd) draggedToCanvasEvent.getContext().selectedWidgets.get(0);
								
								logicPanel.add(logicCheckAnd,
										draggedToCanvasEvent.getTargetX(),
										draggedToCanvasEvent.getTargetY());
								
								
								dragController.makeDraggable(logicCheckAnd);
								
								
							}
							else 
								if (draggedToCanvasEvent.getContext().selectedWidgets.get(0) instanceof LogicCheckOr)
								{
									// do something
									
									LogicCheckOr logicCheckOr = (LogicCheckOr) draggedToCanvasEvent.getContext().selectedWidgets.get(0);
									
									logicPanel.add(logicCheckOr,
											draggedToCanvasEvent.getTargetX(),
											draggedToCanvasEvent.getTargetY());
									
									
									dragController.makeDraggable(logicCheckOr);
									
								}
							

		                
						
						 
					}
					

				});

	}

	
	
	private void addCanvasLabel(DraggedToCanvasEvent draggedToCanvasEvent)
	{
		
		final ExplorerLabel current = (ExplorerLabel) draggedToCanvasEvent
				.getContext().selectedWidgets.get(0);

		RootPanel.get().add(
				new HTML("***** EVENT: dropped "
						+ current.getText() + " at X:"
						+ draggedToCanvasEvent.getTargetX()
						+ " Y:"
						+ draggedToCanvasEvent.getTargetY()
						+ " Sensor: " + current.getIsSensor()));

		
		final CanvasLabel propertyLabel = new CanvasLabel(current.getText(), current.getEndpointName(), current.getPropertyID(), current.getEndpointID(), current.getEndpointClassID(), current.getPropertyClassID(), current.getIsSensor(), current.getIsActor());
		
		logicPanel.add(propertyLabel,
				draggedToCanvasEvent.getTargetX(),
				draggedToCanvasEvent.getTargetY());
		
		
		
		
		dragController.makeDraggable(propertyLabel);
		
		addDragPoint(propertyLabel);
		InCanvasLinkDropController inCanvasLinkDropController = new InCanvasLinkDropController(propertyLabel);
		linkDragController.registerDropController(inCanvasLinkDropController);


						
												
		
	}
	
	
	private void addDragPoint(CanvasLabel propertyLabel)
	{
		WidgetLocation widgetLocation = new WidgetLocation(propertyLabel, logicPanel);
		//final HTML link = new HTML("Manage");
		
		final LinkCreator link = new LinkCreator(propertyLabel);
		
		propertyLabel.setDragPointWidget(link);
		
		logicPanel.add(link, widgetLocation.getLeft(), widgetLocation.getTop()-15);
		linkDragController.makeDraggable(link);
		
	}
	
	private void setLinkedInCanvasEventListener()
	{
		HandlerRegistration linkedInCanvasHandler = EventUtils.EVENT_BUS.addHandler(
				LinkedInCanvasEvent.TYPE, new LinkedInCanvasEventHandler() {

					@Override
					public void onLinkedInCanvas(
							LinkedInCanvasEvent linkedInCanvasEvent) {
						// TODO Auto-generated method stub
						
						RootPanel.get().add(new HTML("LINKED IN FIRED"));
						
						linkedPairs.add(new LinkedPair<CanvasLabel, CanvasLabel>(linkedInCanvasEvent.getSource(), linkedInCanvasEvent.getTarget()));
						
						// re draw the links
						
						drawLinks();
						
						// add new drag point to source label to allow further links
						
						addDragPoint(linkedInCanvasEvent.getSource());
						
					}});
		
	}
	
	private void drawLinks()
	{
	
		context.clearRect(0, 0, canvas.getCoordinateSpaceWidth(), canvas.getCoordinateSpaceHeight());
		
		Iterator<LinkedPair<CanvasLabel, CanvasLabel>> it = linkedPairs.iterator();
		while (it.hasNext())
		{
			LinkedPair<CanvasLabel, CanvasLabel> labels = it.next();
			WidgetLocation sourceLocation = new WidgetLocation(labels.getLeft(), logicPanel);
			WidgetLocation targetLocation = new WidgetLocation(labels.getRight(), logicPanel);
		
			
			context.beginPath();
			//context.setFillStyle(CssColor.make("blue"));
			context.moveTo(sourceLocation.getLeft(), sourceLocation.getTop());
			context.lineTo(targetLocation.getLeft(), targetLocation.getTop());
			context.lineTo(targetLocation.getLeft()+1, targetLocation.getTop()+1);
			context.lineTo(sourceLocation.getLeft()+1, sourceLocation.getTop()+1);
			context.fill();
			context.closePath();
			
			RootPanel.get().add(new HTML("LINE FROM " + sourceLocation.getLeft() + " TO " + targetLocation.getLeft()));

			
		}
	
	}
	
	
	private void setDraggedInCanvasEventListener()
	{
		HandlerRegistration draggedInCanvasHandler = EventUtils.EVENT_BUS.addHandler(
				DraggedInCanvasEvent.TYPE, new DraggedInCanvasEventHandler() {

					@Override
					public void onDraggedInCanvas(
							DraggedInCanvasEvent draggedInCanvasEvent) {
						// TODO Auto-generated method stub
						
						RootPanel.get().add(new HTML("DRAGGED FIRED"));
					
						WidgetLocation newLocation = new WidgetLocation(draggedInCanvasEvent.getCurrentLabel(), logicPanel);
						draggedInCanvasEvent.getCurrentLabel().getDragPointWidget().removeFromParent();
						logicPanel.add(draggedInCanvasEvent.getCurrentLabel().getDragPointWidget(), newLocation.getLeft(), newLocation.getTop()-15);
						
						// re-draw the links
						
						drawLinks();
					
						
						
					}});
		
	}
	
	

}
