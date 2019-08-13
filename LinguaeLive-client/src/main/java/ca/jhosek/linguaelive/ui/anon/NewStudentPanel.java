/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.ui.anon;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.IsWidget;

import ca.jhosek.linguaelive.activity.mainregion.NewStudentFormActivity;
import ca.jhosek.linguaelive.place.NewStudentPlace;
// import ca.jhosek.linguaelive.email.NewStudentEmail;
import ca.jhosek.linguaelive.proxy.UserProxy;

/**
 * New Student Sign Up
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 * @see NewStudentPanelImpl
 * @see NewStudentFormActivity
 * @see NewStudentPlace
 * @see NewStudentEmail
 * 
 */
public interface NewStudentPanel extends IsWidget,  Editor<UserProxy>{
	
	/**
	 * @param presenter
	 */
	public void setPresenter(Presenter presenter);
	
	/**
	 * displays/hides user form (initially hidden)
	 * @param showUserForm
	 */
	public void setUserFormVisibility( boolean showUserForm );
	/**
	 * displays/hides invite code form (initially visible)
	 * @param showInviteCodeForm
	 */
	public void setInviteCodeFormVisibility( boolean showInviteCodeForm );
	
	public interface Presenter {
		/**
		 * checks invite code entered by user;
		 * @param inviteCode
		 */
		void acceptInviteCode( String inviteCode );
		
		void createNewStudent( );
		void cancel();
	}

	public void setInviteCode(String courseInviteCode);
}
