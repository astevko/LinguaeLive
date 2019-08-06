/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.ui.priv;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

import ca.jhosek.main.client.activity.mainregion.AddEditContactInfoActivity;
import ca.jhosek.main.client.ui.priv.AddEditContactInfoViewImpl.Driver;

/**
 * Add Edit ContactInfo View 
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see AddEditContactInfoViewImpl
 * @see AddEditContactInfoActivity
 * 
 */
public interface AddEditContactInfoView extends IsWidget {

	public interface Presenter {

		// save and return to MyProfile
		void save();
//		// save and add new
//		void saveMore();
		void cancel();
	}
	
	public void setPresenter(Presenter presenter);
	public Driver createEditorDriver(EventBus eventBus,
			RequestFactory requestFactory);

}
