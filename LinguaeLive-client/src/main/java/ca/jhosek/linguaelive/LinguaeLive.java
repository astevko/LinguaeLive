/**
 * copyright (c) 2011 - 2014 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class LinguaeLive implements EntryPoint {
	private static final Logger logger = Logger.getLogger(LinguaeLive.class.getName());
	
	
	/* (non-Javadoc)
	 * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
	 */
	public void onModuleLoad() {
		//-------------
		// handle uncaught exceptions this way
		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			public void onUncaughtException(Throwable e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		});

		AppGinjector ginjector = GWT.create(AppGinjector.class);
		// ensure the style sheet is loaded 
		ginjector.getAppResources().css().ensureInjected();
		
		RootLayoutPanel.get().add(ginjector.getMainView());
		// Goes to place represented on URL or default place
		ginjector.getPlaceHistoryHandler().handleCurrentHistory();


//	    // Fast test to see if the sample is not being run from devmode
//	    if (GWT.getHostPageBaseURL().startsWith("file:")) {
//	      logger.log(Level.SEVERE, "The DynaTableRf sample cannot be run without its"
//	          + " server component.  If you are running the sample from a"
//	          + " GWT distribution, use the 'ant devmode' target to launch"
//	          + " the DTRF server.");
//	    }
	}
}
