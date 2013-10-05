package com.velisphere.tigerspice.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class Login implements EntryPoint {

	
	
	public void onModuleLoad() {
	RootPanel rootPanel = RootPanel.get("stockList");

	rootPanel.getElement().getStyle().setPosition(Position.RELATIVE);

	LoginDialogBox loginDialogBox = new LoginDialogBox();
	rootPanel.add(loginDialogBox, 0, 0);
	}
}
