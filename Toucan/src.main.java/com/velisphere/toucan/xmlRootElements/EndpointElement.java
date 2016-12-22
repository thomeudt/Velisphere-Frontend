package com.velisphere.toucan.xmlRootElements;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlRootElement;

import com.velisphere.toucan.dataObjects.EndpointData;

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
