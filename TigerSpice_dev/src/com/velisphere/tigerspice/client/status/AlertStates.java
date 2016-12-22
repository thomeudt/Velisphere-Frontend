package com.velisphere.tigerspice.client.status;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.velisphere.tigerspice.client.actions.ActionService;
import com.velisphere.tigerspice.client.actions.ActionServiceAsync;
import com.velisphere.tigerspice.client.analytics.AnalyticsService;
import com.velisphere.tigerspice.client.analytics.AnalyticsServiceAsync;
import com.velisphere.tigerspice.client.appcontroller.SessionHelper;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.endpoints.EndpointServiceAsync;
import com.velisphere.tigerspice.shared.ActionData;
import com.velisphere.tigerspice.shared.AlertData;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;


public class AlertStates extends Composite {

		private CellTable<AlertItem> cellTable;
		private VerticalPanel verticalPanel;
		
		
		class AlertItem {

			private String id;
			private String alertName;
			private String lastExecution;
			private String payload;
			
		}

		
		
		public AlertStates() {
			
			verticalPanel = new VerticalPanel();
			initWidget(verticalPanel);
		
			
			// fill
			
			String userID = SessionHelper.getCurrentUserID();
			final EndpointServiceAsync endpointService = GWT
					.create(EndpointService.class);

			endpointService.getAllAlertsForUser(userID, new AsyncCallback<LinkedList<AlertData>>()
					{

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onSuccess(LinkedList<AlertData> result) {

							
							if (result.size() > 0)
							{
								
								verticalPanel.add(new HTML("<b>Alert Status</b><br><br>"
										+ "This page gives you an overview of your alerts and their most recent state: <br><br>"));
								createTable(result);
								
							}
							else
								verticalPanel.add(new HTML("<b>No alerts defined</b><br><br>"
										+ "You have currently no alerts defined. <br><br>To set alerts, open the respective endpoint page and select the <i>alerts</i> tab. "
												+ "Once you have alerts defined, this page will give you an overview of your alerts and their most recent state. <br><br>"));
						}
					});
					
			

		}
		
		
		void createTable(LinkedList<AlertData> alertData)
		{
			cellTable = new CellTable<AlertItem>(2);

			
			// Create a data provider for celltable.
			ListDataProvider<AlertItem> dataProvider = new ListDataProvider<AlertItem>();

			// Connect the table to the data provider.
			dataProvider.addDataDisplay(cellTable);
				
			final List<AlertItem> list = dataProvider.getList();
			
			// create columns
			
			createColumns(cellTable, list);

			
			// fill
			Iterator<AlertData> it = alertData.iterator();
			
			final ActionServiceAsync actionService = GWT
					.create(ActionService.class);
			
			cellTable.setRowCount(alertData.size(), false);
			
			final AlertData sourceItem = it.next();
			
			
			actionService.getActionsForCheckpathID(sourceItem.getCheckpathID(), new AsyncCallback<LinkedList<ActionData>>()
					{

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onSuccess(
								LinkedList<ActionData> result) {
							
							Iterator<ActionData> it = result.iterator();
							while(it.hasNext())
							{
								final AnalyticsServiceAsync analyticsService = GWT
										.create(AnalyticsService.class);
								
								analyticsService.getLastActionExecuted(it.next().getActionID(), new AsyncCallback<ActionData>()
										{

											@Override
											public void onFailure(
													Throwable caught) {
												// TODO Auto-generated method stub
												
											}

											@Override
											public void onSuccess(
													ActionData result) {
												

												AlertItem targetItem = new AlertItem();
												targetItem.id = sourceItem.getAlertID();
												targetItem.alertName = sourceItem.getAlertName();
												targetItem.payload = result.getPayload();
												
												if(result.getTimestamp() == null)
												{
													targetItem.lastExecution = "never";
												}
												else
												{
													targetItem.lastExecution = result.getTimestamp();
												}
												
												
												//targetItem.lastExecution = sourceItem.;
												list.add(targetItem);

												
												
											}
									
										});
								
								
							}
							
							
						}
				
					});
				
			
			

			
			// add selection model

			final SingleSelectionModel<AlertItem> selectionModel = new SingleSelectionModel<AlertItem>();
			cellTable.setSelectionModel(selectionModel);
			selectionModel
					.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
						public void onSelectionChange(SelectionChangeEvent event) {
							final AlertItem selected = selectionModel
									.getSelectedObject();

							if (selected != null && selected.lastExecution != "never") {
								
								showWindow(selected.payload);																
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
		
		

		void createColumns(CellTable<AlertItem> celltable, List<AlertItem> list){
		
			// add SortHandler
			
			ListHandler<AlertItem> columnSortHandler = new ListHandler<AlertItem>(
					list);
					
			
			

			// Name Column
			com.google.gwt.user.cellview.client.Column<AlertItem, String> nameColumn = new com.google.gwt.user.cellview.client.Column<AlertItem, String>(
					new TextCell()) {

				@Override
				public String getValue(AlertItem object) {
					// TODO Auto-generated method stub
					return object.alertName;
				}
			};
			nameColumn.setSortable(true);
			cellTable.addColumn(nameColumn, "Alert Name");

			// Last Execution Column
			
			com.google.gwt.user.cellview.client.Column<AlertItem, String> lastExecColumn = new com.google.gwt.user.cellview.client.Column<AlertItem, String>(
					new TextCell()) {

				@Override
				public String getValue(AlertItem object) {
					// TODO Auto-generated method stub
					return object.lastExecution;
				}
			};
			lastExecColumn.setSortable(true);
			cellTable.addColumn(lastExecColumn, "Last Execution");
			
			
						
			// Sort by name
					columnSortHandler.setComparator(nameColumn,
							new Comparator<AlertItem>() {
								public int compare(AlertItem o1, AlertItem o2) {
									if (o1 == o2) {
										return 0;
									}

									// Compare the timestamp columns.
									if (o1 != null) {
										return (o2 != null) ? o1.alertName
												.compareTo(o2.alertName) : 1;
									}
									return -1;
								}
							});

			// Sort by last execution
					columnSortHandler.setComparator(lastExecColumn,
							new Comparator<AlertItem>() {
								public int compare(AlertItem o1, AlertItem o2) {
									if (o1 == o2) {
										return 0;
									}

									// Compare the timestamp columns.
									if (o1 != null) {
										return (o2 != null) ? o1.lastExecution
												.compareTo(o2.lastExecution) : 1;
									}
									return -1;
								}
							});

					

		cellTable.addColumnSortHandler(columnSortHandler);

		}
		
		
		private void showWindow(final String messageText)
		{
					final DialogBox detailDialog = new DialogBox();
					detailDialog.setStyleName("wellappleblue");
					detailDialog.setText("The following message has been sent");
					VerticalPanel panel = new VerticalPanel();
					
					
					panel.add(new HTML(messageText));
					Button closeButton = new Button();
					closeButton.setType(ButtonType.PRIMARY);
					closeButton.setText("OK");
					panel.add(closeButton);
					closeButton.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							// TODO Auto-generated method stub
							detailDialog.removeFromParent();
						}
						
					});
						
					detailDialog.add(panel);
					detailDialog.center();
			
		}

		
	}
