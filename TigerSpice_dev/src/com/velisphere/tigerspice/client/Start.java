package com.velisphere.tigerspice.client;

import com.github.gwtbootstrap.client.ui.Paragraph;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.ui.RootPanel;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.helper.EventUtils;
import com.velisphere.tigerspice.client.helper.SessionHelper;
import com.velisphere.tigerspice.client.helper.SessionVerifiedEvent;
import com.velisphere.tigerspice.client.helper.SessionVerifiedEventHandler;
import com.velisphere.tigerspice.client.users.LoginSuccess;

public class Start implements EntryPoint{

	public void onModuleLoad() {
    
		
		AnimationLoading loading = new AnimationLoading();
		loading.showLoadAnimation("Loading App");
		   	
		
		SessionHelper.getCurrentUserID();
		
	       
		EventUtils.EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
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
	        	
	        	rootPanelMain.add(new LoginSuccess());
				}
	    }); 
    
   }
}
