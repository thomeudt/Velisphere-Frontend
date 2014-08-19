package com.velisphere.tigerspice.client.provisioning;

import java.util.LinkedList;
import java.util.UUID;

import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.Breadcrumbs;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Heading;
import com.github.gwtbootstrap.client.ui.Image;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.admin.epc.EPCInfo;
import com.velisphere.tigerspice.client.analytics.AnalyticsService;
import com.velisphere.tigerspice.client.analytics.AnalyticsServiceAsync;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.endpoints.EndpointServiceAsync;
import com.velisphere.tigerspice.client.images.Images;
import com.velisphere.tigerspice.shared.AnalyticsRawData;
import com.velisphere.tigerspice.shared.UnprovisionedEndpointData;

public class ProvisioningWizard extends Composite {

	@UiField Breadcrumbs brdMain;
	NavLink bread0;
	NavLink bread1;
	@UiField TextBox txtSearchID;
	@UiField Button btnSearchID;
	@UiField Button btnNewSearch;
	@UiField Button btnClaim;	
	@UiField Anchor ancEPC;
	@UiField Anchor ancReloadCaptcha;
	@UiField Image imgFound;
	@UiField Image imgCaptchaImage;
	@UiField TextBox txtCaptchaWord;
	@UiField Heading hdgFound;
	@UiField Paragraph pgpHeadingID;
	@UiField Paragraph pgpHeadingCaptcha;
	// @UiField Paragraph pgpExplainCaptcha;
	@UiField Paragraph pgpHeadingIcon;
	@UiField Paragraph pgpHeadingEPC;
	@UiField Paragraph pgpHeadingIdentifier;
	@UiField Paragraph pgpIdentifier;
	@UiField Paragraph pgpHeadingTime;
	@UiField Paragraph pgpTime;
	@UiField Alert aleError;
	
	String uEPID;
	String endpointclassID;
	String identifier;
	String endpointclassName;
	
	
	private static ProvisioningWizardUiBinder uiBinder = GWT
			.create(ProvisioningWizardUiBinder.class);

	interface ProvisioningWizardUiBinder extends
			UiBinder<Widget, ProvisioningWizard> {
	}

	public ProvisioningWizard() {
		
		initWidget(uiBinder.createAndBindUi(this));
		aleError.setVisible(false);
		bread0 = new NavLink();
		bread0.setText("Home");
		brdMain.add(bread0);

		bread1 = new NavLink();
		bread1.setText("Device Provisioning Wizard");
		brdMain.add(bread1);
		hideResultElements();		
		imgCaptchaImage.setUrl("/SimpleCaptcha.jpg?load_"+Random.nextInt());
		ancReloadCaptcha.setHref("#");
				

	}
	
	@UiHandler("btnSearchID")
	void searchDeviceID (ClickEvent event) {
	
		
		final EndpointServiceAsync endpointService = GWT
				.create(EndpointService.class);

		String searchID = new String("");
		searchID = txtSearchID.getText();
		System.out.println("Search for unprovisioned ID: " + searchID);
		
		endpointService.getUnprovisionedEndpoints(searchID, txtCaptchaWord.getText(),
				new AsyncCallback<UnprovisionedEndpointData>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						ancEPC.setEnabled(false);
						
					}

					@Override
					public void onSuccess(UnprovisionedEndpointData result) {
						// TODO Auto-generated method stub

						
				
						
						if (result == null){
							aleError.setVisible(true);
				        	aleError.setHeading("Search request failed.");
							aleError.setText("Likely reason: Misspelled identifier or captcha, device without connectivity, or provisioning time limit exceeded. Please try again."); 
							imgCaptchaImage.setUrl("/SimpleCaptcha.jpg?load_"+Random.nextInt());
						} else
						{
							aleError.setVisible(false);
							showResultElements();
							ancEPC.setText(result.getEndpointClassName());
							pgpIdentifier.setText(result.getIdentifier());
							pgpTime.setText(result.getSecondsSinceConnection()+" seconds ago.");
							
							imgFound.setResource(Images.INSTANCE.paperplane());
							ancEPC.setHref("#");
							uEPID = result.getUepid();
							identifier = result.getIdentifier();
							endpointclassID = result.getEpcId();
							endpointclassName = result.getEndpointClassName();
						}
						
					}
				
		});
	}
	
	void hideResultElements()
	{
		ancEPC.setEnabled(false);
		hdgFound.setVisible(false);
		pgpHeadingIcon.setVisible(false);
		pgpHeadingEPC.setVisible(false);
		pgpHeadingTime.setVisible(false);
		pgpHeadingIdentifier.setVisible(false);
		pgpIdentifier.setVisible(false);
		pgpTime.setVisible(false);
		btnNewSearch.setVisible(false);
		btnClaim.setVisible(false);
		
	}
	
	void showResultElements(){
	ancEPC.setEnabled(true);
	hdgFound.setVisible(true);
	pgpHeadingIcon.setVisible(true);
	pgpHeadingEPC.setVisible(true);
	pgpHeadingTime.setVisible(true);
	pgpHeadingIdentifier.setVisible(true);
	pgpIdentifier.setVisible(true);
	pgpTime.setVisible(true);
	btnNewSearch.setVisible(true);
	btnClaim.setVisible(true);	
	imgCaptchaImage.setVisible(false);
	ancReloadCaptcha.setVisible(false);
	txtCaptchaWord.setVisible(false);
	pgpHeadingCaptcha.setVisible(false);
	txtSearchID.setVisible(false);
	pgpHeadingID.setVisible(false);
	btnSearchID.setVisible(false);
	}
	
	
	@UiHandler("btnClaim")
	void openTakeOwnership(ClickEvent event){
		AppController.openTakeOwnership(uEPID, identifier, endpointclassID, endpointclassName);
	}
	
	@UiHandler("btnNewSearch")
	void openNewSearch(ClickEvent event){
		AppController.openProvisioningWizard();
	}
	
	
	@UiHandler("ancReloadCaptcha")
	void reloadCaptcha(ClickEvent event){
		imgCaptchaImage.setUrl("/SimpleCaptcha.jpg?load_"+Random.nextInt());
	}
	
	@UiHandler("ancEPC")
	void openEPCPopup(ClickEvent event){
		AppController.openPopUp(new EPCInfo(endpointclassID));
	}

}
