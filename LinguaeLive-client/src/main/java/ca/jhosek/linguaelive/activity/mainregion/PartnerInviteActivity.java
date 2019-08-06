package ca.jhosek.main.client.activity.mainregion;

import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

import ca.jhosek.main.client.domain.CurrentState;
import ca.jhosek.main.client.event.SessionInviteUpdateEvent;
import ca.jhosek.main.client.event.UserStatusChangeEvent;
import ca.jhosek.main.client.place.PartnerInvitePlace;
import ca.jhosek.main.client.place.SessionControlPlace;
import ca.jhosek.main.client.place.StudentYourCoursePlace;
import ca.jhosek.main.client.ui.GroupingHandlerRegistration;
import ca.jhosek.main.client.ui.priv.student.PartnerInviteView;
import ca.jhosek.main.client.ui.priv.student.PartnerInviteViewImpl;
import ca.jhosek.main.client.ui.priv.student.PartnerInviteViewImpl.Driver;
import ca.jhosek.main.shared.LanguageType;
import ca.jhosek.main.shared.UserType;
import ca.jhosek.main.shared.proxy.AppRequestFactory;
import ca.jhosek.main.shared.proxy.PartnerInviteProxy;
import ca.jhosek.main.shared.proxy.PartnerInviteRequestContext;
import ca.jhosek.main.shared.proxy.SessionProxy;
import ca.jhosek.main.shared.proxy.SessionRequestContext;
import ca.jhosek.main.shared.proxy.UserProxy;

/**
 * student's view of a session invitation
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 * @see PartnerInvitePlace
 * @see PartnerInviteView
 * @see PartnerInviteViewImpl
 * SessionInviteDao
 * 
 */
public class PartnerInviteActivity extends AbstractActivity implements PartnerInviteView.Presenter {

	private static final Logger logger = Logger.getLogger( PartnerInviteActivity.class.getName() );

	private final PartnerInviteView view;

	private final PlaceController placeController;

	private final AppRequestFactory requestFactory;

	private final CurrentState currentState;

	private final String token;

	private PartnerInviteProxy sessionInvite;

	private Driver sessionInviteProxyDriver;

	private final EventBus eventBus;

	private final GroupingHandlerRegistration handlerReg = new GroupingHandlerRegistration();

	/**
	 * sent a notification message to the partner
	 */
	private boolean notifiedPartner = false;

	/**
	 * Guice/Gin Factory Module Builder
	 * {@link} http://google-guice.googlecode.com/svn/trunk/javadoc/com/google/inject/assistedinject/FactoryModuleBuilder.html
	 * @see FactoryModuleBuilder
	 * @see GinFactoryModuleBuilder
	 *  
	 */
	public interface Factory {
		PartnerInviteActivity create( String sessionInviteId );
	}

	@Inject
	public PartnerInviteActivity(
			EventBus eventBus,
			final PartnerInviteView view, 
			PlaceController placeController,
			AppRequestFactory requestFactory,
			CurrentState currentState,
			@Assisted String sessionInviteId
			) {
		super();
		this.eventBus = eventBus;
		this.view = view;
		this.placeController = placeController;
		this.requestFactory = requestFactory;
		this.currentState = currentState;
		// local to this   sessionInviteId
		logger.info("SessionInviteActivity( token=" + sessionInviteId );
		this.token = sessionInviteId;
		view.setPresenter(this);
		this.sessionInviteProxyDriver = view.createEditorDriver(eventBus, requestFactory);

		// session invite refresh handler
		handlerReg.add(eventBus.addHandler( SessionInviteUpdateEvent.TYPE, new SessionInviteUpdateEvent.Handler() {

			@Override
			public void onSessionInviteUpdate(SessionInviteUpdateEvent event) {
				if (event.getSessionInviteId().equals(token)) {
					// initiate a refresh for this place and session invite id
					refresh();
				}

			}
		}));


		// user status refresh handler
		handlerReg.add(eventBus.addHandler( UserStatusChangeEvent.TYPE, new UserStatusChangeEvent.Handler() {

			@Override
			public void onUserStatusChange(UserStatusChangeEvent event) {
				final String email = event.getEmailAddress();

				if (sessionInvite != null) {
					final UserProxy user1 = sessionInvite.getMember1().getUser();
					final UserProxy user2 = sessionInvite.getMember2().getUser();
					if (user1.getEmailAddress().equals(email)) {
						view.showMember1UserOnlineStatus(event.getStatus());
						
					} else if (user2.getEmailAddress().equals(email)) {
						view.showMember2UserOnlineStatus(event.getStatus());
						
					}
				}
			}
		}));
	}


