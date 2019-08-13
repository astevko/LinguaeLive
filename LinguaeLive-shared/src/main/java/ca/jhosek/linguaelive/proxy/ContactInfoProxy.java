package ca.jhosek.linguaelive.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyForName;

// import ca.jhosek.linguaelive.domain.ContactInfo;
// import ca.jhosek.linguaelive.domain.ContactInfoDao;
// import ca.jhosek.linguaelive.domain.ObjectifyLocator;
import ca.jhosek.linguaelive.ContactInfoType;

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
@ProxyForName( value="ca.jhosek.linguaelive.domain.ContactInfo", locator="ca.jhosek.linguaelive.domain.ObjectifyLocator" )
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
