package ca.jhosek.linguaelive.domain;

import java.io.Serializable;
import com.googlecode.objectify.annotation.*;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.googlecode.objectify.Key;

import ca.jhosek.linguaelive.ContactInfoType;

/**
 * a single instance of user contact info
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see User
 * @see ContactInfoDao
 * @see ContactInfoType
 */
@Entity
public class ContactInfo extends DatastoreObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4787923588293727590L;

	@Ignore
	transient private User owner;

	@Index
	private Key<User> ownerKey;

	private ContactInfoType type = ContactInfoType.Other;

	private String info = "";

	private Boolean preferred = false;

	
	public ContactInfo() {
		// 		
	}
	
	public ContactInfo( ContactInfoType type, String info, boolean preferred ) {
		this.type = type;
		this.info = info;
		this.preferred = preferred;		
	}
	
	/**
	 * @return the info
	 */
	public String getInfo() {
		return info;
	}

	public Boolean getPreferred() {
		return preferred;
	}

	/**
	 * @return the type
	 */
	public ContactInfoType getType() {
		return type;
	}

	/**
	 * @param info the info to set
	 */
	public void setInfo(String info) {
		this.info = info;
	}

	public void setPreferred(Boolean preferred) {
		this.preferred = preferred;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(ContactInfoType type) {
		this.type = type;
	}

	public void setOwnerKey(Key<User> ownerKey) {
		this.ownerKey = ownerKey;
	}

	public Key<User> getOwnerKey() {
		return ownerKey;
	}
	
	/**
	 * @return
	 */
	public User getOwner() {
		try {
			this.owner = new UserDao().get( ownerKey );
		} catch (EntityNotFoundException e) {
			// 
			e.printStackTrace();
		}
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;	// transient
		this.ownerKey = new UserDao().key(owner);

	}
}
