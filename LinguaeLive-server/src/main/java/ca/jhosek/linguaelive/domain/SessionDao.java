/**
 * 
 */
package ca.jhosek.linguaelive.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

//import com.google.appengine.api.channel.ChannelMessage;
//import com.google.appengine.api.channel.ChannelService;
//import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.googlecode.objectify.Key;

import ca.jhosek.linguaelive.LanguageType;
// import ca.jhosek.linguaelive.proxy.SessionProxy;
// import ca.jhosek.linguaelive.proxy.SessionRequestContext;
import static com.googlecode.objectify.ObjectifyService.ofy;
/**
 * Session object data access layer
 * 
 * @author copyright (C) 2011-2014 Andrew Stevko
 * 
 * @see SessionRequestContext
 * @see SessionProxy
 * @see Session
 */
public class SessionDao extends ObjectifyGenericDao<Session> {

	/**
	 * ChannelService is used to PUSH model changes to interested clients
	 */
//	private static final ChannelService channelService = ChannelServiceFactory
//			.getChannelService();

	/**
	 * compare two sessions by timestamp, used to sort lists
	 * 
	 * @author andy
	 * 
	 */
	private static Comparator<Session> COMPARATOR = new Comparator<Session>() {

		@Override
		public int compare(final Session o1, final Session o2) {
			try {
				return o1.getStartTime().compareTo(o2.getStartTime());
			} catch (final NullPointerException npe) {
				return 0;
			}
		}
	};

	private static final Logger logger = Logger.getLogger(SessionDao.class
			.getName());

	/**
	 * append a note to this session
	 * 
	 * @param sessionId
	 *            this session
	 * @param note
	 *            chat to append
	 * @return the altered session
	 */
	public Session addNote(final User myUser, final Long sessionId,
			final String note) {
		final Session session = findSession(sessionId);
		if (session == null) {
			return null;
		}
		session.addNote(note);
		ofy().save().entity(session).now();

		final MemberDao memberDao = new MemberDao();
		final UserDao userDao = new UserDao();
		try {
			Member m1;
			User u1;
			try {
				m1 = session.getMember1();

			} catch (final EntityNotFoundException e) {
				m1 = memberDao.get(session.getMember1Key());
			}
			try {
				u1 = m1.getUser();

			} catch (final EntityNotFoundException e) {
				u1 = userDao.get(m1.getUserKey());
			}
			// -----------
			// send to member2
			if (u1.equals(myUser)) {
				// ------------
				// send notification to session member2 user
				Member m2;
				User u2;
				try {
					m2 = session.getMember2();

				} catch (final EntityNotFoundException e) {
					m2 = memberDao.get(session.getMember2Key());
				}
				try {
					u2 = m2.getUser();

				} catch (final EntityNotFoundException e) {
					u2 = userDao.get(m2.getUserKey());
				}
				sendSessionNoteNotification(u2, session, note);

			} else {
				// ------------
				// send notification to session member1 user
				sendSessionNoteNotification(u1, session, note);

			}
		} catch (final EntityNotFoundException e) {
			logger.warning("member key not found");
		}
		return session;
	}

	/**
	 * cancel this session
	 * 
	 * @param cancelSession
	 *            the session to cancel
	 * @return cancelled session
	 */
	public Session cancelSession(final Session cancelSession) {
		// cancel the session
		cancelSession.setCancelled(true);
		cancelSession.setStopTime(new Date());
		put(cancelSession);

		// send notification for this session changing
		sendNotification(cancelSession);

		return cancelSession;

	}

	/**
	 * update members totals with the session minutes closed
	 * 
	 * @param putSession
	 *            the session to close
	 */
	private void closeSessionMinutes(final Session putSession) {
		if (!putSession.getCancelled() && putSession.getDurationMinutes() > 0L) {
			// closing the duration of the session
			final MemberDao memberDao = new MemberDao();
			final CourseDao courseDao = new CourseDao();
			// -----------------------------
			// update member 1 totals
			boolean isM1TargetLanguage = false;
			Member m1;
			Course c1;
			try {
				m1 = memberDao.get(putSession.getMember1Key());
				c1 = courseDao.get(m1.getCourseKey());
				isM1TargetLanguage = putSession.getSessionLanguage().equals(
						c1.getTargetLanguage());
				m1.addSessionMinutes(isM1TargetLanguage,
						putSession.getDurationMinutes());
				memberDao.put(m1);
			} catch (final EntityNotFoundException e) {
				logger.warning("Invalid Session Member Key encountered while adding close session minutes");
			}
			// -----------------------------
			// update member 2 totals
			Member m2;
			try {
				m2 = memberDao.get(putSession.getMember2Key());
				m2.addSessionMinutes(!isM1TargetLanguage,
						putSession.getDurationMinutes());
				memberDao.put(m2);
			} catch (final EntityNotFoundException e) {
				logger.warning("Invalid Session Member Key encountered while adding close session minutes");
			}
		} else {
			// session is cancelled therefore do not close the session minutes
		}
	}

