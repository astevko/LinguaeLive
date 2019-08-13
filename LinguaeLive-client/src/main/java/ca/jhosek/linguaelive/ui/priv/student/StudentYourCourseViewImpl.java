/**
 * 
 */
package ca.jhosek.linguaelive.ui.priv.student;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DateLabel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.NumberLabel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

import ca.jhosek.linguaelive.LLConstants;
import ca.jhosek.linguaelive.activity.mainregion.StudentYourCourseActivity;
import ca.jhosek.linguaelive.domain.CurrentState;
import ca.jhosek.linguaelive.place.StudentYourCoursePlace;
import ca.jhosek.linguaelive.ui.EnumRenderer;
import ca.jhosek.linguaelive.widgets.EnumInlineLabel;
import ca.jhosek.linguaelive.widgets.EnumLabel;
// import ca.jhosek.linguaelive.email.SendEmail;
import ca.jhosek.linguaelive.DayOfWeekEnum;
import ca.jhosek.linguaelive.LanguageType;
import ca.jhosek.linguaelive.proxy.CourseProxy;
import ca.jhosek.linguaelive.proxy.MemberProxy;
import ca.jhosek.linguaelive.proxy.PartnerInviteProxy;
import ca.jhosek.linguaelive.proxy.UserProxy;
//import ca.jhosek.linguaelive.proxy.SessionProxy;

/**
 * view a course from the Student's point of view
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see StudentYourCourseView
 * @see StudentYourCourseActivity
 * @see StudentYourCoursePlace
 * 
 */
public class StudentYourCourseViewImpl extends Composite implements IsWidget, StudentYourCourseView, Editor<MemberProxy> {

//	private static final String I_NO_SESSIONS_HAVE_BEEN_CREATED_FOR_THIS_COURSE_PLEASE_SELECT_AN_SESSION_INVITE_ABOVE_TO_INITIATE_A_SESSION_I = "<i>No sessions have been created for this course. Please select an partner invite above to initiate a session.</i>";

//	private static final String I_NO_SESSION_INVITES_FOUND_FOR_THIS_COURSE_PLEASE_SELECT_AN_AVAILABLE_STUDENT_ABOVE_TO_INVITE_I = "<i>No partner invites found for this course. Please select an available student above to invite.</i>";
//
//	private static final String NO_OTHER_STUDENTS_HAVE_COMPLEMENTARY_AVAILABILITY_SLOTS_TRY_CHANGING_YOUR_AVAILABILITY = "<i>No other students have complementary availability slots. Try changing your availability.</i>";
//
//	private static final String NO_SCHEDULE_AVAILABILITY_INFORMATION_RECORDED = "<i>No schedule availability information recorded.</i>";
//
//	private static final String HOUR_COL_HEADER = "Hour";

	private static final String LIST_PROMPT_DIV_W_STYLE = "<div style=\"font-weight: bold;cursor: pointer;color: #d35e46;\" >";
	
	public interface Driver extends RequestFactoryEditorDriver<MemberProxy, StudentYourCourseViewImpl> {

	}

	// logger boilerplate
	private static final Logger logger = Logger.getLogger(StudentYourCourseViewImpl.class
			.getName());

	static EnumRenderer<LanguageType> languageTypeRenderer = new EnumRenderer<LanguageType>();


	private static InstructorCourseViewImplUiBinder uiBinder = GWT
	.create(InstructorCourseViewImplUiBinder.class);

	interface InstructorCourseViewImplUiBinder extends
	UiBinder<Widget, StudentYourCourseViewImpl> {
	}

	//------------------
	// course fields
	@Path( "course.name" )
	@UiField InlineLabel name;
	@Path( "course.description" )
	@UiField Label description;
	@Path( "course.startDate" )
	@UiField DateLabel startDate;
	@Path( "course.endDate" )
	@UiField DateLabel endDate;
	@Path( "course.targetLanguage" )
	@UiField EnumLabel<LanguageType> targetLanguage;  
	@Path( "course.expertLanguage" )
	@UiField EnumLabel<LanguageType> expertLanguage;
	
