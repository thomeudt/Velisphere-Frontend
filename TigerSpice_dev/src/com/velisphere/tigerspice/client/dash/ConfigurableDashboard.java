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
package com.velisphere.tigerspice.client.dash;



import java.util.Iterator;
import java.util.LinkedList;

import com.github.gwtbootstrap.client.ui.Bar;
import com.github.gwtbootstrap.client.ui.Breadcrumbs;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.PageHeader;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.github.gwtbootstrap.client.ui.StackProgressBar;
import com.github.gwtbootstrap.client.ui.TabLink;
import com.github.gwtbootstrap.client.ui.TabPane;
import com.github.gwtbootstrap.client.ui.TabPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.analytics.LogService;
import com.velisphere.tigerspice.client.analytics.LogServiceAsync;
import com.velisphere.tigerspice.client.appcontroller.NavBar;
import com.velisphere.tigerspice.client.appcontroller.SessionHelper;
import com.velisphere.tigerspice.client.endpoints.info.EndpointInformationWidget;
import com.velisphere.tigerspice.client.helper.HelperService;
import com.velisphere.tigerspice.client.helper.HelperServiceAsync;
import com.velisphere.tigerspice.client.users.LoginSuccess;
import com.velisphere.tigerspice.shared.DashData;
import com.velisphere.tigerspice.shared.GaugeData;
import com.velisphere.tigerspice.shared.MontanaStatsData;


public class ConfigurableDashboard extends Composite  {

interface MyBinder extends UiBinder<Widget, ConfigurableDashboard>{}
	
	private static MyBinder uiBinder = GWT.create(MyBinder.class);

	
	RootPanel rootPanel;
	RootPanel rootPanelHeader;
	VerticalPanel mainPanel;
	NavBar navBar;
	@UiField PageHeader pghPageHeader;
	@UiField Breadcrumbs brdMain;
	@UiField TabPanel tabPanel;
	@UiField TabPane tbpEndpoint;

	NavLink bread0;
	NavLink bread1;
	String userName;
	
	
 	
	public ConfigurableDashboard() {
	    
			
		initWidget(uiBinder.createAndBindUi(this));
		loadContent();			
	
	}
	 
	/*
	public ConfigurableDashboard(HandlerRegistration reg) {
		
		initWidget(uiBinder.createAndBindUi(this));
		reg.removeHandler();
		
		loadContent();
			
	}
	*/
	
	public void loadContent(){

		// set page header welcome back message
		pghPageHeader.setText("Dashboards");
    	
        	
    	/**
		NavBar navBar = new NavBar();
		navBar = (NavBar) RootPanel.get("stockList").getWidget(0);
		navBar.activateForCurrentUser();
		**/
    	
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
		buildTabPanel();
		
	}
	
	
	private void buildTabPanel()
	{
		
		tbpEndpoint.clear();
	    FlexBoard flexBoard = new FlexBoard();
	    tbpEndpoint.add(flexBoard);
		tabPanel.selectTab(0);
	
		
		GaugeServiceAsync gaugeService = GWT
				.create(GaugeService.class);

		gaugeService.getAllDashboardsForUser(SessionHelper.getCurrentUserID(), new AsyncCallback<LinkedList<DashData>>()
				{

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(LinkedList<DashData> result) {
						// TODO Auto-generated method stu
						
						Iterator<DashData> it = result.iterator();
						while (it.hasNext())
						{
							DashData dashData = it.next();
							TabLink tabLink = new TabLink();
							tabLink.setText(dashData.getDashboardName());
							tabPanel.add(tabLink);
							
						}
						
					}
			
				}
			);
			
      
		
	}
	
	
}
		
	