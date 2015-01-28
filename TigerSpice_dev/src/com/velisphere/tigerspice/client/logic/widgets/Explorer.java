/*******************************************************************************
 * CONFIDENTIAL INFORMATION
 *  __________________
 *  
 *   Copyright (C) 2014 Thorsten Meudt / Connected Things Lab
 *   All Rights Reserved.
 *  
 *  NOTICE:  All information contained herein is, and remains
 *  the property of Thorsten Meudt and its suppliers,
 *  if any.  The intellectual and technical concepts contained
 *  herein are proprietary to Thorsten Meudt
 *  and its suppliers and may be covered by Patents,
 *  patents in process, and are protected by trade secret or copyright law.
 *  Dissemination of this information or reproduction of this material
 *  is strictly forbidden unless prior written permission is obtained
 *  from Thorsten Meudt.
 ******************************************************************************/
package com.velisphere.tigerspice.client.logic.widgets;

import java.util.Iterator;
import java.util.LinkedList;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.github.gwtbootstrap.client.ui.Accordion;
import com.github.gwtbootstrap.client.ui.AccordionGroup;
import com.github.gwtbootstrap.client.ui.CheckBox;
import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.Row;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.github.gwtbootstrap.client.ui.resources.Bootstrap;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.CustomScrollPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.velisphere.tigerspice.client.appcontroller.SessionHelper;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.endpoints.EndpointServiceAsync;
import com.velisphere.tigerspice.client.event.EventUtils;
import com.velisphere.tigerspice.client.event.SessionVerifiedEvent;
import com.velisphere.tigerspice.client.event.SessionVerifiedEventHandler;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.properties.PropertyService;
import com.velisphere.tigerspice.client.properties.PropertyServiceAsync;
import com.velisphere.tigerspice.client.spheres.SphereService;
import com.velisphere.tigerspice.client.spheres.SphereServiceAsync;
import com.velisphere.tigerspice.client.users.UserService;
import com.velisphere.tigerspice.client.users.UserServiceAsync;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.PropertyData;
import com.velisphere.tigerspice.shared.SphereData;

public class Explorer extends Composite {

	String userID;
	ListBox lbxSpheres;
	ListBox lbxEndpoints;
	ListBox lbxSensors;
	ListBox lbxActors;
	AbsolutePanel container;
	PickupDragController dragController1;
	PickupDragController dragController2;

	DraggableListBox dbxSensors;
	DraggableListBox dbxActors;
	CustomCanvas checkPathCanvas;
	
	private static final String CSS_DRAGDROPWIDGET = "DragDropWidget";

	
	public Explorer(String userID, CustomCanvas checkPathCanvas) {
		this.userID = userID;
		this.checkPathCanvas = checkPathCanvas;
		container = new AbsolutePanel();		
		initWidget(container);
		
		//dragController = new PickupDragController(RootPanel.get(), true);
		createBaseLayout();
		getSpheres();
		addListChangeHandler();
		
		
	}
	
	private void createBaseLayout()
	{
		Row row0 = new Row();
		Column col0 = new Column(2);
		lbxSpheres = new ListBox();
		lbxSpheres.setWidth("100%");
		col0.add(lbxSpheres);
		row0.add(col0);
		container.add(row0);
		
		Row row1 = new Row();
		Column col1 = new Column(2);
		lbxEndpoints = new ListBox();
		lbxEndpoints.setWidth("100%");
		col1.add(lbxEndpoints);
		row1.add(col1);
		container.add(row1);
		
		Row row2 = new Row();
		Column col2 = new Column(2);
		
		dragController1 = new ListBoxDragController(RootPanel.get());
		dragController2 = new ListBoxDragController(RootPanel.get());
		CustomScrollPanel scrollPanelSensors = new CustomScrollPanel();
		
		dbxSensors = new DraggableListBox(dragController1, 64);
		scrollPanelSensors.add(dbxSensors);
		scrollPanelSensors.setHeight("100px");
		scrollPanelSensors.addStyleName(CSS_DRAGDROPWIDGET);
		scrollPanelSensors.addStyleName("wellwhite");
		
		scrollPanelSensors.setHorizontalScrollbar(null, 0);
		
		
		dragController1.addDragHandler(new DragHandler(){

			@Override
			public void onDragEnd(DragEndEvent event) {
				// TODO Auto-generated method stub
				RootPanel.get().add(new HTML("X Coord Drop: " + String.valueOf(event.getContext().mouseX)));
				RootPanel.get().add(new HTML("Y Coord Drop: " + String.valueOf(event.getContext().mouseY)));
	
				RootPanel.get().add(new HTML("Widget Class " + event.getContext().selectedWidgets.get(0).getClass()));
				ExplorerLabel current = (ExplorerLabel) event.getContext().selectedWidgets.get(0);
				RootPanel.get().add(new HTML("Widget Content " + current.getText()));
				
			}

			@Override
			public void onDragStart(DragStartEvent event) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPreviewDragEnd(DragEndEvent event)
					throws VetoDragException {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPreviewDragStart(DragStartEvent event)
					throws VetoDragException {
				// TODO Auto-generated method stub
				
			}

						
			
		});
		
		dragController1.registerDropController(this.checkPathCanvas.getToCanvasDropController());

		
		HorizontalPanel sensorHeader = new HorizontalPanel();
		sensorHeader.add(new Icon(IconType.RSS));
		sensorHeader.add(new HTML("<b>&nbsp;&nbsp;Sensors</b><br>"));
		col2.add(sensorHeader);
		col2.add(scrollPanelSensors);

		row2.add(col2);
		container.add(row2);

		Row row3= new Row();

		Column col3 = new Column(2);
		
		
		CustomScrollPanel scrollPanelActors = new CustomScrollPanel();
	
		dbxActors = new DraggableListBox(dragController2, 64);
		scrollPanelActors.add(dbxActors);
		scrollPanelActors.setHeight("100px");
		scrollPanelActors.addStyleName(CSS_DRAGDROPWIDGET);
		scrollPanelActors.addStyleName("wellwhite");
		
		scrollPanelActors.setHorizontalScrollbar(null, 0);
		
		HorizontalPanel actorHeader = new HorizontalPanel();
		actorHeader.add(new Icon(IconType.COGS));
		actorHeader.add(new HTML("<b>&nbsp;&nbsp;Actors</b><br>"));
		col3.add(actorHeader);
		col3.add(scrollPanelActors);
		
		row3.add(col3);
		container.add(row3);
		
	}

	
	
	private void getSpheres()
	{
		SphereServiceAsync sphereService = GWT
				.create(SphereService.class);
		
		// own spheres
		
		sphereService.getAllSpheresForUserID(this.userID, new AsyncCallback<LinkedList<SphereData>>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(LinkedList<SphereData> result) {
				// TODO Auto-generated method stub
				
				Iterator<SphereData> it = result.iterator();
				while (it.hasNext()){
					SphereData current = it.next();
					lbxSpheres.addItem(current.sphereName, current.sphereId);
				}
				
				if (lbxSpheres.getValue() != null)
				{
					getEndpoints(lbxSpheres.getValue());
				}
				
			}
			
		});

	}
	
