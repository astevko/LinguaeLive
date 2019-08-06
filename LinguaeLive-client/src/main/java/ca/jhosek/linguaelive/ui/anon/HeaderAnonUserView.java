/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.ui.anon;

import com.google.gwt.user.client.ui.IsWidget;

import ca.jhosek.main.client.activity.userregion.HeaderAnonUserActivity;

/**
 * anonymous user view has two actions available: login and change locale
 * 
 * @author copyright (C) 2011,2013 Andrew Stevko
 * @see HeaderAnonUserActivity
 * @see HeaderAnonUserViewImpl
 */
public interface HeaderAnonUserView extends IsWidget {
	  public void setPresenter(Presenter presenter);
	    
	  public interface Presenter {

		/**
		 * display the login panel
		 */
		void loginUser();
		/**
		 * select a language
		 */
		void changeLocale(String locale);
	  }

}
