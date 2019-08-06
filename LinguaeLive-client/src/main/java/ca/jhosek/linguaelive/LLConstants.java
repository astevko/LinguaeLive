/**
 * 
 */
package ca.jhosek.main.client;

import com.google.gwt.i18n.client.Constants;

import ca.jhosek.main.client.activity.mainregion.InstructorYourCourseActivity;
import ca.jhosek.main.client.activity.mainregion.SessionControlActivity;
import ca.jhosek.main.client.ui.anon.ContactUsViewImpl;
import ca.jhosek.main.client.ui.anon.CourseBrowseViewImpl;
import ca.jhosek.main.client.ui.anon.LostAccountViewImpl;
import ca.jhosek.main.client.ui.priv.MyCoursesViewImpl;
import ca.jhosek.main.client.ui.priv.MyProfileViewImpl;
import ca.jhosek.main.client.ui.priv.admin.AdminUserViewerImpl;
import ca.jhosek.main.client.ui.priv.admin.QueryCoursesViewImpl;
import ca.jhosek.main.client.ui.priv.admin.QueryUsersViewImpl;
import ca.jhosek.main.client.ui.priv.instructor.InstructorCourseLinkViewImpl;
import ca.jhosek.main.client.ui.priv.instructor.InstructorHomeViewImpl;
import ca.jhosek.main.client.ui.priv.instructor.InstructorMemberViewImpl;
import ca.jhosek.main.client.ui.priv.instructor.InstructorYourCourseViewImpl;
import ca.jhosek.main.client.ui.priv.student.PartnerInviteDialog;
import ca.jhosek.main.client.ui.priv.student.PartnerInviteViewImpl;
import ca.jhosek.main.client.ui.priv.student.SessionControlViewImpl;
import ca.jhosek.main.client.ui.priv.student.StudentCourseDetailReportViewImpl;
import ca.jhosek.main.client.ui.priv.student.StudentHomeViewImpl;
import ca.jhosek.main.client.ui.priv.student.StudentYourCourseViewImpl;

/**
 * @author andy
 * @see Constants
 */
public interface LLConstants extends Constants {

	String allFieldsAreRequired();

	String bothPasswordsMustMatch();

	/**
	 * @see ContactUsViewImpl
	 */
	String contactUsReason();

	/**
	 * @see ContactUsViewImpl
	 */
	String contactUsReason1();

	/**
	 * @see ContactUsViewImpl
	 */
	String contactUsReason2();

	/**
	 * @see ContactUsViewImpl
	 */
	String contactUsReason3();

	/**
	 * @see ContactUsViewImpl
	 */
	String contactUsReason4();

	/**
	 * @see ContactUsViewImpl
	 */
	String contactUsReason5();

	/**
	 * @see ContactUsViewImpl
	 */
	String contactUsReason6();

	/**
	 * @see PartnerInviteDialog
	 */
	String dateTimeDateFormatterPattern();

	/**
	 * @see QueryUsersViewImpl
	 */
	String emailAddress();

	/**
	 * @see MyCoursesViewImpl
	 */
	String endDate();

	/**
	 * @see QueryUsersViewImpl
	 */
	String estMembership();

	/**
	 * @return
	 * @see CourseBrowseViewImpl
	 */
	String expertLanguage();

	/**
	 * @see StudentHomeViewImpl
	 */
	String from();

	/**
	 * @see InstructorCourseLinkViewImpl
	 */
	String hiddenUntilConfirmed();

	/**
	 * @see InstructorMemberViewImpl
	 * @see StudentYourCourseViewImpl
	 */
	String hour();

	/**
	 * @see QueryUsersViewImpl
	 */
	String instructor();

	/**
	 * @return "<first_name> <last_name>..."
	 */
	String instructorCourseLinkEmailDefaultBody();

	/**
	 * @return "Hello Instructor,"
	 */
	String instructorCourseLinkEmailDefaultGreeting();

	/**
	 * @see StudentHomeViewImpl
	 */
	String invite();

	/**
	 * @see StudentHomeViewImpl
	 */
	String inviteAccepted();

	/**
	 * @see QueryUsersViewImpl
	 */
	String inviteCode();

	/**
	 * @see StudentHomeViewImpl
	 */
	String inviteFor();

	/**
	 * @see StudentHomeViewImpl
	 */
	String invitePending();

	/**
	 * @see StudentHomeViewImpl
	 */
	String inviteRejected();

	/**
	 * @see StudentYourCourseViewImpl
	 */
	String loadingAvailabilitySchedule();

	/**
	 * @see MyProfileViewImpl
	 */
	String loadingContactInfo();

	String loadingLinkedCourses();

	/**
	 * @see InstructorHomeViewImpl
	 */
	String loadingOpenLinkedCourses();

	/**
	 * @see StudentHomeViewImpl
	 */
	String loadingPartnerInvites();

	/**
	 * @see InstructorHomeViewImpl
	 */
	String loadingPendingLinkedCourses();

	/**
	 * @see StudentHomeViewImpl
	 */
	String loadingSessions();

	/**
	 * @return InstructorYourCourseViewImpl
	 */
	String loadingStudents();

	/**
	 * @see InstructorYourCourseActivity
	 */
	String loadingUnlinkedCourses();

