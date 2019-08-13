/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.place;

import java.util.logging.Logger;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import ca.jhosek.linguaelive.activity.mainregion.SessionControlActivity;
import ca.jhosek.linguaelive.ui.priv.student.SessionControlView;
import ca.jhosek.linguaelive.ui.priv.student.SessionControlViewImpl;
import ca.jhosek.linguaelive.proxy.SessionProxy;
import ca.jhosek.linguaelive.proxy.SessionRequestContext;

/**
 * session control panel view (as opposed to the session edit view
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see SessionControlActivity
 * @see SessionControlView
 * @see SessionControlViewImpl
 * @see SessionRequestContext
 *
 */
public class SessionControlPlace extends AuthenticatedPlace {

	private static final Logger logger = Logger.getLogger( SessionControlPlace.class.getName() );
	
	@Prefix("sessioncontrol")
	public static class Tokenizer implements PlaceTokenizer<SessionControlPlace> {

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		public SessionControlPlace getPlace(String token) {
			return new SessionControlPlace( token );
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt.place.shared.Place)
		 */
		public String getToken(SessionControlPlace place) {
			// 
			return place.getSessionId();
		}

	}
	/**
	 * the Id of the CourseLink 
	 */
	private String sessionId;

	public SessionControlPlace( String sessionId ) {
		logger.info("new SessionControlPlace( " + sessionId + " )");
		this.sessionId = sessionId;
	}

	public SessionControlPlace( SessionProxy session ) {
		logger.info("new SessionControlPlace( " + session.getId() + " )");
		this.sessionId = session.getId().toString();
	}


	public SessionControlPlace( Long sessionId ) {
		logger.info("new SessionControlPlace( " + sessionId + " )");
		this.sessionId = sessionId.toString();
	}
	/**
	 * @param sessionId the sessionInviteId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((sessionId == null) ? 0 : sessionId.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SessionControlPlace other = (SessionControlPlace) obj;
		if (sessionId == null) {
			if (other.sessionId != null)
				return false;
		} else if (!sessionId.equals(other.sessionId))
			return false;
		return true;
	}
}
