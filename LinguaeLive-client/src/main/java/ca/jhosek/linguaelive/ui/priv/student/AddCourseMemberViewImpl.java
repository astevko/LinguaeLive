/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.ui.priv.student;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Ignore;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import ca.jhosek.linguaelive.activity.mainregion.AddEditCourseActivity;
import ca.jhosek.linguaelive.place.AddEditCoursePlace;
import ca.jhosek.linguaelive.ui.priv.instructor.AddEditCourseView;

/**
 * admin display of user information
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see AddEditCourseView
 * @see AddEditCourseActivity
 * @see AddEditCoursePlace
 */
public class AddCourseMemberViewImpl extends Composite implements IsWidget, AddCourseMemberView {
	interface AdminCourseViewerUiBinder extends UiBinder<Widget, AddCourseMemberViewImpl> {
	}
	
	// logger boilerplate
	private static final Logger logger = Logger.getLogger(AddCourseMemberViewImpl.class
			.getName());

	private static AdminCourseViewerUiBinder uiBinder = GWT
			.create(AdminCourseViewerUiBinder.class);
	
	//--------- course invite form
	@UiField HTMLPanel courseForm;
	@Ignore	@UiField TextBox courseInviteCode;
	@UiField Button addCourseMemberButton;
	
	
	private Presenter presenter;

	public AddCourseMemberViewImpl() {
		// initialize language widget values
		initWidget(uiBinder.createAndBindUi(this));
	}


	@UiHandler("addCourseMemberButton")
	void onAddCourseMemberButtonButtonClick(ClickEvent event) {
		logger.info( "onCheckInviteCodeButtonClick()");
		presenter.saveCourseMember( courseInviteCode.getValue() );
	}

	/**
	 * @param presenter the presenter to set
	 */
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
}
