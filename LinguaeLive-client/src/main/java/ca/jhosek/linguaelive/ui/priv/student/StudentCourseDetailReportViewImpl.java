/**
 * 
 */
package ca.jhosek.main.client.ui.priv.student;

import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DateLabel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;

import ca.jhosek.main.client.AppResources;
import ca.jhosek.main.client.LLConstants;
import ca.jhosek.main.client.activity.mainregion.InstructorCourseDetailReportActivity;
import ca.jhosek.main.client.activity.mainregion.StudentCourseDetailReportActivity;
import ca.jhosek.main.client.place.InstructorCourseDetailReportPlace;
import ca.jhosek.main.client.ui.priv.instructor.InstructorCourseDetailReportView;
import ca.jhosek.main.shared.proxy.CourseProxy;
import ca.jhosek.main.shared.proxy.SessionProxy;
import ca.jhosek.main.shared.proxy.UserProxy;

/**
 * view a single course from an instructor's point of view
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see InstructorCourseDetailReportActivity
 * @see InstructorCourseDetailReportView
 * @see InstructorCourseDetailReportPlace
 */
public class StudentCourseDetailReportViewImpl extends Composite implements IsWidget, StudentCourseDetailReportView, Editor<CourseProxy> {


//	private static final String LOADING_SESSIONS = "<i>Loading sessions for all students...</i>";
//	private static final String NO_SESSIONS_FOUND = "<i>No sessions found.</i>";
	
	/**
	 * date time format
	 */
	final DateTimeFormat fmt; // = DateTimeFormat.getFormat("MM/dd/yyyy HH:mm");	

	// logger boilerplate
	private static final Logger logger = Logger.getLogger(StudentCourseDetailReportViewImpl.class
			.getName());

	private static InstructorCourseDetailReportViewImplUiBinder uiBinder = GWT
	.create(InstructorCourseDetailReportViewImplUiBinder.class);

	interface InstructorCourseDetailReportViewImplUiBinder extends
	UiBinder<Widget, StudentCourseDetailReportViewImpl> {
	}
	/**
	 * styles
	 */
	@UiField CssResource  style;

	@UiField InlineLabel studentname;
	@UiField InlineLabel name;
	@UiField InlineLabel description;
	@UiField DateLabel startDate;
	@UiField DateLabel endDate;

	private CellTable<SessionProxy> sessionTable;
	
	/**
	 * what happens when the user selects the list
	 * @author copyright (C) 2011 Andrew Stevko
	 *
	 */
	private final class SelectionChangeHandler implements
			SelectionChangeEvent.Handler {
		private final SingleSelectionModel<SessionProxy> selectionModel;

		private SelectionChangeHandler(
				SingleSelectionModel<SessionProxy> selectionModel) {
			this.selectionModel = selectionModel;
		}

		public void onSelectionChange(SelectionChangeEvent event) {
			// 
			SessionProxy session = selectionModel.getSelectedObject();
			if( session != null ) {
				// open the session control panel for this session
				presenter.goToSessionControlPanel(session);
			}
			
		}
	}
	
	// linked member panel, list, button etc
	@UiField FlowPanel linkedMembersPanel;
	private StudentCourseDetailReportActivity presenter;
	
	/**
	 * this user 
	 */
	private Long thisUserId = null;

	private final LLConstants constants;

	// TODO: style rows	
	@SuppressWarnings("unused")
	private final AppResources resources;

