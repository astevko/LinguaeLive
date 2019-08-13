/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.ui.priv.admin;

import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;

import ca.jhosek.linguaelive.LLConstants;
import ca.jhosek.linguaelive.place.QueryCoursesPlace;
// import ca.jhosek.linguaelive.domain.Course;
import ca.jhosek.linguaelive.LanguageType;
import ca.jhosek.linguaelive.proxy.CourseProxy;

/**
 *  Admin query Courses view
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see QueryCoursesView
 * @see QueryCoursesPlace
 * Course
 *
 */

public class QueryCoursesViewImpl extends Composite implements QueryCoursesView {

	interface QueryCoursesUiBinder extends UiBinder<Widget, QueryCoursesViewImpl> {
	}   

	/**
	 * what happens when the user selects the row
	 * @author copyright (C) 2011 Andrew Stevko
	 *
	 */
	final class SelectionChangeHandler implements
			SelectionChangeEvent.Handler {
		final SingleSelectionModel<CourseProxy> selectionModel;

		SelectionChangeHandler(
				SingleSelectionModel<CourseProxy> selectionModel) {
			this.selectionModel = selectionModel;
		}

		public void onSelectionChange(SelectionChangeEvent event) {
			// 
			CourseProxy course = selectionModel.getSelectedObject();
			presenter.selectCourse(course);
		}
	}
	
	private static final DateTimeFormat fmt = DateTimeFormat.getFormat("MM/dd/yyyy");
	
	private static final Logger logger = Logger.getLogger( QueryCoursesViewImpl.class.getName() );

	private static String selectedDate = "current";		// always look forward
	private static LanguageType selectedExpertLanguage;	// can be null
	private static String selectedlink = "";
	/**
	 * can be null == no selection
	 */
	private static LanguageType selectedTargetLanguage;	// can be null
	private static QueryCoursesUiBinder uiBinder = GWT
	.create(QueryCoursesUiBinder.class);
	private CellTable<CourseProxy> celltable;
	@UiField FlowPanel courseListPanel; 
	/**
	 * { "", "current", "past" }
	 */
	@UiField ListBox dateFilter; 
	
//	private Column<CourseProxy, String> descriptionColumn;	

	private Column<CourseProxy, String> endColumn;
	
	private Column<CourseProxy, Number> estimatedMemberSize;
	
	@UiField Button executeQueryButton;
	
	private Column<CourseProxy, String> expertLanguageColumn;
	/**
	 * { "", "..." }
	 * @see LanguageType 
	 */
	@UiField ListBox languageExpertFilter;
	/**
	 * { "", "..." }
	 * @see LanguageType 
	 */
	@UiField ListBox languageTargetFilter;
	/**
	 * { "", "has", "not" }
	 */
	@UiField ListBox linkFilter;
	private Column<CourseProxy, String> nameColumn;	
	private SimplePager pager;
	private Presenter presenter;
	private Column<CourseProxy, String> startColumn;

	
	private Column<CourseProxy, String> targetLanguageColumn;

	private final LLConstants constants;
	
