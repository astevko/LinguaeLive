/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

/**
 * user has just joined , show start page
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class StudentStartPlace extends AuthenticatedPlace {

	@Prefix("studentstart")
	public static class Tokenizer implements PlaceTokenizer<StudentStartPlace> {

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		public StudentStartPlace getPlace(String token) {
			// 
			return new StudentStartPlace();
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt.place.shared.Place)
		 */
		public String getToken(StudentStartPlace place) {
			// 
			return "";
		}
		
	}
}
