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

import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import com.github.gwtbootstrap.client.ui.Alert;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

public class LoginDialogBox extends PopupPanel{
	
	@UiField Alert aleError;
		
	interface MyBinder extends UiBinder<Widget, LoginDialogBox>{}
			
	private static MyBinder uiBinder = GWT.create(MyBinder.class);
	
	public LoginDialogBox() {
		aleError = new Alert();
		setStyleName("");
		add(uiBinder.createAndBindUi(this));
		aleError.setVisible(false);
		
	}
	
	@UiHandler("btnLogin")
	void submitLoginForm (ClickEvent event)  {
		// Window.alert("Logging In");
		Overviewer overviewer = new Overviewer();
		overviewer.open();
	
	}
	

}
