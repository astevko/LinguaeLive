/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

/**
 * user is anonymous, show instructor infomation page
 *   
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class InstructorInfoPlace extends Place {

	@Prefix("instructorinfo")
	public static class Tokenizer implements PlaceTokenizer<InstructorInfoPlace> {

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		public InstructorInfoPlace getPlace(String token) {
			// 
			return new InstructorInfoPlace();
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt.place.shared.Place)
		 */
		public String getToken(InstructorInfoPlace place) {
			// 
			return "";
		}
		
	}
}
