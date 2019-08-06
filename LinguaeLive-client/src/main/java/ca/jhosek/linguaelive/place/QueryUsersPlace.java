/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import ca.jhosek.main.client.activity.mainregion.QueryUsersActivity;
import ca.jhosek.main.client.ui.priv.admin.QueryUsersView;
import ca.jhosek.main.client.ui.priv.admin.QueryUsersViewImpl;

/**
 * admin user is logged in, show query users page
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see QueryUsersActivity
 * @see QueryUsersView
 * @see QueryUsersViewImpl
 *
 */
 
public class QueryUsersPlace extends AuthenticatedPlace {

	@Prefix("queryusers")
	public static class Tokenizer implements PlaceTokenizer<QueryUsersPlace> {

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		public QueryUsersPlace getPlace(String token) {
			// 
			return new QueryUsersPlace();
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt.place.shared.Place)
		 */
		public String getToken(QueryUsersPlace place) {
			// 
			return "";
		}
		
	}
}
