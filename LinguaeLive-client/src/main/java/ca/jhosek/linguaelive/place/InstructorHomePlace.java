/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import ca.jhosek.main.client.activity.mainregion.InstructorHomeActivity;

/**
 * user is logged in, show home page
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see InstructorHomeActivity
 */
public class InstructorHomePlace extends AuthenticatedPlace {

	@Prefix("instructorhome")
	public static class Tokenizer implements PlaceTokenizer<InstructorHomePlace> {

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		public InstructorHomePlace getPlace(String token) {
			// 
			return new InstructorHomePlace();
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt.place.shared.Place)
		 */
		public String getToken(InstructorHomePlace place) {
			// 
			return "";
		}
		
	}
}
