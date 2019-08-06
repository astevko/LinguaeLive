/**
 * 
 */
package ca.jhosek.main.shared.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

import ca.jhosek.main.server.domain.ContactUs;

/**
 * Bean to hold info to act on Lost Account Dialog
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see ContactUs
 * @see ContactUsRequestContext
 * 
 */

@ProxyFor( value=ContactUs.class )
public interface ContactUsProxy extends ValueProxy {

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress();

	/**
	 * @return the messageBody
	 */
	public String getMessageBody();

	/**
	 * @return the name
	 */
	public String getName();

	/**
	 * @return the topic
	 */
	public String getTopic();
	/**
	 * @param emailAddress the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress);
	/**
	 * @param messageBody the messageBody to set
	 */
	public void setMessageBody(String messageBody);

	/**
	 * @param name the name to set
	 */
	public void setName(String name);

	/**
	 * @param topic the topic to set
	 */
	public void setTopic(String topic);

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId);

	/**
	 * @return the userId
	 */
	public Long getUserId();


	/**
	 * @param school the school to set
	 */
	public void setSchool(String school);

	/**
	 * @return the school
	 */
	public String getSchool();
}
