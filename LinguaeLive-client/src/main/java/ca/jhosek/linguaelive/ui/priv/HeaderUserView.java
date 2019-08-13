/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.ui.priv;

import com.google.gwt.user.client.ui.IsWidget;

import ca.jhosek.linguaelive.proxy.UserProxy;

/**
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public interface HeaderUserView extends IsWidget {
	  public void setPresenter(Presenter presenter);
	  
	  void setUser( UserProxy user );
	  
	  public interface Presenter {
	    public void logoutUser();
	  }

	}
