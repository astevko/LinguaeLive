package ca.jhosek.linguaelive.activity.mainregion;

import java.util.logging.Logger;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;

import ca.jhosek.linguaelive.domain.CurrentState;
import ca.jhosek.linguaelive.place.InstructorCourseLinkPlace;
import ca.jhosek.linguaelive.place.InstructorYourCoursePlace;
import ca.jhosek.linguaelive.ui.priv.instructor.InstructorCourseLinkView;
import ca.jhosek.linguaelive.ui.priv.instructor.InstructorCourseLinkViewImpl;
import ca.jhosek.linguaelive.ui.priv.instructor.InstructorCourseLinkViewImpl.Driver;
import ca.jhosek.linguaelive.proxy.AppRequestFactory;
import ca.jhosek.linguaelive.proxy.CourseLinkProxy;
import ca.jhosek.linguaelive.proxy.CourseLinkRequestContext;
import ca.jhosek.linguaelive.proxy.CourseProxy;
import ca.jhosek.linguaelive.proxy.MemberProxy;
import ca.jhosek.linguaelive.proxy.MemberRequestContext;
import ca.jhosek.linguaelive.proxy.UserProxy;
import ca.jhosek.linguaelive.proxy.UserRequestContext;

/**
 * instructor's view of a course and related information
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see InstructorCourseLinkView
 * @see InstructorCourseLinkViewImpl
 * @see InstructorCourseLinkPlace
 *
 * @see MemberRequestContext
 * @see UserRequestContext
 * @see CourseLinkProxy
 * @see CourseProxy
 * @see MemberProxy
 * 
 */
public class InstructorCourseLinkActivity extends AbstractActivity implements InstructorCourseLinkView.Presenter {

	private static final Logger logger = Logger.getLogger( InstructorCourseLinkActivity.class.getName() );
	
	private final InstructorCourseLinkView view;

	private final PlaceController placeController;

	private final AppRequestFactory requestFactory;

	private final CurrentState currentState;

	private final String token;
	
	protected final String courseId;

	protected CourseLinkProxy myCourseLink;

	private Driver courseLinkProxyDriver;

	private final EventBus eventBus;


	/**
	 * Guice/Gin Factory Module Builder
	 * {@link} http://google-guice.googlecode.com/svn/trunk/javadoc/com/google/inject/assistedinject/FactoryModuleBuilder.html
	 * @see FactoryModuleBuilder
	 * @see GinFactoryModuleBuilder
	 *  
	 */
	public interface Factory {
		InstructorCourseLinkActivity create( String courseLinkId, Long courseId );
	}
	
	@Inject
	public InstructorCourseLinkActivity(
			EventBus eventBus,
			InstructorCourseLinkView view, 
			PlaceController placeController,
			AppRequestFactory requestFactory,
			CurrentState currentState,
			@Assisted String courseLinkId,
			@Assisted Long courseId
			) {
		super();
		this.eventBus = eventBus;
		logger.info("InstructorCourseLinkActivity( token=" + courseLinkId + ", courseId=" + courseId );
		this.view = view;
		this.placeController = placeController;
		this.requestFactory = requestFactory;
		this.currentState = currentState;
		this.token = courseLinkId;
		this.courseId = courseId.toString();
		view.setPresenter(this);
		this.courseLinkProxyDriver = view.createEditorDriver(eventBus, requestFactory);
	}

	
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		logger.info("LinkActivity starting( token=" + token + ", courseId=" + courseId );
		panel.setWidget(view);
		try {
			// convert course token to Long id
			final Long courseLinkId = Long.valueOf(token);
			// obtain course context
			final CourseLinkRequestContext courseContext = requestFactory.courseLinkRequest();
			
			final Request<CourseLinkProxy> request = courseContext.findCourseLink(courseLinkId).with("courseA", "courseB", "courseA.owner", "courseB.owner");
			request.fire( new Receiver< CourseLinkProxy >() {

				
				public void onSuccess(CourseLinkProxy response) {
					// 
					final Long uid = currentState.getLoggedInUser().getId();
					// clear the view
					view.clear();						
					// working on this course
					myCourseLink = response;
					// test for success
					if ( response == null ) {
						logger.severe("course link id not found: " + courseLinkId.toString() );
						Window.alert("Bad course link id" );
						placeController.goTo( currentState.getHomePlace() );
						
						
					} else if ( !currentState.isAdminUser() && 
							!myCourseLink.getCourseA().getOwner().getId().equals( uid ) &&				// course A owner 
							!myCourseLink.getCourseB().getOwner().getId().equals( uid ) 				// course B owner
							) {
						// access violation
						logger.warning( "Access Violation - by:" + currentState.getLoggedInUser().getId() + " attempt course link id=" + response.getId() ); 
						Window.alert( "Access Violation: You are not authorized to view this course link." );
						placeController.goTo( currentState.getHomePlace() );
						
					} else {
						// show course in editor
//						CourseLinkRequestContext courseLinkContext = requestFactory.courseLinkRequest();						
//						courseLinkContext.persist(response);
						logger.info( "Displaying course link - courseA id=" + response.getCourseA().getId() + ", courseId=" + courseId );
 						courseLinkProxyDriver.display(response);
						boolean isSender = (response.getCourseA().getId().toString().equals(courseId) );
						
						logger.info( "isSender=" + isSender + ", isPending=" + response.getPending() + ", isAccepted=" + response.getAccepted() );
						view.setViewMode( isSender, response.getPending(), response.getAccepted());
					}
				}

			});
			
		} catch (NumberFormatException e) {
			//
			logger.severe("bad token not a course link id: " + token );
			Window.alert("Bad or unknown course link id" );
			placeController.goTo( currentState.getHomePlace());
		}		
	}

	
	public void respondToCourseLinkInvite( Boolean accept) {
		logger.info("respondToCourseLinkInvite( " + accept + " )");
		
		CourseLinkRequestContext courseLinkContext = requestFactory.courseLinkRequest();
		CourseLinkProxy editCourseLink = courseLinkContext.edit(myCourseLink);
		
		// update CourseLink and save it... update display too...
		editCourseLink.setAccepted(accept);
		editCourseLink.setPending(false);
		
		courseLinkContext.saveAndReturn(editCourseLink).to(new Receiver<CourseLinkProxy>() {

			
			public void onSuccess(CourseLinkProxy response) {
				// 
				myCourseLink = response;
				Window.alert("Status changed, refreshing display");
				// placeController.goTo( );
				eventBus.fireEvent(new PlaceChangeEvent(new InstructorCourseLinkPlace(token, courseId )));
			}
			
		}).fire();
		
	}

	
	public void goToCourseA() {
		// 
		if(myCourseLink!=null){
			placeController.goTo( new InstructorYourCoursePlace(myCourseLink.getCourseA()));
		}
		
	}

	
	public void goToCourseB() {
		if(myCourseLink!=null){
			placeController.goTo( new InstructorYourCoursePlace(myCourseLink.getCourseB()));
		}
		
	}

	
	public UserProxy getLoggedInUser() {
		return currentState.getLoggedInUser();
	}

}
