package com.velisphere.tigerspice.client.images;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ClientBundle.Source;
import com.google.gwt.resources.client.ImageResource;

public interface Images extends ClientBundle {


public Images INSTANCE = GWT.create(Images.class);


@Source("tag.png")
ImageResource tag();

}