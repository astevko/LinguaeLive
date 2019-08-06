/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

/**
 * user is anonymous, show what is page
 *   
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class WhatIsPlace extends Place {

	@Prefix("whatis")
	public static class Tokenizer implements PlaceTokenizer<WhatIsPlace> {

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		public WhatIsPlace getPlace(String token) {
			// 
			return new WhatIsPlace();
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt.place.shared.Place)
		 */
		public String getToken(WhatIsPlace place) {
			// 
			return "";
		}
		
	}
}
