/**
 * 
 */
package ca.jhosek.main.client.ui.priv.instructor;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.cell.client.TextCell;
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
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DateLabel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.NumberLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

import ca.jhosek.main.client.LLConstants;
import ca.jhosek.main.client.activity.mainregion.InstructorMemberActivity;
import ca.jhosek.main.client.place.InstructorMemberPlace;
import ca.jhosek.main.client.ui.EnumRenderer;
import ca.jhosek.main.client.ui.priv.student.HourOfDay;
import ca.jhosek.main.shared.ContactInfoType;
import ca.jhosek.main.shared.DayOfWeekEnum;
import ca.jhosek.main.shared.LanguageType;
import ca.jhosek.main.shared.proxy.ContactInfoProxy;
import ca.jhosek.main.shared.proxy.MemberProxy;
import ca.jhosek.main.shared.proxy.PartnerInviteProxy;
import ca.jhosek.main.shared.proxy.SessionProxy;

/**
 * view a course from the Student's point of view
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see InstructorMemberView
 * @see InstructorMemberActivity
 * @see InstructorMemberPlace
 * 
 */
public class InstructorMemberViewImpl extends Composite implements IsWidget, InstructorMemberView, Editor<MemberProxy> {

	private static final String LIST_PROMPT_DIV_W_STYLE = "<div style=\"font-weight: bold;cursor: pointer;color: #d35e46;\" >";

//	private static final String NO_CONTACT_INFO = "No contact info found. Please click 'add' below.";
//
//
//	private static final String LOADING_CONTACT_INFO_PLEASE_WAIT_BRIEFLY = "Loading contact info. Please wait briefly...";
//
//	private static final String NO_SCHEDULE_AVAILABILITY_INFORMATION_RECORDED = "No schedule availability information recorded.";
//
//	private static final String HOUR_COL_HEADER = "Hour";
	
	private static final int displaySize = 15;
	
	public interface Driver extends RequestFactoryEditorDriver<MemberProxy, InstructorMemberViewImpl> {

	}

	// logger boilerplate
	private static final Logger logger = Logger.getLogger(InstructorMemberViewImpl.class
			.getName());

	static EnumRenderer<LanguageType> languageTypeRenderer = new EnumRenderer<LanguageType>();


	private static InstructorCourseViewImplUiBinder uiBinder = GWT
	.create(InstructorCourseViewImplUiBinder.class);

	interface InstructorCourseViewImplUiBinder extends
	UiBinder<Widget, InstructorMemberViewImpl> {
	}

	//------------------
	@Path( "course.name" )
	@UiField InlineLabel name;

	// user fields
	@Path( "user.firstName")
	@UiField InlineLabel firstName;
	@Path( "user.lastName")
	@UiField InlineLabel lastName;
	@Path( "user.emailAddress")
	@UiField Label emailAddress;
	@Path( "user.currentUserTime")
	@UiField Label currentUserTime;
	@Path( "user.createDate")
	@UiField DateLabel createDate;	
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
	
	
	//------------------
	@UiField FlowPanel schedulePanel;
	private final CellTable<HourOfDay>   scheduleCTable;
	private final SimplePager 			schedulePager;
	private ListDataProvider<HourOfDay> scheduleDP;
	@Ignore Label scheduleMessageLabel;
	
	//------------------
	// available students
	@UiField FlowPanel availableStudentsPanel;
	private final CellList<MemberProxy>   availableStudentsCList;
	private final SimplePager 			availableStudentsPager;
	private ListDataProvider<MemberProxy> availableStudentsDP;
	
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

	// contact info list 
	@UiField HTMLPanel contactInfoPanel;
	private ListDataProvider<ContactInfoProxy> listContactInfoProvider;
	private final CellTable<ContactInfoProxy> contactInfoCTable;
	
	private Presenter presenter;

	private final LLConstants constants;

