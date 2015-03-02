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
import com.velisphere.tigerspice.client.helper.UuidService;
import com.velisphere.tigerspice.client.helper.UuidServiceAsync;
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
import com.velisphere.tigerspice.shared.SerializableLogicPhysicalItem;
import com.velisphere.tigerspice.shared.SharedConstants;

public class ConnectorLogicCheckActor extends Connector {

	PhysicalItem actor;
	LogicCheck logicCheck;
	Button openingButton;
	ListBox lbxSource;
	TextBox txtManualEntry;
	ListBox lbxTypicalValues;
	boolean layoutCreated;
	String txtManualEntryContent;	
	int lbxSourceIndex;
	int lbxValueFromSensorIndex;
	int lbxTypicalValuesIndex;
	
	
	

	
	public ConnectorLogicCheckActor (LogicCheck logicCheck, PhysicalItem actor)
	{
		super();
	
		this.logicCheck = logicCheck;
		this.actor = actor;
		createBaseLayout();
		createOpenerWidget();
	}
	
	public ConnectorLogicCheckActor(LogicCheck logicCheck, PhysicalItem actor, final int lbxSourceIndex, final int lbxTypicalValuesIndex, final int lbxValueFromSensorIndex, 
			final String txtManualEntryContent) {
		super();
		this.logicCheck = logicCheck;
		this.actor = actor;
		
		this.txtManualEntryContent = txtManualEntryContent;	
		this.lbxSourceIndex = lbxSourceIndex;
		this.lbxValueFromSensorIndex = lbxValueFromSensorIndex;
		this.lbxTypicalValuesIndex = lbxTypicalValuesIndex;
		
		
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
		lbxSource.setSelectedIndex(lbxSourceIndex);
		lbxTypicalValues.setSelectedIndex(lbxTypicalValuesIndex);
		txtManualEntry.setValue(txtManualEntryContent);
			

	}
	
	@Override
	public void center()
	{
		super.center();
		if(layoutCreated == false) 
		{
		createBaseLayout();
		}
		lbxSource.setSelectedIndex(lbxSourceIndex);
		lbxTypicalValues.setSelectedIndex(lbxTypicalValuesIndex);
		txtManualEntry.setValue(txtManualEntryContent);
	
	}


