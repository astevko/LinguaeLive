/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.proxy;

import com.google.web.bindery.requestfactory.shared.RequestFactory;

/**
 * 
 * http://code.google.com/webtoolkit/doc/latest/DevGuideRequestFactory.html
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see ContactInfoRequestContext
 * @see CourseRequestContext
 * @see CourseLinkRequestContext
 * @see UserRequestContext
 * @see MemberRequestContext
 * @see ContactUsRequestContext
 * @see SessionRequestContext
 * @see PartnerInviteRequestContext
 * 
 */
public interface AppRequestFactory extends RequestFactory {
	UserRequestContext userRequest();
	CourseRequestContext courseRequest();
	ContactInfoRequestContext contactInfoRequest();
	CourseLinkRequestContext courseLinkRequest();
	MemberRequestContext memberRequest();
	SessionRequestContext sessionRequestContext();
	PartnerInviteRequestContext sessionInviteRequestContext();

	
	/**
	 * @return contact us request context
	 */
	ContactUsRequestContext contactUsRequest();
}
