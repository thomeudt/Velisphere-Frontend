package com.velisphere.tigerspice.client.logic.layoutWidgets;

import java.util.Iterator;
import java.util.LinkedList;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.allen_sauer.gwt.dnd.client.util.WidgetLocation;
import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.constants.IconSize;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.velisphere.tigerspice.client.event.ConnectionSaveEvent;
import com.velisphere.tigerspice.client.event.ConnectionSaveEventHandler;
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


	static final int controlsOffsetY = 25;
	
	Context2d context;
	Canvas canvas;

	HandlerRegistration connectionSaveHandler;
	HandlerRegistration linkedInCanvasHandler;
	
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
		setConnectionSaveEventHandler();

		
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
		
		// add drag point only if it is a sensor
		
		if (current.getIsSensor() == 1) addDragPoint(propertyLabel);
		
		InCanvasLinkDropController inCanvasLinkDropController = new InCanvasLinkDropController(propertyLabel);
		linkDragController.registerDropController(inCanvasLinkDropController);


						
												
		
	}
	
	
	private void addDragPoint(CanvasLabel propertyLabel)
	{
		WidgetLocation widgetLocation = new WidgetLocation(propertyLabel, logicPanel);
		//final HTML link = new HTML("Manage");
		
		final LinkCreator link = new LinkCreator(propertyLabel);
		
		propertyLabel.setDragPointWidget(link);
		
		logicPanel.add(link, widgetLocation.getLeft(), widgetLocation.getTop()-controlsOffsetY);
		linkDragController.makeDraggable(link);
		
	}
	
	private void setLinkedInCanvasEventListener()
	{
		linkedInCanvasHandler = EventUtils.EVENT_BUS.addHandler(
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
						
						ConnectorDialogSensorActor connectorDialog = new ConnectorDialogSensorActor(linkedInCanvasEvent.getSource(), linkedInCanvasEvent.getTarget());
						connectorDialog.show();
						connectorDialog.setAutoHideEnabled(true);
						connectorDialog.center();
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
			 // change to red
			 context.setStrokeStyle(CssColor.make(255,0,0));

			 context.moveTo(sourceLocation.getLeft(), sourceLocation.getTop());
			 context.lineTo(targetLocation.getLeft(), targetLocation.getTop());
			 
			 context.stroke();

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
					
						// move dragPoint if it is a sensor
						
						if(draggedInCanvasEvent.getCanvasLabel().getIsSensor() == 1)
						{
							WidgetLocation newLocation = new WidgetLocation(draggedInCanvasEvent.getCanvasLabel(), logicPanel);
							draggedInCanvasEvent.getCanvasLabel().getDragPointWidget().removeFromParent();
							logicPanel.add(draggedInCanvasEvent.getCanvasLabel().getDragPointWidget(), newLocation.getLeft(), newLocation.getTop()-controlsOffsetY);
						}
						
						// re-draw the links
						
						drawLinks();
					
						
						
					}});
		
	}
	

	private void setConnectionSaveEventHandler()
	{
		connectionSaveHandler = EventUtils.EVENT_BUS.addHandler(
				ConnectionSaveEvent.TYPE, new ConnectionSaveEventHandler() {

					@Override
					public void onConnectionSave(
							ConnectionSaveEvent connectionSaveEvent) {
						// TODO Auto-generated method stub
						
						WidgetLocation sourceLocation = new WidgetLocation(connectionSaveEvent.getSource(), logicPanel);
						WidgetLocation targetLocation = new WidgetLocation(connectionSaveEvent.getTarget(), logicPanel);
						
						int xPos = (sourceLocation.getLeft() + targetLocation.getLeft()) / 2;
						int yPos = (sourceLocation.getTop() + targetLocation.getTop()) / 2;
						
						
						Icon icon = new Icon(IconType.ASTERISK);
						icon.setSize(IconSize.FOUR_TIMES);
						
						logicPanel.add(icon, xPos, yPos);
						
					}
				});
		
	}


}
