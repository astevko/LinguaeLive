/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.ui.priv.student;

import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;

import ca.jhosek.linguaelive.AppResources;
import ca.jhosek.linguaelive.LLConstants;
import ca.jhosek.linguaelive.StylesCssResource;
import ca.jhosek.linguaelive.activity.mainregion.StudentHomeActivity;
import ca.jhosek.linguaelive.place.StudentHomePlace;
import ca.jhosek.linguaelive.ui.GroupingHandlerRegistration;
import ca.jhosek.linguaelive.proxy.CourseProxy;
import ca.jhosek.linguaelive.proxy.PartnerInviteProxy;
import ca.jhosek.linguaelive.proxy.SessionProxy;
import ca.jhosek.linguaelive.proxy.UserProxy;

/**
 *  Student user home page 
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see StudentHomeActivity
 * @see StudentHomePlace
 * @see StudentHomeView
 * 
 */
public class StudentHomeViewImpl extends Composite implements StudentHomeView {

//	private static final String NO_UNFINISHED_SESSIONS_FOUND = "<i>No active sessions found.</i>";
//
//	private static final String NO_PENDING_SESSION_INVITES_FOUND = "<i>No pending partner invites found.</i>";

	// logger boilerplate
	private static final Logger logger = Logger.getLogger(StudentHomeViewImpl.class
			.getName());

	private static final String LIST_PROMPT_DIV_W_STYLE = "<div style=\"font-weight: bold;cursor: pointer;color: #d35e46;\" >";

	private static final String START_DATE_FORMAT = "MMM d, yyyy";
	private static DateTimeFormat mdyDateFormat = DateTimeFormat.getFormat(  START_DATE_FORMAT);  

	
	interface HomeViewUiBinder extends UiBinder<Widget, StudentHomeViewImpl> {
	}
	private static HomeViewUiBinder uiBinder = GWT.create(HomeViewUiBinder.class);
	
	/**
	 * panel for holding Course Buttons
	 */
	@UiField HorizontalPanel coursesPanel;
	
	//------------------
	// session invitations
	@UiField FlowPanel sessionInvitesPanel;
	private final CellList<PartnerInviteProxy> sessionInvitesCList;
	private final SimplePager 		  sessionInvitesPager;
	private ListDataProvider<PartnerInviteProxy> sessionInvitesDP;

	//------------------
	// sessions
	@UiField FlowPanel sessionsPanel;
	private final CellList<SessionProxy> sessionsCList;
	private final SimplePager			 sessionsPager;
	private ListDataProvider<SessionProxy> sessionsDP;
		
	private Presenter presenter;

	final LLConstants constants;

	private StylesCssResource css;