	@Inject
	public StudentCourseDetailReportViewImpl(final LLConstants constants, 
			final AppResources resources) {
		this.constants = constants;
		this.resources = resources;
		// initialize language widget values
		initWidget(uiBinder.createAndBindUi(this));
//		final String dateTimeDateFormatterPattern = "MM/dd/yyyy HH:mm";
		fmt = DateTimeFormat.getFormat(constants.dateTimeDateFormatterPattern());
		{
			// create name1 column
			Column<SessionProxy, SafeHtml> name1Column = new Column<SessionProxy, SafeHtml>( new SafeHtmlCell() ) {

				@Override
				public SafeHtml getValue(SessionProxy session) {
					if(session==null) return SafeHtmlUtils.fromSafeConstant("&nbsp");
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					
					// users are either one or the other
					UserProxy user;
					if (session.getMember1().getUser().getId().equals(thisUserId)) {
						user = session.getMember2().getUser();
					} else /*if (session.getMember1().getUser().getId().equals(thisUserId))*/ {
						user = session.getMember1().getUser();
					} 

					sb.appendHtmlConstant("<div class='" +
							resources.css().listPrompt() +
							"' >" );
					sb.append( SafeHtmlUtils.fromString( user.getFirstName()));
					sb.appendHtmlConstant( "&nbsp;" );
					sb.append( SafeHtmlUtils.fromString( user.getLastName() ) );
					sb.appendHtmlConstant("</div>" );						
					// 
					return sb.toSafeHtml();
				}
			};

//			// create name1 column
//			Column<SessionProxy, SafeHtml> name2Column = new Column<SessionProxy, SafeHtml>( new SafeHtmlCell() ) {
//
//				@Override
//				public SafeHtml getValue(SessionProxy session) {
//					if(session==null) return SafeHtmlUtils.fromSafeConstant("&nbsp");
//					SafeHtmlBuilder sb = new SafeHtmlBuilder();
//					UserProxy user = session.getMember2().getUser();
//
//					sb.appendHtmlConstant("<div class=\"{style.listPrompt}\" >" );
//					sb.append( SafeHtmlUtils.fromString( user.getFirstName()));
//					sb.appendHtmlConstant( "&nbsp;" );
//					sb.append( SafeHtmlUtils.fromString( user.getLastName() ) );
//					sb.appendHtmlConstant("</div>" );						
//					// 
//					return sb.toSafeHtml();
//				}
//			};

			// create Language column
			Column<SessionProxy, SafeHtml> languageColumn = new Column<SessionProxy, SafeHtml>( new SafeHtmlCell() ) {

				@Override
				public SafeHtml getValue(SessionProxy session) {
					if(session==null) return SafeHtmlUtils.fromSafeConstant("&nbsp");
					SafeHtmlBuilder sb = new SafeHtmlBuilder();

					sb.appendHtmlConstant("<div class=\"" +
							resources.css().listPrompt() +
							"\" >" );
					sb.append( SafeHtmlUtils.fromSafeConstant( session.getSessionLanguage().toString() ) );
					sb.appendHtmlConstant("</div>" );						
					// 
					return sb.toSafeHtml();
				}
			};

			// create StartDate column
			Column<SessionProxy, SafeHtml> startDate = new Column<SessionProxy, SafeHtml>( new SafeHtmlCell() ) {

				@Override
				public SafeHtml getValue(SessionProxy session) {
					if(session==null) return SafeHtmlUtils.fromSafeConstant("&nbsp");
					SafeHtmlBuilder sb = new SafeHtmlBuilder();

					sb.appendHtmlConstant("<div class=\"" +
							resources.css().listPrompt() +
							"\" >" );
					if( session.getStartTime() == null  ) {
						// no start time
//						final String sessionNotYetStarted = "<i>NOT YET STARTED</i>";
						sb.append( SafeHtmlUtils.fromSafeConstant( constants.sessionNotYetStarted()  ));
												
					} else {
						sb.append( SafeHtmlUtils.fromTrustedString( fmt.format(session.getStartTime())) );
					}
					sb.appendHtmlConstant("</div>" );						
					// 
					return sb.toSafeHtml();
				}
			};

			// create duration column
			Column<SessionProxy, SafeHtml> duration = new Column<SessionProxy, SafeHtml>( new SafeHtmlCell() ) {

				@Override
				public SafeHtml getValue(SessionProxy session) {
					if(session==null) return SafeHtmlUtils.fromSafeConstant("&nbsp");
					SafeHtmlBuilder sb = new SafeHtmlBuilder();

					sb.appendHtmlConstant("<div class=\"" +
							resources.css().listPrompt() +
							"\" >" );
					sb.append( SafeHtmlUtils.fromSafeConstant( "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;") );
					if( session.getStartTime() == null  ) {
						// no start time
						// sb.append( SafeHtmlUtils.fromSafeConstant( "<i>NOT YET STARTED</i>"  ));

					} else if ( session.getStopTime() == null ){
						// start time but no stop time -- must be in progress
						sb.append( SafeHtmlUtils.fromSafeConstant( "<i>&gt;&gt;&gt; IN PROGRESS &lt;&lt;&lt;</i>"  ));

					} else {
						sb.append( SafeHtmlUtils.fromTrustedString( session.getDurationMinutes().toString() ) );

					}
					sb.appendHtmlConstant("</div>" );						
					// 
					return sb.toSafeHtml();
				}
			};

//			// create link column
//			Column<SessionProxy, SafeHtml> linkColumn = new Column<SessionProxy, SafeHtml>( new SafeHtmlCell() ) {
//
//				@Override
//				public SafeHtml getValue(SessionProxy session) {
//					if(session==null) return SafeHtmlUtils.fromSafeConstant("&nbsp");
//					SafeHtmlBuilder sb = new SafeHtmlBuilder();
//					
//					sb.appendHtmlConstant("<div class=\"{style.link}\" >" );
//					sb.appendHtmlConstant( "link" );
//					sb.appendHtmlConstant("</div>" );						
//					// 
//					return sb.toSafeHtml();
//				}
//			};
			//----
			sessionTable = new CellTable<SessionProxy>(); 
			sessionTable.addColumn(name1Column, "Student");
//			sessionTable.addColumn(name2Column, "Student");
			sessionTable.addColumn(languageColumn, "Language" );
			sessionTable.addColumn(startDate, "Start Time" );
			sessionTable.addColumn(duration, "Duration" );

			sessionTable.setEmptyTableWidget( new HTML( constants.noSessionsFound()) );

			// create link target
			final SingleSelectionModel<SessionProxy> selectionModel = new SingleSelectionModel<SessionProxy>();
			sessionTable.setSelectionModel(selectionModel);
			selectionModel.addSelectionChangeHandler( new SelectionChangeHandler(selectionModel) );
			
		}
	}

