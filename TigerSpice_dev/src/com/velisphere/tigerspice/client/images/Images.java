package com.velisphere.tigerspice.client.images;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ClientBundle.Source;
import com.google.gwt.resources.client.ImageResource;

public interface Images extends ClientBundle {


public Images INSTANCE = GWT.create(Images.class);


@Source("tag.png")
ImageResource tag();

@Source("clock.png")
ImageResource clock();

@Source("fire.png")
ImageResource fire();

@Source("eye.png")
ImageResource eye();

@Source("paperplane.png")
ImageResource paperplane();

@Source("megaphone.png")
ImageResource megaphone();

@Source("globe_green.png")
ImageResource globe_green();

@Source("4spheres.png")
ImageResource fourSpheres();

@Source("big_arrow.png")
ImageResource big_arrow();

@Source("and.png")
ImageResource and();

@Source("or.png")
ImageResource or();

@Source("action.png")
ImageResource action();

}