	/**
	 * easiest way to create a session - use the info from an invite
	 * 
	 * @param invite
	 *            to spawn session from
	 * @return a new session completely filled out from the invite & started
	 */
	public Session createFromInvite(final SessionInvite invite,
			final LanguageType sessionLang, final Integer startMember) {
		final Date startTime = new Date();

		// create new session with info from invite and set langage
		final Session session = new Session(invite, sessionLang);
		// start the session start time
		session.setStartTime(startTime);
		// set the session start member
		session.setStartMember(startMember);
		// everything must have an id.
		ofy().save().entity(session).now();

		// send notification for this session changing
		sendNotification(session);

		// return
		return session;
	}

	/**
	 * create a new session from a previous session but with a difference
	 * language
	 * 
	 * @param previousSession
	 *            old session
	 * @param sessionLang
	 *            new language
	 * @param startMember
	 *            which member is starting this session
	 * @return a new session copied from the previous session
	 */
	public Session createFromSession(final Session previousSession,
			final LanguageType sessionLang, final Integer startMember) {
		final Session session = new Session(previousSession, sessionLang);
		final Date now = new Date();
		// start the session start time
		session.setStartTime(now);
		// set the session start member
		session.setStartMember(startMember);
		// everything must have an id.
		ofy().save().entity(session).now().getId();	// populates session.id

		// update previous session with next session id
		previousSession.setNextSession(session);
		previousSession.setStopTime(now);
		closeSessionMinutes(previousSession);

		put(previousSession);

		// send notification for this session changing
		sendNotification(session);

		// return new session
		return session;
	}

	/**
	 * delete the sessions for this member - not friendly to other members :(
	 * 
	 * @param member
	 *            in focus
	 */
	public void deleteSessionsForMember(final Member member) {
		//
		ofy().delete().entities(getSessionsForMember(member));
	}

	/**
	 * simple file a session by ID
	 * 
	 * @param id
	 *            the id
	 * @return the session or NULL
	 */
	public Session findSession(final Long id) {
		logger.info("findSession( " + id.toString() + " )");
		try {
			return this.get(id);
		} catch (final EntityNotFoundException e) {
			logger.severe("Session id=" + id + " not found!");
			return null;
		}
	}

	/**
	 * lookup all open/runaway sessions for this member
	 * 
	 * @param member
	 *            in question
	 * @return sorted OPEN sessions for this Member
	 */
	public List<Session> getOpenSessionsForMember(final Member member) {
		logger.info("getSessionsForMember( id=" + member.getId() + " ) ");

		final List<Session> sessions = new ArrayList<Session>();
		final MemberDao memberDao = new MemberDao();
		final Key<Member> mKey = memberDao.key(member);

		sessions.addAll(ofy().load().type(Session.class).filter("stopTime =", null)
				.filter("member1Key =", mKey).list());
		sessions.addAll(ofy().load().type(Session.class).filter("stopTime =", null)
				.filter("member2Key =", mKey).list());

		logger.info("getSessionsForMember() found " + sessions.size()
				+ " sessions ");
		return sessions;
	}

	/**
	 * get open sessions for this user (just like getOpenSessionsForMember())
	 * used on dashboard and to redirect user to sessions in progress
	 * 
	 * @param user
	 *            in focus
	 * @return list of open user member sessions (should be only one)
	 */
	public List<Session> getOpenSessionsForUser(final User user) {
		logger.info("getOpenSessionsForUser( id=" + user.getId() + ")");
		final List<Session> openSessions = new ArrayList<Session>();

		// get all member for user
		final MemberDao memberDao = new MemberDao();
		final List<Member> members = memberDao.getMembersOfUser(user);

		// for every member
		for (final Member m : members) {
			// get all open sessions
			openSessions.addAll(getOpenSessionsForMember(m));
		}
		return openSessions;
	}

	/**
	 * used by instructor to get all sessions for this course
	 * 
	 * @param course
	 *            in focus
	 * @return sessions for this course
	 */
	public List<Session> getSessionsForCourse(final Course course) {
		logger.info("getSessionsForCourse( id=" + course.getId() + " ) ");

		// list of sessions to return
		final List<Session> sessions = new ArrayList<Session>();

		// retrieve members of this course
		final MemberDao memberDao = new MemberDao();
		final List<Member> courseMembers = memberDao.getMembersOfCourse(course);
		if (courseMembers != null) {
			for (final Member member : courseMembers) {
				// retrieve sessions of this member
				sessions.addAll(getSessionsForMember(member));
			}
		}

		logger.info("getSessionsForCourse() found " + sessions.size()
				+ " sessions ");
		return sessions;
	}