	@Inject
	public StudentHomeViewImpl(LLConstants constants, AppResources resources) {
		this.constants = constants;
		this.css = resources.css();
		initWidget(uiBinder.createAndBindUi(this));
		
		{	// PAIRED STUDENTS
			SessionInviteCell sessionInviteCell = new SessionInviteCell();
			sessionInvitesCList = new CellList<PartnerInviteProxy>( sessionInviteCell );
			sessionInvitesCList.setEmptyListWidget( new HTML(constants.noPendingSessionInvitesFound() /*NO_PENDING_SESSION_INVITES_FOUND*/) );
			final SingleSelectionModel<PartnerInviteProxy> sessionInviteSelectionModel = new SingleSelectionModel<PartnerInviteProxy>();
			sessionInvitesCList.setSelectionModel( sessionInviteSelectionModel);
			sessionInviteSelectionModel.addSelectionChangeHandler( new SessionInviteSelectionHandler( sessionInviteSelectionModel));
			sessionInvitesPager = new SimplePager();

			sessionInvitesPager.setDisplay(sessionInvitesCList);
		}
		{	// SESSIONS
			SessionCell sessionCell = new SessionCell();
			sessionsCList = new CellList< SessionProxy >( sessionCell );
			sessionsCList.setEmptyListWidget( new HTML(constants.noUnfinishedSessionsFound() /*NO_UNFINISHED_SESSIONS_FOUND*/) );
			final SingleSelectionModel< SessionProxy > sessionSelectionModel = new SingleSelectionModel<SessionProxy>();
			sessionsCList.setSelectionModel(sessionSelectionModel);
			sessionSelectionModel.addSelectionChangeHandler( new SessionSelectionHandler(sessionSelectionModel));
			sessionsPager = new SimplePager();

			sessionsPager.setDisplay(sessionsCList);
		}
		
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.priv.student.StudentHomeView#clear()
	 */
	public void clear() {
		coursesPanel.clear();
		sessionInvitesPanel.clear();
		sessionsPanel.clear();		
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.priv.student.StudentHomeView#setPresenter(ca.jhosek.linguaelive.ui.priv.student.StudentHomeView.Presenter)
	 */
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	/**
	 * 
	 * display a single linked student 
	 * @author copyright (C) 2011 Andrew Stevko
	 *
	 */
	private class SessionInviteCell extends AbstractCell<PartnerInviteProxy> {

		
		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				PartnerInviteProxy sessionInvite, SafeHtmlBuilder sb) {
			//
			if ( sessionInvite == null ) return;
			sb.appendHtmlConstant(LIST_PROMPT_DIV_W_STYLE );
//			if (sessionInvite.getStartDateTime() != null ) {
////				final String inviteFor = "Invite for ";
//				sb.append( SafeHtmlUtils.fromSafeConstant( constants.inviteFor() ));
////				sb.append( SafeHtmlUtils.fromString( sessionInvite.getStartDateTime().toString()));
//				sb.append( SafeHtmlUtils.fromSafeConstant( "<br />"  ));
//			} else {
//				final String invite = "Invite ";
				sb.append( SafeHtmlUtils.fromSafeConstant( constants.invite() ));
//			}
//			final String from = " From: ";
			sb.append( SafeHtmlUtils.fromSafeConstant( constants.from() ));
			sb.append( SafeHtmlUtils.fromString( sessionInvite.getMember1().getUser().getFirstName()  ));
			sb.append( SafeHtmlUtils.fromSafeConstant( "&nbsp;"  ));
			sb.append( SafeHtmlUtils.fromString( sessionInvite.getMember1().getUser().getLastName()  ));
//			final String to = " To: ";
			sb.append( SafeHtmlUtils.fromSafeConstant( constants.to() ));
			sb.append( SafeHtmlUtils.fromString( sessionInvite.getMember2().getUser().getFirstName()   ));
			sb.append( SafeHtmlUtils.fromSafeConstant( "&nbsp;"  ));
			sb.append( SafeHtmlUtils.fromString( sessionInvite.getMember2().getUser().getLastName()  ));
			sb.append( SafeHtmlUtils.fromSafeConstant( "&nbsp;&mdash;&nbsp;<i>"  ));
			if( sessionInvite.getPending() ){
//				final String invitePending = "Pending";
				sb.append( SafeHtmlUtils.fromSafeConstant( constants.invitePending() ));
				
			} else if ( sessionInvite.getAccepted() ){
//				final String inviteAccepted = "Accepted";
				sb.append( SafeHtmlUtils.fromSafeConstant( constants.inviteAccepted()  ));				
				
			} else {
//				final String inviteRejected = "Rejected";
				sb.append( SafeHtmlUtils.fromSafeConstant( constants.inviteRejected()  ));				
				
			}
			sb.append( SafeHtmlUtils.fromSafeConstant( "</i>"  ));
			sb.appendHtmlConstant("</div>" );						
		}

	}

	/**
	 * 
	 * display a single session
	 * @author copyright (C) 2011 Andrew Stevko
	 *
	 */
	private class SessionCell extends AbstractCell<SessionProxy> {

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				SessionProxy session, SafeHtmlBuilder sb) {
			//
			if ( session == null ) return;
			sb.appendHtmlConstant(LIST_PROMPT_DIV_W_STYLE );
			final UserProxy currentUser = presenter.getLoggedInUser();
			UserProxy otherUser;
			if (session.getMember1().getUser().getId().equals(currentUser.getId())) {
				otherUser = session.getMember2().getUser();
			} else {
				otherUser = session.getMember1().getUser();
			}
			sb.append( SafeHtmlUtils.fromString(otherUser.getFirstName()));
			sb.append( SafeHtmlUtils.fromSafeConstant( "&nbsp;") );
			sb.append( SafeHtmlUtils.fromString(otherUser.getLastName()));
			sb.append( SafeHtmlUtils.fromSafeConstant( "&nbsp;&nbsp;") );
			sb.append( SafeHtmlUtils.fromSafeConstant( session.getSessionLanguage().name()  ));
			sb.append( SafeHtmlUtils.fromSafeConstant( "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;") );
			if( session.getStartTime() == null  ) {
				// no start time
				sb.append( SafeHtmlUtils.fromSafeConstant( constants.sessionNotStarted()  ));

			} else if ( session.getStopTime() == null ){
				// start time but no stop time
				sb.append( SafeHtmlUtils.fromSafeConstant( constants.sessionInProgress()  ));				

			} else {
				sb.append( SafeHtmlUtils.fromSafeConstant( session.getDurationMinutes().toString() ));
				sb.append( SafeHtmlUtils.fromSafeConstant( constants.minutes() ));
			}			sb.appendHtmlConstant("</div>" );						
		}

	}

	/**
	 * what happens when a session invite is selected
	 * @author copyright (C) 2011 Andrew Stevko
	 *
	 */
	private final class SessionInviteSelectionHandler implements
	SelectionChangeEvent.Handler {
		private final SingleSelectionModel<PartnerInviteProxy> selectionModel;

		private SessionInviteSelectionHandler(
				SingleSelectionModel<PartnerInviteProxy> selectionModel) {
			this.selectionModel = selectionModel;
		}

		public void onSelectionChange(SelectionChangeEvent event) {
			// 
			PartnerInviteProxy sessionInvite = selectionModel.getSelectedObject();
			if( sessionInvite != null ) {
				// navigate to session invite page
				presenter.goToPartnerInvite( sessionInvite );
			}
		}
	}

