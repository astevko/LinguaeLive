/**
 * 
 */
package ca.jhosek.main.client.ui.priv.instructor;

import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.cell.client.AbstractCell;
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
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Window;
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
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

import ca.jhosek.main.client.LLConstants;
import ca.jhosek.main.client.activity.mainregion.InstructorYourCourseActivity;
import ca.jhosek.main.client.place.InstructorYourCoursePlace;
import ca.jhosek.main.client.ui.EnumRenderer;
import ca.jhosek.main.client.widgets.EnumLabel;
import ca.jhosek.main.server.email.SendEmail;
import ca.jhosek.main.shared.LanguageType;
import ca.jhosek.main.shared.proxy.CourseLinkProxy;
import ca.jhosek.main.shared.proxy.CourseProxy;
import ca.jhosek.main.shared.proxy.MemberProxy;
import ca.jhosek.main.shared.proxy.UserProxy;

/**
 * view a single course from an instructor's point of view
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see InstructorYourCourseView
 * @see InstructorYourCourseViewImpl
 * @see InstructorYourCourseActivity
 * @see InstructorYourCoursePlace
 * 
 * 
 * @see CourseProxy
 */
public class InstructorYourCourseViewImpl extends Composite implements IsWidget, InstructorYourCourseView, Editor<CourseProxy> {

//	private static final String UNLINKED_COURSES_BLURB = "Invite instructors to link their course to yours.";
//	private static final String PAST_COURSES_BLURB = "Contact instructors who previously registered courses complementary to yours.";
//	
//	private static final String SHOW_PAST_COURSES = "Show Past / Upcoming Courses";
//
//	private static final String SHOW_CONCURRENT_COURSES = "Show Concurrent Courses";

	private static final String LIST_PROMPT_DIV_W_STYLE = "<div style=\"font-weight: bold;cursor: pointer;color: #d35e46;\" >";

//	private static final String LOADING_LINKED_COURSES = "<i>Loading linked courses...</i>";
//	private static final String NO_LINKED_COURSES_FOUND = "<i>No other courses are linked to this.</i>";
//
//	private static final String LOADING_STUDENTS = "<i>Loading students...</i>";
//	private static final String NO_STUDENTS_ENROLLED_IN_THIS_COURSE_YET = "<i>No students enrolled in this course yet.</i>";
//
//
//	private static final String LOADING_UNLINKED_COURSES = "<i>Searching for complementary courses...</i>";
//	private static final String NO_CURRENT_COMPLEMENTARY_COURSES_FOUND = "<i>No complementary courses found with overlapping dates. Instructors of past/upcoming courses may have complementary courses available.</i>";
//	private static final String NO_PAST_COMPLEMENTARY_COURSES_FOUND = "<i>No past/upcoming complementary courses found.</i>";

	
	public interface Driver extends RequestFactoryEditorDriver<CourseProxy, InstructorYourCourseViewImpl> {

	}

	// logger boilerplate
	private static final Logger logger = Logger.getLogger(InstructorYourCourseViewImpl.class
			.getName());

	static EnumRenderer<LanguageType> languageTypeRenderer = new EnumRenderer<LanguageType>();


	private static InstructorCourseViewImplUiBinder uiBinder = GWT
	.create(InstructorCourseViewImplUiBinder.class);

	interface InstructorCourseViewImplUiBinder extends
	UiBinder<Widget, InstructorYourCourseViewImpl> {
	}


	@UiField InlineLabel name;
	@UiField TextArea description;
	@UiField NumberLabel<Long> estimatedMemberSize;
	@UiField Label inviteCode;
	@UiField DateLabel startDate;
	@UiField DateLabel endDate;
	@UiField EnumLabel<LanguageType> targetLanguage;  
	@UiField EnumLabel<LanguageType> expertLanguage;  
//	@UiField(provided=true) ValueListBox<LanguageType> targetLanguage = new ValueListBox<LanguageType>( languageTypeRenderer );
//	@UiField(provided=true) ValueListBox<LanguageType> expertLanguage = new ValueListBox<LanguageType>( languageTypeRenderer );
	@UiField 	CheckBox singlePartnerPreferred;
	
	
	@UiField Button editCourseButton;

