/*******************************************************************************
 * CONFIDENTIAL INFORMATION
 *  __________________
 *  
 *   Copyright (C) 2013 Thorsten Meudt 
 *   All Rights Reserved.
 *  
 *  NOTICE:  All information contained herein is, and remains
 *  the property of Thorsten Meudt and its suppliers,
 *  if any.  The intellectual and technical concepts contained
 *  herein are proprietary to Thorsten Meudt
 *  and its suppliers and may be covered by Patents,
 *  patents in process, and are protected by trade secret or copyright law.
 *  Dissemination of this information or reproduction of this material
 *  is strictly forbidden unless prior written permission is obtained
 *  from Thorsten Meudt.
 ******************************************************************************/
package com.velisphere.tigerspice.client.rules;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.github.gwtbootstrap.client.ui.Accordion;
import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.orange.links.client.DiagramController;
import com.orange.links.client.connection.Connection;
import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DragSource;
import com.sencha.gxt.dnd.core.client.DropTarget;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.velisphere.tigerspice.client.checks.CheckService;
import com.velisphere.tigerspice.client.checks.CheckServiceAsync;
import com.velisphere.tigerspice.client.helper.DragobjectContainer;
import com.velisphere.tigerspice.client.helper.UuidService;
import com.velisphere.tigerspice.client.helper.UuidServiceAsync;
import com.velisphere.tigerspice.shared.CheckPathObjectData;


public class CheckpathEditorWidget extends Composite {

	Accordion accordion = new Accordion();
	Paragraph pgpFirstCheck;
	Paragraph pgpAddSameLevel;
	// HorizontalLayoutContainer firstCheckRow;
	FlowLayoutContainer con;
	VerticalLayoutContainer addNextLevelField;
	// VerticalLayoutContainer checkColumn;
	LinkedHashSet<SameLevelCheckpathObject> checkHashSet;
	// LinkedList<SameLevelCheckpathObject> multicheckLinkedList;
	public LinkedList<MulticheckColumn<SameLevelCheckpathObject>> multicheckColumns;
	private CheckPathServiceAsync rpcServiceCheckPath;

	
	private UuidServiceAsync rpcServiceUuid;

	public CheckpathEditorWidget() {

		// widget constructor requires a parameter, thus we need to invoke the
		// no-args constructor of the parent class and then set the value for
		// sphereID
		// also note that we need to use the @UiFactory notation to instantiate
		// a widget that requires arguments like this, see how it is done in
		// SphereOverview class
		super();

		rpcServiceUuid = GWT.create(UuidService.class);
		rpcServiceCheckPath = GWT.create(CheckPathService.class);

		con = new FlowLayoutContainer();
		initWidget(con);

		checkHashSet = new LinkedHashSet<SameLevelCheckpathObject>();

		multicheckColumns = new LinkedList<MulticheckColumn<SameLevelCheckpathObject>>();
		
		
		rebuildCheckpathDiagram();

	}

