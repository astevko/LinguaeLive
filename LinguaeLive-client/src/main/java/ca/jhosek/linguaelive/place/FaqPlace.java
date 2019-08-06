/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

/**
 * user is anonymous, show faq page
 *   
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class FaqPlace extends Place {

	@Prefix("faq")
	public static class Tokenizer implements PlaceTokenizer<FaqPlace> {

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		
		public FaqPlace getPlace(String token) {
			// 
			return new FaqPlace();
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt.place.shared.Place)
		 */
		
		public String getToken(FaqPlace place) {
			// 
			return "";
		}
		
	}
}
