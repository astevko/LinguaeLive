/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.ui.priv;


import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

import ca.jhosek.main.client.activity.mainregion.ChangePswdActivity;
import ca.jhosek.main.client.ui.priv.ChangePswdViewImpl.Driver;

/**
 *  ChangePswdView user page 
 * 
 * @author copyright (C) 2011, 2012 Andrew Stevko
 * @see ChangePswdViewImpl
 * @see ChangePswdActivity
 *
 */
public interface ChangePswdView extends IsWidget {

	public void setPresenter(Presenter presenter);
//	public void setUser( UserProxy user );  // handled by editor framework
	
	public interface Presenter {
		void saveUserEdits();
		void gotoHomePlace();		
	}
	
	public Driver createEditorDriver(EventBus eventBus,
			RequestFactory requestFactory);

	String getNewPassword();
	void resetFocus();

}
