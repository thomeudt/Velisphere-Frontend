package com.velisphere.tigerspice.client.admin.epc;

import java.util.Iterator;
import java.util.LinkedList;

import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.github.gwtbootstrap.client.ui.Row;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.velisphere.tigerspice.client.endpointclasses.EPCService;
import com.velisphere.tigerspice.client.endpointclasses.EPCServiceAsync;
import com.velisphere.tigerspice.client.properties.PropertyService;
import com.velisphere.tigerspice.client.properties.PropertyServiceAsync;
import com.velisphere.tigerspice.shared.EPCData;
import com.velisphere.tigerspice.shared.PropertyData;

public class EpcPropertyList extends Composite {

	String endpointClassID;
	
	public EpcPropertyList(String endpointClassID) {

		this.endpointClassID = endpointClassID;
		
	final VerticalPanel verticalPanel = new VerticalPanel();
	
	getData(verticalPanel);
	initWidget(verticalPanel);
	
	}
	
	
	private void getData(final VerticalPanel verticalPanel){
		
	
		
		PropertyServiceAsync rpcService;
		rpcService = GWT.create(PropertyService.class);
		
		rpcService
		.getPropertiesForEndpointClass(endpointClassID, new AsyncCallback<LinkedList<PropertyData>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(LinkedList<PropertyData> result) {
				Iterator<PropertyData> it = result.iterator();
				while(it.hasNext()){
					verticalPanel.add(createRow(it.next()));
					
				}				
			}
		});

	}
	
	private Row createRow(PropertyData propertyData){
		Row row = new Row();
		Column nameCol = new Column(1);
		Label name = new Label(propertyData.getPropertyName());
		nameCol.add(name);
		row.add(nameCol);		
		
		
		Column propClassCol = new Column(1);
		Label propClass = new Label(propertyData.getPropertyclassId());
		propClassCol.add(propClass);
		row.add(propClassCol);
		
		Column propClassNameCol = new Column(1);
		Label propClassName = new Label(propertyData.getPropertyclassName());
		propClassNameCol.add(propClassName);
		row.add(propClassNameCol);

				
		Column actorCol = new Column(1);
		Label actor = new Label();
		if(propertyData.getIsActor()==1){
			actor.setText("Actor");
		}
		actorCol.add(actor);
		row.add(actorCol);
		
		Column sensorCol = new Column(1);
		Label sensor = new Label();
		if(propertyData.getIsSensor()==1){
			sensor.setText("Sensor");
		}
		sensorCol.add(sensor);
		row.add(sensorCol);
		
		Column configCol = new Column(1);
		Label configurable = new Label();
		if(propertyData.getIsConfigurable()==1){
			configurable.setText("Configurable");
		}
		configCol.add(configurable);
		row.add(configCol);
		
		Column statusCol = new Column(1);
		Label status = new Label();
		if(propertyData.getStatus()==1){
			configurable.setText("Active");
		}
		statusCol.add(status);
		row.add(statusCol);
		
		return row;
		
	}
	
}
