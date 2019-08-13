/**
 * 
 */
package ca.jhosek.linguaelive.ui.priv.instructor;

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
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DateLabel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;

import ca.jhosek.linguaelive.LLConstants;
import ca.jhosek.linguaelive.activity.mainregion.InstructorCourseSummaryReportActivity;
import ca.jhosek.linguaelive.place.InstructorCourseSummaryReportPlace;
import ca.jhosek.linguaelive.LanguageType;
import ca.jhosek.linguaelive.proxy.CourseProxy;
import ca.jhosek.linguaelive.proxy.MemberProxy;
import ca.jhosek.linguaelive.proxy.UserProxy;

/**
 * view a single course from an instructor's point of view
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see InstructorCourseSummaryReportActivity
 * @see InstructorCourseSummaryReportPlace
 * @see InstructorCourseSummaryReportView
 */
public class InstructorCourseSummaryReportViewImpl extends Composite implements IsWidget, InstructorCourseSummaryReportView, Editor<CourseProxy> {


//	private static final String LOADING_STUDENTS = "<i>Loading students...</i>";
//	private static final String NO_STUDENTS_ENROLLED_IN_THIS_COURSE_YET = "<i>No students enrolled in this course yet.</i>";
	
	// logger boilerplate
	private static final Logger logger = Logger.getLogger(InstructorCourseSummaryReportViewImpl.class
			.getName());

	private static InstructorCourseSummaryReportViewImplUiBinder uiBinder = GWT
	.create(InstructorCourseSummaryReportViewImplUiBinder.class);

	interface InstructorCourseSummaryReportViewImplUiBinder extends
	UiBinder<Widget, InstructorCourseSummaryReportViewImpl> {
	}

	private LanguageType targetLanguage = LanguageType.Arabic;
	private LanguageType expertLanguage = LanguageType.Arabic;

	@UiField InlineLabel name;
	@UiField InlineLabel description;
	@UiField DateLabel startDate;
	@UiField DateLabel endDate;

	private CellTable<MemberProxy> memberTable;
	
	// linked member panel, list, button etc
	@UiField FlowPanel linkedMembersPanel;

	/**
	 * owner
	 */
	private InstructorCourseSummaryReportActivity presenter;

	private final LLConstants constants;
	
