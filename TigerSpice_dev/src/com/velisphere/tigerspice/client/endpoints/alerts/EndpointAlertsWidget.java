package com.velisphere.tigerspice.client.endpoints.alerts;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import com.github.gwtbootstrap.client.ui.Heading;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.github.gwtbootstrap.client.ui.TabPane;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
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
import com.velisphere.tigerspice.client.properties.PropertyService;
import com.velisphere.tigerspice.client.properties.PropertyServiceAsync;
import com.velisphere.tigerspice.client.vendors.VendorService;
import com.velisphere.tigerspice.client.vendors.VendorServiceAsync;
import com.velisphere.tigerspice.shared.AlertData;
import com.velisphere.tigerspice.shared.EPCData;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.PropertyData;
import com.velisphere.tigerspice.shared.VendorData;

public class EndpointAlertsWidget extends Composite {

	@UiField
	ListBox lstAlerts;
	
	@UiField
	Heading pgpAlertName;
	
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
		populateAlertList();
		lstAlerts.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				populateAlertDetails(lstAlerts.getValue());
				
			}
		});
	}
	
	
	
	private void populateAlertList() {

		final AnimationLoading animationLoading = new AnimationLoading();

		animationLoading.showLoadAnimation("Loading Endpoint");

		EndpointServiceAsync endpointService = GWT
				.create(EndpointService.class);

		endpointService.getAllAlerts(endpointID,
				new AsyncCallback<LinkedHashMap<String, String>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(LinkedHashMap<String, String> result) {
						// TODO Auto-generated method stub
						animationLoading.removeLoadAnimation();
						Iterator<Entry<String, String>> it = result.entrySet().iterator();
						if (it.hasNext() == false) {

							Entry<String, String> entry = it.next();
							
							/*
							pgpPropertyName
									.setText("This endpoint can't perform any actions.");
							pgpUnitHeader.setText("");
							pgpLastValueHeader.setText("");
							pgpLastUpdateHeader.setText("");
							pgpUnit.setText("");
							pgpCheckHeader.setText("");
							pgpActionHeader.setText("");
							pgpLogicHeader.setText("");
							pgpTriggerHeader.setText("");
							pgpLastValue.setText("");
							pgpLastUpdate.setText("");
							pgpCheck.setText("");
							pgpAction.setText("");
							pgpLogic.setText("");
							pgpTrigger.setText("");
							btnDataTrail.setVisible(false);
							btnSetNewValue.setVisible(false);
							*/
						}
						while (it.hasNext()) {

							Entry<String, String> entry = it.next();
							lstAlerts.addItem(entry.getValue(),
									entry.getKey());
							lstAlerts.setSelectedIndex(0);

							populateAlertDetails(lstAlerts.getValue());
							//populatePropertyData(lstSensors.getValue());

						}

					}

				});

		lstAlerts.setVisibleItemCount(10);

	}

	
	void populateAlertDetails(String alertID)
	{
		
		EndpointServiceAsync endpointService = GWT
				.create(EndpointService.class);

		endpointService.getAlertDetails(alertID, new AsyncCallback<AlertData>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(AlertData result) {
				// TODO Auto-generated method stub
				
				pgpAlertName.setText(result.getAlertName());
				
			}
		
		});
		
		
		
		
		
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
