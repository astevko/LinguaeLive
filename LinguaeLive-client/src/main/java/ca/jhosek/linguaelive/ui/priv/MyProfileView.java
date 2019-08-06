/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.ui.priv;


import java.util.List;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

import ca.jhosek.main.client.activity.mainregion.MyProfileActivity;
import ca.jhosek.main.client.ui.priv.MyProfileViewImpl.Driver;
import ca.jhosek.main.shared.proxy.AppRequestFactory;
import ca.jhosek.main.shared.proxy.ContactInfoProxy;

/**
 *  My Profile user page 
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see MyProfileViewImpl
 * @see MyProfileActivity
 *
 */
public interface MyProfileView extends IsWidget {

	public void setPresenter(Presenter presenter);
//	public void setUser( UserProxy user );  // handled by editor framework
	public void resetView();
	public void acceptEdits();
	public void showContactInfo( List<ContactInfoProxy> contactInfos );
	
	public interface Presenter {
		void saveUserEdits();
		void addNewContactInfo();
		void editContactInfo(ContactInfoProxy contactInfo);
		AppRequestFactory getRequestFactory();
		void gotoQuickStart();
		void gotoChangePassword();		
	}
	
	public Driver createEditorDriver(EventBus eventBus,
			RequestFactory requestFactory);

}
