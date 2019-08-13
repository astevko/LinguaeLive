package ca.jhosek.linguaelive.mvp;

import java.util.logging.Logger;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.Provider;

import ca.jhosek.linguaelive.ActivityAsyncProxy;
import ca.jhosek.linguaelive.activity.mainregion.AddCourseMemberActivity;
import ca.jhosek.linguaelive.activity.mainregion.AddEditContactInfoActivity;
import ca.jhosek.linguaelive.activity.mainregion.AddEditCourseActivity;
import ca.jhosek.linguaelive.activity.mainregion.AdminHomeActivity;
import ca.jhosek.linguaelive.activity.mainregion.ChangePswdActivity;
import ca.jhosek.linguaelive.activity.mainregion.ContactUsActivity;
import ca.jhosek.linguaelive.activity.mainregion.CourseBrowseActivity;
import ca.jhosek.linguaelive.activity.mainregion.FaqActivity;
import ca.jhosek.linguaelive.activity.mainregion.IndexActivity;
import ca.jhosek.linguaelive.activity.mainregion.InstructorCourseDetailReportActivity;
import ca.jhosek.linguaelive.activity.mainregion.InstructorCourseDetailReportActivity.Factory;
import ca.jhosek.linguaelive.activity.mainregion.InstructorCourseLinkActivity;
import ca.jhosek.linguaelive.activity.mainregion.InstructorCourseSummaryReportActivity;
import ca.jhosek.linguaelive.activity.mainregion.InstructorHomeActivity;
import ca.jhosek.linguaelive.activity.mainregion.InstructorInfoActivity;
import ca.jhosek.linguaelive.activity.mainregion.InstructorMemberActivity;
import ca.jhosek.linguaelive.activity.mainregion.InstructorStartActivity;
import ca.jhosek.linguaelive.activity.mainregion.InstructorTipsActivity;
import ca.jhosek.linguaelive.activity.mainregion.InstructorYourCourseActivity;
import ca.jhosek.linguaelive.activity.mainregion.LoginFormActivity;
import ca.jhosek.linguaelive.activity.mainregion.LostAccountActivity;
import ca.jhosek.linguaelive.activity.mainregion.MyCoursesActivity;
import ca.jhosek.linguaelive.activity.mainregion.MyProfileActivity;
import ca.jhosek.linguaelive.activity.mainregion.NewInstructorFormActivity;
import ca.jhosek.linguaelive.activity.mainregion.NewStudentFormActivity;
import ca.jhosek.linguaelive.activity.mainregion.PartnerInviteActivity;
import ca.jhosek.linguaelive.activity.mainregion.PrivacyActivity;
import ca.jhosek.linguaelive.activity.mainregion.QueryCoursesActivity;
import ca.jhosek.linguaelive.activity.mainregion.QueryUsersActivity;
import ca.jhosek.linguaelive.activity.mainregion.RecoverPasswordActivity;
import ca.jhosek.linguaelive.activity.mainregion.SessionControlActivity;
import ca.jhosek.linguaelive.activity.mainregion.StudentCourseDetailReportActivity;
import ca.jhosek.linguaelive.activity.mainregion.StudentHomeActivity;
import ca.jhosek.linguaelive.activity.mainregion.StudentInfoActivity;
import ca.jhosek.linguaelive.activity.mainregion.StudentStartActivity;
import ca.jhosek.linguaelive.activity.mainregion.StudentYourCourseActivity;
import ca.jhosek.linguaelive.activity.mainregion.TermsActivity;
import ca.jhosek.linguaelive.activity.mainregion.Video1Activity;
import ca.jhosek.linguaelive.activity.mainregion.Video2Activity;
import ca.jhosek.linguaelive.activity.mainregion.Video3Activity;
import ca.jhosek.linguaelive.activity.mainregion.WhatIsActivity;
import ca.jhosek.linguaelive.activity.mainregion.WhoIsActivity;
import ca.jhosek.linguaelive.domain.CurrentState;
import ca.jhosek.linguaelive.place.AddCourseMemberPlace;
import ca.jhosek.linguaelive.place.AddEditContactInfoPlace;
import ca.jhosek.linguaelive.place.AddEditCoursePlace;
import ca.jhosek.linguaelive.place.AdminHomePlace;
import ca.jhosek.linguaelive.place.AppPlaceHistoryMapper;
import ca.jhosek.linguaelive.place.AuthenticatedPlace;
import ca.jhosek.linguaelive.place.ChangePswdPlace;
import ca.jhosek.linguaelive.place.ContactUsPlace;
import ca.jhosek.linguaelive.place.CourseBrowsePlace;
import ca.jhosek.linguaelive.place.FaqPlace;
import ca.jhosek.linguaelive.place.IndexPlace;
import ca.jhosek.linguaelive.place.InstructorCourseDetailReportPlace;
import ca.jhosek.linguaelive.place.InstructorCourseLinkPlace;
import ca.jhosek.linguaelive.place.InstructorCourseSummaryReportPlace;
import ca.jhosek.linguaelive.place.InstructorHomePlace;
import ca.jhosek.linguaelive.place.InstructorInfoPlace;
import ca.jhosek.linguaelive.place.InstructorMemberPlace;
import ca.jhosek.linguaelive.place.InstructorStartPlace;
import ca.jhosek.linguaelive.place.InstructorTipsPlace;
import ca.jhosek.linguaelive.place.InstructorYourCoursePlace;
import ca.jhosek.linguaelive.place.LoginFormPlace;
import ca.jhosek.linguaelive.place.LostAccountPlace;
import ca.jhosek.linguaelive.place.MyCoursesPlace;
import ca.jhosek.linguaelive.place.MyProfilePlace;
import ca.jhosek.linguaelive.place.NewInstructorPlace;
import ca.jhosek.linguaelive.place.NewStudentPlace;
import ca.jhosek.linguaelive.place.PartnerInvitePlace;
import ca.jhosek.linguaelive.place.PrivacyPlace;
import ca.jhosek.linguaelive.place.QueryCoursesPlace;
import ca.jhosek.linguaelive.place.QueryUsersPlace;
import ca.jhosek.linguaelive.place.RecoverPasswordPlace;
import ca.jhosek.linguaelive.place.SessionControlPlace;
import ca.jhosek.linguaelive.place.StudentCourseDetailReportPlace;
import ca.jhosek.linguaelive.place.StudentHomePlace;
import ca.jhosek.linguaelive.place.StudentInfoPlace;
import ca.jhosek.linguaelive.place.StudentStartPlace;
import ca.jhosek.linguaelive.place.StudentYourCoursePlace;
import ca.jhosek.linguaelive.place.TermsPlace;
import ca.jhosek.linguaelive.place.Video1Place;
import ca.jhosek.linguaelive.place.Video2Place;
import ca.jhosek.linguaelive.place.Video3Place;
import ca.jhosek.linguaelive.place.WhatIsPlace;
import ca.jhosek.linguaelive.place.WhoIsPlace;
import ca.jhosek.linguaelive.ui.ViewModule;

