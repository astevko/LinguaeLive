/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

/**
 * user is logged in, show home page
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class StudentHomePlace extends AuthenticatedPlace {

	@Prefix("studenthome")
	public static class Tokenizer implements PlaceTokenizer<StudentHomePlace> {

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		public StudentHomePlace getPlace(String token) {
			// 
			return new StudentHomePlace();
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt.place.shared.Place)
		 */
		public String getToken(StudentHomePlace place) {
			// 
			return "";
		}
		
	}
}
