package com.velisphere.tigerspice.client;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.velisphere.tigerspice.client.appcontroller.NavBar;
import com.velisphere.tigerspice.client.users.NewAccountWidget;

public class LoggedOutHome {

	RootPanel rootPanel;
	RootPanel rootPanelHeader;
	RootPanel rootPanelBanderole;
	VerticalPanel mainPanel;
	NavBar navBar;
	
	
	public void open(){

	History.newItem("create_new_account");
	
	rootPanelHeader = RootPanel.get("stockList");
	rootPanelHeader.clear();
	rootPanelHeader.getElement().getStyle().setPosition(Position.RELATIVE);
	navBar = new NavBar();
	rootPanelHeader.add(navBar);
	
	
	rootPanel = RootPanel.get("main");
	rootPanel.clear();
	rootPanel.getElement().getStyle().setPosition(Position.RELATIVE);
	
	rootPanelBanderole = RootPanel.get("banderole");
	rootPanelBanderole.clear();
	rootPanelBanderole.getElement().getStyle().setPosition(Position.RELATIVE);
	
	
	
	
	mainPanel = new VerticalPanel();
	rootPanel.add(mainPanel);
	
	HeroUnitLogin heroUnitLogin = new HeroUnitLogin();
	rootPanel.add(heroUnitLogin);
	
		
	}
	
}
