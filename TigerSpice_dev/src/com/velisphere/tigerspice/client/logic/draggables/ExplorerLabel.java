package com.velisphere.tigerspice.client.logic.draggables;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class ExplorerLabel extends Label {

	String propertyID;
	String propertyClassID;
	String endpointClassID;
	String endpointID;
	String endpointName;
	byte isActor;
	byte isSensor;
	
	
	public ExplorerLabel(String text, String endpointName, String propertyID, String endpointID, String endpointClassID, String propertyClassID, byte isSensor, byte isActor)
	{
		super(text);
		this.endpointName = endpointName;
		this.propertyID = propertyID;
		this.propertyClassID = propertyClassID;
		this.endpointClassID = endpointClassID;
		this.endpointID = endpointID;
		this.isActor = isActor;
		this.isSensor = isSensor;
		removeDefaultMouseDown();
		
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
	
	public String getPropertyID()
	{
		return this.propertyID;
	}
	
	public String getEndpointID()
	{
		return this.endpointID;
	}
	
	public String getEndpointName()
	{
		return this.endpointName;
	}
	
	public String getEndpointClassID()
	{
		return this.endpointClassID;
	}
	
	public String getPropertyClassID()
	{
		return this.propertyClassID;
	}
	public byte getIsActor()
	{
		return this.isActor;
	}
	public byte getIsSensor()
	{
		return this.isSensor;
	}
}
