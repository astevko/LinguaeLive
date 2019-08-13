package ca.jhosek.linguaelive.ui;

import com.google.gwt.inject.client.AbstractGinModule;

import ca.jhosek.linguaelive.mvp.MainRegionActivityMapper;
import ca.jhosek.linguaelive.place.AppPlaceHistoryMapper;
import ca.jhosek.linguaelive.ui.anon.AnonMenuView;
import ca.jhosek.linguaelive.ui.anon.AnonMenuViewImpl;
import ca.jhosek.linguaelive.ui.anon.ContactUsView;
import ca.jhosek.linguaelive.ui.anon.ContactUsViewImpl;
import ca.jhosek.linguaelive.ui.anon.CourseBrowseView;
import ca.jhosek.linguaelive.ui.anon.CourseBrowseViewImpl;
import ca.jhosek.linguaelive.ui.anon.FaqView;
import ca.jhosek.linguaelive.ui.anon.HeaderAnonUserView;
import ca.jhosek.linguaelive.ui.anon.HeaderAnonUserViewImpl;
import ca.jhosek.linguaelive.ui.anon.IndexView;
import ca.jhosek.linguaelive.ui.anon.IndexViewImpl;
import ca.jhosek.linguaelive.ui.anon.InstructorInfoView;
import ca.jhosek.linguaelive.ui.anon.LoginFormView;
import ca.jhosek.linguaelive.ui.anon.LoginFormViewImpl;
import ca.jhosek.linguaelive.ui.anon.LostAccountView;
import ca.jhosek.linguaelive.ui.anon.LostAccountViewImpl;
import ca.jhosek.linguaelive.ui.anon.NewInstructorPanel;
import ca.jhosek.linguaelive.ui.anon.NewInstructorPanelImpl;
import ca.jhosek.linguaelive.ui.anon.NewStudentPanel;
import ca.jhosek.linguaelive.ui.anon.NewStudentPanelImpl;
import ca.jhosek.linguaelive.ui.anon.PrivacyView;
import ca.jhosek.linguaelive.ui.anon.RecoverPasswordView;
import ca.jhosek.linguaelive.ui.anon.RecoverPasswordViewImpl;
import ca.jhosek.linguaelive.ui.anon.StudentInfoView;
import ca.jhosek.linguaelive.ui.anon.TermsView;
import ca.jhosek.linguaelive.ui.anon.Video1View;
import ca.jhosek.linguaelive.ui.anon.Video2View;
import ca.jhosek.linguaelive.ui.anon.Video3View;
import ca.jhosek.linguaelive.ui.anon.WhatIsView;
import ca.jhosek.linguaelive.ui.anon.WhoIsView;
import ca.jhosek.linguaelive.ui.priv.AddEditContactInfoView;
import ca.jhosek.linguaelive.ui.priv.AddEditContactInfoViewImpl;
import ca.jhosek.linguaelive.ui.priv.ChangePswdView;
import ca.jhosek.linguaelive.ui.priv.ChangePswdViewImpl;
import ca.jhosek.linguaelive.ui.priv.HeaderUserView;
import ca.jhosek.linguaelive.ui.priv.HeaderUserViewImpl;
import ca.jhosek.linguaelive.ui.priv.MyCoursesView;
import ca.jhosek.linguaelive.ui.priv.MyCoursesViewImpl;
import ca.jhosek.linguaelive.ui.priv.MyProfileView;
import ca.jhosek.linguaelive.ui.priv.MyProfileViewImpl;
import ca.jhosek.linguaelive.ui.priv.admin.AdminHomeView;
import ca.jhosek.linguaelive.ui.priv.admin.AdminHomeViewImpl;
import ca.jhosek.linguaelive.ui.priv.admin.AdminMenuView;
import ca.jhosek.linguaelive.ui.priv.admin.AdminMenuViewImpl;
import ca.jhosek.linguaelive.ui.priv.admin.QueryCoursesView;
import ca.jhosek.linguaelive.ui.priv.admin.QueryCoursesViewImpl;
import ca.jhosek.linguaelive.ui.priv.admin.QueryUsersView;
import ca.jhosek.linguaelive.ui.priv.admin.QueryUsersViewImpl;
import ca.jhosek.linguaelive.ui.priv.instructor.AddEditCourseView;
import ca.jhosek.linguaelive.ui.priv.instructor.AddEditCourseViewImpl;
import ca.jhosek.linguaelive.ui.priv.instructor.InstructorCourseDetailReportView;
import ca.jhosek.linguaelive.ui.priv.instructor.InstructorCourseDetailReportViewImpl;
import ca.jhosek.linguaelive.ui.priv.instructor.InstructorCourseLinkView;
import ca.jhosek.linguaelive.ui.priv.instructor.InstructorCourseLinkViewImpl;
import ca.jhosek.linguaelive.ui.priv.instructor.InstructorCourseSummaryReportView;
import ca.jhosek.linguaelive.ui.priv.instructor.InstructorCourseSummaryReportViewImpl;
import ca.jhosek.linguaelive.ui.priv.instructor.InstructorHomeView;
import ca.jhosek.linguaelive.ui.priv.instructor.InstructorHomeViewImpl;
import ca.jhosek.linguaelive.ui.priv.instructor.InstructorMemberView;
import ca.jhosek.linguaelive.ui.priv.instructor.InstructorMemberViewImpl;
import ca.jhosek.linguaelive.ui.priv.instructor.InstructorMenuView;
import ca.jhosek.linguaelive.ui.priv.instructor.InstructorMenuViewImpl;
import ca.jhosek.linguaelive.ui.priv.instructor.InstructorStartView;
import ca.jhosek.linguaelive.ui.priv.instructor.InstructorStartViewImpl;
import ca.jhosek.linguaelive.ui.priv.instructor.InstructorTipsView;
import ca.jhosek.linguaelive.ui.priv.instructor.InstructorTipsViewImpl;
import ca.jhosek.linguaelive.ui.priv.instructor.InstructorYourCourseView;
import ca.jhosek.linguaelive.ui.priv.instructor.InstructorYourCourseViewImpl;
import ca.jhosek.linguaelive.ui.priv.student.AddCourseMemberView;
import ca.jhosek.linguaelive.ui.priv.student.AddCourseMemberViewImpl;
import ca.jhosek.linguaelive.ui.priv.student.PartnerInviteView;
import ca.jhosek.linguaelive.ui.priv.student.PartnerInviteViewImpl;
import ca.jhosek.linguaelive.ui.priv.student.SessionControlView;
import ca.jhosek.linguaelive.ui.priv.student.SessionControlViewImpl;
import ca.jhosek.linguaelive.ui.priv.student.StudentCourseDetailReportView;
import ca.jhosek.linguaelive.ui.priv.student.StudentCourseDetailReportViewImpl;
import ca.jhosek.linguaelive.ui.priv.student.StudentHomeView;
import ca.jhosek.linguaelive.ui.priv.student.StudentHomeViewImpl;
import ca.jhosek.linguaelive.ui.priv.student.StudentMenuView;
import ca.jhosek.linguaelive.ui.priv.student.StudentMenuViewImpl;
import ca.jhosek.linguaelive.ui.priv.student.StudentStartView;
import ca.jhosek.linguaelive.ui.priv.student.StudentStartViewImpl;
import ca.jhosek.linguaelive.ui.priv.student.StudentYourCourseView;
import ca.jhosek.linguaelive.ui.priv.student.StudentYourCourseViewImpl;
import ca.jhosek.linguaelive.widgets.EditLabel;