	/**
	 * @return
	 * @see CourseBrowseViewImpl
	 */
	String location();

	/**
	 * @return "dd MMM yyyy"
	 * @see CourseBrowseViewImpl
	 */
	String mdyDateFormatterPattern();

	/**
	 * @return dd MMMM yyyy HH:mm zzz
	 */
	String mdyhmzDateFormatterPattern();

	/**
	 * @see StudentHomeViewImpl
	 */
	String minutes();

	/**
	 * @return "Name"
	 * @see CourseBrowseViewImpl
	 * @see MyCoursesViewImpl
	 * @see QueryCoursesViewImpl
	 */
	String name();

	/**
	 * @see AdminUserViewerImpl
	 */
	String noContactInfoFound();

	/**
	 * @see MyCoursesViewImpl
	 */
	String noCoursesFound();

	/**
	 * @see InstructorYourCourseViewImpl
	 */
	String noCurrentComplementaryCoursesFound();

	String noLinkedCoursesFound();

	/**
	 * @see SessionControlViewImpl
	 */
	String noNotesHaveBeenEnteredForThisSession();

	/**
	 * @see InstructorHomeViewImpl
	 */
	String noOpenLinkedCoursesFound();

	/**
	 * @see StudentYourCourseViewImpl
	 */
	String noOtherStudentsHaveComplementaryAvailability();

	String noPastComplementaryCoursesFound();

	/**
	 * @see InstructorYourCourseViewImpl
	 */
	String noPastCoursesFound();

	/**
	 * @see InstructorHomeViewImpl
	 */
	String noPendingLinkedCoursesFound();

	/**
	 * @see StudentHomeViewImpl
	 */
	String noPendingSessionInvitesFound();

	/**
	 * @see InstructorMemberViewImpl
	 */
	String noScheduleAvailabilityInformationRecorded();

	/**
	 * @see StudentYourCourseViewImpl
	 */
	String noSessionInvitesFoundForThisCourse();

	/**
	 * @see StudentCourseDetailReportViewImpl
	 */
	String noSessionsFound();

	String noStudentsEnrolledWithinThisCourse();

	/**
	 * @see StudentHomeViewImpl
	 */
	String noUnfinishedSessionsFound();

	/**
	 * @see QueryUsersViewImpl
	 */
	String noUsersFound();

	String pleaseAcceptTheTermsOfService();

	/**
	 * @see StudentYourCourseViewImpl
	 */
	String pleaseChangeYourAvailability();

	/**
	 * @see LostAccountViewImpl
	 * @return
	 */
	String pleaseEnterAValidEmailAddress();

	/**
	 * @return
	 * @see CourseBrowseViewImpl
	 * @see QueryCoursesViewImpl
	 */
	String school();

	/**
	 * @see QueryUsersViewImpl
	 */
	String selectAUser();

	/**
	 * @see SessionControlViewImpl
	 * @see PartnerInviteViewImpl
	 */
	String session();

	/**
	 * @see SessionControlViewImpl
	 */
	String sessionCancelled();

	/**
	 * @see SessionControlViewImpl
	 */
	String sessionCompleted();

	/**
	 * @see SessionControlViewImpl
	 * @see StudentHomeViewImpl
	 */
	String sessionInProgress();

	/**
	 * @see SessionControlViewImpl
	 * @see StudentHomeViewImpl
	 * 
	 */
	String sessionNotStarted();

	/**
	 * @see StudentCourseDetailReportViewImpl
	 */
	String sessionNotYetStarted();

	/**
	 * @see InstructorYourCourseViewImpl
	 */
	String showConcurrentCourses();

	/**
	 * @see InstructorYourCourseViewImpl
	 */
	String showPastCourses();

	/**
	 * @return
	 * @see CourseBrowseViewImpl
	 * @see MyCoursesViewImpl
	 */
	String startDate();

	/**
	 * @see PartnerInviteViewImpl
	 */
	String startTiming();

	/**
	 * @see SessionControlViewImpl
	 */
	String stopTiming();

	/**
	 * @return "<first_name> <last_name>..."
	 */
	String studentPartnerInviteDefaultBody();

	/**
	 * @return "Hello Student,"
	 */
	String studentPartnerInviteDefaultGreeting();

	/**
	 * @see SessionControlViewImpl
	 */
	String switchToTiming();

	/**
	 * @return
	 * @see CourseBrowseViewImpl
	 */
	String targetLanguage();

	/**
	 * @see StudentHomeViewImpl
	 */
	String to();

	/**
	 * @see InstructorYourCourseViewImpl
	 */
	String unlinkedCoursesBlurb();

	/**
	 * @see MyCoursesViewImpl
	 */
	String viewEditButton();

	/**
	 * @see PartnerInviteViewImpl
	 * @return "Partner Session Start"
	 */
	String partnerSessionStart();

	/**
	 * @see PartnerInviteViewImpl
	 * @return "Partner Invite"
	 */
	String partnerInvite();

	/**
	 * @see SessionControlActivity
	 * @return do you want to stop the session in progress
	 */
	String stopSessionInProgressQuestion();
	
	/**
	 * add a new course
	 * 32EAE5BD33D7F50720D9E69C33646C58
	 * @return
	 */
	String addANewCourse();
}
