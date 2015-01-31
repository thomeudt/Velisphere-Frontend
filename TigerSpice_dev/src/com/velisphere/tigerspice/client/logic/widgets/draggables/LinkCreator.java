package com.velisphere.tigerspice.client.logic.widgets.draggables;

import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.ui.FocusPanel;

public class LinkCreator extends FocusPanel {
	
	CanvasLabel source;
	
	public LinkCreator (CanvasLabel source)
	{
		super();
		this.source = source;
		this.add(new Icon(IconType.BULLSEYE));
		removeDefaultMouseDown();
		
	}
	
	public CanvasLabel getSource()
	{
		return this.source;
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
