package ca.jhosek.main.client.activity.mainregion;

import java.util.ArrayList;
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
import com.google.web.bindery.requestfactory.shared.ServerFailure;

import ca.jhosek.main.client.domain.CurrentState;
import ca.jhosek.main.client.place.AddEditCoursePlace;
import ca.jhosek.main.client.place.InstructorCourseDetailReportPlace;
import ca.jhosek.main.client.place.InstructorCourseLinkPlace;
import ca.jhosek.main.client.place.InstructorCourseSummaryReportPlace;
import ca.jhosek.main.client.place.InstructorInviteCoursePlace;
import ca.jhosek.main.client.place.InstructorMemberPlace;
import ca.jhosek.main.client.place.InstructorYourCoursePlace;
import ca.jhosek.main.client.ui.priv.instructor.InstructorYourCourseView;
import ca.jhosek.main.client.ui.priv.instructor.InstructorYourCourseViewImpl;
import ca.jhosek.main.client.ui.priv.instructor.InstructorYourCourseViewImpl.Driver;
import ca.jhosek.main.shared.UserType;
import ca.jhosek.main.shared.proxy.AppRequestFactory;
import ca.jhosek.main.shared.proxy.CourseLinkProxy;
import ca.jhosek.main.shared.proxy.CourseProxy;
import ca.jhosek.main.shared.proxy.CourseRequestContext;
import ca.jhosek.main.shared.proxy.MemberProxy;
import ca.jhosek.main.shared.proxy.MemberRequestContext;
import ca.jhosek.main.shared.proxy.UserProxy;
import ca.jhosek.main.shared.proxy.UserRequestContext;

/**
 * instructor's view of a course and related information
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see InstructorYourCourseView
 * @see InstructorYourCourseViewImpl
 * @see InstructorYourCoursePlace
 *
 * @see MemberRequestContext
 * @see UserRequestContext
 * @see CourseLinkProxy
 * @see CourseProxy
 * @see MemberProxy
 * 
 */
public class InstructorYourCourseActivity extends AbstractActivity implements InstructorYourCourseView.Presenter {

	private static final String SERVER_COMMUNICATION_ERROR_WHILE_LINKING_COURSES_PLEASE_TRY_AGAIN
		= "Server communication error while linking courses. please try again.";

	private static final Logger logger = Logger.getLogger( InstructorYourCourseActivity.class.getName() );

	protected static final String SERVER_COMMUNICATION_ERROR_WHILE_DELETING_COURSES_PLEASE_TRY_AGAIN 
		= "Server communication error while deleting your course. Please try again.";

	
	private final InstructorYourCourseView view;

	private final PlaceController placeController;

	private final AppRequestFactory requestFactory;

	private final CurrentState currentState;

	private final String token;

	private final Driver courseProxyDriver;

	protected CourseProxy myCourse;

	protected List<CourseProxy> unlinkedCourses = new ArrayList<CourseProxy>();

	protected List<CourseLinkProxy> linkedCourses = new ArrayList<CourseLinkProxy>();

	/**
	 * Guice/Gin Factory Module Builder
	 * {@link} http://google-guice.googlecode.com/svn/trunk/javadoc/com/google/inject/assistedinject/FactoryModuleBuilder.html
	 * @see FactoryModuleBuilder
	 * @see GinFactoryModuleBuilder
	 *  
	 */
	public interface Factory {
		InstructorYourCourseActivity create( String courseId );
	}
	
