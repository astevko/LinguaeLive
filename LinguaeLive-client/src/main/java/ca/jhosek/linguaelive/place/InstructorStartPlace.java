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
public class InstructorStartPlace extends AuthenticatedPlace {

	@Prefix("instructorstart")
	public static class Tokenizer implements PlaceTokenizer<InstructorStartPlace> {

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		public InstructorStartPlace getPlace(String token) {
			// 
			return new InstructorStartPlace();
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt.place.shared.Place)
		 */
		public String getToken(InstructorStartPlace place) {
			// 
			return "";
		}
		
	}
}
