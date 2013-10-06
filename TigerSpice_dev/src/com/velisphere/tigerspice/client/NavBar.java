package com.velisphere.tigerspice.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class NavBar extends Composite implements HasText {

	private static NavBarUiBinder uiBinder = GWT.create(NavBarUiBinder.class);

	interface NavBarUiBinder extends UiBinder<Widget, NavBar> {
	}

	public NavBar() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	
	public NavBar(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	
	}

	
	public void setText(String text) {
	
	}

	public String getText() {
		return null;
	
	}

}
