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
package com.velisphere.tigerspice.client.admin;

import java.io.IOException;

import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.velisphere.tigerspice.client.Login;
import com.velisphere.tigerspice.client.LoginDialogBox;
import com.velisphere.tigerspice.client.NavBar;
import com.velisphere.tigerspice.client.endpointclasses.EPCList;
import com.velisphere.tigerspice.client.endpoints.EndpointList;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.helper.Banderole;
import com.velisphere.tigerspice.client.properties.PropertyList;
import com.velisphere.tigerspice.client.users.LoginService;
import com.velisphere.tigerspice.client.users.UserList;
import com.velisphere.tigerspice.shared.UserData;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class Overviewer {

	RootPanel rootPanel;
	RootPanel rootPanelHeader;
	VerticalPanel mainPanel;
	NavBar navBar;
	AnimationLoading animationLoading;

	

	public void loadContent() {

		History.newItem("epce");
		rootPanel = RootPanel.get("main");
		rootPanel.clear();
		rootPanel.getElement().getStyle().setPosition(Position.RELATIVE);

	
		mainPanel = new VerticalPanel();
		rootPanel.add(mainPanel);

		RootPanel banderolePanel = RootPanel.get("banderole");
		banderolePanel.clear();
		Banderole banderole = new Banderole();
		banderolePanel.add(banderole);

		UserList userClassList = new UserList();
		mainPanel.add(userClassList);

		EPCList epcList = new EPCList();
		mainPanel.add(epcList);

		EndpointList endpointList = new EndpointList();
		mainPanel.add(endpointList);

		PropertyList propertyList = new PropertyList();
		mainPanel.add(propertyList);
		
				
		removeLoadAnimation();

	}

	private void checkWithServerIfSessionIdIsStillLegal(String sessionID) {

		showLoadAnimation();
		LoginService.Util.getInstance().loginFromSessionServer(
				new AsyncCallback<UserData>() {
					@Override
					public void onFailure(Throwable caught) {
						Login loginScreen = new Login();
						loginScreen.onModuleLoad();
					}

					@Override
					public void onSuccess(UserData result) {
						if (result == null) {
							Login loginScreen = new Login();
							loginScreen.onModuleLoad();
						} else {
							if (result.getLoggedIn()) {
								loadContent();
							} else {
								Login loginScreen = new Login();
								loginScreen.onModuleLoad();
							}
						}
					}

				});
	}

	private void showLoadAnimation() {
		RootPanel rootPanel = RootPanel.get("main");
		rootPanel.getElement().getStyle().setPosition(Position.RELATIVE);
		animationLoading = new AnimationLoading();
		rootPanel.add(animationLoading, 25, 40);
	}
	
	private void removeLoadAnimation() {
		if (animationLoading != null) animationLoading.removeFromParent();
	}
	


	
}
