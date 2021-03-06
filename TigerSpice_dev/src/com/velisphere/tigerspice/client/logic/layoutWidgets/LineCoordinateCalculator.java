package com.velisphere.tigerspice.client.logic.layoutWidgets;

import com.allen_sauer.gwt.dnd.client.util.WidgetLocation;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.logic.draggables.PhysicalItem;

public class LineCoordinateCalculator {
	
	
	int calcSourceX;
	int calcSourceY;
	int calcTargetX;
	int calcTargetY;
	Widget source;
	Widget target;
	WidgetLocation sourceLocation;
	WidgetLocation targetLocation;
	boolean targetAboveSource;
	boolean targetBelowSource;
	boolean targetLeftOfSource;
	boolean targetRightOfSource;

	
	public LineCoordinateCalculator(Widget source, Widget target, Widget reference)
	{
		this.source = source;
		this.target = target;
		sourceLocation = new WidgetLocation(source, reference);
		targetLocation = new WidgetLocation(target, reference);

		
		
		determineRelativeLocation();
		calculateCoordinates();
		
	}
	
	public int getCalcSourceX()
	{
		return this.calcSourceX;
	}
	
	public int getCalcSourceY()
	{
		return this.calcSourceY;
	}
	
	public int getCalcTargetX()
	{
		return this.calcTargetX;
	}
	
	public int getCalcTargetY()
	{
		return this.calcTargetY;
	}
	
	private void determineRelativeLocation()
	{
		int widgetSourceY1 = sourceLocation.getTop();
		int widgetSourceX1 = sourceLocation.getLeft();
	
		int widgetTargetY1 = targetLocation.getTop();
		int widgetTargetX1 = targetLocation.getLeft();
		
		int widgetSourceY2 = widgetSourceY1 + source.getElement().getClientHeight();
		int widgetSourceX2 = widgetSourceX1 + source.getElement().getClientWidth();
		
		int widgetTargetY2 = widgetTargetY1 + target.getElement().getClientHeight();
		int widgetTargetX2 = widgetTargetX1 + target.getElement().getClientWidth();
		
		
		if (widgetTargetY2 < widgetSourceY1) targetAboveSource = true;
		if ((widgetTargetY2 > widgetSourceY1) && 
				(widgetTargetY1 < widgetSourceY2) &&
				(widgetTargetX2 < widgetSourceX1)) targetLeftOfSource = true;
		if ((widgetTargetY2 > widgetSourceY1) && 
				(widgetTargetY1 < widgetSourceY2) &&
				(widgetTargetX1 > widgetSourceX2)) targetRightOfSource = true;
		if (widgetTargetY1 > widgetSourceY2) targetBelowSource = true;
		
	}
	
	private void calculateCoordinates()
	{
		
		
		if (targetAboveSource == true)
		{
			calcSourceX = (int) (sourceLocation.getLeft() + source.getElement().getClientWidth() * 0.5);
			calcTargetX = (int) (targetLocation.getLeft() + target.getElement().getClientWidth() * 0.5);
			calcSourceY = sourceLocation.getTop();
			calcTargetY = targetLocation.getTop() + target.getElement().getClientHeight();
		}
		
		if (targetBelowSource == true)
		{
			calcSourceX = (int) (sourceLocation.getLeft() + source.getElement().getClientWidth() * 0.5);
			calcTargetX = (int) (targetLocation.getLeft() + target.getElement().getClientWidth() * 0.5);
			calcSourceY = sourceLocation.getTop() + source.getElement().getClientHeight();
			calcTargetY = targetLocation.getTop();
		}
		
		if (targetLeftOfSource == true)
		{
			calcSourceX = sourceLocation.getLeft();
			calcTargetX = targetLocation.getLeft() + target.getElement().getClientWidth();
			calcSourceY = (int) (sourceLocation.getTop() + source.getElement().getClientHeight() * 0.5);
			calcTargetY = (int) (targetLocation.getTop() + target.getElement().getClientHeight() *0.5);
		}
		
		if (targetRightOfSource == true)
		{
			calcSourceX = sourceLocation.getLeft() + source.getElement().getOffsetWidth();
			calcTargetX = targetLocation.getLeft();
			calcSourceY = (int) (sourceLocation.getTop() + source.getElement().getOffsetHeight() * 0.5);
			calcTargetY = (int) (targetLocation.getTop() + target.getElement().getOffsetHeight() *0.5);
		}
	}
	
	
}