	@Path( "course.targetLanguage" )
	@UiField EnumInlineLabel<LanguageType> targetLanguagePrompt;
	@Path( "course.targetLanguage" )
	@UiField  EnumInlineLabel<LanguageType> targetSessionMinutesPrompt;
	@Path( "course.expertLanguage" )
	@UiField  EnumInlineLabel<LanguageType> expertSessionsPrompt;
	@Path( "course.expertLanguage" )
	@UiField  EnumInlineLabel<LanguageType> expertSessionMinutesPrompt;
	

	// user fields
	@Path( "user.userTimeZone")
	@UiField InlineLabel userTimeZone;
	
	// member fields
	@UiField NumberLabel<Integer> scheduleSize;
	@UiField CheckBox available;
	@UiField NumberLabel<Long> totalSessionMinutes;
	@UiField NumberLabel<Long> totalSessions;
	@UiField NumberLabel<Long> targetSessionMinutes;
	@UiField NumberLabel<Long> targetSessions;
	@UiField NumberLabel<Long> expertSessionMinutes;
	@UiField NumberLabel<Long> expertSessions;

	/**
	 * layout panel containing the following
	 */
	@UiField StackLayoutPanel stacklayoutpanel;
	//------------------
	// student schedule
	@UiField FlowPanel schedulePanel;
	private final CellTable<HourOfDay>   scheduleCTable;
	private final SimplePager 			schedulePager;
	private ListDataProvider<HourOfDay> scheduleDP;
	@Ignore Label scheduleMessageLabel;

	//------------------
	// available students
	@UiField Button refreshAvailableStudentsPanel;
	
	@UiField FlowPanel availableStudentsPanel;
	private final CellList<MemberProxy>   availableStudentsCList;
	private final SimplePager 			availableStudentsPager;
	private ListDataProvider<MemberProxy> availableStudentsDP;
	@Ignore Label availableStudentsLabel;

	//------------------
	// session invitations
	@UiField FlowPanel sessionInvitesPanel;
	private final CellList<PartnerInviteProxy> sessionInvitesCList;
	private final SimplePager 		  sessionInvitesPager;
	private ListDataProvider<PartnerInviteProxy> sessionInvitesDP;

	
	//------------------
	// sessions
//	@UiField FlowPanel sessionsPanel;
//	private final CellList<SessionProxy> sessionsCList;
//	private final SimplePager			 sessionsPager;
//	private ListDataProvider<SessionProxy> sessionsDP;

	@UiField Button sessionReportButton;


	/**
	 * delete this course Membership
	 */
	@UiField Button deleteMember;
	
	private Presenter presenter;

	private static final int DISPLAY_SIZE = 10;

	private final LLConstants constants;

