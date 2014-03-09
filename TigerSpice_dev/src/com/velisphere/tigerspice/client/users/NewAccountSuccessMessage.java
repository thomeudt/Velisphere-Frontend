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
package com.velisphere.tigerspice.client.users;



import com.github.gwtbootstrap.client.ui.Container;
import com.github.gwtbootstrap.client.ui.Image;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.LoginDialogBox;
import com.velisphere.tigerspice.client.helper.Banderole;


public class NewAccountSuccessMessage extends Composite {

interface MyBinder extends UiBinder<Widget, NewAccountSuccessMessage>{}
	
	private static MyBinder uiBinder = GWT.create(MyBinder.class);
	
	public NewAccountSuccessMessage() {
		
		initWidget(uiBinder.createAndBindUi(this));	
		
		
		/**
		Banderole banderole = new Banderole();
		banderole.setTitle("Account Successfully Created!");
		
		RootPanel banderoleRootPanel = RootPanel.get("banderole");
		
		VerticalPanel banderolePanel = new VerticalPanel();
		banderoleRootPanel.add(banderolePanel);
		
		banderolePanel.clear();
		banderolePanel.add(banderole);
		**/
	}	

	@UiHandler("btnLogin")
	void openLoginForm (ClickEvent event) {
		// Window.alert("Logging In");
		RootPanel rootPanel = RootPanel.get("main");
		rootPanel.getElement().getStyle().setPosition(Position.RELATIVE);
		LoginDialogBox loginDialogBox = new LoginDialogBox();
		rootPanel.add(loginDialogBox, 250, 250);
		
		
		
		// loginDialogBox.setVisible(false);
		
		
	}
	
	
	
}
