/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

/**
 * user is anonymous, show privacy statement
 *   
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class PrivacyPlace extends Place {

	@Prefix("privacy")
	public static class Tokenizer implements PlaceTokenizer<PrivacyPlace> {

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		public PrivacyPlace getPlace(String token) {
			// 
			return new PrivacyPlace();
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt.place.shared.Place)
		 */
		public String getToken(PrivacyPlace place) {
			// 
			return "";
		}
		
	}
}
