package com.velisphere.tigerspice.client.spheres.widgets;


import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasAllTouchHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.shared.EndpointData;

public class EndpointDataLabel extends FocusPanel implements HasAllTouchHandlers {

	EndpointData endpointData;
	String text;
	
	
	public EndpointData getEndpointData() {
		return endpointData;
	}

	public void setEndpointData(EndpointData endpointData) {
		this.endpointData = endpointData;
	}
	
	public EndpointDataLabel(EndpointDataLabel clone)
	{
		super();
		this.text = clone.text;
		this.endpointData = clone.endpointData;
		setupLabel();
	}
	
	public EndpointDataLabel(String text, final EndpointData endpointData)
	{
		super();
		this.text = text;
		this.endpointData = endpointData;
		setupLabel();
		
	}
	
	private void setupLabel()
	{
		Label label = new Label(text);
		this.add(label);
		removeDefaultMouseDown();
		
		
		this.getElement().getStyle().setCursor(Cursor.POINTER);
		
		
		label.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				AppController.openEndpoint(endpointData.getId());
				RootPanel.get().add(new HTML("CLICK"));
			
			}
			
		});
		

	}
	
	private void removeDefaultMouseDown()
	{
		this.addMouseDownHandler(new MouseDownHandler(){

			@Override
			public void onMouseDown(MouseDownEvent event) {
				event.preventDefault();
			}
			
		});
		
	}
	

	
	
}
