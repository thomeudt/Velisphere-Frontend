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
	HashMap<String, LogicCheck> logicCheckHashMap;
		

	public JsonFabrik(LogicCanvas canvas) {
		this.canvas = canvas;
		this.logicContainer = new SerializableLogicContainer();
		this.physicalItemHashMap = new HashMap<String, PhysicalItem>();
		this.logicCheckHashMap = new HashMap<String, LogicCheck>();

				
		generateContainer();
		generateJSON();
	
	}
	
	

	private void generateContainer() {
		
		// set UUID
		
		logicContainer.setUuid(canvas.getUUID());
		logicContainer.setName(canvas.getName());
		
		
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
			
			logicContainer.addConnector(current);
	
		}

		// create json for P2P connectors

		Iterator<ConnectorSensorActor> ics = canvas.getConnectorsSensorActor()
				.iterator();
		
	
		while (ics.hasNext()) {
			SerializableLogicConnector current = ics.next().getSerializableRepresentation();
			
			logicContainer.addConnector(current);
		}

		// create json for P2L connectors

		Iterator<ConnectorSensorLogicCheck> ict = canvas
				.getConnectorsSensorLogicCheck().iterator();

	
		
		while (ict.hasNext()) {
			SerializableLogicConnector current = ict.next().getSerializableRepresentation();
			
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

					
					}

					@Override
					public void onSuccess(String result) {
						// TODO Auto-generated method stub

						checkpathJSON = result;
						EventUtils.RESETTABLE_EVENT_BUS.fireEvent(new JSONreadyEvent());

					}

				});

	}
	
	
	public void unpackContainer(SerializableLogicContainer logicContainer) {

		canvas.getPhysicalItems().clear();
		canvas.getLogicChecks().clear();
		canvas.getConnectorsLogicCheckActor().clear();
		canvas.getConnectorsSensorActor().clear();
		canvas.getConnectorsSensorLogicCheck().clear();
		
		// unpack all physical items from container
		
		
		this.logicContainer = logicContainer;

		loadPhysicalItems();
		loadLogicChecks();
	
		
		loadP2PConnectors();
		loadP2LConnectors();
		loadL2PConnectors();
		
		canvas.setUUID(logicContainer.getUuid());
		canvas.setName(logicContainer.getName());
		
		
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
				current.setOr(false);
				current.setxPos(currentSerializable.getxPos());
				current.setyPos(currentSerializable.getyPos());
				logicCheckHashMap.put(currentSerializable.getId(), current);
				canvas.loadLogicCheckAnd(current);
			}
			
			if(currentSerializable.isOr())
			{
				LogicCheckOr current = new LogicCheckOr();
				current.setContent(currentSerializable.getContent());
				current.setSourceCount(currentSerializable.getSourceCount());
				current.setId(currentSerializable.getId());
				current.setOr(true);
				current.setAnd(false);
				current.setxPos(currentSerializable.getxPos());
				current.setyPos(currentSerializable.getyPos());
				logicCheckHashMap.put(currentSerializable.getId(), current);
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
				
				
				ConnectorSensorActor current = new ConnectorSensorActor(
						physicalItemHashMap.get(currentSerializable.getLeftID()), 
						physicalItemHashMap.get(currentSerializable.getRightID()),
						currentSerializable.getActionID(), currentSerializable.getCheckID(),
						currentSerializable.getLbxOperatorValue(), currentSerializable.getLbxSourceValue(),
						currentSerializable.getLbxTypicalValuesValue(), currentSerializable.getLbxValueFromSensorValue(),
						currentSerializable.getTxtCheckValueContent(), currentSerializable.getTxtManualEntryContent()
						);
				
				
				
				canvas.loadP2PConnector(current);
				
				
			}
			
			
		}
		
	}
	
	private void loadP2LConnectors()
	{
		
		Iterator<SerializableLogicConnector> it = this.logicContainer.getConnectors().iterator();
		
		while (it.hasNext())
		{
	
			SerializableLogicConnector currentSerializable = it.next();
				
			if (currentSerializable.getType()==SharedConstants.CONP2L)
			{
				
			
				
				
				
				ConnectorSensorLogicCheck current = new ConnectorSensorLogicCheck(
						physicalItemHashMap.get(currentSerializable.getLeftID()), 
						logicCheckHashMap.get(currentSerializable.getRightID()),
						currentSerializable.getActionID(), currentSerializable.getCheckID(),
						currentSerializable.getLbxOperatorValue(),currentSerializable.getTxtCheckValueContent());
				
				
				
				canvas.loadP2LConnector(current);
				
				
			}
			
			
		}
		
	}


	private void loadL2PConnectors()
	{
		
		Iterator<SerializableLogicConnector> it = this.logicContainer.getConnectors().iterator();
		
		while (it.hasNext())
		{
	
			SerializableLogicConnector currentSerializable = it.next();
				
			if (currentSerializable.getType()==SharedConstants.CONL2P)
			{
				
				

				
				
				ConnectorLogicCheckActor current = new ConnectorLogicCheckActor(
						logicCheckHashMap.get(currentSerializable.getLeftID()), 
						physicalItemHashMap.get(currentSerializable.getRightID()),
						currentSerializable.getActionID(), currentSerializable.getCheckID(),
						currentSerializable.getLbxSourceValue(),currentSerializable.getLbxTypicalValuesValue(), currentSerializable.getLbxValueFromSensorValue(),
						currentSerializable.getTxtManualEntryContent());

				
				
				
				canvas.loadL2PConnector(current);
				
				
			}
			
			
		}
		
	}


	
	public String getJSON()
	{
		return this.checkpathJSON;
	}

}