	@Inject
	public QueryCoursesViewImpl(LLConstants constants) {
		this.constants = constants;
		initWidget(uiBinder.createAndBindUi(this));
		
		// load target language filter listbox with LanguageTypes
		loadLanguageFilter( this.languageTargetFilter, selectedTargetLanguage, "Target");
		loadLanguageFilter( this.languageExpertFilter, selectedExpertLanguage, "Expert");
		resetForm();
		
						
		//----------------------------------------
		// create a cell table
		celltable = new CellTable<CourseProxy>();
		setupTable();
		
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.priv.student.AdminHomeView#clear()
	 */
	public void clear() {
		// 
		courseListPanel.clear();
//		courseProfilePanel.setVisible(false);
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.priv.admin.QueryCoursesView#emptyQueryResults()
	 */
	public void emptyQueryResults() {
		//
		courseListPanel.clear();
		courseListPanel.add( new Label("No Courses found."));		
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.priv.admin.QueryCoursesView#getCurrentCourses()
	 */
	@Override
	public Boolean getCurrentCourses() {
		/**
		 * { "", "current", "past" }
		 */
		QueryCoursesViewImpl.selectedDate = dateFilter.getValue(dateFilter.getSelectedIndex());
		if (selectedDate.trim().isEmpty()) {
			return null;
		}
		return QueryCoursesViewImpl.selectedDate.equals(CURRENT_COURSES);
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.priv.admin.QueryCoursesView#getExpertLanguage()
	 */
	@Override
	public LanguageType getExpertLanguage() {
		/**
		 * { "", "..." }
		 * @see LanguageType 
		 */
		final String value = languageExpertFilter.getValue(languageExpertFilter.getSelectedIndex());
		if (value.trim().isEmpty()) {
			QueryCoursesViewImpl.selectedExpertLanguage = null;
			return null;
		}
		return (QueryCoursesViewImpl.selectedExpertLanguage = LanguageType.valueOf(value));
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.priv.admin.QueryCoursesView#getTargetLanguage()
	 */
	@Override
	public LanguageType getTargetLanguage() {
		/**
		 * { "", "..." }
		 * @see LanguageType 
		 */
		final String value = languageTargetFilter.getValue(languageTargetFilter.getSelectedIndex());
		if (value.trim().isEmpty()) {
			QueryCoursesViewImpl.selectedTargetLanguage = null;
			return null;
		}
		return (QueryCoursesViewImpl.selectedTargetLanguage = LanguageType.valueOf(value));
	}

/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.priv.admin.QueryCoursesView#getWithCourseLinks()
	 */
	@Override
	public Boolean getWithCourseLinks() {
		/**
		 * { "", "has", "not" }
		 */
		QueryCoursesViewImpl.selectedlink = linkFilter.getValue(linkFilter.getSelectedIndex());
		if (QueryCoursesViewImpl.selectedlink.trim().isEmpty()) {
			return null;
		}  
		return QueryCoursesViewImpl.selectedlink.equals(HAS_COURSE_LINKS);
	}
	
//	/* (non-Javadoc)
//	 * @see ca.jhosek.linguaelive.ui.priv.admin.QueryCoursesView#getAdminCourseViewer()
//	 */
//	@Override
//	public Editor<CourseProxy> getAdminCourseViewer() {
//		// 
//		return courseProfilePanel;
//	}


//	/* 
//	 * show course information
//	 * 
//	 * (non-Javadoc)
//	 * @see ca.jhosek.linguaelive.ui.priv.admin.QueryCoursesView#showCourse(ca.jhosek.linguaelive.CourseProxy)
//	 */
//	@Override
//	public void showCourse(CourseProxy course) {
//		
//		// 
//		timezoneOffset.setText( course.getTimezoneOffset().toString() + " minutes" );
//		currentCourseTime.setText( getCourseLocalTime( course.getTimezoneOffset() ).toString() );
//		// 
//		courseProfilePanel.setVisible(true);
//	}
//
//	/**
//	 * @param timezoneOffset
//	 * @return
//	 */
//	@SuppressWarnings("deprecation")
//	private Date getCourseLocalTime(Integer timezoneOffset ) {
//		// 
//		Date now = new Date();
//		
//	 	int thisCoursesOffset = now.getTimezoneOffset();
//	 	int timezoneDifference = thisCoursesOffset - timezoneOffset;
//	 	// set the minutes to handle the timezone offset
//	 	now.setMinutes(timezoneDifference);
//	 			
//		return now;
//	}


	/**
	 * @see LanguageType
	 * 
	 * @param languageFilter
	 * @param preselected
	 * @param clickme
	 */
	private void loadLanguageFilter(ListBox filter,
			LanguageType preselected, String clickme) {
//	private void loadLanguageFilter(ListBox filter, LanguageType preselected, String clickme ) {

		// add the language to the name
		filter.addItem(clickme, "");
		int i = 1;
		for (LanguageType lang:  LanguageType.values()) {
			// add the language to the name
			filter.addItem(lang.name(), lang.name());
			if (lang.equals(preselected)) {
				filter.setSelectedIndex(i);
			}
			i++;
		}
		
	}
	
	@UiHandler("executeQueryButton")
	void onExecuteQueryButtonClick(ClickEvent event) {
		presenter.queryCourses();
	}


	/**
	 * reset the form controls to last query 
	 */
	private void resetForm() {
		if (selectedDate.isEmpty()) {
			dateFilter.setSelectedIndex(0);
		} else if (selectedDate.equals(CURRENT_COURSES)) {
			dateFilter.setSelectedIndex(1);
		} else { // PAST_COURSES
			dateFilter.setSelectedIndex(2);
		}
		
		if (selectedlink.isEmpty()) {
			linkFilter.setSelectedIndex(0);
		} else if (selectedlink.equals(HAS_COURSE_LINKS)) {
			linkFilter.setSelectedIndex(1);
		} else { // NO_COURSE_LINKS
			linkFilter.setSelectedIndex(2);
		}
		
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.priv.student.AdminHomeView#setPresenter(ca.jhosek.linguaelive.ui.priv.student.AdminHomeView.Presenter)
	 */
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	private void setupTable() {
		//----------------------------------------
		// create School column
		final Column<CourseProxy, String> schoolColumn 
			= new Column<CourseProxy, String>( new TextCell() ) {
			
			@Override
			public String getValue(CourseProxy object) {
				// 
				return object.getOwner().getSchool();
			}
		};
		celltable.addColumn(schoolColumn, constants.school());
		//----------------------------------------
		// create name column
		nameColumn = new Column<CourseProxy, String>( new TextCell() ) {
			
			@Override
			public String getValue(CourseProxy object) {
				// 
				return object.getName();
			}
		};
		celltable.addColumn(nameColumn, constants.name());
		
/*		//----------------------------------------
		// create description column
		descriptionColumn = new Column<CourseProxy, String>( new TextCell() ) {
			
			@Override
			public String getValue(CourseProxy object) {
				// 
				return object.getDescription();
			}
		};
//		descriptionColumn.setFieldUpdater( presenter.getDescriptionFieldUpdater() );		
		celltable.addColumn(descriptionColumn, "Description");
*/		
		//----------------------------------------
		// create Instrcutor name column
		final Column<CourseProxy, String> ownerNameColumn 
			= new Column<CourseProxy, String>( new TextCell() ) {
			
			@Override
			public String getValue(CourseProxy object) {
				// 
				return object.getOwner().getFirstName() + " " + object.getOwner().getLastName();
			}
		};
		celltable.addColumn(ownerNameColumn, constants.instructor());
		
		//----------------------------------------
		// create Instructor name column
		final Column<CourseProxy, String> ownerEmailColumn 
			= new Column<CourseProxy, String>( new TextCell() ) {
			
			@Override
			public String getValue(CourseProxy object) {
				// 
				return object.getOwner().getEmailAddress();
			}
		};
		celltable.addColumn(ownerEmailColumn, constants.emailAddress());
		//----------------------------------------
		// create size column column
		estimatedMemberSize = new Column<CourseProxy, Number>( new NumberCell() ) {
			
			@Override
			public Number getValue(CourseProxy object) {
				// 
				return object.getEstimatedMemberSize();
			}
		};
//		descriptionColumn.setFieldUpdater( presenter.getDescriptionFieldUpdater() );		
		celltable.addColumn(estimatedMemberSize, constants.estMembership());
		
		//----------------------------------------
		startColumn = new Column<CourseProxy, String>( new  TextCell() ) {

			@Override
			public String getValue(CourseProxy object) {
				// 
				return fmt.format(object.getStartDate());
			}
			
		};
//		startColumn.setFieldUpdater( presenter.getStartFieldUpdater());
		celltable.addColumn(startColumn, constants.startDate());

		//----------------------------------------
		endColumn = new Column<CourseProxy, String>( new  TextCell() ) {

			@Override
			public String getValue(CourseProxy object) {
				// 
				return fmt.format(object.getEndDate());
			}
			
		};
//		endColumn.setFieldUpdater( presenter.getEndFieldUpdater());
		celltable.addColumn(endColumn, constants.endDate());

		//----------------------------------------
		expertLanguageColumn = new Column<CourseProxy, String>( new  TextCell() ) {

			@Override
			public String getValue(CourseProxy object) {
				// 
				LanguageType lang = object.getExpertLanguage();
				return lang==null ? "none" : lang.name();
			}
			
		};
//		expertLanguageColumn.setFieldUpdater( presenter.getExpertLanguageFieldUpdater());
		celltable.addColumn(expertLanguageColumn, constants.expertLanguage());

		//----------------------------------------
		targetLanguageColumn = new Column<CourseProxy, String>( new  TextCell() ) {

			@Override
			public String getValue(CourseProxy object) {
				// 
				LanguageType lang = object.getTargetLanguage();
				return lang==null ? "none" : lang.name();
			}
			
		};
//		targetLanguageColumn.setFieldUpdater( presenter.getExpertLanguageFieldUpdater());
		celltable.addColumn(targetLanguageColumn, constants.targetLanguage());
		
		
		//----------------------------
		// invite code column
		final Column<CourseProxy, String> codeColumn = new Column<CourseProxy, String>( new ClickableTextCell() ) {
			@Override
			public String getValue(CourseProxy course) {
				// 
				return course.getInviteCode();
			}
			
			@Override
			public void onBrowserEvent(Context context, Element elem,
					CourseProxy object, NativeEvent event) {
				//
				super.onBrowserEvent(context, elem, object, event);
				Window.open("http://www.LinguaeLive.ca/index.html?#signupstudent:" + object.getInviteCode(),  "Invite", "");
			}
		};
		//  invite code column 
		celltable.addColumn(codeColumn, constants.inviteCode());
		
		// rig the click hooks
		final SingleSelectionModel<CourseProxy> selectionModel = new SingleSelectionModel<CourseProxy>();
		celltable.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler( new SelectionChangeHandler(selectionModel) );
		
		//-----------------------------
		pager = new SimplePager();
		pager.setDisplay(celltable);
	}

	//	/* (non-Javadoc)
//	 * @see ca.jhosek.linguaelive.ui.priv.admin.QueryCoursesView#addQueryResult(java.lang.String, java.lang.Long)
//	 */
//	@Override
//	public void addQueryResult(final String name, final Long id) {
//		// make safe name 
//		SafeHtmlBuilder nameBuilder = new SafeHtmlBuilder();
//		nameBuilder.appendEscaped(name);
//		
//		HTML nameLabel = new HTML(nameBuilder.toSafeHtml().asString());
//		nameLabel.addClickHandler( new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				// click will show course info in the right panel 
//				presenter.showCourse(id);
//			}
//		});
//		courseListPanel.add(nameLabel);
//		courseListPanel.add( new HTML("<br />"));
//	}
	public void showCourseList( List<CourseProxy> courses ){
		logger.info( "showCourseList( count = " + courses.size() + " )" );
		
		ListDataProvider<CourseProxy> dataProvider = new ListDataProvider<CourseProxy>();
		dataProvider.setList(courses);
		dataProvider.addDataDisplay(celltable);
		
		celltable.setRowCount(courses.size());
		celltable.setVisibleRange(0, QUERY_PAGE_SIZE);
		celltable.setRowData(0, courses);

		courseListPanel.clear();	
		courseListPanel.add(pager);
		courseListPanel.add(celltable);
	}
	
//	/**
//	 * used for display of the list of Courses on the left side cell list
//	 * 
//	 * @author copyright (C) 2011 Andrew Stevko
//	 *
//	 */
//	private static class CourseProxyCell extends AbstractCell<CourseProxy> {
//
//		/* (non-Javadoc)
//		 * @see com.google.gwt.cell.client.AbstractCell#render(com.google.gwt.cell.client.Cell.Context, java.lang.Object, com.google.gwt.safehtml.shared.SafeHtmlBuilder)
//		 */
//		@Override
//		public void render(com.google.gwt.cell.client.Cell.Context context,
//				CourseProxy course, SafeHtmlBuilder sb) {
//			// 
//			if ( course == null ) 
//				return;
//			// concat the display
//			sb.appendHtmlConstant("<div class=\"{style.formPrompt}\" >" );
//			sb.append( SafeHtmlUtils.fromString(course.getName() + " " + course.getDescription()) );
//			sb.appendHtmlConstant("</div>" );						
//		}
//		
//	}
//	
//	
//	
//	/**
//	 * @return the courseProfilePanel
//	 */
//	@Override
//	public AdminCourseView getCoursePanel() {
//		return coursePanel;
//	}


}
