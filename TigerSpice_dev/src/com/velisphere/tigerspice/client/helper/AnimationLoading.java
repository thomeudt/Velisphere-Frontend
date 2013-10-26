package com.velisphere.tigerspice.client.helper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class AnimationLoading extends Composite {

	private static AnimationLoadingUiBinder uiBinder = GWT
			.create(AnimationLoadingUiBinder.class);

	interface AnimationLoadingUiBinder extends
			UiBinder<Widget, AnimationLoading> {
	}

	public AnimationLoading() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
