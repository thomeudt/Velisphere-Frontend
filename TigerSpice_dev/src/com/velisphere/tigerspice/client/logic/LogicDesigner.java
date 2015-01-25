package com.velisphere.tigerspice.client.logic;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import com.github.gwtbootstrap.client.ui.Breadcrumbs;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.checks.CheckService;
import com.velisphere.tigerspice.client.checks.CheckServiceAsync;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.logic.widgets.Explorer;
import com.velisphere.tigerspice.client.rules.CheckPathService;
import com.velisphere.tigerspice.client.rules.CheckPathServiceAsync;
import com.velisphere.tigerspice.client.rules.CheckpathCostIndicator;
import com.velisphere.tigerspice.client.rules.CheckpathEditorWidget;
import com.velisphere.tigerspice.client.rules.CheckpathList;
import com.velisphere.tigerspice.client.rules.MulticheckColumn;
import com.velisphere.tigerspice.client.rules.SameLevelCheckpathObject;
import com.velisphere.tigerspice.client.users.LoginSuccess;
import com.velisphere.tigerspice.shared.CheckPathObjectColumn;
import com.velisphere.tigerspice.shared.CheckPathObjectData;
import com.velisphere.tigerspice.shared.CheckPathObjectTree;

public class LogicDesigner extends Composite {

	@UiField
	CheckpathEditorWidget wgtCheckpathEditor;
	@UiField
	Paragraph txtSaveStatus;
	@UiField
	Breadcrumbs brdMain;
	@UiField
	TextBox txtCheckpathTitle;
	@UiField
	Button btnSaveCheckpath;
	NavLink bread0;
	NavLink bread1;
	NavLink bread2;
	String userID;
	@UiField
	CheckpathCostIndicator wgtCostIndicator;


	private CheckPathServiceAsync rpcServiceCheckPath;
	private CheckServiceAsync rpcServiceCheck;
	private String checkPathId;
	
	
	private static LogicDesignerUiBinder uiBinder = GWT
			.create(LogicDesignerUiBinder.class);

	interface LogicDesignerUiBinder extends UiBinder<Widget, LogicDesigner> {
	}

