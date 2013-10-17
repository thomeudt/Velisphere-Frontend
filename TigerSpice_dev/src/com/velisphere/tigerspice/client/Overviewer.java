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

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.velisphere.tigerspice.client.HeroUnitLogin.HeroUnitLoginUiBinder;
import com.velisphere.tigerspice.client.endpointclasses.EPCList;
import com.velisphere.tigerspice.client.endpoints.EndpointList;
import com.velisphere.tigerspice.client.properties.PropertyList;
import com.velisphere.tigerspice.client.users.UserList;


public class Overviewer {

	RootPanel rootPanel;
	VerticalPanel mainPanel;
	NavBar navBar;
	
	public void open(){

	History.newItem("epce");
	rootPanel = RootPanel.get("main");
	rootPanel.clear();
	rootPanel.getElement().getStyle().setPosition(Position.RELATIVE);
	Integer defaultWidth = rootPanel.getOffsetWidth()-60;
	
	mainPanel = new VerticalPanel();
	rootPanel.add(mainPanel);


	UserList userClassList = new UserList();
	mainPanel.add(userClassList);

	EPCList epcList = new EPCList();
	mainPanel.add(epcList);
	

	EndpointList endpointList = new EndpointList();
	mainPanel.add(endpointList);
	
	PropertyList propertyList = new PropertyList();
	mainPanel.add(propertyList);

			
	}

}





