package ca.jhosek.linguaelive.email;

import com.google.appengine.api.datastore.EntityNotFoundException;

import ca.jhosek.linguaelive.domain.SessionInvite;

/**
 * message from Student A to B 
 * inviting B to a session with A  
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see SessionInvite
 * 
 */
public class PartnerInviteEmail extends SendEmail {

	private static final String SUBJECT = "LinguaeLive Session Invite <first_name> <last_name>";
	private static final String BODY = 
//		"Hello,\n\n" +
//		"<first_name> <last_name> would like you to consider setting up a session.\n" +
		"<personal_message>\n\n" +
		"Please go to this link and accept or decline this invitation\n" +
		"<session_invite_link>\n\n" +
		"<first_name> can be reached at <email_address>.\n\n" +		
		"Thanks from LinguaeLive.\n"; 
	
	public PartnerInviteEmail(SessionInvite sessionInvite, String personalMessage ) throws EntityNotFoundException {
		super( sessionInvite, personalMessage );
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
