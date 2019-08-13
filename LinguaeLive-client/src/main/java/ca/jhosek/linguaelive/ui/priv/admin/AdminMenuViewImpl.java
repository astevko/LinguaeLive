/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.ui.priv.admin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;

import ca.jhosek.linguaelive.activity.menuregion.AdminMenuActivity;

/**
 * Admin Menu View
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see AdminMenuActivity
 */
public class AdminMenuViewImpl extends Composite implements AdminMenuView {

	private static AdminMenuViewImplUiBinder uiBinder = GWT
			.create(AdminMenuViewImplUiBinder.class);
	@UiField MenuItem adminHome;
	@UiField MenuItem queryUsers;
	@UiField MenuItem courses;
	
	private Presenter presenter;

	interface AdminMenuViewImplUiBinder extends
			UiBinder<Widget, AdminMenuViewImpl> {
	}

	public AdminMenuViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		
		adminHome.setScheduledCommand( new Scheduler.ScheduledCommand() {

			public void execute() {
				presenter.goToAdminHome();
				
			}
		});		
		
		queryUsers.setScheduledCommand( new Scheduler.ScheduledCommand() {
			
			public void execute() {
				presenter.goToQueryUsers();
				
			}
		});		
		
		courses.setScheduledCommand( new Scheduler.ScheduledCommand() {
			
			public void execute() {
				presenter.goToQueryCourses();
				
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
