package com.velisphere.tigerspice.client.endpoints.actor;

import java.util.Iterator;
import java.util.LinkedList;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Heading;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.analytics.AnalyticsService;
import com.velisphere.tigerspice.client.analytics.AnalyticsServiceAsync;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.properties.PropertyService;
import com.velisphere.tigerspice.client.properties.PropertyServiceAsync;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassService;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassServiceAsync;
import com.velisphere.tigerspice.shared.AnalyticsRawData;
import com.velisphere.tigerspice.shared.PropertyClassData;
import com.velisphere.tigerspice.shared.PropertyData;

public class EndpointActorWidget extends Composite {

	String endpointID;
	String sphereID;
	@UiField
	ListBox lstSensors;
	@UiField
	Heading pgpPropertyName;
	@UiField
	Paragraph pgpLastValue;
	@UiField
	Paragraph pgpLastUpdate;
	@UiField
	Paragraph pgpUnit;
	@UiField
	Paragraph pgpAction;
	@UiField
	Paragraph pgpCheck;
	@UiField
	Paragraph pgpLogic;
	@UiField
	Paragraph pgpTrigger;
	@UiField
	Paragraph pgpLastValueHeader;
	@UiField
	Paragraph pgpLastUpdateHeader;
	@UiField
	Paragraph pgpUnitHeader;
	@UiField
	Paragraph pgpActionHeader;
	@UiField
	Paragraph pgpCheckHeader;
	@UiField
	Paragraph pgpLogicHeader;
	@UiField
	Paragraph pgpTriggerHeader;
	@UiField
	Button btnDataTrail;
	@UiField
	Button btnSetNewValue;
	HandlerRegistration dataTrailClickReg;
	HandlerRegistration setNewValueClickReg;

	private static EndpointSensorWidgetUiBinder uiBinder = GWT
			.create(EndpointSensorWidgetUiBinder.class);

	interface EndpointSensorWidgetUiBinder extends
			UiBinder<Widget, EndpointActorWidget> {
	}

	public EndpointActorWidget(String sphereID, String endpointID) {
		this.endpointID = endpointID;
		this.sphereID = sphereID;
		initWidget(uiBinder.createAndBindUi(this));
		populateSensorList();

		lstSensors.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				populateCurrentState(lstSensors.getValue());
				populatePropertyData(lstSensors.getValue());
			}
		});
	}

	private void populateSensorList() {

		final AnimationLoading animationLoading = new AnimationLoading();

		animationLoading.showLoadAnimation("Loading Endpoint");

		PropertyServiceAsync propertyService = GWT
				.create(PropertyService.class);

		propertyService.getActorPropertiesForEndpointID(endpointID,
				new AsyncCallback<LinkedList<PropertyData>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(LinkedList<PropertyData> result) {
						// TODO Auto-generated method stub
						animationLoading.removeLoadAnimation();
						Iterator<PropertyData> it = result.iterator();
						if (it.hasNext() == false) {

							pgpPropertyName
									.setText("This endpoint can't perform any actions.");
							lstSensors.setVisible(false);
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
						}
						while (it.hasNext()) {

							PropertyData propData = new PropertyData();
							propData = it.next();
							lstSensors.addItem(propData.propertyName,
									propData.propertyId);
							lstSensors.setSelectedIndex(0);
							populateCurrentState(lstSensors.getValue());
							populatePropertyData(lstSensors.getValue());

						}

					}

				});

		lstSensors.setVisibleItemCount(10);

	}

	private void addTrailClickhandler(final String propertyID, final String endpointName,
			final String propertyName) {
		if (dataTrailClickReg != null) {
			dataTrailClickReg.removeHandler();
		}

		dataTrailClickReg = btnDataTrail.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				AppController.openAnalyticsForDataTrail(sphereID, endpointID,
						propertyID, endpointName, propertyName, false);
				System.out.println("EPID source " + endpointID);
			}

		});
	}

	private void populateCurrentState(final String propertyID) {

		final AnimationLoading animationLoading = new AnimationLoading();

		animationLoading.showLoadAnimation("Loading Current State");

		AnalyticsServiceAsync analyticsService = GWT
				.create(AnalyticsService.class);

		analyticsService.getCurrentActorState(endpointID, propertyID,
				new AsyncCallback<AnalyticsRawData>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						animationLoading.removeLoadAnimation();
					}

					@Override
					public void onSuccess(AnalyticsRawData result) {

						if (result.getPropertyValuePairs().get(propertyID) == null) {
							pgpLastValue.setText("no values received yet");
							pgpLastUpdate.setText("no values received yet");
							pgpCheck.setText("no values received yet");
							pgpAction.setText("no values received yet");
							pgpLogic.setText("no values received yet");
							pgpTrigger.setText("no values received yet");
						} else {
							pgpLastValue.setText(result.getPropertyValuePairs()
									.get(propertyID));
							pgpLastUpdate.setText(result.getTimeStamp());
							populateActionData(result.getProcessedByID());
							pgpTrigger.setText(result.getSource());
						}

						animationLoading.removeLoadAnimation();
					}
				});
	}

	private void populatePropertyData(final String propertyID) {

		final AnimationLoading animationLoading = new AnimationLoading();

		animationLoading.showLoadAnimation("Loading Class-specific Data");

		PropertyServiceAsync propertyService = GWT
				.create(PropertyService.class);

		propertyService.getPropertyDetailsForPropertyID(propertyID,
				new AsyncCallback<PropertyData>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(PropertyData result) {
						animationLoading.removeLoadAnimation();
						pgpPropertyName.setText(result.getPropertyName());
						populatePropertyClassData(result.propertyclassId);
						addTrailClickhandler(result.propertyId,
								null, result.propertyName);

						addNewValueClickhandler(result.propertyId);
						
					}

				});
	}

	private void populatePropertyClassData(final String propertyClassID) {

		final AnimationLoading animationLoading = new AnimationLoading();

		animationLoading.showLoadAnimation("Loading Class-specific Data");

		PropertyClassServiceAsync propertyClassService = GWT
				.create(PropertyClassService.class);

		propertyClassService.getPropertyClassForPropertyClassID(
				propertyClassID, new AsyncCallback<PropertyClassData>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(PropertyClassData result) {
						animationLoading.removeLoadAnimation();

						pgpUnit.setText(result.getUnit());
					}

				});
	}

	private void populateActionData(final String actionID) {

		final AnimationLoading animationLoading = new AnimationLoading();

		animationLoading.showLoadAnimation("Loading Action-specific Data");

		AnalyticsServiceAsync analyticsService = GWT
				.create(AnalyticsService.class);

		analyticsService.getActionNameForActionID(actionID,
				new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(String result) {
						animationLoading.removeLoadAnimation();

						pgpAction.setText(result);
					}

				});
	}
	
	private void addNewValueClickhandler(final String propertyID) {
		if (setNewValueClickReg != null) {
			setNewValueClickReg.removeHandler();
		}

		setNewValueClickReg = btnSetNewValue.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final ValueSetter setValue = new
						ValueSetter(endpointID, propertyID);
				  
				setValue.setAutoHideEnabled(true);
				 
				setValue.center();
	}

		});
	}


	  	
}
