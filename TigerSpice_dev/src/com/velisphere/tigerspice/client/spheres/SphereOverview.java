package com.velisphere.tigerspice.client.spheres;

import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.Breadcrumbs;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.PageHeader;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class SphereOverview extends Composite {

	private static SphereOverviewUiBinder uiBinder = GWT
			.create(SphereOverviewUiBinder.class);

	@UiField PageHeader pghSphereName;
	@UiField Breadcrumbs brdMain;
	
	interface SphereOverviewUiBinder extends UiBinder<Widget, SphereOverview> {
	}

	public SphereOverview(String sphereID, String sphereName) {
		initWidget(uiBinder.createAndBindUi(this));
		pghSphereName.setText(sphereName); 
		NavLink bread1 = new NavLink();
		bread1.setText("Sphere Overview");
		brdMain.add(bread1);
		NavLink bread2 = new NavLink();
		bread2.setText(sphereName);
		brdMain.add(bread2);

		bread1.addClickHandler(
				new ClickHandler(){
					public void onClick(ClickEvent event){
			
						RootPanel mainPanel = RootPanel.get("main");
						mainPanel.clear();
						SphereLister sphereOverview = new SphereLister(); 		
						
						mainPanel.add(sphereOverview);
					
					
					}
				});
		
	
	
	}

}
