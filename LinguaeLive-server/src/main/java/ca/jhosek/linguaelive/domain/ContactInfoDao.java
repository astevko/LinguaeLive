package ca.jhosek.linguaelive.domain;

import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.googlecode.objectify.Key;
import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class ContactInfoDao extends ObjectifyGenericDao<ContactInfo> {
	// Inherit all methods from generic Dao


	private static final Logger logger = Logger.getLogger( ContactInfoDao.class.getName());

	/**
	 * find the contact info records for this user
	 * 
	 * @param user
	 * @return a list of contact infos for this user
	 */
	public List<ContactInfo> findContactInfos( User user ){
		logger.info("findContactInfos( " + user.getFirstName() + " " + user.getLastName() + " ) " );
		List<ContactInfo> contactInfos = ofy().load().type(ContactInfo.class).filter("ownerKey =", user).list();
		return contactInfos;
	}


	/**
	 * find the preferred contact info record for this user
	 * 
	 * @param user
	 * @return preferred contact info for this user
	 */
	public ContactInfo findPreferredContactInfo( User user ){
		logger.info("findContactInfos( " + user.getFirstName() + " " + user.getLastName() + " ) " );
		ContactInfo contactInfo = ofy().load().type(ContactInfo.class).filter("ownerKey =", user).filter( "preferred =", true).first().now();
		return contactInfo;
	}

	   /**
     * make this course entity persistent (or delete it if no info exists)
     * 
     * @param contactInfo
     */
    public void persist( ContactInfo contactInfo ) {
    	logger.info( "persist() putting contactInfo id = " + contactInfo.getId() + " w/ ownerId=" + contactInfo.getOwnerKey() );
    	boolean newEntity = (contactInfo.getId() == null);    	
//    	if ( newEntity ) {
//      	}
    	// *** NOTE tricked out to delete rather than add/update empty infos
    	if( contactInfo.getInfo().isEmpty() ) {
    		if (!newEntity) {
    	    	// if the info is empty and it is not a new entity, delete it
    			logger.info("deleting ContactInfo" );
    			this.delete(contactInfo);
    		}
    	} else {
    		this.put(contactInfo);
    	}
    	
//    	if( newEntity ) {
//    	}
    }
    
	public ContactInfo findContactInfo( Long id ){
		try {
			return this.get(id);			
		} catch( EntityNotFoundException e ) {
			logger.severe("ContactInfo id=" + id + " not found!");
			return null;
		}
	}
    
    public ContactInfo update( ContactInfo newEntity )  {
    	Key<ContactInfo> newKey = this.put(newEntity);
    	try {
			return this.get(newKey);
		} catch (EntityNotFoundException e) {
			logger.severe("ContactInfo id=" + newEntity + " not found!");
			return null;
		}
    }
    
    public void remove(ContactInfo entity){
    	this.delete(entity);
    }
 	
	
}
