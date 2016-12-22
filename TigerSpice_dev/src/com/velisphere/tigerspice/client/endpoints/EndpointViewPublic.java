package com.velisphere.tigerspice.client.endpoints;

import com.github.gwtbootstrap.client.ui.Breadcrumbs;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.PageHeader;
import com.github.gwtbootstrap.client.ui.TabLink;
import com.github.gwtbootstrap.client.ui.TabPane;
import com.github.gwtbootstrap.client.ui.TabPanel;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.endpointclasses.EPCService;
import com.velisphere.tigerspice.client.endpointclasses.EPCServiceAsync;
import com.velisphere.tigerspice.client.endpoints.EndpointView.EndpointViewUiBinder;
import com.velisphere.tigerspice.client.endpoints.info.EndpointInformationWidget;
import com.velisphere.tigerspice.client.endpoints.sensor.EndpointSensorWidget;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.locator.logical.LogicalMapWidget;
import com.velisphere.tigerspice.client.locator.maps.InfoWindowMapWidget;
import com.velisphere.tigerspice.client.spheres.SphereLister;
import com.velisphere.tigerspice.client.spheres.SphereView;
import com.velisphere.tigerspice.client.users.LoginSuccess;
import com.velisphere.tigerspice.shared.EPCData;
import com.velisphere.tigerspice.shared.EndpointData;


public class EndpointViewPublic extends Composite {

	interface EndpointViewPublicUiBinder extends UiBinder<Widget, EndpointViewPublic> {
	}

	private static EndpointViewPublicUiBinder uiBinder = GWT
			.create(EndpointViewPublicUiBinder.class);

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
	TabLink tblLocationMap;
	@UiField
	Column imageColumn;

	
	String endpointClassID;
	String endpointID;
	String sphereID;
	NavLink bread0;
	NavLink bread1;
	NavLink bread2;
	NavLink bread3;
	
	private TextBox endpointChangeNameField;
	
	// @UiField EndpointsForSphereListerWidget_unused endpointList;

	private EndpointServiceAsync rpcServiceEndpoint;
	

	interface SphereOverviewUiBinder extends UiBinder<Widget, SphereView> {
	}
	
	
	

	public EndpointViewPublic(String endpointID) {

		
		rpcServiceEndpoint = GWT.create(EndpointService.class);
	
		
		this.endpointID = endpointID;
		

		
		
		initWidget(uiBinder.createAndBindUi(this));
		
		bread0 = new NavLink();
		bread0.setText("Home");
		brdMain.add(bread0);		
		bread1 = new NavLink();
		bread1.setText("Endpoint Manager");
		brdMain.add(bread1);
	
		
		// do not add breadcrumb for sphere if not redirected to endpoint via sphere
		
		
		

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
		
	
		
		
		
		tblLocationMap.addClickHandler(new ClickHandler() {
	          public void onClick(ClickEvent event) {
	        	  tbpEndpoint.clear();
	        	  InfoWindowMapWidget gMap = new InfoWindowMapWidget(endpointID);
	        		tbpEndpoint.add(gMap);
	        		
	              
	          }
	      });
		
		// load default
		
		  tbpEndpoint.clear();
  	    EndpointInformationWidget informationWidget = new EndpointInformationWidget(endpointID);
  		tbpEndpoint.add(informationWidget);
		tabPanel.selectTab(0);
			
		
		
	}

	

	
		
	
}
