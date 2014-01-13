package com.velisphere.tigerspice.client.rules;

import java.util.Iterator;
import java.util.List;

import com.github.gwtbootstrap.client.ui.Breadcrumbs;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.PageHeader;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.spheres.SphereEditorWidget;
import com.velisphere.tigerspice.shared.CheckPathData;
import com.velisphere.tigerspice.shared.CheckPathObjectColumn;
import com.velisphere.tigerspice.shared.CheckPathObjectData;
import com.velisphere.tigerspice.shared.CheckPathObjectTree;

public class CheckpathEditView extends Composite {


	@UiField
	CheckpathEditorWidget wgtCheckpathEditor;
	@UiField
	Paragraph txtSaveStatus;
	@UiField
	Breadcrumbs brdMain;
	@UiField
	PageHeader pghCheckpathName;
	@UiField
	Button btnSaveCheckpath;
	NavLink bread0;
	NavLink bread1;
	NavLink bread2;
	

	private CheckPathServiceAsync rpcServiceCheckPath;
	private TextBox checkpathChangeNameField;

	private static CheckpathEditViewUiBinder uiBinder = GWT
			.create(CheckpathEditViewUiBinder.class);

	interface CheckpathEditViewUiBinder extends UiBinder<Widget, CheckpathEditView> {
	}

	private String userID;
	
	private String checkpathID;

