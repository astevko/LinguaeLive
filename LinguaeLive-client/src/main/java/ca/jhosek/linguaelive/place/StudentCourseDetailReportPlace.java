/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import ca.jhosek.main.client.activity.mainregion.StudentCourseDetailReportActivity;
import ca.jhosek.main.client.ui.priv.student.StudentCourseDetailReportView;
import ca.jhosek.main.client.ui.priv.student.StudentCourseDetailReportViewImpl;
import ca.jhosek.main.shared.proxy.MemberProxy;

/**
 * student course detail report 
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see StudentCourseDetailReportActivity
 * @see StudentCourseDetailReportView
 * @see StudentCourseDetailReportViewImpl
 */
public class StudentCourseDetailReportPlace extends AuthenticatedPlace {

	@Prefix("studentcoursedetailreport")
	public static class Tokenizer implements PlaceTokenizer<StudentCourseDetailReportPlace> {

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		public StudentCourseDetailReportPlace getPlace(String token) {
			// 
			return new StudentCourseDetailReportPlace( token );
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt.place.shared.Place)
		 */
		public String getToken(StudentCourseDetailReportPlace place) {
			// 
			return place.getMemberId();
		}
		
	}
	/**
	 * the Id of the Course 
	 */
	private String memberId;

	public StudentCourseDetailReportPlace( String memberId ) {
		this.memberId = memberId;
	}

	public StudentCourseDetailReportPlace( MemberProxy member ) {
		this.memberId = member.getId().toString();
	}

	public String getMemberId() {
		return memberId;
	}
}
