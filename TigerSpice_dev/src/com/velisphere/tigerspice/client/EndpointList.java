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

import com.google.gwt.core.shared.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.dataproviders.EndpointAsyncDataProvider;
import com.velisphere.tigerspice.shared.EndpointData;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;


// THIS WIDGET IS USING UIBINDER!!!

public class EndpointList extends Composite {
	
	interface EndpointListUiBinder extends
	UiBinder<Widget, EndpointList> {
}

private static EndpointListUiBinder uiBinder = GWT
	.create(EndpointListUiBinder.class);


	private final EndpointServiceAsync endpointService = GWT
			.create(EndpointService.class);

	final DialogBox dialogBox = new DialogBox();

	EndpointAsyncDataProvider dataProviderAsync;

	@UiField(provided = true)
	CellTable<EndpointData> cellTable;
	Column<EndpointData, String> columnId;
	Column<EndpointData, String> columnName;
	Column<EndpointData, String> columnClassId;
	
	
	private Column<EndpointData, String> buildColumnId() {
		columnId = new Column<EndpointData, String>(new TextCell()) {
			@Override
			public String getValue(EndpointData object) {
				return object.getId();
			}
		};
	
		columnId.setDataStoreName("endpointId");
		columnId.setFieldUpdater(new FieldUpdater<EndpointData, String>() {

			@Override
			public void update(int index, EndpointData object, String value) {
				// TODO Auto-generated method stub	
			}
			
		});
		return columnId;
	}

	private Column<EndpointData, String> buildColumnName() {
		columnName = new Column<EndpointData, String>(new TextCell()) {
			@Override
			public String getValue(EndpointData object) {
				return object.getName();
			}
		};
	
		columnName.setDataStoreName("endpointName");
		columnName.setFieldUpdater(new FieldUpdater<EndpointData, String>() {

			@Override
			public void update(int index, EndpointData object, String value) {
				// TODO Auto-generated method stub	
			}
			
		});
		return columnName;
	}

	private Column<EndpointData, String> buildColumnClassId() {
		columnClassId = new Column<EndpointData, String>(new TextCell()) {
			@Override
			public String getValue(EndpointData object) {
				return object.getEpcId();
			}
		};
	
		columnClassId.setDataStoreName("endpointClassId");
		columnClassId.setFieldUpdater(new FieldUpdater<EndpointData, String>() {

			@Override
			public void update(int index, EndpointData object, String value) {
				// TODO Auto-generated method stub	
			}
			
		});
		return columnClassId;
	}

	
	
	public EndpointList()  {
			
		final VerticalPanel verticalPanel = new VerticalPanel();
		//RootPanel.get().add(verticalPanel);
		// initWidget(verticalPanel);
		
			
		cellTable = new CellTable<EndpointData>();
		verticalPanel.add(cellTable);
				
		columnId = buildColumnId();
		columnName = buildColumnName();
		columnClassId = buildColumnClassId();
		
		cellTable.addColumn(columnId, "Veli ID");
		cellTable.addColumn(columnName, "Endpoint Name");
		cellTable.addColumn(columnClassId, "based on Endpoint Class ID");
	
		
		dataProviderAsync = new EndpointAsyncDataProvider();
		dataProviderAsync.addDataDisplay(cellTable);
		dataProviderAsync.updateRowCount(24, false);
		
		initWidget(uiBinder.createAndBindUi(this));
		
	}
	
}
