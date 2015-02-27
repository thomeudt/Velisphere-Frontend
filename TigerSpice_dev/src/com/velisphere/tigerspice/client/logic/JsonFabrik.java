package com.velisphere.tigerspice.client.logic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.velisphere.tigerspice.client.event.DraggedToCanvasEvent;
import com.velisphere.tigerspice.client.event.EventUtils;
import com.velisphere.tigerspice.client.event.JSONreadyEvent;
import com.velisphere.tigerspice.client.logic.connectors.ConnectorLogicCheckActor;
import com.velisphere.tigerspice.client.logic.connectors.ConnectorSensorActor;
import com.velisphere.tigerspice.client.logic.connectors.ConnectorSensorLogicCheck;
import com.velisphere.tigerspice.client.logic.draggables.LinkCreator;
import com.velisphere.tigerspice.client.logic.draggables.LogicCheck;
import com.velisphere.tigerspice.client.logic.draggables.LogicCheckAnd;
import com.velisphere.tigerspice.client.logic.draggables.LogicCheckOr;
import com.velisphere.tigerspice.client.logic.draggables.PhysicalItem;
import com.velisphere.tigerspice.client.logic.layoutWidgets.LogicCanvas;
import com.velisphere.tigerspice.client.rules.CheckPathService;
import com.velisphere.tigerspice.client.rules.CheckPathServiceAsync;
import com.velisphere.tigerspice.shared.SerializableLogicConnector;
import com.velisphere.tigerspice.shared.SerializableLogicContainer;
import com.velisphere.tigerspice.shared.SerializableLogicLogicCheck;
import com.velisphere.tigerspice.shared.SerializableLogicPhysicalItem;
import com.velisphere.tigerspice.shared.SharedConstants;

public class JsonFabrik {

	LogicCanvas canvas;
	SerializableLogicContainer logicContainer;
	String checkpathJSON;
	HashMap<String, PhysicalItem> physicalItemHashMap;
	

	public JsonFabrik(LogicCanvas canvas) {
		this.canvas = canvas;
		this.logicContainer = new SerializableLogicContainer();
		this.physicalItemHashMap = new HashMap<String, PhysicalItem>();
		generateContainer();
		generateJSON();

	}
	
	

	private void generateContainer() {
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

			SerializableLogicConnector current = icl.next().getSerializableRepresentation();
			current.setType(SharedConstants.CONL2P);
			logicContainer.addConnector(current);

		}

		// create json for P2P connectors

		Iterator<ConnectorSensorActor> ics = canvas.getConnectorsSensorActor()
				.iterator();

		while (ics.hasNext()) {

			SerializableLogicConnector current = ics.next().getSerializableRepresentation();
			current.setType(SharedConstants.CONP2P);
			logicContainer.addConnector(current);
		}

		// create json for P2L connectors

		Iterator<ConnectorSensorLogicCheck> ict = canvas
				.getConnectorsSensorLogicCheck().iterator();

		while (ict.hasNext()) {

			SerializableLogicConnector current = ict.next().getSerializableRepresentation();
			current.setType(SharedConstants.CONP2L);
			logicContainer.addConnector(current);

		}

	}

	private void generateJSON() {
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

						checkpathJSON = result;
						EventUtils.EVENT_BUS.fireEvent(new JSONreadyEvent());

					}

				});

	}
	
	
	public void unpackContainer(SerializableLogicContainer logicContainer) {
		
		
		// unpack all physical items from container

		canvas.getPhysicalItems().clear();
		
		this.logicContainer = logicContainer;

		loadPhysicalItems();
		loadLogicChecks();
		loadP2PConnectors();
		
		
	}
	
	private void loadPhysicalItems()
	{
		
		Iterator<SerializableLogicPhysicalItem> it = this.logicContainer.getPhysicalItems().iterator();
		
		while (it.hasNext())
		{
			SerializableLogicPhysicalItem currentSerializable = it.next();
			PhysicalItem current = new PhysicalItem(currentSerializable.getId(), currentSerializable.getContent(), currentSerializable.getEndpointName(), currentSerializable.getPropertyID(), currentSerializable.getEndpointID(), currentSerializable.getEndpointClassID(), currentSerializable.getPropertyClassID(), currentSerializable.getIsSensor(), currentSerializable.getIsActor());
			current.setxPos(currentSerializable.getxPos());
			current.setyPos(currentSerializable.getyPos());
		
			physicalItemHashMap.put(currentSerializable.getId(), current);
			canvas.loadPhysicalItem(current);
			
		}
		
	}
	
	private void loadLogicChecks()
	{
		
		Iterator<SerializableLogicLogicCheck> it = this.logicContainer.getLogicChecks().iterator();
		
		while (it.hasNext())
		{
			SerializableLogicLogicCheck currentSerializable = it.next();

			if(currentSerializable.isAnd())
			{
				LogicCheckAnd current = new LogicCheckAnd();
				current.setContent(currentSerializable.getContent());
				current.setSourceCount(currentSerializable.getSourceCount());
				current.setId(currentSerializable.getId());
				current.setAnd(true);
				current.setxPos(currentSerializable.getxPos());
				current.setyPos(currentSerializable.getyPos());
				canvas.loadLogicCheckAnd(current);
			}
			
			if(currentSerializable.isOr())
			{
				LogicCheckOr current = new LogicCheckOr();
				current.setContent(currentSerializable.getContent());
				current.setSourceCount(currentSerializable.getSourceCount());
				current.setId(currentSerializable.getId());
				current.setOr(true);
				current.setxPos(currentSerializable.getxPos());
				current.setyPos(currentSerializable.getyPos());
				canvas.loadLogicCheckOr(current);
			}
		}	
	}
	
	
	private void loadP2PConnectors()
	{
		
		Iterator<SerializableLogicConnector> it = this.logicContainer.getConnectors().iterator();
		
		while (it.hasNext())
		{
	
			SerializableLogicConnector currentSerializable = it.next();
				
			if (currentSerializable.getType()==SharedConstants.CONP2P)
			{
				
			
			//	RootPanel.get().add(new HTML("Innert " + currentSerializable.getLeftEndpointID() + ".." + lookupLeft +" -- " + lookupRight + "from "+ physicalItemHashMap.keySet().toString()));
				
				ConnectorSensorActor current = new ConnectorSensorActor(
						physicalItemHashMap.get(currentSerializable.getLeftID()), 
						physicalItemHashMap.get(currentSerializable.getRightID()),
						currentSerializable.getLbxOperatorIndex(), currentSerializable.getLbxSourceIndex(),
						currentSerializable.getLbxTypicalValuesIndex(), currentSerializable.getLbxValueFromSensorIndex(),
						currentSerializable.getTxtCheckValueContent(), currentSerializable.getTxtManualEntryContent()
						);
				
				
				
				canvas.loadP2PConnector(current);
				
				
			}
			
			
		}
		
	}
	

	
	public String getJSON()
	{
		return this.checkpathJSON;
	}

}
