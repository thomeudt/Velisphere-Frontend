package com.velisphere.blender.xmlRootElements;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlRootElement;
import com.velisphere.blender.dataObjects.SphereData;

@XmlRootElement
public class SphereElements {


	private LinkedList<SphereData> sphereData;

	public LinkedList<SphereData> getSphereData() {
		return sphereData;
	}

	public void setSphereData(LinkedList<SphereData> sphereData) {
		this.sphereData = sphereData;
	}
	
		  
}
