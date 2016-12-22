package com.velisphere.toucan.xmlRootElements;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlRootElement;

import com.velisphere.toucan.dataObjects.EndpointClassData;
import com.velisphere.toucan.dataObjects.EndpointData;
import com.velisphere.toucan.dataObjects.EndpointPropertyLogData;
import com.velisphere.toucan.dataObjects.PropertyClassData;
import com.velisphere.toucan.dataObjects.PropertyData;

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
