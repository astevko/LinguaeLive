/**
 * 
 */
package ca.jhosek.main.shared.proxy;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

import ca.jhosek.main.server.domain.ContactUs;
import ca.jhosek.main.server.domain.ContactUsLocator;
import ca.jhosek.main.server.domain.ContactUsService;

/**
 * context for submitting contact us request
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see AppRequestFactory
 * 
 * @see ContactUs
 * @see ContactUsProxy
 * 
 * @see ContactUsService
 * @see ContactUsLocator
 * 
 * @link http://code.google.com/p/google-web-toolkit/wiki/RequestFactoryInterfaceValidation
 */
@Service( value=ContactUsService.class, locator=ContactUsLocator.class )
public interface ContactUsRequestContext extends RequestContext {
	// only operation
	Request<Void> submit( ContactUsProxy info );
}