	/**
	 * delete this course
	 */
	@UiField Button deleteCourse;

	/**
	 * layout panel containing the following
	 */
	@UiField StackLayoutPanel stacklayoutpanel;
	
	// linked courses panel, list, button, etc
	@UiField FlowPanel linkedCoursesPanel;
	private final CellList<CourseLinkProxy> linkedCoursesCList;
	private final SimplePager linkedCoursesPager;

	// unlinked courses panel, list, button, etc
	@UiField FlowPanel unlinkedCoursesPanel;
	private final CellList<CourseProxy> unlinkedCoursesCList;
	private final SimplePager unlinkedCoursesPager;
	
	/**
	 * blurb above the unlinked courses list
	 */
	@Ignore
	@UiField Label unlinkedCoursesBlurb	;
	
	/**
	 * toggle past/future courses
	 */
	@UiField Button togglePastCoursesButton;
	private boolean showPastCourses = false;

	// linked member panel, list, button etc
	@UiField FlowPanel linkedMembersPanel;
	private final CellList<MemberProxy> linkedMembersCList;
	private final SimplePager linkedMembersPager;


	private Presenter presenter;
	private ListDataProvider<CourseLinkProxy> linkCourseDataProvider;
	private ListDataProvider<CourseProxy> unlinkCourseDataProvider;
	@SuppressWarnings("unused")
	private boolean ownerMode;
	private Long courseId;
	
	@UiField Button sendStudentInviteEmail;
	
	@UiField Button studentReportButton;
	@UiField Button sessionReportButton;

	private ListDataProvider<MemberProxy> linkMemberDataProvider;

	/**
	 * how many rows to display?
	 */
	private static final int DISPLAY_SIZE = 10;
	private HTML current_empty_list;
	private HTML past_empty_list;
	private boolean bypassShowTheRightPanel = false;

	private final LLConstants constants;

	/**
	 * single sub-dialog for this view 
	 */
	final CourseStudentInviteDialog courseStudentInviteDialog = new CourseStudentInviteDialog();

	@Inject
	public InstructorYourCourseViewImpl(LLConstants constants) {
		this.constants = constants;
		// initialize language widget values
		initWidget(uiBinder.createAndBindUi(this));

		{
			//----------------------------
			// create Linked Courses celllist etc
			LinkedCourseCell linkedCourseCell = new LinkedCourseCell();
			linkedCoursesCList = new CellList<CourseLinkProxy>(linkedCourseCell);
			linkedCoursesCList.setEmptyListWidget(	new HTML(constants.noLinkedCoursesFound() ) );
			final SingleSelectionModel<CourseLinkProxy> linkedCourseSelectionModel = new SingleSelectionModel<CourseLinkProxy>();
			linkedCoursesCList.setSelectionModel(linkedCourseSelectionModel);
			linkedCourseSelectionModel.addSelectionChangeHandler( new LinkedCourseSelectionHandler(linkedCourseSelectionModel));
			linkedCoursesPager= new SimplePager();
			linkedCoursesPager.setDisplay(linkedCoursesCList);
		}
		{
			//----------------------------
			// create un-Linked Courses celllist etc
			UnlinkedCourseCell unlinkedCourseCell = new UnlinkedCourseCell();
			unlinkedCoursesCList = new CellList<CourseProxy>(unlinkedCourseCell);
			current_empty_list = new HTML( constants.noCurrentComplementaryCoursesFound());
			past_empty_list = new HTML( constants.noPastComplementaryCoursesFound());
			unlinkedCoursesCList.setEmptyListWidget( current_empty_list );
			final SingleSelectionModel<CourseProxy> unlinkedCourseSelectionModel = new SingleSelectionModel<CourseProxy>();
			unlinkedCoursesCList.setSelectionModel(unlinkedCourseSelectionModel);
			unlinkedCourseSelectionModel.addSelectionChangeHandler( new UnlinkedCourseSelectionHandler(unlinkedCourseSelectionModel));
			unlinkedCoursesPager= new SimplePager();
			unlinkedCoursesPager.setDisplay(unlinkedCoursesCList);
		}
		{
			//----------------------------
			// create Member celllist etc
			CourseMemberCell linkedMemberCell = new CourseMemberCell();
			linkedMembersCList = new CellList<MemberProxy>(linkedMemberCell);
			linkedMembersCList.setEmptyListWidget( new HTML( constants.noStudentsEnrolledWithinThisCourse()) );
			final SingleSelectionModel<MemberProxy> linkedMemberSelectionModel = new SingleSelectionModel<MemberProxy>();
			linkedMembersCList.setSelectionModel(linkedMemberSelectionModel);
			linkedMemberSelectionModel.addSelectionChangeHandler( new MemberSelectionChangeHandler(linkedMemberSelectionModel));
			linkedMembersPager= new SimplePager();
			linkedMembersPager.setDisplay(linkedMembersCList);
		}
	}