	@Inject
	public StudentYourCourseViewImpl(LLConstants constants, CurrentState currentState) {
		this.constants = constants;
		// initialize language widget values
		initWidget(uiBinder.createAndBindUi(this));

		{	// MEMBER SCHEDULE
			scheduleCTable = new CellTable<HourOfDay>( );
			scheduleMessageLabel = new Label(constants.noScheduleAvailabilityInformationRecorded());
			scheduleCTable.setEmptyTableWidget( scheduleMessageLabel  );
			schedulePager = new SimplePager();			
			schedulePager.setDisplay(scheduleCTable);

			// add columns with ClickableTextCell.
			addColumn(new ClickableTextCell(), null );
			addColumn(new ClickableTextCell(), DayOfWeekEnum.MON );
			addColumn(new ClickableTextCell(), DayOfWeekEnum.TUE );
			addColumn(new ClickableTextCell(), DayOfWeekEnum.WED );
			addColumn(new ClickableTextCell(), DayOfWeekEnum.THU );
			addColumn(new ClickableTextCell(), DayOfWeekEnum.FRI );
			addColumn(new ClickableTextCell(), DayOfWeekEnum.SAT );
			addColumn(new ClickableTextCell(), DayOfWeekEnum.SUN );
		}
		{	// AVAILABLE STUDENTS
			AvailableStudentCell availableStudentCell = new AvailableStudentCell();
			availableStudentsCList = new CellList<MemberProxy>( availableStudentCell );
			availableStudentsLabel = new HTML(constants.noOtherStudentsHaveComplementaryAvailability());
			availableStudentsCList.setEmptyListWidget( availableStudentsLabel );

			final SingleSelectionModel<MemberProxy> availableStudentSelectionModel = new SingleSelectionModel<MemberProxy>();
			availableStudentsCList.setSelectionModel(availableStudentSelectionModel);
			availableStudentSelectionModel.addSelectionChangeHandler( new AvailableStudentSelectionHandler(availableStudentSelectionModel));
			availableStudentsPager = new SimplePager();

			availableStudentsPager.setDisplay(availableStudentsCList);
		}
		{	// PAIRED STUDENTS
			SessionInviteCell sessionInviteCell = new SessionInviteCell(constants, currentState );	// needs constants
			sessionInvitesCList = new CellList<PartnerInviteProxy>( sessionInviteCell );
			sessionInvitesCList.setEmptyListWidget( new HTML(constants.noSessionInvitesFoundForThisCourse()) );
			final SingleSelectionModel<PartnerInviteProxy> sessionInviteSelectionModel = new SingleSelectionModel<PartnerInviteProxy>();
			sessionInvitesCList.setSelectionModel( sessionInviteSelectionModel);
			sessionInviteSelectionModel.addSelectionChangeHandler( new SessionInviteSelectionHandler( sessionInviteSelectionModel));
			sessionInvitesPager = new SimplePager();

			sessionInvitesPager.setDisplay(sessionInvitesCList);
		}
//		{	// SESSIONS
//			SessionCell sessionCell = new SessionCell();
//			sessionsCList = new CellList< SessionProxy >( sessionCell );
//			sessionsCList.setEmptyListWidget( new HTML(I_NO_SESSIONS_HAVE_BEEN_CREATED_FOR_THIS_COURSE_PLEASE_SELECT_AN_SESSION_INVITE_ABOVE_TO_INITIATE_A_SESSION_I) );
//			final SingleSelectionModel< SessionProxy > sessionSelectionModel = new SingleSelectionModel<SessionProxy>();
//			sessionsCList.setSelectionModel(sessionSelectionModel);
//			sessionSelectionModel.addSelectionChangeHandler( new SessionSelectionHandler(sessionSelectionModel));
//			sessionsPager = new SimplePager();
//
//			sessionsPager.setDisplay(sessionsCList);
//		}
	}



	private class ScheduleUpdater implements FieldUpdater<HourOfDay, String> {

		private final DayOfWeekEnum dow;
		
		/**
		 * @param dow
		 */
		public ScheduleUpdater(DayOfWeekEnum dow) {
			super();
			this.dow = dow;
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.cell.client.FieldUpdater#update(int, java.lang.Object, java.lang.Object)
		 */
		public void update(int index, HourOfDay hourOfDay, String value) {
			Integer hourOfWeek = hourOfDay.getHourOfWeek( dow );
			if ( hourOfDay.toggle( dow ) ) {
				// add to schedule
				presenter.addSchedule(hourOfWeek);
			} else {
				// remove from schedule
				presenter.dropSchedule(hourOfWeek);
			}
			scheduleCTable.redraw();
		}
	}

	/**
	 * adds a column to the table wit these parameters
	 * 
	 * @param cell
	 * @param headerText
	 * @param getter
	 * @param fieldUpdater
	 * @return
	 */
	private Column<HourOfDay, String> addColumn(
			Cell<String> cell,
			final DayOfWeekEnum dow
	) {
		Column<HourOfDay, String> column = new Column<HourOfDay, String>(cell) {
			@Override
			public String getValue(HourOfDay hourOfDay) {
				return hourOfDay.getValue( dow );
			}
		};
		if (dow == null ) {
			// first column
			scheduleCTable.addColumn( column, constants.hour() );
			
		} else {
			column.setFieldUpdater( new ScheduleUpdater( dow ) );
			scheduleCTable.addColumn( column, dow.getD() );
		}
		return column;
	}

