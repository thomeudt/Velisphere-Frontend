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
package com.velisphere.tigerspice.client.endpointclasses;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.github.gwtbootstrap.client.ui.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.github.gwtbootstrap.client.ui.SimplePager.Resources;
import com.github.gwtbootstrap.client.ui.SimplePager.TextLocation;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.velisphere.tigerspice.client.admin.EditEPCWidget;
import com.velisphere.tigerspice.client.rules.MulticheckDialogBox;
import com.velisphere.tigerspice.shared.EPCData;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.view.client.HasRows;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.cell.client.ImageCell;
import com.google.gwt.cell.client.TextCell;

public class EPCList extends Composite {

	final DialogBox dialogBox = new DialogBox();
	CellTable<EpcItem> cellTable;
	Column<EPCData, String> columnId;
	Column<EPCData, String> columnName;
	Column<EPCData, String> columnImage;

	
	
	class EpcItem {

		private String id;
		private String name;
		private String imageURL;

	}

	public EPCList() {

		EPCServiceAsync rpcService;

		rpcService = GWT.create(EPCService.class);

		cellTable = new CellTable<EpcItem>(5);

	
		// Create a data provider for celltable.
		ListDataProvider<EpcItem> dataProvider = new ListDataProvider<EpcItem>();

		// Connect the table to the data provider.
		dataProvider.addDataDisplay(cellTable);
			
		final List<EpcItem> list = dataProvider.getList();
		
		// create columns
		
		createColumns(cellTable, list);
	
		
		// fill
		
		rpcService
				.getAllEndpointClassDetails(new AsyncCallback<LinkedList<EPCData>>() {

					public void onFailure(Throwable caught) {
						Window.alert("Error" + caught.getMessage());
					}

					@Override
					public void onSuccess(LinkedList<EPCData> result) {

						Iterator<EPCData> it = result.iterator();

						cellTable.setRowCount(result.size(), false);
						while (it.hasNext()) {

							final EPCData sourceItem = it.next();
							EpcItem targetItem = new EpcItem();
							targetItem.id = sourceItem.getId();
							targetItem.name = sourceItem.getName();
							targetItem.imageURL = sourceItem.getImageURL();
							list.add(targetItem);
						}
						
						
					
					}
				});

		
		// add selection model

		final SingleSelectionModel<EpcItem> selectionModel = new SingleSelectionModel<EpcItem>();
		cellTable.setSelectionModel(selectionModel);
		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						EpcItem selected = selectionModel
								.getSelectedObject();

						if (selected != null) {
							
							
							//Window.alert("You selected: " + selected.name);
							
							final EditEPCWidget editEPCWidget = new EditEPCWidget(selected.id, selected.name, selected.imageURL);
					
							editEPCWidget.setAutoHideEnabled(true);

							//multicheckDialogBox.setAnimationEnabled(true);

							editEPCWidget.show();
							editEPCWidget.center();


							

						}
					}
				});

		
		// display

		final VerticalPanel verticalPanel = new VerticalPanel();
		initWidget(verticalPanel);

		cellTable.setHover(true);
		
		
		verticalPanel.add(cellTable);

		SimplePager pager = new SimplePager();
		pager.setDisplay(cellTable);
		pager.setRangeLimited(false);
		
		verticalPanel.add(pager);


	}
	
	

	void createColumns(CellTable<EpcItem> celltable, List<EpcItem> list){
	
		// add SortHandler
		
		ListHandler<EpcItem> columnSortHandler = new ListHandler<EpcItem>(
				list);
				
		
		
		// ID Column
		com.google.gwt.user.cellview.client.Column<EpcItem, String> idColumn = new com.google.gwt.user.cellview.client.Column<EpcItem, String>(
				new TextCell()) {

			@Override
			public String getValue(EpcItem object) {
				// TODO Auto-generated method stub
				return object.id;
			}
		};
		idColumn.setSortable(true);
		cellTable.addColumn(idColumn, "Velisphere ID");

		// Name Column
		com.google.gwt.user.cellview.client.Column<EpcItem, String> nameColumn = new com.google.gwt.user.cellview.client.Column<EpcItem, String>(
				new TextCell()) {

			@Override
			public String getValue(EpcItem object) {
				// TODO Auto-generated method stub
				return object.name;
			}
		};
		nameColumn.setSortable(true);
		cellTable.addColumn(nameColumn, "Endpoint Class Name");

		// Image Column
		
		ImageCell imageCell = new ImageCell() {
			@Override
			public void render(com.google.gwt.cell.client.Cell.Context context,
					String value, SafeHtmlBuilder sb) {
				super.render(context, value, sb);
				EpcItem e = (EpcItem) context.getKey();

				sb.appendHtmlConstant("<img src = '" + e.imageURL
						+ "' height = '75px' width = '75px' />");

			}
		};
				
		com.google.gwt.user.cellview.client.Column<EpcItem, String> imageColumn = new com.google.gwt.user.cellview.client.Column<EpcItem, String>(
				imageCell) {

			@Override
			public String getValue(EpcItem object) {
				// TODO Auto-generated method stub
				return "";
			}
		};
		cellTable.addColumn(imageColumn, "Visual");
		
		// Sort by id
		columnSortHandler.setComparator(idColumn,
				new Comparator<EpcItem>() {
					public int compare(EpcItem o1, EpcItem o2) {
						if (o1 == o2) {
							return 0;
						}

						// Compare the timestamp columns.
						if (o1 != null) {
							return (o2 != null) ? o1.id
									.compareTo(o2.id) : 1;
						}
						return -1;
					}
				});
		
		// Sort by name
				columnSortHandler.setComparator(nameColumn,
						new Comparator<EpcItem>() {
							public int compare(EpcItem o1, EpcItem o2) {
								if (o1 == o2) {
									return 0;
								}

								// Compare the timestamp columns.
								if (o1 != null) {
									return (o2 != null) ? o1.name
											.compareTo(o2.name) : 1;
								}
								return -1;
							}
						});


	cellTable.addColumnSortHandler(columnSortHandler);

	}
	
	
}
