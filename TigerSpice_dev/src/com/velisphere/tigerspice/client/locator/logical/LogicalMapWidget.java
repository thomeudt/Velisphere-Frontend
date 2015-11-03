package com.velisphere.tigerspice.client.locator.logical;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.allen_sauer.gwt.dnd.client.util.WidgetLocation;
import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.Row;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.appcontroller.SessionHelper;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.endpoints.EndpointServiceAsync;
import com.velisphere.tigerspice.client.event.DraggedInCanvasEvent;
import com.velisphere.tigerspice.client.event.DraggedInCanvasEventHandler;
import com.velisphere.tigerspice.client.event.EventUtils;
import com.velisphere.tigerspice.client.event.FilterAppliedEvent;
import com.velisphere.tigerspice.client.event.FilterAppliedEventHandler;
import com.velisphere.tigerspice.client.helper.widgets.FilterSphereEndpointWidget;
import com.velisphere.tigerspice.client.logic.connectors.ConnectorLogicCheckActor;
import com.velisphere.tigerspice.client.logic.connectors.ConnectorSensorActor;
import com.velisphere.tigerspice.client.logic.connectors.ConnectorSensorLogicCheck;
import com.velisphere.tigerspice.client.logic.controllers.InCanvasDragDropController;
import com.velisphere.tigerspice.client.logic.controllers.ListToCanvasDropController;
import com.velisphere.tigerspice.client.logic.controllers.LogicToCanvasDropController;
import com.velisphere.tigerspice.client.logic.draggables.DraggableButton;
import com.velisphere.tigerspice.client.logic.draggables.LogicCheck;
import com.velisphere.tigerspice.client.logic.draggables.LogicCheckAnd;
import com.velisphere.tigerspice.client.logic.draggables.LogicCheckOr;
import com.velisphere.tigerspice.client.logic.draggables.PhysicalItem;
import com.velisphere.tigerspice.client.logic.draggables.TrashCan;
import com.velisphere.tigerspice.client.logic.layoutWidgets.LineCoordinateCalculator;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.LinkedPair;
import com.velisphere.tigerspice.shared.LogicLinkTargetData;

public class LogicalMapWidget extends Composite {

	static final int controlsOffsetY = 25;

	String uuid;
	String name;
			
	Context2d context;
	Canvas canvas;

	PickupDragController dragController;
	LogicLocatorDragDropController inCanvasMoveDropController;
	
	AbsolutePanel logicPanel;
	HashMap<String, DragBox> labelDirectory;
	HashMap<String, LinkedList<String>> linkDirectory;
	HorizontalPanel mainPanel;

	Column filterCol;
	Column diagramCol;
	
	public LogicalMapWidget() {
	
		logicPanel = new AbsolutePanel();
		mainPanel = new HorizontalPanel();

		FilterSphereEndpointWidget endpointFilter = new FilterSphereEndpointWidget();
		Row row = new Row();
		filterCol = new Column (2);
		filterCol.add(endpointFilter);
		row.add(filterCol);
		
		diagramCol = new Column (8);
		diagramCol.add(logicPanel);
		row.add(diagramCol);
		
		
		mainPanel.add(row);
		
		setFilterHandlerListener();


		logicPanel.setWidth("100%");
		logicPanel.setHeight("500px");
		initWidget(mainPanel);
		linkDirectory = new HashMap<String, LinkedList<String>>();
		labelDirectory = new HashMap<String, DragBox>();

		
		logicPanel.addAttachHandler(new AttachEvent.Handler() {

			@Override
			public void onAttachOrDetach(AttachEvent event) {
				// TODO Auto-generated method stub

				if (event.isAttached()) {
					canvas = Canvas.createIfSupported();
					logicPanel.add(canvas);

					canvas.setWidth("99%");
					canvas.setHeight("99%");
					RootPanel.get().add(
							new HTML("WIDTH OFFSET "
									+ logicPanel.getOffsetWidth()));
					canvas.setCoordinateSpaceWidth(logicPanel.getOffsetWidth() - 10);
					canvas.setCoordinateSpaceHeight((int) (logicPanel
							.getOffsetHeight() * 0.99));

					canvas.addStyleName("wellapple");

					logicPanel
							.add(new HTML(
									"<b>Connection Map</b> Shows a logical map of all your device connections."),
									5, 5);

					context = canvas.getContext2d();

				}

			}
		});

		dragController = new PickupDragController(logicPanel, false);
		dragController.setBehaviorDragStartSensitivity(5);
		inCanvasMoveDropController = new LogicLocatorDragDropController(logicPanel);
		dragController.registerDropController(inCanvasMoveDropController);

		setDraggedInCanvasEventListener();
		
		populateDiagramAllEndpoints();

	}

