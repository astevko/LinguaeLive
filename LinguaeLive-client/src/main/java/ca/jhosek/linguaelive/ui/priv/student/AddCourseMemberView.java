/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.ui.priv.student;

import com.google.gwt.user.client.ui.IsWidget;

import ca.jhosek.linguaelive.activity.mainregion.AddCourseMemberActivity;
import ca.jhosek.linguaelive.place.AddCourseMemberPlace;

/**
 * Add A Course Member View 
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see AddCourseMemberPlace
 * @see AddCourseMemberViewImpl
 * @see AddCourseMemberActivity
 */
public interface AddCourseMemberView extends IsWidget {

	public interface Presenter {
		void saveCourseMember( String inviteCode );
		void cancel();	// goes to currentUser home page
	}		
	public void setPresenter(Presenter presenter);

}
