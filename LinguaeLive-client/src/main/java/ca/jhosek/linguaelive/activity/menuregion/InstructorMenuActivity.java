/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.activity.menuregion;



import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import ca.jhosek.main.client.place.InstructorHomePlace;
import ca.jhosek.main.client.place.InstructorStartPlace;
import ca.jhosek.main.client.place.InstructorTipsPlace;
import ca.jhosek.main.client.place.MyCoursesPlace;
import ca.jhosek.main.client.place.MyProfilePlace;
import ca.jhosek.main.client.ui.priv.instructor.InstructorMenuView;

/**
 * menu activity of admin users
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class InstructorMenuActivity extends AbstractActivity implements InstructorMenuView.Presenter {
	private final InstructorMenuView view;
	private final PlaceController placeController;
	
	/**
	 * 
	 */
	@Inject
	public InstructorMenuActivity( InstructorMenuView view, PlaceController placeController ) {
		this.view = view;
		this.placeController = placeController;
		// 
		view.setPresenter( this );
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.activity.shared.Activity#start(com.google.gwt.user.client.ui.AcceptsOneWidget, com.google.web.bindery.event.shared.EventBus)
	 */
	
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(view);
		
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.main.client.ui.priv.admin.InstructorMenuView.Presenter#goToInstructorHome()
	 */
	
	public void goToInstructorHome() {
		// 
		placeController.goTo( new InstructorHomePlace());
		
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.main.client.ui.priv.admin.InstructorMenuView.Presenter#goToQueryUsers()
	 */
	
	public void goToMyProfile() {
		// 
		placeController.goTo( new MyProfilePlace());

		
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.main.client.ui.priv.instructor.InstructorMenuView.Presenter#goToMyClasses()
	 */
	
	public void goToMyClasses() {
		placeController.goTo( new MyCoursesPlace());
		
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.main.client.ui.priv.instructor.InstructorMenuView.Presenter#goToInstructorStart()
	 */
	
	public void goToInstructorStart() {
		placeController.goTo( new InstructorStartPlace() );
		
	}

	/**
	 * instructor tips
	 * @see ca.jhosek.main.client.ui.priv.instructor.InstructorMenuView.Presenter#goToInstructorTips()
	 */
	
	public void goToInstructorTips() {
		//
		placeController.goTo( new InstructorTipsPlace() );
	}
}
