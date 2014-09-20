package com.velisphere.tigerspice.client.admin.vendor;

import gwtupload.client.IFileInput.FileInputType;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.UploadedInfo;
import gwtupload.client.MultiUploader;
import gwtupload.client.PreloadedImage;
import gwtupload.client.PreloadedImage.OnLoadPreloadedImageHandler;
import gwtupload.client.SingleUploader;

import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.endpointclasses.EPCService;
import com.velisphere.tigerspice.client.endpointclasses.EPCServiceAsync;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.endpoints.EndpointServiceAsync;
import com.velisphere.tigerspice.client.images.Images;
import com.velisphere.tigerspice.client.vendors.VendorService;
import com.velisphere.tigerspice.client.vendors.VendorServiceAsync;
import com.velisphere.tigerspice.shared.UnprovisionedEndpointData;



public class CreateVendor extends Composite {

	@UiField SingleUploader imageUploader;
	@UiField TextBox txtVendorName;
	@UiField Button btnUpload;
	@UiField Alert aleError;
	@UiField AdminMenuVendor menu;
	@UiField Image imgVendorImage;
	String imagePath;

	
	private static CreateEPCUiBinder uiBinder = GWT
			.create(CreateEPCUiBinder.class);

	interface CreateEPCUiBinder extends UiBinder<Widget, CreateVendor> {
	}

	public CreateVendor() {
		initWidget(uiBinder.createAndBindUi(this));
		
		
		  imageUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
		
		 
		  
		  btnUpload.setEnabled(false);
		  aleError.setVisible(false);
		  menu.setAddActive();
	}

	 private IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler() {
		    public void onFinish(IUploader uploader) {
		      if (uploader.getStatus() == Status.SUCCESS) {
		        // The server sends useful information to the client by default
		        UploadedInfo info = uploader.getServerInfo();
		        new PreloadedImage(info.fileUrl, showImage);
		        System.out.println("File name " + info.name);
		        System.out.println("File content-type " + info.ctype);
		        System.out.println("File size " + info.size);
		        System.out.println("Res " + uploader.getServerMessage().getMessage());
		        imagePath = uploader.getServerMessage().getMessage();
		        btnUpload.setEnabled(true);
		        imgVendorImage.setUrl("/tigerspice_dev/tigerspiceDownloads?privateURL="+imagePath
						+ "&outboundFileName=EPC_image&persist=1");
		      }
		    }
		  };
		  
		  //TODO activate preview
		  
		// Attach an image to the pictures viewer - currently disabled
		  
		  private OnLoadPreloadedImageHandler showImage = new OnLoadPreloadedImageHandler() {
		    public void onLoad(PreloadedImage image) {
		      image.setWidth("75px");
		      image.setVisible(false);
		      //imgPreview = image;
		      //image.setVisible(false);
		      
		     
		    }
		  };
		  
		  
		  @UiHandler("btnUpload")
			void searchDeviceID (ClickEvent event) {
			
				
				final VendorServiceAsync vendorService = GWT
						.create(VendorService.class);

				
				String vendorName = new String("");
				vendorName = txtVendorName.getText();
				final String vendorNameSuccess = vendorName;
				
				
				
				
				vendorService.addVendor(vendorName, imagePath,
						new AsyncCallback<String>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								
								
							}

							@Override
							public void onSuccess(String result) {
								AppController.openEPCManager("New Vendor "+vendorNameSuccess+" successfully created.");
								
							}
						
				});
			}
	 
	
}
