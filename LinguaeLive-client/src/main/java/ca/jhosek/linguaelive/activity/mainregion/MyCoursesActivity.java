package ca.jhosek.linguaelive.activity.mainregion;

import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;

import ca.jhosek.linguaelive.domain.CurrentState;
import ca.jhosek.linguaelive.place.AddCourseMemberPlace;
import ca.jhosek.linguaelive.place.AddEditCoursePlace;
import ca.jhosek.linguaelive.place.InstructorYourCoursePlace;
import ca.jhosek.linguaelive.place.MyCoursesPlace;
import ca.jhosek.linguaelive.place.StudentYourCoursePlace;
import ca.jhosek.linguaelive.ui.priv.MyCoursesView;
import ca.jhosek.linguaelive.ui.priv.MyCoursesViewImpl;
import ca.jhosek.linguaelive.UserType;
import ca.jhosek.linguaelive.proxy.AppRequestFactory;
import ca.jhosek.linguaelive.proxy.CourseProxy;
import ca.jhosek.linguaelive.proxy.CourseRequestContext;

/**
 * list of my courses
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 * @see MyCoursesView
 * @see MyCoursesViewImpl
 * @see MyCoursesActivity
 * @see MyCoursesPlace
 */
public class MyCoursesActivity extends AbstractActivity implements MyCoursesView.Presenter {

	private static final Logger logger = Logger.getLogger( MyCoursesActivity.class.getName() );
	
	private final MyCoursesView view;

	private final PlaceController placeController;

	private final AppRequestFactory requestFactory;

	private final CurrentState currentState;

	@Inject
	public MyCoursesActivity( 
			MyCoursesView view, 
			PlaceController placeController,
			AppRequestFactory requestFactory,
			CurrentState currentState
			
			) {
		super();
		this.view = view;
		this.placeController = placeController;
		this.requestFactory = requestFactory;
		this.currentState = currentState;
		view.setPresenter(this);
	}

	
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(view);

		// obtain course context
		CourseRequestContext courseContext = requestFactory.courseRequest();
		// set query
		Request<List<CourseProxy>> request = courseContext.listMyCourses( currentState.getLoggedInUser() );
		// execute query
		request.fire( new Receiver<List<CourseProxy>>(){

			@Override
			public void onSuccess(List<CourseProxy> results) {
				view.clear();
				// 
				// show all results in the list
				if( results.isEmpty() ) {
					logger.info( "no results returned");
					// disregard loading empty result set.
					//					view.emptyQueryResults();
					// go immediately to add a course;
					addACourse();
				} else {
					view.showCourseList( results );
				}
			}
			
		});
	}

	/** goto add a course
	 * @see ca.jhosek.linguaelive.ui.priv.MyCoursesView.Presenter#addACourse()
	 */
	
	public void addACourse() {
		if( currentState.getLoggedInUser().getUserType() == UserType.INSTRUCTOR ) {
			// instructors create courses 
			placeController.goTo( new AddEditCoursePlace("") );
		} else {
			// students add themselves as a member to a course
			placeController.goTo( new AddCourseMemberPlace() );
		}
	}

	/***
	 * goto view my course
	 * @see ca.jhosek.linguaelive.ui.priv.MyCoursesView.Presenter#viewCourse(ca.jhosek.linguaelive.proxy.CourseProxy)
	 */
	
	public void viewCourse(CourseProxy course) {
		//
		if( currentState.getLoggedInUser().getUserType() == UserType.INSTRUCTOR ) {
			// go to instructor view
			placeController.goTo( new InstructorYourCoursePlace(course));
		} else {
			// go to student view
			placeController.goTo( new StudentYourCoursePlace(course));
		}
		
	}
}
