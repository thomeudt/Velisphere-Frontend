package com.velisphere.tigerspice.client.admin.epc;

import java.io.File;






import com.github.gwtbootstrap.client.ui.Paragraph;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.endpointclasses.EPCService;
import com.velisphere.tigerspice.client.endpointclasses.EPCServiceAsync;
import com.velisphere.tigerspice.shared.EPCData;

public class EPCInfo extends Composite {

	@UiField Image imgEPCImage;
	@UiField Paragraph pgpEPCName;
	
	private static EPCInfoUiBinder uiBinder = GWT.create(EPCInfoUiBinder.class);

	interface EPCInfoUiBinder extends UiBinder<Widget, EPCInfo> {
	}

	public EPCInfo(String EPCID) {
		initWidget(uiBinder.createAndBindUi(this));
		final EPCServiceAsync epcService = GWT
				.create(EPCService.class);

		
		
		epcService.getEndpointClassForEndpointClassID(EPCID,
				new AsyncCallback<EPCData>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(EPCData result) {
						// TODO Auto-generated method stub
						System.out.println("URL: " + result.getPath());
						
						
						
						imgEPCImage.setUrl(GWT.getHostPageBaseURL()+"tigerspiceDownloads?privateURL="+result.getPath()
								+ "&outboundFileName=EPC_image&persist=1");
						pgpEPCName.setText(result.endpointclassName);

						
					}

	});
	}
	
	@UiHandler("btnClose")
	void closePopup(ClickEvent event){
		this.setVisible(false);
	}

}
