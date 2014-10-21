package com.velisphere.tigerspice.client.endpoints;

import java.util.Iterator;
import java.util.LinkedList;

import com.github.gwtbootstrap.client.ui.Heading;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.analytics.AnalyticsService;
import com.velisphere.tigerspice.client.analytics.AnalyticsServiceAsync;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.properties.PropertyService;
import com.velisphere.tigerspice.client.properties.PropertyServiceAsync;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassService;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassServiceAsync;
import com.velisphere.tigerspice.shared.AnalyticsRawData;
import com.velisphere.tigerspice.shared.PropertyClassData;
import com.velisphere.tigerspice.shared.PropertyData;

public class EndpointSensorWidget extends Composite {

	String endpointID;
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

	private static EndpointSensorWidgetUiBinder uiBinder = GWT
			.create(EndpointSensorWidgetUiBinder.class);

	interface EndpointSensorWidgetUiBinder extends
			UiBinder<Widget, EndpointSensorWidget> {
	}

	public EndpointSensorWidget(String endpointID) {
		this.endpointID = endpointID;
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

		propertyService.getSensorPropertiesForEndpointID(endpointID,
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

	private void populateCurrentState(final String propertyID) {

		final AnimationLoading animationLoading = new AnimationLoading();

		animationLoading.showLoadAnimation("Loading Current State");

		AnalyticsServiceAsync analyticsService = GWT
				.create(AnalyticsService.class);

		analyticsService.getCurrentSensorState(endpointID, propertyID,
				new AsyncCallback<AnalyticsRawData>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						animationLoading.removeLoadAnimation();
					}

					@Override
					public void onSuccess(AnalyticsRawData result) {
						
						
						if (result.getPropertyValuePairs().get(propertyID) == null)
						{
							pgpLastValue.setText("no values received yet");
							pgpLastUpdate.setText("no values received yet");
						}
						else					
						{
							pgpLastValue.setText(result.getPropertyValuePairs().get(propertyID));
							pgpLastUpdate.setText(result.getTimeStamp());
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

		propertyService.getPropertyDetailsForPropertyID(propertyID, new AsyncCallback<PropertyData>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(PropertyData result) {
						animationLoading.removeLoadAnimation();
						pgpPropertyName.setText(result.getPropertyName());
						populatePropertyClassData(result.propertyclassId);
						
						
						
					}

				});
	}
	
	private void populatePropertyClassData(final String propertyClassID) {

		final AnimationLoading animationLoading = new AnimationLoading();

		animationLoading.showLoadAnimation("Loading Class-specific Data");


		PropertyClassServiceAsync propertyClassService = GWT
				.create(PropertyClassService.class);

		propertyClassService.getPropertyClassForPropertyClassID(propertyClassID, new AsyncCallback<PropertyClassData>() {

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



}
