package com.velisphere.tigerspice.client.endpoints;

import java.util.Iterator;
import java.util.LinkedList;

import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.Row;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.properties.PropertyService;
import com.velisphere.tigerspice.client.properties.PropertyServiceAsync;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.PropertyData;

public class EndpointSensorWidget extends Composite {

	String endpointID;
	@UiField 
	ListBox lstSensors;
	
	private static EndpointSensorWidgetUiBinder uiBinder = GWT
			.create(EndpointSensorWidgetUiBinder.class);

	interface EndpointSensorWidgetUiBinder extends
			UiBinder<Widget, EndpointSensorWidget> {
	}

	public EndpointSensorWidget(String endpointID) {
		this.endpointID = endpointID;
		initWidget(uiBinder.createAndBindUi(this));
		populateSensorList(this.endpointID);
	}

	
	
	private void populateSensorList(String endpointID) {

		final AnimationLoading animationLoading = new AnimationLoading();
		
		animationLoading.showLoadAnimation("Loading Endpoint");
	
		PropertyServiceAsync propertyService = GWT
				.create(PropertyService.class);
		
		
		propertyService.getSensorPropertiesForEndpointID(endpointID, new AsyncCallback<LinkedList<PropertyData>>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(LinkedList<PropertyData> result) {
				// TODO Auto-generated method stub
				animationLoading.removeLoadAnimation();
				Iterator<PropertyData> it = result.iterator();
				if (it.hasNext() == false){
					
				}
				while(it.hasNext()){
			
					PropertyData propData = new PropertyData();
					propData = it.next();
					lstSensors.addItem(propData.propertyName, propData.propertyId);
					
				}
								
			}
			
		});	

		
		lstSensors.setVisibleItemCount(10);
	
	}
	
	
}
