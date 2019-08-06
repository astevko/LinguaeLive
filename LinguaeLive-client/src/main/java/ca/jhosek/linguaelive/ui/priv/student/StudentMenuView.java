/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.ui.priv.student;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * Student Menu 
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public interface StudentMenuView extends IsWidget {

	public interface Presenter {
		void goToStudentHome();
		void goToStudentStart();
		void goToMyProfile();
		void goToMyClasses();
	}		
	public void setPresenter(Presenter presenter);
}