/**
 * One of three places to insert new panels
 * 
 * @see MainRegionActivityMapper
 * @see ViewModule
 * @see AppPlaceHistoryMapper
 * 
 * @author copyright (C) 2011 Andrew Stevko
 */
public class ViewModule extends AbstractGinModule {

	@Override
	protected void configure() {
		// anon views
		bind(AddEditCourseView.class).to(AddEditCourseViewImpl.class);
		bind(AddCourseMemberView.class).to(AddCourseMemberViewImpl.class);
		bind(AdminMenuView.class).to(AdminMenuViewImpl.class);
		bind(AnonMenuView.class).to(AnonMenuViewImpl.class);
		bind(ContactUsView.class).to(ContactUsViewImpl.class);
		bind(CourseBrowseView.class).to(CourseBrowseViewImpl.class);
		bind(FaqView.class);
		bind(HeaderAnonUserView.class).to(HeaderAnonUserViewImpl.class);
		bind(IndexView.class).to(IndexViewImpl.class);
		bind(InstructorInfoView.class);
		bind(LoginFormView.class).to(LoginFormViewImpl.class);
		bind(LostAccountView.class).to(LostAccountViewImpl.class);
		bind(RecoverPasswordView.class).to(RecoverPasswordViewImpl.class);
		bind(MyCoursesView.class).to(MyCoursesViewImpl.class);
		bind(AddEditContactInfoView.class).to(AddEditContactInfoViewImpl.class);
		bind(MyProfileView.class).to(MyProfileViewImpl.class);
		bind(NewInstructorPanel.class).to(NewInstructorPanelImpl.class);
		bind(NewStudentPanel.class).to(NewStudentPanelImpl.class);
		bind(PrivacyView.class);
		bind(QueryUsersView.class).to(QueryUsersViewImpl.class);
		bind(QueryCoursesView.class).to(QueryCoursesViewImpl.class);
		bind(StudentInfoView.class);
		bind(TermsView.class);
		bind(WhatIsView.class);
		bind(WhoIsView.class);
		bind(Video1View.class);
		bind(Video2View.class);
		bind(Video3View.class);
		bind(EditLabel.class);

		bind(ChangePswdView.class).to(ChangePswdViewImpl.class);
		// priv views
		bind(HeaderUserView.class).to(HeaderUserViewImpl.class);

		// Admin views
		bind(AdminHomeView.class).to(AdminHomeViewImpl.class);

		// Instructor views
		bind(InstructorYourCourseView.class).to(
				InstructorYourCourseViewImpl.class);
		bind(InstructorHomeView.class).to(InstructorHomeViewImpl.class);
		bind(InstructorMenuView.class).to(InstructorMenuViewImpl.class);
		bind(InstructorStartView.class).to(InstructorStartViewImpl.class);
		bind(InstructorCourseLinkView.class).to(
				InstructorCourseLinkViewImpl.class);
		bind(InstructorTipsView.class).to(InstructorTipsViewImpl.class);
		bind(InstructorMemberView.class).to(InstructorMemberViewImpl.class);
		bind(InstructorCourseSummaryReportView.class).to(
				InstructorCourseSummaryReportViewImpl.class);
		bind(InstructorCourseDetailReportView.class).to(
				InstructorCourseDetailReportViewImpl.class);

		// Student views
		bind(StudentHomeView.class).to(StudentHomeViewImpl.class);
		bind(StudentMenuView.class).to(StudentMenuViewImpl.class);
		bind(StudentStartView.class).to(StudentStartViewImpl.class);
		bind(StudentYourCourseView.class).to(StudentYourCourseViewImpl.class);
		bind(PartnerInviteView.class).to(PartnerInviteViewImpl.class);
		bind(SessionControlView.class).to(SessionControlViewImpl.class);
		bind(StudentCourseDetailReportView.class).to(
				StudentCourseDetailReportViewImpl.class);

		bind(MainView.class);
	}

}
