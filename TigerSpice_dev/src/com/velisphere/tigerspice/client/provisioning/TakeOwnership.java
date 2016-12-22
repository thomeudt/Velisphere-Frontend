package com.velisphere.tigerspice.client.provisioning;

import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.Breadcrumbs;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Image;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.appcontroller.SessionHelper;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.endpoints.EndpointServiceAsync;
import com.velisphere.tigerspice.client.helper.VeliConstants;

public class TakeOwnership extends Composite {



	@UiField Breadcrumbs brdMain;
	NavLink bread0;
	NavLink bread1;
	@UiField TextBox txtEndpointName;
	@UiField Button btnClaim;
	@UiField Anchor ancFound;
	@UiField Image imgFound;
	@UiField Paragraph pgpIdentifier;
	@UiField Paragraph pgpEndpointClass;
	@UiField Alert aleError;
	String userID;
	String uEPID;
	String endpointclassID;
	
	private static TakeOwnershipUiBinder uiBinder = GWT
			.create(TakeOwnershipUiBinder.class);

	interface TakeOwnershipUiBinder extends
			UiBinder<Widget, TakeOwnership> {
	}

	public TakeOwnership(String uEPID, String identifier, String endpointclassID, String endpointclassName) {
		
		this.userID = SessionHelper.getCurrentUserID();
		this.uEPID = uEPID;
		this.endpointclassID = endpointclassID;
		

		System.out.println("ÜPID: " + uEPID);
		
		initWidget(uiBinder.createAndBindUi(this));
		aleError.setVisible(false);
		
		bread0 = new NavLink();
		bread0.setText("Home");
		brdMain.add(bread0);

		bread1 = new NavLink();
		bread1.setText("Device Provisioning Wizard");
		brdMain.add(bread1);

		this.pgpEndpointClass.setText(endpointclassName);
		this.pgpIdentifier.setText(identifier);
		
		
	}
	
	@UiHandler("btnClaim")
	void openProvisioning (ClickEvent event) {
	
		
		final EndpointServiceAsync endpointService = GWT
				.create(EndpointService.class);

		
		endpointService.addNewEndpoint(uEPID, txtEndpointName.getText(), endpointclassID, userID,
				new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						System.out.println("[ER] " + caught);
					}

					@Override
					public void onSuccess(String result) {
						// TODO Auto-generated method stub

						System.out.println("RES " + result);
						  if(result.equals("OK"))
						  {							 
					        	System.out.println("[OK] " + result);
								AppController.openProvisioningSuccess(); 
						  }
						  else if (result.equals(VeliConstants.VOLT_INSERTFAILED))
						  {							  
					        	System.out.println("[ER] " + result);
					        	aleError.setVisible(true);
					        	aleError.setHeading("Provisioning failed.");
								aleError.setText("Likely reason: Device owned by somebody else. Current owner has to release his ownership, then try again."); 
					       					            
						  }
						
					}
				
		});
		
		
	}

}
