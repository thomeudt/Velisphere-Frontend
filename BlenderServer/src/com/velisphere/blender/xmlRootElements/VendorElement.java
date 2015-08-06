package com.velisphere.blender.xmlRootElements;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlRootElement;
import com.velisphere.blender.dataObjects.VendorData;

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
