package com.velisphere.tigerspice.client.logic.layoutWidgets;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.allen_sauer.gwt.dnd.client.util.WidgetLocation;
import com.github.gwtbootstrap.client.ui.Button;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.event.DraggedInCanvasEvent;
import com.velisphere.tigerspice.client.event.DraggedInCanvasEventHandler;
import com.velisphere.tigerspice.client.event.DraggedToCanvasEvent;
import com.velisphere.tigerspice.client.event.DraggedToCanvasEventHandler;
import com.velisphere.tigerspice.client.event.EventUtils;
import com.velisphere.tigerspice.client.event.LinkedInCanvasL2PEvent;
import com.velisphere.tigerspice.client.event.LinkedInCanvasL2PEventHandler;
import com.velisphere.tigerspice.client.event.LinkedInCanvasP2LEvent;
import com.velisphere.tigerspice.client.event.LinkedInCanvasP2LEventHandler;
import com.velisphere.tigerspice.client.event.LinkedInCanvasP2PEvent;
import com.velisphere.tigerspice.client.event.LinkedInCanvasP2PEventHandler;
import com.velisphere.tigerspice.client.event.LogicNameChangedEvent;
import com.velisphere.tigerspice.client.helper.UuidService;
import com.velisphere.tigerspice.client.helper.UuidServiceAsync;
import com.velisphere.tigerspice.client.logic.DataManager;
import com.velisphere.tigerspice.client.logic.connectors.ConnectorLogicCheckActor;
import com.velisphere.tigerspice.client.logic.connectors.ConnectorSensorActor;
import com.velisphere.tigerspice.client.logic.connectors.ConnectorSensorLogicCheck;
import com.velisphere.tigerspice.client.logic.controllers.InCanvasLinkDropController;
import com.velisphere.tigerspice.client.logic.controllers.InCanvasDragDropController;
import com.velisphere.tigerspice.client.logic.controllers.ListToCanvasDropController;
import com.velisphere.tigerspice.client.logic.controllers.LogicToCanvasDropController;
import com.velisphere.tigerspice.client.logic.draggables.DraggableButton;
import com.velisphere.tigerspice.client.logic.draggables.LogicCheck;
import com.velisphere.tigerspice.client.logic.draggables.ExplorerLabel;
import com.velisphere.tigerspice.client.logic.draggables.LinkCreator;
import com.velisphere.tigerspice.client.logic.draggables.LogicCheckAnd;
import com.velisphere.tigerspice.client.logic.draggables.LogicCheckOr;
import com.velisphere.tigerspice.client.logic.draggables.PhysicalItem;
import com.velisphere.tigerspice.client.logic.draggables.TrashCan;
import com.velisphere.tigerspice.shared.LinkedPair;

public class LogicCanvas extends Composite {

	static final int controlsOffsetY = 25;

	String uuid;
	String name;
			
	Context2d context;
	Canvas canvas;
	TrashCan trashCan;
	int trashCanXPos;
	int trashCanYPos;

	HandlerRegistration linkedInCanvasHandler;
	HandlerRegistration draggedToCanvasHandler;

	ListToCanvasDropController listToCanvasDropController;
	InCanvasDragDropController inCanvasMoveDropController;
	LogicToCanvasDropController logicToCanvasDropController;
	PickupDragController dragController;
	PickupDragController linkDragController;

	LinkedList<PhysicalItem> physicalItems;
	LinkedList<LogicCheck> logicChecks;
	LinkedList<ConnectorLogicCheckActor> connectorsLogicCheckActor;
	LinkedList<ConnectorSensorActor> connectorsSensorActor;
	LinkedList<ConnectorSensorLogicCheck> connectorsSensorLogicCheck;

	LinkedList<LinkedPair<PhysicalItem, PhysicalItem>> linkedP2PPairs;
	LinkedList<LinkedPair<PhysicalItem, LogicCheck>> linkedP2LPairs;
	LinkedList<LinkedPair<LogicCheck, PhysicalItem>> linkedL2PPairs;
	HashMap<LinkedPair<PhysicalItem, PhysicalItem>, Widget> linkedP2PPairConnectorMap;
	HashMap<LinkedPair<PhysicalItem, LogicCheck>, Widget> linkedP2LPairConnectorMap;
	HashMap<LinkedPair<LogicCheck, PhysicalItem>, Widget> linkedL2PPairConnectorMap;
	LinkedList<ConnectorSensorActor> deletedConnectorsSensorActor;
	LinkedList<ConnectorLogicCheckActor> deletedConnectorsLogicCheckActor;
	LinkedList<ConnectorSensorLogicCheck> deletedConnectorsSensorLogicCheck;

	AbsolutePanel logicPanel;

