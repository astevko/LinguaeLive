/**
 * 
 */
package ca.jhosek.main.server.domain;

import java.util.ArrayList;
//import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

//import com.google.appengine.api.channel.ChannelMessage;
//import com.google.appengine.api.channel.ChannelService;
//import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.googlecode.objectify.Key;

import ca.jhosek.main.server.email.PartnerInviteEmail;
import ca.jhosek.main.server.email.StudentPairingUserAcceptEmail;
import ca.jhosek.main.server.email.StudentPairingUserDeclinedEmail;
import ca.jhosek.main.shared.proxy.PartnerInviteRequestContext;
import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Session Invitation (invite to link two members)
 * closely related to Session
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see SessionInvite
 * @see PartnerInviteRequestContext
 */
public class SessionInviteDao extends ObjectifyGenericDao<SessionInvite> {

	// Inherit all methods from generic Dao


	private static final Logger logger = Logger.getLogger( SessionInviteDao.class.getName());
//	public static final ChannelService channelService = ChannelServiceFactory.getChannelService();


	public SessionInvite findSessionInvite( Long id ){
		logger.info( "findSessionInvite( " + id.toString() + " )");
		try {
			return this.get(id);			
		} catch( EntityNotFoundException e ) {
			logger.severe("SessionInvite id=" + id + " not found!");
			return null;
		}
	}

	public SessionInvite saveAndReturn( SessionInvite newEntity ) throws EntityNotFoundException {
		logger.info( "saveAndReturn() ");
		Key<SessionInvite> newKey = this.put(newEntity);
		return this.get(newKey);
	}

	public void remove(SessionInvite entity){
		logger.info( "remove() ");
		this.delete(entity);
	}

	/**
	 * @param member
	 * @return SessionInvites for this Member
	 */
	public List<SessionInvite> getSessionInvitesForMember( final Member member ) {
		logger.info( "getSessionInvitesForMember( id=" + member.getId() +	" )");
		final MemberDao memberDao = new MemberDao();
		Key<Member> mKey = memberDao.key(member);
		final List<SessionInvite> sessions = new ArrayList<SessionInvite>();
		sessions.addAll( ofy().load().type(SessionInvite.class).filter("member1Key =", mKey ).list() );
		sessions.addAll( ofy().load().type(SessionInvite.class).filter("member2Key =", mKey ).list() );
		logger.info( "getSessionInvitesForMember() found " + sessions.size() +	" session invites ");
		return sessions;
	}


	/**
	 * @param member
	 * @return PENDING SessionInvites for this Member
	 */
	public List<SessionInvite> getPendingSessionInvitesForMember( final Member member ) {
		logger.info( "getPendingSessionInvitesForMember( id=" + member.getId() +	" )");
		final MemberDao memberDao = new MemberDao();
		final Key<Member> mKey = memberDao.key(member);
		final List<SessionInvite> sessionInvites = new ArrayList<SessionInvite>();
		sessionInvites.addAll( ofy().load().type(SessionInvite.class).filter("pending", true ).filter("member1Key =", mKey ).list() );
		sessionInvites.addAll( ofy().load().type(SessionInvite.class).filter("pending", true ).filter("member2Key =", mKey ).list() );
		logger.info( "getSessionInvitesForMember() found " + sessionInvites.size() +	" session invites ");
		return sessionInvites;
	}

	/**
	 * make this sessionInvite entity persistent
	 * 
	 * @param contactInfo
	 */
	public void persist( SessionInvite sessionInvite ) {
		logger.info( "persist() putting SessionInvite id = " + sessionInvite.getId() );
		//    	boolean newEntity = (sessionInvite.getId() == null);    	

		final MemberDao memberDao = new MemberDao();
		final CourseDao courseDao = new CourseDao();
		try {
			final Member member1 = memberDao.get(sessionInvite.getMember1Key());
			final Member member2 = memberDao.get(sessionInvite.getMember2Key());
			// if this invitation accepted == true and pending == false
			if (sessionInvite.getAccepted() && !sessionInvite.getPending()) {

				//----------------------
				if (!member1.getAvailable()) { // only happens if both are available
					sessionInvite.setAccepted(false);
					// NEW send email accepting invite to member1 and member2
					try {
						final String msg = "Invite declined because " + member1.getUser().getFirstName() + " is no longer available.";
						new StudentPairingUserDeclinedEmail( sessionInvite,  member2, member1, msg).send();
					} catch (EntityNotFoundException e) {
						// 
						logger.severe("failed to send session invite accept email" + e.getMessage() );
					}    	

					//----------------------
				} else if (!member2.getAvailable()) { // only happens if both are available
					sessionInvite.setAccepted(false);
					// NEW send email accepting invite to member1 and member2
					try {
						final String msg = "Invite declined because " + member2.getUser().getFirstName() + " is no longer available.";
						new StudentPairingUserDeclinedEmail( sessionInvite,  member1, member2, msg).send();
					} catch (EntityNotFoundException e) {
						// 
						logger.severe("failed to send session invite accept email" + e.getMessage() );
					}    	

					//----------------------
				} else {
					//----------------------
					// course 1
					final Course course1 = courseDao.get(member1.getCourseKey());
					// check to see each course single partner preferred flag = true
					if (course1.getSinglePartnerPreferred()) {
						// then update member available = false   		
						member1.setAvailable(false);
						memberDao.persist(member1);					
						// LL-10 close out member's other invitations
						final List<SessionInvite> m1Invites = getSessionInvitesForMember( member1 );
						for (SessionInvite invite : m1Invites) {
							// skip this and nonPending invites 
							if (!invite.getId().equals(sessionInvite.getId()) && invite.getPending()) {
								// decline/cancel other non-pending invites
								invite.setAccepted(false);
								invite.setPending(false);
								this.put(invite);
							}
						}
					}
					//----------------------
					// course 2
					final Course course2 = courseDao.get(member2.getCourseKey());
					// check to see each course single partner preferred flag = true
					if (course2.getSinglePartnerPreferred()) {
						// then update member available = false   		
						member2.setAvailable(false);
						memberDao.persist(member2);
						// LL-10 close out member's other invitations
						final List<SessionInvite> m2Invites = getSessionInvitesForMember( member2 );
						for (SessionInvite invite : m2Invites) {
							// skip this and nonPending invites 
							if (!invite.getId().equals(sessionInvite.getId()) && invite.getPending()) {
								// NEW cancel decline other non-pending invites
								invite.setAccepted(false);
								invite.setPending(false);
								this.put(invite);
							}
						}
						// NEW send email accepting invite to member1 and member2
						try {
							new StudentPairingUserAcceptEmail( sessionInvite,  member1, member2).send();
						} catch (EntityNotFoundException e) {
							// 
							logger.severe("failed to send session invite accept email" + e.getMessage() );
						}    	
						try {
							new StudentPairingUserAcceptEmail( sessionInvite,  member2, member1).send();
						} catch (EntityNotFoundException e) {
							// 
							logger.severe("failed to send session invite accept email" + e.getMessage() );
						}    	

					}
				}
			//-------------------------
			} else  if (!sessionInvite.getAccepted() && !sessionInvite.getPending()) {
				// declined and not pending either..
				// NEW send email decline invite to member 1
				try {
					new StudentPairingUserDeclinedEmail( sessionInvite,  member2, member1).send();
				} catch (EntityNotFoundException e) {
					// 
					logger.severe("failed to send session invite accept email" + e.getMessage() );
				}    	

			}
			
			// save session invite no matter what...
			this.put(sessionInvite);
			
		} catch (EntityNotFoundException e) {
			logger.warning("Member or Course missing key");
		}

	}

