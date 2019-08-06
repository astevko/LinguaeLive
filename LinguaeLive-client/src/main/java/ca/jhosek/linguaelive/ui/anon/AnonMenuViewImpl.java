/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.ui.anon;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;

/**
 * Anon Menu View
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class AnonMenuViewImpl extends Composite implements AnonMenuView {

	private static AnonMenuViewImplUiBinder uiBinder = GWT
			.create(AnonMenuViewImplUiBinder.class);
	
	@UiField MenuItem home;
	@UiField MenuItem whatIs;
	@UiField MenuItem faq;
	@UiField MenuItem instructorInfo;
	@UiField MenuItem studentInfo;
	@UiField MenuItem whoIs;
	
	private Presenter presenter;

	interface AnonMenuViewImplUiBinder extends
			UiBinder<Widget, AnonMenuViewImpl> {
	}

	public AnonMenuViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		
		home.setScheduledCommand( new Scheduler.ScheduledCommand() {
			
			public void execute() {
				presenter.goToIndex();
				
			}
		});		
		
		whatIs.setScheduledCommand( new Scheduler.ScheduledCommand() {
			
			public void execute() {
				presenter.goToWhatIs();
				
			}
		});		
		
		whoIs.setScheduledCommand( new Scheduler.ScheduledCommand() {
			
			public void execute() {
				presenter.goToWhoIs();
				
			}
		});		
		
		faq.setScheduledCommand( new Scheduler.ScheduledCommand() {
			
			public void execute() {
				presenter.goToFaq();
				
			}
		});		
		
		instructorInfo.setScheduledCommand( new Scheduler.ScheduledCommand() {
			
			public void execute() {
				presenter.goToInstructorInfo();
				
			}
		});		
		
		
		studentInfo.setScheduledCommand( new Scheduler.ScheduledCommand() {
			
			public void execute() {
				presenter.goToStudentInfo();
				
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
