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
package com.velisphere.tigerspice.client.endpoints;

import com.github.gwtbootstrap.client.ui.Breadcrumbs;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.PageHeader;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.github.gwtbootstrap.client.ui.Strong;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.LoginDialogBox;
import com.velisphere.tigerspice.client.checks.CheckEditorWidget;
import com.velisphere.tigerspice.client.dataproviders.EndpointAsyncDataProvider;
import com.velisphere.tigerspice.client.endpointclasses.EPCService;
import com.velisphere.tigerspice.client.endpointclasses.EPCServiceAsync;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.properties.PropertyEditorWidget;
import com.velisphere.tigerspice.client.spheres.SphereEditorWidget;
import com.velisphere.tigerspice.client.spheres.SphereLister;
import com.velisphere.tigerspice.client.spheres.SphereView;
import com.velisphere.tigerspice.client.users.LoginSuccess;
import com.velisphere.tigerspice.shared.EPCData;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.PropertyData;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

// THIS WIDGET IS USING UIBINDER!!!

public class EndpointView extends Composite {

	interface EndpointViewUiBinder extends UiBinder<Widget, EndpointView> {
	}

	private static EndpointViewUiBinder uiBinder = GWT
			.create(EndpointViewUiBinder.class);

	@UiField
	PageHeader pghEndpointName;
	@UiField
	Breadcrumbs brdMain;
	@UiField
	Paragraph pgpEndpointClassName;
	
	private String endpointClassID;
	private String endpointID;
	NavLink bread0;
	NavLink bread1;
	NavLink bread2;
	NavLink bread3;
	
	private TextBox endpointChangeNameField;
	
	// @UiField EndpointsForSphereListerWidget_unused endpointList;

	private EndpointServiceAsync rpcServiceEndpoint;
	private EPCServiceAsync rpcServiceEndpointClass;

	interface SphereOverviewUiBinder extends UiBinder<Widget, SphereView> {
	}
	
	
	

	public EndpointView(final String sphereID, final String sphereName,
			final String endpointID, String endpointName, final String endpointClassID) {

		rpcServiceEndpoint = GWT.create(EndpointService.class);
		rpcServiceEndpointClass = GWT.create(EPCService.class);
		this.endpointClassID = endpointClassID;
		this.endpointID = endpointID;

		
		
		initWidget(uiBinder.createAndBindUi(this));
		
		bread0 = new NavLink();
		bread0.setText("Home");
		brdMain.add(bread0);		
		bread1 = new NavLink();
		bread1.setText("Sphere Overview");
		brdMain.add(bread1);
		bread2 = new NavLink();
		bread2.setText(sphereName);
		brdMain.add(bread2);
		bread3 = new NavLink();
		bread3.setText(endpointName);
		brdMain.add(bread3);


		bread0.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				RootPanel.get("main").clear();
				LoginSuccess loginSuccess = new LoginSuccess();
				RootPanel.get("main").add(loginSuccess);

			}
		});

		
		bread1.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				RootPanel mainPanel = RootPanel.get("main");
				mainPanel.clear();
				SphereLister sphereLister = new SphereLister();
				mainPanel.add(sphereLister);

			}
		});

		bread2.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				RootPanel mainPanel = RootPanel.get("main");
				mainPanel.clear();
				SphereView sphereOverview = new SphereView(sphereID,
						sphereName);
				mainPanel.add(sphereOverview);

			}
		});

		
		
		
		pghEndpointName.setText(endpointName);
		
		Icon icnEditEndpointName = new Icon();
		icnEditEndpointName.setType(IconType.EDIT);
		// RootPanel.get().add(icnEditClassName, pghEndpointName..getAbsoluteLeft(), pghEndpointName.getAbsoluteTop());
		pghEndpointName.add(icnEditEndpointName);
		final Anchor ancEditEndpointName = new Anchor();
		ancEditEndpointName.setText(" Change Name of this Endpoint");
		ancEditEndpointName.setHref("#");
	
		// endpointChangeNameDialogBox = new EndpointChangeNameDialogBox();
		// endpointChangeNameDialogBox.addValueChangeHandler(new ValueChangeHandler<String>() {
			
		endpointChangeNameField = new TextBox();
		final Button okButton = new Button();
		okButton.setText("OK");
		okButton.setType(ButtonType.SUCCESS);
		final PopupPanel nameChangePopup = new PopupPanel();
		
		ancEditEndpointName.addClickHandler(
				new ClickHandler(){ 
				public void onClick(ClickEvent event)  {
					
					
					if (nameChangePopup.isShowing()) {
						
						nameChangePopup.removeFromParent();
					} else
					{						
						endpointChangeNameField.setText(pghEndpointName.getText());
						HorizontalPanel horizontalPanel = new HorizontalPanel();
						horizontalPanel.add(endpointChangeNameField);
						horizontalPanel.add(okButton.asWidget());
						nameChangePopup.clear();
						nameChangePopup.add(horizontalPanel);
						nameChangePopup.setModal(true);
						nameChangePopup.setAutoHideEnabled(true);
						nameChangePopup.setAnimationEnabled(true);
						nameChangePopup.showRelativeTo(ancEditEndpointName);
						endpointChangeNameField.setFocus(true);
						okButton.addClickHandler(
								new ClickHandler(){ 
								public void onClick(ClickEvent event)  {
									
									final String newEndpointName = endpointChangeNameField.getText();
									
									nameChangePopup.hide();
									final AnimationLoading animationLoading = new AnimationLoading();
									showLoadAnimation(animationLoading);

									rpcServiceEndpoint.updateEndpointNameForEndpointID(endpointID, newEndpointName,
											new AsyncCallback<String>() {

												@Override
												public void onFailure(Throwable caught) {
													// TODO Auto-generated method stub
													removeLoadAnimation(animationLoading);
												}

												@Override
												public void onSuccess(String result) {
													// TODO Auto-generated method stub
													pghEndpointName.setText(newEndpointName);
													brdMain.remove(bread3);
													bread3.setText(newEndpointName);
													brdMain.add(bread3);
													
													removeLoadAnimation(animationLoading);


												}

											});

								}
								});
					}
				}
				});
		pghEndpointName.add(ancEditEndpointName);
	
		
		
		

		
		setEndpointClassName(endpointID);

	}

	private void setEndpointClassName(String endpointID) {

		final AnimationLoading animationLoading = new AnimationLoading();
		showLoadAnimation(animationLoading);
	

		rpcServiceEndpoint.getEndpointForEndpointID(endpointID,
				new AsyncCallback<EndpointData>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						removeLoadAnimation(animationLoading);
					}

					@Override
					public void onSuccess(EndpointData result) {
						// TODO Auto-generated method stub

						rpcServiceEndpointClass
								.getEndpointClassForEndpointClassID(
										result.getEpcId(),
										new AsyncCallback<EPCData>() {

											@Override
											public void onFailure(
													Throwable caught) {
												// TODO Auto-generated method
												// stub
												removeLoadAnimation(animationLoading);
											}

											@Override
											public void onSuccess(EPCData result) {
												// TODO Auto-generated method
												// stub

												pgpEndpointClassName
														.setText(result.endpointclassName);
												
												
												removeLoadAnimation(animationLoading);
												
												
											}

										});

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
	
	@UiFactory PropertyEditorWidget makePropertyEditor() { // method name is insignificant
	    return new PropertyEditorWidget(this.endpointClassID, this.endpointID);
	  }
	
	@UiFactory CheckEditorWidget makeCheckEditor() { // method name is insignificant
	    return new CheckEditorWidget(this.endpointID);
	  }
	
	

}