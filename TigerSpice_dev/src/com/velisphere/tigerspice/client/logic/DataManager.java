package com.velisphere.tigerspice.client.logic;

import java.util.Iterator;
import java.util.LinkedList;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.velisphere.tigerspice.client.appcontroller.SessionHelper;
import com.velisphere.tigerspice.client.checks.CheckService;
import com.velisphere.tigerspice.client.checks.CheckServiceAsync;
import com.velisphere.tigerspice.client.event.DraggedInCanvasEvent;
import com.velisphere.tigerspice.client.event.DraggedInCanvasEventHandler;
import com.velisphere.tigerspice.client.event.EventUtils;
import com.velisphere.tigerspice.client.event.JSONreadyEvent;
import com.velisphere.tigerspice.client.event.JSONreadyEventHandler;
import com.velisphere.tigerspice.client.logic.connectors.ConnectorLogicCheckActor;
import com.velisphere.tigerspice.client.logic.connectors.ConnectorSensorActor;
import com.velisphere.tigerspice.client.logic.connectors.ConnectorSensorLogicCheck;
import com.velisphere.tigerspice.client.logic.draggables.PhysicalItem;
import com.velisphere.tigerspice.client.logic.layoutWidgets.LogicCanvas;
import com.velisphere.tigerspice.client.rules.CheckPathService;
import com.velisphere.tigerspice.client.rules.CheckPathServiceAsync;
import com.velisphere.tigerspice.shared.ActionObject;

public class DataManager {

	LogicCanvas canvas;

	public DataManager(LogicCanvas canvas) {
		this.canvas = canvas;

	}

	public void processCheckPath(final String checkpathName) {

		// open JSON factory

		final JsonFabrik factory = new JsonFabrik(canvas);

		// wait for JSON to be generated

		HandlerRegistration jsonReadyHandler = EventUtils.EVENT_BUS.addHandler(
				JSONreadyEvent.TYPE, new JSONreadyEventHandler() {

					@Override
					public void onJSONready(JSONreadyEvent jsonReadyEvent) {

						// send to database
						CheckPathServiceAsync checkpathService = GWT
								.create(CheckPathService.class);

						checkpathService.addNewCheckpath(checkpathName,
								SessionHelper.getCurrentUserID(),
								canvas.getUUID(), factory.getJSON(),
								new AsyncCallback<String>() {

									@Override
									public void onFailure(Throwable caught) {
										// TODO Auto-generated method stub

									}

									@Override
									public void onSuccess(String result) {
										// TODO Auto-generated method stub
										RootPanel.get().add(
												new HTML(
														"Result from attempt to create checkpath: "
																+ result));
									}

								});

					}

				});

	}

	public void processP2P() {
		Iterator<ConnectorSensorActor> it = canvas.getConnectorsSensorActor()
				.iterator();

		while (it.hasNext()) {

			ConnectorSensorActor current = it.next();

			// build action object
			ActionObject action = new ActionObject();
			action.actionID = current.getActionUUID();
			action.actionName = "unused";
			action.endpointClassID = current.getActor().getEndpointClassID();
			action.endpointID = current.getActor().getEndpointID();
			action.endpointName = current.getActor().getContentRepresentation();
			action.propertyID = current.getActor().getPropertyID();
			action.propertyIdIntake = current.getSensor().getPropertyID();
			action.sensorEndpointID = current.getSensor().getEndpointID();
			action.settingSourceIndex = current.getSourceIndex();
			action.validValueIndex = current.getTypicalValueIndex();
			action.manualValue = current.getManualValue();

			// TODO this can be simplified - we do not need to take care of
			// multiple actions in new setup

			LinkedList<ActionObject> actions = new LinkedList<ActionObject>();

			actions.add(action);

			// send to database

			CheckServiceAsync checkService = GWT.create(CheckService.class);

			checkService.addNewCheck(current.getCheckUUID(), current
					.getSensor().getEndpointID(), current.getSensor()
					.getPropertyID(), current.getCheckValue(), current
					.getOperator(), "unused", canvas.getUUID(), actions,
					new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onSuccess(String result) {
							// TODO Auto-generated method stub

							RootPanel.get().add(
									new HTML(
											"Result from attempt to generate check and action P2P: "
													+ result));
						}

					});

		}
	}

	public void processP2L() {

		Iterator<ConnectorSensorLogicCheck> it = canvas
				.getConnectorsSensorLogicCheck().iterator();

		while (it.hasNext()) {

			ConnectorSensorLogicCheck current = it.next();

			// TODO this can be simplified - we do not need to take care of
			// multiple actions in new setup

			// build empty action list object as no actions will be linked to
			// this check

			LinkedList<ActionObject> actions = new LinkedList<ActionObject>();

			// send check to database

			CheckServiceAsync checkService = GWT.create(CheckService.class);

			checkService.addNewCheck(current.getCheckUUID(), current
					.getSensor().getEndpointID(), current.getSensor()
					.getPropertyID(), current.getCheckValue(), current
					.getOperator(), "unused", canvas.getUUID(), actions,
					new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onSuccess(String result) {
							// TODO Auto-generated method stub

							RootPanel.get().add(
									new HTML(
											"Result from attempt to generate check and action P2L: "
													+ result));
						}

					});

		}
	}

	public void processL2P() {
		Iterator<ConnectorLogicCheckActor> it = canvas
				.getConnectorsLogicCheckActor().iterator();

		while (it.hasNext()) {

			ConnectorLogicCheckActor current = it.next();

			// TODO this can be simplified - we do not need to take care of
			// multiple actions in new setup

			// build action object
			ActionObject action = new ActionObject();
			action.actionID = current.getActionUUID();
			action.actionName = "unused";
			action.endpointClassID = current.getActor().getEndpointClassID();
			action.endpointID = current.getActor().getEndpointID();
			action.endpointName = current.getActor().getContentRepresentation();
			action.propertyID = current.getActor().getPropertyID();
			action.propertyIdIntake = "";
			action.sensorEndpointID = "";
			action.settingSourceIndex = current.getSourceIndex();
			action.validValueIndex = current.getTypicalValueIndex();
			action.manualValue = current.getManualValue();

			// TODO this can be simplified - we do not need to take care of
			// multiple actions in new setup

			LinkedList<ActionObject> actions = new LinkedList<ActionObject>();

			actions.add(action);

			// determine operator

			String operator = "";

			if (current.getLogicCheck().isAnd())
				operator = "AND";
			if (current.getLogicCheck().isOr())
				operator = "OR";

			// send multicheck to database

			CheckPathServiceAsync checkpathService = GWT
					.create(CheckPathService.class);

			checkpathService.addNewMulticheck(current.getCheckUUID(), operator,
					"unused", canvas.getUUID(), actions,
					new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onSuccess(String result) {
							// TODO Auto-generated method stub

							RootPanel.get().add(
									new HTML(
											"Result from attempt to generate check and action L2P: "
													+ result));
						}

					});

			// link checks

			Iterator<ConnectorSensorLogicCheck> pit = current.getLogicCheck()
					.getChildConnectors().iterator();

			while (pit.hasNext()) {
				checkpathService.addNewMulticheckCheckLink(
						current.getCheckUUID(), pit.next().getCheckUUID(),
						canvas.getUUID(), new AsyncCallback<String>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onSuccess(String result) {
								// TODO Auto-generated method stub
								RootPanel.get().add(
										new HTML(
												"Result from attempt to link checks and to multicheck: "
														+ result));
							}

						});
			}

		}
	}

}