	private void rebuildCheckpathDiagram() {

		// Draw basic layout boxes

		final VerticalLayoutContainer container = new VerticalLayoutContainer();

		container.setBorders(true);
		container.setScrollMode(ScrollSupport.ScrollMode.AUTO);
		container.setHeight((int) ((RootPanel.get().getOffsetHeight()) / 2.5));
		container.setWidth((int) ((RootPanel.get().getOffsetWidth()) / 4));

		DiagramController controller = new DiagramController(800,
				(int) ((RootPanel.get().getOffsetHeight()) / 2.5) - 25);
		controller.showGrid(true); // Display a background grid

		container.add(controller.getView());

		Iterator<SameLevelCheckpathObject> it = checkHashSet.iterator();

		// draw first d&d target field if checkHashSet is empty

		if (it.hasNext() == false) {

			final SameLevelCheckpathObject addCheckField = new SameLevelCheckpathObject(
					null, "drag check here", true, 0);
			controller.addWidget(addCheckField, 10, 300);

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

					SameLevelCheckpathObject addNextLevelField = new SameLevelCheckpathObject(
							null, "drag check here", true, 1);

					// also add an entire multicheck column
					MulticheckColumn<SameLevelCheckpathObject> multicheckList = new MulticheckColumn<SameLevelCheckpathObject>(
							true);
					multicheckList.add(addNextLevelField);
					multicheckColumns.add(multicheckList);
					rebuildCheckpathDiagram();

				}

			};
			target.setGroup("firstlevel");
			target.setOverStyle("drag-ok");

		}

		// draw the first level - "checks" - as the foundation layer by reading
		// the data stored in the checkHashSet

		int xpos = 10;

		while (it.hasNext()) {
			final SameLevelCheckpathObject currentObject = it.next();

			final SafeHtmlBuilder builder = new SafeHtmlBuilder();
			builder.appendHtmlConstant("<div style=\"border:1px solid #ddd;cursor:default\" class=\""
					+ "\">");
			builder.appendHtmlConstant("Drag " + currentObject.text
					+ " into next level to build a tree");
			builder.appendHtmlConstant("</div>");

			DragSource source = new DragSource(currentObject) {
				@Override
				protected void onDragStart(DndDragStartEvent event) {
					super.onDragStart(event);
					// by
					// default
					// drag
					// is
					// allowed

					DragobjectContainer dragAccordion = new DragobjectContainer();
					dragAccordion.checkID = currentObject.checkId;
					dragAccordion.checkName = currentObject.text;
					dragAccordion.isMulticheck = currentObject.isMulticheck;
					dragAccordion.checkpathObject = currentObject;

					event.setData(dragAccordion);
					event.getStatusProxy().update(builder.toSafeHtml());
				}

			};
			source.setGroup("multicheck");

			controller.addWidget(currentObject, xpos, 320);
			xpos = xpos + 120;

			// if no more items are left in the check hashset, draw a new drag
			// target box as the last step to allow for adding a new check to
			// the checkpath

			if (it.hasNext() == false) {

				final SameLevelCheckpathObject addCheckField = new SameLevelCheckpathObject(
						null, "drag check here", true, 0);
				// firstCheckRow.add(addCheckField);
				controller.addWidget(addCheckField, xpos, 320);

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
						
						rebuildCheckpathDiagram();

					}

				};
				target.setGroup("firstlevel");
				target.setOverStyle("drag-ok");

			}

		}

		// check if new column needs to be added

		if (multicheckColumns.size() > 0) {
			System.out.println("Spaltenzahl:" + multicheckColumns.size());

			if (multicheckColumns.getLast().getEmpty() == false) {

				SameLevelCheckpathObject addNextColumnField = new SameLevelCheckpathObject(
						null, "drag check here", true, 1);

				MulticheckColumn<SameLevelCheckpathObject> newMulticheckList = new MulticheckColumn<SameLevelCheckpathObject>(
						true);
				newMulticheckList.add(addNextColumnField);
				multicheckColumns.add(newMulticheckList);
				controller.addWidget(addNextColumnField,
						10 + multicheckColumns.size() * 120, 250);

			}

		}

		// when the first layer is done, now add the vertical layers containing
		// multichecks, in the reverse order of their addition

		HorizontalLayoutContainer hLC = new HorizontalLayoutContainer();

		if (multicheckColumns.size() > 0) {
			hLC.setHeight((4 * 65));
		}

		int countOfMulticheckColumns = multicheckColumns.size();

		for (int i = 1; i <= countOfMulticheckColumns; i = i + 1) {
			drawMulticheckColumn(i - 1, controller);
		}

		con.clear();
		con.add(container);
		drawConnectorLines(controller);
		

	}

	private VerticalLayoutContainer drawMulticheckColumn(
			final int columnElement, final DiagramController controller) {

		VerticalLayoutContainer checkColumn = new VerticalLayoutContainer();

		LinkedList<SameLevelCheckpathObject> multicheckLinkedList = multicheckColumns
				.get(columnElement);

		Iterator<SameLevelCheckpathObject> mit = multicheckLinkedList
				.iterator();

		int yposdelta = 0;
		while (mit.hasNext()) {

			final SameLevelCheckpathObject currentObject = mit.next();

			System.out.println(currentObject.text + " is multicheck: "
					+ currentObject.isMulticheck);
			controller.addWidget(currentObject, 10 + columnElement * 120,
					250 - yposdelta);
			yposdelta = yposdelta + 70;

			DropTarget target = new DropTarget(currentObject) {
				@Override
				protected void onDragDrop(DndDropEvent event) {
					super.onDragDrop(event);

					// do the drag and drop visual action

					final DragobjectContainer dragAccordion = (DragobjectContainer) event
							.getData();

					// action that is triggered if check is dragged into an
					// empty target field
					if (currentObject.checkId == null) {

						multicheckColumns.get(columnElement).remove(
								currentObject);
						currentObject.setText(dragAccordion.checkName);
						currentObject.setIsMulticheck(true);
						currentObject.setEmpty(false);
						multicheckColumns.get(columnElement).setEmpty(false);

						final MulticheckDialogBox multicheckNewDialogBox = new MulticheckDialogBox();

						multicheckNewDialogBox.setModal(true);
						multicheckNewDialogBox.setAutoHideEnabled(true);

						multicheckNewDialogBox.setAnimationEnabled(true);

						multicheckNewDialogBox.setPopupPosition(
								(RootPanel.get().getOffsetWidth()) / 3,
								(RootPanel.get().getOffsetHeight()) / 4);
						multicheckNewDialogBox.show();

						multicheckNewDialogBox
								.addCloseHandler(new CloseHandler<PopupPanel>() {

									public void onClose(CloseEvent event) {

							
																										
										
										rpcServiceUuid
												.getUuid(new AsyncCallback<String>() {

													@Override
													public void onFailure(
															Throwable caught) {
														// TODO Auto-generated
														// method stub

													}

													@Override
													public void onSuccess(
															String result) {

														currentObject
																.setCheckID(result);
														System.out
																.println("ID: "
																		+ currentObject.checkId);
													}

												});

										currentObject
												.setText(multicheckNewDialogBox.multicheckTitle
														+ " ("
														+ multicheckNewDialogBox.combination
														+ ")");
										currentObject
												.setCombination(multicheckNewDialogBox.combination);
										currentObject.ancTextField
												.addClickHandler(new ClickHandler() {
													@Override
													public void onClick(
															ClickEvent event) {

														showUpdateMulticheckDialog(
																currentObject.text,
																currentObject.combination,
																currentObject.childChecks,
																currentObject.childMultichecks);

													}

												});

										if (dragAccordion.isMulticheck) {
											currentObject
													.addChildMulticheck(dragAccordion.checkpathObject);
											

										} else {
											currentObject
													.addChildCheck(dragAccordion.checkpathObject);
											

										}

										System.out.println("Title: "
												+ currentObject.text);
										System.out.println("Combination: "
												+ currentObject.combination);
										System.out
												.println("Child Multichecks: "
														+ currentObject.childMultichecks);
										System.out.println("Child Checks: "
												+ currentObject.childChecks);

										// new json starts here
										
										CheckPathObjectData currentObjectData = new CheckPathObjectData(currentObject.checkId, currentObject.text, currentObject.empty, currentObject.level);
										
										
										rpcServiceCheckPath.addNewUiObject(currentObjectData, 
												new AsyncCallback<String>() {

													@Override
													public void onFailure(Throwable caught) {
														// TODO Auto-generated method stub
														System.out.println("ERROR: " + caught);
													
													}

													@Override
													public void onSuccess(String result) {
														// TODO Auto-generated method stub
														
														System.out.println("Succes: " + result);

													}

												});

										
										
										
										// new json ends here

										
										rebuildCheckpathDiagram();
									}

								});

						final SafeHtmlBuilder builder = new SafeHtmlBuilder();
						builder.appendHtmlConstant("<div style=\"border:1px solid #ddd;cursor:default\" class=\""
								+ "\">");
						builder.appendHtmlConstant("Drag " + currentObject.text
								+ " into next higher level to build a tree");
						builder.appendHtmlConstant("</div>");

						DragSource source = new DragSource(currentObject) {
							@Override
							protected void onDragStart(DndDragStartEvent event) {
								super.onDragStart(event);

								DragobjectContainer dragAccordion = new DragobjectContainer();
								dragAccordion.checkID = currentObject.checkId;
								dragAccordion.checkName = currentObject.text;
								dragAccordion.isMulticheck = currentObject.isMulticheck;
								dragAccordion.checkpathObject = currentObject;

								event.setData(dragAccordion);
								event.getStatusProxy().update(
										builder.toSafeHtml());
							}

						};

						source.setGroup("multicheck");

						multicheckColumns.get(columnElement).add(currentObject);
						rebuildCheckpathDiagram();

						// add new empty field for multicheck

					}

					// action that is triggered if check is dragged into an
					// already used target field (overwriting)
					if (currentObject.checkId != null) {

						currentObject.setEmpty(false);
						multicheckColumns.get(columnElement).setEmpty(false);

						if (dragAccordion.isMulticheck) {
							currentObject
									.addChildMulticheck(dragAccordion.checkpathObject);
						} else {
							currentObject
									.addChildCheck(dragAccordion.checkpathObject);
						}

						final SafeHtmlBuilder builder = new SafeHtmlBuilder();
						builder.appendHtmlConstant("<div style=\"border:1px solid #ddd;cursor:default\" class=\""
								+ "\">");
						builder.appendHtmlConstant("Drag " + currentObject.text
								+ " into next level to build a tree");
						builder.appendHtmlConstant("</div>");

						DragSource source = new DragSource(currentObject) {
							@Override
							protected void onDragStart(DndDragStartEvent event) {
								super.onDragStart(event);

								DragobjectContainer dragAccordion = new DragobjectContainer();
								dragAccordion.checkID = currentObject.checkId;
								dragAccordion.checkName = currentObject.text;
								dragAccordion.isMulticheck = currentObject.isMulticheck;
								dragAccordion.checkpathObject = currentObject;
								event.setData(dragAccordion);
								event.getStatusProxy().update(
										builder.toSafeHtml());
							}

						};
						source.setGroup("multicheck");

					}

					if (currentObject.level == multicheckColumns.get(
							columnElement).size()
							&& multicheckColumns.get(columnElement).size() < 4) {
						SameLevelCheckpathObject addNextLevelField = new SameLevelCheckpathObject(
								null, "drag check here", true,
								currentObject.level + 1);
						multicheckColumns.get(columnElement).add(
								addNextLevelField);

					}

					rebuildCheckpathDiagram();

				}

			};
			target.setGroup("multicheck");
			target.setOverStyle("drag-ok");

		}

		return checkColumn;

	}

	private void showUpdateMulticheckDialog(String title, String combination,
			HashSet<SameLevelCheckpathObject> childChecks,
			HashSet<SameLevelCheckpathObject> childMultichecks) {

		final MulticheckDialogBox multicheckDialogBox = new MulticheckDialogBox();

		multicheckDialogBox.setModal(true);
		multicheckDialogBox.setAutoHideEnabled(true);

		multicheckDialogBox.setAnimationEnabled(true);

		multicheckDialogBox.setPopupPosition(
				(RootPanel.get().getOffsetWidth()) / 3,
				(RootPanel.get().getOffsetHeight()) / 4);
		multicheckDialogBox.show();

		HashMap<String, String> allChecks = new HashMap<String, String>();

		Iterator<SameLevelCheckpathObject> it = childChecks.iterator();
		while (it.hasNext()) {
			SameLevelCheckpathObject check = it.next();
			allChecks.put(check.checkId, check.text);
		}

		Iterator<SameLevelCheckpathObject> mit = childMultichecks.iterator();
		while (mit.hasNext()) {
			SameLevelCheckpathObject multicheck = mit.next();
			allChecks.put(multicheck.checkId, multicheck.text);
		}

		multicheckDialogBox.setParameters("1", title, combination, "1",
				allChecks);

	}

	private void drawConnectorLines(DiagramController controller) {

		
		//System.out.println("Multicheck Columns: " + multicheckColumns);
		if (multicheckColumns.size() > 0) {

			int i = 0;
			while (i < multicheckColumns.size()) {

				MulticheckColumn<SameLevelCheckpathObject> currentColumn = multicheckColumns
						.get(i);

				if (currentColumn.isEmpty() == false) {

					Iterator<SameLevelCheckpathObject> it = currentColumn
							.iterator();
					while (it.hasNext()) {
						SameLevelCheckpathObject parent = it.next();
						Iterator<SameLevelCheckpathObject> cit = parent.childChecks
								.iterator();
						while (cit.hasNext()) {
							SameLevelCheckpathObject child = cit.next();
							PickupDragController dragController = new PickupDragController(
									controller.getView(), true);
							dragController.makeDraggable(parent);
							dragController.makeDraggable(child);
							controller.registerDragController(dragController);
							Connection con = controller
									.drawStraightArrowConnection(parent, child);

						}

						Iterator<SameLevelCheckpathObject> cmit = parent.childMultichecks
								.iterator();
						while (cmit.hasNext()) {
							SameLevelCheckpathObject child = cmit.next();
							PickupDragController dragController = new PickupDragController(
									controller.getView(), true);
							dragController.makeDraggable(parent);
							dragController.makeDraggable(child);
							controller.registerDragController(dragController);
							Connection con = controller
									.drawStraightArrowConnection(parent, child);

						}

					}

				}

				i = i + 1;

			}
		}

	}
	
	
	
}
