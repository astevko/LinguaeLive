/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import ca.jhosek.main.client.ui.priv.instructor.InstructorTipsViewImpl;

/**
 * show tips
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *	@see AppPlaceHistoryMapper
 *  @see InstructorTipsViewImpl
 */
public class InstructorTipsPlace extends Place {

	@Prefix("instructortips")
	public static class Tokenizer implements PlaceTokenizer<InstructorTipsPlace> {

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		public InstructorTipsPlace getPlace(String token) {
			// 
			return new InstructorTipsPlace();
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt.place.shared.Place)
		 */
		public String getToken(InstructorTipsPlace place) {
			// 
			return "";
		}
		
	}
}