	public void save()
	{
		lbxSourceIndex = lbxSource.getSelectedIndex();
		lbxTypicalValuesIndex = lbxTypicalValues.getSelectedIndex();
		txtManualEntryContent = txtManualEntry.getValue();
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
		Image tempImage = this.logicCheck.getImage();
		tempImage.setWidth("25px");
		imageHeader.add(tempImage);
		imageHeader.add(new Icon(IconType.CHEVRON_SIGN_RIGHT));
		imageHeader.add(this.actor.getImage());
		col0a.add(imageHeader);
		row0.add(col0a);
		Column col0b = new Column(4);	
		col0b.add(new HTML("<b>Define Connection between "+ logicCheck.getContentRepresentation() + " "
				+ "and "+ actor.getContentRepresentation()+"</b>"));
		row0.add(col0b);
		
		verticalPanel.add(row0);
		
        
        Row row1 = new Row();
		Column col1 = new Column(5);
		HTML headerHTML = new HTML("<br><br>"
				+ "You can define the condition under which the connection will be triggered based "
						+ "on the state of <b>"+ logicCheck.getContentRepresentation() + "</b>, "
				+ "and then define what data will be send to <b>" + actor.getContentRepresentation() + "</b>. This could be a static "
						+ "value defined in this dialog,"
				+ "or values dynamically pulled from either <b>"+logicCheck.getContentRepresentation()+"</b> or any other sensor you have access to."
						+ "<br><br>&nbsp;");
		col1.add(headerHTML);
		row1.add(col1);
		verticalPanel.add(row1);
		
		Row row2 = new Row();
		Column col2A = new Column(1);
		col2A.add(new HTML("Push Value:"));
		
		Column col2B = new Column(1);
		lbxSource = new ListBox();
		lbxSource.setWidth("100%");
		col2B.add(lbxSource);
		populateLbxSource(lbxSource);
		
		Column col2C = new Column(3);
		txtManualEntry = new TextBox();
		col2C.add(txtManualEntry);
				
		lbxTypicalValues = new ListBox();
		lbxTypicalValues.setWidth("100%");
		col2C.add(lbxTypicalValues);
		lbxTypicalValues.setVisible(false);
		
		row2.add(col2A);
		row2.add(col2B);
		row2.add(col2C);
		
		verticalPanel.add(row2);
		
		Row row4 = new Row();
		Column col4A = new Column(1,2);
		Button btnSave = new Button("Save");
		btnSave.setType(ButtonType.SUCCESS);
		col4A.add(btnSave);
		
		Column col4B = new Column(1);
		Button btnDelete = new Button("Delete");
		btnDelete.setType(ButtonType.DANGER);
		col4B.add(btnDelete);
		
		Column col4C = new Column(1);
		Button btnCancel = new Button("Cancel");
		btnCancel.setType(ButtonType.DEFAULT);
		col4C.add(btnCancel);
		
		row4.add(col4A);
		row4.add(col4B);
		row4.add(col4C);
		
		verticalPanel.add(row4);
		
		
		this.add(verticalPanel);
		
		// set the various handlers
		
		
		btnCancel.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				close();
			}
			
		});
		
		final ConnectorLogicCheckActor thisWidget = this;
		btnSave.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				//EventUtils.EVENT_BUS.fireEvent(new ConnectionSaveEvent(logicCheck, actor, thisWidget));
				save();
				close();
		}
			
		});
		
		lbxSource.addChangeHandler(new ChangeHandler(){

			@Override
			public void onChange(ChangeEvent event) {
				// TODO Auto-generated method stub
				
				if(lbxSource.getValue() == ActionSourceConfig.manual)
				{
					txtManualEntry.setVisible(true);
					lbxTypicalValues.setVisible(false);
					
				}
				
				
				if(lbxSource.getValue() == ActionSourceConfig.otherSensorValue)
				{
					txtManualEntry.setVisible(false);
					lbxTypicalValues.setVisible(false);
					
				}
				
				if(lbxSource.getValue() == ActionSourceConfig.typicalEntries)
				{
					txtManualEntry.setVisible(false);
					lbxTypicalValues.setVisible(true);
					
				}
				
			}
			
		});
		
		
	}
	
	private void populateLbxSource(final ListBox lbxSource)
	{
		lbxSource.addItem(ActionSourceConfig.manual);
		lbxSource.addItem(ActionSourceConfig.typicalEntries);
		
	}
	
	
	private void createOpenerWidget()
	{
		openingButton = new Button();
		openingButton.setBaseIcon(IconType.FILTER);
		
		openingButton.setStyleName("connbtn");
		

	
				
	}
	
	
	
	public Button getOpenerWidget()
	{
		return this.openingButton;
	}
	
	  public SerializableLogicConnector getSerializableRepresentation()
      {
		  
		RootPanel.get().add(new HTML("Getting Rep L2P..."));
		SerializableLogicConnector serializable = new SerializableLogicConnector();
      	serializable.setLeftID(this.logicCheck.getId());
      	serializable.setRightID(this.actor.getId());
      	serializable.setLbxSourceIndex(this.lbxSourceIndex);
		serializable.setLbxTypicalValuesIndex(this.lbxTypicalValuesIndex);
		serializable.setTxtManualEntryContent(this.txtManualEntryContent);
		serializable.setType(SharedConstants.CONL2P);
		RootPanel.get().add(new HTML("Succeeded!"));
		return serializable;
      	
      }
	  
	
	  
	  public LogicCheck getLogicCheck() {
			return logicCheck;
		}

		public PhysicalItem getActor() {
			return actor;
		}
						
		public String getSourceIndex() {
			return lbxSource.getValue();
		}
		
		public String getTypicalValueIndex() {
			return lbxTypicalValues.getValue();
		}
		
		public String getManualValue() {
			return txtManualEntry.getValue();
		}

	
}
