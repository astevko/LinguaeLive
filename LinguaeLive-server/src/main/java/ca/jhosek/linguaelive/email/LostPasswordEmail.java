package ca.jhosek.main.server.email;

import ca.jhosek.main.server.domain.User;

/**
 * user lost password message
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see  SendEmail
 */
public class LostPasswordEmail extends SendEmail {

	private static final String SUBJECT = "LinguaeLive password recovery <first_name> <last_name>";
	private static final String BODY = "Hi <first_name>,\n"
			+ "LinguaLive received a lost password request for your account.\n\n"
			+ "Your password hint is <hint>.\n\n"
			+ "If you need more assistance, please enter the code <lostpasswordkey> at\n"
			+ "https://linguaelive.appspot.com/#lostandfound:me\n\n";

	public LostPasswordEmail(final User user) {
		super(user);
	}

	@Override
	public String getBody() {
		return BODY ;
	}

	@Override
	public String getSubject() {
		return SUBJECT;
	}
}
