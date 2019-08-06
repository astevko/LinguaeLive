package ca.jhosek.main.server.domain;

import com.google.web.bindery.requestfactory.shared.ServiceLocator;

/**
 * Generic locator service that can be referenced in the @Service annotation
 * for any RequestFactory service stub
 *  
 * @author turbomanage
 */
public class DaoServiceLocator implements ServiceLocator {

	public Object getInstance(Class<?> clazz) {
		System.out.println(clazz);
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