	public LogicalMapWidget(String endpointID) 
	{
		
		logicPanel = new AbsolutePanel();
		
		logicPanel.setWidth("100%");
		logicPanel.setHeight("500px");
		initWidget(logicPanel);
		linkDirectory = new HashMap<String, LinkedList<String>>();
		labelDirectory = new HashMap<String, DragBox>();

		
		logicPanel.addAttachHandler(new AttachEvent.Handler() {

			@Override
			public void onAttachOrDetach(AttachEvent event) {
				// TODO Auto-generated method stub

				if (event.isAttached()) {
					canvas = Canvas.createIfSupported();
					logicPanel.add(canvas);

					canvas.setWidth("99%");
					canvas.setHeight("99%");
					RootPanel.get().add(
							new HTML("WIDTH OFFSET "
									+ logicPanel.getOffsetWidth()));
					canvas.setCoordinateSpaceWidth(logicPanel.getOffsetWidth() - 10);
					canvas.setCoordinateSpaceHeight((int) (logicPanel
							.getOffsetHeight() * 0.99));

					canvas.addStyleName("wellapple");

					logicPanel
							.add(new HTML(
									"<b>Connection Map</b> Shows a logical map of all your device connections."),
									5, 5);

					context = canvas.getContext2d();

				}

			}
		});

		dragController = new PickupDragController(logicPanel, false);
		dragController.setBehaviorDragStartSensitivity(5);
		inCanvasMoveDropController = new LogicLocatorDragDropController(logicPanel);
		dragController.registerDropController(inCanvasMoveDropController);

		setDraggedInCanvasEventListener();
		
		populateDiagramSingleEndpoint(endpointID);

		
	}


