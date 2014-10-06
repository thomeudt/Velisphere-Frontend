package com.velisphere.tigerspice.client.admin.epc;

import java.util.Iterator;
import java.util.LinkedList;

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
import com.github.gwtbootstrap.client.ui.ListBox;
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
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassService;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassServiceAsync;
import com.velisphere.tigerspice.client.vendors.VendorService;
import com.velisphere.tigerspice.client.vendors.VendorServiceAsync;
import com.velisphere.tigerspice.shared.PropertyClassData;
import com.velisphere.tigerspice.shared.UnprovisionedEndpointData;
import com.velisphere.tigerspice.shared.VendorData;



public class CreateEPC extends Composite {

	@UiField SingleUploader imageUploader;
	@UiField TextBox txtEPCName;
	@UiField Button btnUpload;
	@UiField Alert aleError;
	@UiField AdminMenuEPC menu;
	@UiField Image imgEPCImage;
	@UiField ListBox lbxVendorID;
	String imagePath;

	
	private static CreateEPCUiBinder uiBinder = GWT
			.create(CreateEPCUiBinder.class);

	interface CreateEPCUiBinder extends UiBinder<Widget, CreateEPC> {
	}

	public CreateEPC() {
		initWidget(uiBinder.createAndBindUi(this));
		
		
		  imageUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
		
		 
		  
		  btnUpload.setEnabled(false);
		  aleError.setVisible(false);
		  menu.setAddActive();
		  fillVendorList();
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
		        imgEPCImage.setUrl(GWT.getHostPageBaseURL()+"tigerspiceDownloads?privateURL="+imagePath
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
			
				
				final EPCServiceAsync epcService = GWT
						.create(EPCService.class);

				
				String epcName = new String("");
				epcName = txtEPCName.getText();
				final String epcNameSuccess = epcName;
				
				
				
				
				epcService.addEndpointClass(epcName, imagePath, lbxVendorID.getValue(),
						new AsyncCallback<String>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								
								
							}

							@Override
							public void onSuccess(String result) {
								AppController.openEPCManager("New Endpoint Class "+epcNameSuccess+" successfully created.");
								
							}
						
				});
			}
	 
	
		  void fillVendorList(){
				
				final VendorServiceAsync vendorService = GWT
						.create(VendorService.class);

				vendorService.getAllVendorDetails(new AsyncCallback<LinkedList<VendorData>>(){

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(LinkedList<VendorData> result) {
						// TODO Auto-generated method stub
						Iterator<VendorData> it = result.iterator();
						while(it.hasNext()){
							VendorData vendorItem = it.next();
							lbxVendorID.addItem(vendorItem.vendorName, vendorItem.vendorID);
						}
					}

					
				});
				
			}
			
		  
}
