/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import ca.jhosek.main.shared.proxy.CourseProxy;

/**
 * student's your course view
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class StudentYourCoursePlace extends AuthenticatedPlace {

	@Prefix("studentyourcourse")
	public static class Tokenizer implements PlaceTokenizer<StudentYourCoursePlace> {

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		public StudentYourCoursePlace getPlace(String token) {
			// 
			return new StudentYourCoursePlace( token );
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt.place.shared.Place)
		 */
		public String getToken(StudentYourCoursePlace place) {
			// 
			return place.getCourseId();
		}
		
	}
	/**
	 * the Id of the Course 
	 */
	private String courseId;

	public StudentYourCoursePlace( String courseId ) {
		this.courseId = courseId;
	}

	public StudentYourCoursePlace( CourseProxy course ) {
		this.courseId = course.getId().toString();
	}

	public String getCourseId() {
		return courseId;
	}
}
