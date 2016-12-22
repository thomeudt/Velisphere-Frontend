package com.velisphere.tigerspice.client.helper.widgets;


import com.google.gwt.canvas.dom.client.Context;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;

public class Circle extends Composite {
	    private Element canvas;
	    public Circle() {
	        HTML html = new HTML("<canvas></canvas>");
	        initWidget(html);
	        canvas = html.getElement().getFirstChildElement();
	    }

	    public native RenderingContext getContext() /*-{
        return this.@com.velisphere.tigerspice.client.helper.widgets.Circle::canvas.getContext("2d");
    }-*/;

	    
	    
}


	
	
