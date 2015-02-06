package com.velisphere.tigerspice.client.logic.layoutWidgets;

import com.allen_sauer.gwt.dnd.client.util.WidgetLocation;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.logic.draggables.CanvasLabel;

public class LineCoordinateCalculator {
	
	
	int calcSourceX;
	int calcSourceY;
	int calcTargetX;
	int calcTargetY;
	CanvasLabel source;
	CanvasLabel target;
	WidgetLocation sourceLocation;
	WidgetLocation targetLocation;
	boolean targetAboveSource;
	boolean targetBelowSource;
	boolean targetLeftOfSource;
	boolean targetRightOfSource;

	
	public LineCoordinateCalculator(CanvasLabel source, CanvasLabel target, Widget reference)
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
			RootPanel.get().add(new HTML("ABOVE SOURCE"));
			calcSourceX = (int) (sourceLocation.getLeft() + source.getElement().getClientWidth() * 0.5 - 20);
			calcTargetX = (int) (targetLocation.getLeft() + target.getElement().getClientWidth() * 0.5 - 20);
			calcSourceY = sourceLocation.getTop() - 20;
			calcTargetY = targetLocation.getTop() + target.getElement().getClientHeight() - 15;
		}
		
		if (targetBelowSource == true)
		{
			RootPanel.get().add(new HTML("BELOW SOURCE"));
			calcSourceX = (int) (sourceLocation.getLeft() + source.getElement().getClientWidth() * 0.5 - 20);
			calcTargetX = (int) (targetLocation.getLeft() + target.getElement().getClientWidth() * 0.5 - 20);
			calcSourceY = sourceLocation.getTop() + source.getElement().getClientHeight() - 20;
			calcTargetY = targetLocation.getTop() - 20;
		}
		
		if (targetLeftOfSource == true)
		{
			RootPanel.get().add(new HTML("LEFT OF  SOURCE"));
			calcSourceX = sourceLocation.getLeft() - 20;
			calcTargetX = targetLocation.getLeft() + target.getElement().getClientWidth() - 20;
			calcSourceY = (int) (sourceLocation.getTop() + source.getElement().getClientHeight() * 0.5 - 20);
			calcTargetY = (int) (targetLocation.getTop() + target.getElement().getClientHeight() *0.5 - 20);
		}
		
		if (targetRightOfSource == true)
		{
			RootPanel.get().add(new HTML("RIGHT OF SOURCE"));
			calcSourceX = sourceLocation.getLeft() + source.getElement().getOffsetWidth() - 20;
			calcTargetX = targetLocation.getLeft() - 20;
			calcSourceY = (int) (sourceLocation.getTop() + source.getElement().getOffsetHeight() * 0.5 - 20);
			calcTargetY = (int) (targetLocation.getTop() + target.getElement().getOffsetHeight() *0.5 - 20);
		}
	}
	
	
}
