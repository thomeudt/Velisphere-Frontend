package com.velisphere.tigerspice.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.velisphere.tigerspice.client.HeroUnitLogin.HeroUnitLoginUiBinder;

public class Login implements EntryPoint {

	RootPanel rootPanel;
	VerticalPanel mainPanel;
	NavBar navBar;
	
	public void onModuleLoad() {

	History.newItem("home");
		
	rootPanel = RootPanel.get("stockList");
	rootPanel.getElement().getStyle().setPosition(Position.RELATIVE);
	
	mainPanel = new VerticalPanel();
	rootPanel.add(mainPanel);
			
	navBar = new NavBar();
	mainPanel.add(navBar);
	
		
	SplashCarouselWidget splashCarouselWidget = new SplashCarouselWidget();
	mainPanel.add(splashCarouselWidget);
		
	HeroUnitLogin heroUnitLogin = new HeroUnitLogin();
	mainPanel.add(heroUnitLogin);
		
	
	// LoginDialogBox loginDialogBox = new LoginDialogBox();
	// mainPanel.add(loginDialogBox);
	}
}
