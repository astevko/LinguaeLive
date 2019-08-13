package ca.jhosek.linguaelive.proxy;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

// import ca.jhosek.linguaelive.domain.ContactInfoDao;
// import ca.jhosek.linguaelive.domain.DaoServiceLocator;

/**
 * Request Service Stub for ContactInfo
 * referenced by RequestFactory
 * This is the service for serving up ContactInfo Objects
 * 
 * @see ContactInfoDao
 * @see DaoServiceLocator
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 * @link http://code.google.com/p/google-web-toolkit/wiki/RequestFactoryInterfaceValidation
 */
@ServiceName( value = "ca.jhosek.linguaelive.domain.ContactInfoDao", locator = "ca.jhosek.linguaelive.domain.DaoServiceLocator" )
public interface ContactInfoRequestContext extends RequestContext{

	Request<List<ContactInfoProxy>> findContactInfos( UserProxy user );
	Request<ContactInfoProxy> findPreferredContactInfo( UserProxy user );
	Request<ContactInfoProxy> findContactInfo( Long id );
	Request<Void> persist( ContactInfoProxy contactInfo );	
	Request<ContactInfoProxy> update( ContactInfoProxy contactInfo );	
	Request<Void> remove( ContactInfoProxy contactInfo );

	
}