	/* (non-Javadoc)
	 * @see com.google.gwt.activity.shared.Activity#start(com.google.gwt.user.client.ui.AcceptsOneWidget, com.google.gwt.event.shared.EventBus)
	 */
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		logger.info("SessionInviteActivity starting( token=" + token  );
		panel.setWidget(view);
		refresh();
	}


	/**
	 * refresh the display
	 */
	private void refresh() {
		try {
			// convert token to Long id
			final Long id = Long.valueOf(token);
			// obtain context
			final PartnerInviteRequestContext findInviteContext = requestFactory.sessionInviteRequestContext();
			final Request<PartnerInviteProxy> req = findInviteContext.findSessionInvite(id);
			req.with("member1", "member2", "member1.user", "member2.user", "member1.course.owner", "member2.course.owner" );			
			req.fire( new Receiver< PartnerInviteProxy >() {

				@Override
				public void onSuccess( PartnerInviteProxy response) {
					final Long loggedInUserId = currentState.getLoggedInUser().getId();
					// clear the view
					view.clear();						
					// working on this course
					sessionInvite = response;
					// test for success
					if ( response == null ) {
						logger.severe("SessionInvite id not found: " + id.toString() );
						// TODO: i18n
						Window.alert("Bad partner invite id" );
						placeController.goTo( currentState.getHomePlace() );

					} else if ( 
							!sessionInvite.getMember1().getUser().getId().equals( loggedInUserId ) &&		// member 1 user 
							!sessionInvite.getMember2().getUser().getId().equals( loggedInUserId ) &&  	// member 2 user
							!sessionInvite.getMember1().getCourse().getOwner().getId().equals( loggedInUserId ) && // member 1 course owner
							!sessionInvite.getMember2().getCourse().getOwner().getId().equals( loggedInUserId ) &&	// member 2 course owner
							!currentState.getLoggedInUser().getUserType().equals(UserType.ADMIN)
							) {
						// access violation
						logger.warning( "Access Violation - by:" + loggedInUserId + " attempt session invite id=" + response.getId() ); 
						// TODO: i18n
						Window.alert( "Access Violation: You are not authorized to view this partner invite." );
						placeController.goTo( currentState.getHomePlace() );

					} else {
						// show partner invite in editor
						logger.info( "Displaying partner invite - member1 id=" + response.getMember1().getId() + ", member2 id=" + response.getMember2().getId() );
						sessionInviteProxyDriver.display(sessionInvite);
						boolean isSender = (response.getMember1().getUser().getId().equals(loggedInUserId) || 
								currentState.getLoggedInUser().getUserType().equals(UserType.ADMIN) );

						logger.info( "isSender=" + isSender + ", isPending=" + response.getPending() + ", isAccepted=" + response.getAccepted() );
						view.setViewMode( isSender, response.getPending(), response.getAccepted());

						// update language names
						view.updateLanguageButtons(sessionInvite.getMember1().getCourse().getExpertLanguage(), sessionInvite.getMember2().getCourse().getExpertLanguage());

						// force check for updates now because the timer waits until 
						checkForInprogressSessions();
					}
				}

			});

		} catch (NumberFormatException e) {
			//
			logger.severe("bad token not a course link id: " + token );
			// TODO: i18n
			Window.alert("Bad or unknown course link id: " + token + ", redirecting to home page" );
			placeController.goTo( currentState.getHomePlace());
		}
	}


	/**
	 * called by onConfirm, onDecline, and onUnlinkCourses user click events.
	 * 
	 * @see ca.jhosek.main.client.ui.priv.student.PartnerInviteView.Presenter#respondToSessionInvite(java.lang.Boolean)
	 */
	public void respondToSessionInvite( Boolean accept) {
		logger.info("respondToSessionInvite( " + accept + " )");

		PartnerInviteRequestContext sessionInviteRequestContext = requestFactory.sessionInviteRequestContext();
		PartnerInviteProxy editInvite = sessionInviteRequestContext.edit( sessionInvite );
		// update domain object 
		editInvite.setAccepted(accept);
		editInvite.setPending(false);

		sessionInviteRequestContext.persist(editInvite).to( new Receiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				// TODO: i18n
				Window.alert( "Status changed, refreshing display");
				eventBus.fireEvent(new PlaceChangeEvent(new PartnerInvitePlace(token) ) );
			}

			//TODO add onFailure 
			//TODO add onViolation
		}).fire();
	}


	/** 
	 * create a new session with the current partner invite
	 * and send user to SessionControl
	 *  
	 * @param sessionLang 
	 * 
	 * @see ca.jhosek.main.client.ui.priv.student.PartnerInviteView.Presenter#startNewSessionWithCurrentInvite()
	 */
	protected void startNewSessionWithCurrentInvite(LanguageType sessionLang) {
		logger.info( "createFromInvite() " );
		// if this is member 1 - then set start member to 1 else set to 2
		boolean isMember1Viewing = sessionInvite.getMember1().getUser().getId().equals(currentState.getLoggedInUser().getId());
		// 
		SessionRequestContext newSessionContext = requestFactory.sessionRequestContext();
		Request<SessionProxy> req = newSessionContext.createFromInvite( sessionInvite, sessionLang, (isMember1Viewing)?1:2 );
		req.with( "member1", "member1.user" , "member2", "member2.user" );
		req.to( new Receiver<SessionProxy>() {

			@Override
			public void onSuccess(SessionProxy newSession ) {
				//
				logger.info( "createFromInvite() has returned a new session with id=" + newSession.getId() );
				placeController.goTo( new SessionControlPlace( newSession ));
			}
			//TODO add onFailure 
			//TODO add onViolation
		}).fire();

	}

	/* (non-Javadoc)
	 * @see ca.jhosek.main.client.ui.priv.student.SessionInviteView.Presenter#gotoMember1Course()
	 */
	public void gotoMember1Course() {
		// 
		placeController.goTo(new StudentYourCoursePlace(sessionInvite.getMember1().getCourse()));

	}

	/* (non-Javadoc)
	 * @see ca.jhosek.main.client.ui.priv.student.SessionInviteView.Presenter#gotoMember2Course()
	 */

	public void gotoMember2Course() {
		//
		placeController.goTo(new StudentYourCoursePlace(sessionInvite.getMember2().getCourse()));

	}

	/* (non-Javadoc)
	 * @see ca.jhosek.main.client.ui.priv.student.SessionInviteView.Presenter#startSessionWithCurrentInvite()
	 */

	public void startSessionWithCurrentInvite() {
		// start a new session with course 1 target language
		checkForInprogressSessionsPriorToStartingNewSession(sessionInvite.getMember1().getCourse().getTargetLanguage());
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.main.client.ui.priv.student.SessionInviteView.Presenter#startOtherSessionWithCurrentInvite()
	 */

	public void startOtherSessionWithCurrentInvite() {
		// start a new session with course 2 target language
		checkForInprogressSessionsPriorToStartingNewSession(sessionInvite.getMember2().getCourse().getTargetLanguage());
	}

	/**
	 * Check to make sure neither user1 nor user2 have open sessions prior to starting a new session.
	 * 
	 * @param targetLanguage language to start a new session with 
	 */
	private void checkForInprogressSessionsPriorToStartingNewSession(final LanguageType targetLanguage) {
		final UserProxy user1 = sessionInvite.getMember1().getUser();
		final UserProxy user2 = sessionInvite.getMember2().getUser();
		final boolean isMember1Viewing = user1.getId().equals(currentState.getLoggedInUser().getId());
		final UserProxy thisUser = isMember1Viewing?user1:user2;
		final UserProxy otherUser = !isMember1Viewing?user2:user1;

		// requires a session request context
		final SessionRequestContext firstSessionContext = requestFactory.sessionRequestContext();
		final Request<List<SessionProxy>> req1 = firstSessionContext.getOpenSessionsForUser(thisUser);
		req1.to( new Receiver<List<SessionProxy>>() {

			@Override
			public void onFailure(ServerFailure error) {
				//
				super.onFailure(error);
				// TODO: i18n
				Window.alert("Communications error while checking for open sessions for " + thisUser.getFirstName() + thisUser.getLastName());
			}

			@Override
			public void onSuccess(List<SessionProxy> inprogressSessions) {
				// check to see if there are in progress sessions for this user
				if (!inprogressSessions.isEmpty()) {
					// in progress session for this user!
					// TODO: i18n
					if (Window.confirm("In progress session detected for you. Please conclude that session prior to starting another. Redirecting you to the session in-progress.")) {
						// redirect to session in response
						placeController.goTo( new SessionControlPlace( inprogressSessions.get(0) ));
					}

				} else {
					// no outstanding sessions for this user.
					// check for sessions for the other user
					final SessionRequestContext secondSessionContext = requestFactory.sessionRequestContext();
					final Request<List<SessionProxy>> req1 = secondSessionContext.getOpenSessionsForUser(otherUser);
					req1.to( new Receiver<List<SessionProxy>>() {

						@Override
						public void onFailure(ServerFailure error) {
							//
							super.onFailure(error);
							// TODO: i18n
							Window.alert("Communications error while checking for open sessions for " + otherUser.getFirstName() + otherUser.getLastName());
						}

						@Override
						public void onSuccess(List<SessionProxy> inprogressSessions) {
							// search in progress sessions for the other user
							if (!inprogressSessions.isEmpty()) {
								// found inprogress sessions for other user!
								// TODO: i18n
								Window.alert("In progress session detected for the other user. Cannot start a new session with him/her until they conclude the in progress session");
							} else {
								// all clear
								startNewSessionWithCurrentInvite(targetLanguage);								
							}
						}

					}).fire();
				}
			}
		}).fire();

	}
	/**
	 * Check to make sure this user does not have open sessions
	 * 
	 */

	public void checkForInprogressSessions() {
		final UserProxy user1 = sessionInvite.getMember1().getUser();
		final UserProxy user2 = sessionInvite.getMember2().getUser();
		final boolean isMember1Viewing = user1.getId().equals(currentState.getLoggedInUser().getId());
		@SuppressWarnings("unused")
		final boolean isMember2Viewing = user2.getId().equals(currentState.getLoggedInUser().getId());
		final UserProxy thisUser = isMember1Viewing?user1:user2;
		final UserProxy otherUser = !isMember1Viewing?user1:user2;

		// requires a session request context
		final SessionRequestContext firstSessionContext = requestFactory.sessionRequestContext();
		final Request<List<SessionProxy>> req1 = firstSessionContext.getOpenSessionsForUser(thisUser);
		req1.to( new Receiver<List<SessionProxy>>() {

			@Override
			public void onFailure(ServerFailure error) {
				//
				super.onFailure(error);
				//				Window.alert("Communications error while checking for open sessions for " + thisUser.getFirstName() + thisUser.getLastName());
			}

			@Override
			public void onSuccess(List<SessionProxy> inprogressSessions) {
				// check to see if there are in progress sessions for this user
				if (!inprogressSessions.isEmpty()) {
					// in progress session for this user!
					// TODO: i18n
					if (Window.confirm("In progress session detected for you. Redirecting you to the session in-progress.")) {
						// redirect to session in response
						placeController.goTo( new SessionControlPlace( inprogressSessions.get(0) ));
					}					
					// stop polling
					view.clear();
				} else if (!notifiedPartner && sessionInvite.getAccepted()) {
					// ok - don't do this again - we get into an infinite notification cycle
					notifiedPartner = true;

					// ok, no inprogress sessions - lets sync the other user onto this page...
					final PartnerInviteRequestContext syncSessionInviteContext = requestFactory.sessionInviteRequestContext();
					syncSessionInviteContext.openSessionInvite(otherUser, sessionInvite).fire();

				}
			}
		}).fire();
	}
}
