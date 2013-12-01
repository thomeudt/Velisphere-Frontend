package com.velisphere.tigerspice.client.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import com.github.gwtbootstrap.client.ui.Accordion;
import com.github.gwtbootstrap.client.ui.AccordionGroup;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.Image;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.github.gwtbootstrap.client.ui.Row;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.github.gwtbootstrap.client.ui.resources.ButtonSize;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DragSource;
import com.sencha.gxt.dnd.core.client.DropTarget;
import com.sencha.gxt.fx.client.Draggable;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.velisphere.tigerspice.client.LoginDialogBox;
import com.velisphere.tigerspice.client.checks.CheckNewDialogBox;
import com.velisphere.tigerspice.client.checks.CheckService;
import com.velisphere.tigerspice.client.checks.CheckServiceAsync;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.helper.DatatypeConfig;
import com.velisphere.tigerspice.client.helper.DragobjectContainer;
import com.velisphere.tigerspice.client.helper.DynamicAnchor;
import com.velisphere.tigerspice.client.images.Images;
import com.velisphere.tigerspice.client.properties.PropertyService;
import com.velisphere.tigerspice.client.properties.PropertyServiceAsync;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassService;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassServiceAsync;
import com.velisphere.tigerspice.client.rules.RuleView;
import com.velisphere.tigerspice.client.spheres.SphereView;
import com.velisphere.tigerspice.shared.CheckData;
import com.velisphere.tigerspice.shared.PropertyClassData;

public class CheckpathEditorWidget extends Composite {

	private String endpointID;
	private CheckServiceAsync rpcService;
	Accordion accordion = new Accordion();
	Paragraph pgpFirstCheck;
	Paragraph pgpAddSameLevel;
	HorizontalLayoutContainer firstCheckRow;
	FlowLayoutContainer con;
	VerticalLayoutContainer addNextLevelField;
	VerticalLayoutContainer firstCheckColumn;
	LinkedHashSet<SameLevelCheckpathObject> checkHashSet;
	LinkedList<SameLevelCheckpathObject> multicheckLinkedList;

	public CheckpathEditorWidget() {

		// widget constructor requires a parameter, thus we need to invoke the
		// no-args constructor of the parent class and then set the value for
		// sphereID
		// also note that we need to use the @UiFactory notation to instantiate
		// a widget that requires arguments like this, see how it is done in
		// SphereOverview class
		super();
		//this.endpointID = endpointID;

		con = new FlowLayoutContainer();
		initWidget(con);

		
		
		checkHashSet = new LinkedHashSet<SameLevelCheckpathObject>();
		multicheckLinkedList = new LinkedList<SameLevelCheckpathObject>();
		// final SameLevelCheckpathObject firstCheckField = new SameLevelCheckpathObject("drag check here", true);
		// checkHashSet.add(firstCheckField);
		
		rebuildCheckpathDiagram();	
			
				
		
		
	}


