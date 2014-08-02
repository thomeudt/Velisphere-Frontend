package com.velisphere.tigerspice.client.admin;


import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.UploadedInfo;
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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.endpointclasses.EPCService;
import com.velisphere.tigerspice.client.endpointclasses.EPCServiceAsync;



public class EditEPCInputWidget extends PopupPanel {

	@UiField SingleUploader imageUploader;
	@UiField TextBox txtEPCName;
	@UiField Button btnUpload;
	@UiField Alert aleError;
	String epcID;
	String imagePath;
	@UiField Image imgEPCImage;

	
	private static EditEPCUiBinder uiBinder = GWT
			.create(EditEPCUiBinder.class);

	interface EditEPCUiBinder extends UiBinder<Widget, EditEPCInputWidget> {
	}

	public EditEPCInputWidget() {
		add(uiBinder.createAndBindUi(this));
		  imageUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
		  btnUpload.setEnabled(false);
		  aleError.setVisible(false);
	
	}
	
	public EditEPCInputWidget(String epcID, String epcName, String imageUrl) {
		add(uiBinder.createAndBindUi(this));
		  imageUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
		  aleError.setVisible(false);
		  
		  int startIndex = imageUrl.indexOf("=");
		  int endIndex = imageUrl.indexOf("&out");
		  
		  System.out.println(imageUrl.substring(startIndex+1, endIndex));
		  this.imagePath = imageUrl.substring(startIndex+1, endIndex);
		   this.txtEPCName.setText(epcName);
		  this.epcID = epcID;
		  imgEPCImage.setUrl(imageUrl);
		  
	
	}

	 private IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler() {
		    public void onFinish(IUploader uploader) {
		      if (uploader.getStatus() == Status.SUCCESS) {

		    	//TODO REMOVE OLD IMAGE FROM FILE SYSTEM  
		    	  
		    	// The server sends useful information to the client by default
		        UploadedInfo info = uploader.getServerInfo();
		        new PreloadedImage(info.fileUrl, showImage);
		        System.out.println("File name " + info.name);
		        System.out.println("File content-type " + info.ctype);
		        System.out.println("File size " + info.size);
		        System.out.println("Res " + uploader.getServerMessage().getMessage());
		        
		        // setNewImagePath
		        imagePath = uploader.getServerMessage().getMessage();
		        imgEPCImage.setUrl("/tigerspice_dev/tigerspiceDownloads?privateURL="+imagePath
						+ "&outboundFileName=EPC_image&persist=1");
		        btnUpload.setEnabled(true);
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
			void upload (ClickEvent event) {
			
				
				final EPCServiceAsync epcService = GWT
						.create(EPCService.class);

				
				String epcName = new String("");
				epcName = txtEPCName.getText();
				final String epcNameSuccess = epcName;
				
				
				
				
				epcService.updateEndpointClass(epcID, epcName, imagePath,
						new AsyncCallback<String>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								
								
							}

							@Override
							public void onSuccess(String result) {
								
								AppController.openEPCManager("Endpoint Class "+epcNameSuccess+" successfully updated.");
								closePopup();
							}
						
				});
			}
		  
		  void closePopup(){
			  this.removeFromParent();
		  }
	 
	
}
