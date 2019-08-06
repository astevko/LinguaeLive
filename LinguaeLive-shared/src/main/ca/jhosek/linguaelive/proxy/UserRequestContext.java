/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.shared.proxy;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

import ca.jhosek.main.server.domain.DaoServiceLocator;
import ca.jhosek.main.server.domain.UserDao;

/**
 * Request Service Stub referenced by RequestFactory This is the service for
 * serving up User objects
 * 
 * has server twin as defined in the @Service value
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see UserDao
 * @see DaoServiceLocator
 * @link http://code.google.com/p/google-web-toolkit/wiki/
 *       RequestFactoryInterfaceValidation
 * 
 */
@Service(value = UserDao.class, locator = DaoServiceLocator.class)
public interface UserRequestContext extends RequestContext {

	/**
	 * used to return user given an email address and hash
	 * 
	 * @param emailAddress
	 * @param hash
	 * @return user or exception
	 */
	Request<UserProxy> checkOkEmailHash(String emailAddress, String hash);

	Request<Long> countUsers();

	Request<List<UserProxy>> findAllUsers();

	Request<UserProxy> findEmailAddress(String emailAddress);

	Request<UserProxy> findUser(Long id);

	Request<List<UserProxy>> getCourseMembers(CourseProxy course,
			Boolean availableOnly, Boolean studentsOnly);

	Request<UserProxy> loginCookie(String emailAddress, String hash);

	Request<UserProxy> loginEmailAddress(String emailAddress, String pswd);

	Request<UserProxy> persist(UserProxy userProxy);

	Request<UserProxy> persistPassword(UserProxy userProxy);

	/**
	 * recover password
	 * 
	 * @param emailAddress
	 * @return void
	 */
	Request<Void> recoverPassword(String emailAddress);

	Request<Void> remove(UserProxy userProxy);

	Request<UserProxy> update(UserProxy userProxy);
}
