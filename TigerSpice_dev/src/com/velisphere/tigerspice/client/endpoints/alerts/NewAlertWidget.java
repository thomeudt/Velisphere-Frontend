package com.velisphere.tigerspice.client.endpoints.alerts;

import java.util.Iterator;
import java.util.LinkedList;

import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.appcontroller.SessionHelper;
import com.velisphere.tigerspice.client.checks.CheckService;
import com.velisphere.tigerspice.client.checks.CheckServiceAsync;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.endpoints.EndpointServiceAsync;
import com.velisphere.tigerspice.client.endpoints.alerts.EndpointAlertsWidget.EndpointAlertsWidgetUiBinder;
import com.velisphere.tigerspice.client.helper.ActionSourceConfig;
import com.velisphere.tigerspice.client.helper.DatatypeConfig;
import com.velisphere.tigerspice.client.helper.UuidService;
import com.velisphere.tigerspice.client.helper.UuidServiceAsync;
import com.velisphere.tigerspice.client.helper.VeliConstants;
import com.velisphere.tigerspice.client.logic.CheckPathService;
import com.velisphere.tigerspice.client.logic.CheckPathServiceAsync;
import com.velisphere.tigerspice.client.logic.connectors.ConnectorSensorActor;
import com.velisphere.tigerspice.client.properties.PropertyService;
import com.velisphere.tigerspice.client.properties.PropertyServiceAsync;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassService;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassServiceAsync;
import com.velisphere.tigerspice.shared.ActionObject;
import com.velisphere.tigerspice.shared.AlertData;
import com.velisphere.tigerspice.shared.PropertyClassData;
import com.velisphere.tigerspice.shared.PropertyData;

public class NewAlertWidget extends Composite {

	String endpointID;
	String epcID;
	String uuidAction;
	String uuidCheck;
	String uuidCanvas;
	String uuidCheckpath;
	UuidServiceAsync uuidService;
	
	@UiField
	ListBox lbxProperty;
	
	@UiField
	ListBox lbxOperator;
	
	@UiField
	ListBox lbxAlertType;
	
	@UiField
	TextBox txtAlertName;
	
	@UiField
	TextBox txtValue;
	
	@UiField
	TextBox txtRecipient;

	@UiField
	TextBox txtText;

	
	private static NewAlertWidgetUiBinder uiBinder = GWT
			.create(NewAlertWidgetUiBinder.class);

	interface NewAlertWidgetUiBinder extends
			UiBinder<Widget, NewAlertWidget> {
	}

