package com.velisphere.tigerspice.client.dash;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import com.github.gwtbootstrap.client.ui.AlertBlock;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ButtonGroup;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.AlertType;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.github.gwtbootstrap.client.ui.resources.ButtonSize;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.kiouri.sliderbar.client.view.SliderBarHorizontal;
import com.velisphere.tigerspice.client.appcontroller.SessionHelper;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.endpoints.EndpointServiceAsync;
import com.velisphere.tigerspice.client.event.EventUtils;
import com.velisphere.tigerspice.client.event.RemoveGaugeFromDashEvent;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.properties.PropertyService;
import com.velisphere.tigerspice.client.properties.PropertyServiceAsync;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassService;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassServiceAsync;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.PropertyClassData;
import com.velisphere.tigerspice.shared.PropertyData;

public class SwitchBox extends Composite{

	VerticalPanel panel;
	HTML description = new HTML("<br><b><i>New Switch</b></i>");
	ListBox lbxProperties = new ListBox();
	ListBox lbxEndpoints = new ListBox();
	Button btnOK = new Button("OK");
	String endpointID;
	String propertyID;
	HashMap<String, PropertyData> propertyDataMap = new HashMap<String, PropertyData>();
	Boolean isOnOffSwitch;

	
	public SwitchBox()
	{
		panel = new VerticalPanel();
		panel.setHeight("180px");
		initWidget(panel);
		
		setConfigOkButton();
		addSwitch();

	}

	
	private void addSwitch()
	{
	
		panel.add(description);
		configure();
		
	}
	
	private void configure() {
		showConfigureBox();
		
		

	}
	
	private void showConfigureBox()
	{
		panel.clear();
		panel.add(new HTML("<br><strong>Configure Switch</strong>"));
		panel.add(lbxEndpoints);
	
		lbxEndpoints.setWidth("100%");
		if(lbxEndpoints.getItemCount()==0)
		{
			fillEndpointDropDown(lbxEndpoints);		
		}
			
			
		
	}
	