	/**
	 * @return a MemberProxy editor driver, userProxyDriver, tied to this presenter and view
	 */
	public Driver createEditorDriver(EventBus eventBus, RequestFactory requestFactory) {
		// 
		logger.info( "createEditorDriver( InstructorCourseViewImpl )");
		Driver driver = GWT.create(Driver.class);
		driver.initialize( eventBus, requestFactory, this );
		return driver;
	}


	public void setPresenter(Presenter presenter) {
		// 
		this.presenter = presenter;
	}

	/**
	 * 
	 * display a single unpaired student 
	 * @author copyright (C) 2011 Andrew Stevko
	 *
	 */
	private static class AvailableStudentCell extends AbstractCell<MemberProxy> {

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				MemberProxy availableMember, SafeHtmlBuilder sb) {
			//
			if ( availableMember == null ) return;
			sb.appendHtmlConstant(LIST_PROMPT_DIV_W_STYLE );
			sb.append( SafeHtmlUtils.fromString( availableMember.getUser().getFirstName()  ));
			sb.append( ' ' );			
			sb.append( SafeHtmlUtils.fromString( availableMember.getUser().getLastName()  ));
			sb.append( '-' );			
			sb.append( SafeHtmlUtils.fromString( availableMember.getUser().getSchool()  ));
			sb.appendHtmlConstant("</div>" );						
		}

	}

	/**
	 * 
	 * display a single linked student 
	 * @author copyright (C) 2011 Andrew Stevko
	 *
	 */
	private static class SessionInviteCell extends AbstractCell<PartnerInviteProxy> {

		private final CurrentState currentState;

		public SessionInviteCell(LLConstants constants, CurrentState currentState) {
			this.constants = constants;
			this.currentState = currentState;
		}
		final LLConstants constants;
		
		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				PartnerInviteProxy sessionInvite, SafeHtmlBuilder sb) {
			//
			if ( sessionInvite == null ) return;
			sb.appendHtmlConstant(LIST_PROMPT_DIV_W_STYLE );
//			if (sessionInvite.getStartDateTime() != null ) {
//				sb.append( SafeHtmlUtils.fromSafeConstant( "Invite for " ));
//				sb.append( SafeHtmlUtils.fromString( sessionInvite.getStartDateTime().toString()));
//				sb.append( SafeHtmlUtils.fromSafeConstant( "<br />"  ));
//			} else {
//				sb.append( SafeHtmlUtils.fromSafeConstant( "Invite " ));
//			}
//			sb.append( SafeHtmlUtils.fromSafeConstant( "<br />"  ));
			// filter out current user portion
			final UserProxy user1 = sessionInvite.getMember1().getUser();
			if (user1.getId().equals(currentState.getLoggedInUser().getId())) {
//				sb.append( SafeHtmlUtils.fromSafeConstant( constants.from() ));
				sb.append( SafeHtmlUtils.fromString( user1.getFirstName()  ));
				sb.append( SafeHtmlUtils.fromSafeConstant( "&nbsp;"  ));
				sb.append( SafeHtmlUtils.fromString( user1.getLastName()  ));
				sb.append( SafeHtmlUtils.fromSafeConstant( "&nbsp;"  ));
			}
			final UserProxy user2 = sessionInvite.getMember2().getUser();
			if (user2.getId().equals(currentState.getLoggedInUser().getId())) {
//				sb.append( SafeHtmlUtils.fromSafeConstant( constants.to() ));
				sb.append( SafeHtmlUtils.fromString( user2.getFirstName()   ));
				sb.append( SafeHtmlUtils.fromSafeConstant( "&nbsp;"  ));
				sb.append( SafeHtmlUtils.fromString( user2.getLastName()  ));
			}
			sb.append( SafeHtmlUtils.fromSafeConstant( "&nbsp;&mdash;&nbsp;<i>"  ));
			if( sessionInvite.getPending() ){
				sb.append( SafeHtmlUtils.fromSafeConstant( constants.invitePending()  ));
				
			} else if ( sessionInvite.getAccepted() ){
				sb.append( SafeHtmlUtils.fromSafeConstant( constants.inviteAccepted() ));				
				
			} else {
				sb.append( SafeHtmlUtils.fromSafeConstant( constants.inviteRejected()  ));				
				
			}
			sb.append( SafeHtmlUtils.fromSafeConstant( "</i>"  ));
			sb.appendHtmlConstant("</div>" );						
		}

	}

