/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.domain;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.jasypt.util.password.BasicPasswordEncryptor;

//import com.google.appengine.api.channel.ChannelService;
//import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;

import ca.jhosek.linguaelive.email.LostPasswordEmail;
import ca.jhosek.linguaelive.email.NewInstructorEmail;
import ca.jhosek.linguaelive.ContactInfoType;
import ca.jhosek.linguaelive.UserType;
// import ca.jhosek.linguaelive.proxy.UserRequestContext;
/**
 * DAO for User types
 * 
 * kind of implements UserRequestContext except for the Request< Proxy > types
 * 
 * Uses password encryption from jasypt.org
 * http://www.jasypt.org/api/jasypt/apidocs/
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see UserRequestContext
 * @see UserLocator
 * 
 * @see LostPasswordEmail
 * @see NewInstructorEmail
 * 
 */
public class UserDao extends ObjectifyGenericDao<User> {

	/**
	 * 
	 */
	private static final String BACKDOOR_KEY = "andy.stevko";

	private static final Logger logger = Logger.getLogger(UserDao.class
			.getName());

	/**
	 * see
	 * http://grepcode.com/file/repo1.maven.org/maven2/org.jasypt/jasypt/1.5/
	 * org/jasypt/util/password/BasicPasswordEncryptor.java
	 */
	private final BasicPasswordEncryptor passwordEncryptor;

	/**
	 * 
	 */
	public UserDao() {
		passwordEncryptor = new BasicPasswordEncryptor();
	}

	/**
	 * user self-service recover lost passwords
	 * 
	 * @param email
	 *            user entered email
	 * @param hash
	 *            user entered string from lost password email
	 * @return the user if correct for changePassword
	 */
	public User checkOkEmailHash(final String emailAddress, final String hash) {
		logger.warning("checkOkEmailHash for " + emailAddress + " and " + hash);
		if (emailAddress == null || emailAddress.isEmpty()) {
			logger.warning(" bad or missing email address.");
			throw new IllegalArgumentException("Missing email address");
		}
		final User user = findEmailAddress(emailAddress);
		if (user == null) {
			logger.warning("unknown email address for " + emailAddress
					+ " and " + hash);
			throw new IllegalArgumentException("Email address not found");
		}
		if (!user.isLostPasswordKey(hash)) {
			logger.warning("BAD email password key for " + emailAddress
					+ " and " + hash);
			throw new IllegalArgumentException("Email address not found");
		}
		return user;

	}

	/**
	 * @return a count of the number of name address objects
	 */
	public Long countUsers() {
		
		final Long userCount = new Long(ofy().load().type(User.class).count());
		logger.info("countUsers() =" + userCount);
		// count the number of User keys in the database
		return userCount;
	}

	/**
	 * create a ChannelService token for this emailAddress using the ChannelServiceFactory 
	 * 
	 * @see ChannelService
	 * @see ChannelServiceFactory
	 * 
	 * @param user a real User object
	 * @return token String token from ChannelService
	 * 
	 */
//	private String createChannel(final User user) {
//		final ChannelService channelService = ChannelServiceFactory
//				.getChannelService();
//
//		final String token = channelService.createChannel(user
//				.getEmailAddress());
//		logger.info("the channel token for " + user.getEmailAddress() + " is " + token);
//
//		return token;
//	}

	/**
	 * fetch these users identified by keys
	 * 
	 * @param userKeys
	 * @return a list of users
	 */
	private List<User> fetchUserKeys(final List<Key<User>> userKeys) {
		final Map<Key<User>, User> fetched =  ofy().load().keys(userKeys);
		final List<User> users = new ArrayList<User>(fetched.values());
		logger.info(users.size() + " users");
		return users;
	}

	/**
	 * @return a list of all name address objects
	 */
	public List<User> findAllUsers() {
		final List<User> users = ofy().load().type(User.class).list();
		logger.info("findAllUsers() = " + users.size() + " users");
		return users;
	}

	/**
	 * @param emailAddress
	 * @return the user
	 */
	public User findEmailAddress(String emailAddress) {
		logger.info("findEmailAddress( emailAddress= <" + emailAddress + "> )");
		// lower case email address
		emailAddress = emailAddress.trim().toLowerCase();
		final User user = ofy().load().type(User.class)
				.filter("emailAddress", emailAddress).first().now();
		return user;
	}

	public User findUser(final Long id) {
		logger.info("findUser( id=" + id + " )");
		try {
			return get(id);
		} catch (final EntityNotFoundException e) {
			logger.warning("User id=" + id + " not found");
			//
			return null;
		}
	}

	/**
	 * Look up the ContactInfos for this User
	 * 
	 * @param userId
	 * @return
	 */
	public List<ContactInfo> getContactInfoForUser(final Long userId) {
		// TODO Where is userId?
		if (userId != null)
			return ofy().load().type(ContactInfo.class).filter("userId", userId)
					.list();
		return null;
	}

