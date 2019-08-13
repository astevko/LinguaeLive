/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import ca.jhosek.linguaelive.activity.mainregion.LoginFormActivity;
import ca.jhosek.linguaelive.ui.anon.LoginFormView;
import ca.jhosek.linguaelive.ui.anon.LoginFormViewImpl;

/**
 * show login form 
 * contains optional forward to Place
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see LoginFormActivity
 * @see LoginFormView
 * @see LoginFormViewImpl
 * 
 */
public class LoginFormPlace extends Place {	
	
	@Prefix("login")
	public static class Tokenizer implements PlaceTokenizer<LoginFormPlace> {

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		public LoginFormPlace getPlace(String token) {
			// 
			return new LoginFormPlace( );
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt.place.shared.Place)
		 */
		public String getToken(LoginFormPlace place) {
			// 
			return "";
		}				
	}
	
	/**
	 * optional forward to place
	 */
	private final Place forwardToPlace;
	
	public LoginFormPlace() {
		forwardToPlace = null;
	}
	
	public LoginFormPlace( Place forwardToPlace ) {
		// 
		this.forwardToPlace = forwardToPlace;
	}

	public Place getForwardToPlace() {
		return forwardToPlace;
	}
	
	
}
