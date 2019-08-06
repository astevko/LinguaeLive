/**
 * 
 */
package ca.jhosek.main.client.ui.priv.instructor;

import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.ClickEvent;
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

import ca.jhosek.main.client.activity.mainregion.InstructorCourseDetailReportActivity;
import ca.jhosek.main.client.place.InstructorCourseDetailReportPlace;
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
public class InstructorCourseDetailReportViewImpl extends Composite implements IsWidget, InstructorCourseDetailReportView, Editor<CourseProxy> {


	private static final String LOADING_SESSIONS = "<i>Loading sessions for all students...</i>";
	private static final String NO_SESSIONS_FOUND = "<i>No sessions found.</i>";

	// logger boilerplate
	private static final Logger logger = Logger.getLogger(InstructorCourseDetailReportViewImpl.class
			.getName());

	private static InstructorCourseDetailReportViewImplUiBinder uiBinder = GWT
	.create(InstructorCourseDetailReportViewImplUiBinder.class);

	interface InstructorCourseDetailReportViewImplUiBinder extends
	UiBinder<Widget, InstructorCourseDetailReportViewImpl> {
	}


	@UiField InlineLabel name;
	@UiField InlineLabel description;
	@UiField DateLabel startDate;
	@UiField DateLabel endDate;

	private CellTable<SessionProxy> sessionTable;

	// linked member panel, list, button etc
	@UiField FlowPanel linkedMembersPanel;
	private InstructorCourseDetailReportActivity presenter;

	public InstructorCourseDetailReportViewImpl() {
		// initialize language widget values
		initWidget(uiBinder.createAndBindUi(this));

		{
			// create name1 column
			Column<SessionProxy, SafeHtml> name1Column = new Column<SessionProxy, SafeHtml>( new SafeHtmlCell() ) {

				@Override
				public SafeHtml getValue(SessionProxy session) {
					if(session==null) return SafeHtmlUtils.fromSafeConstant("&nbsp");
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					UserProxy user = session.getMember1().getUser();

					sb.appendHtmlConstant("<div class=\"{style.listPrompt}\" >" );
					sb.append( SafeHtmlUtils.fromString( user.getFirstName()));
					sb.appendHtmlConstant( "&nbsp;" );
					sb.append( SafeHtmlUtils.fromString( user.getLastName() ) );
					sb.appendHtmlConstant("</div>" );						
					// 
					return sb.toSafeHtml();
				}
			};

			// create name1 column
			Column<SessionProxy, SafeHtml> name2Column = new Column<SessionProxy, SafeHtml>( new SafeHtmlCell() ) {

				@Override
				public SafeHtml getValue(SessionProxy session) {
					if(session==null) return SafeHtmlUtils.fromSafeConstant("&nbsp");
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					UserProxy user = session.getMember2().getUser();

					sb.appendHtmlConstant("<div class=\"{style.listPrompt}\" >" );
					sb.append( SafeHtmlUtils.fromString( user.getFirstName()));
					sb.appendHtmlConstant( "&nbsp;" );
					sb.append( SafeHtmlUtils.fromString( user.getLastName() ) );
					sb.appendHtmlConstant("</div>" );						
					// 
					return sb.toSafeHtml();
				}
			};

			// create Language column
			Column<SessionProxy, SafeHtml> languageColumn = new Column<SessionProxy, SafeHtml>( new SafeHtmlCell() ) {

				@Override
				public SafeHtml getValue(SessionProxy session) {
					if(session==null) return SafeHtmlUtils.fromSafeConstant("&nbsp");
					SafeHtmlBuilder sb = new SafeHtmlBuilder();

					sb.appendHtmlConstant("<div class=\"{style.listPrompt}\" >" );
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

					sb.appendHtmlConstant("<div class=\"{style.listPrompt}\" >" );
					if( session.getStartTime() == null  ) {
						// no start time
						sb.append( SafeHtmlUtils.fromSafeConstant( "<i>NOT YET STARTED</i>"  ));
					} else {
						sb.append( SafeHtmlUtils.fromTrustedString( session.getStartTime().toString() ) );
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

					sb.appendHtmlConstant("<div class=\"{style.listPrompt}\" >" );
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

			//----
			sessionTable = new CellTable<SessionProxy>();
			sessionTable.addColumn(name1Column, "Student");
			sessionTable.addColumn(name2Column, "Student");
			sessionTable.addColumn(languageColumn, "Language" );
			sessionTable.addColumn(startDate, "Start Time" );
			sessionTable.addColumn(duration, "Duration" );

			sessionTable.setEmptyTableWidget( new HTML( NO_SESSIONS_FOUND) );

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
			linkedMembersPanel.add( new HTML(LOADING_SESSIONS ));

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
		linkedMembersPanel.setVisible(instructorMode);
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
		// instructor user clicked on course name
		presenter.goToCourse();
	}

	public void setPresenter(
			InstructorCourseDetailReportActivity presenter) {
		this.presenter = presenter;
	}
}
