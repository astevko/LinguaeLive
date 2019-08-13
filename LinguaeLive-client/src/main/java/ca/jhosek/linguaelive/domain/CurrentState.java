/**
 * copyright (c) 2011 - 2014 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.domain;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

import ca.jhosek.linguaelive.place.AdminHomePlace;
import ca.jhosek.linguaelive.place.IndexPlace;
import ca.jhosek.linguaelive.place.InstructorHomePlace;
import ca.jhosek.linguaelive.place.MyProfilePlace;
import ca.jhosek.linguaelive.place.SessionControlPlace;
import ca.jhosek.linguaelive.place.StudentHomePlace;
import ca.jhosek.linguaelive.ui.priv.AutoLoginPauseDialog;
import ca.jhosek.linguaelive.UserType;
import ca.jhosek.linguaelive.proxy.AppRequestFactory;
import ca.jhosek.linguaelive.proxy.SessionProxy;
import ca.jhosek.linguaelive.proxy.SessionRequestContext;
import ca.jhosek.linguaelive.proxy.UserProxy;
import ca.jhosek.linguaelive.proxy.UserRequestContext;

/**
 * Singleton holds the reference to the current logged in user instance
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see ChannelCallback
 */
@Singleton
public class CurrentState {

	/**
	 * Current state logger
	 */
	static final Logger logger = Logger.getLogger(CurrentState.class.getName());

	/**
	 * cookie 1 contains the login name
	 */
	private static final String LOGIN_COOKIE_1 = "REMEMBERME1";

	/**
	 * cookie 2 contains the hashed password
	 */
	private static final String LOGIN_COOKIE_2 = "REMEMBERME2";

	/**
	 * JSNI wrapper
	 * @return the browser's user agent lower case'd
	 */
	public static native String getUserAgent() /*-{
												return navigator.userAgent.toLowerCase();
												}-*/;

	/**
	 * injected event bus
	 */
	private final EventBus eventBus;

	/**
	 * forward to this place after login
	 */
	private Place forwardLoginTo;

	/**
	 * session in focus
	 */
	private Long inFocusSessionId;

	/**
	 * logged in user
	 */
	private UserProxy loggedInUser = null;

	/**
	 * injected place controller
	 */
	private final PlaceController placeController;

	/**
	 * injected app request factory to get RequestContext instances
	 */
	private final AppRequestFactory requestFactory;

	/**
	 * null means no session
	 */
	private SessionProxy inFocusSession = null;

	/**
	 * c-tor w/ injectors; creates new channelcallback
	 */
	@Inject
	public CurrentState(final EventBus eventBus,
			final AppRequestFactory requestFactory,
			final PlaceController placeController) {
		this.eventBus = eventBus;
		this.requestFactory = requestFactory;
		//
		this.placeController = placeController;
		// attempt autologin
		autoLogin();
	}

	/**
	 * attempt to autologin user
	 */
	public void autoLogin() {

		final UserRequestContext userContext = requestFactory.userRequest();

		final String cookie1 = Cookies.getCookie(LOGIN_COOKIE_1);
		final String cookie2 = Cookies.getCookie(LOGIN_COOKIE_2);

		if (cookie1 != null && cookie2 != null) {
			//
			logger.info("attempting auto login with email address=" + cookie1);
			final AutoLoginPauseDialog pauseDialog = new AutoLoginPauseDialog();
			pauseDialog.show();
			// flush object to persistent storage
			userContext.loginCookie(cookie1, cookie2).fire(
					new Receiver<UserProxy>() {

						@Override
						public void onSuccess(final UserProxy response) {
							pauseDialog.hide();
							//
							loggedInUser = response;

							if (response != null) {
								// cookies worked!!
								logger.info("auto login user with email address = "
										+ loggedInUser.getEmailAddress());

								if (forwardLoginTo == null) {
									placeController.goTo(getHomePlace());

								} else {
									// user came into site via deep link - send
									// them to it
									logger.info("forwarding to previous place - "
											+ forwardLoginTo.toString());

									// placeController.goTo(forwardLoginTo);
									// force a load of this page
									eventBus.fireEvent(new PlaceChangeEvent(
											forwardLoginTo));

								}
							} else {
								// auto-login failed
								placeController.goTo(getHomePlace());

							}
						}

					});
		}
	}

