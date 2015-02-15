package com.velisphere.tigerspice.client.logic.draggables;

import com.github.gwtbootstrap.client.ui.Image;
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
import com.google.gwt.user.client.ui.FocusPanel;
import com.velisphere.tigerspice.client.images.Images;
import com.velisphere.tigerspice.shared.SerializableLogicLogicCheck;
import com.velisphere.tigerspice.shared.SerializableLogicPhysicalItem;

public class LogicCheck extends FocusPanel implements HasAllTouchHandlers {

	String content;
	LinkCreator dragPointWidget;
	int sourceCount;
	int xPos;
	int yPos;
	String id;
	boolean or;
	boolean and;
	

	public LogicCheck()
	{
		super();
		sourceCount = 0;
		removeDefaultMouseDown();
		this.id = Document.get().createUniqueId();
		
		content = "Logic Check";
		  this.setStyleName("wellwhite");
          
          
          getElement().getStyle().setPadding(1, Unit.PX);
          //getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
          //getElement().getStyle().setBorderColor("#bbbbbb");
          //getElement().getStyle().setBorderWidth(1, Unit.PX);
          getElement().getStyle().setBackgroundColor("#ffffff");
          
          this.getElement().getStyle().setCursor(Cursor.POINTER);
          
	
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
    
    private void removeDefaultMouseDown()
	{
    	
		this.addMouseDownHandler(new MouseDownHandler(){

			@Override
			public void onMouseDown(MouseDownEvent event) {
				event.preventDefault();
			}
			
		});
		
	}
    
    public Image getImage()
    {
    	Image tempImage = new Image();
    	tempImage.setUrl(Images.INSTANCE.and_icon().getSafeUri());
    	tempImage.setWidth("100%");
    	return tempImage;
    }

    public String getContentRepresentation() {
        return this.content;
    }
    
    public SerializableLogicLogicCheck getSerializableRepresentation()
    {
    	SerializableLogicLogicCheck serializable = new SerializableLogicLogicCheck();
    	serializable.setAnd(this.and);
    	serializable.setContent(this.content);
    	serializable.setId(this.id);
    	serializable.setOr(this.or);
    	serializable.setSourceCount(this.sourceCount);
    	serializable.setxPos(this.xPos);
    	serializable.setyPos(this.yPos);
    	return serializable;
    	
    }
    
    public void setDragPointWidget(LinkCreator dragPointWidget)
    {
    	this.dragPointWidget = dragPointWidget;
    }
    
    public LinkCreator getDragPointWidget()
    {
    	return this.dragPointWidget;
    }
    
    

    public int getSourceCount() {
		return sourceCount;
	}


	public void setSourceCount(int sourceCount) {
		this.sourceCount = sourceCount;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
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


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}

	public boolean isOr() {
		return or;
	}


	public void setOr(boolean or) {
		this.or = or;
	}


	public boolean isAnd() {
		return and;
	}


	public void setAnd(boolean and) {
		this.and = and;
	}


	
}
