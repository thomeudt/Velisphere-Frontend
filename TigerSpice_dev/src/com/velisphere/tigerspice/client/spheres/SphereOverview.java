package com.velisphere.tigerspice.client.spheres;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class SphereOverview extends Composite {

	private static SphereOverviewUiBinder uiBinder = GWT
			.create(SphereOverviewUiBinder.class);

	interface SphereOverviewUiBinder extends UiBinder<Widget, SphereOverview> {
	}

	public SphereOverview() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
