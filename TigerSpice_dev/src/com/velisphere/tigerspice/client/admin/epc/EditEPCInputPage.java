package com.velisphere.tigerspice.client.admin.epc;


import java.util.Iterator;
import java.util.LinkedList;

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
import com.github.gwtbootstrap.client.ui.constants.AlertType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.endpointclasses.EPCService;
import com.velisphere.tigerspice.client.endpointclasses.EPCServiceAsync;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassService;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassServiceAsync;
import com.velisphere.tigerspice.client.rules.CheckpathEditorWidget;
import com.velisphere.tigerspice.client.vendors.VendorService;
import com.velisphere.tigerspice.client.vendors.VendorServiceAsync;
import com.velisphere.tigerspice.shared.EPCData;
import com.velisphere.tigerspice.shared.PropertyClassData;
import com.velisphere.tigerspice.shared.VendorData;



public class EditEPCInputPage extends Composite {

	@UiField SingleUploader imageUploader;
	@UiField TextBox txtEPCName;
	@UiField Button btnUpload;
	@UiField Alert aleError;
	@UiField AdminMenuEPC menu;
	@UiField Image imgEPCImage;
	@UiField ListBox lbxVendorID;
	String epcID;
	String imagePath;
	String epcName;
	String imageUrl;	
	EPCServiceAsync epcService;
	

	
	private static EditEPCUiBinder uiBinder = GWT
			.create(EditEPCUiBinder.class);

	interface EditEPCUiBinder extends UiBinder<Widget, EditEPCInputPage> {
	}

	public EditEPCInputPage() {
		epcService = GWT
				.create(EPCService.class);
		initWidget(uiBinder.createAndBindUi(this));
		  imageUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
		  btnUpload.setEnabled(false);
		  aleError.setVisible(false);
		  fillVendorList();
	
	}
	
	public EditEPCInputPage(String epcID, String message) {

		epcService = GWT
				.create(EPCService.class);
		
		this.epcID = epcID;		
		initWidget(uiBinder.createAndBindUi(this));
		fillVendorList();
		getEPCDetails();
		menu.setEditActive();
		  imageUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
		  
		  if (message.length() > 0){
			  aleError.setVisible(true);
			  aleError.setText(message);
			  aleError.setType(AlertType.SUCCESS);
		  } else aleError.setVisible(false);
		  
		  	
	}
	
	
	private void getEPCDetails(){
		
		epcService.getEndpointClassForEndpointClassID(epcID, new AsyncCallback<EPCData>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(EPCData result) {
				epcName = result.getName();
				imageUrl = result.getImageURL();
				
				int startIndex = imageUrl.indexOf("=");
				  int endIndex = imageUrl.indexOf("&out");
				  
				  System.out.println(imageUrl.substring(startIndex+1, endIndex));
				  imagePath = imageUrl.substring(startIndex+1, endIndex);
				  txtEPCName.setText(epcName);
				  
				  imgEPCImage.setUrl(imageUrl);
				  imgEPCImage.setWidth("175px");
				  
				  
				  lbxVendorID.setSelectedValue(result.vendorID);
				
			}

		});

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
		      image.setWidth("50px");
		      image.setVisible(false);
		      //imgPreview = image;
		      //image.setVisible(false);
		      
		     
		    }
		  };
		  
		  
		  @UiHandler("btnUpload")
			void upload (ClickEvent event) {
			
				

				
				String epcName = new String("");
				epcName = txtEPCName.getText();
				final String epcNameSuccess = epcName;
				
				
				
				
				epcService.updateEndpointClass(epcID, epcName, imagePath, lbxVendorID.getValue(),
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
		  
		  @UiHandler("btnAddProperty")
		  void addProperty(ClickEvent event)
		  {
			  AppController.openEPCAddProperty(epcID);
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
			
	 
		  
			@UiFactory EpcPropertyList makePropertyList() { // method name is insignificant
			    return new EpcPropertyList(this.epcID);
			  }


			
}
