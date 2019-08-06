package ca.jhosek.main.client.activity;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;

import ca.jhosek.main.client.activity.mainregion.AddEditContactInfoActivity;
import ca.jhosek.main.client.activity.mainregion.AddEditCourseActivity;
import ca.jhosek.main.client.activity.mainregion.AdminHomeActivity;
import ca.jhosek.main.client.activity.mainregion.ChangePswdActivity;
import ca.jhosek.main.client.activity.mainregion.ContactUsActivity;
import ca.jhosek.main.client.activity.mainregion.CourseBrowseActivity;
import ca.jhosek.main.client.activity.mainregion.FaqActivity;
import ca.jhosek.main.client.activity.mainregion.IndexActivity;
import ca.jhosek.main.client.activity.mainregion.InstructorCourseDetailReportActivity;
import ca.jhosek.main.client.activity.mainregion.InstructorCourseLinkActivity;
import ca.jhosek.main.client.activity.mainregion.InstructorCourseSummaryReportActivity;
import ca.jhosek.main.client.activity.mainregion.InstructorHomeActivity;
import ca.jhosek.main.client.activity.mainregion.InstructorInfoActivity;
import ca.jhosek.main.client.activity.mainregion.InstructorMemberActivity;
import ca.jhosek.main.client.activity.mainregion.InstructorStartActivity;
import ca.jhosek.main.client.activity.mainregion.InstructorTipsActivity;
import ca.jhosek.main.client.activity.mainregion.InstructorYourCourseActivity;
import ca.jhosek.main.client.activity.mainregion.LoginFormActivity;
import ca.jhosek.main.client.activity.mainregion.LostAccountActivity;
import ca.jhosek.main.client.activity.mainregion.MyCoursesActivity;
import ca.jhosek.main.client.activity.mainregion.MyProfileActivity;
import ca.jhosek.main.client.activity.mainregion.NewInstructorFormActivity;
import ca.jhosek.main.client.activity.mainregion.NewStudentFormActivity;
import ca.jhosek.main.client.activity.mainregion.PartnerInviteActivity;
import ca.jhosek.main.client.activity.mainregion.PrivacyActivity;
import ca.jhosek.main.client.activity.mainregion.QueryCoursesActivity;
import ca.jhosek.main.client.activity.mainregion.QueryUsersActivity;
import ca.jhosek.main.client.activity.mainregion.RecoverPasswordActivity;
import ca.jhosek.main.client.activity.mainregion.SessionControlActivity;
import ca.jhosek.main.client.activity.mainregion.StudentCourseDetailReportActivity;
import ca.jhosek.main.client.activity.mainregion.StudentHomeActivity;
import ca.jhosek.main.client.activity.mainregion.StudentInfoActivity;
import ca.jhosek.main.client.activity.mainregion.StudentStartActivity;
import ca.jhosek.main.client.activity.mainregion.StudentYourCourseActivity;
import ca.jhosek.main.client.activity.mainregion.TermsActivity;
import ca.jhosek.main.client.activity.mainregion.Video1Activity;
import ca.jhosek.main.client.activity.mainregion.Video2Activity;
import ca.jhosek.main.client.activity.mainregion.Video3Activity;
import ca.jhosek.main.client.activity.mainregion.WhatIsActivity;
import ca.jhosek.main.client.activity.mainregion.WhoIsActivity;
import ca.jhosek.main.client.activity.menuregion.AdminMenuActivity;
import ca.jhosek.main.client.activity.menuregion.AnonMenuActivity;
import ca.jhosek.main.client.activity.userregion.HeaderAnonUserActivity;
import ca.jhosek.main.client.activity.userregion.HeaderUserActivity;
import ca.jhosek.main.client.mvp.MvpModule;
import ca.jhosek.main.client.ui.ViewModule;

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