/**
 * this is the user region in the header of the main view
 * you can have either an anon user or an authenticated user
 * 
 * One of three places to insert new panels
 * 
 * @see MainRegionActivityMapper
 * @see MenuRegionActivityMapper
 * @see ViewModule
 * @see AppPlaceHistoryMapper
 * 
 * 
 * @author copyright (C) 2011, 2012 Andrew Stevko
 *
 */
public class MainRegionActivityMapper implements ActivityMapper {
	private static final Logger logger = Logger.getLogger( MainRegionActivityMapper.class.getName() );

	
	private final CurrentState currentState;
	
	// maintain same alphabetical order as files in ca.jhosek.linguaelive.activity.mainregion
	private final Provider<ActivityAsyncProxy<AdminHomeActivity>>           adminHomeActivityProvider;
	private final Provider<ActivityAsyncProxy<ContactUsActivity>>           contactUsActivityProvider;
	private final Provider<ActivityAsyncProxy<IndexActivity>>				indexActivityProvider;
	private final InstructorYourCourseActivity.Factory 	instructorCourseViewActivityFactory;
	private final StudentYourCourseActivity.Factory 	studentCourseViewActivityFactory;
	private final InstructorCourseLinkActivity.Factory 	instructorCourseLinkActivityFactory;
	private final Provider<ActivityAsyncProxy<InstructorHomeActivity>> 		instructorHomeActivityProvider;
	private final Provider<ActivityAsyncProxy<InstructorStartActivity>> 	instructorStartActivityProvider;
	private final Provider<LoginFormActivity> 			loginFormActivityProvider;
	private final Provider<ActivityAsyncProxy<LostAccountActivity>> 		lostAccountActivityProvider;
	private final MyProfileActivity.Factory				myProfileActivityFactory;
	private final AddEditContactInfoActivity.Factory	addEditContactInfoActivityFactory;
	private final Provider<ActivityAsyncProxy<MyCoursesActivity>>			myClassesActivityProvider;
	private final Provider<ActivityAsyncProxy<NewInstructorFormActivity>>	newInstructorFormActivityProvider;
	private final NewStudentFormActivity.Factory		newStudentFormActivityFactory;	
	private final Provider<ActivityAsyncProxy<PrivacyActivity>> 			privacyActivityProvider;
	private final Provider<ActivityAsyncProxy<StudentHomeActivity>> 		studentHomeActivityProvider;
	private final Provider<ActivityAsyncProxy<StudentStartActivity>> 		studentStartActivityProvider;
	private final Provider<ActivityAsyncProxy<TermsActivity>> 				termsActivityProvider;
	private final Provider<ActivityAsyncProxy<FaqActivity>> 				faqActivityProvider;
	private final Provider<ActivityAsyncProxy<InstructorInfoActivity>> 		instructorInfoActivityProvider;
	private final Provider<ActivityAsyncProxy<StudentInfoActivity>> 		studentInfoActivityProvider;
	private final Provider<ActivityAsyncProxy<WhatIsActivity>> 				whatIsActivityProvider;
	private final Provider<ActivityAsyncProxy<WhoIsActivity>> 				whoIsActivityProvider;
	private final Provider<ActivityAsyncProxy<QueryUsersActivity>> 			queryUsersActivityProvider;
	private final Provider<ActivityAsyncProxy<Video1Activity>> 				video1ActivityProvider;
	private final Provider<ActivityAsyncProxy<Video2Activity>> 				video2ActivityProvider;
	private final Provider<ActivityAsyncProxy<Video3Activity>> 				video3ActivityProvider;
	private final ChangePswdActivity.Factory          changePasswordActivityFactory;


