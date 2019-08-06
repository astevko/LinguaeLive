/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import ca.jhosek.main.client.activity.mainregion.MyProfileActivity;
import ca.jhosek.main.client.mvp.MainRegionActivityMapper;
import ca.jhosek.main.client.ui.priv.MyProfileView;
import ca.jhosek.main.client.ui.priv.MyProfileViewImpl;
import ca.jhosek.main.shared.proxy.UserProxy;

/**
 * show user's profile page
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see UserFocusPlace
 * @see MyProfileActivity
 * @see MyProfileView
 * @see MyProfileViewImpl
 * 
 * @see AppPlaceHistoryMapper
 * @see MainRegionActivityMapper
 * 
 */
 
public class MyProfilePlace extends UserFocusPlace {

	@Prefix("myprofile")
	public static class Tokenizer implements PlaceTokenizer<MyProfilePlace> {

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		public MyProfilePlace getPlace(String token) {
			// 
			return new MyProfilePlace(token);
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt.place.shared.Place)
		 */
		public String getToken(MyProfilePlace place) {
			// 
			return place.getUserId();
		}
		
	}
	
	/**
	 * default user id is blank 
	 */
	public MyProfilePlace() {
		setUserId("");
	}

	
	/**
	 * @param userId show profile for this user id
	 */
	public MyProfilePlace(String userId) {
		this.setUserId(userId);
	}

	/**
	 * @param user show profile for this user
	 */
	public MyProfilePlace(UserProxy user ) {
		this.setUser(user);
		this.setUserId(user.getId().toString());
	}	
}
