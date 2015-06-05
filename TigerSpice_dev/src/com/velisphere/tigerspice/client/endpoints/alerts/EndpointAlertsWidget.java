package com.velisphere.tigerspice.client.endpoints.alerts;

import com.github.gwtbootstrap.client.ui.Paragraph;
import com.github.gwtbootstrap.client.ui.TabPane;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.amqp.AMQPService;
import com.velisphere.tigerspice.client.amqp.AMQPServiceAsync;
import com.velisphere.tigerspice.client.analytics.AnalyticsService;
import com.velisphere.tigerspice.client.analytics.AnalyticsServiceAsync;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.endpointclasses.EPCService;
import com.velisphere.tigerspice.client.endpointclasses.EPCServiceAsync;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.endpoints.EndpointServiceAsync;
import com.velisphere.tigerspice.client.endpoints.info.EndpointInformationWidget;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.vendors.VendorService;
import com.velisphere.tigerspice.client.vendors.VendorServiceAsync;
import com.velisphere.tigerspice.shared.EPCData;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.VendorData;

public class EndpointAlertsWidget extends Composite {

	String endpointID;
	String epcID;
	TabPane enclosingPane;
	
	

	private static EndpointAlertsWidgetUiBinder uiBinder = GWT
			.create(EndpointAlertsWidgetUiBinder.class);

	interface EndpointAlertsWidgetUiBinder extends
			UiBinder<Widget, EndpointAlertsWidget> {
	}

	public EndpointAlertsWidget(String endpointID, TabPane enclosingPane) {
		this.endpointID = endpointID;
		this.enclosingPane = enclosingPane;
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	
	
	
		
	@UiHandler("btnAddAlert")
	void sendPingRequest (ClickEvent event)  {
		
		enclosingPane.clear();
		NewAlertWidget newAlertWidget = new NewAlertWidget(endpointID);
		enclosingPane.add(newAlertWidget);
	}
	
	
	@UiHandler("btnRefresh")
	void onClick(ClickEvent e) {
	}

}
