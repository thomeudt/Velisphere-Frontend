package com.velisphere.tigerspice.client.analytics;

import java.util.Iterator;
import java.util.LinkedList;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.github.gwtbootstrap.client.ui.Row;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.resources.ButtonSize;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.corechart.LineChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.endpoints.EndpointServiceAsync;
import com.velisphere.tigerspice.client.properties.PropertyService;
import com.velisphere.tigerspice.client.properties.PropertyServiceAsync;
import com.velisphere.tigerspice.client.spheres.SphereService;
import com.velisphere.tigerspice.client.spheres.SphereServiceAsync;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.EndpointLogData;
import com.velisphere.tigerspice.shared.PropertyData;
import com.velisphere.tigerspice.shared.SphereData;

public class ChartSensorHistoryWidget extends Composite {

	ListBox lbxSphereFilter;
	ListBox lbxEndpointFilter;
	ListBox lbxPropertyFilter;
	Column graphCol;

	public ChartSensorHistoryWidget(String userID) {
		// initWidget(uiBinder.createAndBindUi(this));

		final VerticalPanel vp = new VerticalPanel();

		initWidget(vp);

		// setup main analyzer layout
		Row mainRow = new Row();
		Column leftCol = new Column(2);
		mainRow.add(leftCol);
		Column rightCol = new Column(8);
		mainRow.add(rightCol);
		vp.add(mainRow);

		// setup special analyzer layout

		Row filterRow1 = new Row();
		Column filterCol1 = new Column(2);
		Label lblSphereFilter = new Label("Sphere");
		filterCol1.add(lblSphereFilter);

		filterRow1.add(filterCol1);

		Row filterRow2 = new Row();
		Column filterCol2 = new Column(2);
		lbxSphereFilter = new ListBox();
		lbxSphereFilter.setSize(2);
		filterCol2.add(lbxSphereFilter);

		filterRow2.add(filterCol2);

		Row filterRow3 = new Row();
		Column filterCol3 = new Column(2);
		Label lblEndpointFilter = new Label("Endpoint");
		filterCol3.add(lblEndpointFilter);

		filterRow3.add(filterCol3);

		Row filterRow4 = new Row();
		Column filterCol4 = new Column(2);
		lbxEndpointFilter = new ListBox();
		lbxEndpointFilter.setSize(2);
		filterCol4.add(lbxEndpointFilter);

		filterRow4.add(filterCol4);

		Row filterRow5 = new Row();
		Column filterCol5 = new Column(2);
		Label lblPropertyFilter = new Label("Sensor");
		filterCol5.add(lblPropertyFilter);

		filterRow5.add(filterCol5);

		Row filterRow6 = new Row();
		Column filterCol6 = new Column(2);
		lbxPropertyFilter = new ListBox();
		lbxPropertyFilter.setSize(2);
		filterCol6.add(lbxPropertyFilter);

		filterRow6.add(filterCol6);

		Row filterRow7 = new Row();
		Column filterCol7 = new Column(2);
		Button btnDownload = new Button("Download");
		btnDownload.setSize(ButtonSize.SMALL);
		btnDownload.setType(ButtonType.PRIMARY);
		filterCol7.add(btnDownload);

		filterRow7.add(filterCol7);

		Row graphRow = new Row();
		graphCol = new Column(8);
		graphRow.add(graphCol);

		leftCol.add(filterRow1);
		leftCol.add(filterRow2);
		leftCol.add(filterRow3);
		leftCol.add(filterRow4);
		leftCol.add(filterRow5);
		leftCol.add(filterRow6);
		leftCol.add(filterRow7);

		rightCol.add(graphRow);

		populateSphereList(userID);
		lbxSphereFilter.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {

				populateEndpointList(lbxSphereFilter.getValue());
			}
		});
		lbxEndpointFilter.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {

				populateSensorList(lbxEndpointFilter.getValue());
			}
		});

		lbxPropertyFilter.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				
				graphCol.add(new Paragraph("Loading chart..."));

				Runnable onLoadCallback = new Runnable() {
					public void run() {

						populateChart(lbxEndpointFilter.getItemText(lbxEndpointFilter.getSelectedIndex()), lbxPropertyFilter.getItemText(lbxPropertyFilter.getSelectedIndex()));
					}
				};

				VisualizationUtils.loadVisualizationApi(onLoadCallback,
						LineChart.PACKAGE);

			}
		});

	}

	private void populateSphereList(String userID) {

		final SphereServiceAsync sphereService = GWT
				.create(SphereService.class);

		sphereService.getAllSpheresForUserID(userID,
				new AsyncCallback<LinkedList<SphereData>>() {
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(LinkedList<SphereData> result) {
						Iterator<SphereData> iT = result.iterator();
						while (iT.hasNext()) {
							SphereData sphere = iT.next();
							lbxSphereFilter.addItem(sphere.getName(),
									sphere.getId());

						}
						populateEndpointList(lbxSphereFilter.getValue());

					}
				});
	}

	private void populateEndpointList(String sphereID) {

		final EndpointServiceAsync endpointService = GWT
				.create(EndpointService.class);

		endpointService.getEndpointsForSphere(sphereID,
				new AsyncCallback<LinkedList<EndpointData>>() {
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(LinkedList<EndpointData> result) {
						lbxEndpointFilter.clear();

						Iterator<EndpointData> iT = result.iterator();
						while (iT.hasNext()) {
							EndpointData endpoint = iT.next();

							lbxEndpointFilter.addItem(endpoint.getName(),
									endpoint.getId());

						}
						populateSensorList(lbxEndpointFilter.getValue());
					}
				});

	}

	private void populateSensorList(String endpointID) {

		final PropertyServiceAsync propertyService = GWT
				.create(PropertyService.class);

		propertyService.getSensorPropertiesForEndpointID(endpointID,
				new AsyncCallback<LinkedList<PropertyData>>() {
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(LinkedList<PropertyData> result) {
						lbxPropertyFilter.clear();

						Iterator<PropertyData> iT = result.iterator();
						while (iT.hasNext()) {
							PropertyData property = iT.next();

							lbxPropertyFilter.addItem(property.getName(),
									property.getId());

						}

						populateChart(lbxEndpointFilter.getItemText(lbxEndpointFilter.getSelectedIndex()), lbxPropertyFilter.getItemText(lbxPropertyFilter.getSelectedIndex()));
					}
				});

	}

	private void populateChart(String endpointName, String propertyName) {
		
		graphCol.clear();

		final Options options = Options.create();

		options.setTitle("Sensor Data Trail for Sensor '" + propertyName + "' on Endpoint '"+ endpointName+"'");
		// options.setWidth(vp.getOffsetWidth());
		options.setHeight((int) (RootPanel.get().getOffsetHeight() * 0.3));
		options.setFontName("Source Sans Pro");
		options.setLineWidth(2);

		final AnalyticsServiceAsync analyticsService = GWT
				.create(AnalyticsService.class);

		System.out.println(lbxEndpointFilter.getValue() + " / " + lbxPropertyFilter.getValue());
		analyticsService.getEndpointLog(lbxEndpointFilter.getValue(), lbxPropertyFilter.getValue(),
				new AsyncCallback<LinkedList<EndpointLogData>>() {
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(LinkedList<EndpointLogData> result) {
						// TODO Auto-generated method stub

						DataTable data = DataTable.create();
						data.addColumn(ColumnType.STRING, "Time");
						data.addColumn(ColumnType.NUMBER, "Value");

						data.addRows(result.size() + 1);

						int i = 1;
						Iterator<EndpointLogData> it = result.iterator();

						while (it.hasNext()) {

							EndpointLogData logItem = it.next();
							data.setValue(i, 0, logItem.getTimeStamp());
							data.setValue(i, 1,
									Integer.parseInt(logItem.getValue()));
							i = i + 1;

						}

						// Create a line chart visualization.
						LineChart lines = new LineChart(data, options);
						graphCol.add(lines);

					}
				});
	}

}
