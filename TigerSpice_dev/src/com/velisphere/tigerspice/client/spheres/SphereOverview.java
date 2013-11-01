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

import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.Breadcrumbs;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.PageHeader;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;


public class SphereOverview extends Composite {

	private static SphereOverviewUiBinder uiBinder = GWT
			.create(SphereOverviewUiBinder.class);

	
	
	@UiField Breadcrumbs brdMain;
	
	//@UiField EndpointsForSphereListerWidget_unused endpointList;
	
	@UiField PageHeader pghSphereNameHeader;
	
	String sphereID;
	String sphereName;
	
	
	interface SphereOverviewUiBinder extends UiBinder<Widget, SphereOverview> {
	}

	public SphereOverview(final String sphereID, String sphereName) {
		
		this.sphereID = sphereID;
		this.sphereName = sphereName;
		initWidget(uiBinder.createAndBindUi(this));
		pghSphereNameHeader.setText(sphereName);
		
	
		
		NavLink bread1 = new NavLink();
		bread1.setText("Sphere Overview");
		brdMain.add(bread1);
		NavLink bread2 = new NavLink();
		bread2.setText(sphereName);
		brdMain.add(bread2);

		bread1.addClickHandler(
				new ClickHandler(){
					public void onClick(ClickEvent event){
			
						RootPanel mainPanel = RootPanel.get("main");
						mainPanel.clear();
						SphereLister sphereLister = new SphereLister(); 		
						mainPanel.add(sphereLister);
						
					}
				});
		
	
	
	}

	
	@UiFactory SphereEditorWidget makeSphereEditor() { // method name is insignificant
	    return new SphereEditorWidget(this.sphereID, this.sphereName);
	  }
	
}
