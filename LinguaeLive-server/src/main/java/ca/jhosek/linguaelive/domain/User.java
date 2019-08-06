/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.server.domain;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Logger;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

//import com.google.appengine.api.channel.ChannelMessage;
//import com.google.appengine.api.channel.ChannelService;
//import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;

import ca.jhosek.main.shared.UserType;
import ca.jhosek.main.shared.proxy.UserProxy;

/**
 * Name and Address of a User
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see UserDao
 * @see UserLocator
 * @see UserProxy
 * 
 */
@Entity
public class User extends DatastoreObject implements Serializable {

	/**
	 * found in memcache? then online! 
	 */
	public static final String IS_ONLINE = "isOnline";

	/**
	 * logger for User class 
	 */
	private static final Logger logger = Logger.getLogger(User.class.getName());

	/**
	 * 
	 */
	private static final long serialVersionUID = 5273578553901721825L;

	public static final int BEGIN_LOSTPASSWORD_KEY = 10;

	public static final int END_LOSTPASSWORD_KEY = 18;


	/**
	 * Using the synchronous memcache
	 * 
	 * turns out this the default error handler so no-op
	 * syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
	 */
	private final static MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();

	/**
	 * ChannelService yay!
	 */
//	private final static ChannelService channelService = ChannelServiceFactory.getChannelService();

	/**
	 * type of user
	 */
	private UserType userType = UserType.ANON;

	@NotNull
	@Size(min = 2, message = "Name must be longer than 2 characters")
	@Pattern(regexp = "[a-z A-Z0-9]*", message = "Name must contain characters from a-zA-Z")
	private String firstName = "";

	@NotNull
	@Size(min = 2, message = "Last name must be longer than 2 characters")
	@Pattern(regexp = "[a-z A-Z]*", message = "Name must contains characters from A-Z")
	private String lastName = "";

	@Index
	@NotNull
	@Pattern(regexp = "\\b[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}\\b", message = "Improperly formatted email address")
	private String emailAddress = "";

	@NotNull
	@Size(min = 2, message = "School must be longer than 2 characters")
	private String school = "";

	private Date createDate = new Date();

	private String browser = "";

	// @Size( min = 8, max = 24, message =
	// "Passwords must be between 8-24 characters" )
	// @ Pattern( regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,24})", message =
	// "Passwords must contain numbers and upper & lower case letters" )
	private String password = "";

	@AssertTrue
	private Boolean acceptsTerms = false;

	/**
	 * number of minutes off of UTC for this user's timezone
	 * user.setTimezoneOffset( new Date().getTimezoneOffset() );
	 */
	@NotNull
	private Integer timezoneOffset;

	@Size(min = 2, message = "Location must be longer than 2 characters")
	private String location = "";

	// @Size( min = 2, message =
	// "Password hint must be longer than 2 characters")
	private String hint = "";

	/**
	 * channel token used by client
	 */
	@Ignore
	private String channelToken;

	private String resetcode;

	/**
	 * default
	 */
	public User() {

		//
		// contactInfos.add( new ContactInfo(ContactInfoType.Skype,
		// "enter your skype id", true));
		// contactInfos.add( new ContactInfo(ContactInfoType.CellPhone,
		// "enter your cell phone number", false));
		// contactInfos.add( new ContactInfo(ContactInfoType.WorkPhone,
		// "enter your work phone number", false));

	}

	/**
	 * @param acceptsTerms
	 */
	public Boolean getAcceptsTerms() {
		return acceptsTerms;
	}

	/**
	 * @return the browser
	 */
	public String getBrowser() {
		return browser;
	}

