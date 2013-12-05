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
package com.velisphere.tigerspice.client.users;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

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
import com.velisphere.tigerspice.client.dataproviders.UserAsyncDataProvider;

import com.velisphere.tigerspice.shared.UserData;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.view.client.HasData;

public class UserList extends Composite {

	private final UserServiceAsync userService = GWT
			.create(UserService.class);

	final DialogBox dialogBox = new DialogBox();

	UserAsyncDataProvider dataProviderAsync;
	CellTable<UserData> cellTable;
	Column<UserData, String> columnEmail;
	Column<UserData, String> columnId;
	Column<UserData, String> columnName;
	
	private Column<UserData, String> buildColumnEmail() {
		columnEmail = new Column<UserData, String>(new TextCell()) {
			@Override
			public String getValue(UserData object) {
				return object.getEmail();
			}
		};
	
		columnEmail.setDataStoreName("userEmail");
		columnEmail.setFieldUpdater(new FieldUpdater<UserData, String>() {

			@Override
			public void update(int index, UserData object, String value) {
				// TODO Auto-generated method stub	
			}
			
		});
		return columnEmail;
	}

	private Column<UserData, String> buildColumnId() {
		columnId = new Column<UserData, String>(new TextCell()) {
			@Override
			public String getValue(UserData object) {
				return object.getId();
			}
		};
	
		columnId.setDataStoreName("userId");
		columnId.setFieldUpdater(new FieldUpdater<UserData, String>() {

			@Override
			public void update(int index, UserData object, String value) {
				// TODO Auto-generated method stub	
			}
			
		});
		return columnId;
	}

	private Column<UserData, String> buildColumnName() {
		columnName = new Column<UserData, String>(new TextCell()) {
			@Override
			public String getValue(UserData object) {
				return object.getName();
			}
		};
	
		columnName.setDataStoreName("userName");
		columnName.setFieldUpdater(new FieldUpdater<UserData, String>() {

			@Override
			public void update(int index, UserData object, String value) {
				// TODO Auto-generated method stub	
			}
			
		});
		return columnName;
	}

	
	
	public UserList()  {
			
		final VerticalPanel verticalPanel = new VerticalPanel();
		//RootPanel.get().add(verticalPanel);
		initWidget(verticalPanel);
		
		
		final Tree tree = new Tree();
		verticalPanel.add(tree);
		
		cellTable = new CellTable<UserData>();
		verticalPanel.add(cellTable);
		
/*
		TextColumn<Object> useridColumn = new TextColumn<Object>() {
			@Override
			public String getValue(Object object) {
				return object.toString();
			}
		};
		cellTable.addColumn(useridColumn, "User ID");
		
		TextColumn<Object> emailColumn = new TextColumn<Object>() {
			@Override
			public String getValue(Object object) {
				return object.toString();
			}
		};
		cellTable.addColumn(emailColumn, "E-Mail");
		
		TextColumn<Object> nameColumn = new TextColumn<Object>() {
			@Override
			public String getValue(Object object) {
				return object.toString();
			}
		};
		cellTable.addColumn(nameColumn, "Name");
	*/	
		



		columnEmail = buildColumnEmail();
		columnId = buildColumnId();
		columnName = buildColumnName();
		
		cellTable.addColumn(columnEmail, "E-Mail");
		cellTable.addColumn(columnId, "Veli ID");
		cellTable.addColumn(columnName, "User Name");
	
		
		dataProviderAsync = new UserAsyncDataProvider();
		dataProviderAsync.addDataDisplay(cellTable);
		dataProviderAsync.updateRowCount(24, false);
		
		
		
	
	
//		addColumnSortAsyncHandling();
		
/*				
		userService.getAllUserDetails(
				new AsyncCallback<Vector<UserData>>() {
					public void onFailure(Throwable caught) {
						// Show the RPC error message to the user
						dialogBox
								.setText("Remote Procedure Call - Failure");
						
						dialogBox.center();
						
					}
				
					List<List> rowList = new ArrayList<List>();
					public void onSuccess(Vector<UserData> result) {
						// TODO Auto-generated method stub
						System.out.println(result);
						
						cellTable.setRowCount(result.size());
															
						int i = 0;
						for (UserData u: result){
							
							TreeItem treeItem = new TreeItem();
							treeItem.setText(u.userEmail);
							tree.addItem(treeItem);
							treeItem.addTextItem(u.userID);
							treeItem.addTextItem(u.userName);
							
							List<String> colList = new ArrayList<String>();
							colList.add(u.userEmail);
							colList.add(u.userID);
							colList.add(u.userName);
							rowList.add(colList);
													
						}
						
						cellTable.setRowData(rowList);
					}
				});
		
	
	*/	 
		
		
		
			
		
		
		
		
	}
		
		
		
					
	
	
	
	
}
