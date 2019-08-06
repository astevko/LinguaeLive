/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import ca.jhosek.main.shared.proxy.CourseProxy;

/**
 * instructor's search for complementary courses
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
@Deprecated
public class InstructorSearchCoursePlace extends AuthenticatedPlace {

	@Prefix("instructorsearchcourse")
	public static class Tokenizer implements PlaceTokenizer<InstructorSearchCoursePlace> {

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		public InstructorSearchCoursePlace getPlace(String token) {
			// 
			return new InstructorSearchCoursePlace( token );
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt.place.shared.Place)
		 */
		public String getToken(InstructorSearchCoursePlace place) {
			// 
			return place.getCourseId();
		}
		
	}
	/**
	 * the Id of the Course 
	 */
	private String courseId;

	public InstructorSearchCoursePlace( String courseId ) {
		this.courseId = courseId;
	}

	public InstructorSearchCoursePlace( CourseProxy course ) {
		this.courseId = course.getId().toString();
	}

	public String getCourseId() {
		return courseId;
	}
}
