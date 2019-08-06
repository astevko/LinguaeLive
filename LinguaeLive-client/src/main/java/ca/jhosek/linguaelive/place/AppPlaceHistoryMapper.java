package ca.jhosek.main.client.place;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

import ca.jhosek.main.client.mvp.MainRegionActivityMapper;
import ca.jhosek.main.client.ui.ViewModule;

/**
 * Map URLs to Places One of three places to insert new panels
 * 
 * @see MainRegionActivityMapper
 * @see ViewModule
 * @see AppPlaceHistoryMapper
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * 
 */
@WithTokenizers({ // in alphabetical order matching the project directory on the
					// left
		AddCourseMemberPlace.Tokenizer.class,
		AddEditCoursePlace.Tokenizer.class,
		AdminHomePlace.Tokenizer.class,
		ContactUsPlace.Tokenizer.class,
		CourseBrowsePlace.Tokenizer.class,
		FaqPlace.Tokenizer.class,
		IndexPlace.Tokenizer.class,
		InstructorYourCoursePlace.Tokenizer.class,
		StudentYourCoursePlace.Tokenizer.class,
		InstructorCourseLinkPlace.Tokenizer.class,
		InstructorHomePlace.Tokenizer.class,
		InstructorInfoPlace.Tokenizer.class,
		InstructorStartPlace.Tokenizer.class,
		InstructorTipsPlace.Tokenizer.class,
		LoginFormPlace.Tokenizer.class,
		LostAccountPlace.Tokenizer.class,
		RecoverPasswordPlace.Tokenizer.class,
		// JoinClassPlace.Tokenizer.class,
		MyCoursesPlace.Tokenizer.class,
		// MyClassPlace.Tokenizer.class,
		MyProfilePlace.Tokenizer.class,
		AddEditContactInfoPlace.Tokenizer.class,
		NewInstructorPlace.Tokenizer.class, NewStudentPlace.Tokenizer.class,
		PrivacyPlace.Tokenizer.class, QueryCoursesPlace.Tokenizer.class,
		QueryUsersPlace.Tokenizer.class, StudentHomePlace.Tokenizer.class,
		StudentInfoPlace.Tokenizer.class, StudentStartPlace.Tokenizer.class,
		PartnerInvitePlace.Tokenizer.class,
		SessionControlPlace.Tokenizer.class, TermsPlace.Tokenizer.class,
		WhatIsPlace.Tokenizer.class, WhoIsPlace.Tokenizer.class,
		InstructorMemberPlace.Tokenizer.class,
		InstructorCourseSummaryReportPlace.Tokenizer.class,
		InstructorCourseDetailReportPlace.Tokenizer.class,
		StudentCourseDetailReportPlace.Tokenizer.class,
		Video1Place.Tokenizer.class, Video2Place.Tokenizer.class,
		Video3Place.Tokenizer.class, ChangePswdPlace.Tokenizer.class })
public interface AppPlaceHistoryMapper extends PlaceHistoryMapper {
}
