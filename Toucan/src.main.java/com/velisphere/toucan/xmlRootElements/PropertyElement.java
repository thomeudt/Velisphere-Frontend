package com.velisphere.toucan.xmlRootElements;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlRootElement;

import com.velisphere.toucan.dataObjects.EndpointClassData;
import com.velisphere.toucan.dataObjects.EndpointData;
import com.velisphere.toucan.dataObjects.PropertyClassData;
import com.velisphere.toucan.dataObjects.PropertyData;

@XmlRootElement
public class PropertyElement {
	
	private PropertyData propertyData;

	public PropertyData getPropertyData() {
		return propertyData;
	}

	public void setPropertyData(PropertyData propertyData) {
		this.propertyData = propertyData;
	}
	
	  
}
