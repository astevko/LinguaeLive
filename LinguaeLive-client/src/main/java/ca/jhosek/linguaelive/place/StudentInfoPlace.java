/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

/**
 * user is anonymous, show student information page
 *   
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class StudentInfoPlace extends Place {

	@Prefix("studentinfo")
	public static class Tokenizer implements PlaceTokenizer<StudentInfoPlace> {

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		public StudentInfoPlace getPlace(String token) {
			// 
			return new StudentInfoPlace();
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt.place.shared.Place)
		 */
		public String getToken(StudentInfoPlace place) {
			// 
			return "";
		}
		
	}
}
