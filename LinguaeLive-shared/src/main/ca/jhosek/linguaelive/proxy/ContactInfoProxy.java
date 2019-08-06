package ca.jhosek.main.shared.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;

import ca.jhosek.main.server.domain.ContactInfo;
import ca.jhosek.main.server.domain.ContactInfoDao;
import ca.jhosek.main.server.domain.ObjectifyLocator;
import ca.jhosek.main.shared.ContactInfoType;

/**
 * a single instance of user contact info
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see ContactInfoRequestContext
 * @see ContactInfo
 * @see ContactInfoDao
 * @link http://code.google.com/p/google-web-toolkit/wiki/RequestFactoryInterfaceValidation
 * 
 */
@ProxyFor( value=ContactInfo.class, locator=ObjectifyLocator.class )
public interface ContactInfoProxy extends DatastoreObjectProxy  {

	public UserProxy getOwner();
	
	public void setOwner( UserProxy owner );
	
	public Long getId();
	
	/**
	 * @return the info
	 */
	public String getInfo();

	public Boolean getPreferred();

	/**
	 * @return the type
	 */
	public ContactInfoType getType();

	/**
	 * @param info the info to set
	 */
	public void setInfo(String info);

	public void setPreferred(Boolean preferred);

	/**
	 * @param type the type to set
	 */
	public void setType(ContactInfoType type);
	
}
