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
import com.google.web.bindery.requestfactory.shared.ServerFailure;

import ca.jhosek.linguaelive.domain.CurrentState;
import ca.jhosek.linguaelive.place.InstructorMemberPlace;
import ca.jhosek.linguaelive.place.InstructorYourCoursePlace;
import ca.jhosek.linguaelive.place.PartnerInvitePlace;
import ca.jhosek.linguaelive.place.SessionControlPlace;
import ca.jhosek.linguaelive.ui.priv.instructor.InstructorMemberView;
import ca.jhosek.linguaelive.ui.priv.instructor.InstructorMemberViewImpl;
import ca.jhosek.linguaelive.ui.priv.instructor.InstructorMemberViewImpl.Driver;
import ca.jhosek.linguaelive.ui.priv.student.HourOfDay;
// import ca.jhosek.linguaelive.domain.ContactInfoDao;
import ca.jhosek.linguaelive.UserType;
import ca.jhosek.linguaelive.proxy.AppRequestFactory;
import ca.jhosek.linguaelive.proxy.ContactInfoProxy;
import ca.jhosek.linguaelive.proxy.ContactInfoRequestContext;
import ca.jhosek.linguaelive.proxy.MemberProxy;
import ca.jhosek.linguaelive.proxy.MemberRequestContext;
import ca.jhosek.linguaelive.proxy.PartnerInviteProxy;
import ca.jhosek.linguaelive.proxy.PartnerInviteRequestContext;
import ca.jhosek.linguaelive.proxy.SessionProxy;
import ca.jhosek.linguaelive.proxy.SessionRequestContext;
import ca.jhosek.linguaelive.proxy.UserProxy;
import ca.jhosek.linguaelive.proxy.UserRequestContext;

/**
 * Instructor's view of a course member and associated objects 
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see InstructorMemberView
 * @see InstructorMemberViewImpl
 * @see InstructorMemberPlace
 *
 * @see MemberRequestContext
 * @see UserRequestContext
 * @see MemberProxy
 * @see UserProxy
 * @see PartnerInviteRequestContext
 * 
 */
public class InstructorMemberActivity extends AbstractActivity implements InstructorMemberView.Presenter {

	private static final Logger logger = Logger.getLogger( InstructorMemberActivity.class.getName() );

	private final InstructorMemberView view;

	private final PlaceController placeController;

	private final AppRequestFactory requestFactory;

	private final CurrentState currentState;

	private final String token;

	private final Driver courseProxyDriver;

	protected MemberProxy viewedMember;

	protected List<MemberProxy> availableStudents = new ArrayList<MemberProxy>();

	protected List<PartnerInviteProxy> sessionInvites = new ArrayList<PartnerInviteProxy>();

	protected List<SessionProxy> sessions = new ArrayList<SessionProxy>();

	protected List<ContactInfoProxy> contactInfos;

	/**
	 * Guice/Gin Factory Module Builder
	 * {@link} http://google-guice.googlecode.com/svn/trunk/javadoc/com/google/inject/assistedinject/FactoryModuleBuilder.html
	 * @see FactoryModuleBuilder
	 * @see GinFactoryModuleBuilder
	 *  
	 */
	public interface Factory {
		InstructorMemberActivity create( String memberIdToken );
	}

	@Inject
	public InstructorMemberActivity(
			EventBus eventBus,
			InstructorMemberView view, 
			PlaceController placeController,
			AppRequestFactory requestFactory,
			CurrentState currentState,
			@Assisted String token
	) {
		super();
		this.view = view;
		this.placeController = placeController;
		this.requestFactory = requestFactory;
		this.currentState = currentState;
		this.token = token;
		view.setPresenter(this);
		this.courseProxyDriver = view.createEditorDriver(eventBus, requestFactory);
	}

