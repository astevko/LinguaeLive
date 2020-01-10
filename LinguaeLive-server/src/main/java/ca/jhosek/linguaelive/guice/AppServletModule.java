																			/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.guice;

import ca.jhosek.linguaelive.HitLoggerFilter;
// import ca.jhosek.linguaelive.channel.UserChannelConnectMonitor;

import com.google.gwt.logging.server.RemoteLoggingServiceImpl;
import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;
import com.googlecode.objectify.ObjectifyFilter;

//import com.google.gwt.logging.shared.RemoteLoggingService;

/**
 * The  Guice Servlet Module
 * does initialization here
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class AppServletModule extends ServletModule {

	/* (non-Javadoc)
	 * @see com.google.inject.servlet.ServletModule#configureServlets()
	 */
	@Override
	protected void configureServlets() {
		
		//--------------
		// Singletons
		bind( HitLoggerFilter.class ).in(Singleton.class);
		// com.google.gwt.logging.shared.RemoteLoggingService
		bind( RemoteLoggingServiceImpl.class).in(Singleton.class);
//		bind( UserDao.class ).in(Singleton.class);
//		requestStaticInjection(UserDao.class );

		
		// SEE http://code.google.com/p/google-web-toolkit/issues/detail?id=5807
		bind( RequestFactoryServlet.class).in(Singleton.class);
		bind( ObjectifyFilter.class).in(Singleton.class);
		
		//--------------
		// filters
		filter("/*").through(ObjectifyFilter.class);
		filter("/gwtRequest").through(HitLoggerFilter.class);
	
		//--------------
		// servlets  
		serve("/gwtRequest").with( RequestFactoryServlet.class );
		serve("/linguaelive/remote_logging").with( RemoteLoggingServiceImpl.class );
		// TODO - remove logging servlet path

		// handle channel connections & disconnections 
		// bind(UserChannelConnectMonitor.class).in(Singleton.class);
		// serve("/_ah/channel/connected/").with( UserChannelConnectMonitor.class);
		// serve("/_ah/channel/disconnected/").with( UserChannelConnectMonitor.class);
		
		//--------------
		// constants
		// user.dir and switch off of the last directory
		String osName = System.getProperty("os.name");
		String userDir = System.getProperty("user.dir");
		if (osName.startsWith("Win") || osName.startsWith("Mac")) {
			// Load MyConstants-dev.properties
//			bind( FacebookConstants.class ).to( FacebookConstantsDev.class ).in(Singleton.class);
//			bind( RendererConstants.class ).to( RendererConstantsDev.class ).in(Singleton.class);
//			bind( MerchantConstants.class ).to( AlphaMerchantConstants.class).in(Singleton.class);
			//bind( MerchantConstants.class ).to( BetaMerchantConstants.class).in(Singleton.class);
			
		} else if ( userDir.indexOf("beta") >= 0 ){
			// Load MyConstants-prod.properties
//			bind( FacebookConstants.class ).to( FacebookConstantsBeta.class ).in(Singleton.class);
//			bind( RendererConstants.class ).to( RendererConstantsBeta.class ).in(Singleton.class);
//			bind( MerchantConstants.class ).to( BetaMerchantConstants.class).in(Singleton.class);
			
		} else {
			// Load MyConstants-prod.properties
//			bind( FacebookConstants.class ).to( FacebookConstantsProd.class ).in(Singleton.class);
//			bind( RendererConstants.class ).to( RendererConstantsProd.class ).in(Singleton.class);
//			bind( MerchantConstants.class ).to( ProductionMerchantConstants.class).in(Singleton.class);
			
		}
		
		
		
	}

}
