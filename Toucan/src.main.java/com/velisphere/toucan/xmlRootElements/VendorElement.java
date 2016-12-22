package com.velisphere.toucan.xmlRootElements;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlRootElement;

import com.velisphere.toucan.dataObjects.EndpointClassData;
import com.velisphere.toucan.dataObjects.EndpointData;
import com.velisphere.toucan.dataObjects.PropertyClassData;
import com.velisphere.toucan.dataObjects.PropertyData;
import com.velisphere.toucan.dataObjects.VendorData;

@XmlRootElement
public class VendorElement {
	
	private VendorData vendorData;

	public VendorData getVendorData() {
		return vendorData;
	}

	public void setVendorData(VendorData vendorData) {
		this.vendorData = vendorData;
	}
	
	  
}
