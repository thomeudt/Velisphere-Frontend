package com.velisphere.tigerspice.client.properties;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.github.gwtbootstrap.client.ui.Accordion;
import com.github.gwtbootstrap.client.ui.AccordionGroup;
import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.Container;
import com.github.gwtbootstrap.client.ui.Image;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.Row;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
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
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.helper.DynamicAnchor;
import com.velisphere.tigerspice.client.images.Images;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.UnusedPropertyDataProperties;
import com.velisphere.tigerspice.shared.UnusedFolderPropertyData;
import com.velisphere.tigerspice.shared.PropertyData;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class PropertyEditorWidget extends Composite {

	private String endpointClassID;
	private PropertyServiceAsync rpcService;

	public PropertyEditorWidget(String endpointClassID) {
		super();

		this.endpointClassID = endpointClassID;
		rpcService = GWT.create(PropertyService.class);
	
		Row row = new Row();
		Column column = new Column(4, 1);
				
		
		
		
		
		VerticalLayoutContainer treeCon = new VerticalLayoutContainer();
		treeCon = buildTree(this.endpointClassID);
		column.add(treeCon);
		row.add(column);
		initWidget(row);

	}

		


	public VerticalLayoutContainer buildTree(final String endpointClassID) {

		final VerticalLayoutContainer con = new VerticalLayoutContainer();

		
		
		
			
	//	loader.addLoadHandler(new ChildTreeStoreBinding<PropertyData>(store));
		
		final AnimationLoading animationLoading = new AnimationLoading();
		showLoadAnimation(animationLoading);
		rpcService.getPropertiesForEndpointClass(
				endpointClassID,
				new AsyncCallback<List<PropertyData>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						removeLoadAnimation(animationLoading);
					}

					@Override
					public void onSuccess(List<PropertyData> result) {
						// TODO Auto-generated method stub
						removeLoadAnimation(animationLoading);

						Iterator<PropertyData> iT = result.iterator();
						
						Accordion accordion = new Accordion();
					
						
						while(iT.hasNext()){
							PropertyData currentItem = iT.next();
							
							DynamicAnchor propertyLabel = new DynamicAnchor(currentItem.getPropertyName(), true, currentItem.getPropertyId());
							Image img = new Image();
							img.setResource(Images.INSTANCE.tag());
							
							
							AccordionGroup accordionGroup = new AccordionGroup();
							accordionGroup.setHeading(currentItem.getPropertyName());
														
							Column iconColumn = new Column(1);
							iconColumn.addStyleName("text-center");
							iconColumn.add(img);
							Column textColumn = new Column(2);
							textColumn.add(propertyLabel);
							Row row = new Row();
							row.add(iconColumn);
							row.add(textColumn);
							accordionGroup.add(row);
							accordion.add(accordionGroup);
						}
						con.add(accordion);
						
					}

				});

				
		
	    // Create the store that the contains the data to display in the tree
	    
		
	    
	    // Create the tree using the store and value provider for the name field
	
		return con;

	}

	public void onModuleLoad() {
		// System.out.println("AAAA" + endpointClassID);
		RootPanel.get().add(asWidget());

	}
	
	private void showLoadAnimation(AnimationLoading animationLoading) {
		RootPanel rootPanel = RootPanel.get("main");
		rootPanel.getElement().getStyle().setPosition(Position.RELATIVE);
		rootPanel.add(animationLoading, 25, 40);
	}

	private void removeLoadAnimation(AnimationLoading animationLoading) {
		if (animationLoading != null)
			animationLoading.removeFromParent();
	}
	
}