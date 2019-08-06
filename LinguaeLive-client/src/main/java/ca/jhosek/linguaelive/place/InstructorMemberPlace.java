/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import ca.jhosek.main.shared.proxy.MemberProxy;

/**
 * instructor's view of Student Course Member
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class InstructorMemberPlace extends AuthenticatedPlace {

	@Prefix("instructormember")
	public static class Tokenizer implements PlaceTokenizer<InstructorMemberPlace> {

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		public InstructorMemberPlace getPlace(String token) {
			// 
			return new InstructorMemberPlace( token );
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt.place.shared.Place)
		 */
		public String getToken(InstructorMemberPlace place) {
			// 
			return place.getMemberId();
		}
		
	}
	/**
	 * the Id of the Member 
	 */
	private String memberId;

	public InstructorMemberPlace( String memberId ) {
		this.memberId = memberId;
	}

	public InstructorMemberPlace( MemberProxy member ) {
		this.memberId = member.getId().toString();
	}

	public String getMemberId() {
		return memberId;
	}
}
