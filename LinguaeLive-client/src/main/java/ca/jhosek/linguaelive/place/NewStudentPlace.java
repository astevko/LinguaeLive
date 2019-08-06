/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import ca.jhosek.main.client.activity.mainregion.NewStudentFormActivity;
import ca.jhosek.main.client.ui.anon.NewStudentPanel;
import ca.jhosek.main.client.ui.anon.NewStudentPanelImpl;

/**
 * sign up new students form
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see NewStudentFormActivity
 * @see NewStudentPanelImpl
 * @see NewStudentPanel
 * 
 */
public class NewStudentPlace extends Place {	
	
	@Prefix("signupstudent")
	public static class Tokenizer implements PlaceTokenizer<NewStudentPlace> {

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		public NewStudentPlace getPlace(String token) {
			// 
			return new NewStudentPlace( token );
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt.place.shared.Place)
		 */
		public String getToken(NewStudentPlace place) {
			// 
			return place.getCourseInviteCode();
		}
		
	}
	
	private final String courseInviteCode;

	public NewStudentPlace( String courseInviteCode ) {
		this.courseInviteCode = courseInviteCode;
	}
	
	public String getCourseInviteCode() {
		return courseInviteCode;
	}
	
}
