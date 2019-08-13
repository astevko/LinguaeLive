/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.ui.priv.instructor;

import com.google.gwt.user.client.ui.IsWidget;

import ca.jhosek.linguaelive.proxy.UserProxy;

/**
 *  Instructor user home page 
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public interface InstructorTipsView extends IsWidget {

	public void setPresenter(Presenter presenter);
	public void setUser( UserProxy user );
	public void clear();

	public interface Presenter {

	}
}
