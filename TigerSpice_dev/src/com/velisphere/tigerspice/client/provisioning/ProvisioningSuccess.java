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
	
	
	@UiHandler("ancFound")
	void openTakeOwnership(ClickEvent event){
		AppController.openEndpointManager();
	}

}
