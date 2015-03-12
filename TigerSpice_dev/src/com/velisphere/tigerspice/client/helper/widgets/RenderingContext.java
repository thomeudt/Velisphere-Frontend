package com.velisphere.tigerspice.client.helper.widgets;

import com.google.gwt.core.client.JavaScriptObject;

public class RenderingContext extends JavaScriptObject {
	// must be protected
	protected RenderingContext() {
	}

	public final native void setFillStyle(String fillStyle) /*-{
		this.fillStyle = fillStyle;
	}-*/;

	public final native void fillRect(int x, int y, int width, int height) /*-{
		this.fillRect(x, y, width, height);
	}-*/;
}