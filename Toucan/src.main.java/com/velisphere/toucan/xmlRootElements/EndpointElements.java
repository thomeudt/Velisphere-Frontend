package com.velisphere.toucan.xmlRootElements;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlRootElement;

import com.velisphere.toucan.dataObjects.EndpointData;

@XmlRootElement
public class EndpointElements {
	
	private LinkedList<EndpointData> endpointData;

	public LinkedList<EndpointData> getEndpointData() {
		return endpointData;
	}

	public void setEndpointData(LinkedList<EndpointData> endpointData) {
		this.endpointData = endpointData;
	}
	
	  
}
