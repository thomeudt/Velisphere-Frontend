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
import com.velisphere.tigerspice.client.helper.ActionSourceConfig;
import com.velisphere.tigerspice.client.logic.connectors.ConnectorLogicCheckActor;
import com.velisphere.tigerspice.client.logic.connectors.ConnectorSensorActor;
import com.velisphere.tigerspice.client.logic.connectors.ConnectorSensorLogicCheck;
import com.velisphere.tigerspice.client.logic.draggables.PhysicalItem;
import com.velisphere.tigerspice.client.logic.layoutWidgets.LogicCanvas;
import com.velisphere.tigerspice.shared.ActionObject;
import com.velisphere.tigerspice.shared.SerializableLogicContainer;

public class DataManager {

	HandlerRegistration jsonReadyHandler;
	LogicCanvas canvas;

	public DataManager(LogicCanvas canvas) {
		this.canvas = canvas;

	}

	public void processCheckPath() {

		// create JSON factory

		final JsonFabrik factory = new JsonFabrik(canvas);

		// wait for JSON to be generated

		HandlerRegistration jsonReadyHandler = EventUtils.RESETTABLE_EVENT_BUS
				.addHandler(JSONreadyEvent.TYPE, new JSONreadyEventHandler() {

					@Override
					public void onJSONready(JSONreadyEvent jsonReadyEvent) {

						// send to database
						CheckPathServiceAsync checkpathService = GWT
								.create(CheckPathService.class);

						checkpathService.addNewCheckpath(canvas.getName(),
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
			action.validValueIndex = current.getTypicalValueValue();
			action.manualValue = current.getManualValue();
		
			if (current.getSourceValue() == ActionSourceConfig.currentSensorValue) {
				action.valueFromInboundPropertyID = current.getValueFromSensorValue();
			} else {
				action.valueFromInboundPropertyID = "no";
			}

		
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

						}

					});

		}
	}

	public void processP2L() {

		Iterator<ConnectorSensorLogicCheck> it = canvas
				.getConnectorsSensorLogicCheck().iterator();

	
		while (it.hasNext()) {

			final ConnectorSensorLogicCheck current = it.next();


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
			
			action.validValueIndex = current.getTypicalValueValue();
			action.manualValue = current.getManualValue();
			

			action.valueFromInboundPropertyID = "no";
			
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

						}

					});

			// link checks

			Iterator<ConnectorSensorLogicCheck> pit = current.getLogicCheck()
					.getChildConnectors().iterator();

			while (pit.hasNext()) {

				ConnectorSensorLogicCheck childConnector = pit.next();

				checkpathService.addNewMulticheckCheckLink(
						childConnector.getUUID(), current.getCheckUUID(),
						childConnector.getCheckUUID(), canvas.getUUID(),
						new AsyncCallback<String>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onSuccess(String result) {
								// TODO Auto-generated method stub
							}

						});
			}

		}
	}

	public void processDeletedL2P() {
		Iterator<ConnectorLogicCheckActor> it = canvas
				.getDeletedConnectorsLogicCheckActor().iterator();

		while (it.hasNext()) {

			ConnectorLogicCheckActor current = it.next();

			// delete multicheck from database

			CheckPathServiceAsync checkpathService = GWT
					.create(CheckPathService.class);

			checkpathService.deleteMulticheck(current.getCheckUUID(),
					new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onSuccess(String result) {
							// TODO Auto-generated method stub

						}

					});

			// delete actions from database

			checkpathService.deleteAllActionsForMulticheckId(
					current.getCheckUUID(), new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onSuccess(String result) {
							// TODO Auto-generated method stub
						}

					});

			// delete links to checks

			Iterator<ConnectorSensorLogicCheck> pit = current.getLogicCheck()
					.getDeletedChildConnectors().iterator();

			while (pit.hasNext()) {
				checkpathService.deleteMulticheckCheckLink(
						pit.next().getUUID(), new AsyncCallback<String>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onSuccess(String result) {
								// TODO Auto-generated method stub
							}

						});
			}

		}
	}

	public void processDeletedP2P() {
		Iterator<ConnectorSensorActor> it = canvas
				.getDeletedConnectorsSensorActor().iterator();

	
		while (it.hasNext()) {

			ConnectorSensorActor current = it.next();

	
			// delete from database

			CheckServiceAsync checkService = GWT.create(CheckService.class);

			checkService.deleteCheck(current.getCheckUUID(),
					new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onSuccess(String result) {
							// TODO Auto-generated method stub

						}

					});

			// delete actions from database

			checkService.deleteAllActionsForCheckId(current.getCheckUUID(),
					new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onSuccess(String result) {
							// TODO Auto-generated method stub

						}

					});

		}
	}

	public void processDeletedP2L() {

		Iterator<ConnectorSensorLogicCheck> it = canvas
				.getDeletedConnectorsSensorLogicCheck().iterator();


		while (it.hasNext()) {

			ConnectorSensorLogicCheck current = it.next();


			// send check to database

			CheckServiceAsync checkService = GWT.create(CheckService.class);

			checkService.deleteCheck(current.getCheckUUID(),
					new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onSuccess(String result) {
							// TODO Auto-generated method stub

						}

					});

			// delete actions from database

			checkService.deleteAllActionsForCheckId(current.getCheckUUID(),
					new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onSuccess(String result) {
							// TODO Auto-generated method stub

						}

					});

		}
	}

	public void loadUI(final String checkpathName) {

		
		// create JSON factory

		final JsonFabrik factory = new JsonFabrik(canvas);

		// wait for JSON to be generated

		jsonReadyHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(
				JSONreadyEvent.TYPE, new JSONreadyEventHandler() {

					@Override
					public void onJSONready(JSONreadyEvent jsonReadyEvent) {

						jsonReadyHandler.removeHandler();

						// send to database
						CheckPathServiceAsync checkpathService = GWT
								.create(CheckPathService.class);

						checkpathService
								.loadJsonToContainer(
										checkpathName,
										new AsyncCallback<SerializableLogicContainer>() {

											@Override
											public void onFailure(
													Throwable caught) {
												// TODO Auto-generated method
												// stub

											}

											@Override
											public void onSuccess(
													SerializableLogicContainer result) {
												// TODO Auto-generated method
												// stub
											
												factory.unpackContainer(result);
											}

										});

					}

				});

	}

	public void deleteCheckpath() {

		// send to database
		CheckPathServiceAsync checkpathService = GWT
				.create(CheckPathService.class);

		checkpathService.deleteCheckpath(canvas.getUUID(),
				new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(String result) {
						// TODO Auto-generated method stub
					
					}

				});
	}

}
