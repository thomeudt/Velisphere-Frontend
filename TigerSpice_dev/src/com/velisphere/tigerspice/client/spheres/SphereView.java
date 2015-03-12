/*******************************************************************************
 * CONFIDENTIAL INFORMATION
 *  __________________
 *  
 *   Copyright (C) 2014 Thorsten Meudt / Connected Things Lab
 *   All Rights Reserved.
 *  
 *  NOTICE:  All information contained herein is, and remains
 *  the property of Thorsten Meudt and its suppliers,
 *  if any.  The intellectual and technical concepts contained
 *  herein are proprietary to Thorsten Meudt
 *  and its suppliers and may be covered by Patents,
 *  patents in process, and are protected by trade secret or copyright law.
 *  Dissemination of this information or reproduction of this material
 *  is strictly forbidden unless prior written permission is obtained
 *  from Thorsten Meudt.
 ******************************************************************************/
package com.velisphere.tigerspice.client.spheres;

import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.Breadcrumbs;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.CheckBox;
import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.PageHeader;
import com.github.gwtbootstrap.client.ui.Row;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.AlertType;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.helper.widgets.AlertWidget;
import com.velisphere.tigerspice.client.users.LoginSuccess;
import com.velisphere.tigerspice.shared.SphereData;


public class SphereView extends Composite {

	private static SphereOverviewUiBinder uiBinder = GWT
			.create(SphereOverviewUiBinder.class);

	
	
	@UiField Breadcrumbs brdMain;
	
	@UiField CheckBox cbxPublic;
	@UiField PageHeader pghSphereNameHeader;
	@UiField Column colShareAlert;	

	
	private SphereServiceAsync rpcServiceSphere;
	
	String sphereID;  
	String sphereName;
	NavLink bread0;
	NavLink bread1;
	NavLink bread2;
	private TextBox sphereChangeNameField;
	
	
	
	interface SphereOverviewUiBinder extends UiBinder<Widget, SphereView> {
	}

