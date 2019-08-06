package ca.jhosek.main.server.email;

import ca.jhosek.main.server.domain.ContactUs;

/**
 * Contact Us Email
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see SendEmail
 *
 */
public class ContactUsEmail extends SendEmail {

	private static final String SUBJECT = "<topic> - <name>";
	private static final String BODY = "LinguaeLive.ca Contact Us form submitted\n\n" +
					"Name:  <name>\n" +
					"Email: <email_address>\n" +
					"School:<school>\n" +
					"User ID: <user_id>\n" +
					"Topic: <topic>\n" +
					"Details\n" +
					"<message_body>";

	public ContactUsEmail( ContactUs contactUs ) {
		super( contactUs );
	}

	@Override
	public String getSubject() {
		return SUBJECT;
	}

	@Override
	public String getBody() {
		return BODY;		
	}

}
