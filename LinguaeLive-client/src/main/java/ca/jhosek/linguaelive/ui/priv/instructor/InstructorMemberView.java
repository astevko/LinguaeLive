package ca.jhosek.linguaelive.ui.priv.instructor;

import java.util.List;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

import ca.jhosek.linguaelive.activity.mainregion.InstructorMemberActivity;
import ca.jhosek.linguaelive.ui.priv.instructor.InstructorMemberViewImpl.Driver;
import ca.jhosek.linguaelive.ui.priv.student.HourOfDay;
import ca.jhosek.linguaelive.proxy.AppRequestFactory;
import ca.jhosek.linguaelive.proxy.ContactInfoProxy;
import ca.jhosek.linguaelive.proxy.MemberProxy;
import ca.jhosek.linguaelive.proxy.PartnerInviteProxy;
import ca.jhosek.linguaelive.proxy.SessionProxy;

/**
 * displays a Member as viewed by a Instructor
 * shows availability
 * shows potentially complementary students
 * shows Paired students
 * shows Sessions
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 * @see InstructorMemberView
 * @see InstructorMemberViewImpl
 * @see InstructorMemberActivity
 * 
 */
public interface InstructorMemberView extends IsWidget {

	public void setPresenter(Presenter presenter);

	public void showAvailableStudents( List<MemberProxy> availableStudents );
	public void showPartnerInvites(    List<PartnerInviteProxy> sessionInvites);
	public void showSessions( 		   List<SessionProxy> sessions );
	
	/**
	 * clear the display
	 */
	public void clear();

	/**
	 * What can the student do from viewing a course?
	 * 
	 * edit availability
	 * view available students
	 * view paired students
	 * view sessions
	 * 
	 * @author copyright (C) 2011 Andrew Stevko
	 *
	 */
	public interface Presenter {

		void inviteToPairCourses( MemberProxy toStudent, String personalMessage );
		MemberProxy getMember();

		/**
		 * when user selects a session invite
		 * @param sessionInvite
		 */
		void goToPartnerInvite(PartnerInviteProxy sessionInvite);
		/**
		 * when user selects a session
		 * @param session
		 */
		void goToSession(SessionProxy session);
		
		void refreshAvailableStudents();
		AppRequestFactory getRequestFactory();
		void goToCourse();
	}
	
	public Driver createEditorDriver(EventBus eventBus,
			RequestFactory requestFactory);

	void showSchedule(List<HourOfDay> scheduleHours);

	public void showContactInfo(List<ContactInfoProxy> response);
}
