package ca.jhosek.linguaelive;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.place.shared.PlaceHistoryHandler;

import ca.jhosek.linguaelive.domain.CurrentState;
import ca.jhosek.linguaelive.ui.MainView;
import ca.jhosek.linguaelive.proxy.AppRequestFactory;


@GinModules({AppGinModule.class})
public interface AppGinjector extends Ginjector {

  PlaceHistoryHandler getPlaceHistoryHandler();  
  MainView getMainView();
  CurrentState getCurrentState();
  AppRequestFactory getRequestFactory();
  AppResources getAppResources();
}