	public LogicDesigner(String userID) {

		this.userID = userID;
		rpcServiceCheckPath = GWT.create(CheckPathService.class);
		rpcServiceCheck = GWT.create(CheckService.class);
		initWidget(uiBinder.createAndBindUi(this));
		
		bread0 = new NavLink();
		bread0.setText("Home");
		brdMain.add(bread0);
		bread1 = new NavLink();
		bread1.setText("Logic Designer");
		brdMain.add(bread1);
		bread2 = new NavLink();
		bread2.setText("Create New Logic Design");
		brdMain.add(bread2);
		btnSaveCheckpath.setWidth("160px");
		wgtCostIndicator.setCheckpathEditor(wgtCheckpathEditor);
		
		
		bread0.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				RootPanel.get("main").clear();
				LoginSuccess loginSuccess = new LoginSuccess();
				RootPanel.get("main").add(loginSuccess);

			}
		});
		
		bread1.addClickHandler(
				new ClickHandler(){
					@Override
					public void onClick(ClickEvent event){
			
						RootPanel mainPanel = RootPanel.get("main");
						mainPanel.clear();
						CheckpathList checkPathList = new CheckpathList("dummy"); 		
						mainPanel.add(checkPathList);
						
					}
				});

		

	}

	@UiHandler("btnSaveCheckpath")
	void saveCheckpath(ClickEvent event) {
		final AnimationLoading loading = new AnimationLoading();

		System.out.println("[IN] Saving checkpath data: " + wgtCheckpathEditor);
				

		// create a new checkpath and get UUID. Checkpath UI Object will be added as a last step as we generate the JSON string as we iterate through the list objects
		
		

		rpcServiceCheckPath.addNewCheckpath(txtCheckpathTitle.getText(), userID, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				System.out.println("[ER] ERROR CREATING CHECKPATH: " + caught);

			}

			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub

				checkPathId = result;

				// first, create all checks
						
				
				LinkedHashSet<SameLevelCheckpathObject> checkpathHashset = wgtCheckpathEditor.checkHashSet;
								
				Iterator<SameLevelCheckpathObject> checksIT = checkpathHashset.iterator();
				while (checksIT.hasNext())
				{
					SameLevelCheckpathObject checkpathObject = checksIT.next();
					rpcServiceCheck.addNewCheck(checkpathObject.checkId, checkpathObject.endpointID, checkpathObject.propertyID, checkpathObject.triggerValue, checkpathObject.operator, checkpathObject.text, checkPathId, checkpathObject.actions,
							new AsyncCallback<String>() {

								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub
									System.out
											.println("[ER] ERROR creating check: "
													+ caught);
									txtSaveStatus
											.setText("Error saving logic design. Data not saved.");

								}

								@Override
								public void onSuccess(String result) {
									// TODO Auto-generated method stub

									System.out
											.println("[IN] Success creating check in checkpath: "
													+ result);
									
								}

							});					
				}
				
				
				
				
				// creating linked list to contain linked list of json objects
				// representing the entire checkpath chart
				CheckPathObjectTree allColumnsObject = new CheckPathObjectTree();

				Iterator<MulticheckColumn<SameLevelCheckpathObject>> it = wgtCheckpathEditor.multicheckColumns
						.iterator();

				while (it.hasNext()) {

					// creating linked list to contain json objects representing
					// a column in the checkpath chart
					CheckPathObjectColumn currentColumnObject = new CheckPathObjectColumn();

					MulticheckColumn<SameLevelCheckpathObject> multicheckColumn = it
							.next();

					Iterator<SameLevelCheckpathObject> checkIt = multicheckColumn
							.iterator();

					while (checkIt.hasNext()) {
						SameLevelCheckpathObject checkpathObject = checkIt
								.next();
						System.out.println("[IN] Creating JSON for:" + checkpathObject.text);

						// creating a serializable representation of the the
						// current check. children will not be added in
						// constructor to avoid double work

						
						
						CheckPathObjectData currentObjectData = new CheckPathObjectData(
								checkpathObject.checkId, checkpathObject.text,
								checkpathObject.empty, checkpathObject.level);
						
						currentObjectData.combination = checkpathObject.combination;

						// discard empty objects for writing checks to database
						if (checkpathObject.empty == false) {

							// write multicheck to database
							showLoadAnimation(loading);

							rpcServiceCheckPath.addNewMulticheck(
									checkpathObject.checkId,
									checkpathObject.combination,
									checkpathObject.text,
									checkPathId,
									checkpathObject.actions,
									new AsyncCallback<String>() {

										@Override
										public void onFailure(Throwable caught) {
											// TODO Auto-generated method stub
											removeLoadAnimation(loading);
											System.out
													.println("[ER] ERROR writing multicheck: "
															+ caught);
											txtSaveStatus
													.setText("Error saving logic design. Data not saved.");

										}

										@Override
										public void onSuccess(String result) {
											// TODO Auto-generated method stub

											removeLoadAnimation(loading);

											System.out
													.println("[IN] Success writing multicheck: "
															+ result);
											txtSaveStatus
													.setText("Logic design saved successfully.");

										}

									});

							// link multicheck to checkpath
			
							/**
							 * 
							 * Not needed anymore, checkpathid added to multicheck itself
							 * 
							rpcServiceCheckPath.addNewCheckpathMulticheckLink(
									checkPathId,
									checkpathObject.checkId,
									new AsyncCallback<String>() {

										@Override
										public void onFailure(Throwable caught) {
											// TODO Auto-generated method stub
											removeLoadAnimation(loading);
											System.out
													.println("ERROR writing multicheck: "
															+ caught);
											txtSaveStatus
													.setText("Error saving logic design. Data not saved.");

										}

										@Override
										public void onSuccess(String result) {
											// TODO Auto-generated method stub

											removeLoadAnimation(loading);

											System.out
													.println("Success linking multicheck to checkpath: "
															+ result);
											
										}

									});

							*/
							
							Iterator<SameLevelCheckpathObject> mccIt = checkpathObject.childChecks
									.iterator();

							// write link to child checks to database and add
							// children to json

							while (mccIt.hasNext()) {
								SameLevelCheckpathObject childCheck = mccIt
										.next();
								showLoadAnimation(loading);

								// json first

								currentObjectData.childChecks
										.add(childCheck.checkId);

								// now write to database

								rpcServiceCheckPath.addNewMulticheckCheckLink(
										checkpathObject.checkId,
										childCheck.checkId,
										checkPathId,
										new AsyncCallback<String>() {

											@Override
											public void onFailure(
													Throwable caught) {
												// TODO Auto-generated method
												// stub
												removeLoadAnimation(loading);
												System.out
														.println("[ER] ERROR writing multicheck_check_link: "
																+ caught);
												txtSaveStatus
														.setText("Error saving logic design. Data not saved.");

											}

											@Override
											public void onSuccess(String result) {
												// TODO Auto-generated method
												// stub
												removeLoadAnimation(loading);
												System.out
														.println("[IN] Success writing multicheck_check_link: "
																+ result);
												txtSaveStatus
														.setText("Logic design saved successfully.");

											}

										});

							}

							Iterator<SameLevelCheckpathObject> mcmcIt = checkpathObject.childMultichecks
									.iterator();

							
							// write link to child multichecks to database
														
							while (mcmcIt.hasNext()) {
								SameLevelCheckpathObject childMulticheck = mcmcIt
										.next();
								showLoadAnimation(loading);

								// json first

								currentObjectData.childMultichecks
										.add(childMulticheck.checkId);

								// now write to database

								rpcServiceCheckPath
										.addNewMulticheckMulticheckLink(
												checkpathObject.checkId,
												childMulticheck.checkId,
												checkPathId,
												new AsyncCallback<String>() {

													@Override
													public void onFailure(
															Throwable caught) {
														// TODO Auto-generated
														// method stub
														removeLoadAnimation(loading);
														System.out
																.println("[ER] ERROR writing multicheck_multicheck_link: "
																		+ caught);
														txtSaveStatus
																.setText("Error saving logic design. Data not saved.");

													}

													@Override
													public void onSuccess(
															String result) {
														// TODO Auto-generated
														// method stub
														removeLoadAnimation(loading);
														System.out
																.println("[IN] Success writing multicheck_multicheck_link: "
																		+ result);
														txtSaveStatus
																.setText("Logic design saved successfully.");

													}

												});
							}
							

						}

						
						currentColumnObject.column.add(currentObjectData);
					}
					allColumnsObject.tree.add(currentColumnObject);
				}
				

				// add baselayer check to checkpath object tree before generating json
				
					// first, create a linked list of CheckpathObjectData Objects that contain the data of the base layer
					
					LinkedList<CheckPathObjectData> baseLayer = new LinkedList<CheckPathObjectData>();
					Iterator<SameLevelCheckpathObject> baseIt = checkpathHashset.iterator();
					while (baseIt.hasNext()){
						CheckPathObjectData cpdItem = new CheckPathObjectData();
						SameLevelCheckpathObject cpItem = baseIt.next();
						cpdItem.checkId = cpItem.checkId;
						cpdItem.empty = cpItem.empty;
						cpdItem.text = cpItem.text;
						cpdItem.operator = cpItem.operator;
						cpdItem.propertyID = cpItem.propertyID;
						cpdItem.triggerValue = cpItem.triggerValue;
						cpdItem.endpointID = cpItem.endpointID;
						baseLayer.add(cpdItem);
						
					}
				
				allColumnsObject.baseLayer.addAll(baseLayer);
				
				
				
				rpcServiceCheckPath.createJsonCheckpath(allColumnsObject,
						new AsyncCallback<String>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								System.out.println("[ER] ERROR Creating JSON Checkpath: " + caught);

							}

							@Override
							public void onSuccess(String result) {
								// TODO Auto-generated method stub

								System.out.println("[IN] Checkpath successfully converted to JSON: " + result);

								rpcServiceCheckPath.updateCheckpath(
										checkPathId, result,
										new AsyncCallback<String>() {

											@Override
											public void onFailure(
													Throwable caught) {
												// TODO Auto-generated method
												// stub
												System.out
														.println("[ER] ERROR SAVING JSON: "
																+ caught);

											}

											@Override
											public void onSuccess(String result) {
												// TODO Auto-generated method
												// stub

												System.out
														.println("[IN] JSON SAVED SUCCESSFULLY: "
																+ result);
												
												
												// open edit view 
												
												/**
												final RootPanel mainPanel = RootPanel.get("main");
												mainPanel.clear();
												final CheckpathEditView checkpathEditView = new CheckpathEditView(checkPathId);
												
												Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
												    public void execute() {
												    	mainPanel.add(checkpathEditView);
												    }									    
												 
												});
												**/
												
												AppController.openLogicDesign(checkPathId);

												
												
												
												

												// Just validating that it
												// works, delete later

												rpcServiceCheckPath
														.getUiObjectJSONForCheckpathID(
																checkPathId,
																new AsyncCallback<CheckPathObjectTree>() {

																	@Override
																	public void onFailure(
																			Throwable caught) {
																		// TODO
																		// Auto-generated
																		// method
																		// stub
																		System.out
																				.println("[ER] ERROR OPENING JSON: "
																						+ caught);

																	}

																	@Override
																	public void onSuccess(
																			CheckPathObjectTree result) {
																		// TODO
																		// Auto-generated
																		// method
																		// stub

																		System.out
																				.println("[IN] JSON RETRIEVED SUCCESSFULLY: "
																						+ result.tree);
																		Iterator<CheckPathObjectColumn> rIT = result.tree
																				.iterator();
																		while (rIT
																				.hasNext()) {
																			CheckPathObjectColumn columnObject = rIT
																					.next();
																			Iterator<CheckPathObjectData> cIT = columnObject.column
																					.iterator();
																			while (cIT
																					.hasNext()) {
																				CheckPathObjectData field = cIT
																						.next();
																				System.out
																						.println("[IN] Field retrieved: "
																								+ field.text
																								+ "with combination "
																								+ field.combination);

																				Iterator<String> ccIT = field.childChecks
																						.iterator();
																				while (ccIT
																						.hasNext()) {
																					System.out
																							.println("[IN] Child Check found: "
																									+ ccIT.next());
																				}

																				Iterator<String> cmcIT = field.childMultichecks
																						.iterator();
																				while (cmcIT
																						.hasNext()) {
																					System.out
																							.println("[IN] Child Multi Check found: "
																									+ cmcIT.next());
																				}

																			}

																		}

																	}

																});

											}

										});

							}

						});

			}

		});
		
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
	
	@UiFactory CheckpathEditorWidget makeCheckpathEditor() { // method name is insignificant
	    return new CheckpathEditorWidget(null);
	  }

	@UiFactory Explorer makeSensorWidget() { // method name is insignificant
	    return new Explorer(this.userID);
	  }


}