	/**
	 * @return a CourseProxy editor driver, userProxyDriver, tied to this presenter and view
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

	@UiHandler("editCourseButton")
	void onSaveCourseButtonClick(ClickEvent event) {
		// move content from edit widgets to value
		presenter.gotoEditCourse();
	}

	@UiHandler("deleteCourse")
	void onDeleteCourseButtonClick(ClickEvent event) {
		if (Window.confirm("Do you really want to delete this course, links, members, and sessions?") 
			&& Window.confirm("Are you sure? This process is irreversible.")) {
			// delete this course
			presenter.deleteCourse();
		}
	}


	@UiHandler("studentReportButton")
	void onStudentReportButtonClick(ClickEvent event) {
		// open student report
		presenter.gotoStudentReport();
	}


	@UiHandler("sessionReportButton")
	void onSessionReportButtonClick(ClickEvent event) {
		// open session report
		presenter.gotoSessionReport();
	}

	/**
	 * display list of linked courses
	 * 
	 * @see ca.jhosek.main.client.ui.priv.instructor.InstructorYourCourseView#showLinkedCourses(java.util.List)
	 */
	public void showLinkedCourses(List<CourseLinkProxy> linkedCourses, boolean updateDisplay) {
		linkedCoursesPanel.clear();
		linkedCoursesPanel.setVisible(true);

		if( linkedCourses == null ) {
			logger.info( "showLinkedCourses() with NULL courses" );
			linkedCoursesPanel.add( new HTML(constants.loadingLinkedCourses() ));
		} else {
			logger.info( "showLinkedCourses() with " + linkedCourses.size() +
			" courses" );
			linkCourseDataProvider = new ListDataProvider<CourseLinkProxy>(linkedCourses);
			linkCourseDataProvider.addDataDisplay(linkedCoursesCList);

			linkedCoursesCList.setRowCount( linkedCourses.size());
			linkedCoursesCList.setVisibleRange(0, DISPLAY_SIZE);
			linkedCoursesCList.setRowData(0, linkedCourses);

			linkedCoursesPanel.add(linkedCoursesCList);
//			if (linkedCourses.size() > DISPLAY_SIZE) {
				linkedCoursesPanel.add(linkedCoursesPager);
//			}
		}
		if (updateDisplay) {
			// show the right panel
			showTheRightPanel();
		}
	}