	/**
	 * lookup all non-cancelled sessions for this member
	 * 
	 * @param member
	 *            in question
	 * @return sorted sessions for this Member
	 */
	public List<Session> getSessionsForMember(final Member member) {
		logger.info("getSessionsForMember( id=" + member.getId() + " ) ");

		final List<Session> sessions = new ArrayList<Session>();
		final MemberDao memberDao = new MemberDao();
		final Key<Member> mKey = memberDao.key(member);

		sessions.addAll(ofy().load().type(Session.class).filter("member1Key =", mKey)
				.filter("cancelled", false).list());
		sessions.addAll(ofy().load().type(Session.class).filter("member2Key =", mKey)
				.filter("cancelled", false).list());
		Collections.sort(sessions, COMPARATOR);

		logger.info("getSessionsForMember() found " + sessions.size()
				+ " sessions ");
		return sessions;
	}

	/**
	 * make this session entity persistent
	 * 
	 * @param contactInfo
	 */
	public void persist(final Session putSession) {
		logger.info("persist() putting Session id = " + putSession.getId());

		// ----------------
		this.put(putSession);

		// send notification for this session changing
		sendNotification(putSession);
	}

	/**
	 * way to remove an entity
	 * 
	 * @param entity
	 *            entity to remove
	 */
	public void remove(final Session entity) {
		logger.info("remove() ");
		this.delete(entity);
	}

	/**
	 * given a new entity - save it and return generated entity
	 * 
	 * @param newEntity
	 *            w/ null id
	 * @return entity with generated id
	 * @throws EntityNotFoundException
	 *             can only happen if not saved
	 */
	public Session saveAndReturn(final Session newEntity)
			throws EntityNotFoundException {
		logger.info("saveAndReturn() ");
		final Key<Session> newKey = this.put(newEntity);
		return this.get(newKey);
	}

	/**
	 * send a notification to both users of a Session Update proceeded by a user
	 * status update
	 * 
	 * @param session
	 *            that is updated
	 * @see ChannelService
	 */
	private void sendNotification(final Session session) {
		// each status is broadcasted to both users to keep browsers insync
		String statusMessage;
		if (session.isActive()) {
			statusMessage = "INSESSION|" + session.getId();
		} else {
			statusMessage = "ONLINE";
		}

		final MemberDao memberDao = new MemberDao();
		final UserDao userDao = new UserDao();
		// ------------
		// send notification to session member1 user
		try {
			Member m1;
			User u1;
			try {
				m1 = session.getMember1();

			} catch (final EntityNotFoundException e) {
				m1 = memberDao.get(session.getMember1Key());
			}
			try {
				u1 = m1.getUser();

			} catch (final EntityNotFoundException e) {
				u1 = userDao.get(m1.getUserKey());
			}
//			u1.setOnlineStatus(statusMessage);
//			sendSessionUpdateNotification(u1, session);

		} catch (final EntityNotFoundException x) {
			logger.warning("member 1 or user not found for session");
		}

		// ------------
		// send notification to session member2 user
		try {
			Member m2;
			User u2;
			try {
				m2 = session.getMember2();

			} catch (final EntityNotFoundException e) {
				m2 = memberDao.get(session.getMember2Key());
			}
			try {
				u2 = m2.getUser();

			} catch (final EntityNotFoundException e) {
				u2 = userDao.get(m2.getUserKey());
			}
//			u2.setOnlineStatus(statusMessage);
//			sendSessionUpdateNotification(u2, session);

		} catch (final EntityNotFoundException x) {
			logger.warning("member 2 or user not found for session");
		}
	}

	/**
	 * use the channel service to send note updates to this user
	 * 
	 * @param user
	 *            user to receive event message
	 * @param session
	 *            session infocus
	 * @param note
	 *            new note
	 */
	private void sendSessionNoteNotification(final User user,
			final Session session, final String note) {
		if (user == null) {
			logger.warning("send notification user == null");
		} else if (session == null) {
			logger.warning("send notification session == null");
		} else {
			logger.info("sending sessionChat notification to user ="
					+ user.getEmailAddress() + " for session id="
					+ session.getId());
//			channelService.sendMessage(new ChannelMessage(user
//					.getEmailAddress(), "sessionChat|" + session.getId() + "|"
//					+ note + "|" + new Date().getTime()));
		}
	}

	/**
	 * use the channel service to send session update event to this user
	 * 
	 * @param user
	 *            user to receive event message
	 * @param session
	 *            session infocus
	 */
	private void sendSessionUpdateNotification(final User user,
			final Session session) {
		if (user == null) {
			logger.warning("send notification user == null");
		} else if (session == null) {
			logger.warning("send notification session == null");
		} else {
			logger.info("sending session notification to user ="
					+ user.getEmailAddress() + " for session id="
					+ session.getId());
//			channelService.sendMessage(new ChannelMessage(user
//					.getEmailAddress(), "session|" + session.getId() + "|"
//					+ new Date().getTime()));
		}
	}

	/**
	 * stop this session
	 * 
	 * @param stopSession
	 *            the session to stop
	 * @return session stopped
	 */
	public Session stopSession(final Session stopSession) {
		final Date stopDate = new Date();
		stopSession.setStopTime(stopDate);
		put(stopSession);
		// close out the session minutes
		closeSessionMinutes(stopSession);

		// send notification for this session changing
//		sendNotification(stopSession);

		return stopSession;

	}

}