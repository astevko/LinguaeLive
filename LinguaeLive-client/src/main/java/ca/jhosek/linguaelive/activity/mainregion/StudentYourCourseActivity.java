package ca.jhosek.linguaelive.activity.mainregion;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

import ca.jhosek.linguaelive.domain.CurrentState;
import ca.jhosek.linguaelive.place.PartnerInvitePlace;
import ca.jhosek.linguaelive.place.SessionControlPlace;
import ca.jhosek.linguaelive.place.StudentCourseDetailReportPlace;
import ca.jhosek.linguaelive.place.StudentYourCoursePlace;
import ca.jhosek.linguaelive.ui.priv.student.HourOfDay;
import ca.jhosek.linguaelive.ui.priv.student.StudentYourCourseView;
import ca.jhosek.linguaelive.ui.priv.student.StudentYourCourseViewImpl;
import ca.jhosek.linguaelive.ui.priv.student.StudentYourCourseViewImpl.Driver;
import ca.jhosek.linguaelive.proxy.AppRequestFactory;
import ca.jhosek.linguaelive.proxy.MemberProxy;
import ca.jhosek.linguaelive.proxy.MemberRequestContext;
import ca.jhosek.linguaelive.proxy.PartnerInviteProxy;
import ca.jhosek.linguaelive.proxy.PartnerInviteRequestContext;
import ca.jhosek.linguaelive.proxy.SessionProxy;
import ca.jhosek.linguaelive.proxy.UserProxy;
import ca.jhosek.linguaelive.proxy.UserRequestContext;

/**
 * student's view of a course member and associated objects 
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see StudentYourCourseView
 * @see StudentYourCourseViewImpl
 * @see StudentYourCoursePlace
 *
 * @see MemberRequestContext
 * @see UserRequestContext
 * @see MemberProxy
 * @see UserProxy
 * @see PartnerInviteRequestContext
 * 
 */
public class StudentYourCourseActivity extends AbstractActivity implements StudentYourCourseView.Presenter {
	
	private static final String SERVER_COMMUNICATION_ERROR_WHILE_LINKING_COURSES_PLEASE_TRY_AGAIN = "Server communication error while deleting course membership. please try again.";

	private static final Logger logger = Logger.getLogger( StudentYourCourseActivity.class.getName() );

	private final StudentYourCourseView view;

	private final PlaceController placeController;

	private final AppRequestFactory requestFactory;

	private final CurrentState currentState;

	private final String token;

	private final Driver memberEditorDriver;

	protected MemberProxy currentMember;

	/**
	 * list of students available for pairing
	 */
	protected List<MemberProxy> availableStudents = new ArrayList<MemberProxy>();

	/**
	 * list of session invites for this course (sent and received)
	 */
	protected List<PartnerInviteProxy> sessionInvites = new ArrayList<PartnerInviteProxy>();

	/**
	 * list of sessions for this course
	 */
	protected List<SessionProxy> sessions = new ArrayList<SessionProxy>();

	/**
	 * Guice/Gin Factory Module Builder
	 * {@link} http://google-guice.googlecode.com/svn/trunk/javadoc/com/google/inject/assistedinject/FactoryModuleBuilder.html
	 * @see FactoryModuleBuilder
	 * @see GinFactoryModuleBuilder
	 *  
	 */
	public interface Factory {
		StudentYourCourseActivity create( String courseId );
	}

	@Inject
	public StudentYourCourseActivity(
			EventBus eventBus,
			StudentYourCourseView view, 
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
		this.memberEditorDriver = view.createEditorDriver(eventBus, requestFactory);
	}

