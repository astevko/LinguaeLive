/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import ca.jhosek.main.client.activity.mainregion.ChangePswdActivity;
import ca.jhosek.main.client.mvp.MainRegionActivityMapper;
import ca.jhosek.main.client.ui.priv.ChangePswdView;
import ca.jhosek.main.client.ui.priv.ChangePswdViewImpl;
import ca.jhosek.main.shared.proxy.UserProxy;

/**
 * show user's profile change password page
 * 
 * @author copyright (C) 2012 Andrew Stevko
 * 
 * @see UserFocusPlace
 * @see ChangePswdActivity
 * @see ChangePswdView
 * @see ChangePswdViewImpl
 * 
 * @see AppPlaceHistoryMapper
 * @see MainRegionActivityMapper
 * 
 */

public class ChangePswdPlace extends Place {
	private static final String DEFAULT_UID = "-1";

	@Prefix("chgpswd")
	public static class Tokenizer implements PlaceTokenizer<ChangePswdPlace> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		@Override
		public ChangePswdPlace getPlace(final String token) {
			//
			return new ChangePswdPlace(token);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt
		 * .place.shared.Place)
		 */
		@Override
		public String getToken(final ChangePswdPlace place) {
			//			
			return place.getUserId();
		}

	}

	protected UserProxy user = null;

	/**
	 * change this user id's password
	 */
	protected String userId = DEFAULT_UID;

	/**
	 * @param userId
	 */
	public ChangePswdPlace(final String userId) {
		// save the user id
		this.setUserId(userId);
	}

	/**
	 * @param user
	 *            user
	 */
	public ChangePswdPlace(final UserProxy user) {
		// save the user and user id
		this.setUser(user);
		this.setUserId(user.getId().toString());
	}

	/**
	 * no user token allowed here...!!!!
	 * CALL setUser( user ) before using it
	 */
	public ChangePswdPlace() {
		// 
	}

	/**
	 * @return the user
	 */
	public UserProxy getUser() {
		return user;
	}

	/**
	 * @return
	 */
	public String getUserId() {
		if (userId.equals(DEFAULT_UID)) {
			throw new IllegalArgumentException("Attempted HACK OF CHANGE PASSWORD PAGE.");
		}
		return userId;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(final UserProxy user) {
		this.user = user;
	}

	public void setUserId(final String userId) {
		this.userId = userId;
	}

}
