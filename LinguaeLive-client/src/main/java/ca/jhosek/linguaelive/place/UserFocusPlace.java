package ca.jhosek.linguaelive.place;

import ca.jhosek.linguaelive.proxy.UserProxy;

public abstract class UserFocusPlace extends AuthenticatedPlace {

	/**
	 * change this user id's password
	 */
	protected String userId;
	
	public void setUserId(String userId) {
		this.userId = userId;
	}

	protected UserProxy user;

	public UserFocusPlace() {
		super();
	}

	/**
	 * @return
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @return the user
	 */
	public UserProxy getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(UserProxy user) {
		this.user = user;
	}

}