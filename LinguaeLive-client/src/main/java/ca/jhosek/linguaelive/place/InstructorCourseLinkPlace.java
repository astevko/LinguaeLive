/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.place;

import java.util.logging.Logger;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import ca.jhosek.main.client.activity.mainregion.InstructorCourseLinkActivity;
import ca.jhosek.main.shared.proxy.CourseLinkProxy;
import ca.jhosek.main.shared.proxy.CourseProxy;

/**
 * instructor's course link view
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see InstructorCourseLinkActivity
 *
 */
public class InstructorCourseLinkPlace extends AuthenticatedPlace {

	private static final Logger logger = Logger.getLogger( InstructorCourseLinkPlace.class.getName() );
	
	@Prefix("instructorcourselink")
	public static class Tokenizer implements PlaceTokenizer<InstructorCourseLinkPlace> {

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		
		public InstructorCourseLinkPlace getPlace(String token) {
			if ( token.contains("-")) {
				// 
				String[] tokens = token.split("-");
				return new InstructorCourseLinkPlace( tokens[0], tokens[1] );
			}
			return null;
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt.place.shared.Place)
		 */
		public String getToken(InstructorCourseLinkPlace place) {
			// 
			return place.getCourseLinkId() + "-" + place.getCourseId();
		}

	}
	/**
	 * the Id of the CourseLink 
	 */
	private String courseLinkId;
	private String courseId;

	public InstructorCourseLinkPlace( String courseLinkId, String courseId ) {
		logger.info("new InstructorCourseLinkPlace( " + courseLinkId + ", " + courseId + " )");
		this.courseLinkId = courseLinkId;
		this.courseId = courseId;
	}

	public InstructorCourseLinkPlace( CourseLinkProxy courseLink, CourseProxy myCourse ) {
		logger.info("new InstructorCourseLinkPlace( " + courseLink.getId() + ", " + myCourse.getId() + " )");
		
		this.courseLinkId = courseLink.getId().toString();
		this.courseId     = myCourse.getId().toString();
	}

	public String getCourseLinkId() {
		return courseLinkId;
	}
	public String getCourseId() {
		return courseId;
	}
}
