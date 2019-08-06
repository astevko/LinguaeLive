package ca.jhosek.main.shared.proxy;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

import ca.jhosek.main.server.domain.ContactInfoDao;
import ca.jhosek.main.server.domain.DaoServiceLocator;

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
@Service( value = ContactInfoDao.class, locator = DaoServiceLocator.class )
public interface ContactInfoRequestContext extends RequestContext{

	Request<List<ContactInfoProxy>> findContactInfos( UserProxy user );
	Request<ContactInfoProxy> findPreferredContactInfo( UserProxy user );
	Request<ContactInfoProxy> findContactInfo( Long id );
	Request<Void> persist( ContactInfoProxy contactInfo );	
	Request<ContactInfoProxy> update( ContactInfoProxy contactInfo );	
	Request<Void> remove( ContactInfoProxy contactInfo );

	
}
