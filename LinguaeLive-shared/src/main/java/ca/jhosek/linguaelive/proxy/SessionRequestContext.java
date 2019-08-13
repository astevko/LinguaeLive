/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.proxy;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

// import ca.jhosek.linguaelive.domain.DaoServiceLocator;
// import ca.jhosek.linguaelive.domain.SessionDao;
import ca.jhosek.linguaelive.LanguageType;

/**
 * Request Service Stub
 * referenced by RequestFactory
 * This is the service for serving up Session objects
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see SessionDao
 * @link http://code.google.com/p/google-web-toolkit/wiki/RequestFactoryInterfaceValidation
 */
@ServiceName( value = "ca.jhosek.linguaelive.domain.SessionDao", locator = "ca.jhosek.linguaelive.domain.DaoServiceLocator" )
public interface SessionRequestContext extends RequestContext {

	//    Request<List<SessionProxy>> listAll();
	Request<SessionProxy> findSession( Long id );
	Request<Void> persist(SessionProxy Session);
	Request<SessionProxy> saveAndReturn(SessionProxy newSession);
	Request<Void> remove(SessionProxy Session);
	Request<SessionProxy> addNote(UserProxy myUser, Long sessionId, String note );
	Request<List<SessionProxy>> getSessionsForMember( MemberProxy member );
	/**
	 * @param invite
	 * @param sessionLang
	 * @param startTime
	 * @param startMember
	 * @return a new session completely filled out from the invite
	 */
	Request<SessionProxy> createFromInvite( PartnerInviteProxy invite, LanguageType sessionLang, Integer startMember);
	/**
	 * create a session from a previous session 
	 * 
	 * @param previousSession
	 * @param sessionLang
	 * @return a new session with the language fields filled in right
	 */
	Request<SessionProxy> createFromSession( SessionProxy previousSession, LanguageType sessionLang, Integer startMember );

	
	/** stop this session and return it
	 * @param stopSession
	 * @return updates session
	 */
	Request<SessionProxy> stopSession(SessionProxy stopSession);

	/** cancel this session and return it
	 * @param stopSession
	 * @return updates session
	 */
	Request<SessionProxy> cancelSession(SessionProxy stopSession);
	
	Request<List<SessionProxy>> getOpenSessionsForUser( UserProxy user );
	Request<List<SessionProxy>> getSessionsForCourse(CourseProxy course);

	Request<Void> deleteSessionsForMember(MemberProxy member);
}
