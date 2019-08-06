/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

/**
 * admin user is logged in, show home page
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
 
public class AdminHomePlace extends AuthenticatedPlace {

	@Prefix("adminhome")
	public static class Tokenizer implements PlaceTokenizer<AdminHomePlace> {

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		public AdminHomePlace getPlace(String token) {
			// 
			return new AdminHomePlace();
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt.place.shared.Place)
		 */
		public String getToken(AdminHomePlace place) {
			// 
			return "";
		}
		
	}
}
