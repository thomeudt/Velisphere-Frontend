package com.velisphere.tigerspice.client.logic.widgets;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.orange.links.client.event.TieLinkEvent;
import com.orange.links.client.event.TieLinkHandler;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.endpointclasses.EPCService;
import com.velisphere.tigerspice.client.endpointclasses.EPCServiceAsync;
import com.velisphere.tigerspice.client.event.DraggedToCanvasEvent;
import com.velisphere.tigerspice.client.event.DraggedToCanvasEventHandler;
import com.velisphere.tigerspice.client.event.EventUtils;

public class CustomCanvas extends Composite {

	Context2d context;
	
	ToCanvasDropController toCanvasDropController;
	InCanvasDropController inCanvasDropController;
	PickupDragController dragController;
	
	public CustomCanvas()
	{
		
		AbsolutePanel a = new AbsolutePanel();
		
		Canvas c = Canvas.createIfSupported();
		initWidget(a);
		
		
		c.setWidth("99%");
		c.setHeight("100%");
		c.addStyleName("wellsilver");
		a.add(c.asWidget());
		a.add(new HTML("<b>Logic Canvas</b> Drag sensors and actors here to build your logic."), 5, 5);
		
		this.context = c.getContext2d();
		/*
		context.setFillStyle(CssColor.make("grey"));
		context.beginPath();
		context.setLineWidth(5);
		context.arc(50, 50,35, 0, 320);
		context.closePath();
		context.fill();
		*/
		
		
		dragController = new PickupDragController(a, false);
		toCanvasDropController = new ToCanvasDropController(a);
		setDraggedToCanvasEventListener(a);
			
	}

	public DropController getToCanvasDropController()
	{
		
		return this.toCanvasDropController;
	}
	
	
	private void setDraggedToCanvasEventListener(final AbsolutePanel logicPanel) {
		HandlerRegistration draggedToCanvasHandler;
		draggedToCanvasHandler = EventUtils.EVENT_BUS.addHandler(
				DraggedToCanvasEvent.TYPE, new DraggedToCanvasEventHandler() {

					@Override
					public void onDraggedToCanvas(
							DraggedToCanvasEvent draggedToCanvasEvent) {

						
						// TODO Auto-generated method stub

						final ExplorerLabel current = (ExplorerLabel) draggedToCanvasEvent
								.getContext().selectedWidgets.get(0);

						RootPanel.get().add(
								new HTML("***** EVENT: dropped "
										+ current.getText() + " at X:"
										+ draggedToCanvasEvent.getTargetX()
										+ " Y:"
										+ draggedToCanvasEvent.getTargetY()));

						HTML h = new HTML(current.getText());
						CanvasLabel a = new CanvasLabel(current.getText(), current.getEndpointName(), current.getPropertyID(), current.getEndpointID(), current.getEndpointClassID(), current.getPropertyClassID(), current.getIsSensor(), current.getIsActor());
						
						logicPanel.add(a,
								draggedToCanvasEvent.getTargetX(),
								draggedToCanvasEvent.getTargetY());
						dragController.makeDraggable(a);
						
						inCanvasDropController = new InCanvasDropController(logicPanel);
						dragController.registerDropController(inCanvasDropController);

						a.addMouseOverHandler(new MouseOverHandler(){


							
							@Override
							public void onMouseOver(MouseOverEvent event) {
								// TODO Auto-generated method stub
								AppController.openEndpoint(current.getEndpointID());
							}
		                	
		                });
		                
						
						 
					}
					

				});

	}

}