	public CheckpathEditView(String checkpathID) {

		this.checkpathID = checkpathID;
		rpcServiceCheckPath = GWT.create(CheckPathService.class);
		initWidget(uiBinder.createAndBindUi(this));
		bread0 = new NavLink();
		bread0.setText("Home");
		brdMain.add(bread0);
		bread1 = new NavLink();
		bread1.setText("Logic Designer");
		brdMain.add(bread1);
		btnSaveCheckpath.setWidth("160px");
		
		
		rpcServiceCheckPath.getCheckpathDetails(this.checkpathID, new AsyncCallback<CheckPathData>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				System.out.println("ERROR CREATING CHECKPATH: " + caught);

			}

			@Override
			public void onSuccess(CheckPathData result) {
				// TODO Auto-generated method stub

				pghCheckpathName.setText(result.checkpathName);
				bread2 = new NavLink();
				bread2.setText(result.checkpathName);
				brdMain.add(bread2);
			}
		});

		// name change popup
		
		Icon icnCheckpathName = new Icon();
		icnCheckpathName.setType(IconType.EDIT);
		pghCheckpathName.add(icnCheckpathName);
		final Anchor ancEditCheckpathName = new Anchor();
		ancEditCheckpathName.setText(" Change Name of this Logic");
		ancEditCheckpathName.setHref("#");
		pghCheckpathName.add(ancEditCheckpathName);
		checkpathChangeNameField = new TextBox();
		final Button okButton = new Button();
		okButton.setText("OK");
		okButton.setType(ButtonType.SUCCESS);
		final PopupPanel nameChangePopup = new PopupPanel();
		final String CheckpathIDforNameChange = checkpathID;
		ancEditCheckpathName.addClickHandler(
				new ClickHandler(){ 
				public void onClick(ClickEvent event)  {
					
					
					if (nameChangePopup.isShowing()) {
						
						nameChangePopup.removeFromParent();
					} else
					{						
						
						HorizontalPanel horizontalPanel = new HorizontalPanel();
						horizontalPanel.add(checkpathChangeNameField);
						horizontalPanel.add(okButton.asWidget());
						nameChangePopup.clear();
						nameChangePopup.add(horizontalPanel);
						nameChangePopup.setModal(true);
						nameChangePopup.setAutoHideEnabled(true);
						nameChangePopup.setAnimationEnabled(true);
						nameChangePopup.showRelativeTo(ancEditCheckpathName);
						checkpathChangeNameField.setFocus(true);
						checkpathChangeNameField.setText(pghCheckpathName.getText());
						
						okButton.addClickHandler(
								new ClickHandler(){ 
								public void onClick(ClickEvent event)  {
									
									final String newCheckpathName = checkpathChangeNameField.getText();
									
									nameChangePopup.hide();
									final AnimationLoading animation = new AnimationLoading();
									animation.showLoadAnimation("Saving...");
									
									
									rpcServiceCheckPath.updateCheckpathName(CheckpathIDforNameChange, 
											newCheckpathName, new AsyncCallback<String>() {

										@Override
										public void onFailure(Throwable caught) {
											// TODO Auto-generated method stub
											System.out.println("ERROR RENAMING CHECKPATH: " + caught);

										}

										@Override
										public void onSuccess(String result) {
											// TODO Auto-generated method stub

											pghCheckpathName.setText(newCheckpathName);
											animation.removeLoadAnimation();

										}
									});
								}
								});
					}
				}
			});
		}
		
		

								

	@UiHandler("btnSaveCheckpath")
	void saveCheckpath(ClickEvent event) {
		final AnimationLoading loading = new AnimationLoading();
		
		// getting current checkpath ID and adding to final variable checkpathIDUpdate
		
		final String checkPathIdUpdate = this.checkpathID;

		System.out.println("Update checkpath data: " + wgtCheckpathEditor);
				
		

		
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
						System.out.println("Name:" + checkpathObject.text);

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
							
								// deleted as we are updating here, this will be handled seperately

							Iterator<SameLevelCheckpathObject> mccIt = checkpathObject.childChecks
									.iterator();

							// write link to child checks to database and add
							// children to json

							while (mccIt.hasNext()) {
								SameLevelCheckpathObject childCheck = mccIt
										.next();
								final AnimationLoading animation = new AnimationLoading();
								

								// json first

								currentObjectData.childChecks
										.add(childCheck.checkId);

								// now write to database
									// skipped as we are updating here

							}

							Iterator<SameLevelCheckpathObject> mcmcIt = checkpathObject.childMultichecks
									.iterator();

							// write link to child multichecks to database
							while (mcmcIt.hasNext()) {
								SameLevelCheckpathObject childMulticheck = mcmcIt
										.next();
								

								// json first

								currentObjectData.childMultichecks
										.add(childMulticheck.checkId);

								// now write to database
								// skipped as we are updating here

							}

						}

						
						
						currentColumnObject.column.add(currentObjectData);
					}
					allColumnsObject.tree.add(currentColumnObject);
				}
				
				// write to database
				
				
				List<SameLevelCheckpathObject> updatedMultichecks = wgtCheckpathEditor.getUpdatedMultichecks();
				List<SameLevelCheckpathObject> newMultichecks = wgtCheckpathEditor.getNewMultichecks();
				updatedMultichecks.removeAll(newMultichecks); // remove duplicates if a new multicheck has been updated before saving
				Iterator<SameLevelCheckpathObject> updatedMultichecksIt = updatedMultichecks.iterator();
				Iterator<SameLevelCheckpathObject> newMultichecksIt = newMultichecks.iterator();
				System.out.println("###########NEW MULTICHECKS: " + newMultichecks);
				System.out.println("###########UPDATED MULTICHECKS: " + updatedMultichecks);
				
				// run the new multichecks first
				
				final AnimationLoading animation = new AnimationLoading();
				animation.showLoadAnimation("Saving multichecks...");
				
				while (newMultichecksIt.hasNext()){
					SameLevelCheckpathObject checkpathObject = newMultichecksIt.next();
					//showLoadAnimation(loading);

					// create check
					
					rpcServiceCheckPath.addNewMulticheck(
							checkpathObject.checkId,
							checkpathObject.combination,
							checkpathObject.text,
							new AsyncCallback<String>() {

								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub
									animation.removeLoadAnimation();
									System.out
											.println("ERROR writing multicheck: "
													+ caught);
									txtSaveStatus
											.setText("Error saving logic design. Data not saved.");

								}

								@Override
								public void onSuccess(String result) {
									// TODO Auto-generated method stub

									animation.removeLoadAnimation();

									System.out
											.println("Success writing multicheck: "
													+ result);
									txtSaveStatus
											.setText("Logic design saved successfully.");

								}

							});
					
					// now link
					
					Iterator<SameLevelCheckpathObject> mccIt = checkpathObject.childChecks
							.iterator();

					// write link to child checks to database
					

					while (mccIt.hasNext()) {
						SameLevelCheckpathObject childCheck = mccIt
								.next();
						final AnimationLoading animationChecks = new AnimationLoading();
						animation.showLoadAnimation("Saving Checks...");

						rpcServiceCheckPath.addNewMulticheckCheckLink(
								checkpathObject.checkId,
								childCheck.checkId,
								new AsyncCallback<String>() {

									@Override
									public void onFailure(
											Throwable caught) {
										// TODO Auto-generated method
										// stub
										animationChecks.removeFromParent();
										System.out
												.println("ERROR writing multicheck_check_link: "
														+ caught);
										txtSaveStatus
												.setText("Error saving logic design. Data not saved.");

									}

									@Override
									public void onSuccess(String result) {
										// TODO Auto-generated method
										// stub
										animationChecks.removeFromParent();
										System.out
												.println("Success writing multicheck_check_link: "
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
						
						animation.showLoadAnimation("Linking...");

						rpcServiceCheckPath.addNewMulticheckMulticheckLink(
								checkpathObject.checkId,
								childMulticheck.checkId,
								new AsyncCallback<String>() {

									@Override
									public void onFailure(
											Throwable caught) {
										// TODO Auto-generated method
										// stub
										animation.removeLoadAnimation();
										System.out
												.println("ERROR writing multicheck_check_link: "
														+ caught);
										txtSaveStatus
												.setText("Error saving logic design. Data not saved.");

									}

									@Override
									public void onSuccess(String result) {
										// TODO Auto-generated method
										// stub
										animation.removeLoadAnimation();
										System.out
												.println("Success writing multicheck_multicheck_link: "
														+ result);
										txtSaveStatus
												.setText("Logic design saved successfully.");

									}

								});

					}


				}
				
				
				// now run the updated multichecks 
				
				while (updatedMultichecksIt.hasNext()){
					final SameLevelCheckpathObject checkpathObject = updatedMultichecksIt.next();
					
					animation.showLoadAnimation("Updating Checks...");

					// update check
					
					rpcServiceCheckPath.updateMulticheck(
							checkpathObject.checkId,
							checkpathObject.combination,
							checkpathObject.text,
							new AsyncCallback<String>() {

								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub
									animation.removeLoadAnimation();
									System.out
											.println("ERROR updating multicheck: "
													+ caught);
									txtSaveStatus
											.setText("Error saving logic design. Data not saved.");

								}

								@Override
								public void onSuccess(String result) {
									// TODO Auto-generated method stub

									animation.removeLoadAnimation();

									System.out
											.println("Success updating multicheck: "
													+ result);
									txtSaveStatus
											.setText("Logic design saved successfully.");
									
									
									// now link
									
									
									
									// first, delete all multichecklinks for the current multicheck, then re-link with the new links
									
									
									animation.showLoadAnimation("Linking...");
									
									final SameLevelCheckpathObject currentCheckpathObject = checkpathObject;
									
									rpcServiceCheckPath.deleteMulticheckMulticheckLink(
											checkpathObject.checkId,
											new AsyncCallback<String>() {

												@Override
												public void onFailure(
														Throwable caught) {
													// TODO Auto-generated method
													// stub
													animation.removeLoadAnimation();
													System.out
															.println("ERROR deleting multicheck_multicheck_link: "
																	+ caught);
													txtSaveStatus
															.setText("Error saving logic design. Data not saved.");

												}

												@Override
												public void onSuccess(String result) {
													// TODO Auto-generated method
													// stub
													Iterator<SameLevelCheckpathObject> mcmcIt = currentCheckpathObject.childMultichecks
															.iterator();

													
													// write link to child multichecks to database
													

													while (mcmcIt.hasNext()) {
														SameLevelCheckpathObject childMulticheck = mcmcIt
																.next();
														
														animation.showLoadAnimation("Linking...");

														rpcServiceCheckPath.addNewMulticheckMulticheckLink(
																currentCheckpathObject.checkId,
																childMulticheck.checkId,
																new AsyncCallback<String>() {

																	@Override
																	public void onFailure(
																			Throwable caught) {
																		// TODO Auto-generated method
																		// stub
																		animation.removeLoadAnimation();
																		System.out
																				.println("ERROR writing multicheck_multicheck_link: "
																						+ caught);
																		txtSaveStatus
																				.setText("Error saving logic design. Data not saved.");

																	}

																	@Override
																	public void onSuccess(String result) {
																		// TODO Auto-generated method
																		// stub
																		animation.removeLoadAnimation();
																		System.out
																				.println("Success rewriting multicheck_multicheck_link: "
																						+ result);
																		txtSaveStatus
																				.setText("Logic design saved successfully.");

																	}

																});

													}

												}

											});

									
									// secondly, delete all checklinks for the current multicheck, then re-link with the new links
									
									rpcServiceCheckPath.deleteMulticheckCheckLink(
											checkpathObject.checkId,
											new AsyncCallback<String>() {

												@Override
												public void onFailure(
														Throwable caught) {
													// TODO Auto-generated method
													// stub
													animation.removeLoadAnimation();
													System.out
															.println("ERROR deleting multicheck_check_link: "
																	+ caught);
													txtSaveStatus
															.setText("Error saving logic design. Data not saved.");

												}

												@Override
												public void onSuccess(String result) {
													// TODO Auto-generated method
													// stub
													Iterator<SameLevelCheckpathObject> mccIt = currentCheckpathObject.childChecks
															.iterator();

													
													// write link to child multichecks to database
													

													while (mccIt.hasNext()) {
														SameLevelCheckpathObject childCheck = mccIt
																.next();
														
														animation.showLoadAnimation("Linking...");

														rpcServiceCheckPath.addNewMulticheckCheckLink(
																currentCheckpathObject.checkId,
																childCheck.checkId,
																new AsyncCallback<String>() {

																	@Override
																	public void onFailure(
																			Throwable caught) {
																		// TODO Auto-generated method
																		// stub
																		animation.removeLoadAnimation();
																		System.out
																				.println("ERROR writing multicheck_check_link: "
																						+ caught);
																		txtSaveStatus
																				.setText("Error saving logic design. Data not saved.");

																	}

																	@Override
																	public void onSuccess(String result) {
																		// TODO Auto-generated method
																		// stub
																		animation.removeLoadAnimation();
																		System.out
																				.println("Success rewriting multicheck_check_link: "
																						+ result);
																		txtSaveStatus
																				.setText("Logic design saved successfully.");

																	}

																});

													}

												}

											});


								}

							});
					
										

				}

				
				// now remove all orphan multichecks from deletions and their links
				
				Iterator<SameLevelCheckpathObject> deletedMultichecksIT = wgtCheckpathEditor.getDeletedMultichecks().iterator();
				while(deletedMultichecksIT.hasNext()){
					SameLevelCheckpathObject checkpathObject = deletedMultichecksIT.next();

					rpcServiceCheckPath.deleteMulticheck(
							checkpathObject.checkId,
							new AsyncCallback<String>() {

								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub
									
									System.out
											.println("ERROR deleting multicheck: "
													+ caught);
								}

								@Override
								public void onSuccess(String result) {
									// TODO Auto-generated method stub
									System.out
									.println("Success deleting multicheck: "
											+ result);
									txtSaveStatus
									.setText("Logic design saved successfully.");

									
								}
							});
					
					
					rpcServiceCheckPath.deleteMulticheckMulticheckLink(
							checkpathObject.checkId,
							new AsyncCallback<String>() {

								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub
									
									System.out
											.println("ERROR deleting multicheck_multicheck_link: "
													+ caught);
								}

								@Override
								public void onSuccess(String result) {
									// TODO Auto-generated method stub
									System.out
									.println("Success deleting multicheck_multicheck_link: "
											+ result);
									txtSaveStatus
									.setText("Logic design saved successfully.");

									
								}
							});
					
					rpcServiceCheckPath.deleteMulticheckCheckLink(
							checkpathObject.checkId,
							new AsyncCallback<String>() {

								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub
									
									System.out
											.println("ERROR deleting multicheck_check_link: "
													+ caught);
								}

								@Override
								public void onSuccess(String result) {
									// TODO Auto-generated method stub
									System.out
									.println("Success deleting multicheck_check_link: "
											+ result);
									txtSaveStatus
									.setText("Logic design saved successfully.");

									
								}
							});


				}
				
				
				
				animation.removeLoadAnimation();
				
				System.out.println("Updated Multichecks: " + wgtCheckpathEditor.getUpdatedMultichecks());

				System.out.println("Added Multichecks: " + wgtCheckpathEditor.getNewMultichecks());

				System.out.println("Deleted Multichecks: " + wgtCheckpathEditor.getDeletedMultichecks());

				
				createCheckpathJSON(allColumnsObject, checkPathIdUpdate);
				
				// reset create/update/delete (CUD) tracker for checks so that DB actions will not be done again with next save and fail
				wgtCheckpathEditor.resetNewMultichecks();
				wgtCheckpathEditor.resetUpdatedMultichecks();
				wgtCheckpathEditor.resetDeletedMultichecks();

				
				
	}

	private void createCheckpathJSON(CheckPathObjectTree allColumnsObject, final String checkPathIdUpdate){
		rpcServiceCheckPath.createJsonCheckpath(allColumnsObject,
				new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						System.out.println("ERROR: " + caught);

					}

					@Override
					public void onSuccess(String result) {
						// TODO Auto-generated method stub

						System.out.println("JSON CHECKPATH: " + result);

						rpcServiceCheckPath.updateCheckpath(
								checkPathIdUpdate, result,
								new AsyncCallback<String>() {

									@Override
									public void onFailure(
											Throwable caught) {
										// TODO Auto-generated method
										// stub
										System.out
												.println("ERROR SAVING JSON: "
														+ caught);

									}

									@Override
									public void onSuccess(String result) {
										// TODO Auto-generated method
										// stub

										System.out
												.println("JSON SAVED SUCCESSFULLY: "
														+ result);

										
									}

								});

					}

			

	

});

	}
	
	
	@UiFactory CheckpathEditorWidget makeCheckpathEditor() { // method name is insignificant
	    return new CheckpathEditorWidget(this.checkpathID);
	  }


}
