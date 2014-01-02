package com.velisphere.tigerspice.client.rules;

import java.util.Iterator;
import java.util.LinkedList;

import com.github.gwtbootstrap.client.ui.Breadcrumbs;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.spheres.SphereEditorWidget;
import com.velisphere.tigerspice.shared.CheckPathObjectData;

public class RuleView extends Composite {

	@UiField
	CheckpathEditorWidget wgtCheckpathEditor;
	@UiField
	Paragraph txtSaveStatus;
	@UiField
	Breadcrumbs brdMain;
	NavLink bread0;
	NavLink bread1;

	private CheckPathServiceAsync rpcServiceCheckPath;

	private static RuleViewUiBinder uiBinder = GWT
			.create(RuleViewUiBinder.class);

	interface RuleViewUiBinder extends UiBinder<Widget, RuleView> {
	}

	private String userID;

	public RuleView() {

		rpcServiceCheckPath = GWT.create(CheckPathService.class);
		initWidget(uiBinder.createAndBindUi(this));
		bread0 = new NavLink();
		bread0.setText("Home");
		brdMain.add(bread0);
		bread1 = new NavLink();
		bread1.setText("Logic Designer");
		brdMain.add(bread1);

	}

	@UiHandler("btnSaveCheckpath")
	void saveCheckpath(ClickEvent event) {
		final AnimationLoading loading = new AnimationLoading();

		System.out.println("Save checkpath data: " + wgtCheckpathEditor);

		// create a new checkpath and get UUID

		rpcServiceCheckPath.addNewCheckpath(new AsyncCallback<String>() {

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
				LinkedList<LinkedList<CheckPathObjectData>> allColumnsObject = new LinkedList<LinkedList<CheckPathObjectData>>();

				Iterator<MulticheckColumn<SameLevelCheckpathObject>> it = wgtCheckpathEditor.multicheckColumns
						.iterator();

				while (it.hasNext()) {

					// creating linked list to contain json objects representing
					// a column in the checkpath chart
					LinkedList<CheckPathObjectData> currentColumnObject = new LinkedList<CheckPathObjectData>();

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

						currentColumnObject.add(currentObjectData);
					}
					allColumnsObject.add(currentColumnObject);
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
								
								rpcServiceCheckPath.updateCheckpath(checkPathId, result,
										new AsyncCallback<String>() {

											@Override
											public void onFailure(Throwable caught) {
												// TODO Auto-generated method stub
												System.out.println("ERROR SAVING JSON: " + caught);

											}

											@Override
											public void onSuccess(String result) {
												// TODO Auto-generated method stub

												System.out.println("JSON SAVED SUCCESSFULLY: " + result);
												
												
												
												

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

}
