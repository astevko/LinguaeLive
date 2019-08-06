package ca.jhosek.main.client.ui.priv.instructor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.InlineLabel;

import ca.jhosek.main.shared.proxy.CourseProxy;

/**
 * Display a modal dialog with the text of an email
 *  to send to students for them join the course
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see InstructorYourCourseViewImpl
 * @see CourseProxy
 */
public class CourseStudentInviteDialog {

	private static CourseInviteDialogUiBinder uiBinder = GWT
	.create(CourseInviteDialogUiBinder.class);

	interface CourseInviteDialogUiBinder extends
	UiBinder<DialogBox, CourseStudentInviteDialog> {
	}

	public CourseStudentInviteDialog() {
		uiBinder.createAndBindUi(this);
		dialogBox.center();
		dialogBox.hide();		
	}

	@UiField
	DialogBox dialogBox;

	@UiField
	InlineLabel name;
	@UiField
	InlineLabel school;

	@UiField InlineLabel inviteCode;
	@UiField InlineLabel inviteUrl;
	@UiField Button button;

	public void show(CourseProxy course) {
		name.setText( course.getName());
		inviteCode.setText( course.getInviteCode() );
		inviteUrl.setText( course.getInviteCode() );
		school.setText(course.getSchoolName());
		if (!dialogBox.isShowing()) {
			dialogBox.show();
		}
	}

	@UiHandler("button")
	void onButtonClick(ClickEvent event) {
		dialogBox.hide();
	}
}
