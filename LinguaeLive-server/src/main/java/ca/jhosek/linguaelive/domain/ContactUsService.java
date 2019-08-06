/**
 * 
 */
package ca.jhosek.main.server.domain;

import java.util.logging.Logger;

import ca.jhosek.main.server.email.ContactUsEmail;
import ca.jhosek.main.server.email.SendEmail;
import ca.jhosek.main.shared.proxy.ContactUsRequestContext;

/**
 * handler for sending contact us emails
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see ContactUsRequestContext
 * @see SendEmail
 */
public class ContactUsService {

	private static final Logger logger = Logger.getLogger( ContactUsService.class.getName());

	public void submit( ContactUs contactUs ) {
		logger.info( "ContactUsHandler.submit() called by " +  contactUs.getEmailAddress() );
		new ContactUsEmail(contactUs).send();
	}
}
