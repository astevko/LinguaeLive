package ca.jhosek.main.client.ui.priv.student;

import java.util.List;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

import ca.jhosek.main.client.activity.mainregion.StudentYourCourseActivity;
import ca.jhosek.main.client.ui.priv.student.StudentYourCourseViewImpl.Driver;
import ca.jhosek.main.shared.proxy.MemberProxy;
import ca.jhosek.main.shared.proxy.PartnerInviteProxy;
import ca.jhosek.main.shared.proxy.SessionProxy;

/**
 * displays a course as viewed by a Student
 * shows availability
 * shows potentially complementary students
 * shows Paired students
 * shows Sessions
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 * @see StudentYourCourseView
 * @see StudentYourCourseViewImpl
 * @see StudentYourCourseActivity
 * 
 */
public interface StudentYourCourseView extends IsWidget {
	
	
	public void setPresenter(Presenter presenter);

	public void showAvailableStudents( List<MemberProxy> availableStudents );
	public void showSessionInvites(    List<PartnerInviteProxy> sessionInvites);
//	public void showSessions( 		   List<SessionProxy> sessions );
	
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
		void goToSessionInvite(PartnerInviteProxy sessionInvite);
		/**
		 * when user selects a session
		 * @param session
		 */
		void goToSession(SessionProxy session);
		
		void addSchedule( Integer hourOfWeek );
		void dropSchedule( Integer hourOfWeek );
		/**
		 * change the available field in member
		 * @param val
		 */
		void setAvailable(Boolean val);
		
		void refreshAvailableStudents();
		void gotoSessionReport();

	
		// admin and owner only 
		void deleteMember();
	}
	
	public Driver createEditorDriver(EventBus eventBus,
			RequestFactory requestFactory);

	void showSchedule(List<HourOfDay> scheduleHours);
}