	private void addListChangeHandler()
	{
		lbxSpheres.addChangeHandler(new ChangeHandler(){

			@Override
			public void onChange(ChangeEvent event) {
				// TODO Auto-generated method stub
			
				getEndpoints(lbxSpheres.getValue());
			}
		});
		
		lbxEndpoints.addChangeHandler(new ChangeHandler(){

			@Override
			public void onChange(ChangeEvent event) {
				// TODO Auto-generated method stub
			
				getSensors(lbxEndpoints.getValue(), lbxEndpoints.getItemText(lbxEndpoints.getSelectedIndex()));
				getActors(lbxEndpoints.getValue(), lbxEndpoints.getItemText(lbxEndpoints.getSelectedIndex()));
			}
			
		});

	}
	
	private void getEndpoints(String sphereID)
	{
		EndpointServiceAsync endpointService = GWT
				.create(EndpointService.class);
		
		// own spheres
		
		lbxEndpoints.clear();
		
		endpointService.getEndpointsForSphere(sphereID, new AsyncCallback<LinkedList<EndpointData>>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(LinkedList<EndpointData> result) {
				// TODO Auto-generated method stub
				
				Iterator<EndpointData> it = result.iterator();
				while (it.hasNext()){
					EndpointData current = it.next();
					lbxEndpoints.addItem(current.getName(), current.getId());
				}
				
				if (lbxEndpoints.getValue() != null)
				{
					getSensors(lbxEndpoints.getValue(), lbxEndpoints.getItemText(lbxEndpoints.getSelectedIndex()));
					getActors(lbxEndpoints.getValue(), lbxEndpoints.getItemText(lbxEndpoints.getSelectedIndex()));
				}
			}
			
		});

	}
	
	
	
	private void getSensors(final String endpointID, final String endpointName)
	{
		PropertyServiceAsync propertyService = GWT
				.create(PropertyService.class);
		
		
		dbxSensors.clear();
		
		propertyService.getSensorPropertiesForEndpointID(endpointID, new AsyncCallback<LinkedList<PropertyData>>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(LinkedList<PropertyData> result) {
				// TODO Auto-generated method stub
				
				Iterator<PropertyData> it = result.iterator();
				while (it.hasNext()){
					PropertyData current = it.next();
					
				
					dbxSensors.add(current.getName(), endpointName, container.getOffsetWidth()+"px", current.getPropertyId(), endpointID, current.getEndpointclassId(), current.getPropertyclassId(), current.isSensor, current.isActor);
				}
				
			}
			
		});

	}

	
	private void getActors(final String endpointID, final String endpointName)
	{
		PropertyServiceAsync propertyService = GWT
				.create(PropertyService.class);
		
		dbxActors.clear();
		
		propertyService.getActorPropertiesForEndpointID(endpointID, new AsyncCallback<LinkedList<PropertyData>>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(LinkedList<PropertyData> result) {
				// TODO Auto-generated method stub
				
				Iterator<PropertyData> it = result.iterator();
				while (it.hasNext()){
					PropertyData current = it.next();
					dbxActors.add(current.getName(), endpointName, container.getOffsetWidth()+"px", current.getPropertyId(), endpointID, current.getEndpointclassId(), current.getPropertyclassId(), current.isSensor, current.isActor);
				}
				
			}
			
		});

	}
	
	
	
}
