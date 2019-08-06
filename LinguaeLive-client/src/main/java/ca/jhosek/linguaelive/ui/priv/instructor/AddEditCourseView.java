/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.ui.priv.instructor;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

import ca.jhosek.main.client.activity.mainregion.AddEditCourseActivity;
import ca.jhosek.main.client.place.AddEditCoursePlace;
import ca.jhosek.main.client.ui.priv.instructor.AddEditCourseViewImpl.Driver;

/**
 * Add A Course View 
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see AddEditCoursePlace
 * @see AddEditCourseViewImpl
 * @see AddEditCourseActivity
 */
public interface AddEditCourseView extends IsWidget {

	public interface Presenter {
		void save();
		void cancel();	// goes to currentUser home page
	}		
	public void setPresenter(Presenter presenter);
//	public void refreshContactInfos();
	public Driver createEditorDriver(EventBus eventBus,
			RequestFactory requestFactory);
	void fixupDisplay();

}
