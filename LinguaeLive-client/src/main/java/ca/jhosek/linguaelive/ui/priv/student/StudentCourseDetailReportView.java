package ca.jhosek.main.client.ui.priv.student;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;

import ca.jhosek.main.client.activity.mainregion.StudentCourseDetailReportActivity;
import ca.jhosek.main.shared.proxy.CourseProxy;
import ca.jhosek.main.shared.proxy.SessionProxy;
import ca.jhosek.main.shared.proxy.UserProxy;

/**
 * @author copyright (C) 2011 Andrew Stevko
 *
 * @see StudentCourseDetailReportViewImpl
 * 
 */
public interface StudentCourseDetailReportView extends IsWidget {

	/**
	 * @param student
	 */
	public void showUser(UserProxy student);
	/**
	 * @param course
	 */
	public void showCourse( CourseProxy course);
	/**
	 * @param studentMembers
	 */
	public void showSessions( List<SessionProxy> sessions );

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
	 * @param presenter
	 */
	void setPresenter(StudentCourseDetailReportActivity presenter);
}
