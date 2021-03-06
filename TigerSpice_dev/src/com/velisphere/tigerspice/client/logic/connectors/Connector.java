package com.velisphere.tigerspice.client.logic.connectors;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PopupPanel;
import com.velisphere.tigerspice.client.helper.UuidService;
import com.velisphere.tigerspice.client.helper.UuidServiceAsync;

public class Connector extends PopupPanel {

	String checkUUID;
	String actionUUID;
	String uuid;

	
	
	public Connector()
	{
		super();
		
		
		createUUID();
	}
	
	protected void createActionUUID()
	{
		UuidServiceAsync uuidService = GWT
				.create(UuidService.class);
		
		uuidService.getUuid(new AsyncCallback<String>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				actionUUID = result;
			}
			
		});
		
	}
	
	protected void createCheckUUID()
	{
		UuidServiceAsync uuidService = GWT
				.create(UuidService.class);
		
		uuidService.getUuid(new AsyncCallback<String>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				checkUUID = result;
			}
			
		});
		
	}
	
	protected void createUUID()
	{
		UuidServiceAsync uuidService = GWT
				.create(UuidService.class);
		
		uuidService.getUuid(new AsyncCallback<String>(){

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

	
	public String getCheckUUID() {
		return checkUUID;
	}
	
	public String getActionUUID() {
		return actionUUID;
	}
	
	public String getUUID() {
		return uuid;
	}
	
	  public void close()
	  {
		  
		  this.removeFromParent();
	  }

}
