package com.velisphere.tigerspice.client.dash;

import java.util.Iterator;
import java.util.LinkedList;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.resources.ButtonSize;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.Gauge;
import com.velisphere.tigerspice.client.appcontroller.SessionHelper;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.endpoints.EndpointServiceAsync;
import com.velisphere.tigerspice.client.properties.PropertyService;
import com.velisphere.tigerspice.client.properties.PropertyServiceAsync;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.PropertyData;

public class GaugeBox extends Composite {

	HTML description = new HTML("<br><b><i>New Gauge</b></i>");
	VerticalPanel panel;
	String gaugeLabel = "---";
	int gaugeValue = 0;
	ListBox lbxProperties = new ListBox();
	ListBox lbxEndpoints = new ListBox();
	Button btnOK = new Button("OK");

	

	public GaugeBox() {
		panel = new VerticalPanel();
		initWidget(panel);
		setConfigOkButton();
		addGauge();
	}

	private void addGauge() {
		panel.add(description);
		VisualizationUtils.loadVisualizationApi(onLoadCallback, Gauge.PACKAGE);

	}

	private Runnable onLoadCallback = new Runnable() {
		public void run() {
			DataTable data = DataTable.create();

			data.addColumn(ColumnType.STRING, "Label");
			data.addColumn(ColumnType.NUMBER, "Value");
			data.addRows(1);
			data.setValue(0, 0, gaugeLabel);
			data.setValue(0, 1, gaugeValue);
			Gauge.Options option = Gauge.Options.create();

			option.setHeight(180);
			option.setWidth(180);
			option.setGreenRange(71, 80);
			option.setMinorTicks(10);
			option.setRedRange(81, 100);
			option.setYellowRange(61, 70);

			panel.add(new Gauge(data, option));
			panel.add(controls());

		}
	};

	private HorizontalPanel controls() {

		HorizontalPanel panel = new HorizontalPanel();
		Button configureButton = new Button("Configure");
		configureButton.setSize(ButtonSize.MINI);
		configureButton.setType(ButtonType.PRIMARY);
		configureButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				configure();

			}

		});

		panel.add(configureButton);

		Button removeButton = new Button("Remove");
		removeButton.setSize(ButtonSize.MINI);
		removeButton.setType(ButtonType.DANGER);

		final Composite gauge = this;

		removeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				gauge.getParent().removeFromParent();
			}

		});

		panel.add(removeButton);
		return panel;

	}

	private void configure() {
		configureBox();

	}
	
	private void configureBox()
	{
		panel.clear();
		panel.add(new HTML("<br><h5>Configure Gauge</h5>"));
		panel.add(lbxEndpoints);
		lbxEndpoints.setWidth("100%");
		fillEndpointDropDown(lbxEndpoints);
	
	}
	
	private void fillEndpointDropDown(final ListBox lbxEndpoints)
	{
		EndpointServiceAsync endpointService = GWT
				.create(EndpointService.class);

	
		
		endpointService.getEndpointsForUser(SessionHelper.getCurrentUserID(), new AsyncCallback<LinkedList<EndpointData>>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(LinkedList<EndpointData> result) {
	
				Iterator<EndpointData> it = result.iterator();
				while(it.hasNext())
				{
					EndpointData endpointData = it.next();
					lbxEndpoints.addItem(endpointData.endpointName, endpointData.endpointId);
								
				}
				
				lbxEndpoints.addChangeHandler(new ChangeHandler(){

					@Override
					public void onChange(ChangeEvent event) {
						if (lbxProperties.isAttached())
						{
							lbxProperties.removeFromParent();
							
						}
						
						panel.add(lbxProperties);
						lbxProperties.setWidth("100%");
						fillPropertyDropDown(lbxEndpoints.getSelectedValue());		
					}
					
				});

				
			}
			
		});
	}

	
	private void fillPropertyDropDown(String endpointID)
	{
		PropertyServiceAsync propertyService = GWT
				.create(PropertyService.class);

	
		
		propertyService.getSensorPropertiesForEndpointID(endpointID, new AsyncCallback<LinkedList<PropertyData>>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(LinkedList<PropertyData> result) {

				lbxProperties.clear();
				Iterator<PropertyData> it = result.iterator();
				while(it.hasNext())
				{
					PropertyData propertyData = it.next();
					lbxProperties.addItem(propertyData.propertyName, propertyData.propertyId);
					
				}
				
				description = new HTML("<br><b><i>"+lbxProperties.getSelectedItemText()+"</b></i>");

				
				lbxProperties.addChangeHandler(new ChangeHandler(){

					@Override
					public void onChange(ChangeEvent event) {
						

						description = new HTML("<br><b><i>"+lbxProperties.getSelectedItemText()+"</b></i>");

						gaugeLabel = "";
						gaugeValue = 80;
							
					
					}
					
				});

				if(btnOK.isAttached())
				{
					btnOK.removeFromParent();
				}
				panel.add(btnOK);
							
			}
			
		});
	}

	
	private void setConfigOkButton()
	{
		btnOK.setSize(ButtonSize.SMALL);
		btnOK.setType(ButtonType.PRIMARY);
		btnOK.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				panel.clear();
				addGauge();
				
			}
			
		});
		
		
	}
}
