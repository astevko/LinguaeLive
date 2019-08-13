package ca.jhosek.linguaelive.activity.mainregion;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import ca.jhosek.linguaelive.place.LoginFormPlace;
import ca.jhosek.linguaelive.place.NewInstructorPlace;
import ca.jhosek.linguaelive.place.NewStudentPlace;
import ca.jhosek.linguaelive.ui.anon.IndexView;

/**
 * Main index page activity
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class IndexActivity extends AbstractActivity implements IndexView.Presenter {

	private IndexView view;
	private PlaceController placeController;

	@Inject
	public IndexActivity( IndexView view, PlaceController placeController) {
		super();
		this.view = view;
		this.placeController = placeController;
		view.setPresenter(this);
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.anon.IndexView.Presenter#goToLoginForm()
	 */
	
	public void goToLoginForm() {
		// 
		placeController.goTo( new LoginFormPlace( ) );
		
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.anon.IndexView.Presenter#goToNewInstructorForm()
	 */
	
	public void goToNewInstructorForm() {
		// 
		placeController.goTo( new NewInstructorPlace() );
				
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.anon.IndexView.Presenter#goToNewStudentForm()
	 */
	
	public void goToNewStudentForm() {
		// 		// 
		placeController.goTo( new NewStudentPlace( "" ) );
		
	}

	
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(view);

	}


}
