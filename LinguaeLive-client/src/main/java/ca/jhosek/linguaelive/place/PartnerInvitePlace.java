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
 * partner invite view
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see PartnerInviteActivity
 * @see PartnerInviteView
 * @see PartnerInviteViewImpl
 * @see PartnerInviteRequestContext
 *
 */
public class PartnerInvitePlace extends AuthenticatedPlace {

	private static final Logger logger = Logger.getLogger( PartnerInvitePlace.class.getName() );
	
	@Prefix("partnerinvite")
	public static class Tokenizer implements PlaceTokenizer<PartnerInvitePlace> {

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		public PartnerInvitePlace getPlace(String token) {
			return new PartnerInvitePlace( token );
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt.place.shared.Place)
		 */
		public String getToken(PartnerInvitePlace place) {
			// 
			return place.getSessionInviteId();
		}

	}
	/**
	 * the Id of the CourseLink 
	 */
	private String sessionInviteId;

	public PartnerInvitePlace( String sessionInviteId ) {
		logger.info("new SessionInvitePlace( " + sessionInviteId + " )");
		this.sessionInviteId = sessionInviteId;
	}

	public PartnerInvitePlace( PartnerInviteProxy sessionInvite ) {
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((sessionInviteId == null) ? 0 : sessionInviteId.hashCode());
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
		PartnerInvitePlace other = (PartnerInvitePlace) obj;
		if (sessionInviteId == null) {
			if (other.sessionInviteId != null)
				return false;
		} else if (!sessionInviteId.equals(other.sessionInviteId))
			return false;
		return true;
	}
}
