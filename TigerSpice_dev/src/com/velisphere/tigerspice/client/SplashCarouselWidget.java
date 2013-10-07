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

public class SplashCarouselWidget extends Composite implements HasText {

	private static SplashCarouselWidgetUiBinder uiBinder = GWT
			.create(SplashCarouselWidgetUiBinder.class);

	interface SplashCarouselWidgetUiBinder extends
			UiBinder<Widget, SplashCarouselWidget> {
	}

	public SplashCarouselWidget() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	
	public SplashCarouselWidget(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	
	}


	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void setText(String text) {
		// TODO Auto-generated method stub
		
	}

	

	
}
