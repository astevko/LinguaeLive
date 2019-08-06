/**
 * 
 */
package ca.jhosek.main.client.ui.priv.student;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DateLabel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

import ca.jhosek.main.client.AppResources;
import ca.jhosek.main.client.LLConstants;
import ca.jhosek.main.client.activity.mainregion.PartnerInviteActivity;
import ca.jhosek.main.client.widgets.OnlineOfflineIndicator;
import ca.jhosek.main.shared.LanguageType;
import ca.jhosek.main.shared.proxy.PartnerInviteProxy;

/**
 * @author copyright (C) 2011 Andrew Stevko
 * @see PartnerInviteProxy
 * @see PartnerInviteView
 * @see PartnerInviteActivity
 * 
 */
public class PartnerInviteViewImpl extends Composite 
	implements IsWidget, PartnerInviteView,	Editor<PartnerInviteProxy> {

	public interface Driver extends RequestFactoryEditorDriver<PartnerInviteProxy, PartnerInviteViewImpl> {

	}

	// logger boilerplate
	private static final Logger logger = Logger.getLogger(PartnerInviteViewImpl.class
			.getName());

	private static SessionInviteUiBinder uiBinder = 
			GWT.create( SessionInviteUiBinder.class);

	interface SessionInviteUiBinder extends
		UiBinder<Widget, PartnerInviteViewImpl> {
	}
	@Ignore @UiField Label partnerInviteMsg;
	
	// Member 1
	@Path( "member1.user.isOnline")
	@UiField (provided = true)
	OnlineOfflineIndicator member1_user_isonline;
	@Path( "member1.user.firstName" )
	@UiField InlineLabel member1_user_fname;
	@Path( "member1.user.lastName" )
	@UiField InlineLabel member1_user_lname;
	@Path( "member1.user.school" )
	@UiField Label member1_user_school;
	@Path( "member1.course.name")
	@UiField Label member1_course_name;
	@Path( "member1.user.location" )
	@UiField Label member1_user_location;
	@Path( "member1.user.emailAddress" )
	@UiField Label member1_user_email;
	
	// member2
	@Path( "member2.user.isOnline")
	@UiField (provided = true)
	OnlineOfflineIndicator member2_user_isonline;
	@Path( "member2.user.firstName" )
	@UiField InlineLabel member2_user_fname;
	@Path( "member2.user.lastName" )
	@UiField InlineLabel member2_user_lname;
	@Path( "member2.user.school" )
	@UiField Label member2_user_school;
	@Path( "member2.course.name")
	@UiField Label member2_course_name;
	@Path( "member2.user.location" )
	@UiField Label member2_user_location;
	@Path( "member2.user.emailAddress" )
	@UiField Label member2_user_email;

	@UiField DateLabel createDate;
//	@UiField DateLabel startDateTime;
	
	// buttons
	@UiField Button confirmLink;
	@UiField Button declineLink;
	@UiField Button unlinkCourses;
	@Ignore @UiField Label pendingBlurb;
	@Ignore @UiField Label declinedBlurb;
	@Ignore @UiField Label acceptedBlurb;
	
	/**
	 * search for inprgress sessions
	 */
	@UiField Button refreshLink;	
	/**
	 * student wants to start a new sesion
	 */
//	@UiField Button startNewSessionButton;
	@UiField Button startSession;
	@UiField Button startOtherSession;
	
	private Presenter presenter;

	
//	/**
//	 * timer used to refresh the web page ever 15 seconds while page is loaded
//	 */
//	@Deprecated
//	private Timer refreshTimer;

	@SuppressWarnings("unused")
	private static int TIMER_REFRESH_WAIT = 1000 * 15;	// delay in updating interface

	private final LLConstants constants;

	@SuppressWarnings("unused")
	private final AppResources resources;
	
	
	@Inject
	public PartnerInviteViewImpl(final LLConstants constants,  
			final AppResources resources) {
		this.constants = constants;
		this.resources = resources;
		
		member1_user_isonline = new OnlineOfflineIndicator();
		member2_user_isonline = new OnlineOfflineIndicator();
		
		initWidget(uiBinder.createAndBindUi(this));
		
		
//		// create timer object here
//		refreshTimer = new Timer() {
//			
//			@Override
//			public void run() {
//				// check for inprogress sessions
//				presenter.checkForInprogressSessions();
//			}
//		};
		
	}

	/**
	 * @return a CourseProxy editor driver, userProxyDriver, tied to this presenter and view
	 */
	public Driver createEditorDriver(EventBus eventBus, RequestFactory requestFactory) {
		// 
		logger.info( "createEditorDriver( SessionInviteViewImpl )");
		Driver driver = GWT.create(Driver.class);
		driver.initialize( eventBus, requestFactory, this );
		return driver;
	}


	public void setPresenter(Presenter presenter) {
		// 
		this.presenter = presenter;
		clear();
	}


	/**
	 * clear the display
	 * @see ca.jhosek.main.client.ui.priv.student.SessionControlView#clear()
	 */
	public void clear() {
		//
//		stopPollingForSessionUpdate();

		acceptedBlurb.setVisible( false );		
		pendingBlurb.setVisible( false);
		confirmLink.setVisible( false );			// pending shows accept
		declineLink.setVisible( false );				// pending shows declined
		unlinkCourses.setVisible( false ); 			// accepted shows unlink
		declinedBlurb.setVisible( false ); 			// declined
//		startNewSessionButton.setVisible( !pending && accepted );	// new session button
		startSession.setVisible( false );	// new session button
		startOtherSession.setVisible( false );	// new session button
		
//		courseB_instructor_email.setVisible( !pending && accepted ) ;
		member2_user_email.setText(constants.hiddenUntilConfirmed());

//		member1_user_isonline.removeStyleName(resources.css().offline());
//		member1_user_isonline.removeStyleName(resources.css().online());
//		
//		member2_user_isonline.removeStyleName(resources.css().offline());
//		member2_user_isonline.removeStyleName(resources.css().online());
//		
//		startPollingForSessionUpdate();
	}
	
	/**
	 * display only the proper buttons
	 * @see ca.jhosek.main.client.ui.priv.instructor.InstructorCourseLinkView#setViewMode(java.lang.Boolean, java.lang.Boolean)
	 */
	public void setViewMode( Boolean isSender, Boolean pending, Boolean accepted ) {
		// is member1 online or offline
		boolean isMember1Online = true; // member1_user_isonline.getValue();
		// is member2 online or offline
		boolean isMember2Online = true; // member2_user_isonline.getValue();

		acceptedBlurb.setVisible( accepted );		
		pendingBlurb.setVisible( pending );
		confirmLink.setVisible( !isSender && !accepted );			// pending shows accept
		declineLink.setVisible( /*!isSender && */pending);				// pending shows declined
		unlinkCourses.setVisible( !pending && accepted ); 			// accepted shows unlink
		declinedBlurb.setVisible( !pending && !accepted ); 			// declined
//		startNewSessionButton.setVisible( !pending && accepted );	// new session button
		startSession.setVisible( !pending && accepted );	// new session button
		startSession.setEnabled( !pending && accepted && isMember1Online && isMember2Online );	// new session button
		startOtherSession.setVisible( !pending && accepted );	// new session button
		startOtherSession.setEnabled( !pending && accepted && isMember1Online && isMember2Online );	// new session button

		// TODO Move to properties 
		if (accepted) {
			partnerInviteMsg.setText(constants.partnerSessionStart());
		} else {
			partnerInviteMsg.setText(constants.partnerInvite());			
		}
		
		
//		courseB_instructor_email.setVisible( !pending && accepted ) ;
		if( pending || !accepted ) {
			member2_user_email.setText(constants.hiddenUntilConfirmed());
		}
		
//		startPollingForSessionUpdate();
	}

	
	
//	/**
//	 * refresh timer stop.
//	 */
//	@Deprecated
//	private void stopPollingForSessionUpdate() {
//		// stop refresh timer
//		refreshTimer.cancel();
//	}
//
//	/**
//	 * start a time that refreshes display every 30 seconds 
//	 */
//	private void startPollingForSessionUpdate() {
//		// start timer
//		//refreshTimer.scheduleRepeating(TIMER_REFRESH_WAIT);
//	}
//	
	@UiHandler("confirmLink")
	void onConfirmLinkClick(ClickEvent event) {
		presenter.respondToSessionInvite(true);
	}
	@UiHandler("declineLink")
	void onDeclineLinkClick(ClickEvent event) {
		presenter.respondToSessionInvite(false);
	}
	@UiHandler("unlinkCourses")
	void onUnlinkCoursesClick(ClickEvent event) {
		presenter.respondToSessionInvite(false);		
	}
	@UiHandler("startSession")
	void onStartSessionClick(ClickEvent event) {
		presenter.startSessionWithCurrentInvite( );		
	}
	@UiHandler("startOtherSession")
	void onStartOtherSessionClick(ClickEvent event) {
		presenter.startOtherSessionWithCurrentInvite( );		
	}
	@UiHandler("member1_course_name")
	void onMember1CourseNameClick(ClickEvent event) {
		presenter.gotoMember1Course();
	}
	@UiHandler("member2_course_name")
	void onMember2CourseNameClick(ClickEvent event) {
		presenter.gotoMember2Course();
	}
	@UiHandler("refreshLink")
	void onRefreshLinkClick(ClickEvent event) {
		logger.info( "onRefreshLinkClick()");
		presenter.checkForInprogressSessions();
	}
	

	public void updateLanguageButtons(LanguageType first, LanguageType second) {
		startSession.setText(constants.startTiming() +
				" " + second.name() + " " + constants.session());		
		startOtherSession.setText(constants.startTiming() +
				" " + first.name() + " " + constants.session());		
	}
	/**
	 * set user2 online status display
	 * @param onlineStatus  ONLINE | OFFLINE | INSESSION | UNKNOWN
	 */
	@Override
	public void showMember2UserOnlineStatus(final String onlineStatus ) {
		final boolean isOnline = ("ONLINE".equals(onlineStatus) || "INSESSION".equals(onlineStatus));
		member2_user_isonline.setValue(isOnline);
	}
	
	/**
	 * set user1 online status display
	 * @param onlineStatus  ONLINE | OFFLINE | INSESSION | UNKNOWN
	 */
	@Override
	public void showMember1UserOnlineStatus(final String onlineStatus ) {
		final boolean isOnline = ("ONLINE".equals(onlineStatus) || "INSESSION".equals(onlineStatus));
		member1_user_isonline.setValue(isOnline);
	}
	
}
