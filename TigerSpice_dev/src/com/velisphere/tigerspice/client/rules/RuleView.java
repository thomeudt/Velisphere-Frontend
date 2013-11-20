package com.velisphere.tigerspice.client.rules;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.spheres.SphereEditorWidget;

public class RuleView extends Composite {

	private static RuleViewUiBinder uiBinder = GWT
			.create(RuleViewUiBinder.class);

	interface RuleViewUiBinder extends UiBinder<Widget, RuleView> {
	}

	public RuleView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	

}
