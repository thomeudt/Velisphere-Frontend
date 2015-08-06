package com.velisphere.blender.xmlRootElements;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlRootElement;

import com.velisphere.blender.dataObjects.EndpointPropertyLogData;

@XmlRootElement
public class EndpointPropertyLogElements {
	
	private LinkedList<EndpointPropertyLogData> endpointPropertyLogData;

	public LinkedList<EndpointPropertyLogData> getPropertyData() {
		return endpointPropertyLogData;
	}

	public void setPropertyData(LinkedList<EndpointPropertyLogData> endpointPropertyLogData) {
		this.endpointPropertyLogData = endpointPropertyLogData;
	}
	
	  
}
