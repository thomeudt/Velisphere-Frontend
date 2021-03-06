package com.velisphere.tigerspice.client.logic.draggables;

import java.util.LinkedList;

import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Unit;
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
import com.velisphere.tigerspice.client.endpointclasses.EPCService;
import com.velisphere.tigerspice.client.endpointclasses.EPCServiceAsync;
import com.velisphere.tigerspice.client.helper.UuidService;
import com.velisphere.tigerspice.client.helper.UuidServiceAsync;
import com.velisphere.tigerspice.client.logic.controllers.InCanvasLinkDropController;
import com.velisphere.tigerspice.client.logic.draggables.LinkCreator;
import com.velisphere.tigerspice.shared.EPCData;
import com.velisphere.tigerspice.shared.LinkedPair;
import com.velisphere.tigerspice.shared.SerializableLogicPhysicalItem;


	
public class PhysicalItem extends FocusPanel implements HasAllTouchHandlers {

        public static String identifier = "diagramlabel";
        String endpointName;
        String content;	
        String propertyID;
    	String propertyClassID;
    	String endpointClassID;
   		String endpointID;
    	byte isActor;
    	byte isSensor;
    	LinkCreator dragPointWidget;
    	int xPos;
    	int yPos;
    	String uuid;
    	Image imgEpcImage;
    	InCanvasLinkDropController linkDropController;

        
        
        public PhysicalItem(String text, String endpointName, String propertyID, String endpointID, String endpointClassID, String propertyClassID, byte isSensor, byte isActor){
                super();
                this.content = text;
                this.endpointName = endpointName;
                this.propertyID = propertyID;
        		this.propertyClassID = propertyClassID;
        		this.endpointClassID = endpointClassID;
        		this.endpointID = endpointID;
        		this.isActor = isActor;
        		this.isSensor = isSensor;
        		createUiItemUUID();
        		buildLayout();
                removeDefaultMouseDown();
        }
        
        public PhysicalItem(String uuid, String text, String endpointName, String propertyID, String endpointID, String endpointClassID, String propertyClassID, byte isSensor, byte isActor){
            super();
            this.content = text;
            this.endpointName = endpointName;
            this.propertyID = propertyID;
    		this.propertyClassID = propertyClassID;
    		this.endpointClassID = endpointClassID;
    		this.endpointID = endpointID;
    		this.isActor = isActor;
    		this.isSensor = isSensor;
    		this.uuid = uuid;
    		buildLayout();
            removeDefaultMouseDown();
    }

        protected void createUiItemUUID()
    	{
    		UuidServiceAsync uuidService = GWT
    				.create(UuidService.class);
    		
    		uuidService.getUuid(new AsyncCallback<String>(){

    			@Override
    			public void onFailure(Throwable caught) {
    				// TODO Auto-generated method stub
    				
    			}

    			@Override
    			public void onSuccess(String result) {
    				// TODO Auto-generated method stub
    				uuid = result;
    			}
    			
    		});
    		
    	}
        
     
        private void buildLayout()
        {
        	HorizontalPanel h = new HorizontalPanel();
    
        	if (this.isSensor != 0){
            	h.add(new Icon(IconType.RSS));
            }
            
            if (this.isActor != 0){
            	h.add(new Icon(IconType.COGS));
            }
            
        	
        	h.add(new HTML("&nbsp;"));
            
        	imgEpcImage = new Image();
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
        
       
        public void setDragPointWidget(LinkCreator dragPointWidget)
        {
        	this.dragPointWidget = dragPointWidget;
        }
        
        public LinkCreator getDragPointWidget()
        {
        	return this.dragPointWidget;
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

      
        public String getContentRepresentation() {
                return this.content;
        }

        public Byte getIsActor()
        {
        	return this.isActor;
        }
        
        public Byte getIsSensor()
        {
        	return this.isSensor;
        }
        
        public String getPropertyClassID()
        {
        	return this.propertyClassID;
        }
        
        public String getPropertyID()
        {
        	return this.propertyID;
        }
        
        public String getEndpointID()
        {
        	return this.endpointID;
        }
        
        public Image getImage()
        {
        	Image tempImage = new Image();
        	tempImage.setUrl(this.imgEpcImage.getUrl());
        	return tempImage;
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
        
        public SerializableLogicPhysicalItem getSerializableRepresentation()
        {
        	SerializableLogicPhysicalItem serializable = new SerializableLogicPhysicalItem();
        	serializable.setContent(this.content);
        	serializable.setEndpointClassID(this.endpointClassID);
        	serializable.setEndpointID(this.endpointID);
        	serializable.setEndpointName(this.endpointName);
        	serializable.setIsActor(this.isActor);
        	serializable.setIsSensor(this.isSensor);
        	serializable.setPropertyClassID(this.propertyClassID);
        	serializable.setPropertyID(this.propertyID);
        	serializable.setId(this.uuid);
        	serializable.setxPos(this.xPos);
        	serializable.setyPos(this.yPos);
        	return serializable;
        	
        }
      
        public int getxPos() {
			return xPos;
		}


		public void setxPos(int xPos) {
			this.xPos = xPos;
		}


		public int getyPos() {
			return yPos;
		}


		public void setyPos(int yPos) {
			this.yPos = yPos;
		}
		
		public String getEndpointClassID() {
			return endpointClassID;
		}


		public void setEndpointClassID(String endpointClassID) {
			this.endpointClassID = endpointClassID;
		}
		
		public String getUUID() {
			return uuid;
		}

		public void setUUID(String id) {
			this.uuid = id;
		}
		
		public InCanvasLinkDropController getLinkDropController() {
			return linkDropController;
		}


		public void setLinkDropController(InCanvasLinkDropController linkDropController) {
			this.linkDropController = linkDropController;
		}





}