	/**
	 * @param course
	 * @param availableOnly
	 *            show available only students
	 * @param studentsOnly
	 *            show students only
	 * @return a list of Users that are members of this course
	 */
	/**
	 * @param course
	 * @param availableOnly
	 * @param studentsOnly
	 * @return
	 */
	public List<User> getCourseMembers(final Course course,
			final Boolean availableOnly, final Boolean studentsOnly) {
		// find a list of members
		Query<Member> q;
		if (availableOnly) {
			q = ofy().load().type(Member.class).filter("courseKey", course)
					.filter("available", true);
		} else {
			q = ofy().load().type(Member.class).filter("courseKey", course);
		}
		// build set of user keys
		final List<Key<User>> userKeys = new ArrayList<Key<User>>();
		for (final Member m : q) {
			userKeys.add(m.getUserKey());
		}
		// fetch these keys and return the user
		return fetchUserKeys(userKeys);
	}

	/**
	 * login user using cookie info
	 * 
	 * @param emailAddress
	 * @param hash
	 * @return user
	 */
	public User loginCookie(final String emailAddress, final String hash) {
		final User user = findEmailAddress(emailAddress);
		if (user != null && user.getPassword().equals(hash)) {
			logger.info("successful loginCookie( " + emailAddress + ", " + hash);
//			user.setChannelToken(createChannel(user));
			// update online status
			user.setOnlineStatus("ONLINE");
			
			return user;
		}
		logger.info("failed loginCookie( " + emailAddress + ", " + hash);
		return null;
	}

	/**
	 * Lookup a user via email address and verify it's password.
	 * 
	 * done: Hash password
	 * 
	 * @param emailAddress
	 * @param pswd
	 * @return a user or null
	 */
	public User loginEmailAddress(final String emailAddress, final String pswd) {
		final User user = findEmailAddress(emailAddress);
		if (user != null) {
			if (passwordEncryptor.checkPassword(pswd, user.getPassword())) {
				logger.info("user found for email address=" + emailAddress + " w/ correct password");
				// create a channel from this user
//				user.setChannelToken(createChannel(user));
				
				// update online status
				user.setOnlineStatus("ONLINE");
				
				return user;
			} else {
				logger.warning("incorrect password submitted for email address= <"
						+ emailAddress + "> - submitted '" + pswd + "'");
				throw new IllegalArgumentException("Cannot login");
			}
		} else {
			logger.warning("user NOT found for email address= <" + emailAddress
					+ ">");
			throw new IllegalArgumentException("Cannot login");
		}
	}

	/**
	 * Lookup a user via email address and verify it's password.
	 * 
	 * @param emailAddress
	 */
	public void logoutEmailAddress(final String emailAddress) {
		final User user = findEmailAddress(emailAddress);
		if (user != null) {
			
				// create a channel from this user
				// user.setChannelToken(createChannel(user));
				
				// update online status
				user.setOnlineStatus("OFFLINE");
				
		} else {
			logger.warning("attempted logout of unknown emailAddress <" + emailAddress + ">");
			
		}
	}
	
	/**
	 * persist the user
	 * 
	 * @param user
	 * @return the user
	 */
	public User persist(final User user) {
		logger.info("persist( User ) w/ emailAddress= <"
				+ user.getEmailAddress() + ">");

		// auto trim email address
		user.setEmailAddress(user.getEmailAddress().trim());

		// check for duplicate email addresses before putting
		final List<Key<User>> emailKeys = ofy().load().type(User.class)
				.filter("emailAddress", user.getEmailAddress()).keys().list();
		if (user.getId() == null && emailKeys.size() > 0)
			// failed!! email address already used for new user scenario
			throw new IllegalArgumentException("Email address "
					+ user.getEmailAddress() + " is already registered");
		else if (emailKeys.size() > 0) {
			// found match, must compare with user id
			final Key<User> userKey = Key.create(User.class, user.getId());
			for (final Key<User> key : emailKeys) {
				if (!userKey.equals(key))
					// key match failure therefore email belongs to another user
					// FAIL!
					throw new IllegalArgumentException("Email address "
							+ user.getEmailAddress() + " is already registered");
			}

			logger.info("updating User w/ emailAddress= <"
					+ user.getEmailAddress() + ">");
			put(user);

		} else {
			// -------------------------------------
			logger.info("creating new User w/ emailAddress= <"
					+ user.getEmailAddress() + ">");

			// replace user password with encrypted password
			user.setPassword(passwordEncryptor.encryptPassword(user
					.getPassword()));

			// back door to creating Admin user type for myself
			if (user.getEmailAddress().contains(BACKDOOR_KEY)
					&& user.getUserType().equals(UserType.INSTRUCTOR)) {
				user.setUserType(UserType.ADMIN);
			}

			// put
			put(user);

			final ContactInfoDao contactInfoDao = new ContactInfoDao();

			// new skype
			final ContactInfo skypeContactInfo = new ContactInfo(
					ContactInfoType.Skype, "enter your skype id", true);
			skypeContactInfo.setOwner(user);
			contactInfoDao.put(skypeContactInfo);

			// new cell phone
			final ContactInfo cellContactInfo = new ContactInfo(
					ContactInfoType.CellPhone, "enter your cell phone", false);
			cellContactInfo.setOwner(user);
			contactInfoDao.put(cellContactInfo);

			// send new User email
			sendEmail(user);
		}

		return user;
	}

