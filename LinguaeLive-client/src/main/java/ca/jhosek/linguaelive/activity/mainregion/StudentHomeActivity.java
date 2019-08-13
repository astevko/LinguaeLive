package ca.jhosek.linguaelive.activity.mainregion;

import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

import ca.jhosek.linguaelive.domain.CurrentState;
import ca.jhosek.linguaelive.place.AddEditCoursePlace;
import ca.jhosek.linguaelive.place.PartnerInvitePlace;
import ca.jhosek.linguaelive.place.SessionControlPlace;
import ca.jhosek.linguaelive.place.StudentYourCoursePlace;
import ca.jhosek.linguaelive.ui.priv.student.StudentHomeView;
import ca.jhosek.linguaelive.proxy.AppRequestFactory;
import ca.jhosek.linguaelive.proxy.CourseProxy;
import ca.jhosek.linguaelive.proxy.CourseRequestContext;
import ca.jhosek.linguaelive.proxy.PartnerInviteProxy;
import ca.jhosek.linguaelive.proxy.PartnerInviteRequestContext;
import ca.jhosek.linguaelive.proxy.SessionProxy;
import ca.jhosek.linguaelive.proxy.SessionRequestContext;
import ca.jhosek.linguaelive.proxy.UserProxy;

/**
 * Activities on the Student home page
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class StudentHomeActivity extends AbstractActivity implements StudentHomeView.Presenter {

	private static final Logger logger = Logger.getLogger( StudentHomeActivity.class.getName() );
	
	private final StudentHomeView view;
	private final PlaceController placeController;
	private final AppRequestFactory requestFactory;
	private final CurrentState currentState;

	@Inject
	public StudentHomeActivity( 
			StudentHomeView view, 
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

		loadOpenSessions( currentState.getLoggedInUser() );
		loadPendingSessionInvites( currentState.getLoggedInUser() );
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



	/**
	 * load the sessions for myCourse and currentUser
	 * @param course 
	 */
	protected void loadOpenSessions( UserProxy user  ) {
		logger.info("loadOpenSessions( " + user.getId() +	")");
		
		// display loading sessions
		view.showSessions(null);
		
		// Check for open sessions
		SessionRequestContext sessionRequestContext = requestFactory.sessionRequestContext();
		Request<List<SessionProxy>> req = sessionRequestContext.getOpenSessionsForUser( user );
		req.with( "member1", "member1.user" , "member2", "member2.user" );
		req.to( new Receiver<List<SessionProxy>>() {

			@Override
			public void onSuccess(List<SessionProxy> loadedSessions) {
				// 
				logger.info( "getOpenSessionsForUser() success " + loadedSessions.size() + " Sessions");
				view.showSessions(loadedSessions);
				if (!loadedSessions.isEmpty()) {
					// open session in progress found!!!
					if (Window.confirm("In-progress partner session found. Do you want to go to it?")) {
						placeController.goTo( new SessionControlPlace(loadedSessions.get(0)));
					}
				}
			}
			@Override
			public void onFailure(ServerFailure error) {
				// 
				logger.severe( error.getMessage() );
				Window.alert( "server error getOpenSessionsForUser:\n" + error.getMessage() );
			}
		}).fire();
	}

	/**
	 * load the paired students for myCourse and currentUser
	 * @param course 
	 */
	protected void loadPendingSessionInvites(UserProxy user ) {
		logger.info( "loadPendingSessionInvites( " + user.getId() +	" )" );
		PartnerInviteRequestContext sessionInviteRequestContext = requestFactory.sessionInviteRequestContext();
		
		// display loading message
		view.showSessionInvites(null);
		
		Request<List<PartnerInviteProxy>> req = sessionInviteRequestContext.getPendingSessionInvitesForUser( user );
		req.with( "member1", "member1.user", "member2", "member2.user" );
		req.to( new Receiver<List<PartnerInviteProxy>>()  {

			@Override
			public void onSuccess(List<PartnerInviteProxy> response) {
				//
				logger.info( "getSessionInvitesForMember() success " + response.size() + " SessionInvites");
				view.showSessionInvites(response);
			}
			@Override
			public void onFailure(ServerFailure error) {
				// 
				logger.severe( error.getMessage() );
				Window.alert( "server error:loadSessionInvites\n" + error.getMessage() );
			}
		}).fire();
	}


	public void goToPartnerInvite(PartnerInviteProxy sessionInvite) {
		// display partner invite
		placeController.goTo( new PartnerInvitePlace(sessionInvite));
	}

	public void goToSession(SessionProxy session) {
		// display session control 
		placeController.goTo( new SessionControlPlace( session ));
	}

	public UserProxy getLoggedInUser() {
		//
		return currentState.getLoggedInUser();
	}


	/***
	 * goto view my course
	 * @see ca.jhosek.linguaelive.ui.priv.MyCoursesView.Presenter#viewCourse(ca.jhosek.linguaelive.proxy.CourseProxy)
	 */	
	@Override
	public void viewCourse(CourseProxy course) {
		// go to instructor view
		placeController.goTo( new StudentYourCoursePlace(course));
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