	private final Provider<ActivityAsyncProxy<QueryCoursesActivity>> queryCoursesActivityProvider;


	private final AddEditCourseActivity.Factory addACourseActivityFactory;


	private final Provider<ActivityAsyncProxy<InstructorTipsActivity>> instructorTipsActivityProvider;


	private final Provider<ActivityAsyncProxy<CourseBrowseActivity>> courseBrowseActivityProvider;


	private final Provider<ActivityAsyncProxy<AddCourseMemberActivity>> addCourseMemberActivityProvider;


	private final PartnerInviteActivity.Factory sessionInviteActivityFactory;


	private final SessionControlActivity.Factory sessionControlActivityFactory;


	private final InstructorMemberActivity.Factory instructorMemberActivityFactory;


	private final InstructorCourseSummaryReportActivity.Factory instructorCourseSummaryReportActivityFactory;


	private final Factory instructorCourseDetailReportActivityFactory;


	private final ca.jhosek.linguaelive.activity.mainregion.StudentCourseDetailReportActivity.Factory studentCourseDetailReportActivityFactory;

	private final Provider<ActivityAsyncProxy<RecoverPasswordActivity>>           recoverPasswordActivityProvider;
	
	@Inject
	public MainRegionActivityMapper(
			PlaceController							placeController,
			CurrentState							currentState,
			Provider<ActivityAsyncProxy<AddCourseMemberActivity>>             addCourseMemberActivityProvider,
			AddEditCourseActivity.Factory 			addACourseActivityFactory,
			Provider<ActivityAsyncProxy<AdminHomeActivity>>             adminHomeActivityProvider,
			Provider<ActivityAsyncProxy<ContactUsActivity>>             contactUsActivityProvider,
			Provider<ActivityAsyncProxy<CourseBrowseActivity>>          courseBrowseActivityProvider,
			Provider<ActivityAsyncProxy<FaqActivity>>					faqActivityProvider,
			Provider<ActivityAsyncProxy<IndexActivity>>                 indexActivityProvider,
			InstructorYourCourseActivity.Factory 	instructorCourseViewActivityFactory,
			InstructorCourseSummaryReportActivity.Factory 	instructorCourseSummaryReportActivityFactory,
			InstructorCourseDetailReportActivity.Factory 	instructorCourseDetailReportActivityFactory,
			StudentCourseDetailReportActivity.Factory 	studentCourseDetailReportActivityFactory,
			StudentYourCourseActivity.Factory 		studentCourseViewActivityFactory,
			InstructorCourseLinkActivity.Factory 	instructorCourseLinkActivityFactory,
			Provider<ActivityAsyncProxy<InstructorHomeActivity>>		instructorHomeActivityProvider,
			Provider<ActivityAsyncProxy<InstructorInfoActivity>>		instructorInfoActivityProvider,
			Provider<ActivityAsyncProxy<InstructorStartActivity>>		instructorStartActivityProvider,
			Provider<ActivityAsyncProxy<InstructorTipsActivity>>		instructorTipsActivityProvider,
			Provider<LoginFormActivity> 			loginFormActivityProvider,
			Provider<ActivityAsyncProxy<LostAccountActivity>> 			lostAccountActivityProvider,
			MyProfileActivity.Factory				myProfileActivityProvider,
			AddEditContactInfoActivity.Factory		addEditContactInfoActivityFactory,
			Provider<ActivityAsyncProxy<MyCoursesActivity>>				myClassesActivityProvider,			
			Provider<ActivityAsyncProxy<NewInstructorFormActivity>>		newInstructorFormActivityProvider,
			NewStudentFormActivity.Factory			newStudentFormActivityFactory,
			Provider<ActivityAsyncProxy<PrivacyActivity>>				privacyActivityProvider,
			Provider<ActivityAsyncProxy<QueryUsersActivity>>			queryUsersActivityProvider,
			Provider<ActivityAsyncProxy<QueryCoursesActivity>>			queryCoursesActivityProvider,
			Provider<ActivityAsyncProxy<StudentHomeActivity>>			studentHomeActivityProvider,
			Provider<ActivityAsyncProxy<StudentInfoActivity>>			studentInfoActivityProvider,
			Provider<ActivityAsyncProxy<StudentStartActivity>>			studentStartActivityProvider,
			Provider<ActivityAsyncProxy<TermsActivity>>					termsActivityProvider,
			Provider<ActivityAsyncProxy<WhatIsActivity>>				whatIsActivityProvider,
			Provider<ActivityAsyncProxy<WhoIsActivity>>					whoIsActivityProvider,
			PartnerInviteActivity.Factory			sessionInviteActivityFactory,
			SessionControlActivity.Factory			sessionControlActivityFactory,
			InstructorMemberActivity.Factory 		instructorMemberActivityFactory,
			Provider<ActivityAsyncProxy<Video1Activity>>					video1ActivityProvider,
			Provider<ActivityAsyncProxy<Video2Activity>>					video2ActivityProvider,
			Provider<ActivityAsyncProxy<Video3Activity>>					video3ActivityProvider,
			 ChangePswdActivity.Factory          changePasswordActivityFactory,
			 Provider<ActivityAsyncProxy<RecoverPasswordActivity>> recoverPasswordActivityProvider
			
	) {
		this.currentState = currentState;
		this.addCourseMemberActivityProvider = addCourseMemberActivityProvider;
		this.addACourseActivityFactory = addACourseActivityFactory;
		this.adminHomeActivityProvider = adminHomeActivityProvider;
		this.contactUsActivityProvider = contactUsActivityProvider;
		this.courseBrowseActivityProvider = courseBrowseActivityProvider;
		this.faqActivityProvider = faqActivityProvider;
		this.indexActivityProvider = indexActivityProvider;
		this.instructorCourseViewActivityFactory = instructorCourseViewActivityFactory;
		this.instructorCourseSummaryReportActivityFactory = instructorCourseSummaryReportActivityFactory;
		this.instructorCourseDetailReportActivityFactory = instructorCourseDetailReportActivityFactory;
		this.studentCourseDetailReportActivityFactory = studentCourseDetailReportActivityFactory;
		this.studentCourseViewActivityFactory = studentCourseViewActivityFactory;
		this.instructorCourseLinkActivityFactory = instructorCourseLinkActivityFactory;
		this.instructorHomeActivityProvider = instructorHomeActivityProvider;
		this.instructorInfoActivityProvider = instructorInfoActivityProvider;
		this.instructorStartActivityProvider = instructorStartActivityProvider;
		this.instructorTipsActivityProvider = instructorTipsActivityProvider;
		this.loginFormActivityProvider = loginFormActivityProvider;
		this.lostAccountActivityProvider = lostAccountActivityProvider;
		this.myProfileActivityFactory = myProfileActivityProvider;
		this.addEditContactInfoActivityFactory = addEditContactInfoActivityFactory;
		this.myClassesActivityProvider = myClassesActivityProvider;
		this.newInstructorFormActivityProvider = newInstructorFormActivityProvider;
		this.newStudentFormActivityFactory = newStudentFormActivityFactory;
		this.privacyActivityProvider = privacyActivityProvider;
		this.queryUsersActivityProvider = queryUsersActivityProvider;
		this.queryCoursesActivityProvider = queryCoursesActivityProvider;
		this.studentHomeActivityProvider = studentHomeActivityProvider;
		this.studentInfoActivityProvider = studentInfoActivityProvider;
		this.studentStartActivityProvider = studentStartActivityProvider;
		this.termsActivityProvider = termsActivityProvider;
		this.whatIsActivityProvider = whatIsActivityProvider;
		this.whoIsActivityProvider = whoIsActivityProvider;
		this.sessionInviteActivityFactory = sessionInviteActivityFactory;
		this.sessionControlActivityFactory = sessionControlActivityFactory;
		this.instructorMemberActivityFactory = instructorMemberActivityFactory;
		this.video1ActivityProvider = video1ActivityProvider;
		this.video2ActivityProvider = video2ActivityProvider;
		this.video3ActivityProvider = video3ActivityProvider;
		this.changePasswordActivityFactory = changePasswordActivityFactory;
		this.recoverPasswordActivityProvider = recoverPasswordActivityProvider;
	}

