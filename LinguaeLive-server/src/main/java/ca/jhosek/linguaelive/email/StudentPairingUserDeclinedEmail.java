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
public class StudentPairingUserDeclinedEmail extends SendEmail {

	private static final Logger logger = Logger.getLogger( StudentPairingUserDeclinedEmail.class.getName());
	
	private static final String SUBJECT = "LinguaeLive Course Pairing Declined <first_name> <last_name>";
	private static final String BODY = 
		"Hello Student,\n\n" +
		"<message> \n\n" +
		"Your course is NOT partnered with \n <first_name> <last_name> \n <school> <course_name> .\n" +
		"Please try to invite other partners via \n" +
		"<courseMemberPanelUrl>" +
		"Thanks from LinguaeLive.\n"; 
	
	public StudentPairingUserDeclinedEmail(SessionInvite sessionInvite, Member memberA, Member memberB ) throws EntityNotFoundException {
		this(sessionInvite, memberA, memberB, "");
	}
	/**
	 * @param sessionInvite the session invite
	 * @param memberA  sessionInvite.getMember1() then sessionInvite.getMember2()
	 * @param memberB  sessionInvite.getMember2() then sessionInvite.getMember1()
	 * @throws EntityNotFoundException
	 */
	public StudentPairingUserDeclinedEmail(SessionInvite sessionInvite, Member memberA, Member memberB, String message) throws EntityNotFoundException {
//		super( entity );
		try {
			// originater
//			Member memberA = sessionInvite.getMember1();
			assert( memberA != null );			
			User sender    = memberA.getUser();
			assert( sender != null );
			Course courseA = memberA.getCourse();
			assert( courseA != null );
			
			// recipient
//			Member memberB = sessionInvite.getMember2();
			assert( memberB != null );			
			User recipient = memberB.getUser();
			assert( recipient != null );
			

			String courseAStudentPanelUrl = "https://linguaelive.appspot.com/#studentyourcourse:" + memberA.getId().toString();
			
			//
			emailAddress = sender.getEmailAddress();
			name = escapeHtml( sender.getFirstName() + " " + sender.getLastName() );
			
			//
			subject = replaceTokens( getSubject(), sender );
			body    = replaceTokens( getBody(), sender );
			body    = replaceTokens( body, courseA 	);
			body    = body.replace(  "<CourseAStudentPanelUrl>", courseAStudentPanelUrl );
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
