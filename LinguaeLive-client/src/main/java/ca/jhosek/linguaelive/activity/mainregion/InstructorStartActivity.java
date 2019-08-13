package ca.jhosek.linguaelive.activity.mainregion;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import ca.jhosek.linguaelive.ui.priv.instructor.InstructorStartView;

/**
 * Activities on the Instructor Start page
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class InstructorStartActivity extends AbstractActivity implements InstructorStartView.Presenter {

	
	private InstructorStartView view;
	@SuppressWarnings("unused")
	private PlaceController placeController;

	@Inject
	public InstructorStartActivity( InstructorStartView view, PlaceController placeController) {
		super();
		this.view = view;
		this.placeController = placeController;
		view.setPresenter(this);
	}

	
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(view);

	}
}
