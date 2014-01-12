package com.velisphere.tigerspice.shared;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface UnusedPropertyDataProperties extends PropertyAccess<PropertyData> {
    @Path("propertyId")
    ModelKeyProvider<PropertyData> key();
    ValueProvider<PropertyData, String> propertyId();
    ValueProvider<PropertyData, String> propertyName();
    ValueProvider<PropertyData, String> propertyclassId();
    ValueProvider<PropertyData, String> endpointclassId();
    
    
  }