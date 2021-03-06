package com.velisphere.tigerspice.client.logic.connectors;

import java.util.Iterator;
import java.util.LinkedList;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.Row;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.IconSize;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.velisphere.tigerspice.client.event.ConnectionSaveEvent;
import com.velisphere.tigerspice.client.event.DraggedToCanvasEvent;
import com.velisphere.tigerspice.client.event.EventUtils;
import com.velisphere.tigerspice.client.helper.ActionSourceConfig;
import com.velisphere.tigerspice.client.helper.DatatypeConfig;
import com.velisphere.tigerspice.client.logic.draggables.DraggableButton;
import com.velisphere.tigerspice.client.logic.draggables.LogicCheck;
import com.velisphere.tigerspice.client.logic.draggables.PhysicalItem;
import com.velisphere.tigerspice.client.properties.PropertyService;
import com.velisphere.tigerspice.client.properties.PropertyServiceAsync;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassService;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassServiceAsync;
import com.velisphere.tigerspice.client.spheres.SphereService;
import com.velisphere.tigerspice.client.spheres.SphereServiceAsync;
import com.velisphere.tigerspice.shared.PropertyClassData;
import com.velisphere.tigerspice.shared.PropertyData;
import com.velisphere.tigerspice.shared.SerializableLogicConnector;
import com.velisphere.tigerspice.shared.SharedConstants;

public class ConnectorSensorLogicCheck extends Connector {

	LogicCheck logicCheck;
	PhysicalItem sensor;
	DraggableButton openingButton;
	TextBox txtCheckValue;
	ListBox lbxOperator;
	boolean layoutCreated;
	String txtCheckValueContent;
	int type;
	String lbxOperatorValue;


	
	public ConnectorSensorLogicCheck (PhysicalItem sensor, LogicCheck logicCheck)
	{
		super();
		createActionUUID();
		createCheckUUID();
		this.sensor = sensor;
		this.logicCheck = logicCheck;
		createBaseLayout();
		createOpenerWidget();
		
	}
	
	public ConnectorSensorLogicCheck (PhysicalItem sensor, LogicCheck logicCheck, String actionID, String checkID,
			final String lbxOperatorValue, final String txtCheckValueContent)
	{
		super();
		this.sensor = sensor;
		this.logicCheck = logicCheck;
		this.txtCheckValueContent = txtCheckValueContent;
		this.lbxOperatorValue = lbxOperatorValue;
		this.actionUUID = actionID;
		this.checkUUID = checkID;
		createOpenerWidget();
		
	}
	
	@Override
	public void show()
	{
		super.show();
		if(layoutCreated == false) 
			{
			createBaseLayout();
			}
		lbxOperator.setSelectedValue(lbxOperatorValue);
		txtCheckValue.setValue(txtCheckValueContent);
	}
	
	@Override
	public void center()
	{
		super.center();
		if(layoutCreated == false) 
		{
		createBaseLayout();
		}
		lbxOperator.setSelectedValue(lbxOperatorValue);
		txtCheckValue.setValue(txtCheckValueContent);
	}

	
	public void save()
	{
		
		lbxOperatorValue = lbxOperator.getValue();
		txtCheckValueContent = txtCheckValue.getValue();
		
	}

	private void fillFields()
	{
		
		lbxOperator.setSelectedValue(lbxOperatorValue);
		
		txtCheckValue.setValue(txtCheckValueContent);
		
		
	}
	
	
	
