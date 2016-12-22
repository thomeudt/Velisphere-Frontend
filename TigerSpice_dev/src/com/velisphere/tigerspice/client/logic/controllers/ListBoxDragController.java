package com.velisphere.tigerspice.client.logic.controllers;


import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.logic.draggables.DraggableListBox;
import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.util.DOMUtil;

import java.util.ArrayList;

/**
 * DragController for {@link DualListExample}.
 */
public class ListBoxDragController extends PickupDragController {

  public ListBoxDragController(AbsolutePanel panel) {
    super(panel, false);
    setBehaviorDragProxy(true);
    setBehaviorMultipleSelection(true);
  }

  @Override
  public void dragEnd() {
    // process drop first
    super.dragEnd();

    if (context.vetoException == null) {
      // remove original items
      DraggableListBox currentMouseListBox = (DraggableListBox) context.draggable.getParent().getParent();
      while (!context.selectedWidgets.isEmpty()) {
        Widget widget = context.selectedWidgets.get(0);
        toggleSelection(widget);
        currentMouseListBox.remove(widget);
      }
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

  @Override
  public void toggleSelection(Widget draggable) {
    super.toggleSelection(draggable);
    DraggableListBox currentMouseListBox = (DraggableListBox) draggable.getParent().getParent();
    ArrayList<Widget> otherWidgets = new ArrayList<Widget>();
    for (Widget widget : context.selectedWidgets) {
      if (widget.getParent().getParent() != currentMouseListBox) {
        otherWidgets.add(widget);
      }
    }
    for (Widget widget : otherWidgets) {
      super.toggleSelection(widget);
    }
  }

  @Override
  protected Widget newDragProxy(DragContext context) {
	  DraggableListBox currentMouseListBox = (DraggableListBox) context.draggable.getParent().getParent();
	  DraggableListBox proxyMouseListBox = new DraggableListBox(context.selectedWidgets.size());
    proxyMouseListBox.setWidth(DOMUtil.getClientWidth(currentMouseListBox.getElement()) + "px");
    for (Widget widget : context.selectedWidgets) {
      HTML htmlClone = new HTML(DOM.getInnerHTML(widget.getElement()));
      proxyMouseListBox.add(htmlClone, "100%");
    }
    return proxyMouseListBox;
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
}