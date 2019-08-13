package ca.jhosek.linguaelive.mvp;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import ca.jhosek.linguaelive.AppGinModule;
import ca.jhosek.linguaelive.place.AppPlaceHistoryMapper;
import ca.jhosek.linguaelive.place.IndexPlace;
import ca.jhosek.linguaelive.proxy.AppRequestFactory;

/**
 * @author copyright (C) 2011 Andrew Stevko
 *
 * @see AppGinModule
 * @see MainRegionActivityMapper
 * @see MenuRegionActivityMapper
 * @see UserRegionActivityMapper
 */
public class MvpModule extends AbstractGinModule {

	private PlaceHistoryHandler historyHandler = null;

	@Override
	protected void configure() {
		bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);

		//-------------
		// request factory
		//bind(AppRequestFactory.class).in(Singleton.class);

		// PlaceHistoryMapper instantiate new places based on the browser URL. You
		// only need one of those for the entire app.
		bind(PlaceHistoryMapper.class).to(AppPlaceHistoryMapper.class).in(Singleton.class);    


		//--------------------
		// REGIONS
		bind(MainRegionActivityMapper.class);
		bind(UserRegionActivityMapper.class);
		bind(MenuRegionActivityMapper.class);

	}

	/**
	 * Creates a new PlaceHistoryHandler.  This object is responsible handling navigation based on the browser URL.
	 * You only need one of those for the entire app.
	 * 
	 * @param placeController the place controller.
	 * @param historyMapper This is used to map the URL to a Place object and vice versa.
	 * @param eventBus the event bus.
	 * @return   */
	@SuppressWarnings("deprecation")
	@Provides 
	@Singleton  
	public PlaceHistoryHandler getHistoryHandler(PlaceController placeController,
			PlaceHistoryMapper historyMapper, EventBus eventBus) {
		if (historyHandler == null) {
			historyHandler = new PlaceHistoryHandler(historyMapper);
			historyHandler.register(placeController, eventBus, new IndexPlace());
		}

		return historyHandler;
	}

	/**
	 * Creates a new PlaceController. The place controller is used by the PlaceHistoryHandler and activities
	 * to tell the app to navigate to a different place. You only need one for the
	 * entire app. However, it is essential for it to get instantiated in order
	 * for the application navigation to work.
	 * 
	 * http://stackoverflow.com/questions/8947726/which-gwt-eventbus-should-i-use
	 * 
	 * @param eventBus the event bus.
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@Provides
	@Singleton
	public PlaceController getPlaceController(EventBus eventBus) {
		return new PlaceController(eventBus);
	}

	/**
	 * singleton to return request factory
	 * 
	 * @param eventBus
	 * @return request factory
	 */
	@Provides
	@Singleton
	public AppRequestFactory getRequestFactory( EventBus eventBus ) {
		AppRequestFactory factory = GWT.create( AppRequestFactory.class );
		factory.initialize(eventBus);
		return factory;
	}


}
