package com.velisphere.tigerspice.client.helper;

import com.google.gwt.user.client.ui.Anchor;

public class DynamicAnchor extends Anchor {

	String stringQueryFirst;
	String stringQuerySecond;
	String stringQueryThird;
	Integer intQueryFirst;
	Integer intQuerySecond;
	Integer intQueryThird;
	
	public DynamicAnchor(String text, boolean asHtml, String stringQueryFirst)
	{
		super(text, asHtml); 
		this.stringQueryFirst = stringQueryFirst;
		
	}
	
	public String getStringQueryFirst(){
		return stringQueryFirst;
	}
}
