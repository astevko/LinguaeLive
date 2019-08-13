/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import ca.jhosek.linguaelive.ui.priv.instructor.InstructorCourseSummaryReportView;
import ca.jhosek.linguaelive.proxy.CourseProxy;

/**
 * instructor's your course view
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see InstructorCourseSummaryReportView
 */
public class InstructorCourseSummaryReportPlace extends AuthenticatedPlace {

	@Prefix("instructorcoursesummaryreport")
	public static class Tokenizer implements PlaceTokenizer<InstructorCourseSummaryReportPlace> {

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		public InstructorCourseSummaryReportPlace getPlace(String token) {
			// 
			return new InstructorCourseSummaryReportPlace( token );
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt.place.shared.Place)
		 */
		public String getToken(InstructorCourseSummaryReportPlace place) {
			// 
			return place.getCourseId();
		}
		
	}
	/**
	 * the Id of the Course 
	 */
	private String courseId;

	public InstructorCourseSummaryReportPlace( String courseId ) {
		this.courseId = courseId;
	}

	public InstructorCourseSummaryReportPlace( CourseProxy course ) {
		this.courseId = course.getId().toString();
	}

	public String getCourseId() {
		return courseId;
	}
}
