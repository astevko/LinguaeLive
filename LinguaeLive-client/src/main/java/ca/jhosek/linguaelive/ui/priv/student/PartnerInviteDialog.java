package ca.jhosek.linguaelive.ui.priv.student;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.inject.Inject;

import ca.jhosek.linguaelive.LLConstants;
import ca.jhosek.linguaelive.ui.priv.student.StudentYourCourseView.Presenter;
import ca.jhosek.linguaelive.proxy.MemberProxy;
import ca.jhosek.linguaelive.proxy.PartnerInviteProxy;
import ca.jhosek.linguaelive.proxy.UserProxy;

/**
 * display a dialog box with a briefing of the member to send a session invite to
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see StudentYourCourseView
 * @see MemberProxy
 * @see PartnerInviteProxy
 */
public class PartnerInviteDialog {

	private static PartnerInviteDialogUiBinder uiBinder = GWT
	.create(PartnerInviteDialogUiBinder.class);

	interface PartnerInviteDialogUiBinder extends
	UiBinder<DialogBox, PartnerInviteDialog> {
	}

	@Inject
	public PartnerInviteDialog(LLConstants constants) {
		uiBinder.createAndBindUi(this);
		dialogBox.center();
		dialogBox.hide();
		
//		final String dateTimeDateFormatterPattern = "MMMM dd, yyyy HH:mm zzzz";
//		DateTimeFormat format = DateTimeFormat.getFormat( constants.dateTimeDateFormatterPattern() );
//		startDate.setFormat( new DateBox.DefaultFormat( format ) );
	}

	@UiField
	DialogBox dialogBox;

		// link to course candidate info 
	@UiField InlineLabel invitee_fname;
	@UiField InlineLabel invitee_lname;
	@UiField InlineLabel invitee_school;
//	@UiField InlineLabel invitee_timezone;
	
	@UiField TextArea personalMessage;

	@UiField Button sendSessionInviteButton;
	@UiField Button cancelButton;

//	@UiField DateBox startDate;

	private MemberProxy inviteeMember;

	private Presenter presenter;

	public void show(Presenter presenter, MemberProxy inviteeMember) {
		// save for later confirmation activity
		this.presenter = presenter;
		this.inviteeMember = inviteeMember;
		
		//---- fill in dialog box contents
		UserProxy inviteeUser = inviteeMember.getUser();
		invitee_fname.setText( inviteeUser.getFirstName());
		invitee_lname.setText( inviteeUser.getLastName());
		invitee_school.setText( inviteeUser.getSchool());
//		invitee_timezone.setText( inviteeUser.getUserTimeZone());

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
	@UiHandler("sendSessionInviteButton")
	void onSendSessionInviteButtonClick(ClickEvent event) {
		presenter.inviteToPairCourses( inviteeMember, personalMessage.getText() );
		dialogBox.hide();
	}

	public void setPersonalMessage(String defaultMessage) {
		// set the default messaging 
		personalMessage.setText(defaultMessage);
	}
}
