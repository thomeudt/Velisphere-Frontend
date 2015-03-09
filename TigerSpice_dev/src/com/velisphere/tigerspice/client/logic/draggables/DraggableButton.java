package com.velisphere.tigerspice.client.logic.draggables;

import com.github.gwtbootstrap.client.ui.Button;
import com.velisphere.tigerspice.client.logic.connectors.Connector;

public class DraggableButton extends Button {

	Connector connector;
	Class<?> connectorType;

	
	public DraggableButton(Connector connector, Class<?> connectorType)
	{
		super();
		this.connector = connector;
		this.connectorType = connectorType;
		
	}
	
	public Connector getConnector() {
		return connector;
	}

	public void setConnector(Connector connector) {
		this.connector = connector;
	}

	public Class<?> getConnectorType() {
		return connectorType;
	}

	public void setConnectorType(Class<?> connectorType) {
		this.connectorType = connectorType;
	}

	
}