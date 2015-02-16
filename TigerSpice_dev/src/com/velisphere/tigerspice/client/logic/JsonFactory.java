package com.velisphere.tigerspice.client.logic;

import java.util.Iterator;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.velisphere.tigerspice.client.logic.draggables.LogicCheck;
import com.velisphere.tigerspice.client.logic.draggables.PhysicalItem;
import com.velisphere.tigerspice.client.logic.layoutWidgets.LogicCanvas;
import com.velisphere.tigerspice.client.rules.CheckPathService;
import com.velisphere.tigerspice.client.rules.CheckPathServiceAsync;

public class JsonFactory {

	LogicCanvas canvas;
	
	public JsonFactory(LogicCanvas canvas)
	{
		this.canvas = canvas;
		
	}
	
	public void getJSON()
	{
	//create json for all physical items
		
		Iterator<PhysicalItem> it = canvas.getPhysicalItems().iterator();
		
		while (it.hasNext())
		{
		
			PhysicalItem current = it.next();
			
			
			CheckPathServiceAsync checkPathService = GWT
					.create(CheckPathService.class);
			
			
			checkPathService.createJsonFromPhysical(current.getSerializableRepresentation(), new AsyncCallback<String>(){

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
					RootPanel.get().add(new HTML("ERROR " + caught.getMessage()));
					
				}

				@Override
				public void onSuccess(String result) {
					// TODO Auto-generated method stub
					
					RootPanel.get().add(new HTML("RUBEL " + result));
				
				}

				
				});
		}
		
		
		//create json for all logic checks
		
		Iterator<LogicCheck> il = canvas.getLogicChecks().iterator();
		
		while (il.hasNext())
		{
		
			LogicCheck current = il.next();
			
			
			CheckPathServiceAsync checkPathService = GWT
					.create(CheckPathService.class);
			
			
			checkPathService.createJsonFromLogical(current.getSerializableRepresentation(), new AsyncCallback<String>(){

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
					RootPanel.get().add(new HTML("ERROR " + caught.getMessage()));
					
				}

				@Override
				public void onSuccess(String result) {
					// TODO Auto-generated method stub
					
					RootPanel.get().add(new HTML("JUBEL " + result));
				
				}

				
				});
		}
		
	}
}
