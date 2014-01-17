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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.github.gwtbootstrap.client.ui.Accordion;
import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.Image;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
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
import com.sencha.gxt.fx.client.Draggable;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.velisphere.tigerspice.client.checks.CheckNewDialogBox;
import com.velisphere.tigerspice.client.checks.CheckService;
import com.velisphere.tigerspice.client.checks.CheckServiceAsync;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.helper.DragobjectContainer;
import com.velisphere.tigerspice.client.helper.UuidService;
import com.velisphere.tigerspice.client.helper.UuidServiceAsync;
import com.velisphere.tigerspice.client.images.Images;
import com.velisphere.tigerspice.shared.CheckPathObjectColumn;
import com.velisphere.tigerspice.shared.CheckPathObjectData;
import com.velisphere.tigerspice.shared.CheckPathObjectTree;

public class CheckpathEditorWidget extends Composite {

	Accordion accordion = new Accordion();
	Paragraph pgpFirstCheck;
	Paragraph pgpAddSameLevel;

	FlowLayoutContainer con;
	VerticalLayoutContainer addNextLevelField;

	LinkedHashSet<SameLevelCheckpathObject> checkHashSet;

	public LinkedList<MulticheckColumn<SameLevelCheckpathObject>> multicheckColumns;
	private CheckPathServiceAsync rpcServiceCheckPath;
	private UuidServiceAsync rpcServiceUuid;
	private CheckServiceAsync rpcServiceCheck;
	private String checkpathID;
	private List<SameLevelCheckpathObject> updatedMultichecks;
	private List<SameLevelCheckpathObject> newMultichecks;
	private List<SameLevelCheckpathObject> deletedMultichecks;
	private Boolean additionalRebuildNeeded;
	private DiagramController controller; 


	public CheckpathEditorWidget(String checkpathID) {

		// widget constructor requires a parameter, thus we need to invoke the
		// no-args constructor of the parent class and then set the value for
		// sphereID
		// also note that we need to use the @UiFactory notation to instantiate
		// a widget that requires arguments like this, see how it is done in
		// SphereOverview class
		super();

		this.checkpathID = checkpathID;

		rpcServiceUuid = GWT.create(UuidService.class);
		rpcServiceCheckPath = GWT.create(CheckPathService.class);
		rpcServiceCheck = GWT.create(CheckService.class);
		this.updatedMultichecks = new ArrayList<SameLevelCheckpathObject>();
		this.newMultichecks = new ArrayList<SameLevelCheckpathObject>();
		this.deletedMultichecks = new ArrayList<SameLevelCheckpathObject>();

		con = new FlowLayoutContainer();

		initWidget(con);

				
		checkHashSet = new LinkedHashSet<SameLevelCheckpathObject>();

		multicheckColumns = new LinkedList<MulticheckColumn<SameLevelCheckpathObject>>();

		if (this.checkpathID != null) {
			loadCheckpathJSON(this.checkpathID);
		}

		System.out.println("MC Columnen: " + multicheckColumns);
		
		additionalRebuildNeeded = false;
		
		rebuildCheckpathDiagram();

	}

	private void rebuildCheckpathDiagram() {

		// Draw basic layout boxes

		final VerticalLayoutContainer container = new VerticalLayoutContainer();

		container.setBorders(false);
		container.setScrollMode(ScrollSupport.ScrollMode.AUTO);
		container.setHeight((int) 400);
		container.setWidth((int) ((RootPanel.get().getOffsetWidth()) / 1.74));
		controller = new DiagramController(1200,(int) 375);
		controller.showGrid(true); // Display a background grid
		controller.setAllowingUserInteractions(false); // prevent user from dragging the lines manually
		

		container.add(controller.getView());

		Iterator<SameLevelCheckpathObject> it = checkHashSet.iterator();

		// draw first d&d target field if checkHashSet is empty

		if (it.hasNext() == false) {
			drawInitialCheckTarget();
		}

		// draw the first level - "checks" - as the foundation layer by reading
		// the data stored in the checkHashSet

		int xpos = 10;
		int ypos = 320;

		while (it.hasNext()) {
			final SameLevelCheckpathObject currentObject = it.next();

			addDragSource(currentObject);
			addDndTargetForAction(currentObject);
			
			currentObject.ancTextField.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {

					// rework this to show checkEditDialog
					
				CheckNewDialogBox checkNewDialogBox = new CheckNewDialogBox(
						"", "",
						"",
						"");

				checkNewDialogBox.setModal(true);
				checkNewDialogBox.setAutoHideEnabled(true);

				checkNewDialogBox.setAnimationEnabled(true);

				checkNewDialogBox.setPopupPosition(
						(RootPanel.get().getOffsetWidth()) / 3,
						(RootPanel.get().getOffsetHeight()) / 4);
				checkNewDialogBox.show();
				checkNewDialogBox.addStyleName("ontop");
			}
				
				});
			
