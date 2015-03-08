package com.velisphere.tigerspice.client.logic.draggables;

import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.constants.IconSize;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasAllTouchHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.velisphere.tigerspice.client.helper.UuidService;
import com.velisphere.tigerspice.client.helper.UuidServiceAsync;


public class TrashCan extends FocusPanel implements HasAllTouchHandlers {

  
    int xPos;
	int yPos;
	
    
    public TrashCan(){
            super();
           
    		buildLayout();
            removeDefaultMouseDown();
    }
    
    
        
   
    private void buildLayout()
    {
    	HorizontalPanel h = new HorizontalPanel();
    	
    	Icon icnTrash = new Icon(IconType.TRASH);
    	icnTrash.setSize(IconSize.FOUR_TIMES);
    	h.add(icnTrash);
            	
        this.add(h);
      
       
    
      
        this.getElement().getStyle().setCursor(Cursor.POINTER);
   	
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
