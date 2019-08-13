/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.ui.priv.student;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;

import ca.jhosek.linguaelive.activity.mainregion.StudentHomeActivity;
import ca.jhosek.linguaelive.place.StudentHomePlace;
import ca.jhosek.linguaelive.proxy.CourseProxy;
import ca.jhosek.linguaelive.proxy.PartnerInviteProxy;
import ca.jhosek.linguaelive.proxy.SessionProxy;
import ca.jhosek.linguaelive.proxy.UserProxy;

/**
 *  Student home page 
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see StudentHomeViewImpl
 * @see StudentHomeActivity
 * @see StudentHomePlace
 *
 */
public interface StudentHomeView extends IsWidget {

	public void setPresenter(Presenter presenter);
	public void clear();

	public void showSessionInvites(    List<PartnerInviteProxy> sessionInvites);
	public void showSessions( 		   List<SessionProxy> sessions );
	
	public interface Presenter {

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

		/**
		 * @return the currently logged in user
		 */
		UserProxy getLoggedInUser();
		
		/**
		 * view the course
		 * @param course
		 */
		void viewCourse( CourseProxy course );
		void goToAddCourse();		
	}

	void showCourseList(List<CourseProxy> courses);
}