	private void createBaseLayout()
	{
		this.layoutCreated = true; 
		 this.setStyleName("wellwhite");
         getElement().getStyle().setPadding(10, Unit.PX);
         //getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
         //getElement().getStyle().setBorderColor("#bbbbbb");
         //getElement().getStyle().setBorderWidth(1, Unit.PX);
         getElement().getStyle().setBackgroundColor("#ffffff");
	
        
        VerticalPanel verticalPanel = new VerticalPanel();
        
        Row row0 = new Row();
		Column col0a = new Column(1);
		HorizontalPanel imageHeader = new HorizontalPanel();
		imageHeader.add(this.sensor.getImage());
		imageHeader.add(new Icon(IconType.CHEVRON_SIGN_RIGHT));
		imageHeader.add(this.logicCheck.getImage());
		col0a.add(imageHeader);
		row0.add(col0a);
		Column col0b = new Column(4);	
		col0b.add(new HTML("<b>Define Connection between "+ sensor.getContentRepresentation() + " "
				+ "and "+ logicCheck.getContentRepresentation()+"</b>"));
		row0.add(col0b);
		
		verticalPanel.add(row0);
		
        
        Row row1 = new Row();
		Column col1 = new Column(5);
		HTML headerHTML = new HTML("<br><br>"
				+ "You can define the condition under which the connection will be triggered based "
						+ "on the state of <b>"+ sensor.getContentRepresentation() + "</b>, "
				+ "and then define what data will be send to <b>" + logicCheck.getContentRepresentation() + "</b>. This could be a static "
						+ "value defined in this dialog,"
				+ "or values dynamically pulled from either <b>"+sensor.getContentRepresentation()+"</b> or any other sensor you have access to."
						+ "<br><br>&nbsp;");
		col1.add(headerHTML);
		row1.add(col1);
		verticalPanel.add(row1);
		
	
		Row row2 = new Row();
		Column col2A = new Column(1);
		col2A.add(new HTML("Trigger:"));
		
		Column col2B = new Column(1);
		lbxOperator = new ListBox();
		lbxOperator.setWidth("100%");
		col2B.add(lbxOperator);
		populateLbxOperator(lbxOperator);
		
		Column col2C = new Column(3);
		txtCheckValue = new TextBox();
		col2C.add(txtCheckValue);
		
		row2.add(col2A);
		row2.add(col2B);
		row2.add(col2C);
		
		verticalPanel.add(row2);
			
		
		Row row4 = new Row();
		Column col4A = new Column(1,4);
		Button btnSave = new Button("Close");
		btnSave.setType(ButtonType.SUCCESS);
		col4A.add(btnSave);
		
		
		row4.add(col4A);
		
		verticalPanel.add(row4);
		
		
		this.add(verticalPanel);
		
		// set the various handlers
		
		
		
		
		btnSave.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				//EventUtils.EVENT_BUS.fireEvent(new ConnectionSaveEvent(sensor, logicCheck, thisWidget));
				save();
				close();
			}
			
		});
		
				
		
	}
	
		
	private void populateLbxOperator(final ListBox lbxOperator)
	{
		PropertyClassServiceAsync propertyClassService = GWT
				.create(PropertyClassService.class);
		
		propertyClassService.getPropertyClassForPropertyClassID(
				this.sensor.getPropertyClassID(), new AsyncCallback<PropertyClassData>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(PropertyClassData result) {
						// TODO Auto-generated method stub

						DatatypeConfig dataTypeConfig = new DatatypeConfig();

						if (result.propertyClassDatatype
								.equalsIgnoreCase("string")) {
							Iterator<String> it = dataTypeConfig
									.getTextOperators().iterator();
							while (it.hasNext()) {
								String listItem = it.next();
								lbxOperator.addItem(listItem);

							}
						}

						else

						if (result.propertyClassDatatype
								.equalsIgnoreCase("byte")) {
							Iterator<String> it = dataTypeConfig
									.getNumberOperators().iterator();
							while (it.hasNext()) {
								String listItem = it.next();
								lbxOperator.addItem(listItem);

							}
						}

						else

						if (result.propertyClassDatatype
								.equalsIgnoreCase("long")) {
							Iterator<String> it = dataTypeConfig
									.getNumberOperators().iterator();
							while (it.hasNext()) {
								String listItem = it.next();
								lbxOperator.addItem(listItem);

							}
						}

						else

						if (result.propertyClassDatatype
								.equalsIgnoreCase("float")) {
							Iterator<String> it = dataTypeConfig
									.getNumberOperators().iterator();
							while (it.hasNext()) {
								String listItem = it.next();
								lbxOperator.addItem(listItem);

							}
						}

						else

						if (result.propertyClassDatatype
								.equalsIgnoreCase("double")) {
							Iterator<String> it = dataTypeConfig
									.getNumberOperators().iterator();
							while (it.hasNext()) {
								String listItem = it.next();
								lbxOperator.addItem(listItem);

							}
						}

						else

						if (result.propertyClassDatatype
								.equalsIgnoreCase("boolean")) {
							Iterator<String> it = dataTypeConfig
									.getBooleanOperators().iterator();
							while (it.hasNext()) {
								String listItem = it.next();
								lbxOperator.addItem(listItem);

							}
						}
						
						else lbxOperator.addItem("Invalid Endpoint Configuration");

						fillFields();
					}

				});

		
	}
	
	private void createOpenerWidget()
	{
		openingButton = new DraggableButton(this, this.getClass());
		
		openingButton.setBaseIcon(IconType.FILTER);
		
		openingButton.setStyleName("connbtn");
		
	
	}
	
	
	
	public DraggableButton getOpenerWidget()
	{
		return this.openingButton;
	}
	
	 public SerializableLogicConnector getSerializableRepresentation()
     {
		SerializableLogicConnector serializable = new SerializableLogicConnector();
     	serializable.setLeftID(this.sensor.getUUID());
     	serializable.setRightID(this.logicCheck.getId());
    	serializable.setLbxOperatorValue(this.lbxOperatorValue);
		serializable.setTxtCheckValueContent(this.txtCheckValueContent);
		serializable.setType(SharedConstants.CONP2L);
		serializable.setActionID(actionUUID);
		serializable.setCheckID(checkUUID);
		return serializable;
     	
     }
	 
	 public PhysicalItem getSensor() {
			return sensor;
		}

	 public LogicCheck getLogicCheck() {
			return logicCheck;
		}

	 
	 
		public String getOperator() {
			return String.valueOf(lbxOperatorValue);
		}

		public String getCheckValue() {
			return txtCheckValueContent;
		}
		
		

	
}
