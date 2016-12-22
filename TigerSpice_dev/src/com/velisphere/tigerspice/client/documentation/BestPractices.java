package com.velisphere.tigerspice.client.documentation;

import com.github.gwtbootstrap.client.ui.Breadcrumbs;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class BestPractices extends Composite {

	private static GettingStartedUiBinder uiBinder = GWT
			.create(GettingStartedUiBinder.class);

	interface GettingStartedUiBinder extends UiBinder<Widget, BestPractices> {
	}

	@UiField
	HTML mainHTML;
	@UiField
	Breadcrumbs brdMain;
	NavLink bread0;
	NavLink bread1;

	
	public BestPractices() {
		initWidget(uiBinder.createAndBindUi(this));
		bread0 = new NavLink();
		bread0.setText("Home");
		brdMain.add(bread0);

		bread1 = new NavLink();
		bread1.setText("Best Practices");
		brdMain.add(bread1);
		fillHTML();
	}

	private void fillHTML(){
		
		String cleanHTML = "<h2>Thanks for using VeliSphere.</h2><ul><li>Is it a WYSIWYG editor? (WYSIWYG means &quot;What You See Is What You Get&quot; - just like this editor!)</li><li>Does it generate valid HTML code?</li><li>Is it quick and easy?</li><li>Is it... FREE?</li></ul><p>Feel free to use this online HTML editor for generating HTML code for your own website, MySpace page, etc. To view the source code, simply click on the &quot;Source&quot; button above.</p>";
		
		this.mainHTML.setHTML(cleanHTML);
	}
	
}