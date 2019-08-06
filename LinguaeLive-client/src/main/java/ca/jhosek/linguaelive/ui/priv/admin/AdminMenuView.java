/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.ui.priv.admin;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * Admin Menu 
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public interface AdminMenuView extends IsWidget {

	public interface Presenter {
		void goToAdminHome();
		void goToQueryUsers();
		void goToQueryCourses();
	}		
	public void setPresenter(Presenter presenter);
}
