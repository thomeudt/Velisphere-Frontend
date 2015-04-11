package com.velisphere.toucan.xmlRootElements;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlRootElement;

import com.velisphere.toucan.dataObjects.EndpointClassData;
import com.velisphere.toucan.dataObjects.EndpointData;

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