	@Inject
	public InstructorCourseSummaryReportViewImpl(LLConstants constants) {
		this.constants = constants;
		// initialize language widget values
		initWidget(uiBinder.createAndBindUi(this));

		{
			//------------------------------------------------
			// create name column
			Column<MemberProxy, SafeHtml> nameColumn = new Column<MemberProxy, SafeHtml>( new SafeHtmlCell() ) {

				@Override
				public SafeHtml getValue(MemberProxy student) {
					if(student==null) return SafeHtmlUtils.fromSafeConstant("&nbsp");
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					UserProxy user = student.getUser();

					sb.appendHtmlConstant("<div class=\"{style.listPrompt}\" >" );
					sb.append( SafeHtmlUtils.fromString( user.getFirstName() + " " + user.getLastName() ) );
					sb.appendHtmlConstant("</div>" );						
					// 
					return sb.toSafeHtml();
				}
			};
			//-----------------------------------------------------------------------
			// create target session minutes
			Column<MemberProxy, SafeHtml> targetSessionMinutes = new Column<MemberProxy, SafeHtml>( new SafeHtmlCell() ) {

				@Override
				public SafeHtml getValue(MemberProxy student) {
					if(student==null) return SafeHtmlUtils.fromSafeConstant("&nbsp");
					SafeHtmlBuilder sb = new SafeHtmlBuilder();

					sb.appendHtmlConstant("<div class=\"{style.listPrompt}\" >" );
					sb.append( SafeHtmlUtils.fromString( student.getTargetSessionMinutes().toString() ));
					sb.appendHtmlConstant("</div>" );						
					// 
					return sb.toSafeHtml();
				}
			};
			Header<SafeHtml> targetMinutesHeader = new Header<SafeHtml>(new SafeHtmlCell()) {

				@Override
				public SafeHtml getValue() {
					// 
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					sb.append( SafeHtmlUtils.fromSafeConstant(targetLanguage.toString()));
					sb.append( SafeHtmlUtils.fromSafeConstant("&nbsp;Minutes"));
					return sb.toSafeHtml();
				}
			};
			//-----------------------------------------------------------------------
			// create target sessions
			Column<MemberProxy, SafeHtml> targetSessions = new Column<MemberProxy, SafeHtml>( new SafeHtmlCell() ) {

				@Override
				public SafeHtml getValue(MemberProxy student) {
					if(student==null) return SafeHtmlUtils.fromSafeConstant("&nbsp");
					SafeHtmlBuilder sb = new SafeHtmlBuilder();

					sb.appendHtmlConstant("<div class=\"{style.listPrompt}\" >" );
					sb.append( SafeHtmlUtils.fromString( student.getTargetSessions().toString() ));
					sb.appendHtmlConstant("</div>" );						
					// 
					return sb.toSafeHtml();
				}
			};
			Header<SafeHtml> targetSessionHeader = new Header<SafeHtml>(new SafeHtmlCell()) {

				@Override
				public SafeHtml getValue() {
					// 
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					sb.append( SafeHtmlUtils.fromSafeConstant(targetLanguage.toString()));
					sb.append( SafeHtmlUtils.fromSafeConstant("&nbsp;Sessions"));
					return sb.toSafeHtml();
				}
			};
			//----------------------------------------------------------------------------
			// create expert session minutes
			Column<MemberProxy, SafeHtml> expertMinutes = new Column<MemberProxy, SafeHtml>( new SafeHtmlCell() ) {

				@Override
				public SafeHtml getValue(MemberProxy student) {
					if(student==null) return SafeHtmlUtils.fromSafeConstant("&nbsp");
					SafeHtmlBuilder sb = new SafeHtmlBuilder();

					sb.appendHtmlConstant("<div class=\"{style.listPrompt}\" >" );
					sb.append( SafeHtmlUtils.fromString( student.getExpertSessionMinutes().toString() ));
					sb.appendHtmlConstant("</div>" );						
					// 
					return sb.toSafeHtml();
				}
			};
			Header<SafeHtml> expertMinutesHeader = new Header<SafeHtml>(new SafeHtmlCell()) {

				@Override
				public SafeHtml getValue() {
					// 
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					if(expertLanguage!=null){
						sb.append( SafeHtmlUtils.fromSafeConstant(expertLanguage.toString()));
					}
					sb.append( SafeHtmlUtils.fromSafeConstant("&nbsp;Minutes"));
					return sb.toSafeHtml();
				}
			};
			//----------------------------------------------------------------------------
			// create expert sessions
			Column<MemberProxy, SafeHtml> expertSessions = new Column<MemberProxy, SafeHtml>( new SafeHtmlCell() ) {

				@Override
				public SafeHtml getValue(MemberProxy student) {
					if(student==null) return SafeHtmlUtils.fromSafeConstant("&nbsp");
					SafeHtmlBuilder sb = new SafeHtmlBuilder();

					sb.appendHtmlConstant("<div class=\"{style.listPrompt}\" >" );
					sb.append( SafeHtmlUtils.fromString( student.getExpertSessions().toString() ));
					sb.appendHtmlConstant("</div>" );						
					// 
					return sb.toSafeHtml();
				}
			};
			Header<SafeHtml> expertSessionHeader = new Header<SafeHtml>(new SafeHtmlCell()) {

				@Override
				public SafeHtml getValue() {
					// 
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					if(expertLanguage!=null){
						sb.append( SafeHtmlUtils.fromSafeConstant(expertLanguage.toString()));
					}
					sb.append( SafeHtmlUtils.fromSafeConstant("&nbsp;Sessions"));
					return sb.toSafeHtml();
				}
			};
			//----------------------------------------------------------------------------
			
			memberTable = new CellTable<MemberProxy>();
			memberTable.addColumn(nameColumn, "Name");
			memberTable.addColumn(targetSessions, targetSessionHeader);
			memberTable.addColumn(targetSessionMinutes, targetMinutesHeader);
			memberTable.addColumn(expertSessions, expertSessionHeader);
			memberTable.addColumn(expertMinutes, expertMinutesHeader);
			
			memberTable.setEmptyTableWidget( new HTML( constants.noStudentsEnrolledWithinThisCourse()) );
			
		}
	}

	public void clear() {
		//
		linkedMembersPanel.clear();
	}

	public void showMembers(List<MemberProxy> linkedMembers ) {
		linkedMembersPanel.clear();

		if( linkedMembers == null ) {
			logger.info( "showLinkedMembers() with NULL members" );
			linkedMembersPanel.add( new HTML(constants.loadingStudents() ));

		} else {
			logger.info( "showLinkedMembers() with " + linkedMembers.size() +
			" members" );
			ListDataProvider<MemberProxy> linkMemberDataProvider = new ListDataProvider<MemberProxy>(linkedMembers);
			linkMemberDataProvider.addDataDisplay(memberTable);

			memberTable.setRowCount( linkedMembers.size());
			memberTable.setVisibleRange(0, linkedMembers.size() );
			memberTable.setRowData(0, linkedMembers);

			linkedMembersPanel.add(memberTable);
		}

	}


	public void refresh() {
		// 
		
	}

	/**
	 * @see ca.jhosek.linguaelive.ui.priv.instructor.InstructorYourCourseView#setViewerMode(boolean, boolean)
	 */
	public void setViewerMode( Long courseId, boolean instructorMode, boolean ownerMode) {
		linkedMembersPanel.setVisible(instructorMode );
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.priv.instructor.InstructorCourseSummaryReportView#showCourse(ca.jhosek.linguaelive.proxy.CourseProxy)
	 */
	public void showCourse(CourseProxy course) {
		// 
		name.setText( course.getName());
		description.setText( course.getDescription());
		startDate.setValue( course.getStartDate());
		endDate.setValue( course.getEndDate());
		expertLanguage = course.getExpertLanguage();
		targetLanguage = course.getTargetLanguage();
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
			InstructorCourseSummaryReportActivity presenter) {
		this.presenter = presenter;
	}
}
