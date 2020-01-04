/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.guice;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import ca.jhosek.linguaelive.domain.ObjectifyMain;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

/**
 * setup guice servlet context listener
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class AppGuiceServletContextListener extends GuiceServletContextListener {
	
	/* (non-Javadoc)
	 * @see com.google.inject.servlet.GuiceServletContextListener#getInjector()
	 */
	@Override
	protected Injector getInjector() {
		// create the injector
		return Guice.createInjector( new AppServletModule() /*, other logic modules */ );
	}

	/* (non-Javadoc)
	 * @see com.google.inject.servlet.GuiceServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		ServletContext sc = servletContextEvent.getServletContext();
		sc.removeAttribute(Injector.class.getName());
		super.contextDestroyed(servletContextEvent);
	}

	/* (non-Javadoc)
	 * @see com.google.inject.servlet.GuiceServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		// No call to super as it also calls getInjector()
		ServletContext sc = servletContextEvent.getServletContext();
		sc.setAttribute(Injector.class.getName(), getInjector());
		ObjectifyMain.init();
	}

	
	
}
