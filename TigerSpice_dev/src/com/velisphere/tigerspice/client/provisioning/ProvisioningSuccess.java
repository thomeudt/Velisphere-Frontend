package com.velisphere.tigerspice.client.provisioning;

import java.util.LinkedList;

import com.github.gwtbootstrap.client.ui.Breadcrumbs;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Image;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.analytics.AnalyticsService;
import com.velisphere.tigerspice.client.analytics.AnalyticsServiceAsync;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.endpoints.EndpointServiceAsync;
import com.velisphere.tigerspice.client.images.Images;
import com.velisphere.tigerspice.shared.AnalyticsRawData;
import com.velisphere.tigerspice.shared.UnprovisionedEndpointData;

public class ProvisioningSuccess extends Composite {

	@UiField Breadcrumbs brdMain;
	NavLink bread0;
	NavLink bread1;
	@UiField TextBox txtSearchID;
	@UiField Button btnSearchID;
	@UiField Anchor ancFound;
	@UiField Image imgFound;
	
	String uEPID;
	String endpointclassID;
	String identifier;
	String endpointclassName;
	
	private static ProvisioningSuccessUiBinder uiBinder = GWT
			.create(ProvisioningSuccessUiBinder.class);

	interface ProvisioningSuccessUiBinder extends
			UiBinder<Widget, ProvisioningSuccess> {
	}

	public ProvisioningSuccess() {
		
		initWidget(uiBinder.createAndBindUi(this));
		bread0 = new NavLink();
		bread0.setText("Home");
		brdMain.add(bread0);

		bread1 = new NavLink();
		bread1.setText("Device Provisioning Wizard");
		brdMain.add(bread1);

	}
	
	@UiHandler("btnSearchID")
	void searchDeviceID (ClickEvent event) {
	
		
		final EndpointServiceAsync endpointService = GWT
				.create(EndpointService.class);

		String searchID = new String("");
		searchID = txtSearchID.getText();
		System.out.println("Search for unprovisioned ID: " + searchID);
		
		endpointService.getUnprovisionedEndpoints(searchID, searchID,
				new AsyncCallback<UnprovisionedEndpointData>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(UnprovisionedEndpointData result) {
						// TODO Auto-generated method stub

						
				
						
						if (result == null){
							ancFound.setText("No recently connected endpoint found for this identifier. Click here for further information.");
							imgFound.setResource(Images.INSTANCE.blocked());
						} else
						{
							ancFound.setText("Device of type " + result.getEndpointClassName() + ", identified as " + result.identifier + " was connected about " + result.getSecondsSinceConnection() + " seconds ago. Click here to take ownership.");
							imgFound.setResource(Images.INSTANCE.paperplane());
							ancFound.setHref("#");
							uEPID = result.getUepid();
							identifier = result.getIdentifier();
							endpointclassID = result.getEpcId();
							endpointclassName = result.getEndpointClassName();
						}
						
					}
				
		});
	}
	
	@UiHandler("ancFound")
	void openTakeOwnership(ClickEvent event){
		AppController.openTakeOwnership(uEPID, identifier, endpointclassID, endpointclassName);
	}

}
