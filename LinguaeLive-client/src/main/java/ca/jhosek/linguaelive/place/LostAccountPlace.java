/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import ca.jhosek.linguaelive.activity.mainregion.LostAccountActivity;
import ca.jhosek.linguaelive.ui.anon.LostAccountView;
import ca.jhosek.linguaelive.ui.anon.LostAccountViewImpl;

/**
 * show lost password form
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see LostAccountActivity
 * @see LostAccountView
 * @see LostAccountViewImpl
 * @see AppPlaceHistoryMapper referenced by
 * 
 */
public class LostAccountPlace extends Place {

	@Prefix("lost")
	public static class Tokenizer implements PlaceTokenizer<LostAccountPlace> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		@Override
		public LostAccountPlace getPlace(final String token) {
			//
			return new LostAccountPlace();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt
		 * .place.shared.Place)
		 */
		@Override
		public String getToken(final LostAccountPlace place) {
			//
			return "";
		}

	}
}
