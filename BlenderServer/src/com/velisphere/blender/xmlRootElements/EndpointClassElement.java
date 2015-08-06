package com.velisphere.blender.xmlRootElements;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlRootElement;

import com.velisphere.blender.dataObjects.EndpointClassData;
import com.velisphere.blender.dataObjects.EndpointData;

@XmlRootElement
public class EndpointClassElement {
	
	private EndpointClassData endpointClassData;

	public EndpointClassData getEndpointClassData() {
		return endpointClassData;
	}

	public void setEndpointClassData(EndpointClassData endpointClassData) {
		this.endpointClassData = endpointClassData;
	}
}
