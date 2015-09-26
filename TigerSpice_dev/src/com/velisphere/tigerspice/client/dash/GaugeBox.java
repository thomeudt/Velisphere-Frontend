package com.velisphere.tigerspice.client.dash;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import com.github.gwtbootstrap.client.ui.AlertBlock;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.constants.AlertType;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.IconSize;
import com.github.gwtbootstrap.client.ui.constants.IconType;
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
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.Gauge;
import com.velisphere.tigerspice.client.analytics.AnalyticsService;
import com.velisphere.tigerspice.client.analytics.AnalyticsServiceAsync;
import com.velisphere.tigerspice.client.appcontroller.SessionHelper;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.endpoints.EndpointServiceAsync;
import com.velisphere.tigerspice.client.properties.PropertyService;
import com.velisphere.tigerspice.client.properties.PropertyServiceAsync;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassService;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassServiceAsync;
import com.velisphere.tigerspice.shared.AnalyticsRawData;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.PropertyClassData;
import com.velisphere.tigerspice.shared.PropertyData;

public class GaugeBox extends Composite {

	HTML description = new HTML("<br><b><i>New Gauge</b></i>");
	VerticalPanel panel;
	String gaugeLabel = "---";
	
	ListBox lbxProperties = new ListBox();
	ListBox lbxEndpoints = new ListBox();
	Button btnOK = new Button("OK");
	HashMap<String, PropertyData> propertyDataMap = new HashMap<String, PropertyData>();
	int gaugeType;
	String endpointID;
	String propertyID;
	String readout;
	
	static int EMPTY_GAUGE = 0;
	static int NUMERIC_GAUGE = 1;
	static int ALPHANUMERIC_GAUGE = 2;
	static int BOOLEAN_GAUGE = 3;

	

	public GaugeBox() {
		panel = new VerticalPanel();
		gaugeType = EMPTY_GAUGE;
		initWidget(panel);
		setConfigOkButton();
		configure();
	}

	private void addGauge() {
		panel.add(description);
		if (gaugeType == EMPTY_GAUGE)
		{
			showEmptyGauge();
		}
		else if (gaugeType == NUMERIC_GAUGE)
		{
			VisualizationUtils.loadVisualizationApi(onLoadCallbackNumericGauge, Gauge.PACKAGE);	
		}
		else if (gaugeType == ALPHANUMERIC_GAUGE)
		{
			showAlphaNumericGauge();	
		}
		else if (gaugeType == BOOLEAN_GAUGE)
		{
			showBooleanGauge();
		}
		
		
		
	}

	private void showAlphaNumericGauge()
	{
		
		
		SimplePanel simplePanel = new SimplePanel();
		simplePanel.setHeight("180px");
		simplePanel.setWidth("180px");
		AlertBlock alertBlock = new AlertBlock();
		alertBlock.setHeading("Readout:");
		alertBlock.setType(AlertType.DEFAULT);
		alertBlock.setClose(false);
		alertBlock.setText(readout);
		alertBlock.setHeight("140px");
		alertBlock.setWidth("140px");
		
		simplePanel.add(alertBlock);
		panel.add(simplePanel);
		panel.add(controls());	
	}

	private void showBooleanGauge()
	{
		SimplePanel simplePanel = new SimplePanel();
		simplePanel.setHeight("180px");
		simplePanel.setWidth("180px");
		Icon emptyIcon = new Icon(IconType.CHECK_EMPTY);
		emptyIcon.setIconSize(IconSize.FIVE_TIMES);
		simplePanel.add(emptyIcon);
		panel.add(simplePanel);
		panel.add(controls());
	}
	
	private void showEmptyGauge()
	{
		SimplePanel simplePanel = new SimplePanel();
		simplePanel.setHeight("180px");
		simplePanel.setWidth("180px");
		HTML html = new HTML("We are sorry - no data available for this sensor.");
		simplePanel.add(html);
		panel.add(simplePanel);
		panel.add(controls());
	}

	
	private Runnable onLoadCallbackNumericGauge = new Runnable() {
		public void run() {
			
			Double gaugeValue = Double.parseDouble(readout);
			
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
				gauge.getParent().removeFromParent();
			}

		});

		panel.add(removeButton);
		return panel;

	}

	private void configure() {
		showConfigureBox();

	}
	
	private void showConfigureBox()
	{
		panel.clear();
		panel.add(new HTML("<br><strong>Configure Gauge</strong>"));
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
						endpointID = lbxEndpoints.getSelectedValue();
						
						if (lbxProperties.isAttached())
						{
							lbxProperties.removeFromParent();
							
						}
						if(btnOK.isAttached())
						{
							btnOK.removeFromParent();
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
				lbxProperties.addItem("---");
				propertyDataMap.clear();
				Iterator<PropertyData> it = result.iterator();
				while(it.hasNext())
				{
					PropertyData propertyData = it.next();
					propertyDataMap.put(propertyData.propertyId, propertyData);
					lbxProperties.addItem(propertyData.propertyName, propertyData.propertyId);
					
				}
				
				description = new HTML("<br><b><i>"+lbxProperties.getSelectedItemText()+"</b></i>");

				
				lbxProperties.addChangeHandler(new ChangeHandler(){

					@Override
					public void onChange(ChangeEvent event) {
						

						description = new HTML("<br><b><i>"+lbxProperties.getSelectedItemText()+"</b></i>");
						getPropertyClassData(propertyDataMap.get(lbxProperties.getSelectedValue()).propertyclassId);
						
						propertyID = lbxProperties.getSelectedValue();
								
					
					}
					
				});

							
			}
			
		});
	}

	
	private void getPropertyClassData(String propertyClassID)
	{



		RootPanel.get().add(new HTML("Searching for " + propertyClassID));

		PropertyClassServiceAsync propertyClassService = GWT
				.create(PropertyClassService.class);

		propertyClassService.getPropertyClassForPropertyClassID(propertyClassID, new AsyncCallback<PropertyClassData>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(PropertyClassData result) {

				RootPanel.get().add(new HTML(result.propertyClassName));
				gaugeLabel = result.propertyClassUnit;
			
				
				if (result.propertyClassDatatype == "Double")
				{
					gaugeType = NUMERIC_GAUGE;	
				} else
					if (result.propertyClassDatatype == "String")
					{
						gaugeType = ALPHANUMERIC_GAUGE;	
					} else
						if (result.propertyClassDatatype == "Byte")
						{
							gaugeType = BOOLEAN_GAUGE;	
						}
							
				
				
		
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
				getData();
			}
			
		});
	}

	private void getData()
	{
		AnalyticsServiceAsync analyticsService = GWT
				.create(AnalyticsService.class);
		
		analyticsService.getCurrentSensorState(endpointID, propertyID, new AsyncCallback<AnalyticsRawData>()
				{

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						panel.add(new HTML("Error obtaining data."));
					}

					@Override
					public void onSuccess(AnalyticsRawData result) {
						// TODO Auto-generated method stub
						
						RootPanel.get().add(new HTML("Data Size: " + result.getPropertyValuePairs().size()));
						
						if(result.getPropertyValuePairs().size() > 0)
						{
							Map.Entry<String, String> entry = result.getPropertyValuePairs().entrySet().iterator().next();
							readout = entry.getValue();	
						}
						else
						{
							// re-set gauge type to empty
							gaugeType = EMPTY_GAUGE;
						}
						
						addGauge();
					}
			
				});

		
	}



}
