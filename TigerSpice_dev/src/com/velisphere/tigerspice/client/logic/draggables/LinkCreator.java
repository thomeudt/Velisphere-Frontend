package com.velisphere.tigerspice.client.logic.draggables;

import java.util.LinkedList;

import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.constants.IconSize;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;

public class LinkCreator extends FocusPanel {
	
	Widget source;
	Icon icon;
	int currentColorID;
	LinkedList<String> colorPalette = new LinkedList<String>();
	boolean isLogic;
	
	public LinkCreator (Widget source, Boolean isLogic)
	{
		super();
		this.isLogic = isLogic;
		currentColorID=3;
		this.source = source;
	    icon = new Icon(IconType.BULLSEYE);
		icon.setIconSize(IconSize.TWO_TIMES);
		this.add(icon);
		icon.getElement().setAttribute("style", "color:cornflowerblue;");
		removeDefaultMouseDown();
		fillColorPalette();
		addColorChanger();
		
	}
	
	private void fillColorPalette()
	{
		colorPalette.add("crimson");
		colorPalette.add("lawngreen");
		colorPalette.add("rebeccapurple");
		colorPalette.add("cornflowerblue");
	}
	
	public Widget getSource()
	{
		return this.source;
	}
	
	

	 public boolean isLogic() {
		return isLogic;
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
	 
	
	 
	 private void addColorChanger()
	 {
		
		 
		 this.addMouseWheelHandler(new MouseWheelHandler() {

		

			@Override
			public void onMouseWheel(MouseWheelEvent event) {
				// TODO Auto-generated method stub
	
				event.preventDefault();
				
				if (currentColorID < colorPalette.size()-1)
					currentColorID = currentColorID +1;
				else
					currentColorID = 0;
				
				icon.getElement().setAttribute("style", "color:"+colorPalette.get(currentColorID)+";");
			}
			 
		 });
	 }

	 public String getCurrentColor()
	 {
			return colorPalette.get(currentColorID);
	 }
}
