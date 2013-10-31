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
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.PageHeader;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.dataproviders.EndpointAsyncDataProvider;
import com.velisphere.tigerspice.client.spheres.SphereEditorWidget;
import com.velisphere.tigerspice.client.spheres.SphereLister;
import com.velisphere.tigerspice.client.spheres.SphereOverview;

import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.PropertyData;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;


// THIS WIDGET IS USING UIBINDER!!!

public class EndpointView extends Composite {
	
	interface EndpointViewUiBinder extends
	UiBinder<Widget, EndpointView> {
}

	private static EndpointViewUiBinder uiBinder = GWT
			.create(EndpointViewUiBinder.class);

	@UiField PageHeader pghEndpointName;
	@UiField Breadcrumbs brdMain;
	//@UiField EndpointsForSphereListerWidget_unused endpointList;
	
	String sphereID;
	String endpointID;
	
	
	interface SphereOverviewUiBinder extends UiBinder<Widget, SphereOverview> {
	}

	public EndpointView(final String sphereID, final String sphereName, String endpointID, String endpointName) {
		
		this.sphereID = sphereID;
		initWidget(uiBinder.createAndBindUi(this));
		pghEndpointName.setText(endpointName);
		
	
		
		NavLink bread1 = new NavLink();
		bread1.setText("Sphere Overview");
		brdMain.add(bread1);
		NavLink bread2 = new NavLink();
		bread2.setText(sphereName);
		brdMain.add(bread2);
		NavLink bread3 = new NavLink();
		bread3.setText(endpointName);
		brdMain.add(bread3);
		
		bread1.addClickHandler(
				new ClickHandler(){
					public void onClick(ClickEvent event){
			
						RootPanel mainPanel = RootPanel.get("main");
						mainPanel.clear();
						SphereLister sphereLister = new SphereLister(); 		
						mainPanel.add(sphereLister);
						
					}
				});
		
		bread2.addClickHandler(
				new ClickHandler(){
					public void onClick(ClickEvent event){
			
						RootPanel mainPanel = RootPanel.get("main");
						mainPanel.clear();
						SphereOverview sphereOverview = new SphereOverview(sphereID, sphereName); 		
						mainPanel.add(sphereOverview);
						
					}
				});
	
	}

	
	
	}