	public void clear() {
		//
		linkedMembersPanel.clear();
	}

	public void showSessions(List<SessionProxy> sessions ) {
		linkedMembersPanel.clear();

		if( sessions == null ) {
			logger.info( "showLinkedMembers() with NULL members" );
			linkedMembersPanel.add( new HTML(constants.loadingStudents() ));

		} else {
			logger.info( "showLinkedMembers() with " + sessions.size() +
			" members" );
			ListDataProvider<SessionProxy> linkMemberDataProvider = new ListDataProvider<SessionProxy>(sessions);
			linkMemberDataProvider.addDataDisplay(sessionTable);

			sessionTable.setRowCount( sessions.size());
			sessionTable.setVisibleRange(0, sessions.size() );
			sessionTable.setRowData(0, sessions);

			linkedMembersPanel.add(sessionTable);
		}

	}

	public void refresh() {
		// 

	}

	/**
	 * @see ca.jhosek.main.client.ui.priv.instructor.InstructorYourCourseView#setViewerMode(boolean, boolean)
	 */
	public void setViewerMode( Long courseId, boolean instructorMode, boolean ownerMode) {
		linkedMembersPanel.setVisible(ownerMode || instructorMode);
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.main.client.ui.priv.instructor.InstructorCourseDetailReportView#showCourse(ca.jhosek.main.shared.proxy.CourseProxy)
	 */
	public void showCourse(CourseProxy course) {
		// 
		name.setText( course.getName());
		description.setText( course.getDescription());
		startDate.setValue( course.getStartDate());
		endDate.setValue( course.getEndDate());		
	}


	/**
	 * user clicked on course name so take him to the course
	 * 
	 * @param event
	 */
	@UiHandler("name")
	void onCourseNameClick( ClickEvent event ){
		// user clicked on course name
		presenter.goToCourse();
	}

	public void setPresenter(
			StudentCourseDetailReportActivity presenter) {
		this.presenter = presenter;
	}

	public void showUser(UserProxy student) {
//		this.thisUser = student;
		if (student != null) {
			String studentname = student.getFirstName() + " " + student.getLastName();
			this.studentname.setText(studentname);
			this.thisUserId = student.getId();
		}
	}
}
