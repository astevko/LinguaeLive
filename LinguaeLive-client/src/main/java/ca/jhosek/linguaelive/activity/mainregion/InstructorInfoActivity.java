package ca.jhosek.main.client.activity.mainregion;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import ca.jhosek.main.client.place.CourseBrowsePlace;
import ca.jhosek.main.client.place.NewInstructorPlace;
import ca.jhosek.main.client.ui.anon.InstructorInfoView;

/**
 * InstructorInfo Activity
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see InstructorInfoView
 */
public class InstructorInfoActivity extends AbstractActivity implements InstructorInfoView.Presenter {

	private InstructorInfoView view;
	private PlaceController placeController;

	@Inject
	public InstructorInfoActivity( InstructorInfoView view, PlaceController placeController) {
		super();
		this.view = view;
		this.placeController = placeController;
		view.setPresenter(this);
	}

	
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(view);

	}

	
	public void goToNewInstructorForm() {
		// 
		placeController.goTo( new NewInstructorPlace() );
		
	}

	
	public void goToBrowseCourse() {
		// 
		placeController.goTo( new CourseBrowsePlace() 	);
		
	}


}
