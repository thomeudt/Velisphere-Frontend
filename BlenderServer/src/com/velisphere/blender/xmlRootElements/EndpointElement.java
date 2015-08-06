package com.velisphere.blender.xmlRootElements;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlRootElement;

import com.velisphere.blender.dataObjects.EndpointData;

@XmlRootElement
public class EndpointElement {
	
	private EndpointData endpointData;

	public EndpointData getEndpointData() {
		return endpointData;
	}

	public void setEndpointData(EndpointData endpointData) {
		this.endpointData = endpointData;
	}
	
	  
}