	@Inject
	public InstructorYourCourseActivity(
			EventBus eventBus,
			InstructorYourCourseView view, 
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
		this.courseProxyDriver = view.createEditorDriver(eventBus, requestFactory);
	}

	
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(view);
		try {
			final UserProxy currentUser = currentState.getLoggedInUser();
			
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
						// if this is your course or you are an admin
						view.setViewerMode(
								response.getId(),
								(currentUser.getUserType() == UserType.INSTRUCTOR
										|| currentUser.getUserType() == UserType.ADMIN),		// is user an instructor or admin 
								(currentUser.getId().equals(response.getOwner().getId()) ||
										currentUser.getUserType() == UserType.ADMIN
										));		// is user owner of course

						// show course in editor
//						CourseRequestContext courseContext = requestFactory.courseRequest();						
//						courseContext.persist(response);
						courseProxyDriver.display(response);
						// load linked courses
						loadLinkedCourses( response );
						// load students
						loadMembers( response );
						// load unlinked courses
						// loadUnlinkedCourses( response );
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
	 * display members of this course
	 * @param course
	 */
	private void loadMembers(CourseProxy course) {
		logger.info("loadMembers( " + course.getName() + " )");
		// 
		MemberRequestContext memberContext = requestFactory.memberRequest();
		Request<List<MemberProxy>> memberRequest = memberContext.getMembersOfCourse( course ).with("user");
		memberRequest.fire( new Receiver<List<MemberProxy>>() {

			
			public void onSuccess(List<MemberProxy> response) {
				logger.info("loadMembers() fetched  " + response.size() + " MemberProxy" );
				// 
				view.showMembers(response);
			}
		});
	}

	/**
	 * display linked courses 
	 * @param course
	 */
	private void loadLinkedCourses(final CourseProxy course) {
		logger.info("loadLinkedCourses( " + course.getName() + " )");
		// 
		CourseRequestContext courseContext = requestFactory.courseRequest();
		courseContext.listLinkedCourses(course).with("courseA","courseB").fire( new Receiver<List<CourseLinkProxy>>() {

			@Override
			public void onSuccess(List<CourseLinkProxy> response) {
				if(response==null) {
					linkedCourses = new ArrayList<CourseLinkProxy>();
					
				} else {
					logger.info("loadLinkedCourses() fetched  " + response.size() + " CourseLinks" );
					//
					linkedCourses = response;
				}
				view.showLinkedCourses(response, false);
				
				// load unlinked courses
				loadUnlinkedCourses( course, false );
			}
		});
	}
	
	/**
	 * view reloads unlinked courses
	 */
	@Override
	public void loadUnlinkedCourses() {
		loadUnlinkedCourses(myCourse, view.isShowPastCourses());
	}
	/**
	 * display unlinked courses
	 *  
	 * @param course the course to match
	 * @param showCurrent show current vs past courses
	 */
	private void loadUnlinkedCourses(final CourseProxy course, final Boolean showCurrent) {
		logger.info("loadUnlinkedCourses( " + course.getName() + " )" );
		// 
		CourseRequestContext courseContext = requestFactory.courseRequest();
		courseContext.listUnlinkedCourses(course, showCurrent).with("owner","courseA","courseB").fire( new Receiver<List<CourseProxy>>() {

			@Override
			public void onSuccess(List<CourseProxy> response) {
				// save good response courses into unlinked courses collection
				unlinkedCourses = new ArrayList<CourseProxy>();
				if(response==null || response.isEmpty() ) {
					// do no filtering
				} else {
					logger.info("loadUnlinkedCourses() fetched  " + response.size() + " Courses" );
					// search for matching partner courses
					for (CourseProxy course : response) {
						final Long unlinkedId = course.getId();
						boolean found = false;
						// find other linked
						for ( CourseLinkProxy cl : linkedCourses ){
							found = cl.getCourseA().getId().equals(unlinkedId) || 
								 cl.getCourseB().getId().equals(unlinkedId);
							if (found) break;
						}
						// not found matching course link so this is good to show.
						if (!found) {
							unlinkedCourses.add(course);
						}
					}
				}					
				view.showUnlinkedCourses( unlinkedCourses, true );
			}
		});
	}
		
	
	public void goToCourseLinkView(CourseLinkProxy courseLink ) {
		logger.info("goToCourseLinkView( linkid=" + courseLink.getId() + ", myCourseId=" +  myCourse.getId() + " )");
		placeController.goTo( new InstructorCourseLinkPlace(courseLink, myCourse));
	}

	/**
	 * flush and save the edits
	 * @see ca.jhosek.main.client.ui.priv.instructor.InstructorYourCourseView.Presenter#saveCourseEdits()
	 */
	
	public void gotoEditCourse() {
		logger.info("gotoEditCourse()");
		if ( !myCourse.getOwner().getId().equals( currentState.getLoggedInUser().getId()) && 
				currentState.getLoggedInUser().getUserType() != UserType.ADMIN ) {
			// current user is not the owner nor administrator
			Window.alert("Sorry - cannot edit course you do not own.");
			return;
		}
		placeController.goTo( new AddEditCoursePlace( myCourse ));
	}
//		// flush/save the edits
//		courseProxyDriver.flush().fire( new Receiver<Void>() {
//
//			@Override
//			public void onSuccess(Void response) {
//				logger.info("course edits saved");
//				// flush failed w/ errors
//				if (courseProxyDriver.hasErrors()) {
//					logger.warning("saveCourseEdits() onSuccess() with hasErrors() after flush()" );
//					Window.alert("errors detected locally in input");
//					return;
//				}
//				
//			}
//			
//			@Override
//			public void onViolation( final Set<Violation> errors ) {
//				logger.warning("saveCourseEdits().flush() onViolation()" );
//				// TODO - remove
//				Window.alert("saveCourseEdits().flush() errors detected");
//				for( Violation error : errors ){
//					Window.alert("flush says: " + error.getMessage() );
//				}
//				courseProxyDriver.setViolations(errors);
//			}
//			
//		});
//	}

	/**
	 * @see ca.jhosek.main.client.ui.priv.instructor.InstructorYourCourseView.Presenter#goToMemberView(ca.jhosek.main.shared.proxy.MemberProxy)
	 */
	
	public void goToMemberView(MemberProxy member) {
		logger.info( "goToMemberView ( )" );
		placeController.goTo( new InstructorMemberPlace(member) );
		
	}

	
	public void goToInviteStudents() {
		logger.info( "goToInviteStudents ()" );
		if ( !myCourse.getOwner().getId().equals( currentState.getLoggedInUser().getId()) && 
				currentState.getLoggedInUser().getUserType() != UserType.ADMIN ) {
			// current user is not the owner nor administrator
			Window.alert("Sorry - cannot invite students for course you do not own.");
			return;
		}
		// 
		placeController.goTo( new InstructorInviteCoursePlace(myCourse));
		
	}

	
	public void inviteToLinkCourses(final CourseProxy unlinkedCourse, String personalMessage ) {
		if ( !myCourse.getOwner().getId().equals( currentState.getLoggedInUser().getId()) && 
				!currentState.isAdminUser() ) {
			// current user is not the owner nor administrator
			Window.alert("Sorry - cannot invite to link courses you do not own.");
			return;
		}
		logger.info( "inviteToLinkCourses( " + myCourse.getName() + " with " + unlinkedCourse.getName() + ")");
		// 
		CourseRequestContext courseContext = requestFactory.courseRequest();
		courseContext.requestToLinkedCourses(myCourse, unlinkedCourse, personalMessage).with("courseA","courseB").fire( new Receiver<CourseLinkProxy>() {

			@Override
			public void onSuccess(CourseLinkProxy response) {
				logger.info( "requestToLinkedCourses() success" );
				linkedCourses.add(response);
				unlinkedCourses.remove(unlinkedCourse);
				// redirect to the new course link display
				// placeController.goTo( new InstructorCourseLinkPlace( response, myCourse ) );

				// update view.
				view.showLinkedCourses(linkedCourses, false);
				view.showUnlinkedCourses(unlinkedCourses, true);
			}
			
			/** 
			 * server error handler
			 * @see com.google.web.bindery.requestfactory.shared.Receiver#onFailure(com.google.web.bindery.requestfactory.shared.ServerFailure)
			 */
			
			public void onFailure(ServerFailure error) {
				//
				logger.warning(SERVER_COMMUNICATION_ERROR_WHILE_LINKING_COURSES_PLEASE_TRY_AGAIN);
				Window.alert(SERVER_COMMUNICATION_ERROR_WHILE_LINKING_COURSES_PLEASE_TRY_AGAIN);
			}						
		});
		
	}

	
	public CourseProxy getMyCourse() {
		// 
		return myCourse;
	}

	
	public void gotoStudentReport() {
		logger.info( "goToStudentReport( )" );
		placeController.goTo( new InstructorCourseSummaryReportPlace(myCourse));
	}

	
	public void gotoSessionReport() {
		logger.info( "goToSessionReport( )" );
		placeController.goTo( new InstructorCourseDetailReportPlace(myCourse));
	}


	@Override
	public void deleteCourse() {
		if ( !myCourse.getOwner().getId().equals( currentState.getLoggedInUser().getId()) && 
				!currentState.isAdminUser() ) {
			// current user is not the owner nor administrator
			Window.alert("Sorry - cannot delete courses you do not own.");
			return;
		}
		logger.warning( "deleteCourse( " + myCourse.getName() +  ")");
		// 
		CourseRequestContext courseContext = requestFactory.courseRequest();
		courseContext.remove(myCourse).fire( new Receiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				logger.info( "requestToLinkedCourses() success" );
				Window.alert("Delete course successful.");
				// redirect to the new course link display
				placeController.goTo(currentState.getHomePlace() );
			}
			
			/** 
			 * server error handler
			 * @see com.google.web.bindery.requestfactory.shared.Receiver#onFailure(com.google.web.bindery.requestfactory.shared.ServerFailure)
			 */
			
			public void onFailure(ServerFailure error) {
				//
				logger.warning(SERVER_COMMUNICATION_ERROR_WHILE_DELETING_COURSES_PLEASE_TRY_AGAIN);
				Window.alert(SERVER_COMMUNICATION_ERROR_WHILE_DELETING_COURSES_PLEASE_TRY_AGAIN);
				placeController.goTo(currentState.getHomePlace() );
			}						
		});
		
	}

}
