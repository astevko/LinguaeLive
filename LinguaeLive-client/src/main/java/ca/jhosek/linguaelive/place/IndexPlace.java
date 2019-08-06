/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

/**
 * user is anonymous, show index  
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class IndexPlace extends Place {

	@Prefix("index")
	public static class Tokenizer implements PlaceTokenizer<IndexPlace> {

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		
		public IndexPlace getPlace(String token) {
			// 
			return new IndexPlace();
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt.place.shared.Place)
		 */
		
		public String getToken(IndexPlace place) {
			// 
			return "";
		}
		
	}
}
