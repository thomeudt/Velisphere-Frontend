package com.velisphere.tigerspice.client.admin.propertyclass;

import com.github.gwtbootstrap.client.ui.Alert;
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
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassService;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassServiceAsync;

public class CreatePropertyClass extends Composite {

	private static ManagePropertyClassUiBinder uiBinder = GWT
			.create(ManagePropertyClassUiBinder.class);

	interface ManagePropertyClassUiBinder extends
			UiBinder<Widget, CreatePropertyClass> {
	}
	
	@UiField ListBox lbxDataType;
	@UiField Alert aleError;
	@UiField TextBox txtPCName;
	@UiField TextBox txtUnit;

	public CreatePropertyClass() {
		initWidget(uiBinder.createAndBindUi(this));
		aleError.setVisible(false);
		fillDataType();
		
		
	}
	
	void fillDataType(){
		lbxDataType.addItem("Numeric (->Double)", "Double");
		lbxDataType.addItem("Digital True/False (0/1) (->Byte)", "Byte");
		lbxDataType.addItem("Text (->String)", "String");
	}

	
	  @UiHandler("btnUpload")
			void searchDeviceID (ClickEvent event) {
			
				
				final PropertyClassServiceAsync propertyClassService = GWT
						.create(PropertyClassService.class);

				String pcName = new String("");
				pcName = txtPCName.getText();
				
				String unit = new String("");
				unit = txtUnit.getText();
				
				final String pcNameSuccess = pcName;
				
				propertyClassService.addPropertyClass(pcName, lbxDataType.getValue(), unit,
						new AsyncCallback<String>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								
								
							}

							@Override
							public void onSuccess(String result) {
								AppController.openPropertyClassManager("Property Class "
										+ pcNameSuccess + " successfully created.");
								
							}
						
				});
			}
	 

	
}
