package com.velisphere.toucan.xmlRootElements;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlRootElement;

import com.velisphere.toucan.dataObjects.EndpointData;
import com.velisphere.toucan.dataObjects.SphereData;

@XmlRootElement
public class Spheres {


	private LinkedList<SphereData> sphereData;

	public LinkedList<SphereData> getEndpointData() {
		return sphereData;
	}

	public void setEndpointData(LinkedList<SphereData> sphereData) {
		this.sphereData = sphereData;
	}
	
		  
}