	public NewAlertWidget(String endpointID) {
		this.endpointID = endpointID;
		initWidget(uiBinder.createAndBindUi(this));
		uuidService = GWT
				.create(UuidService.class);
		
		populatePropertyList();
		populateAlertTypeList();
	}
	
	
	private void populatePropertyList()
	{
		PropertyServiceAsync propertyService = GWT
				.create(PropertyService.class);

		propertyService.getSensorPropertiesForEndpointID(endpointID,
				new AsyncCallback<LinkedList<PropertyData>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(LinkedList<PropertyData> result) {
						// TODO Auto-generated method stub
						
						RootPanel.get().add(new HTML("Gotten results."));
								
						Iterator<PropertyData> it = result.iterator();
						if (it.hasNext() == false) {
							
							lbxProperty.addItem("No monitorable sensors.");
													}
						while (it.hasNext()) {

							PropertyData propData = new PropertyData();
							propData = it.next();
							lbxProperty.addItem(propData.propertyName, propData.propertyId);
							
						}
						
						populateLbxOperator(lbxProperty.getValue());
						
						lbxProperty.addChangeHandler(new ChangeHandler(){

							@Override
							public void onChange(ChangeEvent event) {
								populateLbxOperator(lbxProperty.getValue());
								
							}
							
						});

					}

				});

	}
	
	private void populateAlertTypeList()
	{
		
		lbxAlertType.addItem("E-Mail");
		
	}
	
	
	private void populateLbxOperator(String propertyID)
	{
		
		lbxOperator.clear();
		
		PropertyServiceAsync propertyService = GWT
				.create(PropertyService.class);

		propertyService.getPropertyClass(propertyID, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(String result) {
		
				PropertyClassServiceAsync propertyClassService = GWT
						.create(PropertyClassService.class);
				
				propertyClassService.getPropertyClassForPropertyClassID(
						result, new AsyncCallback<PropertyClassData>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void onSuccess(PropertyClassData result) {
								// TODO Auto-generated method stub

								DatatypeConfig dataTypeConfig = new DatatypeConfig();

								if (result.propertyClassDatatype
										.equalsIgnoreCase("string")) {
									Iterator<String> it = dataTypeConfig
											.getTextOperators().iterator();
									while (it.hasNext()) {
										String listItem = it.next();
										lbxOperator.addItem(listItem);

									}
								}

								else

								if (result.propertyClassDatatype
										.equalsIgnoreCase("byte")) {
									Iterator<String> it = dataTypeConfig
											.getNumberOperators().iterator();
									while (it.hasNext()) {
										String listItem = it.next();
										lbxOperator.addItem(listItem);

									}
								}

								else

								if (result.propertyClassDatatype
										.equalsIgnoreCase("long")) {
									Iterator<String> it = dataTypeConfig
											.getNumberOperators().iterator();
									while (it.hasNext()) {
										String listItem = it.next();
										lbxOperator.addItem(listItem);

									}
								}

								else

								if (result.propertyClassDatatype
										.equalsIgnoreCase("float")) {
									Iterator<String> it = dataTypeConfig
											.getNumberOperators().iterator();
									while (it.hasNext()) {
										String listItem = it.next();
										lbxOperator.addItem(listItem);

									}
								}

								else

								if (result.propertyClassDatatype
										.equalsIgnoreCase("double")) {
									Iterator<String> it = dataTypeConfig
											.getNumberOperators().iterator();
									while (it.hasNext()) {
										String listItem = it.next();
										lbxOperator.addItem(listItem);

									}
								}

								else

								if (result.propertyClassDatatype
										.equalsIgnoreCase("boolean")) {
									Iterator<String> it = dataTypeConfig
											.getBooleanOperators().iterator();
									while (it.hasNext()) {
										String listItem = it.next();
										lbxOperator.addItem(listItem);

									}
								}
								
								else lbxOperator.addItem("Invalid Endpoint Configuration");
								
							}

						});


				
			}
			
		});
		
				
	}

	
		
	@UiHandler("btnAdd")
	void sendPingRequest (ClickEvent event)  {
		
	
		
		createCheckpath();
	
			
	}

		
	void createCheckpath()
	{
		// create UUID
		
		
		uuidService.getUuid(new AsyncCallback<String>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				uuidCheckpath = result;
				
				// send checkpath to database
				CheckPathServiceAsync checkpathService = GWT
						.create(CheckPathService.class);
				createAlertEntry(uuidCheckpath);

				checkpathService.addNewCheckpath(txtAlertName.getText(),
						SessionHelper.getCurrentUserID(),
						uuidCheckpath, "ALERT",
						new AsyncCallback<String>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

								RootPanel
										.get()
										.add(new HTML(
												"Error from attempt to create checkpath: "
														+ caught.getMessage()));
							}

							@Override
							public void onSuccess(String result) {
								// TODO Auto-generated method stub
								RootPanel.get().add(
										new HTML(
												"Result from attempt to create checkpath: "
														+ result));
								createActions();
								
							}

						});
				
				
				
			}
			
		});


	}
	
	
	void createAlertEntry(String checkpathID)
	{
		
		AlertData alert = new AlertData();
		
		alert.setAlertName(txtAlertName.getText());
		alert.setCheckpathID(checkpathID);
		alert.setEndpointID(endpointID);
		alert.setOperator(lbxOperator.getValue());
		alert.setProperty(lbxProperty.getValue());
		alert.setRecipient(txtRecipient.getValue());
		alert.setText(txtText.getValue());
		alert.setThreshold(txtValue.getValue());
		alert.setType(lbxAlertType.getValue());
		alert.setUserID(SessionHelper.getCurrentUserID());
		
		EndpointServiceAsync endpointService = GWT
				.create(EndpointService.class);

		endpointService.addNewAlert(alert, new AsyncCallback<String>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				
			}
			
		});
				
		
			
	
	}

	
	void createActions()
	{
		uuidService.getUuid(new AsyncCallback<String>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				uuidAction = result;
				RootPanel.get().add(new HTML("P2P: started"));

				RootPanel.get().add(new HTML("P2P: fetched connector"));

				// build action object
				ActionObject action = new ActionObject();
				action.actionID = uuidAction;
				RootPanel.get().add(new HTML("P2P: ID processed"));
				action.actionName = "unused";
				RootPanel.get().add(new HTML("P2P: Name processed"));
				action.endpointClassID = VeliConstants.MAILSERVICE_EPCID;
				RootPanel.get().add(new HTML("P2P: EPC processed"));
				action.endpointID = VeliConstants.MAILSERVICE_EPID;
				RootPanel.get().add(new HTML("P2P: AEPID processed"));
				action.endpointName = "MAILSERVICE";
				RootPanel.get().add(new HTML("P2P: Content processed"));
				action.propertyID = VeliConstants.MAILSERVICE_PROPID;
				RootPanel.get().add(new HTML("P2P: APID processed"));
				action.propertyIdIntake = lbxProperty.getValue();
				RootPanel.get().add(new HTML("P2P: SPID processed"));
				action.sensorEndpointID = endpointID;
				RootPanel.get().add(new HTML("P2P: SEPID processed"));
				action.valueFromInboundPropertyID = "no";
				action.manualValue = txtRecipient.getText();
				RootPanel.get().add(new HTML("P2P: Source processed"));
				
				RootPanel.get().add(new HTML("P2P: built action object"));

				// TODO this can be simplified - we do not need to take care of
				// multiple actions in new setup

				LinkedList<ActionObject> actions = new LinkedList<ActionObject>();

				actions.add(action);

				RootPanel.get().add(new HTML("P2P: built action object list"));
				
				createCheck(actions);

			}
			
		});
		
	}
	
	
	void createCheck(final LinkedList<ActionObject> actions)
	{
		// send check to database
		
		uuidService.getUuid(new AsyncCallback<String>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				uuidCheck = result;
				CheckServiceAsync checkService = GWT.create(CheckService.class);

				checkService.addNewCheck(uuidCheck, endpointID, lbxProperty.getValue(), txtValue.getText(), lbxOperator.getItemText(lbxOperator.getSelectedIndex()), "unused", uuidCheckpath, actions,
						new AsyncCallback<String>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onSuccess(String result) {
								// TODO Auto-generated method stub

								RootPanel.get().add(
										new HTML(
												"Result from attempt to generate check and action P2P: "
														+ result));
							}

						});


			}
			
		});
		

	}
	
	@UiHandler("btnRefresh")
	void onClick(ClickEvent e) {
	}

}
