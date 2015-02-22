package com.velisphere.tigerspice.shared;

import java.util.LinkedList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SerializableLogicContainer implements IsSerializable{

	LinkedList<SerializableLogicConnector> connectors;
	LinkedList<SerializableLogicLogicCheck> logicChecks;
	LinkedList<SerializableLogicPhysicalItem> physicalItems;
	
	public SerializableLogicContainer()
	{
		
		connectors = new LinkedList<SerializableLogicConnector>();
		logicChecks = new LinkedList<SerializableLogicLogicCheck>();
		physicalItems = new LinkedList<SerializableLogicPhysicalItem>();
		
		
	}
	
	public LinkedList<SerializableLogicConnector> getConnectors() {
		return connectors;
	}
	public void addConnector(SerializableLogicConnector connector) {
		this.connectors.add(connector);
	}
	public LinkedList<SerializableLogicLogicCheck> getLogicChecks() {
		return logicChecks;
	}
	public void addLogicCheck(SerializableLogicLogicCheck logicCheck) {
		this.logicChecks.add(logicCheck);
	}
	public LinkedList<SerializableLogicPhysicalItem> getPhysicalItems() {
		return physicalItems;
	}
	public void addPhysicalItem(SerializableLogicPhysicalItem physicalItem) {
		this.physicalItems.add(physicalItem);
	}
	
}
