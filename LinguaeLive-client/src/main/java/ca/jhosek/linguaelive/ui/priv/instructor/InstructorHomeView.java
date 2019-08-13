/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.ui.priv.instructor;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;

import ca.jhosek.linguaelive.activity.mainregion.InstructorHomeActivity;
import ca.jhosek.linguaelive.place.InstructorHomePlace;
import ca.jhosek.linguaelive.proxy.CourseLinkProxy;
import ca.jhosek.linguaelive.proxy.CourseProxy;

/**
 *  Instructor home page 
 * 
 * @author copyright (C) 2011, 2014 Andrew Stevko
 * @see InstructorHomeActivity
 * @see InstructorHomePlace
 * @see InstructorHomeViewImpl
 * 
 */
public interface InstructorHomeView extends IsWidget {

	public void setPresenter(Presenter presenter);
	public void clear();

	/**
	 * @param courses list of courses
	 */
	public void showCourseList( List<CourseProxy> courses );
	
	/**
	 * @author andy
	 * @see InstructorHomeActivity
	 */
	public interface Presenter {

		void goToPendingCourseLinkView( CourseLinkProxy courseLink );
		void goToOpenCourseLinkView( CourseLinkProxy courseLink );
		/**
		 * view the course
		 * @param course
		 */
		void viewCourse( CourseProxy course );
		void goToAddCourse();
	}

	public void showPendingLinkedCourses(List<CourseLinkProxy> pending);
	public void showOpenLinkedCourses(List<CourseLinkProxy> open);
}
