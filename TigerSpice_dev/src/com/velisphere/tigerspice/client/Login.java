package com.velisphere.tigerspice.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class Login implements EntryPoint {

	
	
	public void onModuleLoad() {

	RootPanel rootPanel = RootPanel.get("stockList");
	rootPanel.getElement().getStyle().setPosition(Position.RELATIVE);
	
	NavBar navBar = new NavBar();
	rootPanel.add(navBar, 10, 0);

	SplashCarousel splashCarousel = new SplashCarousel("test");
	// Document.get().getBody().appendChild(splashCarousel.getElement());
		
	HeroUnitLogin heroUnitLogin = new HeroUnitLogin();
	rootPanel.add(heroUnitLogin, 10, 70);
	
	LoginDialogBox loginDialogBox = new LoginDialogBox();
	rootPanel.add(loginDialogBox, 10, 490);
	
	}
}
