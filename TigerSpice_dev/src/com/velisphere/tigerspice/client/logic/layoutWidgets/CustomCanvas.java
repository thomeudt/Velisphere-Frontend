package com.velisphere.tigerspice.client.logic.layoutWidgets;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.allen_sauer.gwt.dnd.client.util.WidgetLocation;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.constants.IconSize;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.event.ConnectionSaveEvent;
import com.velisphere.tigerspice.client.event.ConnectionSaveEventHandler;
import com.velisphere.tigerspice.client.event.DraggedInCanvasEvent;
import com.velisphere.tigerspice.client.event.DraggedInCanvasEventHandler;
import com.velisphere.tigerspice.client.event.DraggedToCanvasEvent;
import com.velisphere.tigerspice.client.event.DraggedToCanvasEventHandler;
import com.velisphere.tigerspice.client.event.EventUtils;
import com.velisphere.tigerspice.client.event.LinkedInCanvasEvent;
import com.velisphere.tigerspice.client.event.LinkedInCanvasEventHandler;
import com.velisphere.tigerspice.client.logic.connectors.ConnectorSensorActor;
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
	HashMap<LinkedPair<CanvasLabel, CanvasLabel>, Widget> linkedPairConnectorMap;
	
	AbsolutePanel logicPanel;
	
	public CustomCanvas()
	{
		
		linkedPairs = new LinkedList<LinkedPair<CanvasLabel, CanvasLabel>>();
		linkedPairConnectorMap = new HashMap<LinkedPair<CanvasLabel, CanvasLabel>, Widget>();
		
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
				
					
				}
				
			}
		});
		
	}
	
	public void onUnload()
	{
		linkedInCanvasHandler.removeHandler();
		super.onUnload();
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
								
								addLogicCheckAnd(draggedToCanvasEvent);
								
								
							}
							else 
								if (draggedToCanvasEvent.getContext().selectedWidgets.get(0) instanceof LogicCheckOr)
								{

									addLogicCheckOr(draggedToCanvasEvent);
									
								}
							

		                
						
						 
					}
					

				});

	}

	
	
	private void addLogicCheckAnd(DraggedToCanvasEvent draggedToCanvasEvent)
	{
		

		draggedToCanvasEvent.getContext().selectedWidgets.get(0).removeFromParent();
		
		LogicCheckAnd logicCheckAnd = new LogicCheckAnd();
		
		logicPanel.add(logicCheckAnd,
				draggedToCanvasEvent.getTargetX(),
				draggedToCanvasEvent.getTargetY());
		
		
		dragController.makeDraggable(logicCheckAnd);
		
		InCanvasLinkDropController inCanvasLinkDropController = new InCanvasLinkDropController(logicCheckAnd);
	
		linkDragController.registerDropController(inCanvasLinkDropController);
	}

	private void addLogicCheckOr(DraggedToCanvasEvent draggedToCanvasEvent)
	{
		

		draggedToCanvasEvent.getContext().selectedWidgets.get(0).removeFromParent();
		
		LogicCheckOr logicCheckOr = new LogicCheckOr();
		
		logicPanel.add(logicCheckOr,
				draggedToCanvasEvent.getTargetX(),
				draggedToCanvasEvent.getTargetY());
		
		
		dragController.makeDraggable(logicCheckOr);
		
		InCanvasLinkDropController inCanvasLinkDropController = new InCanvasLinkDropController(logicCheckOr);
	
		linkDragController.registerDropController(inCanvasLinkDropController);
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
						
						LinkedPair<CanvasLabel, CanvasLabel> linkedPair = new LinkedPair<CanvasLabel, CanvasLabel>(linkedInCanvasEvent.getSource(), linkedInCanvasEvent.getTarget());
						linkedPairs.add(linkedPair);
						
						// get the line color
						
						String lineColor = linkedPair.getLeft().getDragPointWidget().getCurrentColor();
						
						// add new drag point to source label to allow further links
						
						addDragPoint(linkedInCanvasEvent.getSource());
						
						// add a connector and open the connector settings dialog
						
						final ConnectorSensorActor connector = new ConnectorSensorActor(linkedInCanvasEvent.getSource(), linkedInCanvasEvent.getTarget());
						linkedPairConnectorMap.put(linkedPair, connector);
						
						//connector.show();
						connector.setAutoHideEnabled(true);
						connector.center();
						
						
						// add button to open dialog and position on connection line 
						
						WidgetLocation sourceLocation = new WidgetLocation(linkedPair.getLeft(), logicPanel);
						WidgetLocation targetLocation = new WidgetLocation(linkedPair.getRight(), logicPanel);
						int xPos = (sourceLocation.getLeft() + targetLocation.getLeft()) / 2;
						int yPos = (sourceLocation.getTop() + targetLocation.getTop()) / 2;
						logicPanel.add(connector.getOpenerWidget(), xPos, yPos);
						connector.getOpenerWidget().addClickHandler(new ClickHandler(){

							@Override
							public void onClick(ClickEvent event) {
								// TODO Auto-generated method stub
								connector.show();
							}
							
						});

					
						
						// re draw the links
						
						drawLinks(lineColor);
						
						
					}});
		
	}
	
	private void drawLinks(String lineColor)
	{
	
		context.clearRect(0, 0, canvas.getCoordinateSpaceWidth(), canvas.getCoordinateSpaceHeight());
		
		Iterator<LinkedPair<CanvasLabel, CanvasLabel>> it = linkedPairs.iterator();
		while (it.hasNext())
		{
			LinkedPair<CanvasLabel, CanvasLabel> labels = it.next();
			
			LineCoordinateCalculator coordinates = new LineCoordinateCalculator(labels.getLeft(),labels.getRight(), logicPanel); 
			
			context.beginPath();
			
			// change to red
			
			 context.setStrokeStyle(CssColor.make(lineColor));

			 context.moveTo(coordinates.getCalcSourceX(), coordinates.getCalcSourceY());
			 context.lineTo(coordinates.getCalcTargetX(), coordinates.getCalcTargetY());
			
			 context.stroke();
			 context.closePath();
			 
			 drawArrow(coordinates, lineColor);
			positionConnectors(labels, coordinates);
			

			
		}
	
	}
	
	private void drawArrow(LineCoordinateCalculator coordinates, String lineColor)
	{
		
		double angle = Math.PI/8;
		
		// calculate the angle of the line
		double lineangle=Math.atan2(coordinates.getCalcTargetY() - coordinates.getCalcSourceY(),coordinates.getCalcTargetX() - coordinates.getCalcSourceX());
		// h is the line length of a side of the arrow head
		double h=Math.abs(10/Math.cos(angle));
		
		double angle1=lineangle+Math.PI+angle;
		double topx=coordinates.getCalcTargetX()+Math.cos(angle1)*h;
		double topy=coordinates.getCalcTargetY()+Math.sin(angle1)*h;
		
		double angle2=lineangle+Math.PI-angle;
		double botx=coordinates.getCalcTargetX()+Math.cos(angle2)*h;
		double boty=coordinates.getCalcTargetY()+Math.sin(angle2)*h;
		  
		 context.beginPath();
		 
		 context.setStrokeStyle(CssColor.make("lineColor"));

		  context.save();
		  context.beginPath();
		  context.moveTo(topx,topy);
		  context.lineTo(coordinates.getCalcTargetX(), coordinates.getCalcTargetY());
		  context.stroke();
		  context.lineTo(botx,boty);
		  context.stroke();
		  context.restore();
		
		 
		 context.closePath();
		 
		
	}
	
	private void positionConnectors(LinkedPair<CanvasLabel, CanvasLabel> currentPair, LineCoordinateCalculator coordinates)
	{
		if(linkedPairConnectorMap.containsKey(currentPair))
		{
			final ConnectorSensorActor currentConnector = (ConnectorSensorActor) linkedPairConnectorMap.get(currentPair);
			int xPos = (coordinates.getCalcSourceX() + coordinates.getCalcTargetX()) / 2;
			int yPos = (coordinates.getCalcSourceY() + coordinates.getCalcTargetY()) / 2;
			logicPanel.remove(currentConnector.getOpenerWidget());
			logicPanel.add(currentConnector.getOpenerWidget(), xPos, yPos);
			currentConnector.getOpenerWidget().addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub
					currentConnector.show();
				}
				
			});

			RootPanel.get().add(new HTML("Positioning opener Button for " + currentPair.getLeft().getContentRepresentation() + " > " + currentPair.getRight().getContentRepresentation()));
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
						
						// first check if this is a physical property or logic check
						
						if (draggedInCanvasEvent.getContext().selectedWidgets.get(0) instanceof CanvasLabel)
						{
							// move dragPoint if it is a sensor
							
							if(draggedInCanvasEvent.getCanvasLabel().getIsSensor() == 1)
							{
								WidgetLocation newLocation = new WidgetLocation(draggedInCanvasEvent.getCanvasLabel(), logicPanel);
								draggedInCanvasEvent.getCanvasLabel().getDragPointWidget().removeFromParent();
								logicPanel.add(draggedInCanvasEvent.getCanvasLabel().getDragPointWidget(), newLocation.getLeft(), newLocation.getTop()-controlsOffsetY);
							}
						}
							
							
					
						
						
						
						// re-draw the links
						
						drawLinks("cornflowerblue");
					
						
						
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
						
						
						
						
						
						
					}
				});
		
	}


}
