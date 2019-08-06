/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import ca.jhosek.main.client.activity.mainregion.AddEditCourseActivity;
import ca.jhosek.main.client.ui.priv.instructor.AddEditCourseView;
import ca.jhosek.main.client.ui.priv.instructor.AddEditCourseViewImpl;
import ca.jhosek.main.shared.proxy.CourseProxy;

/**
 * instructor's 
 * add/edit a course page 
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see AddEditCourseActivity
 * @see AddEditCourseView
 * @see AddEditCourseViewImpl
 * 
 */
 
public class AddEditCoursePlace extends AuthenticatedPlace {

	@Prefix("addeditcourse")
	public static class Tokenizer implements PlaceTokenizer<AddEditCoursePlace> {

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		public AddEditCoursePlace getPlace(String token) {
			// 
			return new AddEditCoursePlace(token);
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt.place.shared.Place)
		 */
		public String getToken(AddEditCoursePlace place) {
			// 
			return place.getCourseId();
		}
	}
	
	private final String courseId;

	public String getCourseId() {
		return courseId;
	}

	/**
	 * @param courseId
	 */
	public AddEditCoursePlace(String courseId) {
		this.courseId = courseId;
	}
	
	/**
	 * @param courseId
	 */
	public AddEditCoursePlace(CourseProxy course) {
		this.courseId = course.getId().toString();
	}
	
	
}
