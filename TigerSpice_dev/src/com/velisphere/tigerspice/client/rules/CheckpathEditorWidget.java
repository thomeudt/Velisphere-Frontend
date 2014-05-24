/*******************************************************************************
 * CONFIDENTIAL INFORMATION
 *  __________________
 *  
 *   Copyright (C) 2014 Thorsten Meudt / Connected Things Lab
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
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.shared.HandlerRegistration;
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
import com.velisphere.tigerspice.client.checks.CheckDialogBox;
import com.velisphere.tigerspice.client.checks.CheckService;
import com.velisphere.tigerspice.client.checks.CheckServiceAsync;
import com.velisphere.tigerspice.client.event.CheckpathCalculatedEvent;
import com.velisphere.tigerspice.client.event.EventUtils;
import com.velisphere.tigerspice.client.event.SessionVerifiedEvent;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.helper.DragobjectContainer;
import com.velisphere.tigerspice.client.helper.UuidService;
import com.velisphere.tigerspice.client.helper.UuidServiceAsync;
import com.velisphere.tigerspice.client.images.Images;
import com.velisphere.tigerspice.shared.ActionObject;
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
	private List<SameLevelCheckpathObject> updatedChecks;
	private List<SameLevelCheckpathObject> newChecks;
	private List<SameLevelCheckpathObject> deletedChecks;
	private Boolean additionalRebuildNeeded;
	private DiagramController controller;
	private int checkCount = 0;
	private int multicheckCount = 0;
	private float cost;
	
	
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
		this.updatedChecks = new ArrayList<SameLevelCheckpathObject>();
		this.newChecks = new ArrayList<SameLevelCheckpathObject>();
		this.deletedChecks = new ArrayList<SameLevelCheckpathObject>();

		con = new FlowLayoutContainer();

		initWidget(con);

		checkHashSet = new LinkedHashSet<SameLevelCheckpathObject>();

		multicheckColumns = new LinkedList<MulticheckColumn<SameLevelCheckpathObject>>();

		// load checkpath from database if a checkpath id is provided (open
		// existing)

		if (this.checkpathID != null) {
			loadCheckpathJSON(this.checkpathID);
		}

	

		additionalRebuildNeeded = false;
		
		int checkCount = 0;
		int multicheckCount = 0;
		
		
		rebuildCheckpathDiagram();
		
		
		
		
		
		

	}

	private float calculateCost()
	{		
		this.cost = checkCount * 1 + multicheckCount * 10;
		EventUtils.EVENT_BUS.fireEvent(new CheckpathCalculatedEvent());
		return cost;
		
	}
	
	public float getCost()
	{
		return this.cost;
	}
	
	
	private void rebuildCheckpathDiagram() {

		
		System.out.println("[IN] Rebuilding checkpath.");
		
		multicheckCount = 0;
		checkCount = 0;
		
		// Draw basic layout boxes

		final VerticalLayoutContainer container = new VerticalLayoutContainer();

		container.setBorders(false);
		
		container.setScrollMode(ScrollSupport.ScrollMode.AUTO);
		container.setHeight((int) 400);
		container.setWidth((int) ((RootPanel.get().getOffsetWidth()) / 1.74));
		
		int canvasWidthExpansion = 0;
		if (checkHashSet.size() > 7){
			canvasWidthExpansion = (checkHashSet.size()-7)*175;
		}
		controller = new DiagramController((int) (RootPanel.get().getOffsetWidth() / 1.74) + canvasWidthExpansion, (int) 375);
		
		
		controller.showGrid(true); // Display a background grid
		
		controller.setAllowingUserInteractions(false); // prevent user from
														// dragging the lines
														// manually

		container.add(controller.getView());
		container.setTitle("Drag and Drop sensors and actors onto the canvas to build your logic.");
	
		// draw checks

		drawCheckLevel();

		
		
		// check if new column needs to be added

		if (multicheckColumns.size() > 0) {
			System.out.println("[IN] Processing multi check columns: " + multicheckColumns.size());

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
			System.out.println("[IN] Additional rebuild of checkpath diagram.");
			rebuildCheckpathDiagram();
		}
		
		System.out.println("[IN] Cost for checkpath execution: " +calculateCost());

	}
	

	private void drawCheckLevel(){
	
	Iterator<SameLevelCheckpathObject> it = checkHashSet.iterator();

	// draw first d&d target field if checkHashSet is empty

	int xpos = 10;
	int ypos = 320;

	if (it.hasNext() == false) {
		drawCheckTarget(xpos, ypos);
	}

	// draw the first level - "checks" - as the foundation layer by reading
	// the data stored in the checkHashSet

	while (it.hasNext()) {
		final SameLevelCheckpathObject currentObject = it.next();
		
		++checkCount;

		addDragSource(currentObject);
		addDndTargetForAction(currentObject);

		controller.addWidget(currentObject, xpos, ypos);

		// add icon for action loaded checks, for now just move into
		// position

		controller
				.addWidget(currentObject.actionIcon, xpos + 83, ypos - 19);
		
		controller.addWidget(currentObject.sensorIcon, xpos, ypos - 15);
		currentObject.sensorIcon.setVisible(true);
		// increment horizontal positon

		xpos = xpos + 120;

		// if no more items are left in the check hashset, draw a new drag
		// target box as the last step to allow for adding a new check to
		// the checkpath

		if (it.hasNext() == false) {
			drawCheckTarget(xpos, ypos);

		}

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

			// increment multicheck count for cost calculation, if field is not empty
			
			if (currentObject.empty == false) ++multicheckCount;
			
			
			// correct level setting of current object within column, might have
			// changed due to child deletions
			// this can be further optimized in the future to allow moving
			// elements vertically in diagram

			if (multicheckLinkedList.indexOf(currentObject) != currentObject.level - 1) {
				currentObject.level = multicheckLinkedList
						.indexOf(currentObject) + 1;
			}

			// cleanup orphan child multicheck links from prior deletions

			List<SameLevelCheckpathObject> allMultichecksForInspection = new ArrayList<SameLevelCheckpathObject>();

			for (int i = 1; i <= multicheckColumns.size(); i = i + 1) {
				allMultichecksForInspection
						.addAll(multicheckColumns.get(i - 1));
			}

			additionalRebuildNeeded = false;
			List<SameLevelCheckpathObject> childMultichecksToRemove = new ArrayList<SameLevelCheckpathObject>();
			Iterator<SameLevelCheckpathObject> linkedMultichecksIT = currentObject.childMultichecks
					.iterator();
			while (linkedMultichecksIT.hasNext()) {
				SameLevelCheckpathObject linkedMulticheck = linkedMultichecksIT
						.next();
				if (allMultichecksForInspection.contains(linkedMulticheck) == false) {
					childMultichecksToRemove.add(linkedMulticheck);
					updatedMultichecks.add(currentObject);
					additionalRebuildNeeded = true;
				}
			}

			currentObject.childMultichecks.removeAll(childMultichecksToRemove);

			// cleanup orphan child check links from prior deletions

			additionalRebuildNeeded = false;
			List<SameLevelCheckpathObject> childChecksToRemove = new ArrayList<SameLevelCheckpathObject>();
			Iterator<SameLevelCheckpathObject> linkedChecksIT = currentObject.childChecks
					.iterator();
			while (linkedChecksIT.hasNext()) {
				SameLevelCheckpathObject linkedCheck = linkedChecksIT.next();
				if (checkHashSet.contains(linkedCheck) == false) {
					childChecksToRemove.add(linkedCheck);
					updatedMultichecks.add(currentObject);
					additionalRebuildNeeded = true;
				}
			}

			currentObject.childChecks.removeAll(childChecksToRemove);

			// now draw object

			
			controller.addWidget(currentObject, 10 + columnElement * 120,
					250 - yposdelta);

			// add icon for combination type

			if (currentObject.combination.equals("AND")) {
				currentObject.showAndIcon();
				controller.addWidget(currentObject.andIcon,
						10 + columnElement * 120, 250 - yposdelta - 17);
			}

			if (currentObject.combination.equals("OR")) {
				currentObject.showOrIcon();
				controller.addWidget(currentObject.orIcon,
						10 + columnElement * 120, 250 - yposdelta - 17);
			}
			
			// add action icon
			
			controller
			.addWidget(currentObject.actionIcon, 93 + columnElement * 120, 250 - yposdelta - 19);

			yposdelta = yposdelta + 70;
			
			addDndMulticheckTargetForAction(currentObject);

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

						LinkedList<ActionObject> tempActions = new LinkedList<ActionObject>();
						if (currentObject.actions.size() == 0){
							tempActions = null;
						} else
						{
							tempActions.addAll(currentObject.actions);
						}
						
						
						final MulticheckDialogBox multicheckNewDialogBox = new MulticheckDialogBox(currentObject.checkId,	currentObject.text, currentObject.combination, "1", null, tempActions);

						multicheckNewDialogBox.setModal(true);
						multicheckNewDialogBox.setAutoHideEnabled(true);

						//multicheckNewDialogBox.setAnimationEnabled(true);

						multicheckNewDialogBox.show();
						multicheckNewDialogBox.addStyleName("ontop");
						multicheckNewDialogBox.center();

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
														newMultichecks
																.add(currentObject);
														System.out
																.println("[IN] UUID for new Multicheck generated: "
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

										setMulticheckEditClickHandler(
												currentObject,
												multicheckLinkedList);

										if (dragAccordion.isMulticheck) {
											currentObject
													.addChildMulticheck(dragAccordion.checkpathObject);

										} else {
											currentObject
													.addChildCheck(dragAccordion.checkpathObject);

										}

										System.out.println("[IN] New Multicheck Title: "
												+ currentObject.text);
										System.out.println("[IN] New Multicheck Combination: "
												+ currentObject.combination);
										System.out
												.println("[IN] Child Multichecks: "
														+ currentObject.childMultichecks);
										System.out.println("[IN] Child Checks: "
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

			// determine if AND or OR icons need to be displayed, does not work
			// yet

		}

		return checkColumn;

	}

	private void showUpdateMulticheckDialog(
			final SameLevelCheckpathObject currentCheck,
			final MulticheckColumn<SameLevelCheckpathObject> currentColumn) {

		HashMap<String, String> allChecks = new HashMap<String, String>();

		Iterator<SameLevelCheckpathObject> it = currentCheck.childChecks
				.iterator();
		while (it.hasNext()) {
			SameLevelCheckpathObject check = it.next();
			allChecks.put(check.checkId, check.text);
		}

		Iterator<SameLevelCheckpathObject> mit = currentCheck.childMultichecks
				.iterator();
		while (mit.hasNext()) {
			SameLevelCheckpathObject multicheck = mit.next();
			allChecks.put(multicheck.checkId, multicheck.text);
		}


		LinkedList<ActionObject> tempActions = new LinkedList<ActionObject>();
		if (currentCheck.actions.size() == 0){
			tempActions = null;
		} else
		{
			tempActions.addAll(currentCheck.actions);
		}
		

		
		final MulticheckDialogBox multicheckDialogBox = new MulticheckDialogBox(currentCheck.checkId,
				currentCheck.text, currentCheck.combination, "1", allChecks, tempActions);

		multicheckDialogBox.setModal(true);

		multicheckDialogBox.setAutoHideEnabled(true);

		//multicheckDialogBox.setAnimationEnabled(true);

		multicheckDialogBox.show();
		multicheckDialogBox.addStyleName("ontop");
		multicheckDialogBox.center();

		//multicheckDialogBox.setParameters(currentCheck.checkId,	currentCheck.text, currentCheck.combination, "1", allChecks, currentCheck.actions);

		multicheckDialogBox.addCloseHandler(new CloseHandler<PopupPanel>() {

			public void onClose(CloseEvent event) {

				if (multicheckDialogBox.deleteFlag == true) {
					// do delete actions
					deletedMultichecks.add(currentCheck);
					if (newMultichecks.contains(currentCheck)) newMultichecks.remove(currentCheck);
					if (updatedMultichecks.contains(currentCheck)) updatedMultichecks.remove(currentCheck);
					currentColumn.remove(currentCheck);
					rebuildCheckpathDiagram();

				} else if (multicheckDialogBox.cancelFlag == true) {
					// do nothing

				} else {
					// do save update actions
					currentCheck.setText(multicheckDialogBox.multicheckTitle);
					currentCheck.combination = multicheckDialogBox.combination;

					
					if (deletedMultichecks.contains(currentCheck)) deletedMultichecks.remove(currentCheck);

					if (newMultichecks.contains(currentCheck) == false)	updatedMultichecks.add(currentCheck);
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

	private void loadCheckpathJSON(final String checkpathId) {

		final AnimationLoading loading = new AnimationLoading();
		showLoadAnimation(loading);

		rpcServiceCheckPath.getUiObjectJSONForCheckpathID(checkpathId,
				new AsyncCallback<CheckPathObjectTree>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO
						// Auto-generated
						// method
						// stub
						System.out.println("[ER] ERROR LOADING JSON: " + caught);

					}

					@Override
					public void onSuccess(CheckPathObjectTree result) {

						System.out.println("[IN] JSON RETRIEVED SUCCESSFULLY: "
								+ result.tree);

						// first round - fill objects without relationship
						// information to populate the entire graph and create
						// all necessary objects
						// child check ids are added to a hashmap for later use
						// (currently unused)
						// Multichecklookup is created to easily lookup
						// multichecks in round two

						// get baselayer

						final HashMap<String, SameLevelCheckpathObject> baseLayerMap = new HashMap<String, SameLevelCheckpathObject>();

						Iterator<CheckPathObjectData> bIT = result.baseLayer
								.iterator();

						while (bIT.hasNext()) {
							CheckPathObjectData baseElementData = bIT.next();
							final SameLevelCheckpathObject baseElement = new SameLevelCheckpathObject(
									baseElementData.checkId,
									baseElementData.text,
									baseElementData.empty,
									baseElementData.level);
							baseElement.triggerValue = baseElementData.triggerValue;
							baseElement.propertyID = baseElementData.propertyID;
							baseElement.operator = baseElementData.operator;
							baseElement.endpointID = baseElementData.endpointID;
							
							rpcServiceCheck.getActionsForCheckID(baseElementData.checkId, checkpathId,
									new AsyncCallback<LinkedList<ActionObject>>() {
								
								@Override
								public void onFailure(Throwable caught) {
									// TODO
									// Auto-generated
									// method
									// stub
									System.out.println("[ER] ERROR RETRIEVING ACTIONS FOR CHECK " + caught);

								}

								@Override
								public void onSuccess(LinkedList<ActionObject> actions){
									baseElement.actions = actions;
									Iterator<ActionObject> it = actions.iterator();
									while(it.hasNext()){
										it.next().sensorEndpointID = baseElement.endpointID;
									}
									
		
									if (baseElement.actions.size() != 0) baseElement.showActionIcon();
								}
							});
							
							
							

							baseLayerMap.put(baseElementData.checkId,
									baseElement);

							setCheckEditClickHandler(baseElement);
							
							checkHashSet.add(baseElement);

							// triggervalue operator

						}

						// get columns

						HashMap<String, List<String>> childChecksForMulticheck = new HashMap<String, List<String>>();
						HashMap<String, List<String>> childMultichecksForMulticheck = new HashMap<String, List<String>>();
						HashMap<String, SameLevelCheckpathObject> multicheckLookup = new HashMap<String, SameLevelCheckpathObject>();
						

						Iterator<CheckPathObjectColumn> rIT = result.tree
								.iterator();

						while (rIT.hasNext()) {
							CheckPathObjectColumn columnObject = rIT.next();
							Iterator<CheckPathObjectData> cIT = columnObject.column
									.iterator();
							MulticheckColumn<SameLevelCheckpathObject> newMulticheckList = new MulticheckColumn<SameLevelCheckpathObject>(
									true);

							while (cIT.hasNext()) {
								CheckPathObjectData field = cIT.next();
								System.out.println("[IN] Field from JSON checkpath retrieved: "
										+ field.text + "with ID "
										+ field.checkId);
								final SameLevelCheckpathObject newMulticheck = new SameLevelCheckpathObject(
										field.checkId, field.text, field.empty,
										field.level);
								newMulticheck.combination = field.combination;
								newMulticheck.checkpathID = checkpathId;
								System.out.println("[IN] Checkpathobject from JSON checkpath created: "
										+ newMulticheck.text + "with ID "
										+ newMulticheck.checkId);
								newMulticheck.isMulticheck = true;
								
								
								// add actions to newly created multicheck
								
								rpcServiceCheckPath.getActionsForMulticheckID(newMulticheck.checkId, checkpathId,
										new AsyncCallback<LinkedList<ActionObject>>() {
									
									@Override
									public void onFailure(Throwable caught) {
										// TODO
										// Auto-generated
										// method
										// stub
										System.out.println("[ER] ERROR RETRIEVING ACTIONS FOR CHECK: " + caught);

									}

									@Override
									public void onSuccess(LinkedList<ActionObject> actions){
										newMulticheck.actions = actions;
										
										System.out.println("[IN] ADDING ACTIONS FOR MULTICHECK: " + newMulticheck.checkId);
										System.out.println("[IN] ACTIONS ARE: " + newMulticheck.actions);
										
										/** this is not applicable/not implemented for multichecks yet
										Iterator<ActionObject> it = actions.iterator();
										while(it.hasNext()){
											it.next().sensorEndpointID = newMulticheck.endpointID;
										}
										**/
			
										if (newMulticheck.actions.size() != 0) newMulticheck.showActionIcon();
									}
								});


								// add dragability
								addDragSource(newMulticheck);
								
								

								// add clickhandler for opening editing box

								setMulticheckEditClickHandler(newMulticheck,
										newMulticheckList);

								// add multicheck to lookup table
								multicheckLookup.put(field.checkId,
										newMulticheck);

								List<String> foundChildChecks = new ArrayList<String>();
								Iterator<String> ccIT = field.childChecks
										.iterator();

								// find child checks and store in lookup list;
								while (ccIT.hasNext()) {
									String foundChildCheck = ccIT.next();
									System.out.println("[IN] Child Check for field "+ field.text + " found: "
											+ foundChildCheck);
									foundChildChecks.add(foundChildCheck);
								}

								childChecksForMulticheck.put(field.checkId,
										foundChildChecks);

								// find child multichecks and store in lookup
								// list;
								List<String> foundChildMultichecks = new ArrayList<String>();
								Iterator<String> cmcIT = field.childMultichecks
										.iterator();
								while (cmcIT.hasNext()) {
									String foundChildMulticheck = cmcIT.next();
									System.out
											.println("[IN] Child Multi Check for field "+ field.text + "found: "
													+ foundChildMulticheck);
									foundChildMultichecks
											.add(foundChildMulticheck);

								}
								childMultichecksForMulticheck.put(
										field.checkId, foundChildMultichecks);

								newMulticheckList.add(newMulticheck);

							}

							multicheckColumns.add(newMulticheckList);

						}

						System.out.println("[IN] Total number of multicheck columns: " + multicheckColumns);
						
						

						// second round - add relationship data from lookup
						// table

						Iterator<CheckPathObjectColumn> relIT = result.tree
								.iterator();
						final List<String> childrenAlreadyAdded = new ArrayList<String>();
						final List<String> childrenAlreadyLinked = new ArrayList<String>();

						while (relIT.hasNext()) {
							CheckPathObjectColumn columnObject = relIT.next();
							Iterator<CheckPathObjectData> cIT = columnObject.column
									.iterator();

							while (cIT.hasNext()) {
								final CheckPathObjectData field = cIT.next();
								System.out
										.println("[IN] Field retrieved for relationship update: "
												+ field.text
												+ "with ID "
												+ field.checkId);

								final SameLevelCheckpathObject objectToUpdate = multicheckLookup
										.get(field.checkId);

								// for multichecks
								Iterator<String> childMultichecksIT = field.childMultichecks
										.iterator();
								while (childMultichecksIT.hasNext()) {
									String childMulticheckID = childMultichecksIT
											.next();
									SameLevelCheckpathObject childMulticheckObject = multicheckLookup
											.get(childMulticheckID);
									objectToUpdate.childMultichecks
											.add(childMulticheckObject);
									System.out
											.println("[IN] Adding multicheck children for "
													+ field.checkId
													+ ": "
													+ childMulticheckObject);

								}

								// for baselayer checks

								Iterator<String> childChecksIT = field.childChecks
										.iterator();

								while (childChecksIT.hasNext()) {
									final String childCheckID = childChecksIT
											.next();

									rpcServiceCheck.getCheckNameForCheckID(
											childCheckID,
											new AsyncCallback<String>() {

												@Override
												public void onFailure(
														Throwable caught) {
													// TODO
													// Auto-generated
													// method
													// stub
													System.out
															.println("[ER] ERROR RETRIEVING CHECK NAME: "
																	+ caught);

												}

												@Override
												public void onSuccess(
														String result) {

													System.out
															.println("[IN] Already contains: "
																	+ objectToUpdate.checkId
																	+ ":"
																	+ childCheckID);

													if (childrenAlreadyLinked
															.contains(objectToUpdate.checkId
																	+ ":"
																	+ childCheckID) == false) {

														// link to children

														System.out
																.println("[IN] LINKING CHILDEN: "
																		+ childCheckID);
														SameLevelCheckpathObject childCheckObject = baseLayerMap
																.get(childCheckID);

														objectToUpdate.childChecks
																.add(childCheckObject);
														childrenAlreadyLinked
																.add(childCheckID);
														System.out
																.println("[IN] HASHSET: "
																		+ checkHashSet);

														removeLoadAnimation(loading);
														rebuildCheckpathDiagram();

													}

												}
											});
								}
							}
						}

						rebuildCheckpathDiagram();
					}

				});
		removeLoadAnimation(loading);
	}

	private void addDragSource(final SameLevelCheckpathObject currentObject) {
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
				event.getStatusProxy().update(builder.toSafeHtml());
			}

		};
		currentObject.getElement().getStyle().setCursor(Cursor.POINTER); 
		source.setGroup("multicheck");

	}

	private void setMulticheckEditClickHandler(
			final SameLevelCheckpathObject currentObject,
			final MulticheckColumn<SameLevelCheckpathObject> currentColumn) {

		if (currentObject.empty == false) {
			currentObject.ancTextField.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {

					showUpdateMulticheckDialog(currentObject, currentColumn);

				}

			});

		}
	}

	private void drawCheckTarget(Integer xPos, Integer yPos) {

		final SameLevelCheckpathObject addCheckField = new SameLevelCheckpathObject(
				null, "empty sensor check", true, 0);
		controller.addWidget(addCheckField, xPos, yPos);

		// define dnd target for checks

		DropTarget checkTarget = new DropTarget(addCheckField) {
			@Override
			protected void onDragDrop(DndDropEvent event) {
				super.onDragDrop(event);

				// do the drag and drop visual action

				final DragobjectContainer dragAccordion = (DragobjectContainer) event
						.getData();

				addCheckField.setText(dragAccordion.propertyName);

				// create UUID for check
				rpcServiceUuid.getUuid(new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated
						// method stub

					}

					@Override
					public void onSuccess(String result) {

						addCheckField.setCheckID(result);

					}

				});
				
				
				
				

				final CheckDialogBox checkNewDialogBox = new CheckDialogBox(
						dragAccordion.endpointID, dragAccordion.propertyID,
						dragAccordion.propertyClassID,
						dragAccordion.propertyName, dragAccordion.endpointName,
						"unnamed check", "", "", null);

				checkNewDialogBox.setModal(true);
				//checkNewDialogBox.setAutoHideEnabled(true);

				//checkNewDialogBox.setAnimationEnabled(true);

				
				checkNewDialogBox.show();
				checkNewDialogBox.addStyleName("ontop");
				checkNewDialogBox.center();

				rebuildCheckpathDiagram();

				checkNewDialogBox
						.addCloseHandler(new CloseHandler<PopupPanel>() {

							public void onClose(CloseEvent event) {

								addCheckField.setText(checkNewDialogBox
										.getCheckTitle());
								addCheckField
										.setPropertyID(dragAccordion.propertyID);
								addCheckField
										.setEndpointID(dragAccordion.endpointID);
								addCheckField.setOperator(checkNewDialogBox
										.getOperator());
								addCheckField.setTriggerValue(checkNewDialogBox
										.getTriggerValue());

								checkHashSet.add(addCheckField);
								newChecks.add(addCheckField);
								addCheckField.empty = false;
								SameLevelCheckpathObject addNextLevelField = new SameLevelCheckpathObject(
										null, "empty logic check", true, 1);

								// also add an entire multicheck column
								MulticheckColumn<SameLevelCheckpathObject> multicheckList = new MulticheckColumn<SameLevelCheckpathObject>(
										true);
								multicheckList.add(addNextLevelField);
								multicheckColumns.add(multicheckList);

								setCheckEditClickHandler(addCheckField);

								rebuildCheckpathDiagram();

							}

						});

			}

		};
		checkTarget.setGroup("sensors");
		checkTarget.setOverStyle("drag-ok");

	}

	private void setCheckEditClickHandler(
			final SameLevelCheckpathObject currentObject) {

		currentObject.ancTextField.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				LinkedList<ActionObject> tempActions = new LinkedList<ActionObject>();
				if (currentObject.actions.size() == 0){
					tempActions = null;
				} else
				{
					tempActions.addAll(currentObject.actions);
				}
				

				
				final CheckDialogBox checkEditDialogBox = new CheckDialogBox(
							currentObject.endpointID, currentObject.propertyID, "",
							"", "", currentObject.text, currentObject.triggerValue,
							currentObject.operator, tempActions);
				
				
				checkEditDialogBox.setModal(true);
				// checkEditDialogBox.setAutoHideEnabled(true);

				//checkEditDialogBox.setAnimationEnabled(true);

				
				checkEditDialogBox.show();
				checkEditDialogBox.addStyleName("ontop");
				checkEditDialogBox.center();

				checkEditDialogBox
						.addCloseHandler(new CloseHandler<PopupPanel>() {

							public void onClose(CloseEvent event) {

								if (checkEditDialogBox.deleteFlag == true) {
									// do delete actions
									if (newChecks.contains(currentObject)) newChecks.remove(currentObject);
									if (updatedChecks.contains(currentObject)) updatedChecks.remove(currentObject);
									deletedChecks.add(currentObject);
									checkHashSet.remove(currentObject);
									rebuildCheckpathDiagram();

								} else if (checkEditDialogBox.cancelFlag == true) {
									// do nothing

								} else {
									// do save update actions
									System.out.println("******************DO UPDATE ACTION");
									currentObject.setText(checkEditDialogBox
											.getCheckTitle());
									currentObject
											.setOperator(checkEditDialogBox
													.getOperator());
									currentObject
											.setTriggerValue(checkEditDialogBox
													.getTriggerValue());
									currentObject.actions.clear();
									if (checkEditDialogBox.getActions() != null) currentObject.actions.addAll(checkEditDialogBox.getActions());
									
									if (deletedChecks.contains(currentObject)) deletedChecks.remove(currentObject);
									
									if (newChecks.contains(currentObject) == false) updatedChecks.add(currentObject);
									
									rebuildCheckpathDiagram();
								}

							}
						});

			}

		});

	}

	
	private void addDndTargetForAction(
			final SameLevelCheckpathObject currentObject) {

		// define dnd target for actions, but only if target field is not empty

		DropTarget actionTarget = new DropTarget(currentObject) {
			@Override
			protected void onDragDrop(DndDropEvent event) {
				super.onDragDrop(event);
								
				System.out.println("[IN] Dropped action on check");
				

				final DragobjectContainer dragAccordion = (DragobjectContainer) event.getData();
				
							
				LinkedList<ActionObject> tempActions = new LinkedList<ActionObject>();
				tempActions.addAll(currentObject.actions);
				
				ActionObject newAction = new ActionObject("", "", dragAccordion.endpointName, dragAccordion.endpointID, dragAccordion.endpointClassID, dragAccordion.propertyName, dragAccordion.propertyID, "", "", "", "", currentObject.endpointID);
				tempActions.add(newAction);
				
				//final ActionDialogBoxTabbed actionNewDialogBox = new ActionDialogBoxTabbed(tempActions, currentObject.text);

				final CheckDialogBox checkEditDialogBox = new CheckDialogBox(
						currentObject.endpointID, currentObject.propertyID, "",
						"", "", currentObject.text, currentObject.triggerValue,
						currentObject.operator, tempActions);
				
				checkEditDialogBox.show();
				Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
				    public void execute() {
				    	checkEditDialogBox.center();
				    }
				});  
								
				
				checkEditDialogBox.addStyleName("ontop");
				
				checkEditDialogBox.setActionsTabEnabled();
				
				
				checkEditDialogBox
				.addCloseHandler(new CloseHandler<PopupPanel>() {

					public void onClose(CloseEvent event) {
						
						if (checkEditDialogBox.cancelFlag == true)
						{
								
						} else if (checkEditDialogBox.deleteFlag == true)
						{
							
						}
						else
						{
							
							
							currentObject.actions.clear();
							currentObject.actions.addAll(checkEditDialogBox.getActions());
							if (currentObject.actions.size() > 0) currentObject.showActionIcon();
							
						}							
						
					}
				});


				rebuildCheckpathDiagram();

			}

		};
		actionTarget.setGroup("actors");
		actionTarget.setOverStyle("drag-ok");

	}

	
	private void addDndMulticheckTargetForAction(
			final SameLevelCheckpathObject currentObject) {

		// define dnd target for actions, but only if target field is not empty

		DropTarget actionTarget = new DropTarget(currentObject) {
			@Override
			protected void onDragDrop(DndDropEvent event) {
				super.onDragDrop(event);
								
				System.out.println("[IN] Dropped action into multicheck!");
				

				final DragobjectContainer dragAccordion = (DragobjectContainer) event.getData();
				
							
				LinkedList<ActionObject> tempActions = new LinkedList<ActionObject>();
				tempActions.addAll(currentObject.actions);
				
				ActionObject newAction = new ActionObject("", "", dragAccordion.endpointName, dragAccordion.endpointID, dragAccordion.endpointClassID, dragAccordion.propertyName, dragAccordion.propertyID, "", "", "", "", currentObject.endpointID);
				tempActions.add(newAction);
				

				HashMap<String, String> allChecks = new HashMap<String, String>();

				Iterator<SameLevelCheckpathObject> it = currentObject.childChecks
						.iterator();
				while (it.hasNext()) {
					SameLevelCheckpathObject check = it.next();
					allChecks.put(check.checkId, check.text);
				}

				Iterator<SameLevelCheckpathObject> mit = currentObject.childMultichecks
						.iterator();
				while (mit.hasNext()) {
					SameLevelCheckpathObject multicheck = mit.next();
					allChecks.put(multicheck.checkId, multicheck.text);
				}
				
				
				final MulticheckDialogBox multicheckDialogBox = new MulticheckDialogBox(currentObject.checkId,
						currentObject.text, currentObject.combination, "1", allChecks, tempActions);

				multicheckDialogBox.setModal(true);

				multicheckDialogBox.setAutoHideEnabled(true);

				//multicheckDialogBox.setAnimationEnabled(true);

				multicheckDialogBox.show();
				multicheckDialogBox.addStyleName("ontop");
				Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
				    public void execute() {
				    	multicheckDialogBox.center();
				    }
				});  

				
				multicheckDialogBox.setActionsTabEnabled();
			

				//multicheckDialogBox.setParameters(currentObject.checkId,currentObject.text, currentObject.combination, "1", allChecks, currentObject.actions);

				multicheckDialogBox.addCloseHandler(new CloseHandler<PopupPanel>() {

					public void onClose(CloseEvent event) {

						if (multicheckDialogBox.deleteFlag == true) {
							// do delete actions
							deletedMultichecks.add(currentObject);
							rebuildCheckpathDiagram();

						} else if (multicheckDialogBox.cancelFlag == true) {
							// do nothing

						} else {
							// do save update actions
							currentObject.setText(multicheckDialogBox.multicheckTitle);
							currentObject.combination = multicheckDialogBox.combination;
							currentObject.actions.clear();
							currentObject.actions.addAll(multicheckDialogBox.getActions());
							if (currentObject.actions.size() > 0) currentObject.showActionIcon();
							
			
							updatedMultichecks.add(currentObject);
							rebuildCheckpathDiagram();
						}

					}

				});
			}

		};
		actionTarget.setGroup("actors");
		actionTarget.setOverStyle("drag-ok");

	}

	
	
	private void showLoadAnimation(AnimationLoading animationLoading) {
		
		animationLoading.show();
		/**
		RootPanel rootPanel = RootPanel.get("main");
		rootPanel.getElement().getStyle().setPosition(Position.RELATIVE);
		rootPanel.add(animationLoading, 25, 40);
		**/
		
	}

	private void removeLoadAnimation(AnimationLoading animationLoading) {
		if (animationLoading != null)
			animationLoading.hide();;
			//animationLoading.removeFromParent();
	}

	public List<SameLevelCheckpathObject> getUpdatedMultichecks() {
		return this.updatedMultichecks;
	}

	public List<SameLevelCheckpathObject> getNewMultichecks() {
		return this.newMultichecks;
	}

	public List<SameLevelCheckpathObject> getDeletedMultichecks() {
		return this.deletedMultichecks;
	}

	public List<SameLevelCheckpathObject> getNewChecks() {
		return this.newChecks;
	}

	public List<SameLevelCheckpathObject> getUpdatedChecks() {
		return this.updatedChecks;
	}

	public List<SameLevelCheckpathObject> getDeletedChecks() {
		return this.deletedChecks;
	}

	public String getCheckpathID() {
		return this.checkpathID;
	}

	public void resetUpdatedMultichecks() {
		this.updatedMultichecks.clear();
	}

	public void resetNewMultichecks() {
		this.newMultichecks.clear();
	}

	public void resetDeletedMultichecks() {
		this.deletedMultichecks.clear();
	}

	public void resetUpdatedChecks() {
		this.updatedChecks.clear();
	}

	public void resetNewChecks() {
		this.newChecks.clear();
	}

	public void resetDeletedChecks() {
		this.deletedChecks.clear();
	}

	
}
