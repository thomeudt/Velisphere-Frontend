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

import java.io.IOException;
import java.util.Date;

import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.github.gwtbootstrap.client.ui.PasswordTextBox;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.admin.Overviewer;
import com.velisphere.tigerspice.client.users.LoginService;
import com.velisphere.tigerspice.client.users.LoginSuccess;
import com.velisphere.tigerspice.client.users.NewAccountDialogbox;
import com.velisphere.tigerspice.shared.UserData;

public class LoginDialogBox extends PopupPanel{
	
	@UiField Alert aleError;
	@UiField Anchor ancSignup;
	@UiField TextBox txtUsername;
	@UiField PasswordTextBox txtPassword; 
		
	interface MyBinder extends UiBinder<Widget, LoginDialogBox>{}
			
	private static MyBinder uiBinder = GWT.create(MyBinder.class);
	
	public LoginDialogBox() {
		aleError = new Alert();
		ancSignup = new Anchor();
		txtUsername = new TextBox();
		txtPassword = new PasswordTextBox();
		
		setStyleName("");
		add(uiBinder.createAndBindUi(this));
		aleError.setVisible(false);
	
		
	}
	
	@UiHandler("btnLogin")
	void submitLoginForm (ClickEvent event)  {
		// Window.alert("Logging In");
	
		LoginService.Util.getInstance().loginServer(txtUsername.getValue(), txtPassword.getValue(), new AsyncCallback<UserData>()
                {
                    @Override
                    public void onSuccess(UserData result)
                    {
                        if (result.getLoggedIn())
                        {
                          
                        	
                        
                        	RootPanel.get().clear();
                            // load the next app page
 
 
                            //set session cookie for 1 day expiry.
                            String sessionID = result.getSessionId();
                            final long DURATION = 1000 * 60 * 60 * 24 * 1;
                            Date expires = new Date(System.currentTimeMillis() + DURATION);
                            Cookies.setCookie("sid", sessionID, expires, null, "/", false);
                            // String rSessionID = Cookies.getCookie("sid");
                            // System.out.println("RSession ID: " +rSessionID);
                        
                            /*
                            
                            RootPanel rootPanelHeader = RootPanel.get("stockList");
                        	rootPanelHeader.clear();
                        	rootPanelHeader.getElement().getStyle().setPosition(Position.RELATIVE);
                        	NavBar navBar = new NavBar();
                        	rootPanelHeader.add(navBar);
                            
                            
                        	RootPanel rootPanelMain = RootPanel.get("main");
                        	rootPanelMain.clear();
                        	rootPanelMain.getElement().getStyle().setPosition(Position.RELATIVE);
                        	LoginSuccess loginSuccess = new LoginSuccess();
                        	rootPanelMain.add(loginSuccess);
                            */
                        	
                            Start start = new Start();
                            start.onModuleLoad();
                            
                    			
                        } else
                        {
                           aleError.setText("Access Denied. Check your user-name and password.");
                           aleError.setVisible(true);
                        }
 
                    }
 
                    @Override
                    public void onFailure(Throwable caught)
                    {
                    	  aleError.setText("Access Denied. Check your user-name and password.");
                          aleError.setVisible(true);
                    }

			
                });
		
	
	}
	
	@UiHandler("ancSignup")
	void redirectSignup (ClickEvent event)  {
		// Window.alert("Logging In");
		RootPanel.get("main").clear();
		NewAccountDialogbox newAccount = new NewAccountDialogbox();
		RootPanel.get("main").add(newAccount);
		this.hide();
	
	}

}
