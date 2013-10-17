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
package com.velisphere.tigerspice.client;

import java.io.IOException;
import java.net.UnknownHostException;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.velisphere.tigerspice.client.HeroUnitLogin.HeroUnitLoginUiBinder;
import com.velisphere.tigerspice.server.VoltConnector;

public class Login implements EntryPoint {

	RootPanel rootPanelHeader;
	RootPanel rootPanelMain;
	VerticalPanel headerPanel;
	VerticalPanel mainPanel;
	NavBar navBar;
	
	public void onModuleLoad() {

	History.newItem("home");
		
	rootPanelHeader = RootPanel.get("stockList");
	rootPanelHeader.clear();
	rootPanelHeader.getElement().getStyle().setPosition(Position.RELATIVE);
	
	rootPanelMain = RootPanel.get("main");
	rootPanelMain.clear();
	rootPanelMain.getElement().getStyle().setPosition(Position.RELATIVE);
	
	headerPanel = new VerticalPanel();
	
	
	rootPanelHeader.add(headerPanel);
			
	navBar = new NavBar();
	headerPanel.add(navBar);
	
	
	mainPanel = new VerticalPanel();
	rootPanelMain.add(mainPanel);
		
	SplashCarouselWidget splashCarouselWidget = new SplashCarouselWidget();
	mainPanel.add(splashCarouselWidget);
		
	HeroUnitLogin heroUnitLogin = new HeroUnitLogin();
	mainPanel.add(heroUnitLogin);
		
	
	// LoginDialogBox loginDialogBox = new LoginDialogBox();
	// mainPanel.add(loginDialogBox);
	}
}