	public Activity getActivity(Place place) {
		//---------------------
		// check if the user is authenticated and the place requires it
		if ( place instanceof AuthenticatedPlace && !currentState.isLoggedIn() ) {
			
			logger.warning("unauthenticated place violation, displaying login");
			LoginFormActivity loginForm = loginFormActivityProvider.get();
			
			// add place holder to capture current login parameter to forward to...			
			currentState.setForwardLoginTo(place);
			
			return loginForm;
		}

		if( place.equals( Place.NOWHERE ) ) {
			// should redirect auto-login user to proper home place
			logger.info( "place = NOWHERE" );
			place = currentState.getHomePlace();
		}
		// set the window title so that bookmarks have clear names
		String name = place.getClass().getName();
		name = name.substring( name.lastIndexOf(".")+1, name.length()-5);
		Window.setTitle( "LinguaeLive.ca - " + name );
		
		//---------------------
		// main Place/Activity decision map
		if (place instanceof AddEditCoursePlace) {
			return addACourseActivityFactory.create( ((AddEditCoursePlace) place).getCourseId() );
			
		} else 	if ( place instanceof AdminHomePlace ) {			
			return adminHomeActivityProvider.get();
			
		} else if ( place instanceof ContactUsPlace ) {
			return contactUsActivityProvider.get();
			
		} else if ( place instanceof CourseBrowsePlace ) {
			return courseBrowseActivityProvider.get();
			
		} else if ( place instanceof FaqPlace ) {
			return faqActivityProvider.get();
			
		} else if ( place instanceof IndexPlace	)  {
			return indexActivityProvider.get(); 
			
		} else if ( place instanceof InstructorYourCoursePlace ) {
			// use a factory to create the activity with the proper course id
			return instructorCourseViewActivityFactory.create(((InstructorYourCoursePlace) place).getCourseId());
			
		} else if ( place instanceof StudentYourCoursePlace ) {
			// use a factory to create the activity with the proper course id
			return studentCourseViewActivityFactory.create(((StudentYourCoursePlace) place).getCourseId());
			
		} else if ( place instanceof InstructorCourseLinkPlace ) {
			// use a factory to create the activity with the proper course link id
			InstructorCourseLinkPlace clPlace = (InstructorCourseLinkPlace) place;
			logger.info("mainRegionActivityMapper calling LinkActivity.create( " + clPlace.getCourseLinkId() + ", " + clPlace.getCourseId() + " )" );
			return instructorCourseLinkActivityFactory.create(clPlace.getCourseLinkId(), Long.parseLong(clPlace.getCourseId() ) );
			
		} else if ( place instanceof InstructorHomePlace ) {
			return instructorHomeActivityProvider.get();
			
		} else if ( place instanceof InstructorTipsPlace ) {
			logger.info("IntructorTipsPlace detected" );
			return instructorTipsActivityProvider.get();
			
		} else if ( place instanceof InstructorInfoPlace ) {
			return instructorInfoActivityProvider.get();
			
		} else if ( place instanceof InstructorStartPlace ) {
			return instructorStartActivityProvider.get();
			
		} else if ( place instanceof LoginFormPlace ) {
			return loginFormActivityProvider.get();
			
		} else if ( place instanceof LostAccountPlace ) {
			return lostAccountActivityProvider.get();
			
		} else if ( place instanceof MyProfilePlace ) {
			return myProfileActivityFactory.create(((MyProfilePlace) place).getUserId() );
			
		} else if ( place instanceof AddEditContactInfoPlace ) {
			return addEditContactInfoActivityFactory.create(((AddEditContactInfoPlace) place).getContactInfoId() );

		} else if ( place instanceof MyCoursesPlace ) {
			return myClassesActivityProvider.get();

		} else if ( place instanceof NewInstructorPlace ) {
			return newInstructorFormActivityProvider.get();
			
		} else if ( place instanceof NewStudentPlace ) {
			return newStudentFormActivityFactory.create(((NewStudentPlace) place).getCourseInviteCode());
			
		} else if ( place instanceof PrivacyPlace ) {
			return privacyActivityProvider.get();
			
		} else if ( place instanceof QueryUsersPlace ) {
			return queryUsersActivityProvider.get();
			
		} else if ( place instanceof QueryCoursesPlace ) {
			return queryCoursesActivityProvider.get();
			
		} else if ( place instanceof StudentHomePlace ) {
			return studentHomeActivityProvider.get();
			
		} else if ( place instanceof StudentInfoPlace ) {
			return studentInfoActivityProvider.get();
			
		} else if ( place instanceof AddCourseMemberPlace ) {
			return addCourseMemberActivityProvider.get();
			
		} else if ( place instanceof StudentStartPlace ) {
			return studentStartActivityProvider.get();
			
		} else if ( place instanceof TermsPlace ) {
			return termsActivityProvider.get();
			
		} else if ( place instanceof WhatIsPlace ) {
			return whatIsActivityProvider.get();
			
		} else if ( place instanceof WhoIsPlace ) {
			return whoIsActivityProvider.get();
			
		} else if ( place instanceof PartnerInvitePlace ){
			return sessionInviteActivityFactory.create( ((PartnerInvitePlace) place).getSessionInviteId() );
			
		} else if ( place instanceof SessionControlPlace ){
			return sessionControlActivityFactory.create( ((SessionControlPlace) place).getSessionId() );
			
		} else if ( place instanceof InstructorMemberPlace ) {
			return instructorMemberActivityFactory.create( ((InstructorMemberPlace) place).getMemberId() );
			
		} else if ( place instanceof InstructorCourseSummaryReportPlace ) {
			return instructorCourseSummaryReportActivityFactory.create(((InstructorCourseSummaryReportPlace) place).getCourseId());

		} else if ( place instanceof InstructorCourseDetailReportPlace ) {
			return instructorCourseDetailReportActivityFactory.create(((InstructorCourseDetailReportPlace) place).getCourseId());
			
		} else if ( place instanceof StudentCourseDetailReportPlace ) {
			return studentCourseDetailReportActivityFactory.create(((StudentCourseDetailReportPlace) place).getMemberId());
			
		} else if ( place instanceof Video1Place ) {
			return video1ActivityProvider.get();
			
		} else if ( place instanceof Video2Place ) {
			return video2ActivityProvider.get();
			
		} else if ( place instanceof Video3Place ) {
			return video3ActivityProvider.get();
			
		} else if ( place instanceof ChangePswdPlace) {
			return  changePasswordActivityFactory.create(((ChangePswdPlace) place).getUser());
			
		} else if ( place instanceof RecoverPasswordPlace ) {
			return recoverPasswordActivityProvider.get(); 
		}
		logger.warning("unknown MainRegion place " + place );
		return null;		
	}
}
