package com.velisphere.toucan.xmlRootElements;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlRootElement;

import com.velisphere.toucan.dataObjects.EndpointClassData;
import com.velisphere.toucan.dataObjects.EndpointData;
import com.velisphere.toucan.dataObjects.PropertyClassData;

@XmlRootElement
public class PropertyClassElement {
	
	private PropertyClassData propertyClassData;

	public PropertyClassData getPropertyClassData() {
		return propertyClassData;
	}

	public void setPropertyClassData(PropertyClassData propertyClassData) {
		this.propertyClassData = propertyClassData;
	}
	
	  
}
