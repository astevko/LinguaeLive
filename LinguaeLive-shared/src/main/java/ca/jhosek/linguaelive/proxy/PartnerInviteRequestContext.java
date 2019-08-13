/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.proxy;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

// import ca.jhosek.linguaelive.domain.DaoServiceLocator;
// import ca.jhosek.linguaelive.domain.SessionInvite;
// import ca.jhosek.linguaelive.domain.SessionInviteDao;

/**
 * Request Service Stub
 * referenced by RequestFactory
 * This is the service for serving up SessionInvite objects
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see SessionInvite
 * @see SessionInviteDao
 * 
 * @link http://code.google.com/p/google-web-toolkit/wiki/RequestFactoryInterfaceValidation
 */
@ServiceName( value = "ca.jhosek.linguaelive.domain.SessionInviteDao", locator = "ca.jhosek.linguaelive.domain.DaoServiceLocator" )
public interface PartnerInviteRequestContext extends RequestContext {

//    Request<List<SessionInviteProxy>> listAll();
	Request<PartnerInviteProxy> findSessionInvite( Long id );
    /**
     * @param SessionInvite
     * @return
     * 
     * 
     */
    Request<Void> persist(PartnerInviteProxy SessionInvite);
    Request<PartnerInviteProxy> saveAndReturn(PartnerInviteProxy newSessionInvite);
    Request<Void> remove(PartnerInviteProxy SessionInvite);
    
    /**
     * @param forMember
     * @return a list of session invites for this member
     */
    Request<List<PartnerInviteProxy>> getSessionInvitesForMember( MemberProxy forMember );
    Request<PartnerInviteProxy> sendInviteToMember( MemberProxy invitor, MemberProxy invitee, String personalMessage );
    
	Request<List<PartnerInviteProxy>> getPendingSessionInvitesForUser(	UserProxy user);
	
	Request<Void>  deleteSessionInvitesForMember(MemberProxy member);
	
	Request<Void>  openSessionInvite(UserProxy otherUser, PartnerInviteProxy sessionInvite);
}
