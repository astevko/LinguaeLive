/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

/**
 * show contact us form
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class ContactUsPlace extends Place {	
	
	@Prefix("contactus")
	public static class Tokenizer implements PlaceTokenizer<ContactUsPlace> {

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		public ContactUsPlace getPlace(String token) {
			// 
			return new ContactUsPlace();
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt.place.shared.Place)
		 */
		public String getToken(ContactUsPlace place) {
			// 
			return "";
		}
		
	}
}
