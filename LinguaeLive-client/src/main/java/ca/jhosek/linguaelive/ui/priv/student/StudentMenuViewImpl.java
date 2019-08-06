/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.ui.priv.student;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;

/**
 * Student Menu View
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class StudentMenuViewImpl extends Composite implements StudentMenuView {

	private static StudentMenuViewImplUiBinder uiBinder = GWT
			.create(StudentMenuViewImplUiBinder.class);
	@UiField MenuItem studentHome;
	@UiField MenuItem studentStart;
	@UiField MenuItem myProfile;
	@UiField MenuItem yourCourses;
	
	private Presenter presenter;

	interface StudentMenuViewImplUiBinder extends
			UiBinder<Widget, StudentMenuViewImpl> {
	}

	public StudentMenuViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		
		studentHome.setScheduledCommand( new Scheduler.ScheduledCommand() {
			
			public void execute() {
				presenter.goToStudentHome();
				
			}
		});		
		
		myProfile.setScheduledCommand( new Scheduler.ScheduledCommand() {
			
			public void execute() {
				presenter.goToMyProfile();
				
			}
		});		
		
		studentStart.setScheduledCommand( new Scheduler.ScheduledCommand() {
			
			public void execute() {
				presenter.goToStudentStart();
				
			}
		});		

		yourCourses.setScheduledCommand( new Scheduler.ScheduledCommand() {
			
			public void execute() {
				presenter.goToMyClasses();
				
			}
		});		
		
	}

	/**
	 * @param anonMenuActivity
	 */
	public void setPresenter( Presenter presenter ) {
		this.presenter = presenter;		
	}

}
