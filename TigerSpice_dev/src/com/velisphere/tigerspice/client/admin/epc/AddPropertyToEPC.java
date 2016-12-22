package com.velisphere.tigerspice.client.admin.epc;

import java.util.Iterator;
import java.util.LinkedList;

import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.CheckBox;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.endpointclasses.EPCService;
import com.velisphere.tigerspice.client.endpointclasses.EPCServiceAsync;
import com.velisphere.tigerspice.client.properties.PropertyService;
import com.velisphere.tigerspice.client.properties.PropertyServiceAsync;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassService;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassServiceAsync;
import com.velisphere.tigerspice.shared.PropertyClassData;


public class AddPropertyToEPC extends Composite {

	@UiField TextBox txtPropertyName;
	@UiField ListBox lbxPropertyClassID;
	@UiField CheckBox cbxStatus;
	@UiField CheckBox cbxActor;
	@UiField CheckBox cbxSensor;
	@UiField CheckBox cbxConfigurable;
	@UiField AdminMenuEPC menu;
	String EPCId;
	
	private static AddPropertyToEPCUiBinder uiBinder = GWT
			.create(AddPropertyToEPCUiBinder.class);

	interface AddPropertyToEPCUiBinder extends
			UiBinder<Widget, AddPropertyToEPC> {
	}

	public AddPropertyToEPC() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public AddPropertyToEPC(String EPCId) {
		this.EPCId = EPCId;
		initWidget(uiBinder.createAndBindUi(this));
		menu.setEditActive();
		fillPropertyClassList();
		
	}
	
	
	void fillPropertyClassList(){
		
		final PropertyClassServiceAsync propertyClassService = GWT
				.create(PropertyClassService.class);

		propertyClassService.getAllPropertyClassDetails(new AsyncCallback<LinkedList<PropertyClassData>>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(LinkedList<PropertyClassData> result) {
				// TODO Auto-generated method stub
				Iterator<PropertyClassData> it = result.iterator();
				while(it.hasNext()){
					PropertyClassData propertyClassItem = it.next();
					lbxPropertyClassID.addItem(propertyClassItem.propertyClassName, propertyClassItem.propertyClassId);
				}
			}

			
		});
		
	}
	
	
	  @UiHandler("btnSave")
		void searchDeviceID (ClickEvent event) {
		
			
			final PropertyServiceAsync propertyService = GWT
					.create(PropertyService.class);

			
			String propertyName = new String("");
			propertyName = txtPropertyName.getText();
			final String propertyNameSuccess = propertyName;
			
			
			
			
			propertyService.addProperty(propertyName, lbxPropertyClassID.getValue(), EPCId, cbxActor.getValue(), cbxSensor.getValue(), cbxStatus.getValue(), cbxConfigurable.getValue(), 
					new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							
							
						}

						@Override
						public void onSuccess(String result) {
							
							AppController.openEPCInput(EPCId, "New Property "+propertyNameSuccess+" successfully created.");
						}
					
			});
		}


}
