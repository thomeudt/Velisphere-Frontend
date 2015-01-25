package com.velisphere.tigerspice.client.logic.widgets;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.ui.Label;

public class DragLabel extends Label {

	DragLabel(String text)
	{
		super(text);
		removeDefaultMouseDown();
		
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
