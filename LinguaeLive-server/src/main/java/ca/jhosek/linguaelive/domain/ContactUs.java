/**
 * 
 */
package ca.jhosek.main.server.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.googlecode.objectify.annotation.Entity;

import ca.jhosek.main.shared.proxy.ContactUsProxy;

/**
 * Bean to hold info to act on Lost Account Dialog
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see ContactUsProxy
 * 
 */
@Entity
public class ContactUs {
	
	@NotNull( message = "Email address is required.")
	@Pattern( regexp = "\\b[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}\\b", message = "Improperly formatted email address" )
	private String emailAddress;
	
	@NotNull( message = "Details are required.")
	@Size( min = 2, message = "Please enter your concern into the message body.")
	private String messageBody;

	@NotNull( message = "Name is required.")
	@Size( min = 2, message = "Please enter your name.")
	private String name;

	@NotNull( message = "Reason for contact is required.")
	private String topic;

	private Long   userId;

	private String school;
	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @return the messageBody
	 */
	public String getMessageBody() {
		return messageBody;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the topic
	 */
	public String getTopic() {
		return topic;
	}
	/**
	 * @param emailAddress the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	/**
	 * @param messageBody the messageBody to set
	 */
	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param topic the topic to set
	 */
	public void setTopic(String topic) {
		this.topic = topic;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param school the school to set
	 */
	public void setSchool(String school) {
		this.school = school;
	}

	/**
	 * @return the school
	 */
	public String getSchool() {
		return school;
	}
}
