package ca.jhosek.main.client.mvp;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;

import ca.jhosek.main.client.activity.userregion.HeaderAnonUserActivity;
import ca.jhosek.main.client.activity.userregion.HeaderUserActivity;
import ca.jhosek.main.client.domain.CurrentState;

/**
 * this is the user region in the header of the main view
 * you can have either an anon user or an authenticated user
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class UserRegionActivityMapper implements ActivityMapper {

	private final Provider<HeaderAnonUserActivity> 	headerAnonUserActivity;
	private final Provider<HeaderUserActivity> 		headerUserActivity;
	private final CurrentState currentState;

	@Inject
	public UserRegionActivityMapper(
			Provider<HeaderAnonUserActivity> 	headerAnonUserActivity,
			Provider<HeaderUserActivity> 		headerUserActivity,
			CurrentState						currentState
	) {
		super();
		this.headerAnonUserActivity = headerAnonUserActivity;
		this.headerUserActivity = headerUserActivity;
		this.currentState = currentState;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.activity.shared.ActivityMapper#getActivity(com.google.gwt.place.shared.Place)
	 * 
	 * shows either one of two user panels - anon or authenticated
	 * 
	 */
	public Activity getActivity(Place place) {

		if ( currentState.isLoggedIn() ) {
			return headerUserActivity.get();			
		}
		return headerAnonUserActivity.get();		
	}
}