	public LogicCanvas() {

		createUUID();
		linkedInCanvasHandler = null;

		linkedP2PPairs = new LinkedList<LinkedPair<PhysicalItem, PhysicalItem>>();
		linkedP2PPairConnectorMap = new HashMap<LinkedPair<PhysicalItem, PhysicalItem>, Widget>();
		linkedP2LPairs = new LinkedList<LinkedPair<PhysicalItem, LogicCheck>>();
		linkedP2LPairConnectorMap = new HashMap<LinkedPair<PhysicalItem, LogicCheck>, Widget>();
		linkedL2PPairs = new LinkedList<LinkedPair<LogicCheck, PhysicalItem>>();
		linkedL2PPairConnectorMap = new HashMap<LinkedPair<LogicCheck, PhysicalItem>, Widget>();
		physicalItems = new LinkedList<PhysicalItem>();
		logicChecks = new LinkedList<LogicCheck>();

		connectorsLogicCheckActor = new LinkedList<ConnectorLogicCheckActor>();
		connectorsSensorActor = new LinkedList<ConnectorSensorActor>();
		connectorsSensorLogicCheck = new LinkedList<ConnectorSensorLogicCheck>();

		deletedConnectorsSensorActor = new LinkedList<ConnectorSensorActor>();
		deletedConnectorsLogicCheckActor = new LinkedList<ConnectorLogicCheckActor>();
		deletedConnectorsSensorLogicCheck = new LinkedList<ConnectorSensorLogicCheck>();

		logicPanel = new AbsolutePanel();

		logicPanel.setWidth("100%");
		logicPanel.setHeight("500px");
		initWidget(logicPanel);

		dragController = new PickupDragController(logicPanel, false);
		dragController.setBehaviorDragStartSensitivity(5);
		linkDragController = new PickupDragController(logicPanel, false);

		listToCanvasDropController = new ListToCanvasDropController(logicPanel);
		logicToCanvasDropController = new LogicToCanvasDropController(
				logicPanel);

		// call all event listeners
		setDraggedToCanvasEventListener();
		setLinkedInCanvasP2PEventListener();
		setLinkedInCanvasP2LEventListener();
		setDraggedInCanvasEventListener();
		setLinkedInCanvasL2PEventListener();

		logicPanel.addAttachHandler(new AttachEvent.Handler() {

			@Override
			public void onAttachOrDetach(AttachEvent event) {
				// TODO Auto-generated method stub

				if (event.isAttached()) {
					canvas = Canvas.createIfSupported();
					logicPanel.add(canvas);

					canvas.setWidth("99%");
					canvas.setHeight("99%");
					canvas.setCoordinateSpaceWidth(logicPanel.getOffsetWidth() - 10);
					canvas.setCoordinateSpaceHeight((int) (logicPanel
							.getOffsetHeight() * 0.99));

					canvas.addStyleName("wellapple");

					logicPanel
							.add(new HTML(
									"<b>Logic Canvas</b> Drag sensors and actors here to build your logic."),
									5, 5);

					context = canvas.getContext2d();
					addTrashCan();

				}

			}
		});

	}

	private void addTrashCan() {

		trashCan = new TrashCan();
		trashCanXPos = 10;
		trashCanYPos = canvas.getCoordinateSpaceHeight() - 60;
		logicPanel.add(trashCan, trashCanXPos, trashCanYPos);

	}

