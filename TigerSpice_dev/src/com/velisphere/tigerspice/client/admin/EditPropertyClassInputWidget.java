package com.velisphere.tigerspice.client.admin;

import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.UploadedInfo;
import gwtupload.client.PreloadedImage;
import gwtupload.client.PreloadedImage.OnLoadPreloadedImageHandler;
import gwtupload.client.SingleUploader;

import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.endpointclasses.EPCService;
import com.velisphere.tigerspice.client.endpointclasses.EPCServiceAsync;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassService;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassServiceAsync;

public class EditPropertyClassInputWidget extends PopupPanel {

	@UiField
	TextBox txtPCName;
	@UiField
	Button btnUpload;
	@UiField
	ListBox lbxDataType;
	@UiField
	TextBox txtUnit;
	@UiField
	Alert aleError;
	String pcID;
	String imagePath;

	private static EditPCUiBinder uiBinder = GWT.create(EditPCUiBinder.class);

	interface EditPCUiBinder extends
			UiBinder<Widget, EditPropertyClassInputWidget> {
	}

	public EditPropertyClassInputWidget() {
		add(uiBinder.createAndBindUi(this));

		btnUpload.setEnabled(false);
		aleError.setVisible(false);

	}

	public EditPropertyClassInputWidget(String pcID, String pcName,
			String pcDataType, String pcUnit) {
		add(uiBinder.createAndBindUi(this));

		aleError.setVisible(false);
		fillDataType();
		txtPCName.setText(pcName);
		txtUnit.setText(pcUnit);
		this.pcID = pcID;
		lbxDataType.setSelectedValue(pcDataType);
		
	}
	
	void fillDataType(){
		lbxDataType.addItem("Numeric (->Double)", "Double");
		lbxDataType.addItem("Digital True/False (0/1) (->Byte)", "Byte");
		lbxDataType.addItem("Text (->String)", "String");
	}
	

	@UiHandler("btnUpload")
	void searchDeviceID(ClickEvent event) {

		final PropertyClassServiceAsync pcService = GWT.create(PropertyClassService.class);

		String pcName = new String("");
		pcName = txtPCName.getText();
		final String epcNameSuccess = pcName;
		
		String pcUnit = new String("");
		pcUnit = txtUnit.getText();
		
		String pcDataType = new String("");
		pcDataType = lbxDataType.getValue();
		

		pcService.updatePropertyClass(pcID, pcName, pcDataType, pcUnit,
				new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(String result) {

						AppController.openPropertyClassManager("Property Class "
								+ epcNameSuccess + " successfully updated.");
						closePopup();
					}

				});
	}

	void closePopup() {
		this.removeFromParent();
	}

}
