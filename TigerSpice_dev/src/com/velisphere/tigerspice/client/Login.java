package com.velisphere.tigerspice.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.velisphere.tigerspice.client.HeroUnitLogin.HeroUnitLoginUiBinder;

public class Login implements EntryPoint {

	
	
	public void onModuleLoad() {

	RootPanel rootPanel = RootPanel.get("stockList");
	rootPanel.getElement().getStyle().setPosition(Position.RELATIVE);
	
	VerticalPanel mainPanel = new VerticalPanel();
	rootPanel.add(mainPanel);
			
	NavBar navBar = new NavBar();
	mainPanel.add(navBar);
	
		
	SplashCarouselWidget splashCarouselWidget = new SplashCarouselWidget();
	mainPanel.add(splashCarouselWidget);
		
	HeroUnitLogin heroUnitLogin = new HeroUnitLogin();
	mainPanel.add(heroUnitLogin);
	
	
	
	// LoginDialogBox loginDialogBox = new LoginDialogBox();
	// mainPanel.add(loginDialogBox);
	
	
	
	}
}
