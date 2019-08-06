package ca.jhosek.main.server.email;

import java.util.logging.Logger;

import com.google.appengine.api.datastore.EntityNotFoundException;

import ca.jhosek.main.server.domain.Course;
import ca.jhosek.main.server.domain.Member;
import ca.jhosek.main.server.domain.SessionInvite;
import ca.jhosek.main.server.domain.User;

/**
 * called twice to send a message to both students 
 * confirming the pairing of Members with a SessionInvite  
 * 
 * @author copyright (C) 2014 Andrew Stevko
 *
 */
public class StudentPairingUserAcceptEmail extends SendEmail {

	private static final Logger logger = Logger.getLogger( StudentPairingUserAcceptEmail.class.getName());
	
	private static final String SUBJECT = "LinguaeLive Course Pairing Accepted <first_name> <last_name>";
	private static final String BODY = 
		"Hello Student,\n\n" +
		"Your course is now partnered with <school> <course_name> .\n" +
		"You can reach <first_name> <last_name> at <email_address>.\n\n" +
		"Start your next session with <first_name> by opening this URL \n" +
		"<sessionInviteControlPanelUrl> \n" + 
		"Thanks from LinguaeLive.\n"; 
	
	/**
	 * @param sessionInvite the session invite
	 * @param memberA  sessionInvite.getMember1() then sessionInvite.getMember2()
	 * @param memberB  sessionInvite.getMember2() then sessionInvite.getMember1()
	 * @throws EntityNotFoundException
	 */
	public StudentPairingUserAcceptEmail(SessionInvite sessionInvite, Member memberA, Member memberB ) throws EntityNotFoundException {
//		super( entity );
		try {
			// originater
//			Member memberA = sessionInvite.getMember1();
			assert( memberA != null );			
			User sender    = memberA.getUser();
			assert( sender != null );
			
			// recipient
//			Member memberB = sessionInvite.getMember2();
			assert( memberB != null );			
			User recipient = memberB.getUser();
			assert( recipient != null );
			Course courseB = memberB.getCourse();
			assert( courseB != null );
			

			String sessionInviteUrl = "https://linguaelive.appspot.com/#partnerinvite:" + sessionInvite.getId().toString();
			
			//
			emailAddress = sender.getEmailAddress();
			name = escapeHtml( sender.getFirstName() + " " + sender.getLastName() );
			
			//
			subject = replaceTokens( getSubject(), sender );
			body    = replaceTokens( getBody(), recipient );
			body    = replaceTokens( body, courseB 	);
			body    = body.replace(  "<sessionInviteControlPanelURL>", sessionInviteUrl );
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
