package com.velisphere.tigerspice.client.logic.layoutWidgets;

import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.Row;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.velisphere.tigerspice.client.logic.draggables.CanvasLabel;

public class ConnectorDialogSensorActor extends PopupPanel {

	CanvasLabel actor;
	CanvasLabel sensor;
	
	public ConnectorDialogSensorActor (CanvasLabel sensor, CanvasLabel actor)
	{
		super();
		this.sensor = sensor;
		this.actor = actor;
		createBaseLayout();
		
		
	}
	
	private void createBaseLayout()
	{
		 this.setStyleName("wellwhite");
         getElement().getStyle().setPadding(10, Unit.PX);
         //getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
         //getElement().getStyle().setBorderColor("#bbbbbb");
         //getElement().getStyle().setBorderWidth(1, Unit.PX);
         getElement().getStyle().setBackgroundColor("#ffffff");
	
        
        VerticalPanel verticalPanel = new VerticalPanel();
        
         
        Row row1 = new Row();
		Column col1 = new Column(5);
		HTML headerHTML = new HTML("<b>Define Connection between "+ sensor.getContentRepresentation() + " "
				+ "and "+ actor.getContentRepresentation()+"</b><br><br>"
				+ "You can define the condition under which the connection will be triggered based "
						+ "on the state of <b>"+ sensor.getContentRepresentation() + "</b>, "
				+ "and then define what data will be send to <b>" + actor.getContentRepresentation() + "</b>. This could be a static "
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
		ListBox lbxOperator = new ListBox();
		lbxOperator.setWidth("100%");
		col2B.add(lbxOperator);
		
		Column col2C = new Column(3);
		TextBox txtCheckValue = new TextBox();
		col2C.add(txtCheckValue);
		
		row2.add(col2A);
		row2.add(col2B);
		row2.add(col2C);
		
		verticalPanel.add(row2);
		
		Row row3 = new Row();
		Column col3A = new Column(1);
		col3A.add(new HTML("Push Value:"));
		
		Column col3B = new Column(1);
		ListBox lbxSource = new ListBox();
		lbxSource.setWidth("100%");
		col3B.add(lbxSource);
		
		Column col3C = new Column(3);
		TextBox dummy = new TextBox();
		col3C.add(dummy);
		
		row3.add(col3A);
		row3.add(col3B);
		row3.add(col3C);
		
		verticalPanel.add(row3);
		
		this.add(verticalPanel);
		
		
		
	}
	
}
