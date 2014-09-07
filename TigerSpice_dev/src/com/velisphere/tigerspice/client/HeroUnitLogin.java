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

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.users.NewAccountWidget;

public class HeroUnitLogin extends Composite implements HasText {

	private static HeroUnitLoginUiBinder uiBinder = GWT
			.create(HeroUnitLoginUiBinder.class);

	interface HeroUnitLoginUiBinder extends UiBinder<Widget, HeroUnitLogin> {
	}

	public HeroUnitLogin() {
		initWidget(uiBinder.createAndBindUi(this));
					
	}
	
	@UiHandler("btnLogin")
	void openLoginForm (ClickEvent event) {
		// Window.alert("Logging In");
		RootPanel rootPanel = RootPanel.get("main");
		rootPanel.getElement().getStyle().setPosition(Position.RELATIVE);
		LoginDialogBox loginDialogBox = new LoginDialogBox();
		loginDialogBox.setAnimationEnabled(false);
		loginDialogBox.setAutoHideEnabled(false);
		loginDialogBox.setAutoHideOnHistoryEventsEnabled(true);
		loginDialogBox.show();
		loginDialogBox.center();
		loginDialogBox.addStyleName("ontop");
		
		// loginDialogBox.setVisible(false);
		
		
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setText(String text) {
		// TODO Auto-generated method stub
		
	}
	
	@UiHandler("ancSignup")
	void redirectSignup (ClickEvent event)  {
		// Window.alert("Logging In");
		RootPanel.get("main").clear();
		NewAccountWidget newAccount = new NewAccountWidget();
		RootPanel.get("main").add(newAccount);
	
	}
	
}
