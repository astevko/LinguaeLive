package ca.jhosek.linguaelive;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

import ca.jhosek.linguaelive.activity.ActivityModule;
import ca.jhosek.linguaelive.domain.CurrentState;
import ca.jhosek.linguaelive.mvp.MvpModule;
import ca.jhosek.linguaelive.ui.ViewModule;


/**
 * Top level dependency injection
 * 
 * @author andy
 * @see MvpModule
 * @see ViewModule
 * @see ActivityModule
 * 
 */
public class AppGinModule extends AbstractGinModule {

	/* (non-Javadoc)
	 * @see com.google.gwt.inject.client.AbstractGinModule#configure()
	 */
	@Override
	protected void configure() {
		// DOMAIN
		// current logged in state singleton
		bind( CurrentState.class).in(Singleton.class);

		// install the Gin module used to setup the GWT MVP framework classes.
		install(new MvpModule());
		install(new ViewModule());
		install(new ActivityModule());
	}


}
