/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.ui.priv.instructor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;

import ca.jhosek.main.client.activity.mainregion.InstructorMemberActivity;
import ca.jhosek.main.client.place.InstructorMemberPlace;

/**
 * 
 * Instructor Menu View
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 * @see InstructorMemberView
 * @see InstructorMemberActivity
 * @see InstructorMemberPlace
 */
public class InstructorMenuViewImpl extends Composite implements InstructorMenuView {

	private static AdminMenuViewImplUiBinder uiBinder = GWT
			.create(AdminMenuViewImplUiBinder.class);
	
	@UiField MenuItem instructorHome;
	@UiField MenuItem instructorStart;
	@UiField MenuItem myProfile;
	@UiField MenuItem myClasses;
	@UiField MenuItem instructorTips;
	
	private Presenter presenter;

	interface AdminMenuViewImplUiBinder extends
			UiBinder<Widget, InstructorMenuViewImpl> {
	}

	public InstructorMenuViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		
		instructorHome.setScheduledCommand( new Scheduler.ScheduledCommand() {
			
			public void execute() {
				presenter.goToInstructorHome();
				
			}
		});		
		
		myProfile.setScheduledCommand( new Scheduler.ScheduledCommand() {
			
			public void execute() {
				presenter.goToMyProfile();
				
			}
		});		

		instructorStart.setScheduledCommand( new Scheduler.ScheduledCommand() {
			
			public void execute() {
				presenter.goToInstructorStart();
				
			}
		});		

		myClasses.setScheduledCommand( new Scheduler.ScheduledCommand() {
			
			public void execute() {
				presenter.goToMyClasses();
				
			}
		});		

		instructorTips.setScheduledCommand( new Scheduler.ScheduledCommand() {
			
			public void execute() {
				// 
				presenter.goToInstructorTips();
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
