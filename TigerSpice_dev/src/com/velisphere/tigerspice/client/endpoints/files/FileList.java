package com.velisphere.tigerspice.client.endpoints.files;

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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.velisphere.tigerspice.client.analytics.AnalyticsService;
import com.velisphere.tigerspice.client.analytics.AnalyticsServiceAsync;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.endpointclasses.EPCService;
import com.velisphere.tigerspice.client.endpointclasses.EPCServiceAsync;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.endpoints.EndpointServiceAsync;
import com.velisphere.tigerspice.shared.FileData;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.view.client.HasRows;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.cell.client.ImageCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;


public class FileList extends Composite {

		private DialogBox dialogBox = new DialogBox();
		private CellTable<FileItem> cellTable;
		private String endpointID;
		private VerticalPanel verticalPanel;
		
		
		class FileItem {

			private String id;
			private String fileName;
			private String originalFileName;
			private String fileType;
			private String timeStamp;
			private String endpointId;

		}

		
		
		public FileList(String endpointId) {

			this.endpointID = endpointId;
			
			AnalyticsServiceAsync rpcService;

			rpcService = GWT.create(AnalyticsService.class);

			verticalPanel = new VerticalPanel();
			initWidget(verticalPanel);
		
			
			// fill
			
			rpcService
					.getAllFileData(endpointId,new AsyncCallback<LinkedList<FileData>>() {

						public void onFailure(Throwable caught) {
							Window.alert("Error" + caught.getMessage());
						}

						@Override
						public void onSuccess(LinkedList<FileData> result) {
							
							if (result.size() > 0)
							{
								
								verticalPanel.add(new HTML("<b>Endpoint Document Inbox</b><br><br>"
										+ "The following documents have been uploaded by this endpoint and are available for your review: <br><br>"));
								createTable(result);
								
							}
							else
								verticalPanel.add(new HTML("<b>Inbox currently empty</b><br><br>"
									+ "Possible reasons are that your selected endpoint has not submitted any documents to its inbox, "
									+ "or that the endpoint is experiencing connectivity issues preventing documents from being uploaded."));
							

							
							
							
						
						}
					});

			
			

		}
		
		
		void createTable(LinkedList<FileData> fileData)
		{
			cellTable = new CellTable<FileItem>(5);

			
			// Create a data provider for celltable.
			ListDataProvider<FileItem> dataProvider = new ListDataProvider<FileItem>();

			// Connect the table to the data provider.
			dataProvider.addDataDisplay(cellTable);
				
			final List<FileItem> list = dataProvider.getList();
			
			// create columns
			
			createColumns(cellTable, list);

			
			// fill
			Iterator<FileData> it = fileData.iterator();

			cellTable.setRowCount(fileData.size(), false);
			while (it.hasNext()) {

				final FileData sourceItem = it.next();
				FileItem targetItem = new FileItem();
				targetItem.id = sourceItem.getFileId();
				targetItem.fileName = sourceItem.getFileName();
				targetItem.fileType = sourceItem.getFileType();
				targetItem.endpointId = sourceItem.getEndpointId();
				targetItem.originalFileName = sourceItem.getOriginalFileName();
				targetItem.timeStamp = sourceItem.getTimeStamp();
				list.add(targetItem);
			}
			
			// add selection model

			final SingleSelectionModel<FileItem> selectionModel = new SingleSelectionModel<FileItem>();
			cellTable.setSelectionModel(selectionModel);
			selectionModel
					.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
						public void onSelectionChange(SelectionChangeEvent event) {
							final FileItem selected = selectionModel
									.getSelectedObject();

							if (selected != null) {
								
								
								//Window.alert("You selected: " + selected.name);
								
								EndpointServiceAsync endpointService = GWT
										.create(EndpointService.class);
								
								
								endpointService.getUploadHmacJSON(selected.id, selected.endpointId, new AsyncCallback<String>()
										{

											@Override
											public void onFailure(
													Throwable caught) {
												// TODO Auto-generated method stub
												
											}

											@Override
											public void onSuccess(String result) {
												
												AppController
												.openDirectLink("https://www.velisphere.com/ToucanServer/rest/files/get/binary/download/"+selected.fileType+"/"+selected.endpointId+"/"+result);
												
											}
										}
											
										);
								
							}
						}
					});

			
			// display

			
			cellTable.setHover(true);
			
			
			verticalPanel.add(cellTable);

			SimplePager pager = new SimplePager();
			pager.setDisplay(cellTable);
			pager.setRangeLimited(false);
			
			verticalPanel.add(pager);

		}
		
		

		void createColumns(CellTable<FileItem> celltable, List<FileItem> list){
		
			// add SortHandler
			
			ListHandler<FileItem> columnSortHandler = new ListHandler<FileItem>(
					list);
					
			
			

			// Name Column
			com.google.gwt.user.cellview.client.Column<FileItem, String> nameColumn = new com.google.gwt.user.cellview.client.Column<FileItem, String>(
					new TextCell()) {

				@Override
				public String getValue(FileItem object) {
					// TODO Auto-generated method stub
					return object.originalFileName;
				}
			};
			nameColumn.setSortable(true);
			cellTable.addColumn(nameColumn, "File Name");

			// Type Column
			
			com.google.gwt.user.cellview.client.Column<FileItem, String> typeColumn = new com.google.gwt.user.cellview.client.Column<FileItem, String>(
					new TextCell()) {

				@Override
				public String getValue(FileItem object) {
					// TODO Auto-generated method stub
					return object.fileType;
				}
			};
			nameColumn.setSortable(true);
			cellTable.addColumn(typeColumn, "File Type");
			

			// Timestamp Column
			
			com.google.gwt.user.cellview.client.Column<FileItem, String> timestampColumn = new com.google.gwt.user.cellview.client.Column<FileItem, String>(
						new TextCell()) {

				@Override
				public String getValue(FileItem object) {
					// TODO Auto-generated method stub
					return object.timeStamp;
				}
			};
			nameColumn.setSortable(true);
			cellTable.addColumn(timestampColumn, "Timestamp");

			
						
			// Sort by name
					columnSortHandler.setComparator(nameColumn,
							new Comparator<FileItem>() {
								public int compare(FileItem o1, FileItem o2) {
									if (o1 == o2) {
										return 0;
									}

									// Compare the timestamp columns.
									if (o1 != null) {
										return (o2 != null) ? o1.fileName
												.compareTo(o2.fileName) : 1;
									}
									return -1;
								}
							});

					// Sort by name
					columnSortHandler.setComparator(typeColumn,
							new Comparator<FileItem>() {
								public int compare(FileItem o1, FileItem o2) {
									if (o1 == o2) {
										return 0;
									}

									// Compare the timestamp columns.
									if (o1 != null) {
										return (o2 != null) ? o1.fileType
												.compareTo(o2.fileType) : 1;
									}
									return -1;
								}
							});


					// Sort by name
					columnSortHandler.setComparator(timestampColumn,
							new Comparator<FileItem>() {
								public int compare(FileItem o1, FileItem o2) {
									if (o1 == o2) {
										return 0;
									}

									// Compare the timestamp columns.
									if (o1 != null) {
										return (o2 != null) ? o1.timeStamp
												.compareTo(o2.timeStamp) : 1;
									}
									return -1;
								}
							});

					

		cellTable.addColumnSortHandler(columnSortHandler);

		}
		
		
	}
