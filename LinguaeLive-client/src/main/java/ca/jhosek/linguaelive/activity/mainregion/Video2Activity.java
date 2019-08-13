package ca.jhosek.linguaelive.activity.mainregion;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import ca.jhosek.linguaelive.place.Video1Place;
import ca.jhosek.linguaelive.ui.anon.Video2View;

/**
 * Video  Activity
 * 
 * @author copyright (C) 2012 Andrew Stevko
 * @see Video1Place
 * @see Video2Activity
 * @see Video2View
 */
public class Video2Activity extends AbstractActivity {

	private Video2View view;
	@SuppressWarnings("unused")
	private PlaceController placeController;

	@Inject
	public Video2Activity( Video2View view, PlaceController placeController) {
		super();
		this.view = view;
		this.placeController = placeController;
	}

	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(view);

	}


}
