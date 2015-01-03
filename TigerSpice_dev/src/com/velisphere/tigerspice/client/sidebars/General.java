package com.velisphere.tigerspice.client.sidebars;

import java.util.Iterator;
import java.util.LinkedList;

import com.github.gwtbootstrap.client.ui.ListBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.appcontroller.SessionHelper;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.endpoints.EndpointServiceAsync;
import com.velisphere.tigerspice.shared.EndpointData;

public class General extends Composite {

	@UiField Anchor ancProvisioning;
	@UiField ListBox ddlShortcut;
	
	private static GeneralUiBinder uiBinder = GWT.create(GeneralUiBinder.class);

	interface GeneralUiBinder extends UiBinder<Widget, General> {
	}

	public General() {
		initWidget(uiBinder.createAndBindUi(this));
		ancProvisioning.setHref("#");
		populateShortcuts();

	}

	
	void populateShortcuts(){
		EndpointServiceAsync rpcServiceEndpoint = GWT.create(EndpointService.class);
		//ddlShortcut.setWidth("150px");
		ddlShortcut.addItem("Select Endpoint");
		rpcServiceEndpoint.getEndpointsForUser(SessionHelper.getCurrentUserID(), new AsyncCallback<LinkedList<EndpointData>>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(LinkedList<EndpointData> result) {
				Iterator<EndpointData> it = result.iterator();
				while(it.hasNext()){
					EndpointData endpointData = it.next();
					ddlShortcut.addItem(endpointData.endpointName, endpointData.endpointId);
				}
				
			}

		});
		
		ddlShortcut.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {

				AppController.openEndpoint(ddlShortcut.getValue());
			}
		});

	}
	
	
	@UiHandler("ancProvisioning")
	void openProvisioning (ClickEvent event) {
	
		AppController.openProvisioningWizard();
		
		
	}

}
