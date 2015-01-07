package com.velisphere.tigerspice.client.locator.logical;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.orange.links.client.DiagramController;
import com.orange.links.client.connection.Connection;
import com.velisphere.tigerspice.client.actions.ActionService;
import com.velisphere.tigerspice.client.actions.ActionServiceAsync;
import com.velisphere.tigerspice.client.appcontroller.SessionHelper;
import com.velisphere.tigerspice.client.checks.CheckService;
import com.velisphere.tigerspice.client.checks.CheckServiceAsync;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.endpoints.EndpointServiceAsync;
import com.velisphere.tigerspice.client.helper.widgets.FilterSphereEndpointWidget;
import com.velisphere.tigerspice.shared.ActionData;
import com.velisphere.tigerspice.shared.CheckData;
import com.velisphere.tigerspice.shared.EndpointData;

public class LogicalMapWidget extends Composite {
	
	
	HorizontalPanel pWidget;
	ScrollPanel scrollWidget;
	DiagramController controller;
	HashMap<String, LabelWithMenu> labelDirectory;
	HashMap<String, LinkedList<String>> linkDirectory;
	
	
	public LogicalMapWidget()
	{
		
		
		 pWidget = new HorizontalPanel();
		 linkDirectory = new HashMap<String, LinkedList<String>>();
		 labelDirectory = new HashMap<String, LabelWithMenu>();
		 controller = createDiagramController();
		 initWidget(pWidget);
		 
		 

		 FilterSphereEndpointWidget endpointFilter = new FilterSphereEndpointWidget();
		 pWidget.add(endpointFilter);

		 
		
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
						
						
						
						int xPos = 10;
						int yPos = 10;
						int count = 1;
						
						while (it.hasNext())
						{
							final EndpointData endpoint = it.next();
							final LabelWithMenu endpointLabel = new LabelWithMenu(endpoint.endpointId, endpoint.endpointName);
							
							labelDirectory.put(endpoint.endpointId, endpointLabel);
							controller.addWidget(endpointLabel, xPos, yPos);
							
							dragController.makeDraggable(endpointLabel);
							
							yPos = yPos + 60;
							// increment horizontally only if element is a multiple of 5
							
							if (count % 5 == 0){
								xPos = xPos + 150;
								yPos = 10;
							}
							count++;
						}
						
						drawLinksFromServer();

						// dummy linking, real linking will have to happen based on action table
						/*
						
						
						Iterator<Entry<String, LabelWithMenu>> lIt = labelDirectory.entrySet().iterator();
						
						while(lIt.hasNext()){
							Map.Entry<String, LabelWithMenu> pair = (Map.Entry<String, LabelWithMenu>)lIt.next();
							
							getLinkedCheckpaths(pair.getValue());
							
							
						}
						*/
						
						
						
						
						
					}
			
				});
		
				// Register the dragController in gwt-links
				controller.registerDragController(dragController);
		
		
	}
	
	
private void getLinkedCheckpaths(final LabelWithMenu label) {
		
		HTML seperator = new HTML("---- Checks for Endpoint -------> " + label.endpointID);
		RootPanel.get().add(seperator);
		
	
		CheckServiceAsync checkService = GWT
				.create(CheckService.class);
		
		checkService.getChecksForEndpointID(label.endpointID,
				new AsyncCallback<LinkedList<CheckData>>()
				{

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(LinkedList<CheckData> result) {
						HTML headML = new HTML("---- Found -------> " + result.toString());
						RootPanel.get().add(headML);
						Iterator<CheckData> it = result.iterator();
						while (it.hasNext()){
							CheckData check = it.next();
							HTML checkML = new HTML("Checked by " + check.checkName + " in CP " + check.checkpathId);
							getLinkedEndpoints(label.endpointID, check.checkpathId);
							RootPanel.get().add(checkML);
						}
						
					}
				
				});
	}

