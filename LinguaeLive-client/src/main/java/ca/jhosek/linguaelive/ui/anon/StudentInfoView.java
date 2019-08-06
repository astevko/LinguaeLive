/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.ui.anon;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import ca.jhosek.main.client.activity.mainregion.StudentInfoActivity;

/**
 * Student Information View
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see StudentInfoActivity
 *
 */
public class StudentInfoView extends Composite  {

	public interface Presenter {
		void goToNewStudentForm();
	}
	
	private static PrivacyViewImplUiBinder uiBinder = GWT
			.create(PrivacyViewImplUiBinder.class);

	interface PrivacyViewImplUiBinder extends UiBinder<Widget, StudentInfoView> {
	}

	private Presenter presenter;

	public StudentInfoView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setPresenter(Presenter studentInfoActivity) {
		// 
		this.presenter = studentInfoActivity;
		
	}

	@UiHandler("newStudentButton")
	void onNewStudentButtonClick(ClickEvent event) {
		presenter.goToNewStudentForm();
	}

}