	/**
	 * @return the channel token for communications.
	 */
	public String getChannelToken() {
		return this.channelToken;
	}

	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * convert server NOW time to user local time for this user
	 * 
	 * @return the timezoneOffset
	 */
	public String getCurrentUserTime() {

		final TimeZone userTZ = getTimeZone();
		final Calendar thenThere = Calendar.getInstance(); // now there

		final String NOW_THERE_FORMAT = "EEE kk:mm zz";
		final DateFormat dateFormat = new SimpleDateFormat(NOW_THERE_FORMAT);
		dateFormat.setTimeZone(userTZ);
		return dateFormat.format(thenThere.getTime());

		// Date now = new Date();
		// int timezoneDifference = timezoneOffset - serverTimezoneOffset ;
		// if( timezoneDifference != 0 ) {
		// // set the minutes to handle the timezone offset
		// now.setMinutes(timezoneDifference);
		// }
		// return now;
	}

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @return the name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @return the hint
	 */
	public String getHint() {
		return this.hint;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @return the string used to recover lost passwords
	 */
	public String getLostPasswordKey() {
		return this.getPassword().substring(BEGIN_LOSTPASSWORD_KEY,
				END_LOSTPASSWORD_KEY);
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * get the reset code
	 */
	public String getResetcode() {
		return resetcode;
	}

	/**
	 * @return the school
	 */
	public String getSchool() {
		return school;
	}

	public TimeZone getTimeZone() {
		final int minutes = timezoneOffset * -1; //
		final int hours = minutes / 60; // automatically cast to int

		// create a timezone for this user
		String tzMinutes;
		if (minutes < 0) {
			tzMinutes = "GMT" + hours;
		} else {
			tzMinutes = "GMT+" + hours;
		}

		final TimeZone userTZ = TimeZone.getTimeZone(tzMinutes);
		return userTZ;
	}

	/**
	 * @return the timezoneOffset
	 */
	public Integer getTimezoneOffset() {
		return timezoneOffset;
	}

	/**
	 * @return current timezone for user
	 */
	public String getUserTimeZone() {
		final TimeZone userTZ = getTimeZone();
		return userTZ.getDisplayName();
	}

	/**
	 * @return the userType
	 */
	public UserType getUserType() {
		return userType;
	}

	/**
	 * @return the string used to recover lost passwords
	 */
	public Boolean isLostPasswordKey(final String userPasswordKey) {
		return userPasswordKey != null && !userPasswordKey.isEmpty()
				&& this.getLostPasswordKey().equals(userPasswordKey);
	}

	/**
	 * @param acceptsTerms
	 */
	public void setAcceptsTerms(final Boolean acceptsTerms) {
		this.acceptsTerms = acceptsTerms;
	}

	/**
	 * @param browser
	 *            the browser to set
	 */
	public void setBrowser(final String browser) {
		//
		this.browser = browser;
	}

	/**
	 * @param channelToken
	 *            the channelToken to set
	 */
	public void setChannelToken(final String channelToken) {
		this.channelToken = channelToken;
	}

	/**
	 * @param createDate
	 *            the createDate to set
	 */
	@SuppressWarnings("unused")
	private void setCreateDate(final Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * @param emailAddress
	 *            the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		// force lower case
		emailAddress = emailAddress.toLowerCase();
		this.emailAddress = emailAddress;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setFirstName(final String firstName) {
		// No nulls allowed here
		this.firstName = firstName;
	}

	/**
	 * @param hint
	 *            the hint to set
	 */
	public void setHint(final String hint) {
		this.hint = hint;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(final String location) {
		this.location = location;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(final String password) {
		this.password = password;
	}

	/**
	 * create a new reset code
	 */
	public void setResetcode() {
		// reset code is timestamp (initally)
		// TODO - need better reset code
		resetcode = Long.toString(new Date().getTime());
	}

	/**
	 * @param school
	 *            the school to set
	 */
	public void setSchool(final String school) {
		//
		this.school = school;
	}

	/**
	 * @param timezoneOffset
	 *            the timezoneOffset to set
	 */
	public void setTimezoneOffset(final Integer timezoneOffset) {
		this.timezoneOffset = timezoneOffset;
	}

	/**
	 * @param userType
	 *            the userType to set
	 */
	public void setUserType(final UserType userType) {
		this.userType = userType;
	}

	/**
	 * determined by checking memcache
	 * public boolean SessionDao isUserOnline(String emailAddress);
	 * 
	 * @see UserChannelConnectMonitor
	 * 
	 * @param isOnline
	 */
	@Deprecated
	public void setIsOnline(final Boolean isOnline) {
		// In the handler for _ah/channel/connected/
		//		ChannelService channelService = ChannelServiceFactory.getChannelService();
		//		ChannelPresence presence = channelService.parsePresence(req);
		//		String isOnlineFlag = presence.isConnected() ? "isOnline" : "";

		logger.info("channel session change: connection for " + this.getEmailAddress() + " is " + (isOnline ? IS_ONLINE : "offline")); 
		if (isOnline) {
			setOnlineStatus( "ONLINE" );
		} else {
			setOnlineStatus( "OFFLINE" );
		}
	}
	/**
	 * determined by checking memcache
	 * public boolean SessionDao isUserOnline(String emailAddress);
	 * @param isOnline
	 */
	@Deprecated
	public Boolean getIsOnline() {
		final String key = "status|" + emailAddress;
		if (syncCache.contains(key)) {
			final String onlineBlurb = getOnlineStatus();
			final boolean isOnline = onlineBlurb.startsWith("ONLINE") || onlineBlurb.startsWith("INSESSION");
			logger.info("User " + this.getEmailAddress() + " isOnline=" + isOnline + " " + onlineBlurb );
			return isOnline;
		}
		// logger.info("User " + this.getEmailAddress() + " is offline");
		return false;
	}

	/**
	 * set online status within memcache
	 * key = status|<emailAddress>
	 * value = [ONLINE | OFFLINE | INSESSION | UNKONWN]|<long time>
	 * 
	 * @param onlineStatus  ONLINE | OFFLINE | INSESSION | UNKNOWN  
	 */
	public void setOnlineStatus( String onlineStatus ) {
		String key = "status|" + emailAddress;
		String value = onlineStatus + "|" + Long.toString(new Date().getTime());
		logger.info("setOnlineStatus " + key + "|" + value );  
		syncCache.put( key, value ); // populate cache		

		// propagate online status change to affiliates
		notifyAffiliates( key + "|" + value );
	}

	/**
	 * get the online status string = [ONLINE | OFFLINE | INSESSION | UNKNOWN] + | + timestamp
	 * @return onlineStatus  [ONLINE | OFFLINE | INSESSION | UNKNOWN] + | + timestamp
	 */
	public String getOnlineStatus() {
		return getOnlineStatus(this.getEmailAddress());
	}

	/**
	 * get the online status string = [ONLINE | OFFLINE | INSESSION | UNKNOWN] + | + timestamp
	 * @param emailAddress 
	 * @return onlineStatus  [ONLINE | OFFLINE | INSESSION | UNKNOWN] + | + timestamp
	 */
	public static String getOnlineStatus( String emailAddress) {
		try {
			final String key = "status|" + emailAddress;
			if (syncCache.contains(key)) {
				return ((String) syncCache.get(key));
			} else {
				return "UNKNOWN";
			}
		} catch (ClassCastException e) {
			// Hmmm! not a string in memcache
			logger.warning("memcache does not contain a string for user online status");
			return "UNKONWN";
		}
	}

	/**
	 * send a message to this email address
	 * @see ChannelService 
	 */
	public static void sendMessage( final String emailAddress, final String message ) {
		if (emailAddress == null || emailAddress.isEmpty()) {
			logger.warning("bad or missing email address"  );
			return;
		} else if (message == null || message.isEmpty()) {
			logger.warning("bad or missing message"  );
			return;			
		} else if (!emailAddress.contains("@")) {
			// ignore affiliates keyword aka email address without at char
			return;
		}
//		logger.info("sending message to " + emailAddress + " - " + message );
//		channelService.sendMessage(new ChannelMessage( emailAddress, message));
	}

	/**
	 * send a message to these email addresses
	 * @param emailAddresses String array of email address
	 * @param message 
	 */
	public static void sendMessage( final String[] emailAddresses, final String message ) {
		for( int i = 0; i < emailAddresses.length; i++) {
			final String affiliate = emailAddresses[i];
			sendMessage(affiliate, message);
			logger.info("User sendMessage() " + affiliate + " " + message);
		}
	}

	/**
	 * update affiliates of this user with this message
	 * 
	 * @param message
	 */
	public void notifyAffiliates(String message ) {

		final String[] affiliatess = getAffiliates();
		// send message to one and all
		sendMessage(affiliatess, message);
	}

	/**
	 * generates or cached version of affiliated email addresses
	 * memcache key = affiliates|email@ddress
	 * memcache value = email | email | email | email | ...
	 * @return an array of affiliated email addresses
	 */
	public String[] getAffiliates() {
		final String key = "affiliates|" + getEmailAddress();
		if (syncCache.contains(key)) {
			// use cache version
			return ((String) syncCache.get(key)).split("\\|");

		} else {
			// no cache version; gather, use, then save into cache
			logger.info("no cached affiliates found for emailAddress " + getEmailAddress());
			final StringBuilder affiliatess = new StringBuilder();
			final UserDao userDao = new UserDao();
			final List<String> emails = userDao.gatherAffiliateEmailAddresses(this);
			if (!emails.isEmpty()) {
				// build cache 
				boolean isFirst = true;
				for (String email : emails) {
					if (!isFirst) {
						affiliatess.append("|");
					} else {
						isFirst = false;
					}
					affiliatess.append(email);
				}
				// save into cache
				logger.info(key + "|" + affiliatess.toString());
				syncCache.put(key, affiliatess.toString());

				// NEED to delay this a few seconds so that the channel can open and stabilize
				// update client with affiliated email addresses
				// sendMessage(getEmailAddress(), key + "|" + affiliatess.toString());
			} 
			// send messages to one and all
			return(emails.toArray(new String[1]));
		}
	}

}
