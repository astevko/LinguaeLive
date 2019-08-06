/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.ui.priv.instructor;

import com.google.gwt.user.client.ui.IsWidget;

import ca.jhosek.main.client.activity.menuregion.InstructorMenuActivity;

/**
 * InstructorMenu 
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see InstructorMenuActivity
 */
public interface InstructorMenuView extends IsWidget {

	public interface Presenter {
		void goToInstructorHome();
		void goToInstructorTips();
		void goToInstructorStart();
		void goToMyProfile();
		void goToMyClasses();
	}		
	public void setPresenter(Presenter presenter);
}
