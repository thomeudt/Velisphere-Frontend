package com.velisphere.blender.xmlRootElements;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlRootElement;
import com.velisphere.blender.dataObjects.PropertyData;

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
