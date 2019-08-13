package ca.jhosek.linguaelive.email;

import ca.jhosek.linguaelive.domain.User;

/**
 * new instructor welcome message
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class NewInstructorEmail extends SendEmail {

	private static final String SUBJECT = "Welcome to LinguaeLive <school> <last_name>";
	private static final String BODY =
		"Hi <first_name> <last_name>,\n\n" +
		"Thanks for signing up to LinguaeLive.ca\n" +
		"Your password hint is <hint>.\n\n" +
		"If you haven't already, please register your courses as soon as possible so that you can link up with other complementary course instructors.\n" +
		"As other instructors register their courses, you will receive link requests from them too.\n\n" +
		"The url for returning is https://linguaelive.appspot.com/index.html#instructorstart:\n\n" +
		"Please add our our domain    @linguaelive.ca    to your email safe senders list so that all messages get to your inbox. For help: http://www.youtube.com/watch?v=TpABBoQwvkQ";
	
		
	
	public NewInstructorEmail(User user) {
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
