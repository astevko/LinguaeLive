package ca.jhosek.linguaelive.activity.mainregion;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import ca.jhosek.linguaelive.ui.anon.FaqView;

/**
 * Faq Activity
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class FaqActivity extends AbstractActivity {

	private FaqView view;
	@SuppressWarnings("unused")
	private PlaceController placeController;

	@Inject
	public FaqActivity( FaqView view, PlaceController placeController) {
		super();
		this.view = view;
		this.placeController = placeController;
	}


	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(view);

	}


}
