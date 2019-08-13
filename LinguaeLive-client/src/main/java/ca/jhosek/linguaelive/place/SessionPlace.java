/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.place;

import java.util.logging.Logger;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import ca.jhosek.linguaelive.activity.mainregion.PartnerInviteActivity;
import ca.jhosek.linguaelive.ui.priv.student.PartnerInviteView;
import ca.jhosek.linguaelive.ui.priv.student.PartnerInviteViewImpl;
import ca.jhosek.linguaelive.proxy.PartnerInviteProxy;
import ca.jhosek.linguaelive.proxy.PartnerInviteRequestContext;

/**
 * partner invite place
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see PartnerInviteActivity
 * @see PartnerInviteView
 * @see PartnerInviteViewImpl
 * @see PartnerInviteRequestContext
 *
 */
public class SessionPlace extends AuthenticatedPlace {

	private static final Logger logger = Logger.getLogger( SessionPlace.class.getName() );
	
	@Prefix("sessioninvite")
	public static class Tokenizer implements PlaceTokenizer<SessionPlace> {

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		public SessionPlace getPlace(String token) {
			return new SessionPlace( token );
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt.place.shared.Place)
		 */
		public String getToken(SessionPlace place) {
			// 
			return place.getSessionInviteId();
		}

	}
	/**
	 * the Id of the CourseLink 
	 */
	private String sessionInviteId;

	public SessionPlace( String sessionInviteId ) {
		logger.info("new SessionInvitePlace( " + sessionInviteId + " )");
		this.sessionInviteId = sessionInviteId;
	}

	public SessionPlace( PartnerInviteProxy sessionInvite ) {
		logger.info("new SessionInvitePlace( " + sessionInvite.getId() + " )");
		this.sessionInviteId = sessionInvite.getId().toString();
	}

	/**
	 * @param sessionInviteId the sessionInviteId to set
	 */
	public void setSessionInviteId(String sessionInviteId) {
		this.sessionInviteId = sessionInviteId;
	}

	/**
	 * @return the sessionInviteId
	 */
	public String getSessionInviteId() {
		return sessionInviteId;
	}
}
