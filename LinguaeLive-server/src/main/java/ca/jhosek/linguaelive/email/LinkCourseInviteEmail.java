package ca.jhosek.main.server.email;

import com.google.appengine.api.datastore.EntityNotFoundException;

import ca.jhosek.main.server.domain.CourseLink;

/**
 * message from Instructor A to B inviting B to link to this course owned by A
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * 
 */
public class LinkCourseInviteEmail extends SendEmail {

	private static final String SUBJECT = "Course Link request LinguaeLive <first_name> <last_name>";
	private static final String BODY =
	// "Hello Instructor,\n\n" +
	// "<first_name> <last_name> would like you to consider partnering their course <course_name> at the <school> with yours.\n"
	// +
	// "You can reach them at <email_address>\n\n" +
	"<personal_message>\n\n"
			+ "Please accept or decline this invitation at \n"
			+ "<course_link_url>\n"
			+ "Remember to add   @linguaelive.ca   to your email safe senders list to ensure that all messages get to your inbox.\n"
			+ "Thanks from LinguaeLive.\n";

	public LinkCourseInviteEmail(final CourseLink entity,
			final String personalMessage) throws EntityNotFoundException {
		super(entity, personalMessage);
	}

	@Override
	public String getBody() {
		return BODY;
	}

	@Override
	public String getSubject() {
		return SUBJECT;
	}

}
