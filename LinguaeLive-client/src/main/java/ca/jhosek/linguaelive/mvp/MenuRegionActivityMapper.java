package ca.jhosek.linguaelive.mvp;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;

import ca.jhosek.linguaelive.activity.menuregion.AdminMenuActivity;
import ca.jhosek.linguaelive.activity.menuregion.AnonMenuActivity;
import ca.jhosek.linguaelive.activity.menuregion.InstructorMenuActivity;
import ca.jhosek.linguaelive.activity.menuregion.StudentMenuActivity;
import ca.jhosek.linguaelive.domain.CurrentState;

/**
 * this is the menu region of the main view
 * you can have either an anon user or an authenticated user
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class MenuRegionActivityMapper implements ActivityMapper {

	private final Provider<AdminMenuActivity> adminMenuActivityProvider;
	private final Provider<AnonMenuActivity> 	anonMenuActivityProvider;
	private final CurrentState currentState;
	private final Provider<InstructorMenuActivity> instructorMenuActivityProvider;
	private final Provider<StudentMenuActivity> studentMenuActivityProvider;

	@Inject
	public MenuRegionActivityMapper(
			Provider<AdminMenuActivity> adminMenuActivityProvider,
			Provider<AnonMenuActivity> 	anonMenuActivityProvider,
			Provider<InstructorMenuActivity> instructorMenuActivityProvider,
			Provider<StudentMenuActivity> 	studentMenuActivityProvider,
			CurrentState						currentState
	) {
		super();
		this.adminMenuActivityProvider = adminMenuActivityProvider;
		this.anonMenuActivityProvider = anonMenuActivityProvider;
		this.instructorMenuActivityProvider = instructorMenuActivityProvider;
		this.studentMenuActivityProvider = studentMenuActivityProvider;
		this.currentState = currentState;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.activity.shared.ActivityMapper#getActivity(com.google.gwt.place.shared.Place)
	 * 
	 * shows either one of two user panels - anon or authenticated
	 * 
	 */
	public Activity getActivity(Place place) {

		if ( !currentState.isLoggedIn() ) {
			return anonMenuActivityProvider.get();		
			
		} else {
			switch ( currentState.getLoggedInUser().getUserType()) {
			case ADMIN:
				return adminMenuActivityProvider.get();				

			case INSTRUCTOR:
				return instructorMenuActivityProvider.get();				

			case STUDENT:
				return studentMenuActivityProvider.get();				

			case ANON:				
			default:
				return anonMenuActivityProvider.get();
			}
		}
	}
}


