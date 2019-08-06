/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.ui.anon;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

import ca.jhosek.main.client.activity.mainregion.ContactUsActivity;
import ca.jhosek.main.client.ui.anon.ContactUsViewImpl.Driver;
import ca.jhosek.main.shared.proxy.ContactUsProxy;

/**
 * Contact Us form view
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see ContactUsProxy
 * @see ContactUsActivity
 * @see ContactUsViewImpl
 * 
 */
public interface ContactUsView extends IsWidget {
	public interface Presenter {
		
		void cancel();
		void submit();
	}

	/**
	 * clears ui
	 */
	public void clear();

	public void setPresenter(Presenter presenter);

	public Driver createEditorDriver(EventBus eventBus,
			RequestFactory requestFactory);

}
