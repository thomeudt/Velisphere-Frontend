package com.velisphere.tigerspice.resources;



import com.github.gwtbootstrap.client.ui.config.Configurator;
import com.github.gwtbootstrap.client.ui.resources.Resources;
import com.google.gwt.core.client.GWT;

public class TigerspiceConfigurator implements Configurator {
  public Resources getResources() {
    return GWT.create(TigerspiceResources.class);
  }

  public boolean hasResponsiveDesign() {
    return false;
  }
}