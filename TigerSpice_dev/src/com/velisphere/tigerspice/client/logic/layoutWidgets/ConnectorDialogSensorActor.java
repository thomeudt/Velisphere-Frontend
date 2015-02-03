package com.velisphere.tigerspice.client.logic.layoutWidgets;

import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.Row;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
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
         getElement().getStyle().setPadding(3, Unit.PX);
         //getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
         //getElement().getStyle().setBorderColor("#bbbbbb");
         //getElement().getStyle().setBorderWidth(1, Unit.PX);
         getElement().getStyle().setBackgroundColor("#ffffff");
		Row row1 = new Row();
		Column col1 = new Column(5);
		HTML headerHTML = new HTML("<b>Define Connection between "+ sensor.getContentRepresentation() + " "
				+ "and "+ actor.getContentRepresentation()+"</b><br><br>"
				+ "You can define the condition under which the connection will be triggered based "
						+ "on the state of <b>"+ sensor.getContentRepresentation() + "</b>, "
				+ "and then define what data will be send to <b>" + actor.getContentRepresentation() + "</b>. This could be a static "
						+ "value defined in this dialog,"
				+ "or values dynamically pulled from either <b>"+sensor.getContentRepresentation()+"</b> or any other sensor you have access to.");
		col1.add(headerHTML);
		row1.add(col1);
		this.add(row1);
		
	}
	
}
