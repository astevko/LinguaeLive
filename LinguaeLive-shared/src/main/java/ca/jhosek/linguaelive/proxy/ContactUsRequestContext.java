/**
 * 
 */
package ca.jhosek.linguaelive.proxy;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

// import ca.jhosek.linguaelive.domain.ContactUs;
// import ca.jhosek.linguaelive.domain.ContactUsLocator;
// import ca.jhosek.linguaelive.domain.ContactUsService;

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
@ServiceName( value="ca.jhosek.linguaelive.domain.ContactUsService", locator="ca.jhosek.linguaelive.domain.ContactUsLocator" )
public interface ContactUsRequestContext extends RequestContext {
	// only operation
	Request<Void> submit( ContactUsProxy info );
}
