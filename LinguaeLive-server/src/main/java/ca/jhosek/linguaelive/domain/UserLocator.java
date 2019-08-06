/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.server.domain;

import java.util.logging.Logger;

/**
 * @author copyright (C) 2011 Andrew Stevko
 * @see ObjectifyLocator
 * 
 * @see UserDao
 * 
 * There is a reference to this class in a {@literal @}Service annotation in
 * {@link com.google.gwt.sample.dynatablerf.shared.DynaTableRequestFactory}
 */
public class UserLocator extends ObjectifyLocator {
	
//	@Inject static private UserDao dao;

	private static final Logger logger = Logger.getLogger(UserLocator.class.getName());
		
	/**
	 * 
	 */
	public UserLocator( ) {
		// 
		logger.info( "new UserLocator()");
		@SuppressWarnings("unused")
		UserDao userDao = new UserDao();
	}

//	/**
//	 * try to find this email address 
//	 * @see com.google.web.bindery.requestfactory.shared.Locator#find(java.lang.Class, java.lang.Object)
//	 */
//	public User findEmail( Class<? extends User> clazz, String emailAddress ) {
//		logger.info( "findEmail( User, <" + emailAddress +  
//				"> )");
//		return dao.ofy().query(User.class).filter( "emailAddress", emailAddress ).get();
//	}	
}
