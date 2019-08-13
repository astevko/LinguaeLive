/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import ca.jhosek.linguaelive.activity.mainregion.Video3Activity;
import ca.jhosek.linguaelive.ui.anon.Video3View;

/**
 * user is anon, show video page
 *   
 * @author copyright (C) 2012 Andrew Stevko
 * @see Video3Place
 * @see Video3Activity
 * @see Video3View
 *
 */
public class Video3Place extends Place {

	@Prefix("video3")
	public static class Tokenizer implements PlaceTokenizer<Video3Place> {

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		public Video3Place getPlace(String token) {
			// 
			return new Video3Place();
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt.place.shared.Place)
		 */
		public String getToken(Video3Place place) {
			// 
			return "";
		}
		
	}
}
