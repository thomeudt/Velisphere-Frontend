package com.velisphere.tigerspice.client.logic.widgets;

import com.allen_sauer.gwt.dnd.client.util.DOMUtil;
import com.allen_sauer.gwt.dnd.client.util.WidgetLocation;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
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
import com.google.gwt.user.client.ui.Widget;
import com.orange.links.client.save.IsDiagramSerializable;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.endpointclasses.EPCService;
import com.velisphere.tigerspice.client.endpointclasses.EPCServiceAsync;
import com.velisphere.tigerspice.shared.EPCData;


	
public class CanvasLabel extends FocusPanel implements HasAllTouchHandlers,IsDiagramSerializable{

        public static String identifier = "diagramlabel";
        String endpointName;
        String content;	
        String propertyID;
    	String propertyClassID;
    	String endpointClassID;
    	String endpointID;
    	byte isActor;
    	byte isSensor;
    	Widget callManagementWidget;
        
        
        public CanvasLabel(String text, String endpointName, String propertyID, String endpointID, String endpointClassID, String propertyClassID, byte isSensor, byte isActor){
                super();
                this.content = text;
                this.endpointName = endpointName;
                this.propertyID = propertyID;
        		this.propertyClassID = propertyClassID;
        		this.endpointClassID = endpointClassID;
        		this.endpointID = endpointID;
        		this.isActor = isActor;
        		this.isSensor = isSensor;
                
                HorizontalPanel h = new HorizontalPanel();
                
                Image imgEpcImage = new Image();
                h.add(imgEpcImage);
                getEndpointClassImage(imgEpcImage);
                
                h.add(new HTML(this.endpointName + "<br><b>"+this.content+"</b>"));
                
              
                
                
                this.add(h);
                this.setStyleName("wellwhite");
                
            
                getElement().getStyle().setPadding(3, Unit.PX);
                //getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
                //getElement().getStyle().setBorderColor("#bbbbbb");
                //getElement().getStyle().setBorderWidth(1, Unit.PX);
                getElement().getStyle().setBackgroundColor("#ffffff");
                
                removeDefaultMouseDown();
                
                this.getElement().getStyle().setCursor(Cursor.POINTER);
        }
        
       
        
        private void getEndpointClassImage(final Image imgEpcImage)
        {
        	EPCServiceAsync epcService = GWT
					.create(EPCService.class);
        	
        	epcService
			.getEndpointClassForEndpointClassID(
					endpointClassID,
					new AsyncCallback<EPCData>() {

						@Override
						public void onFailure(
								Throwable caught) {
							// TODO Auto-generated method
							// stub
							
						}

						@Override
						public void onSuccess(EPCData result) {
							// TODO Auto-generated method
							// stub

							
							imgEpcImage.setUrl(result.endpointclassImageURL);
							imgEpcImage.setWidth("30px");
							
						

																			
							
						}

					});

        }
        
       
        public void setCallManagementWidget(Widget callManagementWidget)
        {
        	this.callManagementWidget = callManagementWidget;
        }
        
        public Widget getCallManagementWidget()
        {
        	return this.callManagementWidget;
        }
        
        @Override
        public HandlerRegistration addTouchStartHandler(TouchStartHandler handler){
                return addDomHandler(handler, TouchStartEvent.getType());
        }
        
        @Override
        public HandlerRegistration addTouchEndHandler(TouchEndHandler handler){
                return addDomHandler(handler, TouchEndEvent.getType());
        }
        
        @Override
        public HandlerRegistration addTouchMoveHandler(TouchMoveHandler handler){
                return addDomHandler(handler, TouchMoveEvent.getType());
        }
        
        @Override
        public HandlerRegistration addTouchCancelHandler(TouchCancelHandler handler){
                return addDomHandler(handler, TouchCancelEvent.getType());
        }

        @Override
        public String getType() {
                return this.identifier;
        }

        @Override
        public String getContentRepresentation() {
                return this.content;
        }

        @Override
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
