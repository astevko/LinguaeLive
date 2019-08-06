package ca.jhosek.main.client.activity.mainregion;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.validation.ConstraintViolation;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

import ca.jhosek.main.client.LLConstants;
import ca.jhosek.main.client.domain.CurrentState;
import ca.jhosek.main.client.event.SessionChatEvent;
import ca.jhosek.main.client.event.SessionUpdateEvent;
import ca.jhosek.main.client.event.UserStatusChangeEvent;
import ca.jhosek.main.client.place.SessionControlPlace;
import ca.jhosek.main.client.ui.GroupingHandlerRegistration;
import ca.jhosek.main.client.ui.priv.student.SessionControlView;
import ca.jhosek.main.client.ui.priv.student.SessionControlViewImpl;
import ca.jhosek.main.client.ui.priv.student.SessionControlViewImpl.Driver;
import ca.jhosek.main.server.domain.ContactInfoDao;
import ca.jhosek.main.server.domain.SessionDao;
import ca.jhosek.main.shared.LanguageType;
import ca.jhosek.main.shared.UserType;
import ca.jhosek.main.shared.proxy.AppRequestFactory;
import ca.jhosek.main.shared.proxy.ContactInfoProxy;
import ca.jhosek.main.shared.proxy.ContactInfoRequestContext;
import ca.jhosek.main.shared.proxy.SessionProxy;
import ca.jhosek.main.shared.proxy.SessionRequestContext;

/**
 * session control view activity
 * 
 * @author copyright (C) 2011-2014 Andrew Stevko
 * 
 * @see SessionControlView
 * @see SessionControlViewImpl
 * @see SessionRequestContext
 * @see SessionDao
 * 
 * @see ContactInfoRequestContext
 * @see ContactInfoDao
 */
