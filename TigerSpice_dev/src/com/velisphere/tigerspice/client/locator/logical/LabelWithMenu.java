package com.velisphere.tigerspice.client.locator.logical;


import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.MenuItem;
import com.orange.links.client.menu.ContextMenu;
import com.orange.links.client.menu.HasContextMenu;
import com.velisphere.tigerspice.client.appcontroller.AppController;

public class LabelWithMenu extends BoxLabel implements HasContextMenu {
    
    ContextMenu customMenu;
    
    public LabelWithMenu(final String endpointID, String endpointName) {
        super(endpointName);
        customMenu = new ContextMenu();
        customMenu.addItem(new MenuItem("Manage "+ endpointName + "... ", new Command() {
                @Override
                public void execute() {
                	AppController.openEndpoint(endpointID);	
                }
            }));
        
        customMenu.addItem(new MenuItem("Locate "+ endpointName + " on map ...", new Command() {
            @Override
            public void execute() {
            	AppController.openEndpoint(endpointID);	
            }
        }));
        
    }

    @Override
    public ContextMenu getContextMenu() {
        return customMenu;
    }
}