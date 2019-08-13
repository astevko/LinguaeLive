/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.ui.anon;

import com.google.gwt.user.client.ui.IsWidget;

import ca.jhosek.linguaelive.activity.mainregion.RecoverPasswordActivity;
import ca.jhosek.linguaelive.place.RecoverPasswordPlace;
import ca.jhosek.linguaelive.ui.ViewModule;

/**
 * Recover Passwords by sending a hash + url to the user's email. Accept the
 * email + hash @ the url to open a reset password & hint form Will overwrite w/
 * new password and hint *
 * 
 * @author copyright (C) 2012 Andrew Stevko
 * 
 * @see ViewModule registered here
 * @see RecoverPasswordView
 * @see RecoverPasswordViewImpl
 * @see RecoverPasswordPlace
 * @see RecoverPasswordActivity
 * 
 */
public interface RecoverPasswordView extends IsWidget {

	public interface Presenter {

		void cancel();

		/**
		 * Compare user's hash with hashed password for email. Identifies the
		 * user attempting to reset Triggers call to change password
		 * 
		 * @param email
		 * @param userHash
		 * @return TRUE to pass
		 */
		void checkOkEmailHash(String email, String hash);
	}

	/**
	 * clears ui
	 */
	public void clear();

	public void setPresenter(Presenter presenter);

}
