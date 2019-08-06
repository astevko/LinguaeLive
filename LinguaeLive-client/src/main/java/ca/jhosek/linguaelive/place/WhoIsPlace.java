/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import ca.jhosek.main.client.activity.mainregion.WhoIsActivity;
import ca.jhosek.main.client.mvp.MainRegionActivityMapper;
import ca.jhosek.main.client.ui.anon.WhoIsView;

/**
 * user is anonymous, show who is page
 *   
 * @author copyright (C) 2011 Andrew Stevko
 *
 * @see WhoIsActivity
 * @see WhoIsPlace
 * @see WhoIsView
 * 
 * @see AppPlaceHistoryMapper
 * @see MainRegionActivityMapper
 */
public class WhoIsPlace extends Place {

	@Prefix("whois")
	public static class Tokenizer implements PlaceTokenizer<WhoIsPlace> {

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		public WhoIsPlace getPlace(String token) {
			// 
			return new WhoIsPlace();
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt.place.shared.Place)
		 */
		public String getToken(WhoIsPlace place) {
			// 
			return "";
		}
		
	}
}
