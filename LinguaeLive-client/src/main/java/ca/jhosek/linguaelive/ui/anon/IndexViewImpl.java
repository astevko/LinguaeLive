/**
2 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.ui.anon;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * Index View
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class IndexViewImpl extends Composite implements IndexView {

	interface IndexViewImplUiBinder extends UiBinder<Widget, IndexViewImpl> {
	}
	private static IndexViewImplUiBinder uiBinder = GWT
			.create(IndexViewImplUiBinder.class);
//	@UiField Button loginButton;
	@UiField Button newStudentButton;
	@UiField Button newInstructorButton;

	private Presenter presenter;

	public IndexViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.anon.IndexView#setPresenter(ca.jhosek.linguaelive.ui.anon.IndexView.Presenter)
	 */
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
//	@UiHandler("loginButton")
//	void onLoginButtonClick(ClickEvent event) {
//		presenter.goToLoginForm();
//	}
	@UiHandler("newInstructorButton")
	void onNewInstructorButtonClick(ClickEvent event) {
		presenter.goToNewInstructorForm();
	}

	@UiHandler("newStudentButton")
	void onNewStudentButtonClick(ClickEvent event) {
		presenter.goToNewStudentForm();
	}
}