	@Inject
	public InstructorMemberViewImpl(LLConstants constants) {
		this.constants = constants;
		// initialize language widget values
		initWidget(uiBinder.createAndBindUi(this));
		{
			{
				//----------------------------
				// create contact info celltable, cells, selectionModel, Pager
				// INFO
				TextCell infoCell = new TextCell();
				Column<ContactInfoProxy, String> infoColumn = new Column<ContactInfoProxy, String>( infoCell ) {

					@Override
					public String getValue(ContactInfoProxy contactInfo) {
						// 
						return contactInfo.getInfo();
					}
				};
				//-------------
				// PREFERRED
				Column< ContactInfoProxy, Boolean> preferredColumn = new Column<ContactInfoProxy, Boolean>(new CheckboxCell(true, false)) {

					@Override
					public Boolean getValue(ContactInfoProxy contactInfo) {
						// 
						return contactInfo.getPreferred();
					}
				};
				//-------------
				// TYPES
				List<String> typeNames = new ArrayList<String>();
				for( ContactInfoType type : ContactInfoType.values() ) {
					typeNames.add(type.toString());
				}
				SelectionCell typeCell= new SelectionCell( typeNames );
				Column< ContactInfoProxy, String > typeColumn = new Column<ContactInfoProxy, String>( typeCell ) {

					@Override
					public String getValue(ContactInfoProxy contactInfo) {
						// 
						return contactInfo.getType().toString();
					}
				};
				//----------------------
				// build table 
				contactInfoCTable = new CellTable<ContactInfoProxy>();
				contactInfoCTable.addColumn( typeColumn, "Type" );
				contactInfoCTable.addColumn( infoColumn, "Number/Id" );
				contactInfoCTable.addColumn( preferredColumn, "Preferred" );

				contactInfoCTable.setEmptyTableWidget(new HTML( constants.noContactInfoFound() ));
				
		}
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
				availableStudentsCList.setEmptyListWidget( new Label("No other students have complementary availability slots. Try changing your availability or select show all.."));
				
	//			final SingleSelectionModel<MemberProxy> availableStudentSelectionModel = new SingleSelectionModel<MemberProxy>();
	//			availableStudentsCList.setSelectionModel(availableStudentSelectionModel);
	//			availableStudentSelectionModel.addSelectionChangeHandler( new AvailableStudentSelectionHandler(availableStudentSelectionModel));
				availableStudentsPager = new SimplePager();
				
				availableStudentsPager.setDisplay(availableStudentsCList);
			}
			{	// PAIRED STUDENTS
				SessionInviteCell sessionInviteCell = new SessionInviteCell();
				sessionInvitesCList = new CellList<PartnerInviteProxy>( sessionInviteCell );
				sessionInvitesCList.setEmptyListWidget( new Label("No partner invites found for this course.") );
				final SingleSelectionModel<PartnerInviteProxy> sessionInviteSelectionModel = new SingleSelectionModel<PartnerInviteProxy>();
				sessionInvitesCList.setSelectionModel( sessionInviteSelectionModel);
				sessionInviteSelectionModel.addSelectionChangeHandler( new SessionInviteSelectionHandler( sessionInviteSelectionModel));
				sessionInvitesPager = new SimplePager();
				
				sessionInvitesPager.setDisplay(sessionInvitesCList);
			}
			{	// SESSIONS
				SessionCell sessionCell = new SessionCell();
				sessionsCList = new CellList< SessionProxy >( sessionCell );
				sessionsCList.setEmptyListWidget( new Label("No sessions have been created for this course.") );
				final SingleSelectionModel< SessionProxy > sessionSelectionModel = new SingleSelectionModel<SessionProxy>();
				sessionsCList.setSelectionModel(sessionSelectionModel);
				sessionSelectionModel.addSelectionChangeHandler( new SessionSelectionHandler(sessionSelectionModel));
				sessionsPager = new SimplePager();
				
				sessionsPager.setDisplay(sessionsCList);
			}
		}
		// do not allow instructor to change value
		available.setEnabled(false);
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
		
		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				PartnerInviteProxy sessionInvite, SafeHtmlBuilder sb) {
			//
			if ( sessionInvite == null ) return;
			
			// display Status Blurb
			String statusBlurb;
			if (sessionInvite.getPending()) {
				statusBlurb = "Invite Pending";
			} else if (sessionInvite.getAccepted()) {
				statusBlurb = "Invite Accepted";
			} else {
				statusBlurb = "Invite Declined";
			}

			
			sb.appendHtmlConstant(LIST_PROMPT_DIV_W_STYLE );
//			sb.append( SafeHtmlUtils.fromSafeConstant( "Invite for " ));
//			sb.append( SafeHtmlUtils.fromString( sessionInvite.getStartDateTime().toString() ));
//			sb.append( SafeHtmlUtils.fromSafeConstant( "<br />"  ));
			sb.append( SafeHtmlUtils.fromSafeConstant( " From: " ));
			sb.append( SafeHtmlUtils.fromString( sessionInvite.getMember1().getUser().getFirstName()  ));
			sb.append( SafeHtmlUtils.fromSafeConstant( "&nbsp;"  ));
			sb.append( SafeHtmlUtils.fromString( sessionInvite.getMember1().getUser().getLastName()  ));
			sb.append( SafeHtmlUtils.fromSafeConstant( " To: " ));
			sb.append( SafeHtmlUtils.fromString( sessionInvite.getMember2().getUser().getFirstName()   ));
			sb.append( SafeHtmlUtils.fromSafeConstant( "&nbsp;"  ));
			sb.append( SafeHtmlUtils.fromString( sessionInvite.getMember2().getUser().getLastName()  ));
			sb.append( SafeHtmlUtils.fromSafeConstant( "&nbsp;"  ));
			sb.append( SafeHtmlUtils.fromSafeConstant(statusBlurb));
			sb.appendHtmlConstant("</div>" );						
		}

	}
	

	/**
	 * 
	 * display a single session
	 * @author copyright (C) 2011 Andrew Stevko
	 *
	 */
	private static class SessionCell extends AbstractCell<SessionProxy> {
		
		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				SessionProxy session, SafeHtmlBuilder sb) {
			//
			if ( session == null ) return;
			sb.appendHtmlConstant(LIST_PROMPT_DIV_W_STYLE );
			sb.append( SafeHtmlUtils.fromSafeConstant( session.getSessionLanguage().name()  ));
			sb.append( SafeHtmlUtils.fromSafeConstant( "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;") );
			if( session.getStartTime() == null  ) {
				// no start time
				sb.append( SafeHtmlUtils.fromSafeConstant( "<i>NOT YET STARTED</i>"  ));
				
			} else if ( session.getStopTime() == null ){
				// start time but no stop time
				sb.append( SafeHtmlUtils.fromSafeConstant( "<i>&gt;&gt;&gt; IN PROGRESS &lt;&lt;&lt;</i>"  ));				
				
			} else {
				sb.append( SafeHtmlUtils.fromSafeConstant( session.getDurationMinutes().toString() ));
				sb.append( SafeHtmlUtils.fromSafeConstant( " minutes" ));
			}
			sb.appendHtmlConstant("</div>" );						
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


	public void showAvailableStudents( List<MemberProxy> availableStudents) {
		//
		availableStudentsPanel.clear();
		final int displaySize = 15;
		
		if ( availableStudents == null ) {
			logger.info("Loading available students from the server");
			availableStudentsPanel.add( new HTML( "Loading available students from the server" ));
			
		} else {
			logger.info( "showAvailableStudents() called with " + availableStudents.size() + " Members");
			
			this.availableStudentsDP = new ListDataProvider<MemberProxy>(availableStudents);
			this.availableStudentsDP.addDataDisplay(availableStudentsCList);
			
			availableStudentsCList.setRowCount( availableStudents.size());
			availableStudentsCList.setVisibleRange(0, displaySize);
			availableStudentsCList.setRowData(0, availableStudents);
			
			availableStudentsPanel.add(availableStudentsCList);
			if ( availableStudents.size() > displaySize ) {
				availableStudentsPanel.add( availableStudentsPager );
			}
		}
		
	}


	public void showPartnerInvites(List<PartnerInviteProxy> partnerInvites) {
		//
		sessionInvitesPanel.clear();
		final int displaySize = 15;
		
		if ( partnerInvites == null ) {
			logger.info("Loading session invites from the server");
			sessionInvitesPanel.add( new HTML("Loading partner invites from the server"));
			
		} else {
			logger.info( "showSessionInvites() called with " + partnerInvites.size() + " SessionInvites ");
			
			this.sessionInvitesDP = new ListDataProvider<PartnerInviteProxy>(partnerInvites);
			this.sessionInvitesDP.addDataDisplay(sessionInvitesCList);
			
			sessionInvitesCList.setRowCount( partnerInvites.size());
			sessionInvitesCList.setVisibleRange(0, displaySize);
			sessionInvitesCList.setRowData(0, partnerInvites);
			
			sessionInvitesPanel.add( sessionInvitesCList );			
			if ( partnerInvites.size() > displaySize ) {
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
			sessionsPanel.add( new HTML( "Loading sessions from the server."));
			
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


	public void showSchedule( List<HourOfDay> scheduleHours ){

		schedulePanel.clear();
		final int displaySize = 8;

		if ( scheduleHours == null ) {
			logger.info( "loading schedule from the server");
			sessionsPanel.add( new HTML("Loading availability schedule from the server."));
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
	}

	public void clear() {
		// 
		availableStudentsPanel.clear();
		sessionInvitesPanel.clear();
		sessionsPanel.clear();		
	}

	/**
	 * show contact info for this user
	 * 
	 * @see ca.jhosek.main.client.ui.priv.MyProfileView#showContactInfo(java.util.List)
	 */

	public void showContactInfo(List<ContactInfoProxy> contactInfos) {
		// clear panel
		contactInfoPanel.clear();
		// 
		if (contactInfos == null ) {
			logger.info("missing contact info for user" );
			contactInfoPanel.add( new HTML( constants.loadingContactInfo() ));

		} else {
			logger.info("contact info for user = " + contactInfos.size() + " records" );

			listContactInfoProvider = new ListDataProvider<ContactInfoProxy>(contactInfos);
			listContactInfoProvider.addDataDisplay(contactInfoCTable);

			contactInfoCTable.setRowCount( contactInfos.size());
			contactInfoCTable.setVisibleRange(0, displaySize);
			contactInfoCTable.setRowData(0, contactInfos);

			contactInfoPanel.add(contactInfoCTable);
		}

	}
	
	@UiHandler("name")
	void onCourseNameClick( ClickEvent event ){
		// instructor user clicked on course name
		presenter.goToCourse();
	}
}
