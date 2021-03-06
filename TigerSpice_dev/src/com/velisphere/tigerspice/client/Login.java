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
package com.velisphere.tigerspice.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.velisphere.tigerspice.client.appcontroller.NavBar;
import com.velisphere.tigerspice.client.appcontroller.SessionHelper;
import com.velisphere.tigerspice.client.users.LoginSuccess;

public class Login implements EntryPoint {

	RootPanel rootPanelHeader;
	RootPanel rootPanelMain;
	RootPanel rootPanelBanderole;
	VerticalPanel headerPanel;
	HorizontalPanel mainPanel;
	NavBar navBar;
	
	public void onModuleLoad() {


		displayLoginScreen();		
		
	
	
		
	History.addValueChangeHandler(new ValueChangeHandler<String>() {
		   @Override
		   public void onValueChange(ValueChangeEvent<String> event) {
		      String historyToken = event.getValue();
		      /* parse the history token */
		      try {
		         if (historyToken.equals("home")) {
		        	 displayLoginScreen();          
		         } else {
		        	 // System.out.println("nix");
		         }
		      } catch (IndexOutOfBoundsException e) {
		    	  // System.out.println("nix"); 
		      }
		   }
		});	
		
	
	// LoginDialogBox loginDialogBox = new LoginDialogBox();
	// mainPanel.add(loginDialogBox);
	}


	private void displayLoginScreen(){
	
		History.newItem("home");
		rootPanelHeader = RootPanel.get("stockList");
		rootPanelHeader.clear();
		rootPanelHeader.getElement().getStyle().setPosition(Position.RELATIVE);
		
		rootPanelMain = RootPanel.get("main");
		rootPanelMain.clear();
		rootPanelMain.getElement().getStyle().setPosition(Position.RELATIVE);
		
		rootPanelBanderole = RootPanel.get("banderole");
		rootPanelBanderole.clear();
		rootPanelBanderole.getElement().getStyle().setPosition(Position.RELATIVE);
		
		headerPanel = new VerticalPanel();
		rootPanelHeader.add(headerPanel);
				
		navBar = new NavBar();
		headerPanel.add(navBar);
		
		
		mainPanel = new HorizontalPanel();
		rootPanelMain.add(mainPanel);
		
		
		// the following is just a temp work around for damn IE
		
		/**
		int rootPanelWidth = RootPanel.get().getOffsetWidth();
		
		if (rootPanelWidth < 740) {
			rootPanelWidth = 740; 
		}else rootPanelWidth = (rootPanelWidth * 33) / 100;
		
		String rootPanelWidthPx = rootPanelWidth + "px"; 
		**/
		//SplashCarouselWidget splashCarouselWidget = new SplashCarouselWidget();
		//splashCarouselWidget.setWidth("550px");
		//mainPanel.add(splashCarouselWidget);
		
			
		HeroUnitLogin heroUnitLogin = new HeroUnitLogin();
		//heroUnitLogin.setWidth("550px");
		//heroUnitLogin.setHeight("450px");
		mainPanel.add(heroUnitLogin);

	}

}
