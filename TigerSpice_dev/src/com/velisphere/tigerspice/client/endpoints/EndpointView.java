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
package com.velisphere.tigerspice.client.endpoints;

import com.github.gwtbootstrap.client.ui.Breadcrumbs;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.PageHeader;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.github.gwtbootstrap.client.ui.TabLink;
import com.github.gwtbootstrap.client.ui.TabPane;
import com.github.gwtbootstrap.client.ui.TabPanel;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.actions.ActionService;
import com.velisphere.tigerspice.client.actions.ActionServiceAsync;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.appcontroller.SessionHelper;
import com.velisphere.tigerspice.client.endpointclasses.EPCService;
import com.velisphere.tigerspice.client.endpointclasses.EPCServiceAsync;
import com.velisphere.tigerspice.client.endpoints.actor.EndpointActorWidget;
import com.velisphere.tigerspice.client.endpoints.alerts.EndpointAlertsWidget;
import com.velisphere.tigerspice.client.endpoints.config.EndpointConfiguratorWidget;
import com.velisphere.tigerspice.client.endpoints.files.EndpointInboxWidget;
import com.velisphere.tigerspice.client.endpoints.info.EndpointInformationWidget;
import com.velisphere.tigerspice.client.endpoints.sensor.EndpointSensorWidget;
import com.velisphere.tigerspice.client.favorites.FavoriteService;
import com.velisphere.tigerspice.client.favorites.FavoriteServiceAsync;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.helper.VeliConstants;
import com.velisphere.tigerspice.client.locator.logical.LogicalMapWidget;
import com.velisphere.tigerspice.client.locator.maps.HeatMapLayerWidget;
import com.velisphere.tigerspice.client.locator.maps.InfoWindowMapWidget;
import com.velisphere.tigerspice.client.locator.maps.PolylineMapWidget;
import com.velisphere.tigerspice.client.spheres.SphereLister;
import com.velisphere.tigerspice.client.spheres.SphereView;
import com.velisphere.tigerspice.client.users.LoginSuccess;
import com.velisphere.tigerspice.shared.EPCData;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.FavoriteData;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

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
	Image imgEPCImage;
	@UiField
	TabPanel tabPanel;
	@UiField
	TabPane tbpEndpoint;
	@UiField
	TabLink tblGeneral;
	@UiField
	TabLink tblSensors;
	@UiField
	TabLink tblActors;
	@UiField
	TabLink tblAlerts;
	@UiField
	TabLink tblConfiguration;
	@UiField
	TabLink tblConnectionMap;
	@UiField
	TabLink tblLocationMap;
	@UiField
	TabLink tblInbox;
	@UiField
	Column imageColumn;
	
	String endpointClassID;
	String endpointID;
	String sphereID;
	String sphereName;
	NavLink bread0;
	NavLink bread1;
	NavLink bread2;
	NavLink bread3;
	String favoriteID;
	
	private TextBox endpointChangeNameField;
	
	// @UiField EndpointsForSphereListerWidget_unused endpointList;

	private EndpointServiceAsync rpcServiceEndpoint;
	

	interface SphereOverviewUiBinder extends UiBinder<Widget, SphereView> {
	}
	
	
	

	public EndpointView(final String sphereID, final String sphereName,
			final String endpointID, String endpointName, final String endpointClassID) {

		
		rpcServiceEndpoint = GWT.create(EndpointService.class);
	
		this.endpointClassID = endpointClassID;
		this.endpointID = endpointID;
		this.sphereID = sphereID;
		this.sphereName = sphereName;
		
		
		initWidget(uiBinder.createAndBindUi(this));
		
		initialize();
		
		
	}
	
	
	public EndpointView(final String sphereID, final String sphereName,
			final String endpointID, String endpointName, final String endpointClassID, final int viewMode) {
		
		rpcServiceEndpoint = GWT.create(EndpointService.class);
		
		this.endpointClassID = endpointClassID;
		this.endpointID = endpointID;
		this.sphereID = sphereID;
		this.sphereName = sphereName;
		
		
		initWidget(uiBinder.createAndBindUi(this));
		
		initialize();
		
		
		switch(viewMode)
		{
		case VeliConstants.ENDPOINT_VIEWMODE_CONFIG:   {
						tbpEndpoint.clear();
						EndpointConfiguratorWidget endpointConfiguratorWidget = new EndpointConfiguratorWidget(endpointID);
						tbpEndpoint.add(endpointConfiguratorWidget);
						tabPanel.selectTab(4);
					}  	  
		case VeliConstants.ENDPOINT_VIEWMODE_ALERTS:   {
						tbpEndpoint.clear();
						EndpointAlertsWidget alertsWidget = new EndpointAlertsWidget(endpointID, tbpEndpoint);
						tbpEndpoint.add(alertsWidget);
						tabPanel.selectTab(3);
						 
		}  	  
		}
		
		
		
	}
	
	private void initialize()
	{
		bread0 = new NavLink();
		bread0.setText("Home");
		brdMain.add(bread0);		
		bread1 = new NavLink();
		bread1.setText("Endpoint Manager");
		brdMain.add(bread1);
	
		
		// do not add breadcrumb for sphere if not redirected to endpoint via sphere
		
		if (sphereName.length() != 0)
		{
			bread2 = new NavLink();
			bread2.setText(sphereName);
			brdMain.add(bread2);
			bread2.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {

					RootPanel mainPanel = RootPanel.get("main");
					mainPanel.clear();
					SphereView sphereOverview = new SphereView(sphereID);
					mainPanel.add(sphereOverview);

				}
			});

		}
	
		

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

		
		setEndpointNameAndImage(this.endpointID);
		buildTabPanes();
		
		final VerticalPanel vp = new VerticalPanel();
		
	
		vp.add(addEditNameWidget());
		
		
		final FavoriteServiceAsync favoriteService = GWT
				.create(FavoriteService.class);
		
		favoriteService.getFavoriteForTargetID(endpointID, new AsyncCallback<FavoriteData>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(FavoriteData result) {
				// TODO Auto-generated method stub
				
				if (result.getId() == null)
				{
					vp.add(addFavoriteWidget());			
				}
				else
				{
					favoriteID = result.getId();
					vp.add(removeFavoriteWidget());
					
				}
					
				
			}
			
		});
		
		
		
		pghEndpointName.add(vp);
		
		
		
	}
	
			
	
	private HorizontalPanel addEditNameWidget()
	{
		HorizontalPanel hp = new HorizontalPanel();
		
		Icon icnEditEndpointName = new Icon();
		icnEditEndpointName.setType(IconType.EDIT);
		hp.add(icnEditEndpointName);
		final Anchor ancEditEndpointName = new Anchor();
		ancEditEndpointName.setText(" Change Name");
		ancEditEndpointName.setHref("#");

			
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
		hp.add(ancEditEndpointName);
		return hp;

	}
	
	private HorizontalPanel addFavoriteWidget()
	{
		HorizontalPanel hp = new HorizontalPanel();
		
		Icon icnEditEndpointName = new Icon();
		icnEditEndpointName.setType(IconType.STAR_EMPTY);
		hp.add(icnEditEndpointName);
		final Anchor ancEditEndpointName = new Anchor();
		ancEditEndpointName.setText(" Add Favorite");
		ancEditEndpointName.setHref("#");
		
		ancEditEndpointName.addClickHandler(
				new ClickHandler(){ 
				public void onClick(ClickEvent event)  {
					FavoriteData favoriteData = new FavoriteData();
					favoriteData.setName("Device: " + pghEndpointName.getText());
					favoriteData.setTargetId(endpointID);
					favoriteData.setUserId(SessionHelper.getCurrentUserID());
					favoriteData.setType(VeliConstants.FAVORITE_ENDPOINT);
					
										
					final FavoriteServiceAsync favoriteService = GWT
							.create(FavoriteService.class);
					
					favoriteService.addFavorite(favoriteData, new AsyncCallback<String>(){

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onSuccess(String result) {
							// TODO Auto-generated method stub
							
							AppController.openEndpoint(endpointID);
							
						}
						
					}
							
							);
					
				
				
				}
				});
		hp.add(ancEditEndpointName);
		return hp;

	}
	
	
	private HorizontalPanel removeFavoriteWidget()
	{
		HorizontalPanel hp = new HorizontalPanel();
		
		Icon icnEditEndpointName = new Icon();
		icnEditEndpointName.setType(IconType.STAR);
		hp.add(icnEditEndpointName);
		final Anchor ancEditEndpointName = new Anchor();
		ancEditEndpointName.setText(" Remove Favorite");
		ancEditEndpointName.setHref("#");
		
		ancEditEndpointName.addClickHandler(
				new ClickHandler(){ 
				public void onClick(ClickEvent event)  {
										
										
					final FavoriteServiceAsync favoriteService = GWT
							.create(FavoriteService.class);
					
					favoriteService.deleteFavoriteForFavoriteID(favoriteID, new AsyncCallback<String>(){

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onSuccess(String result) {
							// TODO Auto-generated method stub
							
							AppController.openEndpoint(endpointID);
							
						}
						
					}
							
							);
					
				
				
				}
				});
		hp.add(ancEditEndpointName);
		return hp;

	}


	
	private void setEndpointNameAndImage(String endpointID) {

		final AnimationLoading animationLoading = new AnimationLoading();
		
		animationLoading.showLoadAnimation("Loading Image");
	
		EndpointServiceAsync endpointService = GWT
				.create(EndpointService.class);
		
		
		endpointService.getEndpointForEndpointID(endpointID,
				new AsyncCallback<EndpointData>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						animationLoading.removeLoadAnimation();
					}

					@Override
					public void onSuccess(EndpointData result) {
						// TODO Auto-generated method stub
						
						animationLoading.removeLoadAnimation();

						bread3 = new NavLink();
						bread3.setText(result.getName());
						brdMain.add(bread3);
						pghEndpointName.setText(result.getName());

						
						EPCServiceAsync epcService = GWT
								.create(EPCService.class);
						
						final AnimationLoading animationLoading = new AnimationLoading();
						
						animationLoading.showLoadAnimation("Loading Class");
					
						
						epcService
								.getEndpointClassForEndpointClassID(
										result.endpointclassId,
										new AsyncCallback<EPCData>() {

											@Override
											public void onFailure(
													Throwable caught) {
												// TODO Auto-generated method
												// stub
												animationLoading.removeLoadAnimation();
											}

											@Override
											public void onSuccess(EPCData result) {
												// TODO Auto-generated method
												// stub

												
												imgEPCImage.setUrl(result.endpointclassImageURL);
												Integer imageWidth = imageColumn.getOffsetWidth()-15;
												imgEPCImage.setWidth("100%");
												animationLoading.removeLoadAnimation();
											

																								
												
											}

										});


		
											}

				});

	
	}
	
	
	private void buildTabPanes() {

		tblGeneral.addClickHandler(new ClickHandler() {
	          public void onClick(ClickEvent event) {
	        	    tbpEndpoint.clear();
	        	    EndpointInformationWidget informationWidget = new EndpointInformationWidget(endpointID);
	        		tbpEndpoint.add(informationWidget);
	              
	          }
	      });
		
		tblSensors.addClickHandler(new ClickHandler() {
	          public void onClick(ClickEvent event) {
	        	  tbpEndpoint.clear();
	        	  EndpointSensorWidget sensorWidget = new EndpointSensorWidget(sphereID, endpointID);
	        	  tbpEndpoint.add(sensorWidget);
	              
	          }
	      });
		
		tblActors.addClickHandler(new ClickHandler() {
	          public void onClick(ClickEvent event) {
	        	  tbpEndpoint.clear();
	        	  EndpointActorWidget actorWidget = new EndpointActorWidget(sphereID, endpointID);
	        	  tbpEndpoint.add(actorWidget);
	              
	          }
	      });
		
		tblAlerts.addClickHandler(new ClickHandler() {
	          public void onClick(ClickEvent event) {
	        	  tbpEndpoint.clear();
	        	  EndpointAlertsWidget alertsWidget = new EndpointAlertsWidget(endpointID, tbpEndpoint);
	        	  tbpEndpoint.add(alertsWidget);
	        	 
	        		
	              
	          }
	      });
		
		tblConfiguration.addClickHandler(new ClickHandler() {
	          public void onClick(ClickEvent event) {
	        	  tbpEndpoint.clear();
	        	  EndpointConfiguratorWidget endpointConfiguratorWidget = new EndpointConfiguratorWidget(endpointID);
	        	  tbpEndpoint.add(endpointConfiguratorWidget);
	              
	          }
	      });
		
		
		tblConnectionMap.addClickHandler(new ClickHandler() {
	          public void onClick(ClickEvent event) {
	        	  tbpEndpoint.clear();
	        	  LogicalMapWidget gMap = new LogicalMapWidget(endpointID);
	        	  tbpEndpoint.add(gMap);
	        		
	              
	          }
	      });
		
		tblLocationMap.addClickHandler(new ClickHandler() {
	          public void onClick(ClickEvent event) {
	        	  tbpEndpoint.clear();
	        	  InfoWindowMapWidget gMap = new InfoWindowMapWidget(endpointID);
	        		tbpEndpoint.add(gMap);
	        		
	              
	          }
	      });

		tblInbox.addClickHandler(new ClickHandler() {
	          public void onClick(ClickEvent event) {
	        	  tbpEndpoint.clear();
	        	  EndpointInboxWidget endpointInboxWidget = new EndpointInboxWidget(endpointID);
	        	  
	        		tbpEndpoint.add(endpointInboxWidget);
	        		
	              
	          }
	      });

		
		// load default
		
		  tbpEndpoint.clear();
  	    EndpointInformationWidget informationWidget = new EndpointInformationWidget(endpointID);
  		tbpEndpoint.add(informationWidget);
		tabPanel.selectTab(0);
			
		
		
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
	
	
	
}
