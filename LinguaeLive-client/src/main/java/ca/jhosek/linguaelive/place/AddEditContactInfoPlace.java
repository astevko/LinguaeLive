/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import ca.jhosek.main.client.activity.mainregion.AddEditContactInfoActivity;
import ca.jhosek.main.client.ui.priv.AddEditContactInfoView;
import ca.jhosek.main.client.ui.priv.AddEditContactInfoViewImpl;
import ca.jhosek.main.shared.proxy.ContactInfoProxy;

/**
 * Add Edit Contact Info
 *  
 *  null means add new contact info
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see AddEditContactInfoView
 * @see AddEditContactInfoViewImpl
 * @see AddEditContactInfoActivity
 *
 */
 
public class AddEditContactInfoPlace extends AuthenticatedPlace {

	@Prefix("addeditcontactinfo")
	public static class Tokenizer implements PlaceTokenizer<AddEditContactInfoPlace> {

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getPlace(java.lang.String)
		 */
		public AddEditContactInfoPlace getPlace(String token) {
			// 
			return new AddEditContactInfoPlace(token);
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.place.shared.PlaceTokenizer#getToken(com.google.gwt.place.shared.Place)
		 */
		public String getToken(AddEditContactInfoPlace place) {
			// 
			return place.getContactInfoId();
		}
		
	}
	
	private final String contactInfoId;
	
	/**
	 * @param contactInfoId
	 */
	public AddEditContactInfoPlace(String contactInfoId) {
		this.contactInfoId = contactInfoId;
	}

	/**
	 * @param contactInfo
	 */
	public AddEditContactInfoPlace(ContactInfoProxy contactInfo ) {
		this.contactInfoId = contactInfo.getId().toString();
	}

	/**
	 * @return
	 */
	public String getContactInfoId() {
		return contactInfoId;
	}

	
	
}
