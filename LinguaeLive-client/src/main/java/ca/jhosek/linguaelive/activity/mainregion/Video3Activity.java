package ca.jhosek.linguaelive.activity.mainregion;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import ca.jhosek.linguaelive.place.Video1Place;
import ca.jhosek.linguaelive.ui.anon.Video1View;
import ca.jhosek.linguaelive.ui.anon.Video3View;

/**
 * Video  Activity
 * 
 * @author copyright (C) 2012 Andrew Stevko
 * @see Video1Place
 * @see Video3Activity
 * @see Video1View
 */
public class Video3Activity extends AbstractActivity {

	private Video3View view;
	@SuppressWarnings("unused")
	private PlaceController placeController;

	@Inject
	public Video3Activity( Video3View view, PlaceController placeController) {
		super();
		this.view = view;
		this.placeController = placeController;
	}

	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(view);

	}


}