	private void createUUID() {
		UuidServiceAsync uuidService = GWT.create(UuidService.class);

		uuidService.getUuid(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				uuid = result;
			}

		});

	}

	public DropController getListToCanvasDropController() {

		return this.listToCanvasDropController;
	}

	public DropController getLogicToCanvasDropController() {

		return this.logicToCanvasDropController;
	}

	private void setDraggedToCanvasEventListener() {

		inCanvasMoveDropController = new InCanvasDragDropController(logicPanel);
		dragController.registerDropController(inCanvasMoveDropController);

		draggedToCanvasHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(
				DraggedToCanvasEvent.TYPE, new DraggedToCanvasEventHandler() {

					@Override
					public void onDraggedToCanvas(
							DraggedToCanvasEvent draggedToCanvasEvent) {

						// TODO Auto-generated method stub

						if (draggedToCanvasEvent.getContext().selectedWidgets
								.get(0) instanceof ExplorerLabel) {
							addPhysicalItem(draggedToCanvasEvent);

						} else if (draggedToCanvasEvent.getContext().selectedWidgets
								.get(0) instanceof LogicCheckAnd) {
							addLogicCheckAnd(draggedToCanvasEvent);

						} else if (draggedToCanvasEvent.getContext().selectedWidgets
								.get(0) instanceof LogicCheckOr) {
							addLogicCheckOr(draggedToCanvasEvent);

						}

					}

				});

	}

	private void addLogicCheckAnd(DraggedToCanvasEvent draggedToCanvasEvent) {

		draggedToCanvasEvent.getContext().selectedWidgets.get(0)
				.removeFromParent();

		LogicCheckAnd logicCheckAnd = new LogicCheckAnd();

		logicPanel.add(logicCheckAnd, draggedToCanvasEvent.getTargetX(),
				draggedToCanvasEvent.getTargetY());

		logicCheckAnd.setxPos(draggedToCanvasEvent.getTargetX());
		logicCheckAnd.setyPos(draggedToCanvasEvent.getTargetY());

		logicChecks.add(logicCheckAnd);

		dragController.makeDraggable(logicCheckAnd);

		addDragPoint(logicCheckAnd);

		logicCheckAnd.setLinkDropController(new InCanvasLinkDropController(
				logicCheckAnd));

		linkDragController.registerDropController(logicCheckAnd
				.getLinkDropController());

	}

	private void addLogicCheckOr(DraggedToCanvasEvent draggedToCanvasEvent) {

		draggedToCanvasEvent.getContext().selectedWidgets.get(0)
				.removeFromParent();

		LogicCheckOr logicCheckOr = new LogicCheckOr();

		logicPanel.add(logicCheckOr, draggedToCanvasEvent.getTargetX(),
				draggedToCanvasEvent.getTargetY());

		logicCheckOr.setxPos(draggedToCanvasEvent.getTargetX());
		logicCheckOr.setyPos(draggedToCanvasEvent.getTargetY());

		logicChecks.add(logicCheckOr);

		dragController.makeDraggable(logicCheckOr);

		addDragPoint(logicCheckOr);

		logicCheckOr.setLinkDropController(new InCanvasLinkDropController(
				logicCheckOr));

		linkDragController.registerDropController(logicCheckOr
				.getLinkDropController());

	}

	private void addPhysicalItem(DraggedToCanvasEvent draggedToCanvasEvent) {

		final ExplorerLabel current = (ExplorerLabel) draggedToCanvasEvent
				.getContext().selectedWidgets.get(0);

		
		final PhysicalItem physicalItem = new PhysicalItem(current.getText(),
				current.getEndpointName(), current.getPropertyID(),
				current.getEndpointID(), current.getEndpointClassID(),
				current.getPropertyClassID(), current.getIsSensor(),
				current.getIsActor());

		logicPanel.add(physicalItem, draggedToCanvasEvent.getTargetX(),
				draggedToCanvasEvent.getTargetY());

		physicalItem.setxPos(draggedToCanvasEvent.getTargetX());
		physicalItem.setyPos(draggedToCanvasEvent.getTargetY());

		physicalItems.add(physicalItem);

		dragController.makeDraggable(physicalItem);

		// add drag point only if it is a sensor

		if (current.getIsSensor() == 1)
			addDragPoint(physicalItem);

		physicalItem.setLinkDropController(new InCanvasLinkDropController(
				physicalItem));

		linkDragController.registerDropController(physicalItem
				.getLinkDropController());

	}

	public void loadPhysicalItem(PhysicalItem physicalItem) {

		logicPanel.add(physicalItem, physicalItem.getxPos(),
				physicalItem.getyPos());

		physicalItems.add(physicalItem);

		dragController.makeDraggable(physicalItem);

		// add drag point only if it is a sensor

		if (physicalItem.getIsSensor() == 1)
			addDragPoint(physicalItem);

		physicalItem.setLinkDropController(new InCanvasLinkDropController(
				physicalItem));

		linkDragController.registerDropController(physicalItem
				.getLinkDropController());

	}

	public void loadLogicCheckAnd(LogicCheckAnd logicCheckAnd) {

		logicPanel.add(logicCheckAnd, logicCheckAnd.getxPos(),
				logicCheckAnd.getyPos());

		logicChecks.add(logicCheckAnd);

		dragController.makeDraggable(logicCheckAnd);

		addDragPoint(logicCheckAnd);

		logicCheckAnd.setLinkDropController(new InCanvasLinkDropController(
				logicCheckAnd));

		linkDragController.registerDropController(logicCheckAnd
				.getLinkDropController());

	}

	public void loadLogicCheckOr(LogicCheckOr logicCheckOr) {

		logicPanel.add(logicCheckOr, logicCheckOr.getxPos(),
				logicCheckOr.getyPos());

		logicChecks.add(logicCheckOr);

		dragController.makeDraggable(logicCheckOr);

		addDragPoint(logicCheckOr);

		logicCheckOr.setLinkDropController(new InCanvasLinkDropController(
				logicCheckOr));

		linkDragController.registerDropController(logicCheckOr
				.getLinkDropController());

	}

	private void addDragPoint(PhysicalItem propertyLabel) {
		WidgetLocation widgetLocation = new WidgetLocation(propertyLabel,
				logicPanel);
		// final HTML link = new HTML("Manage");

		final LinkCreator link = new LinkCreator(propertyLabel, false);

		propertyLabel.setDragPointWidget(link);

		logicPanel.add(link, widgetLocation.getLeft(), widgetLocation.getTop()
				- controlsOffsetY);
		linkDragController.makeDraggable(link);

	}

	private void addDragPoint(LogicCheck logicCheck) {
		WidgetLocation widgetLocation = new WidgetLocation(logicCheck,
				logicPanel);
		// final HTML link = new HTML("Manage");

		final LinkCreator link = new LinkCreator(logicCheck, true);

		logicCheck.setDragPointWidget(link);

		logicPanel.add(link, widgetLocation.getLeft(), widgetLocation.getTop()
				- controlsOffsetY);
		linkDragController.makeDraggable(link);

	}

	private void setLinkedInCanvasP2PEventListener() {
		linkedInCanvasHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(
				LinkedInCanvasP2PEvent.TYPE,
				new LinkedInCanvasP2PEventHandler() {

					@Override
					public void onLinkedInCanvas(
							LinkedInCanvasP2PEvent linkedInCanvasEvent) {
						// TODO Auto-generated method stub

		
						LinkedPair<PhysicalItem, PhysicalItem> linkedP2PPair = new LinkedPair<PhysicalItem, PhysicalItem>(
								linkedInCanvasEvent.getSource(),
								linkedInCanvasEvent.getTarget());
						linkedP2PPairs.add(linkedP2PPair);

						// get the line color

						String lineColor = linkedP2PPair.getLeft()
								.getDragPointWidget().getCurrentColor();

						// add new drag point to source label to allow further
						// links

						addDragPoint(linkedInCanvasEvent.getSource());

						// add a connector and open the connector settings
						// dialog

						final ConnectorSensorActor connector = new ConnectorSensorActor(
								linkedInCanvasEvent.getSource(),
								linkedInCanvasEvent.getTarget());
						linkedP2PPairConnectorMap.put(linkedP2PPair, connector);
						connectorsSensorActor.add(connector);

						// connector.show();

						connector.center();

						// add button to open dialog and position on connection
						// line

						WidgetLocation sourceLocation = new WidgetLocation(
								linkedP2PPair.getLeft(), logicPanel);
						WidgetLocation targetLocation = new WidgetLocation(
								linkedP2PPair.getRight(), logicPanel);
						int xPos = (sourceLocation.getLeft() + targetLocation
								.getLeft()) / 2;
						int yPos = (sourceLocation.getTop() + targetLocation
								.getTop()) / 2;
						logicPanel.add(connector.getOpenerWidget(), xPos, yPos);
						connector.getOpenerWidget().addClickHandler(
								new ClickHandler() {

									@Override
									public void onClick(ClickEvent event) {
										// TODO Auto-generated method stub
										connector.center();
									}

								});

						dragController.makeDraggable(connector
								.getOpenerWidget());
						// re draw the links

						drawLinks("cornflowerblue");

					}
				});

	}

	public void loadP2PConnector(final ConnectorSensorActor connector) {

		LinkedPair<PhysicalItem, PhysicalItem> linkedP2PPair = new LinkedPair<PhysicalItem, PhysicalItem>(
				connector.getSensor(), connector.getActor());
		linkedP2PPairs.add(linkedP2PPair);

		// add a connector and open the connector settings
		// dialog

		linkedP2PPairConnectorMap.put(linkedP2PPair, connector);
		connectorsSensorActor.add(connector);

		// add button to open dialog and position on connection
		// line

		WidgetLocation sourceLocation = new WidgetLocation(
				linkedP2PPair.getLeft(), logicPanel);
		WidgetLocation targetLocation = new WidgetLocation(
				linkedP2PPair.getRight(), logicPanel);
		int xPos = (sourceLocation.getLeft() + targetLocation.getLeft()) / 2;
		int yPos = (sourceLocation.getTop() + targetLocation.getTop()) / 2;
		logicPanel.add(connector.getOpenerWidget(), xPos, yPos);
		connector.getOpenerWidget().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				connector.center();
			}

		});

		dragController.makeDraggable(connector.getOpenerWidget());

		// re draw the links

		drawLinks("cornflowerblue");

	}

	public void loadP2LConnector(final ConnectorSensorLogicCheck connector) {

		LinkedPair<PhysicalItem, LogicCheck> linkedPair = new LinkedPair<PhysicalItem, LogicCheck>(
				connector.getSensor(), connector.getLogicCheck());
		linkedP2LPairs.add(linkedPair);

		// increment source count for logicCheck

		linkedPair.getRight().setSourceCount(
				linkedPair.getRight().getSourceCount() + 1);

		// add a connector and open the connector settings
		// dialog

		linkedP2LPairConnectorMap.put(linkedPair, connector);
		connectorsSensorLogicCheck.add(connector);

		// add connector to child items list of target

		linkedPair.getRight().addChildConnector(connector);

		// add button to open dialog and position on connection
		// line

		WidgetLocation sourceLocation = new WidgetLocation(
				linkedPair.getLeft(), logicPanel);
		WidgetLocation targetLocation = new WidgetLocation(
				linkedPair.getRight(), logicPanel);
		int xPos = (sourceLocation.getLeft() + targetLocation.getLeft()) / 2;
		int yPos = (sourceLocation.getTop() + targetLocation.getTop()) / 2;
		logicPanel.add(connector.getOpenerWidget(), xPos, yPos);
		connector.getOpenerWidget().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				connector.center();
			}

		});

		dragController.makeDraggable(connector.getOpenerWidget());

		// re draw the links

		drawLinks("cornflowerblue");

	}

	public void loadL2PConnector(final ConnectorLogicCheckActor connector) {

		LinkedPair<LogicCheck, PhysicalItem> linkedPair = new LinkedPair<LogicCheck, PhysicalItem>(
				connector.getLogicCheck(), connector.getActor());
		linkedL2PPairs.add(linkedPair);

		// add a connector and open the connector settings
		// dialog

		linkedL2PPairConnectorMap.put(linkedPair, connector);
		connectorsLogicCheckActor.add(connector);

		// add button to open dialog and position on connection
		// line

		WidgetLocation sourceLocation = new WidgetLocation(
				linkedPair.getLeft(), logicPanel);
		WidgetLocation targetLocation = new WidgetLocation(
				linkedPair.getRight(), logicPanel);
		int xPos = (sourceLocation.getLeft() + targetLocation.getLeft()) / 2;
		int yPos = (sourceLocation.getTop() + targetLocation.getTop()) / 2;
		logicPanel.add(connector.getOpenerWidget(), xPos, yPos);
		connector.getOpenerWidget().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				connector.center();
			}

		});

		dragController.makeDraggable(connector.getOpenerWidget());

		// re draw the links

		drawLinks("cornflowerblue");

	}

	private void setLinkedInCanvasP2LEventListener() {
		linkedInCanvasHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(
				LinkedInCanvasP2LEvent.TYPE,
				new LinkedInCanvasP2LEventHandler() {

					@Override
					public void onLinkedInCanvas(
							LinkedInCanvasP2LEvent linkedInCanvasEvent) {
						// TODO Auto-generated method stub

				
						LinkedPair<PhysicalItem, LogicCheck> linkedPair = new LinkedPair<PhysicalItem, LogicCheck>(
								linkedInCanvasEvent.getSource(),
								linkedInCanvasEvent.getTarget());
						linkedP2LPairs.add(linkedPair);

						// increment source count for logicCheck

						linkedPair.getRight().setSourceCount(
								linkedPair.getRight().getSourceCount() + 1);

						// get the line color

						String lineColor = linkedPair.getLeft()
								.getDragPointWidget().getCurrentColor();

						// add new drag point to source label to allow further
						// links

						addDragPoint(linkedInCanvasEvent.getSource());

						// add a connector and open the connector settings
						// dialog

						final ConnectorSensorLogicCheck connector = new ConnectorSensorLogicCheck(
								linkedInCanvasEvent.getSource(),
								linkedInCanvasEvent.getTarget());
						linkedP2LPairConnectorMap.put(linkedPair, connector);
						connectorsSensorLogicCheck.add(connector);

						// add connector to child items list of target

						linkedPair.getRight().addChildConnector(connector);

						// connector.show();
						connector.setAutoHideEnabled(true);
						connector.center();

						// add button to open dialog and position on connection
						// line

						WidgetLocation sourceLocation = new WidgetLocation(
								linkedPair.getLeft(), logicPanel);
						WidgetLocation targetLocation = new WidgetLocation(
								linkedPair.getRight(), logicPanel);
						int xPos = (sourceLocation.getLeft() + targetLocation
								.getLeft()) / 2;
						int yPos = (sourceLocation.getTop() + targetLocation
								.getTop()) / 2;
						logicPanel.add(connector.getOpenerWidget(), xPos, yPos);
						connector.getOpenerWidget().addClickHandler(
								new ClickHandler() {

									@Override
									public void onClick(ClickEvent event) {
										// TODO Auto-generated method stub
										connector.show();
									}

								});

						dragController.makeDraggable(connector
								.getOpenerWidget());

						// re draw the links

						drawLinks("cornflowerblue");

					}
				});

	}

	private void setLinkedInCanvasL2PEventListener() {
		linkedInCanvasHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(
				LinkedInCanvasL2PEvent.TYPE,
				new LinkedInCanvasL2PEventHandler() {

					@Override
					public void onLinkedInCanvas(
							LinkedInCanvasL2PEvent linkedInCanvasEvent) {
						// TODO Auto-generated method stub

			
						LinkedPair<LogicCheck, PhysicalItem> linkedPair = new LinkedPair<LogicCheck, PhysicalItem>(
								linkedInCanvasEvent.getSource(),
								linkedInCanvasEvent.getTarget());
						linkedL2PPairs.add(linkedPair);

						// get the line color

						String lineColor = linkedPair.getLeft()
								.getDragPointWidget().getCurrentColor();

						// add new drag point to source label to allow further
						// links

						addDragPoint(linkedInCanvasEvent.getSource());

						// add a connector and open the connector settings
						// dialog

						final ConnectorLogicCheckActor connector = new ConnectorLogicCheckActor(
								linkedInCanvasEvent.getSource(),
								linkedInCanvasEvent.getTarget());
						linkedL2PPairConnectorMap.put(linkedPair, connector);
						connectorsLogicCheckActor.add(connector);

						// connector.show();
						connector.setAutoHideEnabled(true);
						connector.center();

						// add button to open dialog and position on connection
						// line

						WidgetLocation sourceLocation = new WidgetLocation(
								linkedPair.getLeft(), logicPanel);
						WidgetLocation targetLocation = new WidgetLocation(
								linkedPair.getRight(), logicPanel);
						int xPos = (sourceLocation.getLeft() + targetLocation
								.getLeft()) / 2;
						int yPos = (sourceLocation.getTop() + targetLocation
								.getTop()) / 2;
						logicPanel.add(connector.getOpenerWidget(), xPos, yPos);
						connector.getOpenerWidget().addClickHandler(
								new ClickHandler() {

									@Override
									public void onClick(ClickEvent event) {
										// TODO Auto-generated method stub
										connector.show();
									}

								});

						dragController.makeDraggable(connector
								.getOpenerWidget());

						// re draw the links

						drawLinks("cornflowerblue");

					}
				});

	}

	private void drawLinks(String lineColor) {
		context.clearRect(0, 0, canvas.getCoordinateSpaceWidth(),
				canvas.getCoordinateSpaceHeight());
		drawP2PLinks(lineColor);
		drawP2LLinks(lineColor);
		drawL2PLinks(lineColor);

	}

	private void drawP2PLinks(String lineColor) {

		Iterator<LinkedPair<PhysicalItem, PhysicalItem>> it = linkedP2PPairs
				.iterator();
		while (it.hasNext()) {
			LinkedPair<PhysicalItem, PhysicalItem> labels = it.next();

			LineCoordinateCalculator coordinates = new LineCoordinateCalculator(
					labels.getLeft(), labels.getRight(), logicPanel);

			context.beginPath();

			// change to red

			context.setStrokeStyle(CssColor.make(lineColor));

			context.moveTo(coordinates.getCalcSourceX(),
					coordinates.getCalcSourceY());
			context.lineTo(coordinates.getCalcTargetX(),
					coordinates.getCalcTargetY());

			context.stroke();
			context.closePath();

			drawArrow(coordinates, lineColor);
			positionP2PConnectors(labels, coordinates);

		}

	}

	private void drawP2LLinks(String lineColor) {

		Iterator<LinkedPair<PhysicalItem, LogicCheck>> it = linkedP2LPairs
				.iterator();
		while (it.hasNext()) {
			LinkedPair<PhysicalItem, LogicCheck> labels = it.next();

			LineCoordinateCalculator coordinates = new LineCoordinateCalculator(
					labels.getLeft(), labels.getRight(), logicPanel);

			context.beginPath();

			// change to red

			context.setStrokeStyle(CssColor.make(lineColor));

			context.moveTo(coordinates.getCalcSourceX(),
					coordinates.getCalcSourceY());
			context.lineTo(coordinates.getCalcTargetX(),
					coordinates.getCalcTargetY());

			context.stroke();
			context.closePath();

			drawArrow(coordinates, lineColor);
			positionP2LConnectors(labels, coordinates);

		}

	}

	private void drawL2PLinks(String lineColor) {

		Iterator<LinkedPair<LogicCheck, PhysicalItem>> it = linkedL2PPairs
				.iterator();
		while (it.hasNext()) {
			LinkedPair<LogicCheck, PhysicalItem> labels = it.next();

			LineCoordinateCalculator coordinates = new LineCoordinateCalculator(
					labels.getLeft(), labels.getRight(), logicPanel);

			context.beginPath();

			// change to red

			context.setStrokeStyle(CssColor.make(lineColor));

			context.moveTo(coordinates.getCalcSourceX(),
					coordinates.getCalcSourceY());
			context.lineTo(coordinates.getCalcTargetX(),
					coordinates.getCalcTargetY());

			context.stroke();
			context.closePath();

			drawArrow(coordinates, lineColor);
			positionL2PConnectors(labels, coordinates);

		}

	}

	private void drawArrow(LineCoordinateCalculator coordinates,
			String lineColor) {

		double angle = Math.PI / 8;

		// calculate the angle of the line
		double lineangle = Math.atan2(coordinates.getCalcTargetY()
				- coordinates.getCalcSourceY(), coordinates.getCalcTargetX()
				- coordinates.getCalcSourceX());
		// h is the line length of a side of the arrow head
		double h = Math.abs(10 / Math.cos(angle));

		double angle1 = lineangle + Math.PI + angle;
		double topx = coordinates.getCalcTargetX() + Math.cos(angle1) * h;
		double topy = coordinates.getCalcTargetY() + Math.sin(angle1) * h;

		double angle2 = lineangle + Math.PI - angle;
		double botx = coordinates.getCalcTargetX() + Math.cos(angle2) * h;
		double boty = coordinates.getCalcTargetY() + Math.sin(angle2) * h;

		context.beginPath();

		context.setStrokeStyle(CssColor.make("lineColor"));

		context.save();
		context.beginPath();
		context.moveTo(topx, topy);
		context.lineTo(coordinates.getCalcTargetX(),
				coordinates.getCalcTargetY());
		context.stroke();
		context.lineTo(botx, boty);
		context.stroke();
		context.restore();

		context.closePath();

	}

	private void positionP2PConnectors(
			LinkedPair<PhysicalItem, PhysicalItem> currentPair,
			LineCoordinateCalculator coordinates) {
		if (linkedP2PPairConnectorMap.containsKey(currentPair)) {
			final ConnectorSensorActor currentConnector = (ConnectorSensorActor) linkedP2PPairConnectorMap
					.get(currentPair);
			int xPos = (coordinates.getCalcSourceX() + coordinates
					.getCalcTargetX()) / 2;
			int yPos = (coordinates.getCalcSourceY() + coordinates
					.getCalcTargetY()) / 2;
			logicPanel.remove(currentConnector.getOpenerWidget());
			logicPanel.add(currentConnector.getOpenerWidget(), xPos, yPos);
			currentConnector.getOpenerWidget().addClickHandler(
					new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							// TODO Auto-generated method stub
							currentConnector.show();
						}

					});

		}

	}

	private void positionP2LConnectors(
			LinkedPair<PhysicalItem, LogicCheck> currentPair,
			LineCoordinateCalculator coordinates) {
		if (linkedP2LPairConnectorMap.containsKey(currentPair)) {
			final ConnectorSensorLogicCheck currentConnector = (ConnectorSensorLogicCheck) linkedP2LPairConnectorMap
					.get(currentPair);
			int xPos = (coordinates.getCalcSourceX() + coordinates
					.getCalcTargetX()) / 2;
			int yPos = (coordinates.getCalcSourceY() + coordinates
					.getCalcTargetY()) / 2;
			logicPanel.remove(currentConnector.getOpenerWidget());
			logicPanel.add(currentConnector.getOpenerWidget(), xPos, yPos);
			currentConnector.getOpenerWidget().addClickHandler(
					new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							// TODO Auto-generated method stub
							currentConnector.show();
						}

					});

		}

	}

	private void positionL2PConnectors(
			LinkedPair<LogicCheck, PhysicalItem> currentPair,
			LineCoordinateCalculator coordinates) {
		if (linkedL2PPairConnectorMap.containsKey(currentPair)) {
			final ConnectorLogicCheckActor currentConnector = (ConnectorLogicCheckActor) linkedL2PPairConnectorMap
					.get(currentPair);
			int xPos = (coordinates.getCalcSourceX() + coordinates
					.getCalcTargetX()) / 2;
			int yPos = (coordinates.getCalcSourceY() + coordinates
					.getCalcTargetY()) / 2;
			logicPanel.remove(currentConnector.getOpenerWidget());
			logicPanel.add(currentConnector.getOpenerWidget(), xPos, yPos);
			currentConnector.getOpenerWidget().addClickHandler(
					new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							// TODO Auto-generated method stub
							currentConnector.show();
						}

					});

					}

	}

	private void setDraggedInCanvasEventListener() {
		HandlerRegistration draggedInCanvasHandler = EventUtils.RESETTABLE_EVENT_BUS
				.addHandler(DraggedInCanvasEvent.TYPE,
						new DraggedInCanvasEventHandler() {

							@Override
							public void onDraggedInCanvas(
									DraggedInCanvasEvent draggedInCanvasEvent) {
								// TODO Auto-generated method stub

		
								// first check if this is a physical property or
								// logic check

								if (draggedInCanvasEvent.getContext().selectedWidgets
										.get(0) instanceof PhysicalItem) {

									// update position data

									WidgetLocation widgetLocation = new WidgetLocation(
											draggedInCanvasEvent
													.getCanvasLabel(),
											logicPanel);
									draggedInCanvasEvent.getCanvasLabel()
											.setxPos(widgetLocation.getLeft());
									draggedInCanvasEvent.getCanvasLabel()
											.setyPos(
													widgetLocation.getTop()
															- controlsOffsetY);

									// check if moved to trash

									if ((draggedInCanvasEvent.getCanvasLabel()
											.getyPos() > trashCanYPos - 20)
											&& (draggedInCanvasEvent
													.getCanvasLabel().getxPos() < trashCanXPos + 50))

									{
										removePhysicalItem(draggedInCanvasEvent
												.getCanvasLabel());
									}

									// move dragPoint if it is a sensor

									if (draggedInCanvasEvent.getCanvasLabel()
											.getIsSensor() == 1) {
										WidgetLocation newLocation = new WidgetLocation(
												draggedInCanvasEvent
														.getCanvasLabel(),
												logicPanel);
										draggedInCanvasEvent.getCanvasLabel()
												.getDragPointWidget()
												.removeFromParent();
										logicPanel.add(draggedInCanvasEvent
												.getCanvasLabel()
												.getDragPointWidget(),
												newLocation.getLeft(),
												newLocation.getTop()
														- controlsOffsetY);
									}
								}

								// logic check and

								if (draggedInCanvasEvent.getContext().selectedWidgets
										.get(0) instanceof LogicCheckAnd) {
									// move dragPoint if it is a logical and
									// check

									// update position data

									WidgetLocation widgetLocation = new WidgetLocation(
											draggedInCanvasEvent
													.getLogicCheckAnd(),
											logicPanel);
									draggedInCanvasEvent.getLogicCheckAnd()
											.setxPos(widgetLocation.getLeft());
									draggedInCanvasEvent.getLogicCheckAnd()
											.setyPos(
													widgetLocation.getTop()
															- controlsOffsetY);

									// check if moved to trash

									if ((draggedInCanvasEvent
											.getLogicCheckAnd().getyPos() > trashCanYPos - 20)
											&& (draggedInCanvasEvent
													.getLogicCheckAnd()
													.getxPos() < trashCanXPos + 50))

									{
										removeLogicCheck(draggedInCanvasEvent
												.getLogicCheckAnd());
									}

									WidgetLocation newLocation = new WidgetLocation(
											draggedInCanvasEvent
													.getLogicCheckAnd(),
											logicPanel);
									draggedInCanvasEvent.getLogicCheckAnd()
											.getDragPointWidget()
											.removeFromParent();
									logicPanel.add(draggedInCanvasEvent
											.getLogicCheckAnd()
											.getDragPointWidget(), newLocation
											.getLeft(), newLocation.getTop()
											- controlsOffsetY);

								}

								// logic check or

								if (draggedInCanvasEvent.getContext().selectedWidgets
										.get(0) instanceof LogicCheckOr) {

									// update position data

									WidgetLocation widgetLocation = new WidgetLocation(
											draggedInCanvasEvent
													.getLogicCheckOr(),
											logicPanel);
									draggedInCanvasEvent.getLogicCheckOr()
											.setxPos(widgetLocation.getLeft());
									draggedInCanvasEvent.getLogicCheckOr()
											.setyPos(
													widgetLocation.getTop()
															- controlsOffsetY);

						
						
									// check if moved to trash

									if ((draggedInCanvasEvent.getLogicCheckOr()
											.getyPos() > trashCanYPos - 20)
											&& (draggedInCanvasEvent
													.getLogicCheckOr()
													.getxPos() < trashCanXPos + 50))

									{
										removeLogicCheck(draggedInCanvasEvent
												.getLogicCheckOr());
									}

									WidgetLocation newLocation = new WidgetLocation(
											draggedInCanvasEvent
													.getLogicCheckOr(),
											logicPanel);
									draggedInCanvasEvent.getLogicCheckOr()
											.getDragPointWidget()
											.removeFromParent();
									logicPanel.add(draggedInCanvasEvent
											.getLogicCheckOr()
											.getDragPointWidget(), newLocation
											.getLeft(), newLocation.getTop()
											- controlsOffsetY);

								}

								// connector

							
								if (draggedInCanvasEvent.getContext().selectedWidgets
										.get(0) instanceof DraggableButton) {

									WidgetLocation newLocation = new WidgetLocation(
											draggedInCanvasEvent.getButton(),
											logicPanel);


									// check if moved to trash

									if ((newLocation.getTop() > trashCanYPos - 20)
											&& (newLocation.getLeft() < trashCanXPos + 50))

									{

										
										removeConnector(draggedInCanvasEvent
												.getButton());

									}
								}

								// re-draw the links

								drawLinks("cornflowerblue");

							}
						});

	}

	private void removePhysicalItem(PhysicalItem physicalItem) {

		// unregister drop controller for this drop target
		linkDragController.unregisterDropController(physicalItem
				.getLinkDropController());

		// visually remove item from diagram
		physicalItem.removeFromParent();

		// remove from physical items list
		physicalItems.remove(physicalItem);

		// remove all related connectors and redraw
		Iterator<LinkedPair<PhysicalItem, PhysicalItem>> ip = linkedP2PPairs
				.iterator();
		while (ip.hasNext()) {
			LinkedPair<PhysicalItem, PhysicalItem> pair = ip.next();
			if (pair.getLeft() == physicalItem
					|| pair.getRight() == physicalItem) {
				linkedP2PPairs.remove(pair);
				ConnectorSensorActor connector = (ConnectorSensorActor) linkedP2PPairConnectorMap
						.get(pair);
				deletedConnectorsSensorActor.add(connector);
				connectorsSensorActor.remove(connector);
				linkedP2PPairConnectorMap.remove(pair);
				logicPanel.remove(connector.getOpenerWidget());
				drawLinks("cornflowerblue");
			}
		}

		Iterator<LinkedPair<LogicCheck, PhysicalItem>> il2p = linkedL2PPairs
				.iterator();
		while (il2p.hasNext()) {
			LinkedPair<LogicCheck, PhysicalItem> pair = il2p.next();
			if (pair.getRight() == physicalItem) {
				linkedL2PPairs.remove(pair);
				ConnectorLogicCheckActor connector = (ConnectorLogicCheckActor) linkedL2PPairConnectorMap
						.get(pair);
				deletedConnectorsLogicCheckActor.add(connector);
				connectorsLogicCheckActor.remove(connector);
				linkedL2PPairConnectorMap.remove(pair);
				logicPanel.remove(connector.getOpenerWidget());
				drawLinks("cornflowerblue");
			}
		}

		Iterator<LinkedPair<PhysicalItem, LogicCheck>> ip2l = linkedP2LPairs
				.iterator();
		while (ip2l.hasNext()) {
			LinkedPair<PhysicalItem, LogicCheck> pair = ip2l.next();
			if (pair.getLeft() == physicalItem) {
				linkedP2LPairs.remove(pair);
				ConnectorSensorLogicCheck connector = (ConnectorSensorLogicCheck) linkedP2LPairConnectorMap
						.get(pair);
				deletedConnectorsSensorLogicCheck.add(connector);
				connectorsSensorLogicCheck.remove(connector);
				linkedP2LPairConnectorMap.remove(pair);
				pair.getRight().removeChildConnector(connector);
				logicPanel.remove(connector.getOpenerWidget());
				drawLinks("cornflowerblue");
			}
		}

	}

	private void removeLogicCheck(LogicCheck logicCheck) {

		// unregister drop controller for this drop target
		linkDragController.unregisterDropController(logicCheck
				.getLinkDropController());

		// visually remove item from diagram
		logicCheck.removeFromParent();

		// remove from physical items list
		logicChecks.remove(logicCheck);

		// remove all related connectors and redraw

		Iterator<LinkedPair<LogicCheck, PhysicalItem>> il2p = linkedL2PPairs
				.iterator();
		while (il2p.hasNext()) {
			LinkedPair<LogicCheck, PhysicalItem> pair = il2p.next();
			if (pair.getLeft() == logicCheck) {
				linkedL2PPairs.remove(pair);
				ConnectorLogicCheckActor connector = (ConnectorLogicCheckActor) linkedL2PPairConnectorMap
						.get(pair);
				deletedConnectorsLogicCheckActor.add(connector);
				connectorsLogicCheckActor.remove(connector);
				linkedL2PPairConnectorMap.remove(pair);
				logicPanel.remove(connector.getOpenerWidget());
				drawLinks("cornflowerblue");
			}
		}

		Iterator<LinkedPair<PhysicalItem, LogicCheck>> ip2l = linkedP2LPairs
				.iterator();
		while (ip2l.hasNext()) {
			LinkedPair<PhysicalItem, LogicCheck> pair = ip2l.next();
			if (pair.getRight() == logicCheck) {
				linkedP2LPairs.remove(pair);
				ConnectorSensorLogicCheck connector = (ConnectorSensorLogicCheck) linkedP2LPairConnectorMap
						.get(pair);
				deletedConnectorsSensorLogicCheck.add(connector);
				connectorsSensorLogicCheck.remove(connector);
				linkedP2LPairConnectorMap.remove(pair);
				logicPanel.remove(connector.getOpenerWidget());
				drawLinks("cornflowerblue");
			}
		}
	}

	private void removeConnector(DraggableButton draggableButton) {

		if (draggableButton.getConnectorType() == ConnectorLogicCheckActor.class) {
			ConnectorLogicCheckActor connector = (ConnectorLogicCheckActor) draggableButton
					.getConnector();
			connectorsLogicCheckActor.remove(connector);

			Iterator<Entry<LinkedPair<LogicCheck, PhysicalItem>, Widget>> l2pIT = linkedL2PPairConnectorMap
					.entrySet().iterator();
			while (l2pIT.hasNext()) {
				Map.Entry<LinkedPair<LogicCheck, PhysicalItem>, Widget> pair = l2pIT
						.next();
				if (pair.getValue() == connector) {
					linkedL2PPairs.remove(pair.getKey());
					linkedL2PPairConnectorMap.remove(pair.getKey());
				}

			}

			deletedConnectorsLogicCheckActor.add(connector);
			connectorsLogicCheckActor.remove(connector);
			logicPanel.remove(connector.getOpenerWidget());
			drawLinks("cornflowerblue");

		}

		if (draggableButton.getConnectorType() == ConnectorSensorActor.class) {

			ConnectorSensorActor connector = (ConnectorSensorActor) draggableButton
					.getConnector();
			connectorsLogicCheckActor.remove(connector);

			Iterator<Entry<LinkedPair<PhysicalItem, PhysicalItem>, Widget>> p2pIT = linkedP2PPairConnectorMap
					.entrySet().iterator();
			while (p2pIT.hasNext()) {
				Map.Entry<LinkedPair<PhysicalItem, PhysicalItem>, Widget> pair = p2pIT
						.next();
				if (pair.getValue() == connector) {
					linkedP2PPairs.remove(pair.getKey());
					linkedP2PPairConnectorMap.remove(pair.getKey());
				}

			}

			deletedConnectorsSensorActor.add(connector);
			connectorsSensorActor.remove(connector);
			logicPanel.remove(connector.getOpenerWidget());
			drawLinks("cornflowerblue");

		}

		if (draggableButton.getConnectorType() == ConnectorSensorLogicCheck.class) {
			ConnectorSensorLogicCheck connector = (ConnectorSensorLogicCheck) draggableButton
					.getConnector();
			connectorsSensorLogicCheck.remove(connector);

			Iterator<Entry<LinkedPair<PhysicalItem, LogicCheck>, Widget>> p2lIT = linkedP2LPairConnectorMap
					.entrySet().iterator();
			while (p2lIT.hasNext()) {
				Map.Entry<LinkedPair<PhysicalItem, LogicCheck>, Widget> pair = p2lIT
						.next();
				if (pair.getValue() == connector) {
					linkedP2LPairs.remove(pair.getKey());
					linkedP2LPairConnectorMap.remove(pair.getKey());
				}

			}

			deletedConnectorsSensorLogicCheck.add(connector);
			connectorsSensorLogicCheck.remove(connector);
			logicPanel.remove(connector.getOpenerWidget());
			drawLinks("cornflowerblue");
		}

	}

	public void saveToDatabase(String newName) {
		this.setName(newName);
		DataManager dataManager = new DataManager(this);
		dataManager.processCheckPath();
		dataManager.processP2P();
		dataManager.processP2L();
		dataManager.processL2P();
		dataManager.processDeletedP2P();
		dataManager.processDeletedP2L();
		dataManager.processDeletedL2P();
		
	}
	
	public void deleteFromDatabase() {
		
		
		final DialogBox deleteDialog = new DialogBox();
		deleteDialog.setText("Are you sure you want to delete this checkpath?");
		deleteDialog.setStyleName("wellappleblue");
		HorizontalPanel panel = new HorizontalPanel();
		Button btnDelete = new Button("Yes");
		panel.add(btnDelete);
		Button btnCancel = new Button("No");
		panel.add(btnCancel);
		deleteDialog.add(panel);
		deleteDialog.setAutoHideEnabled(true);
		deleteDialog.center();
		
		final LogicCanvas currentCanvas = this;
		
		btnDelete.addClickHandler(new ClickHandler(){
			
			@Override
			public void onClick(ClickEvent event) {
				Iterator<PhysicalItem> pit = physicalItems.iterator();
				while (pit.hasNext())
				{
					removePhysicalItem(pit.next());
				}
				
				Iterator<LogicCheck> lit = logicChecks.iterator();
				while (lit.hasNext())
				{
					removeLogicCheck(lit.next());
				}
				
				DataManager dataManager = new DataManager(currentCanvas);
				//dataManager.processCheckPath();
				dataManager.processP2P();
				dataManager.processP2L();
				dataManager.processL2P();
				dataManager.processDeletedP2P();
				dataManager.processDeletedP2L();
				dataManager.processDeletedL2P();
				dataManager.deleteCheckpath();
				deleteDialog.removeFromParent();
				AppController.openLogicDesigner();
				
			}
			
		});
		
		btnCancel.addClickHandler(new ClickHandler(){
			
			@Override
			public void onClick(ClickEvent event) {
				deleteDialog.removeFromParent();
				AppController.openLogicDesigner();
			}
		});
						
	}

	public void openFromDatabase(String checkpathID) {
		DataManager dataManager = new DataManager(this);
		dataManager.loadUI(checkpathID);

	}

	public LinkedList<PhysicalItem> getPhysicalItems() {
		return physicalItems;
	}

	public LinkedList<LogicCheck> getLogicChecks() {
		return logicChecks;
	}

	public LinkedList<ConnectorLogicCheckActor> getConnectorsLogicCheckActor() {
		return connectorsLogicCheckActor;
	}

	public LinkedList<ConnectorSensorActor> getConnectorsSensorActor() {
		return connectorsSensorActor;
	}

	public LinkedList<ConnectorSensorLogicCheck> getConnectorsSensorLogicCheck() {
		return connectorsSensorLogicCheck;
	}

	public String getUUID() {
		return this.uuid;
	}

	public void setUUID(String uuid) {
		this.uuid = uuid;
	}
	
	public LinkedList<ConnectorSensorActor> getDeletedConnectorsSensorActor() {
		return deletedConnectorsSensorActor;
	}

	public LinkedList<ConnectorLogicCheckActor> getDeletedConnectorsLogicCheckActor() {
		return deletedConnectorsLogicCheckActor;
	}

	public LinkedList<ConnectorSensorLogicCheck> getDeletedConnectorsSensorLogicCheck() {
		return deletedConnectorsSensorLogicCheck;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		
		EventUtils.RESETTABLE_EVENT_BUS.fireEvent(new LogicNameChangedEvent(name));
	}


}
