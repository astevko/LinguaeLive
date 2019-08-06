/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.ui.priv.admin;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;

import ca.jhosek.main.shared.LanguageType;
import ca.jhosek.main.shared.proxy.CourseProxy;

/**
 *  Admin query classes view 
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */

public interface QueryCoursesView extends IsWidget {

	public static final String NO_SELECTION = "";
	public static final String HAS_COURSE_LINKS = "has";
	public static final String NO_COURSE_LINKS = "not";
	public static final String CURRENT_COURSES = "current";
	public static final String PAST_COURSES = "past";
	public static final int QUERY_PAGE_SIZE = 15;

	public void setPresenter(Presenter presenter);
	
	/**
	 * clear query results list & panel
	 */
	public void clear();
	/**
	 * display no query results
	 */
	public void emptyQueryResults();
	/**
	 * @param users
	 */
	public void showCourseList( List<CourseProxy> courses );
	
	/**
	 * @return null or target Language to filter on
	 */
	public LanguageType getTargetLanguage();
	/**
	 * @return null or expert Language to filter on
	 */
	public LanguageType getExpertLanguage();
	/**
	 * @return null or True = with course links or False = without course links
	 */
	public Boolean getWithCourseLinks();
	/**
	 * @return null or True = current course or False = past course
	 */
	public Boolean getCurrentCourses();
	
	public interface Presenter {
		public void queryCourses();
		public void selectCourse(CourseProxy course);
	}

}
