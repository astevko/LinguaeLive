/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.shared.proxy;

import java.util.Date;

import com.google.web.bindery.requestfactory.shared.ProxyFor;

import ca.jhosek.main.server.domain.User;
import ca.jhosek.main.server.domain.UserLocator;
import ca.jhosek.main.shared.UserType;

/**
 * User Proxy for Request Factory
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see User
 * 
 *      references http://tbroyer.posterous.com/gwt-211-requestfactory
 * 
 */
@ProxyFor(value = User.class, locator = UserLocator.class)
public interface UserProxy extends DatastoreObjectProxy {

	/**
	 * @param acceptsTerms
	 */
	public Boolean getAcceptsTerms();

	/**
	 * @return the browser
	 */
	public String getBrowser();

	/**
	 * @return channel token for this user
	 */
	public String getChannelToken();

	/**
	 * @return the createDate
	 */
	public Date getCreateDate();

	/**
	 * @return current date/time for user
	 */
	public String getCurrentUserTime();

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress();

	/**
	 * @return the first name
	 */
	public String getFirstName();

	/**
	 * @return the hint
	 */
	public String getHint();

	/**
	 * @return the last name
	 */
	public String getLastName();

	/**
	 * @return the location
	 */
	public String getLocation();

	/**
	 * Only shows HASH of password
	 * 
	 * @return the password
	 */
	public String getPassword();

	/**
	 * @return the school
	 */
	public String getSchool();

	/**
	 * @return the timezoneOffset
	 */
	public Integer getTimezoneOffset();

	/**
	 * @return current timezone for user
	 */
	public String getUserTimeZone();

	/**
	 * @return
	 */
	public UserType getUserType();

	/**
	 * @param acceptsTerms
	 */
	public void setAcceptsTerms(Boolean acceptsTerms);

	/**
	 * @param browser
	 *            the browser to set
	 */
	public void setBrowser(String browser);

	/**
	 * @param emailAddress
	 *            the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress);

	/**
	 * @param firstName
	 */
	public void setFirstName(String firstName);

	/**
	 * @param hint
	 *            the hint to set
	 */
	public void setHint(String hint);

	/**
	 * @param lastName
	 */
	public void setLastName(String lastName);

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(String location);

	/**
	 * @param password
	 */
	public void setPassword(String password);

	/**
	 * @param school
	 *            the school to set
	 */
	public void setSchool(String school);

	/**
	 * @param timezoneOffset
	 *            the timezoneOffset to set
	 */
	public void setTimezoneOffset(Integer timezoneOffset);

	/**
	 * @param userType
	 */
	public void setUserType(UserType userType);
	
	/**
	 * @return is this user online??
	 */
	public Boolean getIsOnline();
	
	/**
	 * set this user online
	 * @param isOnline
	 */
	public void setIsOnline(Boolean isOnline);
	
	/**
	 * set online status
	 * @param onlineStatus  ONLINE | OFFLINE | INSESSION | UNKNOWN
	 */
	public void setOnlineStatus( String onlineStatus );
	
	/**
	 * get the online status string = ONLINE | OFFLINE | INSESSION | UNKNOWN
	 * @return onlineStatus  ONLINE | OFFLINE | INSESSION | KNOWN
	 */
	public String getOnlineStatus();
	
}
