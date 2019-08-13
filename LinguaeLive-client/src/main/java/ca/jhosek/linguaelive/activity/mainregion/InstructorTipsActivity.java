package ca.jhosek.linguaelive.activity.mainregion;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import ca.jhosek.linguaelive.activity.ActivityModule;
import ca.jhosek.linguaelive.place.InstructorTipsPlace;
import ca.jhosek.linguaelive.ui.priv.instructor.InstructorTipsView;
import ca.jhosek.linguaelive.ui.priv.instructor.InstructorTipsViewImpl;

/**
 * Activities on the Instructor Tips page
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see InstructorTipsPlace
 * @see InstructorTipsView
 * @see InstructorTipsViewImpl
 * @see ActivityModule
 */
public class InstructorTipsActivity extends AbstractActivity implements InstructorTipsView.Presenter {

	
	private InstructorTipsView view;
	@SuppressWarnings("unused")
	private PlaceController placeController;

	@Inject
	public InstructorTipsActivity( InstructorTipsView view, PlaceController placeController) {
		super();
		this.view = view;
		this.placeController = placeController;
	}

	
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(view);

	}
}
