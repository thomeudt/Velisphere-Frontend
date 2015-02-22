package com.velisphere.tigerspice.client.logic;

import java.util.Iterator;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.velisphere.tigerspice.client.logic.connectors.ConnectorLogicCheckActor;
import com.velisphere.tigerspice.client.logic.connectors.ConnectorSensorActor;
import com.velisphere.tigerspice.client.logic.connectors.ConnectorSensorLogicCheck;
import com.velisphere.tigerspice.client.logic.draggables.LogicCheck;
import com.velisphere.tigerspice.client.logic.draggables.PhysicalItem;
import com.velisphere.tigerspice.client.logic.layoutWidgets.LogicCanvas;
import com.velisphere.tigerspice.client.rules.CheckPathService;
import com.velisphere.tigerspice.client.rules.CheckPathServiceAsync;
import com.velisphere.tigerspice.shared.SerializableLogicContainer;

public class JsonFabrik {

	LogicCanvas canvas;
	SerializableLogicContainer logicContainer;

	public JsonFabrik(LogicCanvas canvas) {
		this.canvas = canvas;
		logicContainer = new SerializableLogicContainer();
		generateContainer();

	}

	public void generateContainer() {
		// create json for all physical items

		Iterator<PhysicalItem> it = canvas.getPhysicalItems().iterator();

		while (it.hasNext()) {

			logicContainer.addPhysicalItem(it.next()
					.getSerializableRepresentation());

		}

		// create json for all logic checks

		Iterator<LogicCheck> il = canvas.getLogicChecks().iterator();

		while (il.hasNext()) {

			logicContainer.addLogicCheck(il.next()
					.getSerializableRepresentation());

		}

		// create json for L2P connectors

		Iterator<ConnectorLogicCheckActor> icl = canvas
				.getConnectorsLogicCheckActor().iterator();

		while (icl.hasNext()) {

			logicContainer.addConnector(icl.next()
					.getSerializableRepresentation());

		}

		// create json for P2P connectors

		Iterator<ConnectorSensorActor> ics = canvas.getConnectorsSensorActor()
				.iterator();

		while (ics.hasNext()) {
			logicContainer.addConnector(ics.next()
					.getSerializableRepresentation());

		}

		// create json for L2P connectors

		Iterator<ConnectorSensorLogicCheck> ict = canvas
				.getConnectorsSensorLogicCheck().iterator();

		while (ict.hasNext()) {

			logicContainer.addConnector(ict.next()
					.getSerializableRepresentation());

		}

	}

	public void getJSON() {
		// create json for all physical items

		CheckPathServiceAsync checkPathService = GWT
				.create(CheckPathService.class);

		checkPathService.createJsonFromContainer(this.logicContainer,
				new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

						RootPanel.get().add(
								new HTML("ERROR " + caught.getMessage()));

					}

					@Override
					public void onSuccess(String result) {
						// TODO Auto-generated method stub

						RootPanel.get().add(new HTML("FULL JSON " + result));

					}

				});

	}

}