	public SphereView(final String sphereID) {
		
		this.sphereID = sphereID;
		
		rpcServiceSphere = GWT.create(SphereService.class);
		initWidget(uiBinder.createAndBindUi(this));
		
		
		bread0 = new NavLink();
		bread0.setText("Home");
		brdMain.add(bread0);		
		bread1 = new NavLink();
		bread1.setText("Endpoint Manager");
		brdMain.add(bread1);
		bread2 = new NavLink();
		bread2.setText("-");
		brdMain.add(bread2);
		

		bread0.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				RootPanel.get("main").clear();
				LoginSuccess loginSuccess = new LoginSuccess();
				RootPanel.get("main").add(loginSuccess);

			}
		});
		
		bread1.addClickHandler(
				new ClickHandler(){
					@Override
					public void onClick(ClickEvent event){
			
						RootPanel mainPanel = RootPanel.get("main");
						mainPanel.clear();
						SphereLister sphereLister = new SphereLister(); 		
						mainPanel.add(sphereLister);
						
					}
				});

		
		addShareToggleListener();
	
		// Change button for header
		
		Icon icnEditEndpointName = new Icon();
		icnEditEndpointName.setType(IconType.EDIT);
		pghSphereNameHeader.add(icnEditEndpointName);
		final Anchor ancEditSphereName = new Anchor();
		ancEditSphereName.setText(" Change Name of this Sphere");
		ancEditSphereName.setHref("#");
		sphereChangeNameField = new TextBox();
		final Button okButton = new Button();
		okButton.setText("OK");
		okButton.setType(ButtonType.SUCCESS);
		final PopupPanel nameChangePopup = new PopupPanel();
		
		ancEditSphereName.addClickHandler(
				new ClickHandler(){ 
				public void onClick(ClickEvent event)  {
					
					
					if (nameChangePopup.isShowing()) {
						
						nameChangePopup.removeFromParent();
					} else
					{						
						sphereChangeNameField.setText(pghSphereNameHeader.getText());
						HorizontalPanel horizontalPanel = new HorizontalPanel();
						horizontalPanel.add(sphereChangeNameField);
						horizontalPanel.add(okButton.asWidget());
						nameChangePopup.clear();
						nameChangePopup.add(horizontalPanel);
						nameChangePopup.setModal(true);
						nameChangePopup.setAutoHideEnabled(true);
						nameChangePopup.setAnimationEnabled(true);
						nameChangePopup.showRelativeTo(ancEditSphereName);
						sphereChangeNameField.setFocus(true);
						okButton.addClickHandler(
								new ClickHandler(){ 
								public void onClick(ClickEvent event)  {
									
									final String newSphereName = sphereChangeNameField.getText();
								
									
									nameChangePopup.hide();
									final AnimationLoading animationLoading = new AnimationLoading();
									showLoadAnimation(animationLoading);

									rpcServiceSphere.updateSpherenameForSphereID(sphereID, newSphereName,
											new AsyncCallback<String>() {

												@Override
												public void onFailure(Throwable caught) {
													// TODO Auto-generated method stub
													removeLoadAnimation(animationLoading);
												}

												@Override
												public void onSuccess(String result) {
													// TODO Auto-generated method stub
													pghSphereNameHeader.setText(newSphereName);
													brdMain.remove(bread2);
													bread2.setText(newSphereName);
													brdMain.add(bread2);
													removeLoadAnimation(animationLoading);


												}

											});


								}
							
								});
					}
				}
				});
		pghSphereNameHeader.add(ancEditSphereName);

		
		
	
		updateSphereNameAndState();
		
	
	
	}
	
	
	private void addShareToggleListener(){
		
		cbxPublic.addValueChangeHandler(new ValueChangeHandler<Boolean>(){

		

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				// TODO Auto-generated method stub
				if(event.getValue() == true) {
					showWarningBox();
				} else 
					setPublicState(0);
			}
			
		});
		
	}
	
	private void showWarningBox(){
		final DialogBox warningBox = new DialogBox();
		HTML html = new HTML("<b>Privacy Warning</b><br><br>Activating this option will allow other users to access all endpoints allocated to this sphere and read sensor values. <br><br>Use this option with extreme caution<br>&nbsp;");
		Row row1 = new Row();
		Column col1 = new Column(3, 0);
		col1.add(html);
		row1.add(col1);
		
		Button btnConfirm = new Button();
		btnConfirm.setText("Confirm");
		btnConfirm.setType(ButtonType.WARNING);
		
		Button btnCancel = new Button();
		btnCancel.setText("Cancel");
		btnCancel.setType(ButtonType.DEFAULT);
		
		
		Row row2 = new Row();
		Column col2 = new Column(1, 0);
		Column col3 = new Column(1, 1);
		col2.add(btnConfirm);
		col3.add(btnCancel);
		row2.add(col2);
		row2.add(col3);
		
		VerticalPanel contentPanel = new VerticalPanel();
		contentPanel.add(row1);
		contentPanel.add(row2);
		
		warningBox.add(contentPanel);
		warningBox.addStyleName("vcenter well");
		

		warningBox.center();
		
		btnConfirm.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				warningBox.hide();
				setPublicState(1);
				
				
			}
			
		});
	
		btnCancel.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				warningBox.hide();
				setPublicState(0);
				
				cbxPublic.setValue(false);
				
			}
			
		});
	}

	private void updateSphereNameAndState(){
		
		
		
		rpcServiceSphere.getSphereForSphereID(sphereID,
				new AsyncCallback<SphereData>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(SphereData result) {
						// TODO Auto-generated method stub
						pghSphereNameHeader.setText(result.getName());
						brdMain.remove(bread2);
						bread2.setText(result.getName());
						brdMain.add(bread2);
						
						sphereName = result.getName();
						if(result.getIsPublic() == 1){
							RootPanel.get().add(new HTML("Shared Sphere Value " + result.getIsPublic()));
							colShareAlert.add(new AlertWidget(new HTML("<b>&nbsp;Privacy Alert:</b> This is a shared sphere, visible to other VeliSphere users. Other user will be able to access sensor data from this Sphere."), AlertType.WARNING));
							cbxPublic.setValue(true);
						}
						else
						{
							RootPanel.get().add(new HTML("Private Sphere Value " + result.getIsPublic()));
							colShareAlert.clear();
							cbxPublic.setValue(false);
						}
								
						
						

					}

				});

		
	}
	
	private void setPublicState(int publicState){
		rpcServiceSphere.updatePublicStateForSphereID(sphereID, publicState,
				new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(String result) {
						// TODO Auto-generated method stub
						
						updateSphereNameAndState();
						
						

					}

				});

		
	}
	
	private void showLoadAnimation(AnimationLoading animationLoading) {
		RootPanel rootPanel = RootPanel.get("main");
		rootPanel.getElement().getStyle().setPosition(Position.RELATIVE);
		rootPanel.add(animationLoading, 25, 40);
	}

	private void removeLoadAnimation(AnimationLoading animationLoading) {
		if (animationLoading != null)
			animationLoading.removeFromParent();
	}
	
	
	@UiFactory SphereEditorWidget makeSphereEditor() { // method name is insignificant
	    return new SphereEditorWidget(this.sphereID, this.sphereName);
	  }
	
}
