/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import ca.jhosek.main.shared.proxy.CourseProxy;

/**
 * instructor's invite students to course view
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class InstructorInviteCoursePlace extends AuthenticatedPlace {

	@Prefix("instructorinvitecourse")
	public static class Tokenizer implements PlaceTokenizer<InstructorInviteCoursePlace> {

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		public InstructorInviteCoursePlace getPlace(String token) {
			// 
			return new InstructorInviteCoursePlace( token );
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt.place.shared.Place)
		 */
		public String getToken(InstructorInviteCoursePlace place) {
			// 
			return place.getCourseId();
		}
		
	}
	/**
	 * the Id of the Course 
	 */
	private String courseId;

	public InstructorInviteCoursePlace( String courseId ) {
		this.courseId = courseId;
	}

	public InstructorInviteCoursePlace( CourseProxy course ) {
		this.courseId = course.getId().toString();
	}

	public String getCourseId() {
		return courseId;
	}
}
