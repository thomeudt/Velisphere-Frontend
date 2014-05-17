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
package com.velisphere.tigerspice.client.rules;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.github.gwtbootstrap.client.ui.Breadcrumbs;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.github.gwtbootstrap.client.ui.TabPane;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.users.LoginSuccess;

public class CheckpathList extends Composite {

	@UiField
	Breadcrumbs brdMain;
	@UiField
	ListBox lstCheckpath;
	@UiField
	TabPane tbpMyLogic;
	@UiField
	TabPane tbpTmplLogic;
	@UiField
	TabPane tbpEmbLogic;

	
	
	NavLink bread0;
	NavLink bread1;
	
	private CheckPathServiceAsync rpcServiceCheckPath;
	
	private static CheckpathListUiBinder uiBinder = GWT
			.create(CheckpathListUiBinder.class);

	interface CheckpathListUiBinder extends UiBinder<Widget, CheckpathList> {
	}

	public CheckpathList(String userID) {
		initWidget(uiBinder.createAndBindUi(this));
		rpcServiceCheckPath = GWT.create(CheckPathService.class);
		
		bread0 = new NavLink();
		bread0.setText("Home");
		brdMain.add(bread0);
		bread1 = new NavLink();
		bread1.setText("Logic Designer");
		brdMain.add(bread1);

		
		bread0.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				RootPanel.get("main").clear();
				LoginSuccess loginSuccess = new LoginSuccess();
				RootPanel.get("main").add(loginSuccess);

			}
		});
		
		// activate default tab
		
		
			tbpMyLogic.setActive(true);
			tbpTmplLogic.setActive(false);
			tbpEmbLogic.setActive(false);
		
		
		
		
		
		// load existing checkpaths
		
		

		rpcServiceCheckPath.getAllCheckpaths(userID,
				new AsyncCallback<LinkedHashMap<String, String>>() {

					@Override
					public void onFailure(
							Throwable caught) {
						// TODO Auto-generated method
						// stub
						System.out
								.println("ERROR SAVING JSON: "
										+ caught);

					}

					@Override
					public void onSuccess(LinkedHashMap<String, String> result) {
						// TODO Auto-generated method
						// stub
						
						Iterator<Entry<String, String>> it = result.entrySet().iterator();
						while (it.hasNext()){
							Map.Entry<String, String> checkPair = (Map.Entry<String, String>)it.next();
							lstCheckpath.addItem(checkPair.getValue(), checkPair.getKey());
						}
						lstCheckpath.setVisibleItemCount(7);

					}
				});
		
		
		
	}
	
	
	@UiHandler("btnCreateCheckpath")
	void createCheckpath(ClickEvent event) {
	
		/**
		
		RootPanel mainPanel = RootPanel.get("main");
		mainPanel.clear();
		
		CheckpathCreateView checkpathView = new CheckpathCreateView(); 		
		mainPanel.add(checkpathView);
		**/
		AppController.createLogicDesign();

		
	}

	@UiHandler("btnOpenCheckpath")
	void openCheckpath(ClickEvent event) {
	
		/**
		RootPanel mainPanel = RootPanel.get("main");
		mainPanel.clear();
		
		CheckpathEditView checkpathView = new CheckpathEditView(lstCheckpath.getValue()); 		
		mainPanel.add(checkpathView);
		**/
		
		AppController.openLogicDesign(lstCheckpath.getValue());
		
	}

}
