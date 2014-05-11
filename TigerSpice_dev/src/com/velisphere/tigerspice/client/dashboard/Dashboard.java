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
package com.velisphere.tigerspice.client.dashboard;



import com.github.gwtbootstrap.client.ui.Bar;
import com.github.gwtbootstrap.client.ui.Breadcrumbs;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.PageHeader;
import com.github.gwtbootstrap.client.ui.StackProgressBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.NavBar;
import com.velisphere.tigerspice.client.helper.SessionHelper;
import com.velisphere.tigerspice.client.users.LoginSuccess;


public class Dashboard extends Composite  {

interface MyBinder extends UiBinder<Widget, Dashboard>{}
	
	private static MyBinder uiBinder = GWT.create(MyBinder.class);

	
	RootPanel rootPanel;
	RootPanel rootPanelHeader;
	VerticalPanel mainPanel;
	NavBar navBar;
	@UiField PageHeader pageHeader;
	@UiField Bar pgbGreen;
	@UiField Bar pgbYellow;
	@UiField Bar pgbRed;
	@UiField StackProgressBar pgbUtilization;
	@UiField
	Breadcrumbs brdMain;
	NavLink bread0;
	NavLink bread1;
	String userName;
	
 	
	public Dashboard() {
	    
			
		initWidget(uiBinder.createAndBindUi(this));
		loadContent();
		
	
		

		
	}
	 
	public Dashboard(HandlerRegistration reg) {
		
		initWidget(uiBinder.createAndBindUi(this));
		reg.removeHandler();
		
		loadContent();
			
	}
	
	public void loadContent(){

		
		
		//initWidget(uiBinder.createAndBindUi(this));	
		// set history for back button support
		History.newItem("dashboard");
		
		//
		
		
		// set page header welcome back message
    	pageHeader.setText("Dashboard for " + SessionHelper.getCurrentUserName());
    	pgbGreen.setPercent(30);
    	
    	pgbRed.setPercent(0);
    	pgbYellow.setPercent(0);
    	pgbGreen.setText("30/100 endpoints activated");
    	
		NavBar navBar = new NavBar();
		navBar = (NavBar) RootPanel.get("stockList").getWidget(0);
		navBar.activateForCurrentUser();
		
		bread0 = new NavLink();
		bread0.setText("Home");
		brdMain.add(bread0);
		bread1 = new NavLink();
		bread1.setText("Dashboard");
		brdMain.add(bread1);
		bread0.addClickHandler(new ClickHandler() {
		public void onClick(ClickEvent event) {

				RootPanel.get("main").clear();
				LoginSuccess loginSuccess = new LoginSuccess();
				RootPanel.get("main").add(loginSuccess);

			}
		});

		
	}
		

	
	
			
	
	
	
}
