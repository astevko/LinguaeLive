/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import ca.jhosek.main.client.activity.mainregion.Video1Activity;
import ca.jhosek.main.client.ui.anon.Video1View;

/**
 * user is anon, show video page
 *   
 * @author copyright (C) 2012 Andrew Stevko
 * @see Video2Place
 * @see Video1Activity
 * @see Video1View
 *
 */
public class Video2Place extends Place {

	@Prefix("video2")
	public static class Tokenizer implements PlaceTokenizer<Video2Place> {

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		public Video2Place getPlace(String token) {
			// 
			return new Video2Place();
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt.place.shared.Place)
		 */
		public String getToken(Video2Place place) {
			// 
			return "";
		}
		
	}
}