	/**
	 * Create an invite from Member invitor to Member invitee  with this personal message.
	 * LL-138 Needs to check for existing SessionInvite from invitee to invitor
	 * 
	 * @see <a href="http://jira.stevkocyberservices.net:8080/browse/LL-138> LL-138 </a>
	 * 
	 * @param invitor
	 * @param invitee
	 * @param personalMessage
	 * @return the invite
	 */
	public SessionInvite sendInviteToMember( Member invitor, Member invitee, String personalMessage) {
		logger.info( "sendInviteToMember()" );
		// LL-138 check for existing invites - if found then simply confirm it.
		// TODO

		SessionInvite sessionInvite = new SessionInvite(invitor, invitee, personalMessage );
		Key<SessionInvite> key = this.put(sessionInvite);

		sessionInvite.setId( key.getId() );
		// send email to other student
		try {
			new PartnerInviteEmail( sessionInvite, personalMessage ).send();
		} catch (EntityNotFoundException e) {
			// 
			logger.severe("failed to send session invite email" + e.getMessage() );
		}    	
		return (ofy().load().key(key).now());
	}

	/**
	 * @param user
	 * @return pending session invites for this user
	 */
	public List<SessionInvite> getPendingSessionInvitesForUser(	User user) {
		logger.info( "getPendingSessionInvitesForUser( id=" + user.getId() +	")" );
		List<SessionInvite> pendingSessionInvites = new ArrayList<SessionInvite>();

		// get all member for user
		MemberDao memberDao = new MemberDao();
		List<Member> members = memberDao.getMembersOfUser(user);

		// for every member
		for (Member m : members ) {
			// 	get all pending sessions
			pendingSessionInvites.addAll( getPendingSessionInvitesForMember(m));
		}

		return pendingSessionInvites;
	}


	/**
	 * @param member
	 */
	public void deleteSessionInvitesForMember( Member member ) {
		logger.info( "deleteSessionInvitesForMember( id=" + member.getId() +	" )");
		MemberDao memberDao = new MemberDao();
		Key<Member> mKey = memberDao.key(member);
		ofy().delete().entities( ofy().load().type(SessionInvite.class).filter("member1Key =", mKey ).list() );
		ofy().delete().entities(  ofy().load().type(SessionInvite.class).filter("member2Key =", mKey ).list() );
		logger.info( "deleteSessionInvitesForMember() complete ");
	}

	/**
	 * open the session invite page for this user
	 * 
	 * @param forThisUser
	 * @param sessionInvite
	 */
	public void openSessionInvite(final User forThisUser, final SessionInvite sessionInvite) {
		sendNotification(forThisUser, sessionInvite);
	}

	/**
	 * send session invite id to other user to indicate they should be ready to start.
	 * 
	 * @param user  	invite.getMember2()
	 * @param sessionId session.getId()
	 */
	private void sendNotification(final User user, final SessionInvite sessionInvite) {
		if (user == null) {
			logger.warning("send notification user == null");
		} else if (sessionInvite == null) {
			logger.warning("send notification sessionInvite == null");
		} else {
			logger.info("sending notification to user <" + user.getEmailAddress() + "> for sessionInvite id=" + sessionInvite.getId() );
//			channelService.sendMessage(new ChannelMessage(user.getEmailAddress(), "sessionInvite|" + sessionInvite.getId() + "|" + new Date().getTime()));
		}
	}
}
