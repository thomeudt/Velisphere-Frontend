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
import com.velisphere.tigerspice.shared.SerializableLogicContainer;

public class DataManager {

	HandlerRegistration jsonReadyHandler;
	LogicCanvas canvas;

	public DataManager(LogicCanvas canvas) {
		this.canvas = canvas;

	}

	public void processCheckPath(final String checkpathName) {

		// create JSON factory

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

										RootPanel.get().add(
												new HTML(
														"Error from attempt to create checkpath: "
																+ caught.getMessage()));
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

		RootPanel.get().add(new HTML("P2P: started"));
		
		while (it.hasNext()) {

			ConnectorSensorActor current = it.next();
			
			RootPanel.get().add(new HTML("P2P: fetched connector"));

			// build action object
			ActionObject action = new ActionObject();
			action.actionID = current.getActionUUID();
			RootPanel.get().add(new HTML("P2P: ID processed"));
			action.actionName = "unused";
			RootPanel.get().add(new HTML("P2P: Name processed"));
			action.endpointClassID = current.getActor().getEndpointClassID();
			RootPanel.get().add(new HTML("P2P: EPC processed"));
			action.endpointID = current.getActor().getEndpointID();
			RootPanel.get().add(new HTML("P2P: AEPID processed"));
			action.endpointName = current.getActor().getContentRepresentation();
			RootPanel.get().add(new HTML("P2P: Content processed"));
			action.propertyID = current.getActor().getPropertyID();
			RootPanel.get().add(new HTML("P2P: APID processed"));
			action.propertyIdIntake = current.getSensor().getPropertyID();
			RootPanel.get().add(new HTML("P2P: SPID processed"));
			action.sensorEndpointID = current.getSensor().getEndpointID();
			RootPanel.get().add(new HTML("P2P: SEPID processed"));
			action.settingSourceIndex = current.getSourceIndex();
			RootPanel.get().add(new HTML("P2P: Source processed"));
			action.validValueIndex = current.getTypicalValueIndex();
			RootPanel.get().add(new HTML("P2P: Typical processed"));
			action.manualValue = current.getManualValue();
			RootPanel.get().add(new HTML("P2P: Manual processed"));
			
			RootPanel.get().add(new HTML("P2P: built action object"));

			// TODO this can be simplified - we do not need to take care of
			// multiple actions in new setup

			LinkedList<ActionObject> actions = new LinkedList<ActionObject>();

			actions.add(action);
			
			RootPanel.get().add(new HTML("P2P: built action object list"));

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
		
		RootPanel.get().add(new HTML("P2L: started"));

		while (it.hasNext()) {

			ConnectorSensorLogicCheck current = it.next();
			
			RootPanel.get().add(new HTML("P2L: fetched connector"));

			// TODO this can be simplified - we do not need to take care of
			// multiple actions in new setup

			// build empty action list object as no actions will be linked to
			// this check

			LinkedList<ActionObject> actions = new LinkedList<ActionObject>();

			RootPanel.get().add(new HTML("P2L: created empty actions list"));
			
			// send check to database

			CheckServiceAsync checkService = GWT.create(CheckService.class);

			RootPanel.get().add(new HTML("P2L: processing UUID " + current.getCheckUUID()));
			RootPanel.get().add(new HTML("P2L: processing sensor EPID " + current.getSensor().getEndpointID()));
			RootPanel.get().add(new HTML("P2L: processing sensor PID " + current.getSensor().getPropertyID()));
			RootPanel.get().add(new HTML("P2L: processing check value " + current.getCheckValue()));
			RootPanel.get().add(new HTML("P2L: processing operator " + current.getOperator()));
			
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

	public void loadUI(final String checkpathName) {

		RootPanel.get().add(
				new HTML(
						"UILOAD--- "));

		
		// create JSON factory

		final JsonFabrik factory = new JsonFabrik(canvas);

		// wait for JSON to be generated

		jsonReadyHandler = EventUtils.EVENT_BUS.addHandler(
				JSONreadyEvent.TYPE, new JSONreadyEventHandler() {

					@Override
					public void onJSONready(JSONreadyEvent jsonReadyEvent) {
						
						jsonReadyHandler.removeHandler();

						// send to database
						CheckPathServiceAsync checkpathService = GWT
								.create(CheckPathService.class);

						checkpathService.loadJsonToContainer(checkpathName,
								new AsyncCallback<SerializableLogicContainer>() {

									@Override
									public void onFailure(Throwable caught) {
										// TODO Auto-generated method stub

									}

									@Override
									public void onSuccess(SerializableLogicContainer result) {
										// TODO Auto-generated method stub
										RootPanel.get().add(
												new HTML(
														"Result from attempt to load checkpath: "
																+ result.toString()));
										
										factory.unpackContainer(result);
									}

								});

					}

				});

	}

}