	/**
	 * START
	 * @see com.google.gwt.activity.shared.Activity#start(com.google.gwt.user.client.ui.AcceptsOneWidget, com.google.web.bindery.event.shared.EventBus)
	 */
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(view);
		loadMember();		
	}

	/**
	 * step 1 - load the MemberProxy via the MemberRequestContext
	 */
	private void loadMember() {
		try {
			// convert course token to Long id
			final Long courseId = Long.valueOf(token);
			// obtain course context
			MemberRequestContext memberContext = requestFactory.memberRequest();
			// find the member of this course for the logged in user
			Request<MemberProxy> request = memberContext
				.findCourseMember( currentState.getLoggedInUser().getId(), courseId)
				.with("course","user","course.owner");
			request.fire( new Receiver< MemberProxy >() {

				@Override
				public void onSuccess(MemberProxy memberResponse) {
					// clear the view
					view.clear();
					
					// working on this course
					currentMember = memberResponse;
					
					// test for success
					if ( memberResponse == null ) {
						logger.severe("Member not found - course id:" + courseId.toString() + ", user id:" + currentState.getLoggedInUser().getId() );
						//						view.setViewerMode( 
						//								false,	// is user an instrcutor 
						//								false );		// is user owner of course
						Window.alert("You are not member of this course." );
						placeController.goTo( currentState.getHomePlace() );
						
					} else if ( !memberResponse.getUser().getId().equals( currentState.getLoggedInUser().getId() )) {
						// access violation
						logger.warning( "Access Violation - by:" + currentState.getLoggedInUser().getId() + " attempt member id=" + memberResponse.getId() ); 
						Window.alert( "Access Violation: You are not authorized to view this member." );
						placeController.goTo( currentState.getHomePlace() );
						
					} else {
						logger.info("retrieved member id=" + memberResponse.getId() );
						// if this is your course
						//						view.setViewerMode( 
						//								(user.getUserType() == UserType.INSTRUCTOR),		// is user an instrcutor 
						//								(user.getId()==response.getOwner().getId()));		// is user owner of course

						// show course in editor
						MemberRequestContext persistContext = requestFactory.memberRequest();						
						persistContext.persist(memberResponse);
						memberEditorDriver.edit(currentMember, persistContext);
//						memberEditorDriver.display(memberResponse);

						// load availability
						loadSchedule( memberResponse );
						// load the paired students from the database
						loadSessionInvites( memberResponse );
						// load the sessions from the database
//						loadSessions( memberResponse  );
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
	 * step 2 load the schedule
	 * 
	 * @param memberResponse
	 */
	private void loadSchedule(MemberProxy memberResponse) {
		Set<Integer> schedule = memberResponse.getAvailableHoursOfWeek();
		int tzHourOffset =  memberResponse.getUser().getTimezoneOffset() / 60;
		
		if ( schedule == null || schedule.size() == 0 ) {
			schedule = new HashSet<Integer>();
			// autobean issues...
//			memberResponse.setSchedule(schedule);
		}
		// 
		List<HourOfDay> hours = new ArrayList<HourOfDay>(24);
		for( int hour = 0; hour < 24; hour++){
			hours.add( new HourOfDay( schedule, hour, tzHourOffset  ) );
		}
		view.showSchedule(hours);
	}

	/**
	 * step 3 load available students
	 * 
	 * LoadAvailableStudents
	 * 
	 * @param member
	 */
	private void loadAvailableStudents( MemberProxy member ) {
		logger.info( "loadAvailableStudents() for CourseId=" + member.getCourse().getId() );
		
		if ( member.getAvailable() ) {
			// 
			MemberRequestContext availStudentsContext = requestFactory.memberRequest();
			Request<List<MemberProxy>> availableStudentsRequest = availStudentsContext.getComplementaryStudents( member.getId(), member.getCourse() );
			availableStudentsRequest.with("course", "user");
			availableStudentsRequest.to( new Receiver<List<MemberProxy>>() {
	
				@Override
				public void onSuccess(List<MemberProxy> response) {
					// 
					logger.info( "loadAvailableStudents() success returned " + response.size() + " Members" );					
					availableStudents = response;
					// remove invites
					for ( PartnerInviteProxy invite : sessionInvites ){
						for ( MemberProxy member2 : response ) {
							if(invite.getMember2().getId().equals(member2.getId())) {
								// invite sent to member 2
								response.remove(member2);
								break;
							}
						}
					}					
					view.showAvailableStudents(response);
				}
	
				@Override
				public void onFailure(ServerFailure error) {
					// 
					logger.severe( error.getMessage() );
					Window.alert( "server error: loadAvailableStudents\n" + error.getMessage() );
				}
			}).fire();
		} else {
			// display loading available students message
			view.showAvailableStudents(null);
		}
		
	}

//	/**
//	 * load the sessions for myCourse and currentUser
//	 * @param course 
//	 */
//	protected void loadSessions(MemberProxy member  ) {
//		logger.info("loadSessions( )");
//		
//		// display loading sessions
//		view.showSessions(null);
//		
//		// 
//		SessionRequestContext sessionRequestContext = requestFactory.sessionRequestContext();
//		Request<List<SessionProxy>> req = sessionRequestContext.getSessionsForMember( member );
//		req.to( new Receiver<List<SessionProxy>>() {
//
//			@Override
//			public void onSuccess(List<SessionProxy> loadedSessions) {
//				// 
//				logger.info( "getSessionsForMember() success " + loadedSessions.size() + " Sessions");
//				sessions = loadedSessions;
//				view.showSessions(loadedSessions);
//			}
//			@Override
//			public void onFailure(ServerFailure error) {
//				// 
//				logger.severe( error.getMessage() );
//				Window.alert( "server error loadSessions:\n" + error.getMessage() );
//			}
//		}).fire();
//	}

	/**
	 * step 4 load session invites
	 * 
	 * load the paired students for myCourse and currentUser
	 * @param course 
	 */
	protected void loadSessionInvites(final MemberProxy member ) {
		logger.info( "loadSessionInvites( )" );
		PartnerInviteRequestContext sessionInviteRequestContext = requestFactory.sessionInviteRequestContext();
		
		// display loading message
		view.showSessionInvites(null);
		
		Request<List<PartnerInviteProxy>> req = sessionInviteRequestContext.getSessionInvitesForMember(member);
		req.with( "member1", "member1.user", "member2", "member2.user" );
		req.to( new Receiver<List<PartnerInviteProxy>>()  {

			@Override
			public void onSuccess(List<PartnerInviteProxy> response) {
				//
				logger.info( "getSessionInvitesForMember() success " + response.size() + " SessionInvites");
				sessionInvites = response;
				view.showSessionInvites(response);
				// these students are available for sessions
				loadAvailableStudents( member );
				
			}
			@Override
			public void onFailure(ServerFailure error) {
				// 
				logger.severe( error.getMessage() );
				Window.alert( "server error:loadSessionInvites\n" + error.getMessage() );
			}
		}).fire();
	}


	/**
	 * @see ca.jhosek.linguaelive.ui.priv.student.StudentYourCourseView.Presenter#getMember()
	 */
	public MemberProxy getMember() {
		// 
		return currentMember;
	}

	public void inviteToPairCourses( final MemberProxy toStudent, String personalMessage ) {
		// 
		logger.info( "inviteToPairCourses( )" );
		PartnerInviteRequestContext sessionInviteRequestContext = requestFactory.sessionInviteRequestContext();
		
		Request<PartnerInviteProxy> req = sessionInviteRequestContext.sendInviteToMember( currentMember, toStudent, personalMessage );
		req.with( "member1", "member1.user", "member2", "member2.user" );
		req.to( new Receiver<PartnerInviteProxy>()  {

			@Override
			public void onSuccess(PartnerInviteProxy response) {
				//
				logger.info( "sendInviteToMember() success SessionInvites id=" + response.getId());
				sessionInvites.add( response );
				view.showSessionInvites(sessionInvites);
				// update list to remove student
				availableStudents.remove(toStudent);
				Window.alert( "Partner invite sent to\n" + toStudent.getUser().getFirstName() + " " + toStudent.getUser().getLastName() );
			}
			@Override
			public void onFailure(ServerFailure error) {
				// 
				logger.severe( error.getMessage() );
				Window.alert( "server error: InviteToPairCourses\n" + error.getMessage() );
			}
		}).fire();

	}

	public void goToSessionInvite(PartnerInviteProxy sessionInvite) {
		// display partner invite
		placeController.goTo( new PartnerInvitePlace(sessionInvite));
	}

	public void goToSession(SessionProxy session) {
		// display session control 
		placeController.goTo( new SessionControlPlace( session ));
	}

	public void addSchedule(Integer hourOfWeek) {
		
		MemberRequestContext addScheduleContext = requestFactory.memberRequest();
		Request<Void> addScheduleRequest = addScheduleContext.addSchedule( currentMember.getId(), hourOfWeek);
		addScheduleRequest.to( new Receiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				// 
				logger.info( "addSchedule() success" );
			}

			@Override
			public void onFailure(ServerFailure error) {
				// 
				logger.severe( error.getMessage() );
				Window.alert( "server error: addSchedule\n" + error.getMessage() );
			}
		}).fire();
		
	}

	public void dropSchedule(Integer hourOfWeek) {
		
		MemberRequestContext dropScheduleContext = requestFactory.memberRequest();
		Request<Void> dropScheduleRequest = dropScheduleContext.dropSchedule( currentMember.getId(), hourOfWeek);
		dropScheduleRequest.to( new Receiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				// 
				logger.info( "dropSchedule() success" );
			}

			@Override
			public void onFailure(ServerFailure error) {
				// 
				logger.severe( error.getMessage() );
				Window.alert( "server error: dropSchedule\n" + error.getMessage() );
			}
		}).fire();
		
		
	}

	public void setAvailable(final Boolean isAvailable ) {
//		MemberRequestContext setAvailableContext = requestFactory.memberRequest();
//		Request<Void> setAvailableRequest = setAvailableContext.setAvailable( currentMember.getId(), isAvailable );
//		setAvailableRequest.to( new Receiver<Void>() {
//
//			@Override
//			public void onSuccess(Void response) {
//				// 
//				logger.info( "setAvailable( " + isAvailable +	" ) success" );
//			}
//
//			@Override
//			public void onFailure(ServerFailure error) {
//				// 
//				logger.severe( error.getMessage() );
//				Window.alert( "server error: setAvailable\n" + error.getMessage() );
//			}
//		}).fire();
		RequestContext context = memberEditorDriver.flush();
		context.fire( new Receiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				// saved available so reload
				loadMember();
			}
		});
		
	}

	/**
	 * refresh the available students query
	 * @see ca.jhosek.linguaelive.ui.priv.student.StudentYourCourseView.Presenter#refreshAvailableStudents()
	 */
	public void refreshAvailableStudents() {
		// 
		loadAvailableStudents(currentMember);
	}

	public void gotoSessionReport() {
		// 
		placeController.goTo( new StudentCourseDetailReportPlace(currentMember));
	}


	@Override
	public void deleteMember() {
		if ( !currentMember.getUser().getId().equals( currentState.getLoggedInUser().getId()) && 
				!currentState.isAdminUser() ) {
			// current user is not the owner nor administrator
			Window.alert("Sorry - cannot delete courses you do not own.");
			return;
		}
		logger.warning( "deleteMember( " + currentMember.getCourse().getName() +  ")");
		// 
		MemberRequestContext memberRequest = requestFactory.memberRequest();
		memberRequest.remove(currentMember).fire( new Receiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				logger.info( "requestToLinkedCourses() success" );
				Window.alert("Delete course successful.");
				// redirect to the new course link display
				placeController.goTo( currentState.getHomePlace() );
			}
			
			/** 
			 * server error handler
			 * @see com.google.web.bindery.requestfactory.shared.Receiver#onFailure(com.google.web.bindery.requestfactory.shared.ServerFailure)
			 */
			
			public void onFailure(ServerFailure error) {
				//
				logger.warning(SERVER_COMMUNICATION_ERROR_WHILE_LINKING_COURSES_PLEASE_TRY_AGAIN);
				Window.alert(SERVER_COMMUNICATION_ERROR_WHILE_LINKING_COURSES_PLEASE_TRY_AGAIN);
				placeController.goTo( currentState.getHomePlace() );
			}						
		});
		
	}

}
