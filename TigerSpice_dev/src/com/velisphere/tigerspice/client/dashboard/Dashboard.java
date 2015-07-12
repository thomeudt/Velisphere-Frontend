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



import java.util.Iterator;
import java.util.LinkedList;

import com.github.gwtbootstrap.client.ui.Bar;
import com.github.gwtbootstrap.client.ui.Breadcrumbs;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.PageHeader;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.github.gwtbootstrap.client.ui.StackProgressBar;
import com.github.gwtbootstrap.client.ui.TabPane;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.analytics.LogService;
import com.velisphere.tigerspice.client.analytics.LogServiceAsync;
import com.velisphere.tigerspice.client.appcontroller.NavBar;
import com.velisphere.tigerspice.client.appcontroller.SessionHelper;
import com.velisphere.tigerspice.client.helper.HelperService;
import com.velisphere.tigerspice.client.helper.HelperServiceAsync;
import com.velisphere.tigerspice.client.users.LoginSuccess;
import com.velisphere.tigerspice.shared.MontanaStatsData;


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
	@UiField Breadcrumbs brdMain;
	@UiField Paragraph pgpLogCount;
	@UiField Paragraph pgpUtilInfo;
	@UiField Paragraph pgpStatusInfo;
	@UiField TabPane tbpAlerts;
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

		
		
		
		
		// set page header welcome back message
    	pageHeader.setText("Dashboard for " + SessionHelper.getCurrentUserName());
    	pgbGreen.setPercent(100);
    	
    	pgbRed.setPercent(0);
    	pgbYellow.setPercent(0);
    	pgbUtilization.setHeight("20px");
    
    	pgpUtilInfo.setText("You are running a public beta version of VeliSphere, which gives you unlimited usage rights (in exchange for no performance guarantee "+
    	"or any level of SLA). Usage metering and charge back will be introduced after completion of the beta phase.");
    	
    	pgpStatusInfo.setText("System is up and operating normally. Last update: June 29, 2015 / 12:19 CET");
    	
    	tbpAlerts.add(new AlertsState());
    	
    	
    	final LogServiceAsync logService = GWT
				.create(LogService.class);
		
    	    	

    	logService.getLogCount(new AsyncCallback<LinkedList<String>>(){
    		@Override
    		public void onFailure(Throwable caught) {
    			// TODO Auto-generated method stub
    			
    		}

    		@Override
    		public void onSuccess(LinkedList<String> result) {
    			// TODO Auto-generated method stub
    	
    			int logCount = 0;
    			Iterator<String> iT = result.iterator();
    			while (iT.hasNext()){
    				int entry = Integer.parseInt(iT.next());
    				logCount = logCount + entry;
    			}
    			
    			pgpLogCount.setText("Number of record sets in analytics database: " + logCount);
    		}
    		});

    	

    	
    	
    	
    	
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
	}
}
		
	
		

	
	
			
	
	
	
