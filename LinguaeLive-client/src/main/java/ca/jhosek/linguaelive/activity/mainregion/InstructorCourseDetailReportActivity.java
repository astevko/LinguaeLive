package ca.jhosek.main.client.activity.mainregion;

import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;

import ca.jhosek.main.client.domain.CurrentState;
import ca.jhosek.main.client.place.InstructorYourCoursePlace;
import ca.jhosek.main.client.ui.priv.instructor.InstructorCourseDetailReportView;
import ca.jhosek.main.shared.UserType;
import ca.jhosek.main.shared.proxy.AppRequestFactory;
import ca.jhosek.main.shared.proxy.CourseProxy;
import ca.jhosek.main.shared.proxy.CourseRequestContext;
import ca.jhosek.main.shared.proxy.MemberProxy;
import ca.jhosek.main.shared.proxy.SessionProxy;
import ca.jhosek.main.shared.proxy.SessionRequestContext;
import ca.jhosek.main.shared.proxy.UserProxy;
import ca.jhosek.main.shared.proxy.UserRequestContext;

/**
 * instructor's summary report of student activity
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 * @see UserRequestContext
 * @see CourseProxy
 * @see MemberProxy
 * 
 */
public class InstructorCourseDetailReportActivity extends AbstractActivity {

	private static final Logger logger = Logger.getLogger( InstructorCourseDetailReportActivity.class.getName() );
	
	private final InstructorCourseDetailReportView view;

	private final PlaceController placeController;

	private final AppRequestFactory requestFactory;

	private final CurrentState currentState;

	private final String token;

	protected CourseProxy myCourse;
	/**
	 * Guice/Gin Factory Module Builder
	 * {@link} http://google-guice.googlecode.com/svn/trunk/javadoc/com/google/inject/assistedinject/FactoryModuleBuilder.html
	 * @see FactoryModuleBuilder
	 * @see GinFactoryModuleBuilder
	 *  
	 */
	public interface Factory {
		InstructorCourseDetailReportActivity create( String courseId );
	}
	
	@Inject
	public InstructorCourseDetailReportActivity(
			InstructorCourseDetailReportView view, 
			PlaceController placeController,
			AppRequestFactory requestFactory,
			CurrentState currentState,
			@Assisted String courseId
			) {
		super();
		this.view = view;
		this.placeController = placeController;
		this.requestFactory = requestFactory;
		this.currentState = currentState;
		this.token = courseId;
		view.setPresenter(this);
	}

	
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(view);
		try {
			final UserProxy user = currentState.getLoggedInUser();
			
			// convert course token to Long id
			final Long courseId = Long.valueOf(token);
			// obtain course context
			CourseRequestContext courseContext = requestFactory.courseRequest();
			
			Request<CourseProxy> request = courseContext.findCourse(courseId).with("owner");
			request.fire( new Receiver< CourseProxy >() {

				
				public void onSuccess(CourseProxy response) {
					// clear the view
					view.clear();
					// working on this course
					myCourse = response;
					// test for success
					if ( response == null ) {
						logger.severe("course id not found: " + courseId.toString() );
						view.setViewerMode(
								null,
								false,	// is user an instrcutor 
								false );		// is user owner of course
						Window.alert("Bad course id" );
						placeController.goTo( currentState.getHomePlace() );
						
//					} else if ( !response.getOwner().getId().equals( currentState.getLoggedInUser().getId() ) ) {
//						// access violation
//						logger.warning( "Access Violation - by:" + currentState.getLoggedInUser().getId() + " attempt course id=" + response.getId() ); 
//						Window.alert("Access Violation - You are not the instructor of this course.");
//						placeController.goTo( currentState.getHomePlace() );
						
					} else {
						logger.info("retrieved course id=" + response.getId() );
						// if this is your course
						view.showCourse(response);
						view.setViewerMode(
								response.getId(),
								(user.getUserType() == UserType.INSTRUCTOR),		// is user an instrcutor 
								(user.getId().equals(response.getOwner().getId())));		// is user owner of course

						// load sessions
						loadSessions( response );
					}
				}

			});
			
		} catch (NumberFormatException e) {
			//
			logger.severe("bad token not a course id: " + token );
			Window.alert("Bad or unknown course id" );
			placeController.goTo( currentState.getHomePlace());
		}		
	}

	/**
	 * display sessions of this course
	 * @param course
	 */
	private void loadSessions(CourseProxy course) {
		logger.info("loadSessions( " + course.getName() + " )");
		// 
		SessionRequestContext sessionContext = requestFactory.sessionRequestContext();
		Request<List<SessionProxy>> memberRequest = sessionContext.getSessionsForCourse( course ).with("member1","member2","member1.user","member2.user");
		memberRequest.fire( new Receiver<List<SessionProxy>>() {

			
			public void onSuccess(List<SessionProxy> response) {
				logger.info("loadMembers() fetched  " + response.size() + " SessionProxy" );
				// 
				view.showSessions(response);
			}
		});
	}

	/**
	 * take user to course view
	 */
	public void goToCourse() {
		//
		placeController.goTo(new InstructorYourCoursePlace(myCourse));
		
	}		

}
