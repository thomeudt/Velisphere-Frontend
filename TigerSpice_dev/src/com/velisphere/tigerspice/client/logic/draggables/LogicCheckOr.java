package com.velisphere.tigerspice.client.logic.draggables;

import com.github.gwtbootstrap.client.ui.Image;
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

public class LogicCheckOr extends LogicCheck {

	
	public LogicCheckOr()
	{
		//super();
	
		content = "Logic Check OR";
		setOr(true);
		Image andIcon = new Image();
		andIcon.setResource(Images.INSTANCE.or_icon());
		andIcon.setTitle("This logic check is true if all checks linked to this logic check are true");
		this.add(andIcon);
		
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

   
	
}