	private void rebuildCheckpathDiagram() {
		final VerticalLayoutContainer container = new VerticalLayoutContainer();
		container.setBorders(true);
		container.setScrollMode(ScrollSupport.ScrollMode.AUTO);
		// container.setHeight((int)((RootPanel.get().getOffsetHeight())/2.5));
		container.setWidth((int) ((RootPanel.get().getOffsetWidth()) / 4));

		firstCheckColumn = new VerticalLayoutContainer();
		//firstCheckColumn.setBorders(true);
		firstCheckColumn.setWidth((int) (400));
		firstCheckColumn.setHeight((int) (400));
		
		firstCheckRow = new HorizontalLayoutContainer();
		firstCheckRow.setBorders(false);
		//firstCheckRow.setScrollMode(ScrollSupport.ScrollMode.AUTOX);
		
		Iterator<SameLevelCheckpathObject> it = checkHashSet.iterator();

		if (it.hasNext() == false) {
			
			final SameLevelCheckpathObject addCheckField = new SameLevelCheckpathObject(null, "drag check here", true, 0);
			firstCheckRow.add(addCheckField);
			
			DropTarget target = new DropTarget(addCheckField) {
				@Override
				protected void onDragDrop(DndDropEvent event) {
					super.onDragDrop(event);

					// do the drag and drop visual action

					DragobjectContainer dragAccordion = (DragobjectContainer) event
							.getData();

					addCheckField.setText(dragAccordion.checkName);
					addCheckField.setCheckID(dragAccordion.checkID);
					checkHashSet.add(addCheckField);
					
					SameLevelCheckpathObject addNextLevelField = new SameLevelCheckpathObject(null, "drag check here", true, 1);
					multicheckLinkedList.add(addNextLevelField);
										
					rebuildCheckpathDiagram();
					
					

				}

			};
			target.setGroup("checkpath");
			target.setOverStyle("drag-ok");

		}

		
		while (it.hasNext())
		{		
			final SameLevelCheckpathObject currentObject = it.next();

			final SafeHtmlBuilder builder = new SafeHtmlBuilder();
			builder.appendHtmlConstant("<div style=\"border:1px solid #ddd;cursor:default\" class=\""
					+ "\">");
			builder.appendHtmlConstant("Drag "
					+ currentObject.text
					+ " into next level to build a tree");
			builder.appendHtmlConstant("</div>");
			
			DragSource source = new DragSource(
					currentObject) {
				@Override
				protected void onDragStart(
						DndDragStartEvent event) {
					super.onDragStart(event);
					// by
					// default
					// drag
					// is
					// allowed
					
					DragobjectContainer dragAccordion = new DragobjectContainer();
					dragAccordion.checkID = currentObject.checkId;
					dragAccordion.checkName = currentObject.text;
					
					
					event.setData(dragAccordion);
					event.getStatusProxy()
							.update(builder
									.toSafeHtml());
				}
				

			};
			source.setGroup("multicheck");
			
			
			firstCheckRow.add(currentObject);
			firstCheckRow.add(new Icon(IconType.CHEVRON_RIGHT));
			if (it.hasNext() == false) {
				
				final SameLevelCheckpathObject addCheckField = new SameLevelCheckpathObject(null, "drag check here", true, 0);
				firstCheckRow.add(addCheckField);
				
				DropTarget target = new DropTarget(addCheckField) {
					@Override
					protected void onDragDrop(DndDropEvent event) {
						super.onDragDrop(event);

						// do the drag and drop visual action

						DragobjectContainer dragAccordion = (DragobjectContainer) event
								.getData();

						addCheckField.setText(dragAccordion.checkName);
						addCheckField.setCheckID(dragAccordion.checkID);
						checkHashSet.add(addCheckField);
												
						addNextLevelField = new VerticalLayoutContainer();
						addNextLevelField.setBorders(true);
						addNextLevelField.setWidth((int) (100));

						Paragraph pghAddNextLevel = new Paragraph();
						pghAddNextLevel.setText("add next level");
						addNextLevelField.add(pghAddNextLevel);
				
						rebuildCheckpathDiagram();

					}

				};
				target.setGroup("checkpath");
				target.setOverStyle("drag-ok");
	
			}
				
		}
		
	
		
		
			//ArrayList<SameLevelCheckpathObject> reverseMulticheckList = new ArrayList<>(multicheckHashSet);
			//Collections.reverse(reverseMulticheckList);

			Collections.reverse(multicheckLinkedList);
			Iterator<SameLevelCheckpathObject> mit = multicheckLinkedList.iterator();		
				
			
			while (mit.hasNext())
			{		
				final SameLevelCheckpathObject currentObject = mit.next();
				firstCheckColumn.add(currentObject);
				firstCheckColumn.add(new Icon(IconType.CHEVRON_UP));
										
				if (it.hasNext() == false) {
									
					DropTarget target = new DropTarget(currentObject) {
						@Override
						protected void onDragDrop(DndDropEvent event) {
							super.onDragDrop(event);

							// do the drag and drop visual action

							DragobjectContainer dragAccordion = (DragobjectContainer) event
									.getData();

							multicheckLinkedList.remove(currentObject);
							currentObject.setText(dragAccordion.checkName);
							currentObject.setCheckID(dragAccordion.checkID);
							multicheckLinkedList.add(currentObject);

							if (currentObject.level < 4)
							{
								SameLevelCheckpathObject addNextLevelField = new SameLevelCheckpathObject(null, "drag check here", true, currentObject.level+1);
								multicheckLinkedList.add(addNextLevelField);
															
							}
							
							
							rebuildCheckpathDiagram();

						}

					};
					target.setGroup("multicheck");
					target.setOverStyle("drag-ok");
		
				}
				
				
				
			}
	
		
		
						
	
		firstCheckColumn.add(firstCheckRow);
					
		container.add(firstCheckColumn);
		
		con.clear();
		con.add(container);
	
		/*
		Iterator<SameLevelCheckpathObject> itt = checkHashSet.iterator();
		while (itt.hasNext())
		{		
			SameLevelCheckpathObject currentObject = new SameLevelCheckpathObject(null, null, null);
			currentObject = itt.next();
			System.out.println("ID: "+currentObject.checkId +" *** Text: " + currentObject.text);
		}
		*/

	}


}
