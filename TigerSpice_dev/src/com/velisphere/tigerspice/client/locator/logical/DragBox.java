package com.velisphere.tigerspice.client.locator.logical;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasAllTouchHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.TouchCancelEvent;
import com.google.gwt.event.dom.client.TouchCancelHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.endpointclasses.EPCService;
import com.velisphere.tigerspice.client.endpointclasses.EPCServiceAsync;
import com.velisphere.tigerspice.client.helper.widgets.Circle;
import com.velisphere.tigerspice.client.helper.widgets.RenderingContext;
import com.velisphere.tigerspice.shared.EPCData;

public class DragBox extends FocusPanel implements HasAllTouchHandlers {

	public static String identifier = "DragBox";
	String content;
	Image epcImage;
	String endpointID;

	public DragBox(final String endpointID, String text, String epcID) {
		super();

		this.endpointID = endpointID;
		removeDefaultMouseDown();
		
		HorizontalPanel hPanel = new HorizontalPanel();
		
		if (text.length() > 25)
			this.content = text.substring(0, 24) + "(...)";
		else
			this.content = text;
		
		this.content = text;
		
		this.getElement().getStyle().setCursor(Cursor.POINTER);
		
		this.setSize("130px", "35px");
		Label label = new Label();
		label.setText(text);
		epcImage = new Image();
		hPanel.add(epcImage);
		hPanel.add(label);
		this.add(hPanel);
		addImage(epcID);
		
		getElement().getStyle().setBackgroundColor("aliceblue");
		addStyleName("roundborders");

		getElement().getStyle().setPadding(10, Unit.PX);
		getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
		getElement().getStyle().setBorderWidth(1, Unit.PX);
		
		
		
		this.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				AppController.openEndpoint(endpointID);
			
			}
			
		});

	}

	private void addImage(String epcID) {
		EPCServiceAsync epcService = GWT.create(EPCService.class);
		
		epcService.getEndpointClassForEndpointClassID(epcID,
				new AsyncCallback<EPCData>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method
						// stub
						
					}

					@Override
					public void onSuccess(EPCData result) {
						// TODO Auto-generated method
						// stub
						epcImage.setUrl(result.endpointclassImageURL);
						epcImage.setWidth("40px");
						epcImage.setHeight("30px");
						

					}

				});

	}

	@Override
	public HandlerRegistration addTouchStartHandler(TouchStartHandler handler) {
		return addDomHandler(handler, TouchStartEvent.getType());
	}

	@Override
	public HandlerRegistration addTouchEndHandler(TouchEndHandler handler) {
		return addDomHandler(handler, TouchEndEvent.getType());
	}

	@Override
	public HandlerRegistration addTouchMoveHandler(TouchMoveHandler handler) {
		return addDomHandler(handler, TouchMoveEvent.getType());
	}

	@Override
	public HandlerRegistration addTouchCancelHandler(TouchCancelHandler handler) {
		return addDomHandler(handler, TouchCancelEvent.getType());
	}

	
	public String getType() {
		return this.identifier;
	}

	public String getContentRepresentation() {
		return this.content;
	}

	public void setContentRepresentation(String contentRepresentation) {
		this.content = contentRepresentation;
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