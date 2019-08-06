/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.ui.anon;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public interface AnonMenuView extends IsWidget {

	public interface Presenter {
		void goToIndex();
		void goToWhatIs();
		void goToFaq();
		void goToInstructorInfo();
		void goToStudentInfo();
		void goToWhoIs();
	}		
	public void setPresenter(Presenter presenter);
}
