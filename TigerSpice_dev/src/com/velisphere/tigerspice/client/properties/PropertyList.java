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
package com.velisphere.tigerspice.client.properties;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.dataproviders.EndpointAsyncDataProvider;
import com.velisphere.tigerspice.client.dataproviders.PropertyAsyncDataProvider;
import com.velisphere.tigerspice.shared.PropertyData;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;


// THIS WIDGET IS USING UIBINDER!!!

public class PropertyList extends Composite {
	
	interface PropertyListUiBinder extends
	UiBinder<Widget, PropertyList> {
}

private static PropertyListUiBinder uiBinder = GWT
	.create(PropertyListUiBinder.class);


	private final PropertyServiceAsync propertyService = GWT
			.create(PropertyService.class);

	final DialogBox dialogBox = new DialogBox();

	PropertyAsyncDataProvider dataProviderAsync;

	@UiField(provided = true)
	CellTable<PropertyData> cellTable;
	Column<PropertyData, String> columnId;
	Column<PropertyData, String> columnName;
	Column<PropertyData, String> columnPropertyClassId;
	Column<PropertyData, String> columnEndpointClassId;
	
	
	private Column<PropertyData, String> buildColumnId() {
		columnId = new Column<PropertyData, String>(new TextCell()) {
			@Override
			public String getValue(PropertyData object) {
				return object.getId();
			}
		};
	
		columnId.setDataStoreName("propertyId");
		columnId.setFieldUpdater(new FieldUpdater<PropertyData, String>() {

			@Override
			public void update(int index, PropertyData object, String value) {
				// TODO Auto-generated method stub	
			}
			
		});
		return columnId;
	}

	private Column<PropertyData, String> buildColumnName() {
		columnName = new Column<PropertyData, String>(new TextCell()) {
			@Override
			public String getValue(PropertyData object) {
				return object.getName();
			}
		};
	
		columnName.setDataStoreName("propertyName");
		columnName.setFieldUpdater(new FieldUpdater<PropertyData, String>() {

			@Override
			public void update(int index, PropertyData object, String value) {
				// TODO Auto-generated method stub	
			}
			
		});
		return columnName;
	}

	private Column<PropertyData, String> buildColumnPropertyClassId() {
		columnPropertyClassId = new Column<PropertyData, String>(new TextCell()) {
			@Override
			public String getValue(PropertyData object) {
				return object.getPropertyclassId();
			}
		};
	
		columnPropertyClassId.setDataStoreName("propertyClassId");
		columnPropertyClassId.setFieldUpdater(new FieldUpdater<PropertyData, String>() {

			@Override
			public void update(int index, PropertyData object, String value) {
				// TODO Auto-generated method stub	
			}
			
		});
		return columnPropertyClassId;
	}

	private Column<PropertyData, String> buildColumnEndpointClassId() {
		columnEndpointClassId = new Column<PropertyData, String>(new TextCell()) {
			@Override
			public String getValue(PropertyData object) {
				return object.getEpcId();
			}
		};
	
		columnEndpointClassId.setDataStoreName("endpointClassId");
		columnEndpointClassId.setFieldUpdater(new FieldUpdater<PropertyData, String>() {

			@Override
			public void update(int index, PropertyData object, String value) {
				// TODO Auto-generated method stub	
			}
			
		});
		return columnEndpointClassId;
	}

	
	
	public PropertyList()  {
			
		final VerticalPanel verticalPanel = new VerticalPanel();
		//RootPanel.get().add(verticalPanel);
		// initWidget(verticalPanel);
		
			
		cellTable = new CellTable<PropertyData>();
		verticalPanel.add(cellTable);
				
		columnId = buildColumnId();
		columnName = buildColumnName();
		columnEndpointClassId = buildColumnEndpointClassId();
		columnPropertyClassId = buildColumnPropertyClassId();
		
		cellTable.addColumn(columnId, "Veli ID");
		cellTable.addColumn(columnName, "Endpoint Name");
		cellTable.addColumn(columnEndpointClassId, "valid for Endpoint Class ID");
		cellTable.addColumn(columnPropertyClassId, "based on Property Class ID");
	
		
		dataProviderAsync = new PropertyAsyncDataProvider();
		dataProviderAsync.addDataDisplay(cellTable);
		dataProviderAsync.updateRowCount(24, false);
		
		initWidget(uiBinder.createAndBindUi(this));
		
	}
	
}