	/**
	 * display list of unlinked courses
	 * 
	 * @see ca.jhosek.main.client.ui.priv.instructor.InstructorYourCourseView#showLinkedCourses(java.util.List)
	 */
	public void showUnlinkedCourses(List<CourseProxy> unlinkedCourses, boolean updateDisplay) {
		unlinkedCoursesPanel.clear();
		unlinkedCoursesPanel.setVisible(true);

		// set empty list blurb
		this.unlinkedCoursesCList.setEmptyListWidget( getUnlinkedCoursesEmptyListWidget() );
		
		unlinkedCoursesBlurb.setText(getUnlinkedCoursesBlurb());
		if( unlinkedCourses == null ) {
			logger.info( "showUnlinkedCourses() with NULL courses" );
			unlinkedCoursesPanel.add( new HTML(constants.loadingUnlinkedCourses()));

		} else {
			logger.info( "showUnlinkedCourses() with " + unlinkedCourses.size() +
			" courses" );
			unlinkCourseDataProvider = new ListDataProvider<CourseProxy>(unlinkedCourses);
			unlinkCourseDataProvider.addDataDisplay(unlinkedCoursesCList);

			unlinkedCoursesCList.setRowCount( unlinkedCourses.size());
			unlinkedCoursesCList.setVisibleRange(0, DISPLAY_SIZE);
			unlinkedCoursesCList.setRowData(0, unlinkedCourses);

			unlinkedCoursesPanel.add(unlinkedCoursesCList);
//			if ( unlinkedCourses.size() > DISPLAY_SIZE ) {
				unlinkedCoursesPanel.add(unlinkedCoursesPager);
//			}
		}
		if (updateDisplay) {
			// show the right panel
			showTheRightPanel();
		}

	}

	public void clear() {
		//
		linkedCoursesPanel.clear();
		unlinkedCoursesPanel.clear();
		linkedMembersPanel.clear();
	}

