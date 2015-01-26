package com.velisphere.tigerspice.client.logic.widgets;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.orange.links.client.DiagramController;
import com.orange.links.client.connection.Connection;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.appcontroller.SessionHelper;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.endpoints.EndpointServiceAsync;
import com.velisphere.tigerspice.client.event.EventUtils;
import com.velisphere.tigerspice.client.event.FilterAppliedEvent;
import com.velisphere.tigerspice.client.event.FilterAppliedEventHandler;
import com.velisphere.tigerspice.client.helper.widgets.FilterSphereEndpointWidget;
import com.velisphere.tigerspice.client.locator.logical.LabelWithMenu;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.LogicLinkTargetData;

public class CheckPathCanvas extends Composite {

	HorizontalPanel pWidget;
	ScrollPanel scrollWidget;
	DiagramController controller;
	HashMap<String, LabelWithMenu> labelDirectory;
	HashMap<String, LinkedList<String>> linkDirectory;
	AbsolutePanel diagramContainer;
	PickupDragController dragController;
	DropController dropController;
	
	
	// general constructor showing all endpoints per default
	
	public CheckPathCanvas() {

		pWidget = new HorizontalPanel();
		diagramContainer = new AbsolutePanel();
		
		initWidget(pWidget);
		controller = createDiagramController();
		dragController = new PickupDragController(
				controller.getView(), true);
		controller.registerDragController(dragController);
		pWidget.add(diagramContainer);
		diagramContainer.add(controller.getView());
		dropController = new CanvasDropController(controller.getView());
		
	
		
	}
	
	private DiagramController createDiagramController() {
		DiagramController controller = new DiagramController(800, 500);
		controller.getView().addStyleName("span8");
		controller.showGrid(true); // Display a background grid
		return controller;
	}
	
	public DropController getDropController()
	{
		return this.dropController;
	}
	
	
}

