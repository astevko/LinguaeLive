/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import ca.jhosek.main.client.activity.mainregion.QueryCoursesActivity;
import ca.jhosek.main.client.ui.priv.admin.QueryCoursesView;
import ca.jhosek.main.client.ui.priv.admin.QueryCoursesViewImpl;

/**
 * admin user is logged in, show query courses page
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see QueryCoursesView
 * @see QueryCoursesViewImpl
 * @see QueryCoursesActivity
 *
 */
public class QueryCoursesPlace extends AuthenticatedPlace {

	@Prefix("querycourses")
	public static class Tokenizer implements PlaceTokenizer<QueryCoursesPlace> {

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		public QueryCoursesPlace getPlace(String token) {
			// 
			return new QueryCoursesPlace();
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt.place.shared.Place)
		 */
		public String getToken(QueryCoursesPlace place) {
			// 
			return "";
		}
		
	}
}
