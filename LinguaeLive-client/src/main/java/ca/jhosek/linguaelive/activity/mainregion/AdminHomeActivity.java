package ca.jhosek.main.client.activity.mainregion;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import ca.jhosek.main.client.ui.priv.admin.AdminHomeView;

public class AdminHomeActivity extends AbstractActivity implements AdminHomeView.Presenter {

	
	private AdminHomeView view;
	@SuppressWarnings("unused")
	private PlaceController placeController;

	@Inject
	public AdminHomeActivity( AdminHomeView view, PlaceController placeController) {
		super();
		this.view = view;
		this.placeController = placeController;
		view.setPresenter(this);
	}

	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(view);

	}
}
