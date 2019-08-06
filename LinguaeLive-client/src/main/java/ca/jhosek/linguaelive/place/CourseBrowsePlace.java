/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

/**
 * course browser 
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class CourseBrowsePlace extends Place {	
	
	@Prefix("courses")
	public static class Tokenizer implements PlaceTokenizer<CourseBrowsePlace> {

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		public CourseBrowsePlace getPlace(String token) {
			// 
			return new CourseBrowsePlace();
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt.place.shared.Place)
		 */
		public String getToken(CourseBrowsePlace place) {
			// 
			return "";
		}
		
	}
}