//	/**
//	 * 
//	 * display a single session
//	 * @author copyright (C) 2011 Andrew Stevko
//	 *
//	 */
//	private static class SessionCell extends AbstractCell<SessionProxy> {
//
//		@Override
//		public void render(com.google.gwt.cell.client.Cell.Context context,
//				SessionProxy session, SafeHtmlBuilder sb) {
//			//
//			if ( session == null ) return;
//			sb.appendHtmlConstant(LIST_PROMPT_DIV_W_STYLE );
//			sb.append( SafeHtmlUtils.fromSafeConstant( session.getSessionLanguage().name()  ));
//			sb.append( SafeHtmlUtils.fromSafeConstant( "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;") );
//			if( session.getStartTime() == null  ) {
//				// no start time
//				sb.append( SafeHtmlUtils.fromSafeConstant( "<i>NOT YET STARTED</i>"  ));
//
//			} else if ( session.getStopTime() == null ){
//				// start time but no stop time
//				sb.append( SafeHtmlUtils.fromSafeConstant( "<i>&gt;&gt;&gt; IN PROGRESS &lt;&lt;&lt;</i>"  ));				
//
//			} else {
//				sb.append( SafeHtmlUtils.fromSafeConstant( session.getDurationMinutes().toString() ));
//				sb.append( SafeHtmlUtils.fromSafeConstant( " minutes" ));
//			}
//			sb.appendHtmlConstant("</div>" );						
//		}
//
//	}

	/**
	 * what happens when the UNpaired student is selected
	 * @author copyright (C) 2011 Andrew Stevko
	 *
	 */
	private final class AvailableStudentSelectionHandler implements
	SelectionChangeEvent.Handler {
		private final SingleSelectionModel<MemberProxy> selectionModel;

		private AvailableStudentSelectionHandler(
				SingleSelectionModel<MemberProxy> selectionModel) {
			this.selectionModel = selectionModel;
		}

		public void onSelectionChange(SelectionChangeEvent event) {
			// 
			MemberProxy unpairedStudent = selectionModel.getSelectedObject();
			if( unpairedStudent != null ) {
				// create a default message
				final StringBuilder msg = new StringBuilder();
				msg.append(replaceTokens( constants.studentPartnerInviteDefaultGreeting(), unpairedStudent.getUser()));
				final MemberProxy myMember = presenter.getMember();
				String body = replaceTokens( constants.studentPartnerInviteDefaultBody(), myMember.getCourse() );
				msg.append(replaceTokens( body, myMember.getUser()));
				
				
				// show dialog prompting user to confirm sending the invite
				PartnerInviteDialog dialogBox = new PartnerInviteDialog(constants);
				dialogBox.setPersonalMessage(msg.toString());
				dialogBox.show(presenter, unpairedStudent);
			}
		}
	}

	/**
	 * @see SendEmail for details
	 * 
	 * @param template
	 * @param course
	 * @return processed template
	 */
	protected String replaceTokens(String template, CourseProxy course) {
		String courseLink = "https://linguaelive.appspot.com/#studentyourcourse:" + course.getId().toString();
		return( template
				.replace( "<description>" , course.getDescription() ) 
				.replace( "<end_date>" , course.getEndDate().toString() )
				.replace( "<est_size>" , course.getEstimatedMemberSize().toString() )
				.replace( "<expert_language>" , course.getExpertLanguage().name() )
				.replace( "<target_language>" , course.getTargetLanguage().name() ) 
				.replace( "<start_date>", course.getStartDate().toString() )
				.replace( "<course_name>" , course.getName() ) )
				.replace( "<course_link>", courseLink )
				;
	}
	/**
	 * @see SendEmail for details
	 * 
	 * @param template
	 * @param user
	 * @return modified template
	 */
	protected String replaceTokens(String template, UserProxy user) {
		return( template
			.replace( "<first_name>", user.getFirstName()  )
			.replace( "<last_name>", user.getLastName())
			.replace( "<school>", user.getSchool())
			.replace( "<email_address>", user.getEmailAddress() ) )
			.replace( "<hint>", user.getHint() )
			;
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
				presenter.goToSessionInvite( sessionInvite );
			}
		}
	}

