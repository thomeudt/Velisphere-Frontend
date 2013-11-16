package com.velisphere.tigerspice.client.properties;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.github.gwtbootstrap.client.ui.Accordion;
import com.github.gwtbootstrap.client.ui.AccordionGroup;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.Container;
import com.github.gwtbootstrap.client.ui.Image;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.Row;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.resources.ButtonSize;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.loader.ChildTreeStoreBinding;
import com.sencha.gxt.data.shared.loader.TreeLoader;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DragSource;
import com.sencha.gxt.dnd.core.client.DropTarget;
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
import com.velisphere.tigerspice.client.checks.CheckService;
import com.velisphere.tigerspice.client.checks.CheckServiceAsync;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.endpoints.EndpointServiceAsync;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.helper.DragobjectAccordion;
import com.velisphere.tigerspice.client.helper.DynamicAnchor;
import com.velisphere.tigerspice.client.images.Images;
import com.velisphere.tigerspice.shared.CheckData;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.UnusedPropertyDataProperties;
import com.velisphere.tigerspice.shared.UnusedFolderPropertyData;
import com.velisphere.tigerspice.shared.PropertyData;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class PropertyEditorWidget extends Composite {

	private String endpointClassID;
	private String endpointID;
	private PropertyServiceAsync rpcService;
	
	private class PropContent{
		String currentValue;
		
		public String getCurrentValue(){
			return currentValue;
		}
		
		public void setCurrentValue(String currentValue){
			this.currentValue = currentValue;
		}
		
	}
	
	public PropertyEditorWidget(String endpointClassID, String endpointID) {
		super();

		this.endpointClassID = endpointClassID;
		this.endpointID = endpointID;
		
		rpcService = GWT.create(PropertyService.class);
	
		Row row = new Row();
		Column column = new Column(4, 0);
				
		
		
		
		
		ScrollPanel treeCon = new ScrollPanel();
		treeCon = buildTree(this.endpointClassID);
		column.add(treeCon);
		row.add(column);
		initWidget(row);
		

	}

		


	public ScrollPanel buildTree(final String endpointClassID) {

		final ScrollPanel con = new ScrollPanel();
		
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
						
						final Accordion accordion = new Accordion();
						
						Collections.sort(result);
						
						while(iT.hasNext()){
		
							//PropertyData currentItem = new PropertyData();
							final PropertyData currentItem = iT.next();
							
							final AccordionGroup accordionGroup = new AccordionGroup();
							accordionGroup.setHeading(currentItem.propertyName);
							
							final PropContent pc = new PropContent();
							
							rpcService.getValueForEndpointProperty(endpointID, currentItem.propertyId,
									new AsyncCallback<String>() {
								
								

										@Override
										public void onFailure(Throwable caught) {
											// TODO Auto-generated method stub
											removeLoadAnimation(animationLoading);
										}

										@Override
										public void onSuccess(String result) {
											// TODO Auto-generated method stub
											removeLoadAnimation(animationLoading);
											pc.setCurrentValue(result);
											
											Row row = new Row();
											row = accordionRowBuilder(Images.INSTANCE.tag(), currentItem.getPropertyName(), "Name:");					
											accordionGroup.add(row);
											row = accordionRowBuilder(Images.INSTANCE.eye(), pc.getCurrentValue(), "Currently set to: ");
											accordionGroup.add(row);
											row = accordionRowBuilder(Images.INSTANCE.clock(), currentItem.propertyName, "Last update:");
											accordionGroup.add(row);
											row = accordionRowBuilder(Images.INSTANCE.fire(), currentItem.propertyName, "Alerts:");
											accordionGroup.add(row);
											row = accordionRowBuilder(Images.INSTANCE.megaphone(), currentItem.propertyName, "Set new value:");
											accordionGroup.add(row);
																		
											accordionGroup.setHeading(currentItem.propertyName);
											
											accordion.add(accordionGroup);
											
											final SafeHtmlBuilder builder = new SafeHtmlBuilder();
											builder.appendHtmlConstant("<div style=\"border:1px solid #ddd;cursor:default\" class=\""
													+ "\">");
											builder.appendHtmlConstant("Drag "
													+ accordionGroup.getTitle()
													+ " into Check list to create a new check item");
											builder.appendHtmlConstant("</div>");
											
											DragSource source = new DragSource(
													accordionGroup) {
												@Override
												protected void onDragStart(
														DndDragStartEvent event) {
													super.onDragStart(event);
													// by
													// default
													// drag
													// is
													// allowed
													
													DragobjectAccordion dragAccordion = new DragobjectAccordion();
													dragAccordion.accordionGroup = accordionGroup;
													dragAccordion.propertyID = currentItem.propertyId;
													dragAccordion.properyClassID = currentItem.propertyclassId;
													dragAccordion.propertyName = currentItem.propertyName;
													
													event.setData(dragAccordion);
													event.getStatusProxy()
															.update(builder
																	.toSafeHtml());
												}
												

											};
											source.setGroup("check");
											
										}
									});

							
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
	
	private Row accordionRowBuilder(ImageResource imRes, String propertyItem, String itemDescriptor){
		Column iconColumn = new Column(1);
		iconColumn.addStyleName("text-center");
		Image img = new Image();
		img.setResource(imRes);
		iconColumn.add(img);
		
		
		Column textColumn = new Column(2);
		DynamicAnchor propertyLine = new DynamicAnchor(itemDescriptor + " " + propertyItem, true, null);
		
										
		textColumn.add(propertyLine);
		Row row = new Row();
		row.add(iconColumn);
		row.add(textColumn);
		return row;
	
	}
	
	
	
}