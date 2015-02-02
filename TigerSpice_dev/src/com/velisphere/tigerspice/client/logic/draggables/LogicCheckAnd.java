package com.velisphere.tigerspice.client.logic.draggables;

import com.github.gwtbootstrap.client.ui.Image;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasAllTouchHandlers;
import com.google.gwt.user.client.ui.FocusPanel;
import com.velisphere.tigerspice.client.images.Images;

public class LogicCheckAnd extends FocusPanel implements HasAllTouchHandlers {

	
	public LogicCheckAnd()
	{
		super();
		Image andIcon = new Image();
		andIcon.setResource(Images.INSTANCE.and_icon());
		andIcon.setTitle("This logic check is true if all checks linked to this logic check are true");
		this.add(andIcon);
		
		  this.setStyleName("wellwhite");
          
          
          getElement().getStyle().setPadding(1, Unit.PX);
          //getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
          //getElement().getStyle().setBorderColor("#bbbbbb");
          //getElement().getStyle().setBorderWidth(1, Unit.PX);
          getElement().getStyle().setBackgroundColor("#ffffff");
          
	
	}
	
}