private void getLinkedEndpoints(final String endpointID, String checkpathID)
{
	HTML seperator = new HTML("---- Linked Targets for Endpoint -------> " + checkpathID);
	RootPanel.get().add(seperator);
	

	ActionServiceAsync actionService = GWT
			.create(ActionService.class);
	
	actionService.getActionsForCheckpathID(checkpathID,
			new AsyncCallback<LinkedList<ActionData>>()
			{

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				
				
				@Override
				public void onSuccess(LinkedList<ActionData> result) {
					 
					HTML headML = new HTML("---- Found -------> " + result.toString());
					RootPanel.get().add(headML);
					Iterator<ActionData> it = result.iterator();
					while (it.hasNext()){
						ActionData action = it.next();
						HTML checkML = new HTML("Linked to " + action.getTargetEndpointID() + " via action " + action.getActionID());
						RootPanel.get().add(checkML);
						if (linkDirectory.containsKey(endpointID)){
							linkDirectory.get(endpointID).add(action.getTargetEndpointID());
						} else
						{
							LinkedList<String> entry = new LinkedList<String>();
							entry.add(action.getTargetEndpointID());
							linkDirectory.put(endpointID, entry);
						}
					}
					
					RootPanel.get().add(new HTML("Added to directory: " + linkDirectory.values()));
					drawLinks();
				}
			
			});
	
	
	
}

private void drawLinks()
{

	Iterator<Entry<String, LabelWithMenu>> it = labelDirectory.entrySet().iterator();
		
	
	while (it.hasNext()){
	
		Map.Entry<String, LabelWithMenu> pair = (Map.Entry<String, LabelWithMenu>)it.next();
		
		LabelWithMenu originLabel = pair.getValue();
		LinkedList<String> targets = linkDirectory.get(originLabel.endpointID);
		Iterator<String> tIt = targets.iterator();
		while (tIt.hasNext()){
			String target = tIt.next();
			LabelWithMenu targetLabel = labelDirectory.get(target);
			Connection c1 = controller.drawStraightArrowConnection(originLabel, targetLabel);	
		}
		
		
	}
	
}

private void drawLinksFromServer()
{

	LinkedList<String> sourceEndpointIDs = new LinkedList<String>(labelDirectory.keySet());
	
	EndpointServiceAsync endpointService = GWT
			.create(EndpointService.class);
	
	
	endpointService.getLinksForEndpointList(sourceEndpointIDs,
			new AsyncCallback<HashMap<String, LinkedList<String>>>()
			{

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(HashMap<String, LinkedList<String>> result) {

					RootPanel.get().add(new HTML("Raw data " + result.toString()));
					
					Iterator<Entry<String, LabelWithMenu>> it = labelDirectory.entrySet().iterator();
					
					
					while (it.hasNext()){
					
						Map.Entry<String, LabelWithMenu> originPair = (Map.Entry<String, LabelWithMenu>)it.next();
						LabelWithMenu originLabel = originPair.getValue();
						
						RootPanel.get().add(new HTML("Origin data " + originPair.getValue().endpointID));
						
						LinkedList<String> targetEndpoints = result.get(originPair.getKey());
						
						RootPanel.get().add(new HTML("Raw target data " + targetEndpoints.toString()));
												
						Iterator<String> iTt = targetEndpoints.iterator();
						
						while (iTt.hasNext()){
							
							
							LabelWithMenu targetLabel = labelDirectory.get(iTt.next());
							RootPanel.get().add(new HTML("Connecting " + originLabel.endpointID + " with " + targetLabel.endpointID));
							Connection c1 = controller.drawStraightArrowConnection(originLabel, targetLabel);	
						}
						
						
					}
					
					
				}
		
			});
	
}


// MOVE ALL THIS CRAP (to build the links) TO THE SERVER SIDE AND MAKE IT ONE SINGLE CALLBACK THAT GETS CALLED ONCE ALL LABELS ARE DRAWN!

}
