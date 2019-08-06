package ca.jhosek.main.server.email;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.google.appengine.api.datastore.EntityNotFoundException;

import ca.jhosek.main.server.domain.ContactUs;
import ca.jhosek.main.server.domain.Course;
import ca.jhosek.main.server.domain.CourseLink;
import ca.jhosek.main.server.domain.Member;
import ca.jhosek.main.server.domain.SessionInvite;
import ca.jhosek.main.server.domain.User;

/**
 * 
 * sending email templates with Users and Courses
 * 
 * <first_name> \n * <last_name>  \n * <school>  \n * <email_address> \n\n * <description> \n * <end_date> \n * <est_size> \n * <expert_language> \n * <target_language>
 * 
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see User
 * @see Course
 *
	User Tokens
	<first_name>
	<last_name>
	<school>
	<email_address>

	Course Tokens
	-------------
	<description>
	<end_date>
	<est_size>
	<expert_language>
	<target_language>
	<start_date>
	<course_name>


	Course Link Tokens
	(same as course tokens prefixed by courseA and courseB )

	Contact Us Tokens
	-----------------
	<school>
	<email_address>
	<name>
	<topic>	
	<user_id>
	<message_body>
 *
 *
 *

New Instructor Sign Up - sent when instructor creates a new account
New Student Sign Up - sent when student creates a new account
New Course - sent when instructor creates a new course
Lost Password
Request to link courses
Confirmation of linked courses


 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
public abstract class SendEmail {

	private static final Logger logger = Logger.getLogger( SendEmail.class.getName());

	private static final String FROM_ADDRESS = "admin@linguaelive.ca";	
	private static final String FROM_NAME = "LinguaeLive";
	private static final String BCC_ADDRESS = "linguaelive@linguaelive.ca";

	protected String subject;
	protected String body;
	protected String name;
	protected String emailAddress;

	protected SendEmail() {
		// do nothing
	}
	/**
	 * send email, user only context
	 * 
	 * @param user
	 */
	protected SendEmail( User user ) {

		// Escape data from the client to avoid cross-site script vulnerabilities.
		emailAddress = escapeHtml( user.getEmailAddress() );
		name = escapeHtml( user.getFirstName() + " " + user.getLastName() );

		// get the email templates from the resource
		subject = replaceTokens( getSubject(), user );
		body    = replaceTokens( getBody(), user );		
	}

	/**
	 * send email, user & course context
	 * 
	 * @param user
	 * @param course
	 */
	protected SendEmail( User user, Course course ) {

		// Escape data from the client to avoid cross-site script vulnerabilities.
		emailAddress = escapeHtml( user.getEmailAddress() );
		name = escapeHtml( user.getFirstName() + " " + user.getLastName() );

		// get the email templates from the resource
		subject = replaceTokens( getSubject(), user );
		body    = replaceTokens( getBody(), user );
		subject = replaceTokens( subject, course );
		body    = replaceTokens( body, course );
	}

	/**
	 * send an email using ContactUs type
	 * @param contactUs
	 */
	protected SendEmail(ContactUs contactUs) {
		// 
		emailAddress = FROM_ADDRESS;
		name = "Contact Us";

		subject = replaceTokens( getSubject(), contactUs );
		body    = replaceTokens( getBody(),    contactUs );
	}

	/**
	 * sending a course Link email with a personal message 
	 * 
	 * @param courseLink
	 * @param personalMessage
	 */
	protected SendEmail( CourseLink courseLink , String personalMessage ) {
		assert( courseLink != null );

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

			String courseLinkUrl = "https://linguaelive.appspot.com/index.html?#instructorcourselink:" + courseLink.getId().toString() + "-" + courseB.getId().toString();

			//
			emailAddress = recipient.getEmailAddress();
			name = escapeHtml( recipient.getFirstName() + " " + recipient.getLastName() );

			//
			subject = replaceTokens( getSubject(), recipient );
			body    = replaceTokens( getBody(), sender );
			body    = replaceTokens( body, courseA 	);
			body    = body.replace(  "<course_link_url>", courseLinkUrl );
			body    = body.replace( "<personal_message>", personalMessage );

		} catch (EntityNotFoundException e) {
			// 
			logger.severe("send email database lookup failed:" + e.getMessage() );
		}
	}

	/**
	 * @param sessionInvite
	 * @param personalMessage
	 */
	public SendEmail(SessionInvite sessionInvite, String personalMessage) {
		assert( sessionInvite != null );

		try {
			// originater
			Member member1 = sessionInvite.getMember1();
			assert( member1 != null );			
			User sender    = member1.getUser();
			assert( sender != null );

			// recipient
			Member member2 = sessionInvite.getMember2();
			assert( member2 != null );			
			User recipient = member2.getUser();
			assert( recipient != null );
			TimeZone recipientTZ = recipient.getTimeZone();

			String sessionInviteUrl = "https://linguaelive.appspot.com/index.html?#sessioninvite:" + sessionInvite.getId();

			//
			emailAddress = recipient.getEmailAddress();
			name = escapeHtml( recipient.getFirstName() + " " + recipient.getLastName() );

			//
			subject = replaceTokens( getSubject(), recipient );
			body    = replaceTokens( getBody(), sessionInvite, recipientTZ );
			body    = replaceTokens( body, sender	);
			body    = body.replace(  "<session_invite_url>", sessionInviteUrl ); 
			body    = body.replace( "<personal_message>", personalMessage );

		} catch (EntityNotFoundException e) {
			// 
			logger.severe("send email database lookup failed:" + e.getMessage() );
		}
	}

	/**
	 * convert server NOW time to user local time for this user
	 * @return the timezoneOffset
	 */
	@SuppressWarnings("unused")
	private String getUserTime( TimeZone userTZ, Date then ){

		Calendar thenThere = Calendar.getInstance( ); // now there
		thenThere.setTime(then);


		String NOW_THERE_FORMAT = "yyyy.MM.dd 'at' HH:mm:ss zz";
		DateFormat dateFormat = new SimpleDateFormat( NOW_THERE_FORMAT);
		dateFormat.setTimeZone(userTZ);
		return dateFormat.format(thenThere.getTime());
	}	

	/**
	 * @param template
	 * @param user
	 * @return modified template
	 */
	protected String replaceTokens(String template, User user) {
		return( template
				.replace( "<first_name>", user.getFirstName()  )
				.replace( "<last_name>", user.getLastName())
				.replace( "<school>", user.getSchool())
				.replace( "<email_address>", user.getEmailAddress() ) )
				.replace( "<hint>", user.getHint() )
				.replace( "<lostpasswordkey>", user.getLostPasswordKey() )
				;
	}

	/**
	 * @param template
	 * @param course
	 * @return processed template
	 */
	protected String replaceTokens(String template, Course course) {
		String courseLink = "https://linguaelive.appspot.com/index.html?#instructoryourcourse:" + course.getId().toString();
		return( template
				.replace( "<description>" , course.getDescription() ) 
				.replace( "<end_date>" , course.getEndDate().toString() )
				.replace( "<est_size>" , course.getEstimatedMemberSize().toString() )
				.replace( "<expert_language>" , course.getExpertLanguage().name() )
				.replace( "<target_language>" , course.getTargetLanguage().name() ) 
				.replace( "<start_date>", course.getStartDate().toString() )
				.replace( "<course_name>" , course.getName() ) )
				.replace( "<course_link>", courseLink )
				;
	}

	/**
	 * @param template
	 * @param sessionInvite
	 * @param recipientTZ 
	 * @return processed template
	 */
	protected String replaceTokens(String template, SessionInvite sessionInvite, TimeZone recipientTZ ) {
		String sessionInviteLink = "https://linguaelive.appspot.com/index.html?#sessioninvite:" + sessionInvite.getId().toString();
		String result = template
				.replace( "<session_invite_link>", sessionInviteLink );
		//		if ( sessionInvite.getStartDateTime() != null ) {
		//			result = result
		//				.replace( "<startDateTime>" , getUserTime( recipientTZ, sessionInvite.getStartDateTime() ) );
		//		} else {
		//			result = result
		//			.replace( "<startDateTime>" , " not specified" );			
		//		}
		//				.replace( "<end_date>" , course.getEndDate().toString() )
		//				.replace( "<est_size>" , course.getEstimatedMemberSize().toString() )
		//				.replace( "<expert_language>" , course.getExpertLanguage().name() )
		//				.replace( "<target_language>" , course.getTargetLanguage().name() ) 
		//				.replace( "<start_date>", course.getStartDate().toString() )
		//				.replace( "<course_name>" , course.getName() ) )
		return( result );
	}

	/**
	 * @param template
	 * @param course
	 * @return processed template
	 */
	protected String replaceTokens(String template, ContactUs contactUs ) {
		template = template				
				.replace( "<email_address>", contactUs.getEmailAddress() ) 
				.replace( "<name>" , contactUs.getName() )
				.replace( "<message_body>", contactUs.getMessageBody() )
				.replace( "<topic>", contactUs.getTopic() );
		if ( contactUs.getSchool() != null ) {
			template.replace( "<school>", contactUs.getSchool());
		}
		if ( contactUs.getUserId() != null ) {
			template.replace( "<user_id>", contactUs.getUserId().toString() );
		}
		return( template );
	}

	/**
	 * @return TEST subject template
	 */
	abstract public String getSubject();

	/**
	 * @return TEST body template
	 */
	abstract public String getBody();


	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}

	/**
	 * send invite email
	 * 
	 * @return success 
	 */
	public boolean send() {
		logger.info("SendEmail.send()" );
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		try {
			//--------------------------------
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress( FROM_ADDRESS, FROM_NAME ));
			msg.addRecipient(Message.RecipientType.TO,
					new InternetAddress( emailAddress, name ));
			if ( !BCC_ADDRESS.isEmpty() ) {
				msg.addRecipient( Message.RecipientType.BCC, new InternetAddress( BCC_ADDRESS ));
			}
			msg.setSubject(subject);
			msg.setText( body );
			logger.info("SendEmail - subject=" + subject );
			logger.info("SendEmail - body=" + body  );
			Transport.send(msg);

			return true;
		} catch (AddressException e) {
			// ...
			logger.warning( "FAILURE Error sending email - to:" + emailAddress + " due to Address Exception " + e.getMessage() );
			return false;

		} catch (MessagingException e) {
			// ...
			logger.warning( "RETRY Error sending email - to:" + emailAddress + " due to Messaging Exception  " + e.getMessage() );
			return false;

		} catch (UnsupportedEncodingException e) {
			// ...
			logger.warning( "FAILURE Error sending email - to:" + emailAddress + " due to Encoding Exception  " + e.getMessage() );
			return false;
		}

	}

	/**
	 * @return the template body
	 */
	public String getBodySent() {
		return body;
	}
}
