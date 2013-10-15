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
import com.velisphere.tigerspice.client.newaccount.NewAccountScreen;

public class NavBar extends Composite implements HasText {

	private static NavBarUiBinder uiBinder = GWT.create(NavBarUiBinder.class);

	interface NavBarUiBinder extends UiBinder<Widget, NavBar> {
	}

	public NavBar() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	
	public NavBar(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	
	}

	@UiHandler("btnAccount")
	void openNewAccountScreen (ClickEvent event) {
		// Window.alert("Logging In");
		NewAccountScreen newAccountScreen = new NewAccountScreen();
		newAccountScreen.open();
		
		// loginDialogBox.setVisible(false);
		
		
	}
	
	@UiHandler("btnHome")
	void openHome (ClickEvent event) {
		// Window.alert("Logging In");
		Login loginScreen = new Login();
		
		loginScreen.onModuleLoad();
		
		// loginDialogBox.setVisible(false);
		
		
	}
	
	public void setText(String text) {
	
	}

	public String getText() {
		return null;
	
	}

}
