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
package com.velisphere.tigerspice.client.endpointclasses;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.velisphere.tigerspice.client.dataproviders.EPCAsyncDataProvider;
import com.velisphere.tigerspice.shared.EPCData;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;

public class EPCList extends Composite {

	private final EPCServiceAsync epcService = GWT
			.create(EPCService.class);

	final DialogBox dialogBox = new DialogBox();

	EPCAsyncDataProvider dataProviderAsync;
	CellTable<EPCData> cellTable;
	Column<EPCData, String> columnId;
	Column<EPCData, String> columnName;
	
	
	private Column<EPCData, String> buildColumnId() {
		columnId = new Column<EPCData, String>(new TextCell()) {
			@Override
			public String getValue(EPCData object) {
				return object.getId();
			}
		};
	
		columnId.setDataStoreName("epcId");
		columnId.setFieldUpdater(new FieldUpdater<EPCData, String>() {

			@Override
			public void update(int index, EPCData object, String value) {
				// TODO Auto-generated method stub	
			}
			
		});
		return columnId;
	}

	private Column<EPCData, String> buildColumnName() {
		columnName = new Column<EPCData, String>(new TextCell()) {
			@Override
			public String getValue(EPCData object) {
				return object.getName();
			}
		};
	
		columnName.setDataStoreName("epcName");
		columnName.setFieldUpdater(new FieldUpdater<EPCData, String>() {

			@Override
			public void update(int index, EPCData object, String value) {
				// TODO Auto-generated method stub	
			}
			
		});
		return columnName;
	}

	
	
	public EPCList()  {
			
		final VerticalPanel verticalPanel = new VerticalPanel();
		//RootPanel.get().add(verticalPanel);
		initWidget(verticalPanel);
		
		
		final Tree tree = new Tree();
		verticalPanel.add(tree);
		
		cellTable = new CellTable<EPCData>();
		verticalPanel.add(cellTable);
		
		columnId = buildColumnId();
		columnName = buildColumnName();
		
		cellTable.addColumn(columnId, "Veli ID");
		cellTable.addColumn(columnName, "Endpoint Class Name");
	
		
		dataProviderAsync = new EPCAsyncDataProvider();
		dataProviderAsync.addDataDisplay(cellTable);
		dataProviderAsync.updateRowCount(24, false);
		
	}
	
}
