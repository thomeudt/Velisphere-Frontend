package com.velisphere.tigerspice.client.propertyclasses;

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
import com.velisphere.tigerspice.client.admin.EditEPCInputWidget;
import com.velisphere.tigerspice.client.admin.EditPropertyClassInputWidget;
import com.velisphere.tigerspice.client.rules.MulticheckDialogBox;
import com.velisphere.tigerspice.shared.PropertyClassData;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.view.client.HasRows;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.cell.client.ImageCell;
import com.google.gwt.cell.client.TextCell;

public class PropertyClassList extends Composite {

	final DialogBox dialogBox = new DialogBox();
	CellTable<PcItem> cellTable;
	
	class PcItem {

		private String id;
		private String name;
		private String datatype;
		private String unit;

	}

	public PropertyClassList() {

		PropertyClassServiceAsync rpcService;

		rpcService = GWT.create(PropertyClassService.class);

		cellTable = new CellTable<PcItem>(5);

		// Create a data provider for celltable.
		ListDataProvider<PcItem> dataProvider = new ListDataProvider<PcItem>();

		// Connect the table to the data provider.
		dataProvider.addDataDisplay(cellTable);

		final List<PcItem> list = dataProvider.getList();

		// create columns

		createColumns(cellTable, list);

		// fill

		rpcService
				.getAllPropertyClassDetails(new AsyncCallback<LinkedList<PropertyClassData>>() {

					public void onFailure(Throwable caught) {
						Window.alert("Error" + caught.getMessage());
					}

					@Override
					public void onSuccess(LinkedList<PropertyClassData> result) {

						Iterator<PropertyClassData> it = result.iterator();

						cellTable.setRowCount(result.size(), false);
						while (it.hasNext()) {

							final PropertyClassData sourceItem = it.next();
							PcItem targetItem = new PcItem();
							targetItem.id = sourceItem.getId();
							targetItem.name = sourceItem.getName();
							targetItem.datatype = sourceItem.getDatatype();
							targetItem.unit = sourceItem.getUnit();
							list.add(targetItem);
						}

					}
				});

		// add selection model

		final SingleSelectionModel<PcItem> selectionModel = new SingleSelectionModel<PcItem>();
		cellTable.setSelectionModel(selectionModel);
		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						PcItem selected = selectionModel.getSelectedObject();

						if (selected != null) {

							// Window.alert("You selected: " + selected.name);
							
							  final EditPropertyClassInputWidget editPCWidget = new
									  EditPropertyClassInputWidget(selected.id, selected.name,
							  selected.datatype, selected.unit);
							  
							  editPCWidget.setAutoHideEnabled(true);
							  
							  //multicheckDialogBox.setAnimationEnabled(true);
							  
							  editPCWidget.show(); 
							  editPCWidget.center();
							 

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

	void createColumns(CellTable<PcItem> celltable, List<PcItem> list) {

		// add SortHandler

		ListHandler<PcItem> columnSortHandler = new ListHandler<PcItem>(list);

		// ID Column
		com.google.gwt.user.cellview.client.Column<PcItem, String> idColumn = new com.google.gwt.user.cellview.client.Column<PcItem, String>(
				new TextCell()) {

			@Override
			public String getValue(PcItem object) {
				// TODO Auto-generated method stub
				return object.id;
			}
		};
		idColumn.setSortable(true);
		cellTable.addColumn(idColumn, "Velisphere ID");

		// Name Column
		com.google.gwt.user.cellview.client.Column<PcItem, String> nameColumn = new com.google.gwt.user.cellview.client.Column<PcItem, String>(
				new TextCell()) {

			@Override
			public String getValue(PcItem object) {
				// TODO Auto-generated method stub
				return object.name;
			}
		};
		nameColumn.setSortable(true);
		cellTable.addColumn(nameColumn, "Endpoint Class Name");

		// Datatype Column
		com.google.gwt.user.cellview.client.Column<PcItem, String> dataTypeColumn = new com.google.gwt.user.cellview.client.Column<PcItem, String>(
				new TextCell()) {

			@Override
			public String getValue(PcItem object) {
				// TODO Auto-generated method stub
				return object.datatype;
			}
		};
		dataTypeColumn.setSortable(true);
		cellTable.addColumn(dataTypeColumn, "Datatype");

		// Unit Column
		com.google.gwt.user.cellview.client.Column<PcItem, String> unitColumn = new com.google.gwt.user.cellview.client.Column<PcItem, String>(
				new TextCell()) {

			@Override
			public String getValue(PcItem object) {
				// TODO Auto-generated method stub
				return object.unit;
			}
		};
		unitColumn.setSortable(true);
		cellTable.addColumn(unitColumn, "Unit descriptor");

		// Sort by id
		columnSortHandler.setComparator(idColumn, new Comparator<PcItem>() {
			public int compare(PcItem o1, PcItem o2) {
				if (o1 == o2) {
					return 0;
				}

				// Compare the id columns.
				if (o1 != null) {
					return (o2 != null) ? o1.id.compareTo(o2.id) : 1;
				}
				return -1;
			}
		});

		// Sort by name
		columnSortHandler.setComparator(nameColumn, new Comparator<PcItem>() {
			public int compare(PcItem o1, PcItem o2) {
				if (o1 == o2) {
					return 0;
				}

				// Compare the name columns.
				if (o1 != null) {
					return (o2 != null) ? o1.name.compareTo(o2.name) : 1;
				}
				return -1;
			}
		});

		cellTable.addColumnSortHandler(columnSortHandler);

	}

}
