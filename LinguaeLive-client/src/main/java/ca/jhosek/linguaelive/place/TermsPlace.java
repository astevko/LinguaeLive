/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

/**
 * user is anonymous, show terms statement
 *   
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class TermsPlace extends Place {

	@Prefix("terms")
	public static class Tokenizer implements PlaceTokenizer<TermsPlace> {

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		public TermsPlace getPlace(String token) {
			// 
			return new TermsPlace();
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt.place.shared.Place)
		 */
		public String getToken(TermsPlace place) {
			// 
			return "";
		}
		
	}
}
