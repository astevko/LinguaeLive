package ca.jhosek.linguaelive.email;

import java.util.logging.Logger;

import com.google.appengine.api.datastore.EntityNotFoundException;

import ca.jhosek.linguaelive.domain.Course;
import ca.jhosek.linguaelive.domain.CourseLink;
import ca.jhosek.linguaelive.domain.User;

/**
 * message to instructor A 
 * confirming the linking of courses  
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class LinkCourseAcceptEmail extends SendEmail {

	private static final Logger logger = Logger.getLogger( LinkCourseAcceptEmail.class.getName());
	
	private static final String SUBJECT = "LinguaeLive Course Link Accepted <first_name> <last_name>";
	private static final String BODY = 
		"Hello Instructor,\n\n" +
		"Your course is now partnered with <school> <course_name> .\n" +
		"You can reach <first_name> <last_name> at <email_address>.\n\n" +		
		"Thanks from LinguaeLive.\n"; 
	
	public LinkCourseAcceptEmail(CourseLink courseLink ) throws EntityNotFoundException {
//		super( entity );
		try {
			// originater
			Course courseA = courseLink.getCourseA();
			assert( courseA != null );			
			User sender    = courseA.getOwner();
			assert( sender != null );
			
			// recipient
			Course courseB = courseLink.getCourseB();
			assert( courseB != null );			
			User recipient = courseB.getOwner();
			assert( recipient != null );

//			String courseLinkUrl = "https://linguaelive.appspot.com/index.html?#instructorcourselink:" + courseLink.getId().toString() + "-" + courseB.getId().toString();
			
			//
			emailAddress = sender.getEmailAddress();
			name = escapeHtml( sender.getFirstName() + " " + sender.getLastName() );
			
			//
			subject = replaceTokens( getSubject(), sender );
			body    = replaceTokens( getBody(), recipient );
			body    = replaceTokens( body, courseB 	);
//			body    = body.replace(  "<course_link_url>", courseLinkUrl );
//			body    = body.replace( "<personal_message>", personalMessage );
			
		} catch (EntityNotFoundException e) {
			// 
			logger.severe("send email database lookup failed:" + e.getMessage() );
		}
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
