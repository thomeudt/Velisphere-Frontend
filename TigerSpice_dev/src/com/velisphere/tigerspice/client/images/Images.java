/*******************************************************************************
 * CONFIDENTIAL INFORMATION
 *  __________________
 *  
 *   Copyright (C) 2014 Thorsten Meudt / Connected Things Lab
 *   All Rights Reserved.
 *  
 *  NOTICE:  All information contained herein is, and remains
 *  the property of Thorsten Meudt and its suppliers,
 *  if any.  The intellectual and technical concepts contained
 *  herein are proprietary to Thorsten Meudt
 *  and its suppliers and may be covered by Patents,
 *  patents in process, and are protected by trade secret or copyright law.
 *  Dissemination of this information or reproduction of this material
 *  is strictly forbidden unless prior written permission is obtained
 *  from Thorsten Meudt.
 ******************************************************************************/
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

@Source("blocked.png")
ImageResource blocked();

}