	/**
	 * START
	 * @see com.google.gwt.activity.shared.Activity#start(com.google.gwt.user.client.ui.AcceptsOneWidget, com.google.web.bindery.event.shared.EventBus)
	 */
	
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(view);
		try {
			// convert course token to Long id
			final Long memberId = Long.valueOf(token);
			// obtain course context
			MemberRequestContext memberContext = requestFactory.memberRequest();

			// find the member of this course for the logged in user
			Request<MemberProxy> request = memberContext.findMember(memberId).with("course","user","course.owner");
			request.fire( new Receiver< MemberProxy >() {

				
				public void onSuccess(MemberProxy memberResponse) {
					// clear the view
					view.clear();
					
					// working on this course
					viewedMember = memberResponse;
					
					// test for success
					if ( memberResponse == null ) {
						logger.severe("Member not found - member id:" + memberId.toString() );
						//						view.setViewerMode( 
						//								false,	// is user an instrcutor 
						//								false );		// is user owner of course
						Window.alert("Member not found." );
						placeController.goTo( currentState.getHomePlace() );

					} else {
						final UserProxy loggedInUser = currentState.getLoggedInUser();
						if ( !memberResponse.getCourse().getOwner().getId().equals( loggedInUser.getId()) 
								&& loggedInUser.getUserType() != UserType.ADMIN ) {
							logger.warning( "Access Violation - by:" + loggedInUser.getId() + " attempt member id=" + memberResponse.getId() ); 
							Window.alert("Access Violation - You are not the instructor of this member.");
							placeController.goTo( currentState.getHomePlace() );
							
						
						} else {
							logger.info("retrieved member id=" + memberResponse.getId() );
							// if this is your course
							//						view.setViewerMode( 
							//								(user.getUserType() == UserType.INSTRUCTOR),		// is user an instrcutor 
							//								(user.getId()==response.getOwner().getId()));		// is user owner of course

							// show course in editor
							courseProxyDriver.display(memberResponse);

							// load contact info
							loadContactInfo( memberResponse.getUser() );
							// load availability
							loadSchedule( memberResponse );						
							// load the paired students from the database
							loadSessionInvites( memberResponse );
							// these students are available for sessions
							loadAvailableStudents( memberResponse );
							// load the sessions from the database
							loadSessions( memberResponse  );
						}
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
	 * display User's Contact Info 
	 * @param user
	 * 
	 * @see ContactInfoDao
	 */
	private void loadContactInfo( UserProxy user) {
		logger.info("loadContactInfo( " + user.getFirstName() + " " + user.getLastName() + " )");
		// 
		ContactInfoRequestContext contactInfoContext = requestFactory.contactInfoRequest();
		contactInfoContext.findContactInfos(user).fire( new Receiver<List<ContactInfoProxy>>() {

			
			public void onSuccess(List<ContactInfoProxy> response) {
				logger.info("loadContactInfo() fetched  " + response.size() + " ContactInfoProxy" );
				//
				contactInfos = response;
				view.showContactInfo(response);
			}
		});
	}

	/**
	 * LoadAvailableStudents
	 * 
	 * @param member
	 */
	protected void loadAvailableStudents( MemberProxy member ) {
		logger.info( "loadAvailableStudents() for CourseId=" + member.getCourse().getId() );
		
		// display loading available students message
		view.showAvailableStudents(null);
		
		// 
		MemberRequestContext availStudentsContext = requestFactory.memberRequest();
		Request<List<MemberProxy>> availableStudentsRequest = availStudentsContext.getAllComplementaryStudents( member.getCourse() );
		availableStudentsRequest.with("course", "user");
		availableStudentsRequest.to( new Receiver<List<MemberProxy>>() {

			
			public void onSuccess(List<MemberProxy> response) {
				// 
				logger.info( "getComplementaryStudents() success returned " + response.size() + " Members" );
				availableStudents = response;
				view.showAvailableStudents(response);
			}

			
			public void onFailure(ServerFailure error) {
				// 
				logger.severe( error.getMessage() );
				Window.alert( "server error: loadAvailableStudents\n" + error.getMessage() );
			}
		}).fire();

		
	}

	protected void loadSchedule(MemberProxy memberResponse) {
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
			hours.add( new HourOfDay( schedule, hour, tzHourOffset ) );
		}
		view.showSchedule(hours);
	}


	/**
	 * load the sessions for myCourse
	 * @param course 
	 */
	protected void loadSessions(MemberProxy member  ) {
		logger.info("loadSessions( )");
		
		// display loading sessions
		view.showSessions(null);
		
		// 
		SessionRequestContext sessionRequestContext = requestFactory.sessionRequestContext();
		Request<List<SessionProxy>> req = sessionRequestContext.getSessionsForMember( member );
		req.to( new Receiver<List<SessionProxy>>() {

			
			public void onSuccess(List<SessionProxy> loadedSessions) {
				// 
				logger.info( "getSessionsForMember() success " + loadedSessions.size() + " Sessions");
				sessions = loadedSessions;
				view.showSessions(loadedSessions);
			}
			
			public void onFailure(ServerFailure error) {
				// 
				logger.severe( error.getMessage() );
				Window.alert( "server error loadSessions:\n" + error.getMessage() );
			}
		}).fire();
	}

	/**
	 * load the paired students for myCourse and currentUser
	 * @param course 
	 */
	protected void loadSessionInvites(MemberProxy member ) {
		logger.info( "loadSessionInvites( )" );
		PartnerInviteRequestContext sessionInviteRequestContext = requestFactory.sessionInviteRequestContext();
		
		// display loading message
		view.showPartnerInvites(null);
		
		Request<List<PartnerInviteProxy>> req = sessionInviteRequestContext.getSessionInvitesForMember(member);
		req.with( "member1", "member1.user", "member2", "member2.user" );
		req.to( new Receiver<List<PartnerInviteProxy>>()  {

			
			public void onSuccess(List<PartnerInviteProxy> response) {
				//
				logger.info( "getSessionInvitesForMember() success " + response.size() + " SessionInvites");
				sessionInvites = response;
				view.showPartnerInvites(response);
			}
			
			public void onFailure(ServerFailure error) {
				// 
				logger.severe( error.getMessage() );
				Window.alert( "server error:loadSessionInvites\n" + error.getMessage() );
			}
		}).fire();
	}


	/**
	 * @see ca.jhosek.linguaelive.ui.priv.student.InstructorMemberView.Presenter#getMember()
	 */
	
	public MemberProxy getMember() {
		// 
		return viewedMember;
	}

	
	public void inviteToPairCourses( MemberProxy toStudent, String personalMessage ) {
		// 
		logger.info( "inviteToPairCourses( )" );
		PartnerInviteRequestContext sessionInviteRequestContext = requestFactory.sessionInviteRequestContext();
		
		Request<PartnerInviteProxy> req = sessionInviteRequestContext.sendInviteToMember( viewedMember, toStudent, personalMessage);
		req.with( "member1", "member1.user", "member2", "member2.user" );
		req.to( new Receiver<PartnerInviteProxy>()  {

			
			public void onSuccess(PartnerInviteProxy response) {
				//
				logger.info( "sendInviteToMember() success SessionInvites id=" + response.getId());
				sessionInvites.add( response );
				view.showPartnerInvites(sessionInvites);
				Window.alert( "Partner invite sent." );
			}
			
			public void onFailure(ServerFailure error) {
				// 
				logger.severe( error.getMessage() );
				Window.alert( "server error: inviteToPairCourses\n" + error.getMessage() );
			}
		}).fire();

	}

	
	public void goToPartnerInvite(PartnerInviteProxy partnerInvite) {
		// display session invite
		placeController.goTo( new PartnerInvitePlace(partnerInvite));
	}

	
	public void goToSession(SessionProxy session) {
		// display session control 
		placeController.goTo( new SessionControlPlace( session ));
	}

	/**
	 * refresh the available students query
	 * @see ca.jhosek.linguaelive.ui.priv.student.StudentYourCourseView.Presenter#refreshAvailableStudents()
	 */
	
	public void refreshAvailableStudents() {
		// 
		loadAvailableStudents(viewedMember);
	}

	
	public AppRequestFactory getRequestFactory() {
		// 
		return requestFactory;
	}

	
	public void goToCourse() {
		// 
		placeController.goTo( new InstructorYourCoursePlace(viewedMember.getCourse() ));
		
	}
}
