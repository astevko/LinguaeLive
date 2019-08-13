/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import ca.jhosek.linguaelive.activity.mainregion.RecoverPasswordActivity;
import ca.jhosek.linguaelive.ui.anon.RecoverPasswordView;
import ca.jhosek.linguaelive.ui.anon.RecoverPasswordViewImpl;

/**
 * reset/recover lost password form
 * 
 * @author copyright (C) 2012 Andrew Stevko
 * 
 * @see RecoverPasswordActivity
 * @see RecoverPasswordView
 * @see RecoverPasswordViewImpl
 * @see AppPlaceHistoryMapper registered here
 */
public class RecoverPasswordPlace extends Place {

	@Prefix("lostandfound")
	public static class Tokenizer implements
			PlaceTokenizer<RecoverPasswordPlace> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		@Override
		public RecoverPasswordPlace getPlace(final String token) {
			//
			return new RecoverPasswordPlace();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt
		 * .place.shared.Place)
		 */
		@Override
		public String getToken(final RecoverPasswordPlace place) {
			//
			return "";
		}

	}
}
