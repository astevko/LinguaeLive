/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

/**
 * students's 
 * add a course member page 
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
 
public class AddCourseMemberPlace extends AuthenticatedPlace {

	@Prefix("addmember")
	public static class Tokenizer implements PlaceTokenizer<AddCourseMemberPlace> {

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		public AddCourseMemberPlace getPlace(String token) {
			// 
			return new AddCourseMemberPlace();
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt.place.shared.Place)
		 */
		public String getToken(AddCourseMemberPlace place) {
			// 
			return "";
		}
	}	
}