	/**
	 * checks loggedin user for open sessions
	 */
	public void checkForInprogressSessions() {
		// Check for open sessions
		final SessionRequestContext sessionRequestContext = requestFactory
				.sessionRequestContext();
		final Request<List<SessionProxy>> req = sessionRequestContext
				.getOpenSessionsForUser(loggedInUser);
		req.with("member1", "member1.user", "member2", "member2.user");
		req.to(new Receiver<List<SessionProxy>>() {

			@Override
			public void onFailure(final ServerFailure error) {
				//
				logger.severe(error.getMessage());
				Window.alert("server error getOpenSessionsForUser:\n"
						+ error.getMessage());
			}

			@Override
			public void onSuccess(final List<SessionProxy> loadedSessions) {
				//
				logger.info("getOpenSessionsForUser() success "
						+ loadedSessions.size() + " Sessions");
				if (!loadedSessions.isEmpty()) {

					// open session in progress found!!!
					final SessionProxy openSession = loadedSessions.get(0);
					if (openSession.getId().equals(getInFocusSessionId())
							|| Window
									.confirm("In-progress partner session found. Do you want to go to it?")) {
						placeController.goTo(new SessionControlPlace(
								openSession));
					}
				}
			}
		}).fire();
	}


	/**
	 * @return the email address last used for logging in *
	 */
	public String getCookieEmailAddress() {
		return Cookies.getCookie(LOGIN_COOKIE_1);
	}

	/**
	 * @return the forwardLoginTo
	 */
	public Place getForwardLoginTo() {
		return forwardLoginTo;
	}

	/**
	 * @return the home place of the logged in user
	 */
	public Place getHomePlace() {
		if (loggedInUser == null) {
			return new IndexPlace();
		} else {
			switch (loggedInUser.getUserType()) {
			case ADMIN:
				return new AdminHomePlace();
			case INSTRUCTOR:
				return new InstructorHomePlace();
			case STUDENT:
				return new StudentHomePlace();
			case ANON:
			default:
				return new IndexPlace();
			}
		}
	}

	public Long getInFocusSessionId() {
		return inFocusSessionId;
	}

	/**
	 * @return the loggedInUser
	 */
	public UserProxy getLoggedInUser() {
		logger.fine("getLoggedInUser()");
		return loggedInUser;
	}

	/**
	 * @return is there a user logged in & its an admin
	 */
	public boolean isAdminUser() {
		logger.fine("isAdminUser() ="
				+ ((loggedInUser != null) ? "true" : "false"));
		return isLoggedIn()
				&& UserType.ADMIN.equals(loggedInUser.getUserType());
	}

	/**
	 * @return is there a user logged in
	 */
	public boolean isLoggedIn() {
		logger.fine("isLoggedIn() ="
				+ ((loggedInUser != null) ? "true" : "false"));
		return loggedInUser != null;
	}

	/**
	 * @param loggedInUser
	 *            the loggedInUser to set
	 */
	public void loginThisUser(final UserProxy loggedInUser) {
		logger.info("loginThisUser()");
		this.loggedInUser = loggedInUser;

		rememberUserLogin(true);
	}

	/**
	 * logout user
	 */
	public void logoutUser() {
		logger.fine("logoutUser()");
		this.loggedInUser = null;
		Cookies.removeCookie(LOGIN_COOKIE_2);

		// clear forward
		this.forwardLoginTo = null;


		Window.alert("Please close this window to complete the logout process.");
	}

	/**
	 * fetches the current user profile
	 */
	public void refetchProfile() {
		final UserRequestContext userContext = requestFactory.userRequest();
		userContext.findUser(loggedInUser.getId()).fire(
				new Receiver<UserProxy>() {

					@Override
					public void onSuccess(final UserProxy response) {
						//
						loggedInUser = response;
						//
						logger.info("auto login user with email address = "
								+ loggedInUser.getEmailAddress());

						// goto your profile
						placeController.goTo(new MyProfilePlace());
					}

				});
	}

	/**
	 * remember or forget user login
	 * 
	 * @param rememberMe
	 */
	@SuppressWarnings("deprecation")
	public void rememberUserLogin(final boolean rememberMe) {
		//
		if (rememberMe) {
			// (re)set cookie into next year
			final Date expireDate = new Date();
			expireDate.setYear(expireDate.getYear() + 1);
			Cookies.setCookie(LOGIN_COOKIE_1, loggedInUser.getEmailAddress(),
					expireDate);
			// only save password within window rather than on disk
			Cookies.setCookie(LOGIN_COOKIE_2, loggedInUser.getPassword());

		} else {
			// clear password cookie
			// Cookies.removeCookie(LOGIN_COOKIE_1);
			Cookies.removeCookie(LOGIN_COOKIE_2);

		}
	}

	/**
	 * the Place where to goto after login
	 * 
	 * @param place
	 */
	public void setForwardLoginTo(final Place place) {
		//
		this.forwardLoginTo = place;
	}

	public void setInFocusSession(SessionProxy session) {
		
		this.inFocusSession = session;
		this.inFocusSessionId = (session == null) ? null : session.getId();
	}

	public void setLoggedInUser(final UserProxy loggedInUser) {
		this.loggedInUser = loggedInUser;
	}

	/**
	 * @return the inFocusSession
	 */
	public final SessionProxy getInFocusSession() {
		return inFocusSession;
	}

}
