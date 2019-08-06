package ca.jhosek.main.client.ui.priv.instructor;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;

import ca.jhosek.main.client.activity.mainregion.InstructorCourseSummaryReportActivity;
import ca.jhosek.main.client.place.InstructorCourseSummaryReportPlace;
import ca.jhosek.main.shared.proxy.CourseProxy;
import ca.jhosek.main.shared.proxy.MemberProxy;

/**
 * @author copyright (C) 2011 Andrew Stevko
 *
 * related view
 * @see InstructorCourseSummaryReportView
 * 
 * @see InstructorCourseSummaryReportActivity
 * @see InstructorCourseSummaryReportPlace
 * 
 */
public interface InstructorCourseSummaryReportView extends IsWidget {

	public void showCourse( CourseProxy course);
	/**
	 * @param studentMembers
	 */
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
	public void setViewerMode( Long courseId, boolean instructorModel, boolean ownerMode );
	
	/**
	 * the owner of this view - used for click back
	 * @param instructorCourseSummaryReportActivity
	 */
	void setPresenter(InstructorCourseSummaryReportActivity instructorCourseSummaryReportActivity);
}
