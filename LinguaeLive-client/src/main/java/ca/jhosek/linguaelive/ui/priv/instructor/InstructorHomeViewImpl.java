/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.ui.priv.instructor;

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
import ca.jhosek.linguaelive.activity.mainregion.InstructorHomeActivity;
import ca.jhosek.linguaelive.place.InstructorHomePlace;
import ca.jhosek.linguaelive.ui.GroupingHandlerRegistration;
import ca.jhosek.linguaelive.proxy.CourseLinkProxy;
import ca.jhosek.linguaelive.proxy.CourseProxy;

/**
 *  Instructor user home page 
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see InstructorHomeActivity
 * @see InstructorHomePlace
 * @see InstructorHomeView
 */
public class InstructorHomeViewImpl extends Composite implements InstructorHomeView {
	
	// logger boilerplate
	private static final Logger logger = Logger.getLogger( InstructorHomeViewImpl.class.getName());
	
	private static final String LIST_PROMPT_DIV_W_STYLE = "<div style=\"font-weight: bold;cursor: pointer;color: #d35e46;\" >";
	
	
	private static final String START_DATE_FORMAT = "MMM d, yyyy";
	private static DateTimeFormat mdyDateFormat = DateTimeFormat.getFormat(  START_DATE_FORMAT);  

	
	
	interface HomeViewUiBinder extends UiBinder<Widget, InstructorHomeViewImpl> {
	}
	private static HomeViewUiBinder uiBinder = GWT.create(HomeViewUiBinder.class);

	
	/**
	 * panel for holding Course Buttons
	 */
	@UiField HorizontalPanel coursesPanel;
	
	//================
	// Pending course link invites
	@UiField FlowPanel pendingLinkedCoursesPanel;
	private final CellList<CourseLinkProxy> pendingLinkedCoursesCList;
	private final SimplePager pendingLinkedCoursesPager;	
	private ListDataProvider<CourseLinkProxy> pendingLinkCourseDataProvider;
	
	//================
	// Open course link invites
	@UiField FlowPanel openLinkedCoursesPanel;
	private final CellList<CourseLinkProxy> openLinkedCoursesCList;
	private final SimplePager openLinkedCoursesPager;	
	private ListDataProvider<CourseLinkProxy> openLinkCourseDataProvider;
	
	
	private Presenter presenter;

	private final LLConstants constants;

	/**
	 * ClientBundle resources
	 */
	private StylesCssResource css;

