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
import java.util.HashSet;
import java.util.List;

import org.voltdb.VoltTable;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.i18n.server.testing.Parent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.velisphere.tigerspice.server.VoltConnector;
import com.velisphere.tigerspice.shared.UserData;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.cellview.client.CellTable;

public class UserClassList extends Composite {

	private final UserServiceAsync userService = GWT
			.create(UserService.class);

	final DialogBox dialogBox = new DialogBox();

	
	public UserClassList()  {
			
		final VerticalPanel verticalPanel = new VerticalPanel();
		//RootPanel.get().add(verticalPanel);
		initWidget(verticalPanel);
		
		final ListBox listBox = new ListBox();
		verticalPanel.add(listBox);
		listBox.setVisibleItemCount(5);
		
		userService.getAllUserDetails(
				new AsyncCallback<HashSet<UserData>>() {
					public void onFailure(Throwable caught) {
						// Show the RPC error message to the user
						dialogBox
								.setText("Remote Procedure Call - Failure");
						
						dialogBox.center();
						
					}

					
					
					public void onSuccess(HashSet<UserData> result) {
						// TODO Auto-generated method stub
						System.out.println(result);
						
						for (UserData u: result){
							
							listBox.addItem(u.userEmail);
							listBox.addItem(u.userID);
							listBox.addItem(u.userName);
							
						}
						
						
					}
				});
	}
		
		
		
					
	
	
	
	
}
