package com.velisphere.tigerspice.client.rules;

import java.util.Iterator;

import com.github.gwtbootstrap.client.ui.Breadcrumbs;
import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.PageHeader;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
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
	NavLink bread0;
	NavLink bread1;
	NavLink bread2;

	private CheckPathServiceAsync rpcServiceCheckPath;

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
		
		
		

	}

	@UiHandler("btnSaveCheckpath")
	void saveCheckpath(ClickEvent event) {
		final AnimationLoading loading = new AnimationLoading();

		System.out.println("Save checkpath data: " + wgtCheckpathEditor);
				

		// create a new checkpath and get UUID. Checkpath UI Object will be added as a last step as we generate the JSON string as we iterate through the list objects
		
		

		rpcServiceCheckPath.addNewCheckpath(pghCheckpathName.getText(), new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				System.out.println("ERROR CREATING CHECKPATH: " + caught);

			}

			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub

				final String checkPathId = result;

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
							showLoadAnimation(loading);

							rpcServiceCheckPath.addNewMulticheck(
									checkpathObject.checkId,
									checkpathObject.combination,
									checkpathObject.text,
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
													.println("Success writing multicheck: "
															+ result);
											txtSaveStatus
													.setText("Logic design saved successfully.");

										}

									});

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
										new AsyncCallback<String>() {

											@Override
											public void onFailure(
													Throwable caught) {
												// TODO Auto-generated method
												// stub
												removeLoadAnimation(loading);
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
												removeLoadAnimation(loading);
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
								showLoadAnimation(loading);

								// json first

								currentObjectData.childMultichecks
										.add(childMulticheck.checkId);

								// now write to database

								rpcServiceCheckPath
										.addNewMulticheckMulticheckLink(
												checkpathObject.checkId,
												childMulticheck.checkId,
												new AsyncCallback<String>() {

													@Override
													public void onFailure(
															Throwable caught) {
														// TODO Auto-generated
														// method stub
														removeLoadAnimation(loading);
														System.out
																.println("ERROR writing multicheck_multicheck_link: "
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
																.println("Success writing multicheck_multicheck_link: "
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
				System.out.println("Gesamtobject:" + allColumnsObject);

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
										checkPathId, result,
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
																				.println("ERROR SAVING JSON: "
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
																				.println("JSON RETRIEVED SUCCESSFULLY: "
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
																						.println("Field retrieved: "
																								+ field.text
																								+ "with combination "
																								+ field.combination);

																				Iterator<String> ccIT = field.childChecks
																						.iterator();
																				while (ccIT
																						.hasNext()) {
																					System.out
																							.println("Child Check found: "
																									+ ccIT.next());
																				}

																				Iterator<String> cmcIT = field.childMultichecks
																						.iterator();
																				while (cmcIT
																						.hasNext()) {
																					System.out
																							.println("Child Multi Check found: "
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
	    return new CheckpathEditorWidget(this.checkpathID);
	  }


}