	private HorizontalPanel controls() {

		HorizontalPanel panel = new HorizontalPanel();
		Button configureButton = new Button("Configure");
		configureButton.setSize(ButtonSize.MINI);
		configureButton.setType(ButtonType.PRIMARY);
		configureButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				configure();

			}

		});

		panel.add(configureButton);

		Button removeButton = new Button("Remove");
		removeButton.setSize(ButtonSize.MINI);
		removeButton.setType(ButtonType.DANGER);

		final SwitchBox gauge = this;

		removeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				// remove physically
				gauge.getParent().removeFromParent();
				

			}

		});

		panel.add(removeButton);
		return panel;

	}

	
	private void fillEndpointDropDown(final ListBox lbxEndpoints)
	{
		
		final AnimationLoading animationLoading = new AnimationLoading("Loading Device List...");
		animationLoading.showLoadAnimation();

		
		EndpointServiceAsync endpointService = GWT
				.create(EndpointService.class);

	
		
		endpointService.getEndpointsForUser(SessionHelper.getCurrentUserID(), new AsyncCallback<LinkedList<EndpointData>>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(LinkedList<EndpointData> result) {
	
				Iterator<EndpointData> it = result.iterator();
				while(it.hasNext())
				{
					EndpointData endpointData = it.next();
					lbxEndpoints.addItem(endpointData.endpointName, endpointData.endpointId);
								
				}
				
				lbxEndpoints.addChangeHandler(new ChangeHandler(){

					@Override
					public void onChange(ChangeEvent event) {
						endpointID = lbxEndpoints.getSelectedValue();
						
						if (lbxProperties.isAttached())
						{
							lbxProperties.removeFromParent();
							
						}
						if(btnOK.isAttached())
						{
							btnOK.removeFromParent();
						}
						panel.add(lbxProperties);
						lbxProperties.setWidth("100%");
						fillPropertyDropDown(lbxEndpoints.getSelectedValue());		
					}
					
				});

				animationLoading.removeLoadAnimation();
				
			}
			
		});
	}


	private void fillPropertyDropDown(String endpointID)
	{
		
		final AnimationLoading animationLoading = new AnimationLoading("Loading property list...");
		animationLoading.showLoadAnimation();

		
		PropertyServiceAsync propertyService = GWT
				.create(PropertyService.class);

	
		
		propertyService.getActorPropertiesForEndpointID(endpointID, new AsyncCallback<LinkedList<PropertyData>>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(LinkedList<PropertyData> result) {

				lbxProperties.clear();
				lbxProperties.addItem("---");
				propertyDataMap.clear();
				Iterator<PropertyData> it = result.iterator();
				while(it.hasNext())
				{
				
					PropertyData propertyData = it.next();


					propertyDataMap.put(propertyData.propertyId, propertyData);
					lbxProperties.addItem(propertyData.propertyName, propertyData.propertyId);
					
				}
				
				description = new HTML("<br><b><i>"+lbxProperties.getSelectedItemText()+"</b></i>");

				
				lbxProperties.addChangeHandler(new ChangeHandler(){

					@Override
					public void onChange(ChangeEvent event) {
						

						description = new HTML("<br><b><i>"+lbxProperties.getSelectedItemText()+"</b></i>");
						RootPanel.get().add(new HTML("Selected: " + lbxProperties.getSelectedValue()));

						getPropertyClassData(propertyDataMap.get(lbxProperties.getSelectedValue()).propertyclassId);
						
						propertyID = lbxProperties.getSelectedValue();
								
						
					
					}
					
				});

					
				animationLoading.removeLoadAnimation();
			}
			
		});
	}

	
	private void getPropertyClassData(String propertyClassID)
	{


		final AnimationLoading animationLoading = new AnimationLoading("Loading property classes...");
		animationLoading.showLoadAnimation();



		PropertyClassServiceAsync propertyClassService = GWT
				.create(PropertyClassService.class);

		propertyClassService.getPropertyClassForPropertyClassID(propertyClassID, new AsyncCallback<PropertyClassData>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(PropertyClassData result) {

				
				if (result.propertyClassDatatype == "Double")
				{

					isOnOffSwitch = false;
				
				} else
					if (result.propertyClassDatatype == "String")
					{
						isOnOffSwitch = false;
					} else
						if (result.propertyClassDatatype == "Byte")
						{
							isOnOffSwitch = true;
							
						}
							
				
				
		
				if(btnOK.isAttached())
				{
					btnOK.removeFromParent();
				}
				panel.add(btnOK);
		
				animationLoading.removeLoadAnimation();
			}
		});
		
		

	}

	
	
	private void setConfigOkButton()
	{
		btnOK.setSize(ButtonSize.SMALL);
		btnOK.setType(ButtonType.PRIMARY);
		btnOK.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				
				panel.clear();
				panel.add(description);

				
				if (isOnOffSwitch == true)
				{
					addOnOffButton();
				}
				else
				{
					addEntryField();
				}
					
				panel.add(controls());
			}
			
		});
	}

	
	
	private void addOnOffButton()
	{
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.setToggle("radio");
		Button on = new Button("On");
		on.setType(ButtonType.SUCCESS);
		on.setIcon(IconType.OFF);
		Button off = new Button("Off");
		off.setType(ButtonType.DANGER);
		off.setIcon(IconType.OFF);
		
		buttonGroup.add(on);
		buttonGroup.add(off);
		
		SimplePanel simplePanel = new SimplePanel();
		simplePanel.setHeight("180px");
		simplePanel.setWidth("180px");
		
		simplePanel.add(buttonGroup);
	
		
		panel.add(simplePanel);
		
	}

	private void addEntryField()
	{
		
		TextBox textBox = new TextBox();
		textBox.addStyleName("span2");
		

		SimplePanel simplePanel = new SimplePanel();
		simplePanel.setHeight("180px");
		simplePanel.setWidth("180px");
		
		simplePanel.add(textBox);
	
		
		panel.add(simplePanel);
		
	}

	
 
	
}


