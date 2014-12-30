package com.velisphere.tigerspice.client.locator.logical;

import java.util.Iterator;
import java.util.LinkedList;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.constants.LabelType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.orange.links.client.DiagramController;
import com.orange.links.client.connection.Connection;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.velisphere.tigerspice.client.analytics.AnalyticsService;
import com.velisphere.tigerspice.client.analytics.AnalyticsServiceAsync;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.appcontroller.SessionHelper;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.endpoints.EndpointServiceAsync;
import com.velisphere.tigerspice.client.helper.widgets.FilterSphereEndpointWidget;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.GeoLocationData;

public class LogicalMapWidget extends Composite {
	
	
	HorizontalPanel pWidget;
	ScrollPanel scrollWidget;
	
	
	public LogicalMapWidget()
	{
		
		
		 pWidget = new HorizontalPanel();
		 initWidget(pWidget);

		 FilterSphereEndpointWidget endpointFilter = new FilterSphereEndpointWidget();
		 pWidget.add(endpointFilter);

		 DiagramController controller = createDiagramController();
		
		 scrollWidget = new ScrollPanel();
		 scrollWidget.add(controller.getView());
		 pWidget.add(scrollWidget);
		 
		 populateDiagram(controller);
	}
	
	
	private DiagramController createDiagramController()
	{
		 DiagramController controller = new DiagramController(1000, 400);
		 controller.showGrid(true); // Display a background grid
		 return controller;
	}
	
	private void populateDiagram(final DiagramController controller) {
		
		
		EndpointServiceAsync endpointService = GWT
				.create(EndpointService.class);
		
		final PickupDragController dragController = new PickupDragController(controller.getView(), true);
		
		endpointService.getEndpointsForUser(SessionHelper.getCurrentUserID(),
				new AsyncCallback<LinkedList<EndpointData>>()
				{

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(LinkedList<EndpointData> result) {
						// TODO Auto-generated method stub
						
						Iterator<EndpointData> it = result.iterator();
						
						LinkedList<Label> labelList = new LinkedList<Label>();
						
						int xPos = 10;
						int yPos = 10;
						int count = 1;
						
						while (it.hasNext())
						{
							final EndpointData endpoint = it.next();
							Label endpointLabel = new Label(endpoint.endpointName);
							endpointLabel.setType(LabelType.INFO);
							labelList.add(endpointLabel);
							
							endpointLabel.addMouseOverHandler(new MouseOverHandler(){

								@Override
								public void onMouseOver(MouseOverEvent event) {
									AppController.openEndpoint(endpoint.endpointId);
									
								}
								
							});
							
							controller.addWidget(endpointLabel, xPos, yPos);
							
							dragController.makeDraggable(endpointLabel);
							
							yPos = yPos + 30;
							// increment horizontally only if element is a multiple of 5
							
							if (count % 5 == 0){
								xPos = xPos + 150;
								yPos = 10;
							}
							count++;
						}
						
						

						// dummy linking, real linking will have to happen based on action table
						
						Iterator<Label> lIt = labelList.iterator();
						
						while(lIt.hasNext()){
							Iterator<Label> tIt = labelList.iterator();
							Label master = lIt.next();
							
							while(tIt.hasNext()){
								Label slave = tIt.next();
								Connection c1 = controller.drawStraightArrowConnection(master, slave);		
							}
						}
						
						
						
						
					}
			
				});
		
				// Register the dragController in gwt-links
				controller.registerDragController(dragController);
		
		
	}

}
