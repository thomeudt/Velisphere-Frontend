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

import com.github.gwtbootstrap.client.ui.Paragraph;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.helper.EventUtils;
import com.velisphere.tigerspice.client.helper.HelperService;
import com.velisphere.tigerspice.client.helper.HelperServiceAsync;
import com.velisphere.tigerspice.client.helper.SessionHelper;
import com.velisphere.tigerspice.client.helper.SessionVerifiedEvent;
import com.velisphere.tigerspice.client.helper.SessionVerifiedEventHandler;
import com.velisphere.tigerspice.client.users.LoginSuccess;
import com.velisphere.tigerspice.client.users.UserService;
import com.velisphere.tigerspice.client.users.UserServiceAsync;

public class Start implements EntryPoint{
	
	private HandlerRegistration reg;

	public void onModuleLoad() {
    
		
		
		AnimationLoading loading = new AnimationLoading();
		loading.showLoadAnimation("Loading App");
		

		final HelperServiceAsync helperService = GWT
				.create(HelperService.class);
		
		helperService.autoConfMontana(new AsyncCallback<String>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				System.out.println("Failed to create Monata Stats Context. Error: " + caught);
				
			}

			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				System.out.println("Successfully created Monata Stats Context " + result);
				
			}
			
		});
		   	
		
		SessionHelper.validateCurrentSession();
		
	       
		reg = EventUtils.EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
			
			@Override
	        public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
	        	RootPanel.get().clear();
	            RootPanel rootPanelHeader = RootPanel.get("stockList");
	        	rootPanelHeader.clear();
	        	rootPanelHeader.getElement().getStyle().setPosition(Position.RELATIVE);
	        	NavBar navBar = new NavBar();
	        	rootPanelHeader.add(navBar);
	              	
	        	RootPanel rootPanelMain = RootPanel.get("main");
	        	rootPanelMain.clear();
	        	rootPanelMain.getElement().getStyle().setPosition(Position.RELATIVE);
	        	
	        	//NavBar navBar = new NavBar();
	    		//navBar = (NavBar) RootPanel.get("stockList").getWidget(0);
	    		navBar.activateForCurrentUser();
	    		
	        	
	        	rootPanelMain.add(new LoginSuccess(reg));
	        	
	        	
				}
	    }); 
		
		
   }
}