	/**
	 * what happens when a session is selected
	 * @author copyright (C) 2011 Andrew Stevko
	 *
	 */
	private final class SessionSelectionHandler implements
	SelectionChangeEvent.Handler {
		private final SingleSelectionModel<SessionProxy> selectionModel;

		private SessionSelectionHandler(
				SingleSelectionModel<SessionProxy> selectionModel) {
			this.selectionModel = selectionModel;
		}

		public void onSelectionChange(SelectionChangeEvent event) {
			// 
			SessionProxy session = selectionModel.getSelectedObject();
			if( session != null ) {
				// show dialog prompting user to confirm linking to course
				logger.info("session selected; id=" + session.getId()	);
				presenter.goToSession(session);
			}

		}
	}

	public void showSessionInvites(List<PartnerInviteProxy> sessionInvites) {
		//
		sessionInvitesPanel.clear();
		final int displaySize = 15;

		if ( sessionInvites == null ) {
			logger.info("Loading partner invites from the server");
			sessionInvitesPanel.add( new HTML( constants.loadingPartnerInvites()));

		} else {
			logger.info( "showSessionInvites() called with " + sessionInvites.size() + " SessionInvites ");

			this.sessionInvitesDP = new ListDataProvider<PartnerInviteProxy>(sessionInvites);
			this.sessionInvitesDP.addDataDisplay(sessionInvitesCList);

			sessionInvitesCList.setRowCount( sessionInvites.size());
			sessionInvitesCList.setVisibleRange(0, displaySize);
			sessionInvitesCList.setRowData(0, sessionInvites);

			sessionInvitesPanel.add( sessionInvitesCList );			
			if ( sessionInvites.size() > displaySize ) {
				sessionInvitesPanel.add( sessionInvitesPager );
			}
		}		
	}

	public void showSessions(List<SessionProxy> sessions) {
		//
		sessionsPanel.clear();
		final int displaySize = 10;

		if ( sessions == null ) {
			logger.info("Loading sessions from the server");
			sessionsPanel.add( new HTML( constants.loadingSessions() ));

		} else {
			logger.info( "showSessions() called with " + sessions.size() + " Sessions");

			sessionsDP = new ListDataProvider<SessionProxy>(sessions);
			sessionsDP.addDataDisplay(sessionsCList);

			sessionsCList.setRowCount( sessions.size());
			sessionsCList.setVisibleRange(0, displaySize);
			sessionsCList.setRowData(0, sessions);

			sessionsPanel.add(sessionsCList);
			if ( sessions.size() > displaySize ) {
				sessionsPanel.add( sessionsPager );
			}
		}		
	}

	/**
	 * clear course list and show new
	 * @see ca.jhosek.linguaelive.ui.priv.instructor.InstructorHomeView#showCourseList(java.util.List)
	 */
	@Override
	public void showCourseList(List<CourseProxy> courses) {
		// clear list of courses
		coursesPanel.clear();
		// for every course
		for (CourseProxy course : courses) {
			// paint a course button
			CourseButton button = new CourseButton(course);
			coursesPanel.add(button);
		}
		// add courses panel is always there
		coursesPanel.add(new AddCourseButton());
		
	}
	
	/**
	 * Base button consists of a DecoratorPanel, FocusPanel, and a VerticalPanel
	 *  
	 * @author andy
	 *
	 */
	abstract class CourseBaseButton extends Composite {
		// track click handlers
		GroupingHandlerRegistration regs = new GroupingHandlerRegistration();
		
		VerticalPanel vPanel;

		FocusPanel fPanel;
		
		public CourseBaseButton() {
			
			DecoratorPanel dPanel = new DecoratorPanel();
			dPanel.addStyleName(css.courseBaseButton());
			fPanel = new FocusPanel();
			dPanel.setWidget(fPanel);
			vPanel = new VerticalPanel();
			fPanel.setWidget(vPanel);

			// register composite 
			initWidget(dPanel);
		}
	}
	
	/**
	 * course button
	 * @author andy
	 *
	 */
	class CourseButton extends CourseBaseButton {
		public CourseButton(final CourseProxy course) {
			super();
			// populate panels
			String name = course.getName();
			if (name.length() > 13) {
				name = name.substring(0, 11) + "...";
			}
			vPanel.add(new Label(name));
			vPanel.add(new Label(mdyDateFormat.format(course.getStartDate())));
			vPanel.add(new Label(mdyDateFormat.format(course.getEndDate())));
			
			
			regs.add( fPanel.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					presenter.viewCourse(course);
				}
			}));
		}		
	}
	/**
	 * add a course button
	 * @author andy
	 *
	 */
	class AddCourseButton extends CourseBaseButton {
		public AddCourseButton() {
			super();
			vPanel.add(new Label(constants.addANewCourse().substring(0, 5)));
			vPanel.add(new Label(constants.addANewCourse().substring(6)));
			vPanel.add(new HTML("&nbsp;"));
			
			regs.add( fPanel.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					presenter.goToAddCourse();
				}
			}));
		}
	}
}
