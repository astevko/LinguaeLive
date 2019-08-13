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
import ca.jhosek.linguaelive.place.AddEditCoursePlace;
import ca.jhosek.linguaelive.place.InstructorCourseLinkPlace;
import ca.jhosek.linguaelive.place.InstructorYourCoursePlace;
import ca.jhosek.linguaelive.ui.priv.instructor.InstructorHomeView;
import ca.jhosek.linguaelive.proxy.AppRequestFactory;
import ca.jhosek.linguaelive.proxy.CourseLinkProxy;
import ca.jhosek.linguaelive.proxy.CourseLinkRequestContext;
import ca.jhosek.linguaelive.proxy.CourseProxy;
import ca.jhosek.linguaelive.proxy.CourseRequestContext;
import ca.jhosek.linguaelive.proxy.UserProxy;

/**
 * Activities on the Instructor home page
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class InstructorHomeActivity extends AbstractActivity implements InstructorHomeView.Presenter {

	private static final Logger logger = Logger.getLogger( InstructorHomeActivity.class.getName() );
	
	
	private InstructorHomeView view;
	private PlaceController placeController;


	private final CurrentState currentState;


	private final AppRequestFactory requestFactory;

	@Inject
	public InstructorHomeActivity( 
			InstructorHomeView view, 
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
		loadPendingLinkedCourses( currentState.getLoggedInUser() );
		loadOpenLinkedCourses( currentState.getLoggedInUser() );
		loadCourses(currentState.getLoggedInUser());
	}
	
	
	/**
	 * load courses for this user
	 * @param loggedInUser
	 */
	private void loadCourses(UserProxy loggedInUser) {
		// obtain course context
		CourseRequestContext courseContext = requestFactory.courseRequest();
		// set query
		Request<List<CourseProxy>> request = courseContext.listMyCourses( currentState.getLoggedInUser() );
		// execute query
		request.fire( new Receiver<List<CourseProxy>>(){

			@Override
			public void onSuccess(List<CourseProxy> results) {
				// 
				view.showCourseList( results );
			}
			
		});
	}


	public void goToPendingCourseLinkView(CourseLinkProxy courseLink ) {
		logger.info("goToCourseLinkView( linkid=" + courseLink.getId() + " )");
		placeController.goTo( new InstructorCourseLinkPlace(courseLink, courseLink.getCourseA() ));
	}
	
	/**
	 * display pending course link invites sent by this user.  
	 * @param course
	 */
	private void loadPendingLinkedCourses( UserProxy user ) {
		logger.info("loadPendingLinkedCourses( user id=" + user.getId() + " )");
		// 
		CourseLinkRequestContext courseLinkContext = requestFactory.courseLinkRequest();
		courseLinkContext.listPendingLinkedCourses( user )
			.with("courseB").with("courseA")
			.to( new Receiver<List<CourseLinkProxy>>() {

			
			public void onSuccess(List<CourseLinkProxy> pending) {
				logger.info("listPendingLinkedCourses() fetched  " + pending.size() + " CourseLinks" );
				//
				view.showPendingLinkedCourses(pending);				
			}
		}).fire();
	}
	
	
	public void goToOpenCourseLinkView(CourseLinkProxy courseLink ) {
		logger.info("goToOpenCourseLinkView( linkid=" + courseLink.getId() + " )");
		placeController.goTo( new InstructorCourseLinkPlace(courseLink, courseLink.getCourseB() ));
	}
	
	/**
	 * display open linked courses received by this user
	 * @param user
	 */
	private void loadOpenLinkedCourses( UserProxy user ) {
		logger.info("loadOpenLinkedCourses( user id=" + user.getId() + " )");
		// 
		CourseLinkRequestContext courseLinkContext = requestFactory.courseLinkRequest();
		courseLinkContext.listOpenLinkedCourses( user )
			.with("courseB").with("courseA")
			.to( new Receiver<List<CourseLinkProxy>>() {

			
			public void onSuccess(List<CourseLinkProxy> open) {
				logger.info("listOpenLinkedCourses() fetched  " + open.size() + " CourseLinks" );
				//
				view.showOpenLinkedCourses(open);				
			}
		}).fire();
	}


	/***
	 * goto view my course
	 * @see ca.jhosek.linguaelive.ui.priv.MyCoursesView.Presenter#viewCourse(ca.jhosek.linguaelive.proxy.CourseProxy)
	 */	
	@Override
	public void viewCourse(CourseProxy course) {
		// go to instructor view
		placeController.goTo( new InstructorYourCoursePlace(course));
	}


	/**
	 * goto add course 
	 * @see ca.jhosek.linguaelive.ui.priv.instructor.InstructorHomeView.Presenter#goToAddCourse()
	 */
	@Override
	public void goToAddCourse() {
		// goto add course view
		placeController.goTo( new AddEditCoursePlace(""));
	}
}
