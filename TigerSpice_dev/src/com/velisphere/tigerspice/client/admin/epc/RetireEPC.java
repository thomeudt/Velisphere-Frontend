package com.velisphere.tigerspice.client.admin.epc;

import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.UploadedInfo;
import gwtupload.client.MultiUploader;
import gwtupload.client.PreloadedImage;
import gwtupload.client.PreloadedImage.OnLoadPreloadedImageHandler;
import gwtupload.client.SingleUploader;

import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Image;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.endpointclasses.EPCService;
import com.velisphere.tigerspice.client.endpointclasses.EPCServiceAsync;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.endpoints.EndpointServiceAsync;
import com.velisphere.tigerspice.client.images.Images;
import com.velisphere.tigerspice.shared.UnprovisionedEndpointData;



public class RetireEPC extends Composite {

	@UiField EPCList epcList;
	
	@UiField AdminMenuEPC menu;
	String imagePath;

	
	private static RetireEPCUiBinder uiBinder = GWT
			.create(RetireEPCUiBinder.class);

	interface RetireEPCUiBinder extends UiBinder<Widget, RetireEPC> {
	}

	public RetireEPC() {
		initWidget(uiBinder.createAndBindUi(this));
		menu.setRetireActive();
		
		
		  
	}

	 	
}
