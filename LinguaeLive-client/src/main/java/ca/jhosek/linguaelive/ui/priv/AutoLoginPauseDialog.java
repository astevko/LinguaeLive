package ca.jhosek.main.client.ui.priv;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DialogBox;

import ca.jhosek.main.client.ui.priv.instructor.InstructorYourCourseViewImpl;
import ca.jhosek.main.shared.proxy.CourseProxy;

/**
 * display a Dialogbox telling the user that autologin is in progrss
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see InstructorYourCourseViewImpl
 * @see CourseProxy
 */
public class AutoLoginPauseDialog {

	private static CourseInviteDialogUiBinder uiBinder = GWT
	.create(CourseInviteDialogUiBinder.class);

	interface CourseInviteDialogUiBinder extends
	UiBinder<DialogBox, AutoLoginPauseDialog> {
	}

	public AutoLoginPauseDialog() {
		uiBinder.createAndBindUi(this);
		dialogBox.center();
		dialogBox.hide();		
	}

	@UiField
	DialogBox dialogBox;

	public void show() {
		// show 
		dialogBox.show();
	}

	public void hide() {
		// 
		dialogBox.hide();
	}
}
