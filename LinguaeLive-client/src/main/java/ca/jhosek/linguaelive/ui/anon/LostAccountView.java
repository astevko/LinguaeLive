/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.ui.anon;

import com.google.gwt.user.client.ui.IsWidget;

import ca.jhosek.main.client.activity.mainregion.LostAccountActivity;

/**
 * lost password view
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see LostAccountViewImpl
 * @see LostAccountActivity
 * 
 */
public interface LostAccountView extends IsWidget {

	public interface Presenter {
		
		void cancel();
		void recoverPassword( String emailAddress );
	}

	/**
	 * clears ui
	 */
	public void clear();


	public void setPresenter(Presenter presenter);
}