	public void showMembers(List<MemberProxy> linkedMembers ) {
		linkedMembersPanel.clear();
		final int displaySize = 15;

		if( linkedMembers == null ) {
			logger.info( "showLinkedMembers() with NULL members" );
			linkedMembersPanel.add( new HTML(constants.loadingStudents() ));

		} else {
			logger.info( "showLinkedMembers() with " + linkedMembers.size() +
			" members" );
			linkMemberDataProvider = new ListDataProvider<MemberProxy>(linkedMembers);
			linkMemberDataProvider.addDataDisplay(linkedMembersCList);

			linkedMembersCList.setRowCount( linkedMembers.size());
			linkedMembersCList.setVisibleRange(0, displaySize );
			linkedMembersCList.setRowData(0, linkedMembers);

			linkedMembersPanel.add(linkedMembersCList);
//			if ( linkedMembers.size() > displaySize ) {
				linkedMembersPanel.add(linkedMembersPager);
//			}
		}
		// show the right panel
		showTheRightPanel();
	}
	/**
	 * 
	 * display a single linked course 
	 * @author copyright (C) 2011 Andrew Stevko
	 *
	 */
	private class LinkedCourseCell extends AbstractCell<CourseLinkProxy> {

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				CourseLinkProxy courseLink, SafeHtmlBuilder sb) {
			// 
			if( courseLink==null	) return;
			sb.appendHtmlConstant(LIST_PROMPT_DIV_W_STYLE );
			if( courseLink.getCourseA().getId().equals( courseId )) {
				// show course B
				sb.append( SafeHtmlUtils.fromString( courseLink.getCourseB().getSchoolName() )); 
				sb.append( ' ' );			
				sb.append( SafeHtmlUtils.fromString( courseLink.getCourseB().getName() ));
			} else {
				// show course A
				sb.append( SafeHtmlUtils.fromString( courseLink.getCourseA().getSchoolName() ));
				sb.append( ' ' );			
				sb.append( SafeHtmlUtils.fromString( courseLink.getCourseA().getName() ));
			}
			sb.append( ' ' );			
			sb.append( SafeHtmlUtils.fromTrustedString( getStatusBlurb( courseLink ) ) );
			sb.appendHtmlConstant("</div>" );						
		}

	}

	/**
	 * 
	 * display a single0 linked course 
	 * @author copyright (C) 2011 Andrew Stevko
	 *
	 */
	private static class UnlinkedCourseCell extends AbstractCell<CourseProxy> {

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				CourseProxy course, SafeHtmlBuilder sb) {
			// 
			if(course==null	) return;

			sb.appendHtmlConstant(LIST_PROMPT_DIV_W_STYLE );

			sb.append( SafeHtmlUtils.fromString( 
					course.getSchoolName() 
					+ ", " 
					+ course.getName() 
					) );
			sb.appendHtmlConstant("</div>" );						
		}

	}

	/**
	 * 
	 * display a single linked Member 
	 * @author copyright (C) 2011 Andrew Stevko
	 *
	 */
	private static class CourseMemberCell extends AbstractCell<MemberProxy> {

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				MemberProxy member, SafeHtmlBuilder sb) {
			// 
			if(member==null	) return;
			UserProxy user = member.getUser();

			sb.appendHtmlConstant(LIST_PROMPT_DIV_W_STYLE );

			sb.append( SafeHtmlUtils.fromString( user.getFirstName() + " " + user.getLastName() ) );
			sb.appendHtmlConstant("</div>" );						
		}

	}

	/**
	 * what happens when the UNLinked course is selected
	 * @author copyright (C) 2011 Andrew Stevko
	 *
	 */
	private final class UnlinkedCourseSelectionHandler implements
	SelectionChangeEvent.Handler {
		private final SingleSelectionModel<CourseProxy> selectionModel;

		private UnlinkedCourseSelectionHandler(
				SingleSelectionModel<CourseProxy> selectionModel) {
			this.selectionModel = selectionModel;
		}

		public void onSelectionChange(SelectionChangeEvent event) {
			// 
			final CourseProxy sendInviteToCourse = selectionModel.getSelectedObject();
			
			if( sendInviteToCourse != null ) {
				// create a default message
				final StringBuilder msg = new StringBuilder();
				msg.append(replaceTokens( constants.instructorCourseLinkEmailDefaultGreeting(), sendInviteToCourse.getOwner()));
				final CourseProxy myCourse = presenter.getMyCourse();
				String body = replaceTokens( constants.instructorCourseLinkEmailDefaultBody(), myCourse);
				msg.append(replaceTokens( body, myCourse.getOwner()));
				
				// show dialog prompting user to confirm linking to course
				CourseLinkInviteDialog dialogBox = new CourseLinkInviteDialog();
				dialogBox.setCourseLinkMessage(msg.toString());
				dialogBox.show(presenter, presenter.getMyCourse(), sendInviteToCourse);
//				presenter.inviteToLinkCourses(unlinkedCourse);
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
		String courseLink = "https://linguaelive.appspot.com/#instructoryourcourse:" + course.getId().toString();
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
	 * what happens when the linked course is selected
	 * @author copyright (C) 2011 Andrew Stevko
	 *
	 */
	private final class LinkedCourseSelectionHandler implements
	SelectionChangeEvent.Handler {
		private final SingleSelectionModel<CourseLinkProxy> selectionModel;

		private LinkedCourseSelectionHandler(
				SingleSelectionModel<CourseLinkProxy> selectionModel) {
			this.selectionModel = selectionModel;
		}

		public void onSelectionChange(SelectionChangeEvent event) {
			// 
			CourseLinkProxy courseLink = selectionModel.getSelectedObject();
			if( courseLink != null ) {
				presenter.goToCourseLinkView(courseLink);
			}

		}
	}

	/**
	 * what happens when the member is selected
	 * @author copyright (C) 2011 Andrew Stevko
	 *
	 */
	private final class MemberSelectionChangeHandler implements
	SelectionChangeEvent.Handler {
		private final SingleSelectionModel<MemberProxy> selectionModel;

		private MemberSelectionChangeHandler(
				SingleSelectionModel<MemberProxy> selectionModel) {
			this.selectionModel = selectionModel;
		}

		public void onSelectionChange(SelectionChangeEvent event) {
			// 
			MemberProxy member = selectionModel.getSelectedObject();
			if( member != null ) {
				presenter.goToMemberView(member);
			}

		}
	}

	/**
	 * button click to display student invite email
	 * @param event
	 */
	@UiHandler("sendStudentInviteEmail")
	void onSendStudentInviteEmailClick(ClickEvent event) {
		courseStudentInviteDialog.show( presenter.getMyCourse() );
	}

	public static String getStatusBlurb(CourseLinkProxy courseLink) {
		// 
		if ( courseLink.getPending() ) {
			return "<i>Invite Pending</i>";
			
		} else if( courseLink.getAccepted() ) {
			return "<i>Linked</i>";
		}
		return "<i>Link declined</i>";
	}

	public void refresh() {
		// 
		linkCourseDataProvider.refresh();
		unlinkCourseDataProvider.refresh();
		
	}

	/**
	 * @see ca.jhosek.main.client.ui.priv.instructor.InstructorYourCourseView#setViewerMode(boolean, boolean)
	 */
	public void setViewerMode( Long courseId, boolean instructorMode, boolean ownerMode) {
		this.courseId = courseId;
		this.ownerMode = ownerMode;
		//  edit course button only visible for owner instructors
		editCourseButton.setVisible( instructorMode && ownerMode );
		unlinkedCoursesPanel.setVisible(instructorMode && ownerMode );
		linkedMembersPanel.setVisible(instructorMode);
		sendStudentInviteEmail.setVisible(instructorMode && ownerMode);
		deleteCourse.setVisible(instructorMode && ownerMode);
		// set button text
		this.togglePastCoursesButton.setText(getTogglePastCoursesButtonLabel());
		// set empty list blurb
		this.unlinkedCoursesCList.setEmptyListWidget( getUnlinkedCoursesEmptyListWidget() );
		
	}



	/**
	 * open the correct panel for the state of the instructor's course
	 */
	@Override
	public void showTheRightPanel() {
		if (bypassShowTheRightPanel) {
			return;
			
		} else if (linkMemberDataProvider != null && !linkMemberDataProvider.getList().isEmpty()) {	// Students are available to show
			// show the members
			stacklayoutpanel.showWidget(2);
			
		} else if (linkCourseDataProvider != null && !linkCourseDataProvider.getList().isEmpty()) {	// linked courses are available to show
			// have linked courses but no students then show students & student invite dialog
			stacklayoutpanel.showWidget(2);
			// show student invite dialog
			// onSendStudentInviteEmailClick(null);
			
		} else { /* unlinkCourseDataProvider */
			// show the unlinked courses
			stacklayoutpanel.showWidget(0);
			
		}
	}
	
	/**
	 * button click to show past/current courses
	 * @param event
	 */
	@UiHandler("togglePastCoursesButton")
	void onTogglePastCoursesClick(ClickEvent event) {
		
		this.bypassShowTheRightPanel  = true;
		this.showPastCourses = !this.showPastCourses;
		this.togglePastCoursesButton.setText(getTogglePastCoursesButtonLabel());
		presenter.loadUnlinkedCourses();
	}
	
	/**
	 * @param showPastCourses the showPastCourses to set
	 */
	public void setShowPastCourses(boolean showPastCourses) {
		this.showPastCourses = showPastCourses;
	}

	/**
	 * @return the showPastCourses
	 */
	@Override
	public boolean isShowPastCourses() {
		return showPastCourses;
	}
	
	/**
	 * @return button label based on showPastCourses
	 */
	private String getTogglePastCoursesButtonLabel() {
//		return (this.showPastCourses) ? SHOW_CONCURRENT_COURSES : SHOW_PAST_COURSES;
		return (this.showPastCourses) ? constants.showConcurrentCourses() : constants.showPastCourses();
	}
	/**
	 * @return blurb displayed above unlinked courses list 
	 */
    private String getUnlinkedCoursesBlurb() {
//		return (!this.showPastCourses) ? UNLINKED_COURSES_BLURB : PAST_COURSES_BLURB;
		return (!this.showPastCourses) ? constants.unlinkedCoursesBlurb() : constants.noPastCoursesFound();
	}
	/**
	 * @return blurb displayed above unlinked courses list 
	 */
    private HTML getUnlinkedCoursesEmptyListWidget() {
		return (this.showPastCourses) ? past_empty_list : current_empty_list;
	}
}
