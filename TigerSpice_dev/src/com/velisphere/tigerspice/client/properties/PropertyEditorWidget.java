package com.velisphere.tigerspice.client.properties;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.loader.ChildTreeStoreBinding;
import com.sencha.gxt.data.shared.loader.TreeLoader;
import com.sencha.gxt.state.client.TreeStateHandler;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.ButtonBar;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.endpoints.EndpointServiceAsync;
import com.velisphere.tigerspice.client.images.Images;
import com.velisphere.tigerspice.shared.UnusedFolderPropertyData;
import com.velisphere.tigerspice.shared.PropertyData;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class PropertyEditorWidget extends Composite {

	String endpointClassID;
	
	
	
	
	public PropertyEditorWidget(String endpointClassID)
	{
		super(); 
		
		
		this.endpointClassID = endpointClassID;
		
		   ContentPanel panel = new ContentPanel();
		    
		    panel.setHeaderVisible(false);
		    
		    panel.setCollapsible(true);
		    panel.setPixelSize(500, 400);
		    panel.addStyleName("margin-10");
		     
		    initWidget(panel);
		 	VerticalLayoutContainer treeCon = new VerticalLayoutContainer();
		 	treeCon = buildTree(this.endpointClassID);
		 	panel.add(treeCon);
		
	}
	
	
	
	 class KeyProvider implements ModelKeyProvider<PropertyData> {
		    @Override
		    public String getKey(PropertyData item) {
		      return (item instanceof UnusedFolderPropertyData ? "f-" : "m-") + item.getId().toString();
		    }
		  }
		 
	 
		   
		  
		  public VerticalLayoutContainer buildTree(final String endpointClassID) {
	
			  VerticalLayoutContainer con = new VerticalLayoutContainer();
			  
			  final PropertyServiceAsync RPCservice = GWT.create(PropertyService.class);
			    RpcProxy<PropertyData, List<PropertyData>> proxy = new RpcProxy<PropertyData, List<PropertyData>>() {
			 
			      @Override
			      public void load(PropertyData loadConfig, AsyncCallback<List<PropertyData>> callback) {
			        RPCservice.getPropertiesForEndpointClass(endpointClassID, callback);
			        System.out.println("Looked Up Loaded " + endpointClassID);
			      }
			    };
			 
			    TreeLoader<PropertyData> loader = new TreeLoader<PropertyData>(proxy) {
			      @Override
			      public boolean hasChildren(PropertyData parent) {
			        return parent instanceof UnusedFolderPropertyData;
			      }
			    };
			 
			    TreeStore<PropertyData> store = new TreeStore<PropertyData>(new KeyProvider());
			    loader.addLoadHandler(new ChildTreeStoreBinding<PropertyData>(store));
			 
			    final Tree<PropertyData, String> tree = new Tree<PropertyData, String>(store, new ValueProvider<PropertyData, String>() {
			 
			      @Override
			      public String getValue(PropertyData object) {
			        return object.getName();
			      }
			 
			      @Override
			      public void setValue(PropertyData object, String value) {
			      }
			 
			      @Override
			      public String getPath() {
			        return "name";
			      }
			    });
			    tree.setLoader(loader);
			 
			    TreeStateHandler<PropertyData> stateHandler = new TreeStateHandler<PropertyData>(tree);
			    //stateHandler.loadState();
			    tree.getStyle().setLeafIcon(Images.INSTANCE.tag());
			 
			    
			    /*
			    ToolBar buttonBar = new ToolBar();
			 
			    buttonBar.add(new TextButton("Expand All", new SelectHandler() {
			 
			      @Override
			      public void onSelect(SelectEvent event) {
			        tree.expandAll();
			      }
			    }));
			    buttonBar.add(new TextButton("Collapse All", new SelectHandler() {
			      @Override
			      public void onSelect(SelectEvent event) {
			        tree.collapseAll();
			      }
			 
			    }));
			 
			    con.add(buttonBar, new VerticalLayoutData(1, -1));
			    
			    */
			    con.add(tree, new VerticalLayoutData(1, 1));
				return con;
			 
		  }
		  
		 
		  public void onModuleLoad() {
			  // System.out.println("AAAA" + endpointClassID);
			  RootPanel.get().add(asWidget());
		   
		    
		    
		  }	   
}