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
	HashMap<String, LogicCheck> logicCheckHashMap;
		

	public JsonFabrik(LogicCanvas canvas) {
		this.canvas = canvas;
		this.logicContainer = new SerializableLogicContainer();
		this.physicalItemHashMap = new HashMap<String, PhysicalItem>();
		this.logicCheckHashMap = new HashMap<String, LogicCheck>();

		RootPanel.get().add(new HTML("Fabrik initialized."));
				
		generateContainer();
		
		RootPanel.get().add(new HTML("Fabrik: Container generated."));
		generateJSON();
		RootPanel.get().add(new HTML("Fabrik: JSON generated."));

	}
	
	

	private void generateContainer() {
		
		// set UUID
		
		logicContainer.setUuid(canvas.getUUID());
		
		
		
		// create json for all physical items

			Iterator<PhysicalItem> it = canvas.getPhysicalItems().iterator();

			while (it.hasNext()) {

				logicContainer.addPhysicalItem(it.next()
						.getSerializableRepresentation());
				RootPanel.get().add(new HTML("Container: Physical item added."));			
			}
				
		

		// create json for all logic checks

			Iterator<LogicCheck> il = canvas.getLogicChecks().iterator();
			
			RootPanel.get().add(new HTML("Container: Iterator for Logic Check created: " + canvas.getLogicChecks().size()));

			while (il.hasNext()) {

				RootPanel.get().add(new HTML("Container: Processing "));

				logicContainer.addLogicCheck(il.next()
						.getSerializableRepresentation());
				RootPanel.get().add(new HTML("Container: Logical item added."));

			}
		
		
		// create json for L2P connectors

		Iterator<ConnectorLogicCheckActor> icl = canvas
				.getConnectorsLogicCheckActor().iterator();

		RootPanel.get().add(new HTML("Container: Iterator for L2P created: " + canvas.getConnectorsLogicCheckActor().size()));
		
		while (icl.hasNext()) {

			SerializableLogicConnector current = icl.next().getSerializableRepresentation();
			
			logicContainer.addConnector(current);
			RootPanel.get().add(new HTML("Container: L2P connector added."));

		}

		// create json for P2P connectors

		Iterator<ConnectorSensorActor> ics = canvas.getConnectorsSensorActor()
				.iterator();
		
		RootPanel.get().add(new HTML("Container: Iterator for P2P created: " + canvas.getConnectorsSensorActor().size()));

		while (ics.hasNext()) {
			RootPanel.get().add(new HTML("Container: P2P traversed."));
			SerializableLogicConnector current = ics.next().getSerializableRepresentation();
			RootPanel.get().add(new HTML("Container: P2P serialized."));
			
			logicContainer.addConnector(current);
			RootPanel.get().add(new HTML("Container: P2P connector added."));
		}

		// create json for P2L connectors

		Iterator<ConnectorSensorLogicCheck> ict = canvas
				.getConnectorsSensorLogicCheck().iterator();

		RootPanel.get().add(new HTML("Container: Iterator for P2L created: " + canvas.getConnectorsSensorLogicCheck().size()));

		
		while (ict.hasNext()) {
			RootPanel.get().add(new HTML("Container: P2L traversed."));
			SerializableLogicConnector current = ict.next().getSerializableRepresentation();
			RootPanel.get().add(new HTML("Container: P2L serialized."));
			
			logicContainer.addConnector(current);
			RootPanel.get().add(new HTML("Container: P2L connector added."));
		}

	}

	private void generateJSON() {
		// create json for all physical items

		RootPanel.get().add(new HTML("JSON: Requested."));
				
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

			RootPanel.get().add(new HTML("Trying to check P2P Type..."));

			
			if (currentSerializable.getType()==SharedConstants.CONP2P)
			{
				
			
			//	RootPanel.get().add(new HTML("Innert " + currentSerializable.getLeftEndpointID() + ".." + lookupLeft +" -- " + lookupRight + "from "+ physicalItemHashMap.keySet().toString()));
				
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
			
			RootPanel.get().add(new HTML("Succeeded!"));
			
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
				
			
			//	RootPanel.get().add(new HTML("Innert " + currentSerializable.getLeftEndpointID() + ".." + lookupLeft +" -- " + lookupRight + "from "+ physicalItemHashMap.keySet().toString()));
				
				
				
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
				
			
			//	RootPanel.get().add(new HTML("Innert " + currentSerializable.getLeftEndpointID() + ".." + lookupLeft +" -- " + lookupRight + "from "+ physicalItemHashMap.keySet().toString()));
				

				
				
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
