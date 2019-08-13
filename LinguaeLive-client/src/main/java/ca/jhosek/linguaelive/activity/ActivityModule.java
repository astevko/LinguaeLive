package ca.jhosek.linguaelive.activity;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;

import ca.jhosek.linguaelive.activity.mainregion.AddEditContactInfoActivity;
import ca.jhosek.linguaelive.activity.mainregion.AddEditCourseActivity;
import ca.jhosek.linguaelive.activity.mainregion.AdminHomeActivity;
import ca.jhosek.linguaelive.activity.mainregion.ChangePswdActivity;
import ca.jhosek.linguaelive.activity.mainregion.ContactUsActivity;
import ca.jhosek.linguaelive.activity.mainregion.CourseBrowseActivity;
import ca.jhosek.linguaelive.activity.mainregion.FaqActivity;
import ca.jhosek.linguaelive.activity.mainregion.IndexActivity;
import ca.jhosek.linguaelive.activity.mainregion.InstructorCourseDetailReportActivity;
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
import ca.jhosek.linguaelive.activity.menuregion.AdminMenuActivity;
import ca.jhosek.linguaelive.activity.menuregion.AnonMenuActivity;
import ca.jhosek.linguaelive.activity.userregion.HeaderAnonUserActivity;
import ca.jhosek.linguaelive.activity.userregion.HeaderUserActivity;
import ca.jhosek.linguaelive.mvp.MvpModule;
import ca.jhosek.linguaelive.ui.ViewModule;

/**
 * @author copyright (C) 2011 Andrew Stevko
 * 
 *         One of many places where everything seems to be referenced...
 * 
 * @see MvpModule
 * @see ViewModule
 * 
 */
public class ActivityModule extends AbstractGinModule {

	@Override
	protected void configure() {
		// header user region activities
		bind(HeaderAnonUserActivity.class);
		bind(HeaderUserActivity.class);

		// menu region activities
		bind(AnonMenuActivity.class);
		bind(AdminMenuActivity.class);

		// main panel region activities
		bind(AdminHomeActivity.class);
		bind(ContactUsActivity.class);
		bind(CourseBrowseActivity.class);
		bind(FaqActivity.class);
		bind(IndexActivity.class);
		bind(InstructorHomeActivity.class);
		bind(InstructorInfoActivity.class);
		bind(InstructorTipsActivity.class);
		bind(InstructorStartActivity.class);

		bind(LoginFormActivity.class);
		bind(LostAccountActivity.class);
		bind(RecoverPasswordActivity.class);
		// bind( MyProfileActivity.class);
		bind(MyCoursesActivity.class);
		bind(NewInstructorFormActivity.class);
		// bind( NewStudentFormActivity.class);
		bind(PrivacyActivity.class);
		bind(QueryUsersActivity.class);
		bind(QueryCoursesActivity.class);
		bind(StudentHomeActivity.class);
		bind(StudentInfoActivity.class);
		bind(StudentStartActivity.class);
		bind(TermsActivity.class);
		bind(WhatIsActivity.class);
		bind(WhoIsActivity.class);

		bind(Video1Activity.class);
		bind(Video2Activity.class);
		bind(Video3Activity.class);

		// Change Password Activity
		install(new GinFactoryModuleBuilder()
				.build(ChangePswdActivity.Factory.class));

		// bind( AddEditContactInfoActivity.class ); // do not bind and add
		// factory in the same project
		// add factory module builder for parameterized classes
		install(new GinFactoryModuleBuilder()
				.build(MyProfileActivity.Factory.class));

		// add factory module builder for parameterized classes
		install(new GinFactoryModuleBuilder()
				.build(InstructorYourCourseActivity.Factory.class));
		// add factory module builder for parameterized classes
		install(new GinFactoryModuleBuilder()
				.build(StudentYourCourseActivity.Factory.class));
		// add factory module builder for parameterized classes
		install(new GinFactoryModuleBuilder()
				.build(InstructorCourseLinkActivity.Factory.class));
		// add factory module builder for parameterized classes
		install(new GinFactoryModuleBuilder()
				.build(AddEditContactInfoActivity.Factory.class));

		// add factory module builder for parameterized classes
		install(new GinFactoryModuleBuilder()
				.build(InstructorMemberActivity.Factory.class));

		// add factory module builder for parameterized classes
		install(new GinFactoryModuleBuilder()
				.build(NewStudentFormActivity.Factory.class));
		// add factory module builder for parameterized classes
		install(new GinFactoryModuleBuilder()
				.build(AddEditCourseActivity.Factory.class));

		// add factory module builder for parameterized classes
		install(new GinFactoryModuleBuilder()
				.build(PartnerInviteActivity.Factory.class));

		// add factory module builder for parameterized classes
		install(new GinFactoryModuleBuilder()
				.build(SessionControlActivity.Factory.class));

		// add factory module builder for parameterized classes
		install(new GinFactoryModuleBuilder()
				.build(InstructorCourseSummaryReportActivity.Factory.class));

		// add factory module builder for parameterized classes
		install(new GinFactoryModuleBuilder()
				.build(InstructorCourseDetailReportActivity.Factory.class));

		// add factory module builder for parameterized classes
		install(new GinFactoryModuleBuilder()
				.build(StudentCourseDetailReportActivity.Factory.class));

		// bind( InstructorCourseViewActivity.class);
		// bind( InstructorCourseLinkActivity.class);
	}

}
