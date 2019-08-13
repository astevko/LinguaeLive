/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.ui.priv.admin;

import java.util.List;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

import ca.jhosek.linguaelive.activity.mainregion.QueryUsersActivity;
import ca.jhosek.linguaelive.ui.priv.admin.AdminUserViewerImpl.Driver;
import ca.jhosek.linguaelive.proxy.AppRequestFactory;
import ca.jhosek.linguaelive.proxy.ContactInfoProxy;
import ca.jhosek.linguaelive.proxy.UserProxy;

/**
 * Admin Menu 
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see QueryUsersActivity.java
 */
public interface AdminUserView extends IsWidget {

	public interface Presenter {
		void saveUserEdits();
		void resetUser();
//		void addContactInfo();
		AppRequestFactory getRequestFactory();
		void gotoChangePassword();
		void gotoViewOwnedCourses(UserProxy user);		
	}		
	public void setPresenter(Presenter presenter);
	public void resetView();
	public void acceptEdits();
//	public void refreshContactInfos();
	public Driver createEditorDriver(EventBus eventBus,
			RequestFactory requestFactory);
	void showContactInfo(List<ContactInfoProxy> contactInfos);

}
