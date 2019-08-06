package ca.jhosek.main.client.activity.userregion;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import ca.jhosek.main.client.domain.CurrentState;
import ca.jhosek.main.client.place.IndexPlace;
import ca.jhosek.main.client.ui.priv.HeaderUserView;
import ca.jhosek.main.client.ui.priv.HeaderUserViewImpl;

/**
 * Authenticated users see their name etc...
 * 
 * @author copyright (C) 2011, 2012 Andrew Stevko
 * 
 * @see CurrentState
 * @see PlaceController
 * @see HeaderUserView
 * @see HeaderUserViewImpl
 *
 */
public class HeaderUserActivity extends AbstractActivity implements HeaderUserView.Presenter {

	private final HeaderUserView view;
	private final PlaceController placeController;
	private final CurrentState currentState;

	@Inject
	public HeaderUserActivity(HeaderUserView view, PlaceController placeController, CurrentState currentState) {
		super();
		this.view = view;
		this.placeController = placeController;
		this.currentState = currentState;
		view.setPresenter(this);
	}

	/**
	 * 
	 * 
	 * @see ca.jhosek.main.client.ui.priv.HeaderUserView.Presenter#logout()
	 */
	public void logoutUser() {
		// logout any logged in user 
		currentState.logoutUser();
		// redirect to IndexPlace
		placeController.goTo(new IndexPlace());    

	}


	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		view.setUser(currentState.getLoggedInUser());
		panel.setWidget(view);

	}

}
