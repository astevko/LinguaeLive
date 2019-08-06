/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import ca.jhosek.main.client.activity.mainregion.MyCoursesActivity;

/**
 * show user's courses page
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see MyCoursesActivity
 */
 
public class MyCoursesPlace extends AuthenticatedPlace {

	@Prefix("yourcourses")
	public static class Tokenizer implements PlaceTokenizer<MyCoursesPlace> {

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		public MyCoursesPlace getPlace(String token) {
			// 
			return new MyCoursesPlace();
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt.place.shared.Place)
		 */
		public String getToken(MyCoursesPlace place) {
			// 
			return "";
		}
		
	}
}
