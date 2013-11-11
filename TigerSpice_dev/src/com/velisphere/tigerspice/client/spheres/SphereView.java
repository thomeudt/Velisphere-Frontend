/*******************************************************************************
 * CONFIDENTIAL INFORMATION
 *  __________________
 *  
 *   Copyright (C) 2013 Thorsten Meudt 
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

import com.github.gwtbootstrap.client.ui.Breadcrumbs;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.PageHeader;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.users.LoginSuccess;


public class SphereView extends Composite {

	private static SphereOverviewUiBinder uiBinder = GWT
			.create(SphereOverviewUiBinder.class);

	
	
	@UiField Breadcrumbs brdMain;
	
	
	@UiField PageHeader pghSphereNameHeader;
	
	private SphereServiceAsync rpcServiceSphere;
	
	String sphereID;  
	String sphereName;
	NavLink bread0;
	NavLink bread1;
	NavLink bread2;
	private TextBox sphereChangeNameField;
	
	
	
	interface SphereOverviewUiBinder extends UiBinder<Widget, SphereView> {
	}

	public SphereView(final String sphereID, String sphereName) {
		
		this.sphereID = sphereID;
		this.sphereName = sphereName;
		rpcServiceSphere = GWT.create(SphereService.class);
		initWidget(uiBinder.createAndBindUi(this));
		pghSphereNameHeader.setText(sphereName);
		
		bread0 = new NavLink();
		bread0.setText("Home");
		brdMain.add(bread0);		
		bread1 = new NavLink();
		bread1.setText("Sphere Overview");
		brdMain.add(bread1);
		bread2 = new NavLink();
		bread2.setText(sphereName);
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

		
		Icon icnEditEndpointName = new Icon();
		icnEditEndpointName.setType(IconType.EDIT);
		// RootPanel.get().add(icnEditClassName, pghEndpointName..getAbsoluteLeft(), pghEndpointName.getAbsoluteTop());
		pghSphereNameHeader.add(icnEditEndpointName);
		final Anchor ancEditSphereName = new Anchor();
		ancEditSphereName.setText(" Change Name of this Sphere");
		ancEditSphereName.setHref("#");
	
		// endpointChangeNameDialogBox = new EndpointChangeNameDialogBox();
		// endpointChangeNameDialogBox.addValueChangeHandler(new ValueChangeHandler<String>() {
			
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
