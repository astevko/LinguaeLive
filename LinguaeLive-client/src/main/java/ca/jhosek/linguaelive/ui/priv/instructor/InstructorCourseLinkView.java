package ca.jhosek.linguaelive.ui.priv.instructor;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

import ca.jhosek.linguaelive.activity.mainregion.InstructorCourseLinkActivity;
import ca.jhosek.linguaelive.ui.priv.instructor.InstructorCourseLinkViewImpl.Driver;
import ca.jhosek.linguaelive.proxy.UserProxy;

/**
 * @author copyright (C) 2011 Andrew Stevko
 *
 * @see InstructorCourseLinkActivity
 * @see InstructorCourseLinkViewImpl
 */
public interface InstructorCourseLinkView extends IsWidget {

	public void setPresenter(Presenter presenter);

	/**
	 * clear the display
	 */
	public void clear();

	/**
	 * 
	 * @author copyright (C) 2011 Andrew Stevko
	 *
	 */
	public interface Presenter {
		/**
		 * accept or decline - respond to course link request
		 * 
		 * @param accept
		 */
		void respondToCourseLinkInvite( Boolean accept );

		/**
		 * user wants to navigate to Course A
		 */
		void goToCourseA();
		/**
		 * user wants to navigate to Course B
		 */
		void goToCourseB();

		/**
		 * @return the currently logged in user
		 */
		UserProxy getLoggedInUser();
	}
	
	public Driver 
		createEditorDriver(EventBus eventBus, RequestFactory requestFactory);

	/**
	 * displays/hides buttons
	 * 
	 * @param pending
	 * @param accepted
	 */
	void setViewMode( Boolean sender, Boolean pending, Boolean accepted);
}
