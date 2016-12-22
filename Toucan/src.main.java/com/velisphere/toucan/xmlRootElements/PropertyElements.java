package com.velisphere.toucan.xmlRootElements;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlRootElement;

import com.velisphere.toucan.dataObjects.EndpointClassData;
import com.velisphere.toucan.dataObjects.EndpointData;
import com.velisphere.toucan.dataObjects.PropertyClassData;
import com.velisphere.toucan.dataObjects.PropertyData;

@XmlRootElement
public class PropertyElements {
	
	private LinkedList<PropertyData> propertyData;

	public LinkedList<PropertyData> getPropertyData() {
		return propertyData;
	}

	public void setPropertyData(LinkedList<PropertyData> propertyData) {
		this.propertyData = propertyData;
	}
	
	  
}
