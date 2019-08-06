package ca.jhosek.main.client.activity.mainregion;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import ca.jhosek.main.client.place.Video1Place;
import ca.jhosek.main.client.ui.anon.Video1View;

/**
 * Video  Activity
 * 
 * @author copyright (C) 2012 Andrew Stevko
 * @see Video1Place
 * @see Video1Activity
 * @see Video1View
 */
public class Video1Activity extends AbstractActivity {

	private Video1View view;
	@SuppressWarnings("unused")
	private PlaceController placeController;

	@Inject
	public Video1Activity( Video1View view, PlaceController placeController) {
		super();
		this.view = view;
		this.placeController = placeController;
	}

	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(view);

	}


}
