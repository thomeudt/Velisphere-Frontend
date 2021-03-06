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

import java.io.IOException;

import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.velisphere.tigerspice.client.appcontroller.NavBar;




public class NewAccountScreen {

	RootPanel rootPanel;
	RootPanel rootPanelHeader;
	RootPanel rootPanelBanderole;
	VerticalPanel mainPanel;
	NavBar navBar;
	NewAccountWidget newAccount;
	
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
	
	newAccount = new NewAccountWidget();
	rootPanel.add(newAccount);
	
		
	}

}





