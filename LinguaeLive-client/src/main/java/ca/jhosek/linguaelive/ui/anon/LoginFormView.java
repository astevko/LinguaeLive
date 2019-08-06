/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.ui.anon;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

import ca.jhosek.main.client.activity.mainregion.LoginFormActivity;

/**
 * login form view
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see LoginFormActivity
 * @see LoginFormViewImpl
 */
public interface LoginFormView extends IsWidget {
	public interface Presenter {
		
		void cancel();
		void forgotPassword();
		void loginUser(String emailAddress, String pswd, boolean remember );
		void loginViaFacebook();
	}

	/**
	 * clears form
	 */
	public void clear(String defaultEmailAddress);

	/**
	 * @param presenter callback to Presenter
	 */
	public void setPresenter(Presenter presenter);

	Widget asWidget();
}
