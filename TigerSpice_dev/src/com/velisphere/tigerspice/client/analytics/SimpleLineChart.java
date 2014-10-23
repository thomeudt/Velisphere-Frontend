package com.velisphere.tigerspice.client.analytics;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import com.github.gwtbootstrap.client.ui.CellTable;
import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.corechart.LineChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.helper.ConversionHelpers;
import com.velisphere.tigerspice.shared.AnalyticsRawData;
import com.velisphere.tigerspice.shared.TableRowData;

public class SimpleLineChart {

	DataTable data;
	LinkedList<TableRowData> tableExportData;
	LineChart lines;
	Column graphCol;

	SimpleLineChart(LinkedList<AnalyticsRawData> dataSource, String endpointID,
			String propertyID, final String propertyName,
			final String endpointName, final Timestamp startDate,
			final Timestamp endDate) {

		// check if datasource is not empty

		if (dataSource.size() == 0) {
			graphCol = new Column(8);
			graphCol.add(new Paragraph(
					"Sorry - There is no data available for your selection."));
		} else {

			graphCol = new Column(8);

			int columnCount = dataSource.getLast().getPropertyValuePairs()
					.size();

			// create container class for cell table

			class SensorTrail {

				private String timeStamp;
				private String value[];
				private Integer itemNumber;

			}

			final Options options = Options.create();

			options.setTitle("Sensor Data Trail for Sensor '" + propertyName
					+ "' on Endpoint '" + endpointName + "'");

			options.setHeight((int) (RootPanel.get().getOffsetHeight() * 0.3));
			options.setFontName("Source Sans Pro");
			options.setLineWidth(2);

			//System.out.println(endpointID + " / " + propertyID);

			// prep the datatable for celltable

			final CellTable<SensorTrail> table = new CellTable<SensorTrail>(
					10,
					GWT.<CellTable.SelectableResources> create(CellTable.SelectableResources.class));

			TextColumn<SensorTrail> timeStampColumn = new TextColumn<SensorTrail>() {
				@Override
				public String getValue(SensorTrail entry) {
					return entry.timeStamp;
				}
			};

			timeStampColumn.setSortable(true);

			// Create a data provider for celltable.
			ListDataProvider<SensorTrail> dataProvider = new ListDataProvider<SensorTrail>();

			// Connect the table to the data provider.
			dataProvider.addDataDisplay(table);

			List<SensorTrail> list = dataProvider.getList();

			// Add a ColumnSortEvent.ListHandler to connect sorting to the
			// java.util.List.
			ListHandler<SensorTrail> columnSortHandler = new ListHandler<SensorTrail>(
					list);
			// Sort by TimeStamp
			columnSortHandler.setComparator(timeStampColumn,
					new Comparator<SensorTrail>() {
						public int compare(SensorTrail o1, SensorTrail o2) {
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

			// Add the timestamp column to table.
			table.addColumn(timeStampColumn, "Time Stamp");

			// prep the datatable for chart

			data = DataTable.create();
			data.addColumn(ColumnType.STRING, "Time");

			TableRowData introRow = new TableRowData();
			String[] introRowArray = new String[columnCount + 1]; // +1 because
																	// of
																	// timestamp
																	// column
			introRowArray[0] = "VeliSphere Data Trail Export for "
					+ propertyName + " on " + endpointName;
			int introCount = 1;
			while (introCount <= columnCount) {
				introRowArray[introCount] = "";
				introCount = introCount + 1;
			}
			introRow.setRow(introRowArray);

			// prep data table for csv export
			tableExportData = new LinkedList<TableRowData>();

			tableExportData.add(introRow);

			TableRowData spacerRow = new TableRowData();
			String[] spacerRowArray = new String[columnCount + 1]; // +1 because
																	// of
																	// timestamp
																	// column
			spacerRowArray[0] = "--------------------------------------------------------------------";
			int spacerCount = 1;
			while (spacerCount <= columnCount) {
				spacerRowArray[spacerCount] = "";
				spacerCount = spacerCount + 1;
			}
			spacerRow.setRow(spacerRowArray);
			tableExportData.add(spacerRow);

			TableRowData headerRow = new TableRowData();
			String[] headerRowArray = new String[columnCount + 1]; // +1 because
																	// of
																	// timestamp
																	// column
			headerRowArray[0] = "Timestamp";

			// set the headers in all tables

			// @SuppressWarnings("rawtypes")
			// LinkedList valueColumns = new
			// LinkedList<com.google.gwt.user.cellview.client.Column<SensorTrail,
			// SafeHtml>>();

			int iColNo = 0;

			Iterator<Entry<String, String>> dsIT = dataSource.getLast()
					.getPropertyValuePairs().entrySet().iterator();
			while (dsIT.hasNext()) {

				Entry<String, String> pvPair = dsIT.next();
				String columnHeader = pvPair.getKey();
				String columnContent = pvPair.getValue();

				// check if column content is numeric to arrange for proper
				// sorting behavior
				if (ConversionHelpers.isNumeric(columnContent)) {
					//System.out.println(columnHeader + " is numeric.");
					final int entryNo = iColNo;
					com.google.gwt.user.cellview.client.Column<SensorTrail, Number> valueColumn = new com.google.gwt.user.cellview.client.Column<SensorTrail, Number>(
							new NumberCell()) {
						@Override
						public Number getValue(SensorTrail entry) {
							return Double.parseDouble(entry.value[entryNo]);
						}
					};
					// ---
					data.addColumn(ColumnType.NUMBER, columnHeader);
					valueColumn.setSortable(true);
					table.addColumn(valueColumn, columnHeader);
					// valueColumns.add(valueColumn);
					headerRowArray[entryNo + 1] = columnHeader; // +1 because of
																// timestamp
					// ---

					// Add sort handler to column

					//System.out.println("creating value sort handler for numeric cells");

					columnSortHandler.setComparator(valueColumn,
							new Comparator<SensorTrail>() {
								public int compare(SensorTrail o1,
										SensorTrail o2) {
									if (o1 == o2) {
										return 0;
									}

									// Compare the value columns.
									if (o1 != null) {
										Double o1number = Double
												.parseDouble(o1.value[entryNo]);
										Double o2number = Double
												.parseDouble(o2.value[entryNo]);
										return (o2 != null) ? o1number
												.compareTo(o2number) : 1;
									}
									return -1;
								}
							});

				} else {
					final int entryNo = iColNo;
					com.google.gwt.user.cellview.client.Column<SensorTrail, SafeHtml> valueColumn = new com.google.gwt.user.cellview.client.Column<SensorTrail, SafeHtml>(
							new SafeHtmlCell()) {
						@Override
						public SafeHtml getValue(SensorTrail entry) {
							return SafeHtmlUtils
									.fromString(entry.value[entryNo]);
						}
					};
					// ---
					data.addColumn(ColumnType.NUMBER, columnHeader);
					valueColumn.setSortable(true);
					table.addColumn(valueColumn, columnHeader);
					// valueColumns.add(valueColumn);
					headerRowArray[entryNo + 1] = columnHeader; // +1 because of
																// timestamp
					// ---

					// Add sort handler to column

					//System.out.println("creating value sort handler for HTML cells");

					columnSortHandler.setComparator(valueColumn,
							new Comparator<SensorTrail>() {
								public int compare(SensorTrail o1,
										SensorTrail o2) {
									if (o1 == o2) {
										return 0;
									}

									// Compare the value columns.
									if (o1 != null) {
										return (o2 != null) ? o1.value[entryNo]
												.compareTo(o2.value[entryNo])
												: 1;
									}
									return -1;
								}
							});

				}

				iColNo = iColNo + 1;
			}

			headerRow.setRow(headerRowArray);
			tableExportData.add(headerRow);

			// set correct row count for all tables
			data.addRows(dataSource.size() + 1);

			table.setRowCount(dataSource.size(), true);
			// table.setVisibleRange(0, 10);

			int iRow = 1;
			Iterator<AnalyticsRawData> it = dataSource.iterator();

			if (startDate != null && endDate != null) {

				// populate table with date filter applied
				while (it.hasNext()) {

					AnalyticsRawData logItem = it.next();

					if (Timestamp.valueOf(logItem.getTimeStamp()).after(
							startDate)
							&& Timestamp.valueOf(logItem.getTimeStamp())
									.before(endDate)) {

						// add to data table for graph
						data.setValue(iRow, 0, logItem.getTimeStamp());

						// add to datasource for table display
						SensorTrail trailItem = new SensorTrail();
						trailItem.timeStamp = logItem.getTimeStamp();
						trailItem.value = new String[logItem
								.getPropertyValuePairs().size()];

						int iColumn = 0;
						Iterator<Entry<String, String>> itItem = logItem
								.getPropertyValuePairs().entrySet().iterator();
						while (itItem.hasNext()) {

							Entry<String, String> logItemColumn = itItem.next();

							// add to chart table only if numeric value,
							// otherwise set to 1 to indicate data transmission
							if (ConversionHelpers.isNumeric(logItemColumn
									.getValue())) {
								data.setValue(iRow, iColumn + 1,
										logItemColumn.getValue());
							} else {
								data.setValue(iRow, iColumn + 1, 1);
							}

							trailItem.value[iColumn] = logItemColumn.getValue();
							iColumn = iColumn + 1;

						}

						trailItem.itemNumber = iRow;
						list.add(trailItem);

						TableRowData row = new TableRowData();
						String[] rowArray = new String[logItem
								.getPropertyValuePairs().size() + 1];// +1
																		// because
																		// of
																		// timestamp
																		// in
																		// column
																		// 0
						rowArray[0] = logItem.getTimeStamp();
						Iterator<Entry<String, String>> itPV = logItem
								.getPropertyValuePairs().entrySet().iterator();
						int iColumnPV = 1;
						while (itPV.hasNext()) {
							rowArray[iColumnPV] = String.valueOf(itPV.next()
									.getValue());
							iColumnPV = iColumnPV + 1;
						}
						row.setRow(rowArray);

						tableExportData.add(row);

						// Add the data to the data provider, which
						// automatically pushes it to the
						// widget.

					}

					iRow = iRow + 1;
				}

			} else {
				// populate table without date filter applied
				while (it.hasNext()) {

					AnalyticsRawData logItem = it.next();

					// add to data table for graph
					data.setValue(iRow, 0, logItem.getTimeStamp());

					// add to datasource for table display
					SensorTrail trailItem = new SensorTrail();
					trailItem.timeStamp = logItem.getTimeStamp();
					trailItem.value = new String[logItem
							.getPropertyValuePairs().size()];

					int iColumn = 0;
					Iterator<Entry<String, String>> itItem = logItem
							.getPropertyValuePairs().entrySet().iterator();
					while (itItem.hasNext()) {

						Entry<String, String> logItemColumn = itItem.next();

						// add to chart table only if numeric value, otherwise
						// set to 1 to indicate data transmission
						if (ConversionHelpers.isNumeric(logItemColumn
								.getValue())) {
							data.setValue(iRow, iColumn + 1,
									logItemColumn.getValue());
						} else {
							data.setValue(iRow, iColumn + 1, 1);
						}

						trailItem.value[iColumn] = logItemColumn.getValue();
						iColumn = iColumn + 1;

					}

					trailItem.itemNumber = iRow;
					list.add(trailItem);

					TableRowData row = new TableRowData();
					String[] rowArray = new String[logItem
							.getPropertyValuePairs().size() + 1];// +1 because
																	// of
																	// timestamp
																	// in column
																	// 0
					rowArray[0] = logItem.getTimeStamp();
					Iterator<Entry<String, String>> itPV = logItem
							.getPropertyValuePairs().entrySet().iterator();
					int iColumnPV = 1;
					while (itPV.hasNext()) {
						rowArray[iColumnPV] = String.valueOf(itPV.next()
								.getValue());
						iColumnPV = iColumnPV + 1;
					}
					row.setRow(rowArray);

					tableExportData.add(row);

					// Add the data to the data provider, which
					// automatically pushes it to the
					// widget.

					iRow = iRow + 1;

				}

			}

			//System.out.println(tableExportData.toString());

			// Create a line chart visualization.
			lines = new LineChart(data, options);
			graphCol.clear();

			graphCol.add(lines);

			table.addColumnSortHandler(columnSortHandler);

			table.setStriped(false);
			table.setHover(true);

			table.getColumnSortList().push(timeStampColumn);
			table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

			// add selection model

			final SingleSelectionModel<SensorTrail> selectionModel = new SingleSelectionModel<SensorTrail>();
			table.setSelectionModel(selectionModel);
			selectionModel
					.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
						public void onSelectionChange(SelectionChangeEvent event) {
							SensorTrail selected = selectionModel
									.getSelectedObject();

							if (selected != null) {
								// Window.alert("You selected: " +
								// selected.value);

								JsArray arr = JavaScriptObject.createArray()
										.cast();
								arr.push(newJsEntry(
										selected.itemNumber.toString(), "1"));
								lines.setSelections(arr);

							}
						}
					});

			graphCol.add(table);

			SimplePager pager = new SimplePager();
			pager.setDisplay(table);
			pager.setRangeLimited(false);

			graphCol.add(pager);
			graphCol.add(new Paragraph(" "));

		}
	}

	public void startDownload(final String endpointName,
			final String propertyName) {
		final AnalyticsServiceAsync analyticsService = GWT
				.create(AnalyticsService.class);

		analyticsService.getEndpointLogAsFile(tableExportData,
				new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(String result) {
						AppController
								.openDirectLink("/tigerspice_dev/tigerspiceDownloads?privateURL="
										+ result
										+ "&outboundFileName=Sensortrail_for_"
										+ endpointName
										+ "_"
										+ propertyName
										+ ".csv");

					}

				});

	}

	public void getImage(final String endpointName, final String propertyName) {
		//System.out.println(lines.getElement().getInnerHTML());

		final AnalyticsServiceAsync analyticsService = GWT
				.create(AnalyticsService.class);

		final String dataUri = nativeGetUrl(lines.getJso());

		analyticsService.getEndpointLogChartAsFile(dataUri,
				new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(String result) {
						
						AppController
								.openDirectLink("/tigerspice_dev/tigerspiceDownloads?privateURL="
										+ result
										+ "&outboundFileName=Sensortrail_for_"
										+ endpointName
										+ "_"
										+ propertyName
										+ ".png&persist=0");

					}

				});

	}

	public Column getGraphColumn() {
		return graphCol;
	}

	private final native JavaScriptObject newJsEntry(String rowNo, String colNo)/*-{
		return {
			row : rowNo,
			col : colNo
		};

	}-*/;

	private final native String nativeGetUrl(JavaScriptObject jso) /*-{
		return jso.getImageURI();
	}-*/;

}
