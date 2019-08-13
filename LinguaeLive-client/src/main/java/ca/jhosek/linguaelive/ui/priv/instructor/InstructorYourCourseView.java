package ca.jhosek.linguaelive.ui.priv.instructor;

import java.util.List;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

import ca.jhosek.linguaelive.activity.mainregion.InstructorYourCourseActivity;
import ca.jhosek.linguaelive.ui.priv.instructor.InstructorYourCourseViewImpl.Driver;
import ca.jhosek.linguaelive.proxy.CourseLinkProxy;
import ca.jhosek.linguaelive.proxy.CourseProxy;
import ca.jhosek.linguaelive.proxy.MemberProxy;

/**
 * @author copyright (C) 2011 Andrew Stevko
 *
 * @see InstructorYourCourseView
 * @see InstructorYourCourseViewImpl
 * @see InstructorYourCourseActivity
 * 
 */
public interface InstructorYourCourseView extends IsWidget {

	public void setPresenter(Presenter presenter);
	/**
	 * @param course
	 */
	public void showLinkedCourses( List<CourseLinkProxy> linkedCourses, boolean updateDisplay );
	public void showUnlinkedCourses(List<CourseProxy> unlinkedCourses, boolean updateDisplay);
	public void showMembers( List<MemberProxy> studentMembers );

	/**
	 * clear the display
	 */
	public void clear();

	/**
	 * refresh the display editors
	 */
	public void refresh();
	
	/**
	 *	Is this an instructor viewing the course?
	 *  Is this the owner viewing the course? 
	 *  
	 * @param instrcutorModel
	 * @param ownerMode
	 */
	public void setViewerMode( Long courseId, boolean instrcutorModel, boolean ownerMode );
	/**
	 * What can the instructor do from viewing a course?
	 * edit it
	 * view members
	 * view linked courses
	 * view unlinked courses
	 * 
	 * @author copyright (C) 2011 Andrew Stevko
	 *
	 */
	public interface Presenter {
		void gotoEditCourse();
//		void goToCourseSearch( CourseProxy myCourse );
		void goToMemberView( MemberProxy member );
		void goToCourseLinkView( CourseLinkProxy courseLink );
		void inviteToLinkCourses( CourseProxy unlinkedCourse, String personalMessage );
//		void goToSearchCompCourses();
		void goToInviteStudents();
		CourseProxy getMyCourse();
		void gotoStudentReport();
		void gotoSessionReport();

		// admin and owner only 
		void deleteCourse();
		/**
		 * reload unlinked courses to show current vs past/future
		 */
		void loadUnlinkedCourses();
	}
	
	public Driver createEditorDriver(EventBus eventBus,
			RequestFactory requestFactory);
	boolean isShowPastCourses();
	void showTheRightPanel();
}