	private void populateDiagramAllEndpoints() {

		
		EndpointServiceAsync endpointService = GWT
				.create(EndpointService.class);

		endpointService.getEndpointsForUser(SessionHelper.getCurrentUserID(),
				new AsyncCallback<LinkedList<EndpointData>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(LinkedList<EndpointData> result) {

						drawSelectedLabels(200, 200, result);

					}

				});

	}

	private void populateDiagramSingleSphere(String sphereID) {

		
		EndpointServiceAsync endpointService = GWT
				.create(EndpointService.class);

		endpointService.getEndpointsForSphere(sphereID,
				new AsyncCallback<LinkedList<EndpointData>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(LinkedList<EndpointData> result) {

						drawSelectedLabels(200, 200, result);

					}

				});

	}
	
	private void populateDiagramSingleEndpoint(String endpointID) {

		
		EndpointServiceAsync endpointService = GWT
				.create(EndpointService.class);

		endpointService.getEndpointForEndpointID(endpointID,
				new AsyncCallback<EndpointData>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(EndpointData result) {

						LinkedList<EndpointData> listSingleItem = new LinkedList<EndpointData>();
						listSingleItem.add(result);
						drawSelectedLabels(200, 200, listSingleItem);

					}

				});

	}


	private void drawSelectedLabels(int xPosCenter, int yPosCenter, LinkedList<EndpointData> result) {
		
		Iterator<EndpointData> it = result.iterator();

		int totalLabelCount = result.size();
		int radius = 125;

		double seperationDegrees = 360 / totalLabelCount;

		RootPanel.get()
				.add(new HTML("Seperation Degrees " + seperationDegrees));

		int counter = 1;

		while (it.hasNext()) {
			final EndpointData endpoint = it.next();
			final DragBox endpointLabel = new DragBox(
					endpoint.endpointId, endpoint.endpointName,
					endpoint.getEpcId());

			double radians = (seperationDegrees * counter * Math.PI) / 180;
			RootPanel.get().add(
					new HTML("Degrees " + seperationDegrees * counter));
			int x = (int) (xPosCenter + radius * Math.cos(radians));
			int y = (int) (yPosCenter + radius * Math.sin(radians));

			counter++;

			labelDirectory.put(endpoint.endpointId, endpointLabel);
			
			
			logicPanel.add(endpointLabel, x, y);
			dragController.makeDraggable(endpointLabel);

		}

		
		

		drawLinksFromServer();
	}

	private void drawLinksFromServer() {

		
		// clear canvas
		
		context.clearRect(0, 0, canvas.getCoordinateSpaceWidth(),
				canvas.getCoordinateSpaceHeight());
		
		// get data
		
		LinkedList<String> sourceEndpointIDs = new LinkedList<String>(
				labelDirectory.keySet());
		
		final LinkedList<String> missingLabelDirectory = new LinkedList<String>();

		EndpointServiceAsync endpointService = GWT
				.create(EndpointService.class);

		endpointService
				.getLinksForEndpointList(
						sourceEndpointIDs,
						new AsyncCallback<HashMap<String, LinkedList<LogicLinkTargetData>>>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onSuccess(
									HashMap<String, LinkedList<LogicLinkTargetData>> result) {

								RootPanel.get().add(
										new HTML("Raw data "
												+ result.toString()));

								Iterator<Entry<String, DragBox>> it = labelDirectory
										.entrySet().iterator();

								while (it.hasNext()) {

									Map.Entry<String, DragBox> originPair = it
											.next();
									DragBox originLabel = originPair
											.getValue();

									RootPanel
											.get()
											.add(new HTML(
													"Origin data "
															+ originPair
																	.getValue().endpointID));

									LinkedList<LogicLinkTargetData> targetEndpoints = result
											.get(originPair.getKey());

									RootPanel.get().add(
											new HTML("Raw target data "
													+ targetEndpoints
															.toString()));

									Iterator<LogicLinkTargetData> iTt = targetEndpoints
											.iterator();

									while (iTt.hasNext()) {

										final LogicLinkTargetData targetEndpoint = iTt
												.next();

										// check if target is already drawn in
										// same sphere

										if (labelDirectory
												.containsKey(targetEndpoint
														.getTargetEndpointID())) {
											drawLink(targetEndpoint, originLabel);
	
										} else
										{
											if(!missingLabelDirectory.contains(targetEndpoint.getTargetEndpointID())){
												missingLabelDirectory.add(targetEndpoint.getTargetEndpointID());	
											}
											
											
										}
											
											

									}

								}

								if (missingLabelDirectory.size() > 0)
								{
									addOutOfSelectionLabels(missingLabelDirectory);
								}
									
								
							}

						});

	}

	private void drawLink(final LogicLinkTargetData targetEndpoint, DragBox originLabel)
	{
		
		String lineColor = "cornflowerblue";
		
		DragBox targetLabel = labelDirectory.get(targetEndpoint
				.getTargetEndpointID());
		
		LineCoordinateCalculator coordinates = new LineCoordinateCalculator(
				originLabel, targetLabel, logicPanel);

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

	
	private void addOutOfSelectionLabels(LinkedList<String> missingLabelDirectory)
	{
		
		EndpointServiceAsync endpointService = GWT
				.create(EndpointService.class);
		
	
		endpointService.getEndpointsForMultipleIDs(missingLabelDirectory,
				new AsyncCallback<LinkedList<EndpointData>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(LinkedList<EndpointData> result) {
						
						drawSelectedLabels(500, 200, result);

					}

				});
					
	}
	
	private void setFilterHandlerListener() {
		HandlerRegistration applyFilterHandler;
		applyFilterHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(
				FilterAppliedEvent.TYPE, new FilterAppliedEventHandler() {
					@Override
					public void onFilterApplied(
							FilterAppliedEvent filterAppliedEvent) {
						HTML html = new HTML("FILTER APPLIED. Sphere ID: "
								+ filterAppliedEvent.getSphereID()
								+ ", Endpoint ID: "
								+ filterAppliedEvent.getEndpointID());
						RootPanel.get().add(html);
						// applyFilterHandler.removeHandler();
						
						Iterator<Entry<String, DragBox>> it = labelDirectory.entrySet().iterator();
						
						while (it.hasNext())
						{
							Map.Entry<String, DragBox> pair = it.next();
							logicPanel.remove(pair.getValue());
						}
						
						labelDirectory.clear();
						
						
						if (filterAppliedEvent.getEndpointID() != "0") {

							populateDiagramSingleEndpoint(filterAppliedEvent.getEndpointID());

						} else if (filterAppliedEvent.getSphereID() != "0")

						{
							populateDiagramSingleSphere(filterAppliedEvent
									.getSphereID());

						}

						else
							populateDiagramAllEndpoints();

					}
				});
	}
	
	
	private void setDraggedInCanvasEventListener() {
		HandlerRegistration draggedInCanvasHandler = EventUtils.RESETTABLE_EVENT_BUS
				.addHandler(DraggedInCanvasEvent.TYPE,
						new DraggedInCanvasEventHandler() {
					
							@Override
							public void onDraggedInCanvas(
									DraggedInCanvasEvent draggedInCanvasEvent) {
								// TODO Auto-generated method stub

								
								drawLinksFromServer();

							}
						});

	}


}