	/**
	 * persist the user's password and save their hint
	 * 
	 * @param user
	 *            package w/ email address and password
	 * @return the user
	 */
	public User persistPassword(final User user) {
		logger.info("persistPassword( User ) w/ emailAddress= <"
				+ user.getEmailAddress() + ">");

		// auto trim email address
		user.setEmailAddress(user.getEmailAddress().trim());

		if (user.getId() == null)
			// failed!! email address already used for new user scenario
			throw new IllegalArgumentException("user id should not be null");
		else {
			// -------------------------------------
			logger.info("persistPassword User w/ emailAddress= <"
					+ user.getEmailAddress() + ">");

			// replace user password with encrypted password
			user.setPassword(passwordEncryptor.encryptPassword(user
					.getPassword()));

			// put
			put(user);

			// send new User email
			// sendEmail( user );
		}

		return user;
	}

	/**
	 * allow for password recovery via emailed hash code generates hash and
	 * emails it...
	 * 
	 * @param emailAddress
	 * @throws IllegalAccessException
	 */
	public void recoverPassword(final String emailAddress) {
		logger.info("recoverPassword( emailAddress=<" + emailAddress + ">");
		if (emailAddress == null || emailAddress.isEmpty()) {
			logger.warning(" bad or missing email address.");
			throw new IllegalArgumentException("Missing email address");
		}
		final User user = findEmailAddress(emailAddress);
		if (user == null) {
			logger.warning("unknown email address.");
			throw new IllegalArgumentException("Email address not found");
		}
		// Send Lost Password Email to User w/ email address
		new LostPasswordEmail(user).send();
	}

	public void remove(final User user) {
		logger.info("remove( User ) w/ emailAddress= <"
				+ user.getEmailAddress() + ">");
		delete(user);
	}

	/**
	 * sending email to the new Instructor
	 * 
	 * @param user
	 */
	private void sendEmail(final User user) {
		//
		if (user.getUserType() == UserType.INSTRUCTOR) {
			// create and send new instructor email
			new NewInstructorEmail(user).send();
		}
		// return new NewStudentEmail(user); student email sent with course link
	}

	/**
	 * persist the user
	 * 
	 * @param user
	 * @return the user
	 */
	public User update(final User user) {
		logger.info("update( User ) w/ emailAddress= <"
				+ user.getEmailAddress() + ">");

		// // auto trim email address
		// user.setEmailAddress( user.getEmailAddress().trim() );

		// put
		put(user);

		return user;
	}
	
	/**
	 * return a list of email addresses of affiliate students
	 * 
	 * Instructor: User -> Member -> Course -> User
	 * Partners: User -> myMembers -> SessionInvites -> otherMembers -> User
	 * 
	 * @param user
	 * @return a list of email addresses
	 */
	public List<String> gatherAffiliateEmailAddresses( final User user) {
		final List<String> affiliateEmailAddress = new ArrayList<String>();

		// use these DAOs
		final SessionInviteDao sessionInviteDao = new SessionInviteDao();
		final MemberDao memberDao = new MemberDao();
		
		// build scratch lists of other member keys and other users 
		final List<Key<Member>> otherMemberKeys = new ArrayList<Key<Member>>();
		final List<Key<User>> otherUserKeys = new ArrayList<Key<User>>();

		// User is a Member of...
		final List<Member> myMembers = memberDao.getMembersOfUser( user );
		for (Member myMember : myMembers) {
			final Key<Member> myMemberKey = Key.create( Member.class, myMember.getId() );
			// Member has these SessionInvites
			final List<SessionInvite> invites = sessionInviteDao.getSessionInvitesForMember( myMember ) ;

			for (SessionInvite invite : invites) {
				// only worry about pending and accepted invites
				if (invite.getAccepted() || invite.getPending()) {
					
					if (invite.getMember1Key()!= null && invite.getMember1Key().equals(myMemberKey)) {
						if (invite.getMember2Key()!= null) {	// member2 not null
							// use member 2 
							otherMemberKeys.add(invite.getMember2Key());
						}
					} else if (invite.getMember1Key()!= null) {
						// use member 1
						otherMemberKeys.add(invite.getMember1Key());						
					}
				}
			}
		}
		//------------
		// build set of user keys from other member keys
		for (final Member m : memberDao.fetchMemberKeys(otherMemberKeys)) {
			// only unique users
			if (!otherMemberKeys.contains(m.getUserKey())) {
				otherUserKeys.add(m.getUserKey());				
			}
		}
		//---------------
		// fetch these user keys and compile the email addresses
		affiliateEmailAddress.add(user.getEmailAddress());	// add user's own email address to this mix
		for (final User u : fetchUserKeys(otherUserKeys)) {
			affiliateEmailAddress.add(u.getEmailAddress());	// add fiend's email address
		}
		logger.info("found " + affiliateEmailAddress.size() + " affiliates for " + user.getEmailAddress());
		return affiliateEmailAddress;
	}
}
