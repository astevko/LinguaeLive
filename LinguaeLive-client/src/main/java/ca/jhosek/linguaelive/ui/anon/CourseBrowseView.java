/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.ui.anon;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;

import ca.jhosek.main.client.activity.mainregion.ContactUsActivity;
import ca.jhosek.main.shared.proxy.ContactUsProxy;
import ca.jhosek.main.shared.proxy.CourseProxy;

/**
 * Contact Us form view
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see ContactUsProxy
 * @see ContactUsActivity
 * @see ContactUsViewImpl
 * 
 */
public interface CourseBrowseView extends IsWidget {
	public interface Presenter {
		
//		void cancel();
//		void submit();
	}

	/**
	 * clears ui
	 */
	public void clear();

	public void setPresenter(Presenter presenter);

	void showCourseList(List<CourseProxy> courses);

}
