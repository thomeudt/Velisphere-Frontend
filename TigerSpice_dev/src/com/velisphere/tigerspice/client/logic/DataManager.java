package com.velisphere.tigerspice.client.logic;

import java.util.Iterator;
import java.util.LinkedList;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.velisphere.tigerspice.client.appcontroller.SessionHelper;
import com.velisphere.tigerspice.client.checks.CheckService;
import com.velisphere.tigerspice.client.checks.CheckServiceAsync;
import com.velisphere.tigerspice.client.logic.connectors.ConnectorSensorActor;
import com.velisphere.tigerspice.client.logic.draggables.PhysicalItem;
import com.velisphere.tigerspice.client.logic.layoutWidgets.LogicCanvas;
import com.velisphere.tigerspice.client.rules.CheckPathService;
import com.velisphere.tigerspice.client.rules.CheckPathServiceAsync;
import com.velisphere.tigerspice.shared.ActionObject;

public class DataManager {

	
	LogicCanvas canvas;
	
	public DataManager(LogicCanvas canvas)
	{
		this.canvas = canvas;
		
	}
	
	public void processCheckPath(String checkpathName)
	{
		
		// send to database
		
		CheckPathServiceAsync checkpathService = GWT
				.create(CheckPathService.class);
	
		checkpathService.addNewCheckpath(checkpathName, SessionHelper.getCurrentUserID(), canvas.getUUID(), new AsyncCallback<String>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				RootPanel.get().add(new HTML("Result from attempt to generate check and action P2P: " + result));
			}
			
		});
	
		
	}
	
	public void processP2P()
	{
		Iterator<ConnectorSensorActor> it = canvas.getConnectorsSensorActor().iterator();
		
		while (it.hasNext())
		{
		
			ConnectorSensorActor current = it.next();
			
			// build action object
			ActionObject action = new ActionObject();
			action.actionID = current.getActionUUID();
			action.actionName = "unused";
			action.endpointClassID = current.getActor().getEndpointClassID();
			action.endpointID = current.getActor().getEndpointID();
			action.endpointName = current.getActor().getContentRepresentation();
			action.propertyID = current.getActor().getPropertyID();
			action.propertyIdIntake = current.getSensor().getPropertyID();
			action.sensorEndpointID = current.getSensor().getEndpointID();
			action.settingSourceIndex = current.getSourceIndex();
			action.validValueIndex = current.getTypicalValueIndex();
			action.manualValue = current.getManualValue();
			
			// TODO this can be simplified - we do not need to take care of multiple actions in new setup
			
			LinkedList<ActionObject> actions = new LinkedList<ActionObject>();
			
			actions.add(action);
			
			// send to database
			
			CheckServiceAsync checkService = GWT
					.create(CheckService.class);
			
			checkService.addNewCheck(current.getCheckUUID(), current.getSensor().getEndpointID(), current.getSensor().getPropertyID(), current.getCheckValue(), current.getOperator(), "unused", canvas.getUUID(), actions, new AsyncCallback<String>(){

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(String result) {
					// TODO Auto-generated method stub
				
					RootPanel.get().add(new HTML("Result from attempt to generate check and action P2P: " + result));
				}
				
			});
			
			
			}
	}
	
}
