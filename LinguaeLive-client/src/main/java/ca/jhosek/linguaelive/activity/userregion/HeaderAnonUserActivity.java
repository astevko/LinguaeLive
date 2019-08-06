package ca.jhosek.main.client.activity.userregion;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import ca.jhosek.main.client.domain.CurrentState;
import ca.jhosek.main.client.place.LoginFormPlace;
import ca.jhosek.main.client.ui.anon.HeaderAnonUserView;
import ca.jhosek.main.client.ui.anon.HeaderAnonUserViewImpl;

/**
 * anon users see Login & Sign up options
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see HeaderAnonUserView
 * @see HeaderAnonUserViewImpl
 */
public class HeaderAnonUserActivity extends AbstractActivity implements HeaderAnonUserView.Presenter {

	private static final Logger logger = Logger.getLogger( HeaderAnonUserActivity.class.getName() );

	private final PlaceController placeController;

	@SuppressWarnings("unused")
	private final CurrentState currentState;

	@SuppressWarnings("unused")
	private final EventBus eventBus;


	private HeaderAnonUserView view;

	@Inject
	public HeaderAnonUserActivity(HeaderAnonUserView view, PlaceController placeController, CurrentState currentState, EventBus eventBus) {
		this.view = view;
		this.placeController = placeController;
		this.currentState = currentState;
		this.eventBus = eventBus;
		view.setPresenter(this);
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.main.client.ui.anon.HeaderAnonUserView.Presenter#loginUser()
	 */
	public void loginUser() {
		placeController.goTo( new LoginFormPlace( ));    
	}


	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(view);

	}

	/* (non-Javadoc)
	 * @see ca.jhosek.main.client.ui.anon.HeaderAnonUserView.Presenter#changeLocale()
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void changeLocale(String locale) {
		logger.info("selecting a locale " + locale);
		final Date now = new Date();
		final Date nextYear = new Date();
		nextYear.setYear(now.getYear()+1);
		Cookies.setCookie("locale_cookie", locale, nextYear);
		
		final StringBuffer url = new StringBuffer("/index.html?");
		boolean firstParam = true;
		final Map<String, List<String>> params = Window.Location.getParameterMap();
		for (String parameter : params.keySet()) {
			// skip locale
			if (!parameter.equals("locale")) {
				if (firstParam) {
					firstParam = false;
				} else {
					url.append("&");
				}
				url.append(parameter);
				url.append("=");
				for (String vals : params.get(parameter)) {
					url.append(vals);
				}
			}	
		}
		if (!firstParam) {
			url.append("&");
		}
		url.append("locale=");
		url.append(locale); // set locale

		url.append("#index:");
		Window.Location.assign( url.toString() );
//	    eventBus.fireEvent(new PlaceChangeEvent(currentState.getHomePlace()));
	}

}
