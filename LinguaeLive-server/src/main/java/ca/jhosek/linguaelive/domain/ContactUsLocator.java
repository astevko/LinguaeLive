/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.server.domain;

import com.google.web.bindery.requestfactory.shared.ServiceLocator;

import ca.jhosek.main.shared.proxy.ContactUsProxy;

/**
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see ContactUsService
 * @see ContactUsProxy
 * 
 */
public class ContactUsLocator implements ServiceLocator {

//	private static final Logger logger = Logger.getLogger(ContactUsLocator.class.getName());

	public Object getInstance(Class<?> clazz) {
		// 
		return new ContactUsService();
	}
		
}
