package ca.jhosek.main.client.activity.mainregion;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import ca.jhosek.main.client.place.NewStudentPlace;
import ca.jhosek.main.client.ui.anon.StudentInfoView;

/**
 * StudentInfo Activity
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see StudentInfoView
 * 
 */
public class StudentInfoActivity extends AbstractActivity implements StudentInfoView.Presenter {

	private StudentInfoView view;
	private PlaceController placeController;

	@Inject
	public StudentInfoActivity( StudentInfoView view, PlaceController placeController) {
		super();
		this.view = view;
		this.placeController = placeController;
		view.setPresenter(this);
	}

	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(view);

	}

	/**
	 * goto new student signup
	 * @see ca.jhosek.main.client.ui.anon.StudentInfoView.Presenter#goToNewStudentForm()
	 */
	public void goToNewStudentForm() {
		// 
		placeController.goTo( new NewStudentPlace( "" ));
		
	}


}