//	/**
//	 * what happens when a session is selected
//	 * @author copyright (C) 2011 Andrew Stevko
//	 *
//	 */
//	private final class SessionSelectionHandler implements
//	SelectionChangeEvent.Handler {
//		private final SingleSelectionModel<SessionProxy> selectionModel;
//
//		private SessionSelectionHandler(
//				SingleSelectionModel<SessionProxy> selectionModel) {
//			this.selectionModel = selectionModel;
//		}
//
//		@Override
//		public void onSelectionChange(SelectionChangeEvent event) {
//			// 
//			SessionProxy session = selectionModel.getSelectedObject();
//			if( session != null ) {
//				// show dialog prompting user to confirm linking to course
//				logger.info("session selected; id=" + session.getId()	);
//				presenter.goToSession(session);
//			}
//
//		}
//	}


	public void showAvailableStudents( List<MemberProxy> availableStudents) {
		//
		availableStudentsPanel.clear();

		if ( availableStudents == null ) {
			logger.info("unavailable for partner invites");
//			final String pleaseChangeYourAvailability = "Please change your availability for partner invites above.";
			availableStudentsPanel.add( new HTML( constants.pleaseChangeYourAvailability() ));

		} else {
			logger.info( "showAvailableStudents() called with " + availableStudents.size() + " Members");

			this.availableStudentsDP = new ListDataProvider<MemberProxy>(availableStudents);
			this.availableStudentsDP.addDataDisplay(availableStudentsCList);

			availableStudentsCList.setRowCount( availableStudents.size());
			availableStudentsCList.setVisibleRange(0, DISPLAY_SIZE);
			availableStudentsCList.setRowData(0, availableStudents);

			availableStudentsPanel.add(availableStudentsCList);
			if ( availableStudents.size() > DISPLAY_SIZE ) {
				availableStudentsPanel.add( availableStudentsPager );
			}
			// show the right panel
			showTheRightPanel();
		}

	}

	/**
	 * open the default panel
	 */
	private void showTheRightPanel() {
		if (sessionInvitesDP != null && !sessionInvitesDP.getList().isEmpty()){
			// show the invites
			stacklayoutpanel.showWidget(2);		
		}
		else if (availableStudentsDP != null && !availableStudentsDP.getList().isEmpty()){
			// show students
			stacklayoutpanel.showWidget(1);
		} else {
			// show the availability panel
			stacklayoutpanel.showWidget(0);
		}
	}	

	public void showSessionInvites(List<PartnerInviteProxy> sessionInvites) {
		//
		sessionInvitesPanel.clear();

		if ( sessionInvites == null ) {
			logger.info("Loading partner invites from the server");
			sessionInvitesPanel.add( new HTML(constants.loadingPartnerInvites()));

		} else {
			logger.info( "showSessionInvites() called with " + sessionInvites.size() + " SessionInvites ");
			// sort invites to show accepted | open | declined
			java.util.Collections.sort(sessionInvites, getPartnerInviteSort());
			
			this.sessionInvitesDP = new ListDataProvider<PartnerInviteProxy>(sessionInvites);
			this.sessionInvitesDP.addDataDisplay(sessionInvitesCList);

			sessionInvitesCList.setRowCount( sessionInvites.size());
			sessionInvitesCList.setVisibleRange(0, DISPLAY_SIZE);
			sessionInvitesCList.setRowData(0, sessionInvites);

			sessionInvitesPanel.add( sessionInvitesCList );			
			if ( sessionInvites.size() > DISPLAY_SIZE ) {
				sessionInvitesPanel.add( sessionInvitesPager );
			}
			// show this panel
			showTheRightPanel();
		}		
	}

	/**
	 * sort PartnerInviteProxy
	 * 
	 * ACCEPTED | OPEN | DECLINED
	 * 
	 * @return +2 ... -2
	 */
	private Comparator<PartnerInviteProxy> getPartnerInviteSort() {
		return new Comparator<PartnerInviteProxy>() {

			@Override
			public int compare(PartnerInviteProxy o1, PartnerInviteProxy o2) {					
				if (o1.getAccepted() == o2.getAccepted() || (o1.getPending() && o2.getPending())) {
					// both accepted/declined or both pending = equivalent
					return 0;
					
				} else if (o1.getAccepted()) {						
					// o1 is accepted and o2 is not accepted
					if (o2.getPending()) {
						// o1 accepted & o2 pending
						return 1;
					} else {
						// o2 not pending, not accepted, = declined
						return 2;
					}
				} else if (o1.getPending()) {
					// o1 not accepted, o1 is pending
					if (o2.getAccepted()) {
						return -1;
					} else {
						// o2 not accepted, not pending = declined
						return 1;
					}
				} else {
					// o1 not accepted, not pending = declined
					if (o2.getPending()) {
						// o1 declined, o2 pending
						return -1;
					} else {
						// o1 declined, o2 accepted
						return -2;
					}
				}
			}
		};
	}

