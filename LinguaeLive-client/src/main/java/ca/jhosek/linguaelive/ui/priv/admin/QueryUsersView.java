/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.ui.priv.admin;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;

import ca.jhosek.linguaelive.activity.mainregion.QueryUsersActivity;
import ca.jhosek.linguaelive.place.QueryUsersPlace;
import ca.jhosek.linguaelive.proxy.ContactInfoProxy;
import ca.jhosek.linguaelive.proxy.UserProxy;

/**
 *  Admin query users view
 *  
 * @see QueryUsersView
 * @see QueryUsersViewImpl
 * @see QueryUsersPlace
 * @see QueryUsersActivity   
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public interface QueryUsersView extends IsWidget {

	public void setPresenter(Presenter presenter);
	
	/**
	 * clear query results list & panel
	 */
	public void clear();
	/**
	 * display no query results
	 */
	public void emptyQueryResults();
	/**
	 * @param users
	 */
	public void showUserList( List<UserProxy> users );
	
	/**
	 * @return user entered email address
	 */
	public String getEmailAddressFilter();
	
	public AdminUserView getUserProfilePanel();
	
	public interface Presenter {
		public void queryUsers();
		public void showUser( UserProxy user );
		public void gotoViewOwnedCourses( final UserProxy ownedBy);
	}

	public void showContactInfo(List<ContactInfoProxy> response);

}
