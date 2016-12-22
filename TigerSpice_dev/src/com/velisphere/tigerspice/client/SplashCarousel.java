package com.velisphere.tigerspice.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.UIObject;

public class SplashCarousel extends UIObject {

	private static SplashCarouselUiBinder uiBinder = GWT
			.create(SplashCarouselUiBinder.class);

	interface SplashCarouselUiBinder extends UiBinder<Element, SplashCarousel> {
	}



	public SplashCarousel(String firstName) {
		setElement(uiBinder.createAndBindUi(this));

	}

}
