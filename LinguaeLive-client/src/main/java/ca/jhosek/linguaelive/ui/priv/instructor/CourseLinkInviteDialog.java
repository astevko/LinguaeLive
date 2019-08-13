package ca.jhosek.linguaelive.ui.priv.instructor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DateLabel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.NumberLabel;
import com.google.gwt.user.client.ui.TextArea;

import ca.jhosek.linguaelive.ui.priv.instructor.InstructorYourCourseView.Presenter;
import ca.jhosek.linguaelive.proxy.CourseProxy;
import ca.jhosek.linguaelive.proxy.UserProxy;

/**
 * display a dialogbox with a briefing of the course to send a link invite to
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see InstructorYourCourseViewImpl
 * @see CourseProxy
 */
public class CourseLinkInviteDialog {
	
	private static String INVITE_CAPTION = "Do you want to send an invitation to link your course with this course?";
	private static String INVITE_BUTTON = "Request to link courses";
	private static String CONTACT_CAPTION = "Contact the instructor of this expired course about collaboration in the future?";
	private static String CONTACT_BUTTON = "Contact about collaborating";


	
	private static CourseInviteDialogUiBinder uiBinder = GWT
	.create(CourseInviteDialogUiBinder.class);

	interface CourseInviteDialogUiBinder extends
	UiBinder<DialogBox, CourseLinkInviteDialog> {
	}

	public CourseLinkInviteDialog() {
		uiBinder.createAndBindUi(this);
		dialogBox.center();
		dialogBox.hide();		
	}

	@UiField
	DialogBox dialogBox;

	@UiField TextArea courseLinkMessage;
	@UiField Button linkCoursesButton;
	@UiField Button cancelButton;

	// link to course candidate info 
	@UiField InlineLabel instructor_fname;
	@UiField InlineLabel instructor_lname;
	@UiField InlineLabel instructor_school;
	@UiField InlineLabel name;
	@UiField InlineLabel description;
	@UiField NumberLabel<Long> estimatedMemberSize;
	@UiField DateLabel startDate;
	@UiField DateLabel endDate;


	private CourseProxy linkToCourse;

	private Presenter presenter;

	public void show(Presenter presenter, CourseProxy fromCourse, CourseProxy toCourse) {
		// save for later confirmation activity
		this.presenter = presenter;
		this.linkToCourse = toCourse;
		// set caption based on toCourse stop date
		
		if (fromCourse.getStartDate().before(toCourse.getEndDate()) 
				&& fromCourse.getEndDate().after(toCourse.getStartDate())) {
			// offer to link
			dialogBox.setText(INVITE_CAPTION);
			linkCoursesButton.setText(INVITE_BUTTON);
		} else {
			// offer to contact
			dialogBox.setText(CONTACT_CAPTION);
			linkCoursesButton.setText(CONTACT_BUTTON);
		}
		//---- fill in dialog box contents
		UserProxy owner = toCourse.getOwner();
		
		instructor_fname.setText( owner.getFirstName());
		instructor_lname.setText( owner.getLastName());
		instructor_school.setText( owner.getSchool());		
		name.setText( linkToCourse.getName());
		
		description.setText( toCourse.getDescription());
		estimatedMemberSize.setValue( toCourse.getEstimatedMemberSize());
		startDate.setValue( toCourse.getStartDate());
		endDate.setValue(toCourse.getEndDate());
		
		// show 
		dialogBox.show();
	}

	@UiHandler("cancelButton")
	void onButtonClick(ClickEvent event) {
		dialogBox.hide();
	}
	/**
	 * instructor wants to send invite
	 * @param event
	 */
	@UiHandler("linkCoursesButton")
	void onLinkCoursesButtonClick(ClickEvent event) {
		presenter.inviteToLinkCourses(linkToCourse, courseLinkMessage.getValue() );
		dialogBox.hide();
	}

	public void setCourseLinkMessage(String defaultMessage) {
		// set the default messaging 
		courseLinkMessage.setText(defaultMessage);
	}
}
