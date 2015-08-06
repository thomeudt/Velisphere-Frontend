package com.velisphere.blender.xmlRootElements;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlRootElement;
import com.velisphere.blender.dataObjects.PropertyData;

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