	@Inject
	public InstructorHomeViewImpl(LLConstants constants, AppResources resources) {
		this.constants = constants;
		this.css = resources.css();
		initWidget(uiBinder.createAndBindUi(this));

		{
			//----------------------------
			// create Linked Courses celllist etc
			PendingLinkedCourseCell linkedCourseCell = new PendingLinkedCourseCell();
			pendingLinkedCoursesCList = new CellList<CourseLinkProxy>(linkedCourseCell);
			pendingLinkedCoursesCList.setEmptyListWidget(	new HTML(constants.noPendingLinkedCoursesFound()) );
			final SingleSelectionModel<CourseLinkProxy> linkedCourseSelectionModel = new SingleSelectionModel<CourseLinkProxy>();
			pendingLinkedCoursesCList.setSelectionModel(linkedCourseSelectionModel);
			linkedCourseSelectionModel.addSelectionChangeHandler( new PendingLinkedCourseSelectionHandler(linkedCourseSelectionModel));
			pendingLinkedCoursesPager= new SimplePager();
			pendingLinkedCoursesPager.setDisplay(pendingLinkedCoursesCList);
		}

		{
			//----------------------------
			// create Linked Courses celllist etc
			OpenLinkedCourseCell linkedCourseCell = new OpenLinkedCourseCell();
			openLinkedCoursesCList = new CellList<CourseLinkProxy>(linkedCourseCell);
			openLinkedCoursesCList.setEmptyListWidget(	new HTML(constants.noOpenLinkedCoursesFound()) );
			final SingleSelectionModel<CourseLinkProxy> linkedCourseSelectionModel = new SingleSelectionModel<CourseLinkProxy>();
			openLinkedCoursesCList.setSelectionModel(linkedCourseSelectionModel);
			linkedCourseSelectionModel.addSelectionChangeHandler( new OpenLinkedCourseSelectionHandler(linkedCourseSelectionModel));
			openLinkedCoursesPager= new SimplePager();
			openLinkedCoursesPager.setDisplay(openLinkedCoursesCList);
		}
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.priv.student.InstructorHomeView#clear()
	 */
	public void clear() {
		//
		coursesPanel.clear();
		pendingLinkCourseDataProvider.getList().clear();
		openLinkCourseDataProvider.getList().clear();

	}

	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.priv.student.InstructorHomeView#setPresenter(ca.jhosek.linguaelive.ui.priv.student.InstructorHomeView.Presenter)
	 */
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	/**
	 * 
	 * display a single linked course 
	 * @author copyright (C) 2011 Andrew Stevko
	 *
	 */
	private static class PendingLinkedCourseCell extends AbstractCell<CourseLinkProxy> {

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				CourseLinkProxy courseLink, SafeHtmlBuilder sb) {
			// 
			if( courseLink==null	) return;
			sb.appendHtmlConstant(LIST_PROMPT_DIV_W_STYLE );
			sb.append( SafeHtmlUtils.fromString( courseLink.getCourseB().getSchoolName() ));
			sb.append( ' ' );			
			sb.append( SafeHtmlUtils.fromString( courseLink.getCourseB().getName() ));
			sb.append( ' ' );			
			sb.append( SafeHtmlUtils.fromTrustedString( getStatusBlurb( courseLink ) ) );
			sb.appendHtmlConstant("</div>" );						
		}

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

	/**
	 * what happens when the linked course is selected
	 * @author copyright (C) 2011 Andrew Stevko
	 *
	 */
	private final class PendingLinkedCourseSelectionHandler implements
	SelectionChangeEvent.Handler {
		private final SingleSelectionModel<CourseLinkProxy> selectionModel;

		private PendingLinkedCourseSelectionHandler(
				SingleSelectionModel<CourseLinkProxy> selectionModel) {
			this.selectionModel = selectionModel;
		}

		public void onSelectionChange(SelectionChangeEvent event) {
			// 
			CourseLinkProxy courseLink = selectionModel.getSelectedObject();
			if( courseLink != null ) {
				presenter.goToPendingCourseLinkView(courseLink);
			}

		}
	}

	public void showPendingLinkedCourses(List<CourseLinkProxy> linkedCourses) {
		int displaySize = 15;
		pendingLinkedCoursesPanel.clear();

		if( linkedCourses == null ) {
			logger.info( "showLinkedCourses() with NULL courses" );
			pendingLinkedCoursesPanel.add( new HTML(constants.loadingPendingLinkedCourses()));
		} else {
			logger.info( "showLinkedCourses() with " + linkedCourses.size() +
			" courses" );
			pendingLinkCourseDataProvider = new ListDataProvider<CourseLinkProxy>(linkedCourses);
			pendingLinkCourseDataProvider.addDataDisplay(pendingLinkedCoursesCList);

			pendingLinkedCoursesCList.setRowCount( linkedCourses.size());
			pendingLinkedCoursesCList.setVisibleRange(0, displaySize);
			pendingLinkedCoursesCList.setRowData(0, linkedCourses);

			pendingLinkedCoursesPanel.add(pendingLinkedCoursesCList);
			if ( linkedCourses.size() > displaySize ) {
				pendingLinkedCoursesPanel.add(pendingLinkedCoursesPager);
			}
		}
		
	}

	
	/**
	 * 
	 * display a single linked course 
	 * @author copyright (C) 2011 Andrew Stevko
	 *
	 */
	private static class OpenLinkedCourseCell extends AbstractCell<CourseLinkProxy> {

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				CourseLinkProxy courseLink, SafeHtmlBuilder sb) {
			// 
			if( courseLink==null	) return;
			sb.appendHtmlConstant(LIST_PROMPT_DIV_W_STYLE );
			sb.append( SafeHtmlUtils.fromString( courseLink.getCourseA().getSchoolName() ));
			sb.append( ' ' );			
			sb.append( SafeHtmlUtils.fromString( courseLink.getCourseA().getName() ));
			sb.append( ' ' );			
			sb.append( SafeHtmlUtils.fromTrustedString( getStatusBlurb( courseLink ) ) );
			sb.appendHtmlConstant("</div>" );						
		}

	}
	
	/**
	 * what happens when the linked course is selected
	 * @author copyright (C) 2011 Andrew Stevko
	 *
	 */
	private final class OpenLinkedCourseSelectionHandler implements
	SelectionChangeEvent.Handler {
		private final SingleSelectionModel<CourseLinkProxy> selectionModel;

		private OpenLinkedCourseSelectionHandler(
				SingleSelectionModel<CourseLinkProxy> selectionModel) {
			this.selectionModel = selectionModel;
		}

		public void onSelectionChange(SelectionChangeEvent event) {
			// 
			CourseLinkProxy courseLink = selectionModel.getSelectedObject();
			if( courseLink != null ) {
				presenter.goToOpenCourseLinkView(courseLink);
			}

		}
	}

	public void showOpenLinkedCourses(List<CourseLinkProxy> linkedCourses) {
		int displaySize = 15;
		openLinkedCoursesPanel.clear();

		if( linkedCourses == null ) {
			logger.info( "showOpenLinkedCourses() with NULL courses" );
			openLinkedCoursesPanel.add( new HTML(constants.loadingOpenLinkedCourses()));
		} else {
			logger.info( "showLinkedCourses() with " + linkedCourses.size() +
			" courses" );
			openLinkCourseDataProvider = new ListDataProvider<CourseLinkProxy>(linkedCourses);
			openLinkCourseDataProvider.addDataDisplay(openLinkedCoursesCList);

			openLinkedCoursesCList.setRowCount( linkedCourses.size());
			openLinkedCoursesCList.setVisibleRange(0, displaySize);
			openLinkedCoursesCList.setRowData(0, linkedCourses);

			openLinkedCoursesPanel.add(openLinkedCoursesCList);
			if ( linkedCourses.size() > displaySize ) {
				openLinkedCoursesPanel.add(openLinkedCoursesPager);
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
	 * Base button 
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
