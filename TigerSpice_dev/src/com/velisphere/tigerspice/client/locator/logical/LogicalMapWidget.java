package com.velisphere.tigerspice.client.locator.logical;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.orange.links.client.DiagramController;
import com.orange.links.client.connection.Connection;
import com.orange.links.client.shapes.DecorationShape;
import com.velisphere.tigerspice.client.actions.ActionService;
import com.velisphere.tigerspice.client.actions.ActionServiceAsync;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.appcontroller.SessionHelper;
import com.velisphere.tigerspice.client.checks.CheckService;
import com.velisphere.tigerspice.client.checks.CheckServiceAsync;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.endpoints.EndpointServiceAsync;
import com.velisphere.tigerspice.client.event.EventUtils;
import com.velisphere.tigerspice.client.event.FilterAppliedEvent;
import com.velisphere.tigerspice.client.event.FilterAppliedEventHandler;
import com.velisphere.tigerspice.client.helper.widgets.FilterSphereEndpointWidget;
import com.velisphere.tigerspice.shared.ActionData;
import com.velisphere.tigerspice.shared.CheckData;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.LogicLinkTargetData;

public class LogicalMapWidget extends Composite {

	HorizontalPanel pWidget;
	ScrollPanel scrollWidget;
	DiagramController controller;
	HashMap<String, LabelWithMenu> labelDirectory;
	HashMap<String, LinkedList<String>> linkDirectory;
	HorizontalPanel diagramContainer;
	PickupDragController dragController;
	
	public LogicalMapWidget() {

		pWidget = new HorizontalPanel();
		diagramContainer = new HorizontalPanel();
		linkDirectory = new HashMap<String, LinkedList<String>>();
		labelDirectory = new HashMap<String, LabelWithMenu>();
		FilterSphereEndpointWidget endpointFilter = new FilterSphereEndpointWidget();
		pWidget.add(endpointFilter);
		setFilterHandlerListener();
		initWidget(pWidget);
		controller = createDiagramController();
		dragController = new PickupDragController(
				controller.getView(), true);
		controller.registerDragController(dragController);
		pWidget.add(diagramContainer);
		diagramContainer.add(controller.getView());
		populateDiagramAllEndpoints();
	}

	private DiagramController createDiagramController() {
		DiagramController controller = new DiagramController(800, 500);
		controller.getView().addStyleName("span8");
		controller.showGrid(true); // Display a background grid
		return controller;
	}

	private void populateDiagramAllEndpoints() {

		// clean up diagram first
		controller.clearDiagram();

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

		// clean up diagram first
		controller.clearDiagram();

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

		// clean up diagram first
		controller.clearDiagram();

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
			final LabelWithMenu endpointLabel = new LabelWithMenu(
					endpoint.endpointId, endpoint.endpointName,
					endpoint.getEpcId());

			double radians = (seperationDegrees * counter * Math.PI) / 180;
			RootPanel.get().add(
					new HTML("Degrees " + seperationDegrees * counter));
			int x = (int) (xPosCenter + radius * Math.cos(radians));
			int y = (int) (yPosCenter + radius * Math.sin(radians));

			RootPanel.get().add(new HTML("x Pos " + x + "y Pos " + y));

			counter++;

			labelDirectory.put(endpoint.endpointId, endpointLabel);
			controller.addWidget(endpointLabel, x, y);

			dragController.makeDraggable(endpointLabel);

		}

		
		

		drawLinksFromServer();
	}

	private void drawLinksFromServer() {

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

								Iterator<Entry<String, LabelWithMenu>> it = labelDirectory
										.entrySet().iterator();

								while (it.hasNext()) {

									Map.Entry<String, LabelWithMenu> originPair = (Map.Entry<String, LabelWithMenu>) it
											.next();
									LabelWithMenu originLabel = originPair
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

	private void drawLink(final LogicLinkTargetData targetEndpoint, LabelWithMenu originLabel)
	{
		
		LabelWithMenu targetLabel = labelDirectory.get(targetEndpoint
				.getTargetEndpointID());
		RootPanel
				.get()
				.add(new HTML(
						"Connecting "
								+ originLabel.endpointID
								+ " with "
								+ targetLabel.endpointID));
		Connection c1 = controller
				.drawStraightArrowConnection(
						originLabel,
						targetLabel);
		HTML decorationLabel = new HTML(
				"<a>"
						+ targetEndpoint
								.getCheckpathName()
						+ "</a>");
		controller.addDecoration(
				decorationLabel, c1);
		decorationLabel
				.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(
							ClickEvent event) {
						AppController
								.openLogicDesign(targetEndpoint
										.getCheckpathID());

					}

				});

	
	}
	
	private void addOutOfSelectionLabels(LinkedList<String> missingLabelDirectory)
	{
		
		EndpointServiceAsync endpointService = GWT
				.create(EndpointService.class);
		
		RootPanel.get().add(new HTML("Missing Label Directoy: " + missingLabelDirectory.toString()));

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
		applyFilterHandler = EventUtils.EVENT_BUS.addHandler(
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
						labelDirectory.clear();
						pWidget.clear();
						FilterSphereEndpointWidget endpointFilter = new FilterSphereEndpointWidget(
								filterAppliedEvent.getSphereID(),
								filterAppliedEvent.getEndpointID());
						pWidget.add(endpointFilter);
						pWidget.add(diagramContainer);

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

}
