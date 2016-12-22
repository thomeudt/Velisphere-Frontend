package com.velisphere.tigerspice.client.logic.draggables;
/*
 * Copyright 2009 Fred Sauer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */


import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.logic.controllers.ListBoxDragController;

import java.util.ArrayList;

/**
 * Either left or right hand side of a {@link DualListBox}.
 */
public class DraggableListBox extends Composite {

  private static final class SpacerHTML extends HTML {

    public SpacerHTML() {
      super("&nbsp;");
    }
  }


  private static final String CSS_DEMO_DUAL_LIST_EXAMPLE_ITEM_HAS_CONTENT = "DragListBox-item-has-content";

  private PickupDragController dragController;

  private Grid grid;

  private int widgetCount = 0;

  /**
   * Used by {@link ListBoxDragController} to create a draggable listbox containing the selected
   * items.
   */
  public DraggableListBox(int size) {
    
	grid = new Grid(size, 1);
    initWidget(grid);
    grid.setCellPadding(0);
    grid.setCellSpacing(0);
    for (int i = 0; i < size; i++) {
      //grid.getCellFormatter().addStyleName(i, 0, CSS_DEMO_DUAL_LIST_EXAMPLE_ITEM);
      setWidget(i, null, null);
    }
  }

  /**
   * Used by {@link DualListBox} to create the left and right list boxes.
   */
  public DraggableListBox(PickupDragController dragController, int size) {
    this(size);
    this.dragController = dragController;
  }

  public void add(String text, String endpointName, String width, String propertyID, String endpointID, String endpointClassID, String propertyClassID, byte isSensor, byte isActor) {
	  
    add(new ExplorerLabel(text, endpointName, propertyID,  endpointID, endpointClassID, propertyClassID, isSensor, isActor), width);
  }

  public void add(Widget widget, String width) {
    setWidget(widgetCount++, widget, width);
  }

  int getWidgetCount() {
    return widgetCount;
  }

  public boolean remove(Widget widget) {
    int index = getWidgetIndex(widget);
    if (index == -1) {
      return false;
    }
    for (int i = index; i < widgetCount - 1; i++) {
      // explicitly remove and add widget back for correct draggability
      setWidget(i, removeWidget(i + 1), null);
    }
    setWidget(widgetCount - 1, null, null);
    widgetCount--;
    return true;
  }

  ArrayList<Widget> widgetList() {
    ArrayList<Widget> widgetList = new ArrayList<Widget>();
    for (int i = 0; i < getWidgetCount(); i++) {
      widgetList.add(getWidget(i));
    }
    return widgetList;
  }

  private Widget getWidget(int index) {
    return grid.getWidget(index, 0);
  }

  private int getWidgetIndex(Widget widget) {
    for (int i = 0; i < getWidgetCount(); i++) {
      if (getWidget(i) == widget) {
        return i;
      }
    }
    return -1;
  }

  private Widget removeWidget(int index) {
    Widget widget = getWidget(index);
   
    
    if (widget != null && dragController != null && !(widget instanceof SpacerHTML)) {
      dragController.makeNotDraggable(widget);
    }
    
    grid.getCellFormatter().removeStyleName(index, 0, CSS_DEMO_DUAL_LIST_EXAMPLE_ITEM_HAS_CONTENT);
    grid.setWidget(index, 0, new SpacerHTML());
    return widget;
  }

  private void setWidget(int index, Widget widget, String width) {
    removeWidget(index);
    if (widget == null) {
      widget = new SpacerHTML();
    } else {
      grid.getCellFormatter().addStyleName(index, 0, CSS_DEMO_DUAL_LIST_EXAMPLE_ITEM_HAS_CONTENT);
      
      
      if (dragController != null) {
        dragController.makeDraggable(widget);
        
      }
      
    }
    if (width != null) widget.setWidth(width);
    widget.getElement().getStyle().setCursor(Cursor.POINTER);
    grid.setWidget(index, 0, widget);
  }
  

  public void clear()
  {
	
	    for (int i = 0; i <= getWidgetCount(); i++) {
		   removeWidget(i);

	      }
	    
	    widgetCount = 0;
	  	
  }
  
}