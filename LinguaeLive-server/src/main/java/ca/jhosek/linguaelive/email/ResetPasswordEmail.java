package ca.jhosek.main.server.email;

import ca.jhosek.main.server.domain.User;

/**
 * user lost password reset
 * 
 * @author copyright (C) 2012 Andrew Stevko
 */
public class ResetPasswordEmail extends SendEmail {

	
	private static final String SUBJECT = "LinguaeLive password reset <first_name> <last_name>";
	private static final String BODY = 
		"Hi <first_name>,\n" +
		"LinguaLive received a reset password request for your account.\n\n" +
		"Your password reset code is <resetcode>.\n\n" +
		"Please enter this code at https://linguaelive.appspot.com/#lostandfound:me" +
		"\n\n" +
		"If you need any more assistance, please use the contact us form at\n" +
		"https://linguaelive.appspot.com/#contactus:";
		
	
	public ResetPasswordEmail(User user) {
		super(user);
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
