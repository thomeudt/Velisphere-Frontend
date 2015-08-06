package com.velisphere.blender.xmlRootElements;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlRootElement;
import com.velisphere.blender.dataObjects.PropertyClassData;

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
