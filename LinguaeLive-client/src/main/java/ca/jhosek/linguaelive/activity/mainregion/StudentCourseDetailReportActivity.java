package ca.jhosek.linguaelive.activity.mainregion;

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

import ca.jhosek.linguaelive.domain.CurrentState;
import ca.jhosek.linguaelive.place.SessionControlPlace;
import ca.jhosek.linguaelive.place.StudentYourCoursePlace;
import ca.jhosek.linguaelive.ui.priv.student.StudentCourseDetailReportView;
import ca.jhosek.linguaelive.UserType;
import ca.jhosek.linguaelive.proxy.AppRequestFactory;
import ca.jhosek.linguaelive.proxy.CourseProxy;
import ca.jhosek.linguaelive.proxy.MemberProxy;
import ca.jhosek.linguaelive.proxy.MemberRequestContext;
import ca.jhosek.linguaelive.proxy.SessionProxy;
import ca.jhosek.linguaelive.proxy.SessionRequestContext;
import ca.jhosek.linguaelive.proxy.UserProxy;
import ca.jhosek.linguaelive.proxy.UserRequestContext;

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
public class StudentCourseDetailReportActivity extends AbstractActivity {

	private static final Logger logger = Logger.getLogger( StudentCourseDetailReportActivity.class.getName() );
	
	private final StudentCourseDetailReportView view;

	private final PlaceController placeController;

	private final AppRequestFactory requestFactory;

	private final CurrentState currentState;

	private final String token;

	protected MemberProxy myMember;
	/**
	 * Guice/Gin Factory Module Builder
	 * {@link} http://google-guice.googlecode.com/svn/trunk/javadoc/com/google/inject/assistedinject/FactoryModuleBuilder.html
	 * @see FactoryModuleBuilder
	 * @see GinFactoryModuleBuilder
	 *  
	 */
	public interface Factory {
		StudentCourseDetailReportActivity create( String courseId );
	}
	
	@Inject
	public StudentCourseDetailReportActivity(
			StudentCourseDetailReportView view, 
			PlaceController placeController,
			AppRequestFactory requestFactory,
			CurrentState currentState,
			@Assisted String memberId
			) {
		super();
		this.view = view;
		this.placeController = placeController;
		this.requestFactory = requestFactory;
		this.currentState = currentState;
		this.token = memberId;
		view.setPresenter(this);
	}

	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(view);
		try {
			final UserProxy user = currentState.getLoggedInUser();
			
			// convert member token to Long id
			final Long memberId = Long.valueOf(token);
			// obtain course context
			MemberRequestContext memberContext = requestFactory.memberRequest();
			
			Request<MemberProxy> request = memberContext.findMember(memberId).with("user","course");
			request.fire( new Receiver< MemberProxy >() {

				@Override
				public void onSuccess(MemberProxy response) {
					// clear the view
					view.clear();
					// working on this course
					myMember = response;
					// test for success
					if ( response == null ) {
						logger.severe("member id not found: " + memberId.toString() );
						view.setViewerMode(
								null,
								false,	// is user an instrcutor 
								false );		// is user owner of course
						Window.alert("Bad member id" );
						placeController.goTo( currentState.getHomePlace() );
						
//					} else if ( !response.getOwner().getId().equals( currentState.getLoggedInUser().getId() ) ) {
//						// access violation
//						logger.warning( "Access Violation - by:" + currentState.getLoggedInUser().getId() + " attempt course id=" + response.getId() ); 
//						Window.alert("Access Violation - You are not the instructor of this course.");
//						placeController.goTo( currentState.getHomePlace() );
						
					} else {
						logger.info("retrieved member id=" + response.getId() );
						// if this is your course
						view.showCourse(response.getCourse());
						view.showUser(response.getUser());
						view.setViewerMode(
								response.getId(),
								(user.getUserType() == UserType.INSTRUCTOR),		// is user an instrcutor 
								(user.getId().equals(response.getUser().getId())));		// is user owner of course

						// load sessions
						loadSessions( response );
					}
				}

			});
			
		} catch (NumberFormatException e) {
			//
			logger.severe("bad token not a member id: " + token );
			Window.alert("Bad or unknown member id" );
			placeController.goTo( currentState.getHomePlace());
		}		
	}

	/**
	 * display sessions of this course
	 * @param response
	 */
	private void loadSessions(MemberProxy response) {
		logger.info("loadSessions( " + response.getUser().getLastName() + ", " + response.getCourse().getDescription() + " )");
		// 
		SessionRequestContext sessionContext = requestFactory.sessionRequestContext();
		Request<List<SessionProxy>> memberRequest = sessionContext.getSessionsForMember( response ).with("member1","member2","member1.user","member2.user");
		memberRequest.fire( new Receiver<List<SessionProxy>>() {

			@Override
			public void onSuccess(List<SessionProxy> response) {
				logger.info("getSessionsForMember() fetched  " + response.size() + " SessionProxy" );
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
		placeController.goTo(new StudentYourCoursePlace(myMember.getCourse()));
		
	}

	/**
	 * @param session the session to open the control panel to 
	 */
	public void goToSessionControlPanel(SessionProxy session) {
		// 
		if (session != null) {
			placeController.goTo(new SessionControlPlace(session));
		}
	}		

}
