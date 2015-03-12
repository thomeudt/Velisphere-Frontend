package com.velisphere.tigerspice.client.logic.controllers;


import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.logic.draggables.DraggableListBox;
import com.velisphere.tigerspice.client.logic.layoutWidgets.Explorer;
import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.util.DOMUtil;

import java.util.ArrayList;


public class LogicDragController extends PickupDragController {

	Explorer explorer;
	
  public LogicDragController(AbsolutePanel panel, Explorer explorer) {
    super(panel, false);
    this.explorer = explorer;
    setBehaviorDragProxy(true);
    setBehaviorMultipleSelection(true);
  }

  @Override
  public void dragEnd() {
    // process drop first
    super.dragEnd();

    if (context.vetoException == null) {
      // remove original items
      /*
    	
    	DraggableListBox currentMouseListBox = (DraggableListBox) context.draggable.getParent().getParent();
      while (!context.selectedWidgets.isEmpty()) {
        Widget widget = context.selectedWidgets.get(0);
        toggleSelection(widget);
        currentMouseListBox.remove(widget);
        
      }*/
    }
  }

  @Override
  public void previewDragStart() throws VetoDragException {
    super.previewDragStart();
    if (context.selectedWidgets.isEmpty()) {
      throw new VetoDragException();
    }
  }

  @Override
  public void setBehaviorDragProxy(boolean dragProxyEnabled) {
    if (!dragProxyEnabled) {
      throw new IllegalArgumentException();
    }
    super.setBehaviorDragProxy(dragProxyEnabled);
  }



  ArrayList<Widget> getSelectedWidgets(DraggableListBox mouseListBox) {
    ArrayList<Widget> widgetList = new ArrayList<Widget>();
    for (Widget widget : context.selectedWidgets) {
      if (widget.getParent().getParent() == mouseListBox) {
        widgetList.add(widget);
      }
    }
    return widgetList;
  }
  
  public Explorer getExplorer(){
	  return this.explorer;
  }
  
}