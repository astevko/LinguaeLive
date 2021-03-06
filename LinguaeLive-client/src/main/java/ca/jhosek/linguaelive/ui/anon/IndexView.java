/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.ui.anon;

import com.google.gwt.user.client.ui.IsWidget;

import ca.jhosek.linguaelive.activity.mainregion.IndexActivity;
import ca.jhosek.linguaelive.place.IndexPlace;

/**
 *  Anon user home page 
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see IndexActivity
 * @see IndexPlace
 *
 */
public interface IndexView extends IsWidget {

	public void setPresenter(Presenter presenter);

	public interface Presenter {
		void goToLoginForm();
		void goToNewStudentForm();
		void goToNewInstructorForm();
	}
}