//	@Override
//	public void showSessions(List<SessionProxy> sessions) {
//		//
//		sessionsPanel.clear();
//		final int displaySize = 10;
//
//		if ( sessions == null ) {
//			logger.info("Loading sessions from the server");
//			sessionsPanel.add( new HTML( "Loading sessions from the server."));
//
//		} else {
//			logger.info( "showSessions() called with " + sessions.size() + " Sessions");
//
//			sessionsDP = new ListDataProvider<SessionProxy>(sessions);
//			sessionsDP.addDataDisplay(sessionsCList);
//
//			sessionsCList.setRowCount( sessions.size());
//			sessionsCList.setVisibleRange(0, displaySize);
//			sessionsCList.setRowData(0, sessions);
//
//			sessionsPanel.add(sessionsCList);
//			if ( sessions.size() > displaySize ) {
//				sessionsPanel.add( sessionsPager );
//			}
//		}		
//	}
	public void showSchedule( List<HourOfDay> scheduleHours ){

		schedulePanel.clear();
		final int displaySize = 8;

		if ( scheduleHours == null ) {
			logger.info( "loading schedule from the server");
//			final String loadingAvailabilitySchedule = "Loading availability schedule from the server.";
			schedulePanel.add( new HTML(constants.loadingAvailabilitySchedule()));
			
		} else {
			logger.info( "showSchedule() called with " + scheduleHours.size() + " hours. ");

			scheduleDP = new ListDataProvider<HourOfDay>( scheduleHours );
			scheduleDP.addDataDisplay(scheduleCTable);

			scheduleCTable.setRowCount( 24 );
			scheduleCTable.setVisibleRange(0, displaySize);
			scheduleCTable.setRowData(0, scheduleHours);

			schedulePanel.add(scheduleCTable);
			schedulePanel.add(schedulePager);
		}
		// show the right panel
		showTheRightPanel();
	}

	public void clear() {
		//
		availableStudentsPanel.clear();
		sessionInvitesPanel.clear();
//		sessionsPanel.clear();		
	}

	/**
	 * Refresh available students query
	 * 
	 * @param event
	 */
	@UiHandler("refreshAvailableStudentsPanel")
	void onRefreshAvailableStudentsButtonClick( ClickEvent event ){
		presenter.refreshAvailableStudents();
	}

	/**
	 * Available check box click
	 * 
	 * @param event
	 */
	@UiHandler("available")
	void onAvailableCheckBoxClick(ClickEvent event) {
		Boolean val = available.getValue();
		presenter.setAvailable( val );
		// toggle visibility of availability chart and matching students
		schedulePanel.setVisible(val);
		availableStudentsCList.setVisible(val);
	}

	@UiHandler("sessionReportButton")
	void onSessionReportButtonClick(ClickEvent event) {
		// open session report
		presenter.gotoSessionReport();
	}

}