public class SessionControlActivity extends AbstractActivity implements
		SessionControlView.Presenter {

	/**
	 * Guice/Gin Factory Module Builder {@link}
	 * http://google-guice.googlecode.com/svn/trunk/javadoc/com/google/inject/
	 * assistedinject/FactoryModuleBuilder.html
	 * 
	 * @see FactoryModuleBuilder
	 * @see GinFactoryModuleBuilder
	 * 
	 */
	public interface Factory {
		SessionControlActivity create(String sessionInviteId);
	}

	private static final Logger logger = Logger
			.getLogger(SessionControlActivity.class.getName());

	/**
	 * firewall one operation per display
	 */
	private boolean busy = false;

	@SuppressWarnings("unused")
	private final LLConstants constants;

	private final CurrentState currentState;

	@SuppressWarnings("unused")
	private final EventBus eventBus;

	private final GroupingHandlerRegistration handlerReg = new GroupingHandlerRegistration();

	private final PlaceController placeController;

	private final AppRequestFactory requestFactory;

	private SessionProxy session;

	private final Driver sessionProxyDriver;

	private final String token;

	private final SessionControlView view;

	/**
	 * create a new session control activity
	 * 
	 * @param eventBus
	 * @param view
	 * @param placeController
	 * @param requestFactory
	 * @param currentState
	 * @param sessionId
	 */
	@Inject
	public SessionControlActivity(final EventBus eventBus,
			final SessionControlView view,
			final PlaceController placeController,
			final AppRequestFactory requestFactory,
			final CurrentState currentState, final LLConstants constants,
			@Assisted final String sessionId) {
		super();
		this.eventBus = eventBus;
		this.view = view;
		this.placeController = placeController;
		this.requestFactory = requestFactory;
		this.currentState = currentState;
		this.constants = constants;
		//
		logger.info("SessionInviteActivity( token=" + sessionId);
		this.token = sessionId;
		view.setPresenter(this);
		this.sessionProxyDriver = view.createEditorDriver(eventBus,
				requestFactory);
		// ensure not busy
		this.busy = false;

		// refresh handler for Session Control Refresh events for this session
		handlerReg.add(eventBus.addHandler(SessionUpdateEvent.TYPE,
				new SessionUpdateEvent.Handler() {

					@Override
					public void onSessionUpdate(final SessionUpdateEvent event) {
						if (event.getSessionId().equals(token)) {
							logger.info("SessionControlActivity refreshing updated Session "
									+ event.getSessionId());
							// initiate a refresh for this place and session id
							refresh();
						}
					}
				}));
		// refresh handler for Session Control chat events for this session
		handlerReg.add(eventBus.addHandler(SessionChatEvent.TYPE,
				new SessionChatEvent.Handler() {

					@Override
					public void onSessionChat(final SessionChatEvent event) {
						if (event.getSessionId().equals(token)) {
							// update session with this note
							logger.info("SessionControlActivity appending chat "
									+ event.getChat());
							final List<String> notes = session.getNotes();
							notes.add(event.getChat());
							view.showNotes(notes);
						}
					}
				}));

		// refresh handler for User Status Change events for this session
		handlerReg.add(eventBus.addHandler(UserStatusChangeEvent.TYPE,
				new UserStatusChangeEvent.Handler() {

					@Override
					public void onUserStatusChange(
							final UserStatusChangeEvent event) {
						final String email = event.getEmailAddress();
						if (email.equals(session.getMember1().getUser()
								.getEmailAddress())) {
							logger.info("Member 1 user status change to "
									+ event.getStatus());
							view.showMember1UserOnlineStatus(event.getStatus());

						} else if (email.equals(session.getMember2().getUser()
								.getEmailAddress())) {
							logger.info("Member 2 user status change to "
									+ event.getStatus());
							view.showMember2UserOnlineStatus(event.getStatus());

						} else {
							logger.info("Ignoring user status change for "
									+ email);
						}
					}
				}));
	}

	/**
	 * user hit enter on text box to add this note to the session...
	 * 
	 * @see ca.jhosek.main.client.ui.priv.student.SessionControlView.Presenter#addNote(java.lang.String)
	 */
	@Override
	public void addNote(final String note) {
		final Long id = session.getId();
		// get Current user and prepend first name
		final String prefix = currentState.getLoggedInUser().getFirstName()
				+ ": ";

		// obtain context
		final SessionRequestContext addNoteSessionContext = requestFactory
				.sessionRequestContext();
		final Request<SessionProxy> req = addNoteSessionContext.addNote(
				currentState.getLoggedInUser(), session.getId(), prefix + note);
		req.with("member1", "member2", "member1.user", "member2.user",
				"member1.course", "member1.course.owner", "member2.course",
				"member2.course.owner");
		req.fire(new Receiver<SessionProxy>() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see com.google.web.bindery.requestfactory.shared.Receiver#
			 * onConstraintViolation(java.util.Set)
			 */
			@Override
			public void onConstraintViolation(
					final Set<ConstraintViolation<?>> errors) {
				//
				final StringBuffer msg = new StringBuffer(
						"Incomplete or errors found. Please correct and resubmit.");
				for (final ConstraintViolation<?> vio : errors) {
					msg.append("\n* ");
					msg.append(vio.getMessage());
				}
				Window.alert(msg.toString());
				super.onConstraintViolation(errors);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * com.google.web.bindery.requestfactory.shared.Receiver#onFailure
			 * (com.google.web.bindery.requestfactory.shared.ServerFailure)
			 */
			@Override
			public void onFailure(final ServerFailure error) {
				//
				Window.alert("Server communication error " + error.getMessage());
			}

			@Override
			public void onSuccess(final SessionProxy response) {
				// clear the view
				// view.clear();

				// working on this course
				session = response;

				// test for success
				if (response == null) {
					logger.severe("Session id not found: " + id.toString());
					Window.alert("Bad session id");
					placeController.goTo(currentState.getHomePlace());

				} else {
					// show partner invite in editor
					logger.info("Displaying session - member1 id="
							+ response.getMember1().getId() + ", member2 id="
							+ response.getMember2().getId());
					sessionProxyDriver.display(session);

					logger.info("session start time=" + session.getStartTime()
							+ " stop time=" + session.getStopTime()
							+ " duration="
							+ session.getDurationMinutes().toString());

					setViewMode(session);
					view.showNotes(response.getNotes());

				}
			}
		});

	}

	/**
	 * cancel the session
	 */
	@Override
	public void cancelSession() {
		// firewall multiple clicks
		if (busy) {
			logger.info("ignored duplicate cancel session id "
					+ session.getId());
			return;
		}
		// mark as busy
		busy = true;

		logger.info("cancelSession() ");
		final SessionRequestContext cancelSessionContext = requestFactory
				.sessionRequestContext();
		cancelSessionContext
				.cancelSession(session)
				.with("member1", "member2", "member1.user", "member2.user",
						"member1.course", "member1.course.owner",
						"member2.course", "member2.course.owner")
				.to(new Receiver<SessionProxy>() {

					/*
					 * (non-Javadoc)
					 * 
					 * @see
					 * com.google.web.bindery.requestfactory.shared.Receiver
					 * #onConstraintViolation(java.util.Set)
					 */
					@Override
					public void onConstraintViolation(
							final Set<ConstraintViolation<?>> errors) {
						//
						final StringBuffer msg = new StringBuffer(
								"Incomplete or errors found. Please correct and resubmit.");
						for (final ConstraintViolation<?> vio : errors) {
							msg.append("\n* ");
							msg.append(vio.getMessage());
						}
						Window.alert(msg.toString());
						super.onConstraintViolation(errors);
					}

					/*
					 * (non-Javadoc)
					 * 
					 * @see
					 * com.google.web.bindery.requestfactory.shared.Receiver
					 * #onFailure
					 * (com.google.web.bindery.requestfactory.shared.ServerFailure
					 * )
					 */
					@Override
					public void onFailure(final ServerFailure error) {
						//
						Window.alert("Server communication error "
								+ error.getMessage());
					}

					@Override
					public void onSuccess(final SessionProxy cancelSession) {
						//
						session = cancelSession;
						sessionProxyDriver.display(cancelSession);
						logger.info("session start time="
								+ session.getStartTime() + " cancel time="
								+ cancelSession.getStopTime() + " duration="
								+ cancelSession.getDurationMinutes().toString());
						setViewMode(cancelSession);
					}

				});
		cancelSessionContext.fire();
	}

//
// /*a (non-Javadoc)
// * @see com.google.gwt.activity.shared.AbstractActivity#mayStop()
// */
// @Override
// public String mayStop() {
// if (session != null && isActive(session) ) {
// return constants.stopSessionInProgressQuestion();
// }
// return null;
// }
//
// /**
// * stopping the activity will stop the session in progress
// * @see com.google.gwt.activity.shared.AbstractActivity#onStop()
// */
// @Override
// public void onStop() {
// //
// if (session != null && isActive(session) ) {
// stopSession();
// }
// }

	private void fetchMember1ContactInfo(final SessionProxy session) {
		logger.info("fetchMember1ContactInfo()");
		//
		final ContactInfoRequestContext fetchMember = requestFactory
				.contactInfoRequest();
		final Request<List<ContactInfoProxy>> req = fetchMember
				.findContactInfos(session.getMember1().getUser());
		req.to(new Receiver<List<ContactInfoProxy>>() {

			@Override
			public void onSuccess(final List<ContactInfoProxy> response) {
				// determine selected
				ContactInfoProxy selected = null;
				for (final ContactInfoProxy s : response) {
					if (s.getType().equals(session.getMember1ChannelType())) {
						selected = s;
						break;
					}
				}
				// display list
				view.showMember1ContactInfoList(response, selected);
			}

			// TODO add failure handlers
		}).fire();

	}

	private void fetchMember2ContactInfo(final SessionProxy session) {
		logger.info("fetchMember2ContactInfo()");

		final ContactInfoRequestContext fetchMember = requestFactory
				.contactInfoRequest();
		final Request<List<ContactInfoProxy>> req = fetchMember
				.findContactInfos(session.getMember2().getUser());
		req.to(new Receiver<List<ContactInfoProxy>>() {

			@Override
			public void onSuccess(final List<ContactInfoProxy> response) {
				// determine selected
				ContactInfoProxy selected = null;
				for (final ContactInfoProxy s : response) {
					if (s.getType().equals(session.getMember2ChannelType())) {
						selected = s;
						break;
					}
				}
				//
				view.showMember2ContactInfoList(response, selected);
			}
			// TODO add failure handlers
		}).fire();

	}

	/**
	 * isActive means... not canceled and has a start time and does not have a
	 * start time
	 * 
	 * @param session
	 * @return is the session active
	 */
	private boolean isActive(final SessionProxy session) {

		return (!session.getCancelled() && session.getStartTime() != null && session
				.getStopTime() == null);
	}

	/**
	 * @param session
	 * @return is the session started
	 */
	private boolean isStarted(final SessionProxy session) {
		return (session.getStartTime() != null);
	}

	/**
	 * refresh the view with the latest from the server
	 * 
	 * @see ca.jhosek.main.client.ui.priv.student.SessionControlView.Presenter#refresh()
	 */
	@Override
	public void refresh() {
		final Long id = session.getId();
		// obtain context
		final SessionRequestContext findSessionContext = requestFactory
				.sessionRequestContext();
		final Request<SessionProxy> req = findSessionContext.findSession(id);
		req.with("member1", "member2", "member1.user", "member2.user",
				"member1.course", "member1.course.owner", "member2.course",
				"member2.course.owner");
		req.fire(new Receiver<SessionProxy>() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see com.google.web.bindery.requestfactory.shared.Receiver#
			 * onConstraintViolation(java.util.Set)
			 */
			@Override
			public void onConstraintViolation(
					final Set<ConstraintViolation<?>> errors) {
				//
				final StringBuffer msg = new StringBuffer(
						"Incomplete or errors found. Please correct and resubmit.");
				for (final ConstraintViolation<?> vio : errors) {
					msg.append("\n* ");
					msg.append(vio.getMessage());
				}
				Window.alert(msg.toString());
				super.onConstraintViolation(errors);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * com.google.web.bindery.requestfactory.shared.Receiver#onFailure
			 * (com.google.web.bindery.requestfactory.shared.ServerFailure)
			 */
			@Override
			public void onFailure(final ServerFailure error) {
				//
				logger.warning("Server communication error "
						+ error.getMessage());
			}

			@Override
			public void onSuccess(final SessionProxy response) {

				// working on this course
				session = response;

				// test for success
				if (response == null) {
					logger.severe("Session id not found: " + id.toString());
					Window.alert("Bad session id:" + id.toString());
					placeController.goTo(currentState.getHomePlace());

				} else if (response.getNextSessionId().compareTo(0L) > 0) {
					logger.info("Next session found, forwarding to it");
					// next session is register so go to it.
					placeController.goTo(new SessionControlPlace(response
							.getNextSessionId()));

				} else {
					// show partner invite in editor
					logger.info("Displaying session - member1 id="
							+ response.getMember1().getId() + ", member2 id="
							+ response.getMember2().getId());
					sessionProxyDriver.display(session);

					logger.info("session start time=" + session.getStartTime()
							+ " stop time=" + session.getStopTime()
							+ " duration="
							+ session.getDurationMinutes().toString());

					setViewMode(session);
					view.showNotes(session.getNotes());
					fetchMember1ContactInfo(session);
					fetchMember2ContactInfo(session);
				}
			}
		});

	}

	@Override
	public void setMember1ContactInfoSelection(
			final ContactInfoProxy contactInfo) {
		// only member one can set the contact info
		if (currentState.getLoggedInUser().getId()
				.equals(session.getMember1().getUser().getId())) {
			logger.info("setting member1 contact type to "
					+ contactInfo.getType().name());

			// Save
			final SessionRequestContext setInfoContext = requestFactory
					.sessionRequestContext();
			final SessionProxy editable = setInfoContext.edit(session);
			editable.setMember1ChannelType(contactInfo.getType());
			setInfoContext.persist(editable).fire();
			// TODO - add error handlers
		}

	}

	@Override
	public void setMember2ContactInfoSelection(
			final ContactInfoProxy contactInfo) {
		// only member two can set the contact info
		if (currentState.getLoggedInUser().getId()
				.equals(session.getMember2().getUser().getId())) {
			logger.info("setting member2 contact type to "
					+ contactInfo.getType().name());

			// save
			final SessionRequestContext setInfoContext = requestFactory
					.sessionRequestContext();
			final SessionProxy editable = setInfoContext.edit(session);
			editable.setMember2ChannelType(contactInfo.getType());
			setInfoContext.persist(editable).fire();
			// TODO - add error handlers
		}

	}

	/**
	 * Set the viewer's buttons and fields displayed based on a complex state
	 * 
	 * @param sessionViewed
	 */
	private void setViewMode(final SessionProxy sessionViewed) {
		// track the current focused session
		currentState.setInFocusSession(sessionViewed);

		if (sessionViewed == null || currentState.getLoggedInUser() == null) {
			logger.warning("SessionControlActivity.setViewMode() called with null session/user, ignoring");
			return;
		}
		final Long loggedInUserId = currentState.getLoggedInUser().getId();
		final boolean isMember1Viewing = sessionViewed.getMember1().getUser()
				.getId().equals(loggedInUserId)
				|| currentState.isAdminUser();
		final boolean isMember2Viewing = sessionViewed.getMember2().getUser()
				.getId().equals(loggedInUserId);
		final boolean isMember1onLine = sessionViewed.getMember1().getUser()
				.getIsOnline();
		final boolean isMember2onLine = sessionViewed.getMember2().getUser()
				.getIsOnline();

		boolean canStartStopSession = true;
		if (isStarted(sessionViewed) && isActive(sessionViewed)) {
			canStartStopSession = (isMember1Viewing && sessionViewed
					.getStartMember().equals(2))
					|| (isMember2Viewing && sessionViewed.getStartMember()
							.equals(1));
		}

		view.setViewMode(sessionViewed.getCancelled(),
				isStarted(sessionViewed), isActive(sessionViewed),
				sessionViewed.getSessionLanguage(),
				sessionViewed.getOtherLanguage(), isMember1Viewing,
				isMember2Viewing, canStartStopSession, isMember1onLine,
				isMember2onLine);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.activity.shared.Activity#start(com.google.gwt.user.client
	 * .ui.AcceptsOneWidget, com.google.web.bindery.event.shared.EventBus)
	 */
	@Override
	public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
		logger.info("SessionControlActivity starting( token=" + token);
		panel.setWidget(view);
		// ensure not busy
		this.busy = false;
		try {
			// convert token to Long id
			final Long id = Long.valueOf(token);
			// obtain context
			final SessionRequestContext findSessionContext = requestFactory
					.sessionRequestContext();
			final Request<SessionProxy> req = findSessionContext
					.findSession(id);
			req.with("member1", "member2", "member1.user", "member2.user",
					"member1.course", "member1.course.owner", "member2.course",
					"member2.course.owner");
			req.fire(new Receiver<SessionProxy>() {

				/*
				 * (non-Javadoc)
				 * 
				 * @see com.google.web.bindery.requestfactory.shared.Receiver#
				 * onConstraintViolation(java.util.Set)
				 */
				@Override
				public void onConstraintViolation(
						final Set<ConstraintViolation<?>> errors) {
					//
					final StringBuffer msg = new StringBuffer(
							"Incomplete or errors found. Please correct and resubmit.");
					for (final ConstraintViolation<?> vio : errors) {
						msg.append("\n* ");
						msg.append(vio.getMessage());
					}
					Window.alert(msg.toString());
					super.onConstraintViolation(errors);
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see
				 * com.google.web.bindery.requestfactory.shared.Receiver#onFailure
				 * (com.google.web.bindery.requestfactory.shared.ServerFailure)
				 */
				@Override
				public void onFailure(final ServerFailure error) {
					//
					Window.alert("Server communication error "
							+ error.getMessage());
				}

				@Override
				public void onSuccess(final SessionProxy response) {
					// Long uid = currentState.getLoggedInUser().getId();
					// clear the view
					view.clear();
					// working on this course
					session = response;
					final Long uid = currentState.getLoggedInUser().getId();
					// test for success
					if (response == null) {
						logger.severe("Session id not found: " + id.toString());
						Window.alert("Bad session id");
						placeController.goTo(currentState.getHomePlace());

					} else if (!session.getMember1().getUser().getId()
							.equals(uid)
							&& // member 1 user
							!session.getMember2().getUser().getId().equals(uid)
							&& // member 2 user
							!session.getMember1().getCourse().getOwner()
									.getId().equals(uid) && // member 1 course
															// owner
							!session.getMember2().getCourse().getOwner()
									.getId().equals(uid) && // member 2 course
															// owner
							!currentState.getLoggedInUser().getUserType()
									.equals(UserType.ADMIN)) {
						// access violation
						logger.warning("Access Violation - by:"
								+ currentState.getLoggedInUser().getId()
								+ " attempt session id=" + response.getId());
						Window.alert("Access Violation: You are not authorized to view this session.");
						placeController.goTo(currentState.getHomePlace());

					} else if (response.getNextSessionId().compareTo(0L) > 0
							&& Window
									.confirm("Next session found, forward to it?")) {
						// next session is register so go to it.
						placeController.goTo(new SessionControlPlace(response
								.getNextSessionId()));

					} else {
						// show partner invite in editor
						logger.info("Displaying session - member1 id="
								+ response.getMember1().getId()
								+ ", member2 id="
								+ response.getMember2().getId());
						sessionProxyDriver.display(session);

						logger.info("session start time="
								+ session.getStartTime() + " stop time="
								+ session.getStopTime() + " duration="
								+ session.getDurationMinutes().toString());

						setViewMode(session);
						view.showNotes(session.getNotes());
						fetchMember1ContactInfo(session);
						fetchMember2ContactInfo(session);
					}

				}
			});

		} catch (final NumberFormatException e) {
			//
			logger.severe("bad token not a course link id: " + token);
			Window.alert("Bad or unknown course link id");
			placeController.goTo(currentState.getHomePlace());
		}
	}

	/**
	 * from a stopped session, user can start a new session in their desired
	 * language
	 */
	@Override
	public void startNewSession(final LanguageType sessionLang) {
		// firewall multiple clicks
		if (busy) {
			logger.info("ignored duplicate start session " + sessionLang);
			return;
		}
		// mark as busy
		busy = true;

		//
		logger.info("startNewSession( " + sessionLang.name() + " ) ");
		// if this is member 1 - then set start member to 1 else set to 2
		final boolean isMember1Viewing = session.getMember1().getUser().getId()
				.equals(currentState.getLoggedInUser().getId());
		Integer startMember = 2;
		if (isMember1Viewing) {
			startMember = 1;
		}

		final SessionRequestContext startNewSessionContext = requestFactory
				.sessionRequestContext();
		final Request<SessionProxy> req = startNewSessionContext
				.createFromSession(session, sessionLang, startMember);
		// req.with("member1", "member2", "member1.user", "member2.user",
		// "member1.course", "member1.course.owner", "member2.course",
		// "member2.course.owner");
		req.to(new Receiver<SessionProxy>() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see com.google.web.bindery.requestfactory.shared.Receiver#
			 * onConstraintViolation(java.util.Set)
			 */
			@Override
			public void onConstraintViolation(
					final Set<ConstraintViolation<?>> errors) {
				//
				final StringBuffer msg = new StringBuffer(
						"Incomplete or errors found. Please correct and resubmit.");
				for (final ConstraintViolation<?> vio : errors) {
					msg.append("\n* ");
					msg.append(vio.getMessage());
				}
				Window.alert(msg.toString());
				super.onConstraintViolation(errors);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * com.google.web.bindery.requestfactory.shared.Receiver#onFailure
			 * (com.google.web.bindery.requestfactory.shared.ServerFailure)
			 */
			@Override
			public void onFailure(final ServerFailure error) {
				//
				Window.alert("Server communication error " + error.getMessage());
			}

			@Override
			public void onSuccess(final SessionProxy response) {
				logger.info("startNewSession( ) success with id="
						+ response.getId().toString());
				//
				placeController.goTo(new SessionControlPlace(response));
			}
		}).fire();
	}

	/**
	 * stop the session
	 */
	@Override
	public void stopSession() {
		// firewall multiple clicks
		if (busy) {
			logger.info("ignored duplicate stop session id" + session.getId());
			return;
		}
		// mark as busy
		busy = true;
		logger.info("stopSession(" + session.getId() + ") ");

		final SessionRequestContext stopSessionContext = requestFactory
				.sessionRequestContext();
		stopSessionContext
				.stopSession(session)
				.with("member1", "member2", "member1.user", "member2.user",
						"member1.course", "member1.course.owner",
						"member2.course", "member2.course.owner")
				.to(new Receiver<SessionProxy>() {

					/*
					 * (non-Javadoc)
					 * 
					 * @see
					 * com.google.web.bindery.requestfactory.shared.Receiver
					 * #onConstraintViolation(java.util.Set)
					 */
					@Override
					public void onConstraintViolation(
							final Set<ConstraintViolation<?>> errors) {
						//
						final StringBuffer msg = new StringBuffer(
								"Incomplete or errors found. Please correct and resubmit.");
						for (final ConstraintViolation<?> vio : errors) {
							msg.append("\n* ");
							msg.append(vio.getMessage());
						}
						Window.alert(msg.toString());
						super.onConstraintViolation(errors);
					}

					/*
					 * (non-Javadoc)
					 * 
					 * @see
					 * com.google.web.bindery.requestfactory.shared.Receiver
					 * #onFailure
					 * (com.google.web.bindery.requestfactory.shared.ServerFailure
					 * )
					 */
					@Override
					public void onFailure(final ServerFailure error) {
						//
						Window.alert("Server communication error "
								+ error.getMessage());
					}

					@Override
					public void onSuccess(final SessionProxy session) {
						//
						sessionProxyDriver.display(session);
						logger.info("stopSession() success: start time="
								+ session.getStartTime() + " stop time="
								+ session.getStopTime() + " duration="
								+ session.getDurationMinutes().toString());
						setViewMode(session);
					}

				});
		stopSessionContext.fire();
	}

	/**
	 * for active sessions only swap the languages by - stopping the current
	 * session and starting a new session
	 */
	@Override
	public void swapLanguages() {
		//
		logger.info("swapLanguages() ");
		if (isStarted(session)) {
			startNewSession(session.getOtherLanguage());
		}

	}
}
