/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import ca.jhosek.linguaelive.proxy.CourseProxy;

/**
 * instructor's  course report
 * 
 * @author copyright (C) 2011 Andrew Stevko
 */
public class InstructorCourseDetailReportPlace extends AuthenticatedPlace {

	@Prefix("instructorcoursedetailreport")
	public static class Tokenizer implements PlaceTokenizer<InstructorCourseDetailReportPlace> {

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		
		public InstructorCourseDetailReportPlace getPlace(String token) {
			// 
			return new InstructorCourseDetailReportPlace( token );
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt.place.shared.Place)
		 */
		
		public String getToken(InstructorCourseDetailReportPlace place) {
			// 
			return place.getCourseId();
		}
		
	}
	/**
	 * the Id of the Course 
	 */
	private String courseId;

	public InstructorCourseDetailReportPlace( String courseId ) {
		this.courseId = courseId;
	}

	public InstructorCourseDetailReportPlace( CourseProxy course ) {
		this.courseId = course.getId().toString();
	}

	public String getCourseId() {
		return courseId;
	}
}
