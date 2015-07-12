package com.velisphere.tigerspice.client.endpoints.alerts;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import com.github.gwtbootstrap.client.ui.Button;
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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
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
import com.velisphere.tigerspice.client.helper.VeliConstants;
import com.velisphere.tigerspice.client.logic.CheckPathService;
import com.velisphere.tigerspice.client.logic.CheckPathServiceAsync;
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
	@UiField
	Paragraph pgpPropMonitored;
	@UiField
	Paragraph pgpOperator;
	@UiField
	Paragraph pgpThreshold;
	@UiField
	Paragraph pgpType;
	@UiField
	Paragraph pgpRecipient;
	@UiField
	Paragraph pgpText;
	
	@UiField
	Paragraph pgpPropMonitoredHeader;
	@UiField
	Paragraph pgpOperatorHeader;
	@UiField
	Paragraph pgpThresholdHeader;
	@UiField
	Paragraph pgpTypeHeader;
	@UiField
	Paragraph pgpRecipientHeader;
	@UiField
	Paragraph pgpTextHeader;
	@UiField
	Button btnDelete;
	@UiField
	Button btnEdit;
	@UiField
	Button btnAddAlert;
	

	
	String endpointID;
	String epcID;
	TabPane enclosingPane;
	String currentCheckpathID;
	String currentAlertID;
	
	

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

		endpointService.getAllAlertsForEndpoint(endpointID,
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

							pgpAlertName.setText("No Alerts found for this Endpoint.");
							
							 lstAlerts.setVisible(false);
							
							 pgpPropMonitored.setVisible(false);
							 pgpOperator.setVisible(false);
							 pgpThreshold.setVisible(false);
							 pgpType.setVisible(false);
							 pgpRecipient.setVisible(false);
							 pgpText.setVisible(false);
							 pgpPropMonitoredHeader.setVisible(false);
							 pgpOperatorHeader.setVisible(false);
							 pgpThresholdHeader.setVisible(false);
							 pgpTypeHeader.setVisible(false);
							 pgpRecipientHeader.setVisible(false);
							 pgpTextHeader.setVisible(false);
							 btnDelete.setVisible(false);
							 btnEdit.setVisible(false);
							
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
		
		this.currentAlertID = alertID;
		
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
				pgpOperator.setText(result.getOperator());
				pgpType.setText(result.getType());
				pgpRecipient.setText(result.getRecipient());
				pgpThreshold.setText(result.getThreshold());
				pgpText.setText(result.getText());
				currentCheckpathID = result.getCheckpathID();
				
				populatePropertyName(result.getProperty());
				
			}
		
		});
		
		
		
		
		
	}
	
	void populatePropertyName(String propertyID)
	{
		PropertyServiceAsync propertyService = GWT
				.create(PropertyService.class);

		propertyService.getPropertyName(propertyID, new AsyncCallback<String>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				pgpPropMonitored.setText(result);				
			}
		});
	}
	
		
	@UiHandler("btnAddAlert")
	void sendPingRequest (ClickEvent event)  {
		
		enclosingPane.clear();
		NewAlertWidget newAlertWidget = new NewAlertWidget(endpointID);
		enclosingPane.add(newAlertWidget);
	}
	
	
	@UiHandler("btnEdit")
	void onEdit(ClickEvent e) {
		
		enclosingPane.clear();
		EditAlertWidget editAlertWidget = new EditAlertWidget(endpointID, currentAlertID);
		RootPanel.get().add(new HTML("Edit for " + currentAlertID));
		enclosingPane.add(editAlertWidget);
		
	}

	@UiHandler("btnDelete")
	void onDelete(ClickEvent e) {
		
		EndpointServiceAsync endpointService = GWT
				.create(EndpointService.class);

		
		
		endpointService.deleteAlert(currentAlertID, currentCheckpathID, new AsyncCallback<String>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				RootPanel.get().add(new HTML("DELETE SHOULD HAVE SUCCEEDED"));
				AppController.openEndpoint(endpointID, VeliConstants.ENDPOINT_VIEWMODE_ALERTS);
			}
			
		});

		

		
	}

	
}
