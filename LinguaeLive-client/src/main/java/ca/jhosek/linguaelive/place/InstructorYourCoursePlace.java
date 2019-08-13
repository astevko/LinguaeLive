/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import ca.jhosek.linguaelive.activity.mainregion.InstructorYourCourseActivity;
import ca.jhosek.linguaelive.ui.priv.instructor.InstructorYourCourseView;
import ca.jhosek.linguaelive.ui.priv.instructor.InstructorYourCourseViewImpl;
import ca.jhosek.linguaelive.proxy.CourseProxy;

/**
 * instructor's your course view
 * 
 * @see InstructorYourCoursePlace
 * @see InstructorYourCourseActivity
 * @see InstructorYourCourseView
 * @see InstructorYourCourseViewImpl
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see InstructorYourCourseActivity
 */
public class InstructorYourCoursePlace extends AuthenticatedPlace {

	@Prefix("instructoryourcourse")
	public static class Tokenizer implements PlaceTokenizer<InstructorYourCoursePlace> {

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		public InstructorYourCoursePlace getPlace(String token) {
			// 
			return new InstructorYourCoursePlace( token );
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt.place.shared.Place)
		 */
		public String getToken(InstructorYourCoursePlace place) {
			// 
			return place.getCourseId();
		}
		
	}
	/**
	 * the Id of the Course 
	 */
	private String courseId;

	public InstructorYourCoursePlace( String courseId ) {
		this.courseId = courseId;
	}

	public InstructorYourCoursePlace( CourseProxy course ) {
		this.courseId = course.getId().toString();
	}

	public String getCourseId() {
		return courseId;
	}
}
