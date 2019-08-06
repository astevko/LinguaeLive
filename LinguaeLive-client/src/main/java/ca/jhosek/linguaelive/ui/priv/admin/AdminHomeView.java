/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.ui.priv.admin;

import com.google.gwt.user.client.ui.IsWidget;

import ca.jhosek.main.shared.proxy.UserProxy;

/**
 *  Admin user home page 
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public interface AdminHomeView extends IsWidget {

	public void setPresenter(Presenter presenter);
	public void setUser( UserProxy user );
	public void clear();

	public interface Presenter {

	}
}