			controller.addWidget(currentObject, xpos, ypos);
			
			// add icon for action loaded checks, for now just move into position
			
			controller.addWidget(currentObject.actionIcon, xpos+83 , ypos - 17);
			
			// increment horizontal positon
			
			xpos = xpos + 120;
			
				
			


			// if no more items are left in the check hashset, draw a new drag
			// target box as the last step to allow for adding a new check to
			// the checkpath

			if (it.hasNext() == false) {

				final SameLevelCheckpathObject addCheckField = new SameLevelCheckpathObject(
						null, "empty status check", true, 0);
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
						
						
						// new and to be reworked - define the check parameters in checknewdialog
						
						CheckNewDialogBox checkNewDialogBox = new CheckNewDialogBox(
								"", "",
								"",
								"");

						checkNewDialogBox.setModal(true);
						checkNewDialogBox.setAutoHideEnabled(true);

						checkNewDialogBox.setAnimationEnabled(true);

						checkNewDialogBox.setPopupPosition(
								(RootPanel.get().getOffsetWidth()) / 3,
								(RootPanel.get().getOffsetHeight()) / 4);
						checkNewDialogBox.show();
						checkNewDialogBox.addStyleName("ontop");
						
						


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
						null, "empty logic check", true, 1);

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
		
		if (additionalRebuildNeeded == true) {
			System.out.println("ADDITIONAL REBUILD");
			rebuildCheckpathDiagram();
		}

	}

	private VerticalLayoutContainer drawMulticheckColumn(
			final int columnElement, final DiagramController controller) {

		VerticalLayoutContainer checkColumn = new VerticalLayoutContainer();

		final MulticheckColumn<SameLevelCheckpathObject> multicheckLinkedList = multicheckColumns
				.get(columnElement);

		Iterator<SameLevelCheckpathObject> mit = multicheckLinkedList
				.iterator();

		int yposdelta = 0;
		while (mit.hasNext()) {

			final SameLevelCheckpathObject currentObject = mit.next();
			
			// correct level setting of current object within column, might have changed due to child deletions
			// this can be further optimized in the future to allow moving elements vertically in diagram
			
			if (multicheckLinkedList.indexOf(currentObject) != currentObject.level-1){
				currentObject.level = multicheckLinkedList.indexOf(currentObject) + 1;
			}
			
			//cleanup orphan child multicheck links from prior deletions
			
			List<SameLevelCheckpathObject> allMultichecksForInspection = new ArrayList<SameLevelCheckpathObject>();
			
			for (int i = 1; i <= multicheckColumns.size(); i = i + 1) {
				allMultichecksForInspection.addAll(multicheckColumns.get(i-1));	
			}
			
			additionalRebuildNeeded = false;
			List<SameLevelCheckpathObject> childMultichecksToRemove = new ArrayList<SameLevelCheckpathObject>();
			Iterator<SameLevelCheckpathObject> linkedMultichecksIT = currentObject.childMultichecks.iterator();
			while(linkedMultichecksIT.hasNext()){
				SameLevelCheckpathObject linkedMulticheck = linkedMultichecksIT.next();
				if (allMultichecksForInspection.contains(linkedMulticheck) == false){
					childMultichecksToRemove.add(linkedMulticheck);
					additionalRebuildNeeded = true;
				}
			}
			
			currentObject.childMultichecks.removeAll(childMultichecksToRemove);
						
			//now draw object
			
			
			
			
			
			
			System.out.println(currentObject.text + " is multicheck: "
					+ currentObject.isMulticheck);

			controller.addWidget(currentObject, 10 + columnElement * 120,
					250 - yposdelta);
			
			// add icon for combination type
			
			if (currentObject.combination.equals("AND")){
				currentObject.showAndIcon();
				controller.addWidget(currentObject.andIcon, 10 + columnElement * 120, 250 - yposdelta - 17);	
			}
			
			if (currentObject.combination.equals("OR")){
				currentObject.showOrIcon();
				controller.addWidget(currentObject.orIcon, 10 + columnElement * 120, 250 - yposdelta - 17);	
			}
					
			
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
						multicheckNewDialogBox.addStyleName("ontop");

						multicheckNewDialogBox
								.addCloseHandler(new CloseHandler<PopupPanel>() {

									@Override
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
														newMultichecks.add(currentObject);
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
										
										 
										
										
										setEditClickHandler(currentObject, multicheckLinkedList);
										
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
										
										
										rebuildCheckpathDiagram();
									}

								});

						addDragSource(currentObject);
						
						
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

						updatedMultichecks.add(currentObject);
						addDragSource(currentObject);
						
					}

					if (currentObject.level == multicheckColumns.get(
							columnElement).size()
							&& multicheckColumns.get(columnElement).size() < 4) {
						SameLevelCheckpathObject addNextLevelField = new SameLevelCheckpathObject(
								null, "empty logic check", true,
								currentObject.level + 1);
						multicheckColumns.get(columnElement).add(
								addNextLevelField);

					}

					rebuildCheckpathDiagram();

				}

			};
			target.setGroup("multicheck");
			target.setOverStyle("drag-ok");

			// determine if AND or OR icons need to be displayed, does not work yet
		

						

		}
		
		

		return checkColumn;

	}

	private void showUpdateMulticheckDialog(final SameLevelCheckpathObject currentCheck, final MulticheckColumn<SameLevelCheckpathObject> currentColumn) {

		final MulticheckDialogBox multicheckDialogBox = new MulticheckDialogBox();

		multicheckDialogBox.setModal(true);
		
		multicheckDialogBox.setAutoHideEnabled(true);

		multicheckDialogBox.setAnimationEnabled(true);

		multicheckDialogBox.setPopupPosition(
				(RootPanel.get().getOffsetWidth()) / 3,
				(RootPanel.get().getOffsetHeight()) / 4);
		multicheckDialogBox.show();
		multicheckDialogBox.addStyleName("ontop");
		
		

		HashMap<String, String> allChecks = new HashMap<String, String>();

		Iterator<SameLevelCheckpathObject> it = currentCheck.childChecks.iterator();
		while (it.hasNext()) {
			SameLevelCheckpathObject check = it.next();
			allChecks.put(check.checkId, check.text);
		}

		Iterator<SameLevelCheckpathObject> mit = currentCheck.childMultichecks.iterator();
		while (mit.hasNext()) {
			SameLevelCheckpathObject multicheck = mit.next();
			allChecks.put(multicheck.checkId, multicheck.text);
		}

		multicheckDialogBox.setParameters(currentCheck.checkId, currentCheck.text, currentCheck.combination, "1",
				allChecks);
		
		multicheckDialogBox
		.addCloseHandler(new CloseHandler<PopupPanel>() {

			public void onClose(CloseEvent event) {

				if (multicheckDialogBox.deleteFlag == true) {
					//do delete actions
					deletedMultichecks.add(currentCheck);
					currentColumn.remove(currentCheck);
					rebuildCheckpathDiagram();	
					
				} else
				{
					//do save update actions
					currentCheck.setText(multicheckDialogBox.multicheckTitle); 
					currentCheck.combination = multicheckDialogBox.combination;
					
					
					
					updatedMultichecks.add(currentCheck);
					rebuildCheckpathDiagram();	
				}
				
			}

		});

		
		

	}

	private void drawConnectorLines(DiagramController controller) {

		// System.out.println("Multicheck Columns: " + multicheckColumns);
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

	private void loadCheckpathJSON(String checkpathId)
	{
		
		final AnimationLoading loading = new AnimationLoading();
		showLoadAnimation(loading);
		
		rpcServiceCheckPath
		.getUiObjectJSONForCheckpathID(
				checkpathId,
				new AsyncCallback<CheckPathObjectTree>() {

					@Override
					public void onFailure(
							Throwable caught) {
						// TODO
						// Auto-generated
						// method
						// stub
						System.out
								.println("ERROR LOADING JSON: "
										+ caught);

					}

					@Override
					public void onSuccess(
							CheckPathObjectTree result) {
					
						System.out.println("JSON RETRIEVED SUCCESSFULLY: "+ result.tree);
			
						// first round - fill objects without relationship information to populate the entire graph and create all necessary objects
						// child check ids are added to a hashmap for later use (currently unused)
						// Multichecklookup is created to easily lookup multichecks in round two
						
						
						HashMap<String, List<String>> childChecksForMulticheck = new HashMap<String, List<String>>();
						HashMap<String, List<String>> childMultichecksForMulticheck = new HashMap<String, List<String>>();
						HashMap<String, SameLevelCheckpathObject> multicheckLookup = new HashMap<String, SameLevelCheckpathObject>();
						final HashMap<String, SameLevelCheckpathObject> checkLookup = new HashMap<String, SameLevelCheckpathObject>();
						
						Iterator<CheckPathObjectColumn> rIT = result.tree.iterator();
											
						while (rIT.hasNext()) {
							CheckPathObjectColumn columnObject = rIT.next();
							Iterator<CheckPathObjectData> cIT = columnObject.column.iterator();
							MulticheckColumn<SameLevelCheckpathObject> newMulticheckList = new MulticheckColumn<SameLevelCheckpathObject>(true);
							
							while (cIT.hasNext()) {
								CheckPathObjectData field = cIT.next();
								System.out.println("Field retrieved: "+ field.text+ "with ID "+ field.checkId);
								SameLevelCheckpathObject newMulticheck = new SameLevelCheckpathObject(field.checkId , field.text, field.empty, field.level);
								newMulticheck.combination = field.combination;
								System.out.println("Checkpathobject created: "+  newMulticheck.text + "with ID "+ newMulticheck.checkId);
								newMulticheck.isMulticheck = true;
								
								// add dragability
								addDragSource(newMulticheck);
								
								// add clickhandler for opening editing box
								
								
								
								setEditClickHandler(newMulticheck, newMulticheckList);
																
								// add multicheck to lookup table
								multicheckLookup.put(field.checkId, newMulticheck);
					        								
								
								List<String> foundChildChecks = new ArrayList<String>();								
								Iterator<String> ccIT = field.childChecks.iterator();
								
								
								// find child checks and store in lookup list;
								while (ccIT.hasNext()) {
									String foundChildCheck = ccIT.next();
									System.out.println("Child Check found: "+ foundChildCheck);
									foundChildChecks.add(foundChildCheck);								
								}
								
								childChecksForMulticheck.put(field.checkId, foundChildChecks);
								

								// find child multichecks and store in lookup list;
								List<String> foundChildMultichecks = new ArrayList<String>();
								Iterator<String> cmcIT = field.childMultichecks.iterator();
								while (cmcIT.hasNext()) {
									String foundChildMulticheck = cmcIT.next();
									System.out.println("Child Multi Check found: "+ foundChildMulticheck);
									foundChildMultichecks.add(foundChildMulticheck);
									
								}
								childMultichecksForMulticheck.put(field.checkId, foundChildMultichecks);
								
								
								newMulticheckList.add(newMulticheck);

							}
						
							multicheckColumns.add(newMulticheckList);
							
						}
							
											
						System.out.println("MCC: " + multicheckColumns);
						System.out.println("*/*" + childMultichecksForMulticheck +" / " + childChecksForMulticheck);
						

						// second round - add relationship data from lookup table
						
						
						Iterator<CheckPathObjectColumn> relIT = result.tree.iterator();
						final List<String> childrenAlreadyAdded = new ArrayList<String>();
						final List<String> childrenAlreadyLinked = new ArrayList<String>();
						
						while (relIT.hasNext()) {
							CheckPathObjectColumn columnObject = relIT.next();
							Iterator<CheckPathObjectData> cIT = columnObject.column.iterator();
														
							while (cIT.hasNext()) {
								final CheckPathObjectData field = cIT.next();
								System.out.println("Field retrieved for relationship update: "+ field.text+ "with ID "+ field.checkId);
								
								final SameLevelCheckpathObject objectToUpdate = multicheckLookup.get(field.checkId);
								
								// for multichecks
								Iterator<String> childMultichecksIT = field.childMultichecks.iterator();
								while(childMultichecksIT.hasNext()){
									String childMulticheckID = 	childMultichecksIT.next();
									SameLevelCheckpathObject childMulticheckObject = multicheckLookup.get(childMulticheckID);
									objectToUpdate.childMultichecks.add(childMulticheckObject);
									System.out.println("Adding multicheck children for " + field.checkId + ": " + childMulticheckObject);
									
								}
								
								
								// for baselayer checks
								
								// build hashmap to be able to look up baselayer checks by checkid
						
								
				
								
								Iterator<String> childChecksIT = field.childChecks.iterator();
								
								
								
								while(childChecksIT.hasNext()){
									final String childCheckID = childChecksIT.next();
									
									rpcServiceCheck.getCheckNameForCheckID(childCheckID, 
											new AsyncCallback<String>() {

												@Override
												public void onFailure(
														Throwable caught) {
													// TODO
													// Auto-generated
													// method
													// stub
													System.out.println("ERROR RETRIEVING CHECK NAME: " + caught);

												}

												@Override
												public void onSuccess(
														String result) {
													// add to chart
													
													System.out.println("Already contains: " + objectToUpdate.checkId+":"+childCheckID);
													
													if (childrenAlreadyLinked.contains(objectToUpdate.checkId+":"+childCheckID) == false)
													{
														
														
														// add check element to graph only if it does not already exist
														if (childrenAlreadyAdded.contains(childCheckID)){
															
															System.out.println("LINKING CHILDEN: "+ childCheckID);
															SameLevelCheckpathObject childCheckObject = checkLookup.get(childCheckID);
															objectToUpdate.childChecks.add(childCheckObject);
															childrenAlreadyLinked.add(childCheckID);
															System.out.println("HASHSET: " + checkHashSet);
														}
														else
														{	
															
															SameLevelCheckpathObject childCheckObject = new SameLevelCheckpathObject(childCheckID, result, true, 0);
															checkLookup.put(childCheckID, childCheckObject);
															checkHashSet.add(childCheckObject);
															objectToUpdate.childChecks.add(childCheckObject);
															childrenAlreadyLinked.add(objectToUpdate.checkId+":"+childCheckID);
															childrenAlreadyAdded.add(childCheckID);
															System.out.println("Adding check children for " + field.checkId + ": " + childCheckObject);
														}
														// add link between parent and children in all cases
														
														
														removeLoadAnimation(loading);
														rebuildCheckpathDiagram();

													}
													
														
												}
											});
								}
							}
						}
						 
						// rebuildCheckpathDiagram();
					}

				});
		removeLoadAnimation(loading);
	}
	
	
	
	private void addDragSource(final SameLevelCheckpathObject currentObject){
		// add drag source to multicheck
		
		final SafeHtmlBuilder builder = new SafeHtmlBuilder();
		builder.appendHtmlConstant("<div style=\"border:1px solid #ddd;cursor:default\" class=\""
				+ "\">");
		builder.appendHtmlConstant("Drag " + currentObject.text
				+ " to a logic check field to build your logic");
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

	private void setEditClickHandler(final SameLevelCheckpathObject currentObject, final MulticheckColumn<SameLevelCheckpathObject> currentColumn){

		if (currentObject.empty == false)
		{
			currentObject.ancTextField
			.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(
						ClickEvent event) {

					showUpdateMulticheckDialog(
							currentObject, currentColumn);

				}

			});
			
		}
		}

	private void drawInitialCheckTarget(){

		final SameLevelCheckpathObject addCheckField = new SameLevelCheckpathObject(
				null, "empty sensor check", true, 0);
		controller.addWidget(addCheckField, 10, 300);

		// define dnd target for checks
		
		DropTarget checkTarget = new DropTarget(addCheckField) {
			@Override
			protected void onDragDrop(DndDropEvent event) {
				super.onDragDrop(event);

				// do the drag and drop visual action

				DragobjectContainer dragAccordion = (DragobjectContainer) event
						.getData();

				addCheckField.setText(dragAccordion.checkName);
				addCheckField.setCheckID(dragAccordion.checkID);
				addCheckField.empty = false;
				checkHashSet.add(addCheckField);

				SameLevelCheckpathObject addNextLevelField = new SameLevelCheckpathObject(
						null, "empty logic check", true, 1);

				// also add an entire multicheck column
				MulticheckColumn<SameLevelCheckpathObject> multicheckList = new MulticheckColumn<SameLevelCheckpathObject>(
						true);
				multicheckList.add(addNextLevelField);
				multicheckColumns.add(multicheckList);
				rebuildCheckpathDiagram();

			}

		};
		checkTarget.setGroup("firstlevel");
		checkTarget.setOverStyle("drag-ok");
		
		
	}
	

	private void addDndTargetForAction(final SameLevelCheckpathObject currentObject)
	{
	
	// define dnd target for actions, but only if target field is not empty
		
	DropTarget actionTarget = new DropTarget(currentObject) {
		@Override
		protected void onDragDrop(DndDropEvent event) {
			super.onDragDrop(event);
			
			System.out.println("Dropped action!");

			final RuleDialogBox ruleNewDialogBox = new RuleDialogBox();

			ruleNewDialogBox.setModal(true);
			ruleNewDialogBox.setAutoHideEnabled(true);

			ruleNewDialogBox.setAnimationEnabled(true);

			ruleNewDialogBox.setPopupPosition(
					(RootPanel.get().getOffsetWidth()) / 3,
					(RootPanel.get().getOffsetHeight()) / 4);
			ruleNewDialogBox.show();
			ruleNewDialogBox.addStyleName("ontop");

			currentObject.showActionIcon();
			
			rebuildCheckpathDiagram();


		}

	};
	actionTarget.setGroup("actors");
	actionTarget.setOverStyle("drag-ok");

	}
	
	
	private void showLoadAnimation(AnimationLoading animationLoading) {
		RootPanel rootPanel = RootPanel.get("main");
		rootPanel.getElement().getStyle().setPosition(Position.RELATIVE);
		rootPanel.add(animationLoading, 25, 40);
	}

	private void removeLoadAnimation(AnimationLoading animationLoading) {
		if (animationLoading != null)
			animationLoading.removeFromParent();
	}
	
	public List<SameLevelCheckpathObject> getUpdatedMultichecks(){
		return this.updatedMultichecks;	
	}

	public List<SameLevelCheckpathObject> getNewMultichecks(){
		return this.newMultichecks;	
	}
	
	public List<SameLevelCheckpathObject> getDeletedMultichecks(){
		return this.deletedMultichecks;	
	}

	public void resetUpdatedMultichecks(){
		this.updatedMultichecks.clear();
	}

	public void resetNewMultichecks(){
		this.newMultichecks.clear();
	}
	public void resetDeletedMultichecks(){
		this.deletedMultichecks.clear();
	}
	
}
