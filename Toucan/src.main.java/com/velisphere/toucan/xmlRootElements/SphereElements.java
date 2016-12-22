package com.velisphere.toucan.xmlRootElements;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlRootElement;

import com.velisphere.toucan.dataObjects.EndpointData;
import com.velisphere.toucan.dataObjects.SphereData;

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
