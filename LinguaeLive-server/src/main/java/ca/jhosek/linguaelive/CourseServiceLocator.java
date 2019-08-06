/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.server;

import com.google.web.bindery.requestfactory.shared.ServiceLocator;

import ca.jhosek.main.server.domain.CourseDao;

/**
 * @author copyright (C) 2011 Andrew Stevko
 * @see CourseDao
 */
public class CourseServiceLocator implements ServiceLocator {
	/* (non-Javadoc)
	 * @see com.google.web.bindery.requestfactory.shared.ServiceLocator#getInstance(java.lang.Class)
	 */
	public Object getInstance(Class<?> clazz) {
		// 
		return clazz.equals(CourseDao.class) ? new CourseDao() : null;
	}

}
