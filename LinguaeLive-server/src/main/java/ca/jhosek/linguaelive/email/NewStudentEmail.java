package ca.jhosek.main.server.email;

import ca.jhosek.main.server.domain.Course;
import ca.jhosek.main.server.domain.User;

/**
 * new student message 
 * - welcome to the system
 * - your password hint is
 * - and use this link to return
 *   
 * requires user and course
 * 
 * 
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see SendEmail
 *
 */
public class NewStudentEmail extends SendEmail {

	private static final String SUBJECT = "Welcome to LinguaeLive <last_name> <course_name>";
	private static final String BODY = 
			"Hi <first_name> <last_name>,\n" +
			"Thanks for signing up to LinguaeLive.\n" +
			"Your password hint is <hint>.\n\n" +
			"You have been signed up for <course_name> at <school>.\n\n" +
			"The next steps are to \n" +
			"1) Enter your contact info (phone, skype, etc)\n" +
			"2) Specify your availability\n" +
			"3) Invite other students to pair up\n\n" +
			"The url for returning is https://linguaelive.appspot.com/index.html#studentstart:\n\n" +
			"Please add our our domain    @linguaelive.ca    to your email safe senders list so that all messages get to your inbox. For help: http://www.youtube.com/watch?v=TpABBoQwvkQ";
	
	public NewStudentEmail(User user, Course course ) {
		super(user, course);
	}

	@Override
	public String getSubject() {
		return SUBJECT;
	}

	@Override
	public String getBody() {
		return BODY;		
	}
	
//	@Override
//	public String getSubject() {
//		return "Thanks for signing up to LinguaeLive <first_name> <last_name>\n\n";
//	}
//
//	@Override
//	public String getBody() {
//		return "Welcome to LinguaeLive <first_name> <last_name>\n\n" +
//				"Your request for a complementary partner is now posted on the LinguaeLive site. You may now search for a complementary partner by following this link "; 
//		// 		+ course.getLink();
//	}

}
