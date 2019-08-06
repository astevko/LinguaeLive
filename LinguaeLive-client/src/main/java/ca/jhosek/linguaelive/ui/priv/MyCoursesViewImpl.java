/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.ui.priv;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.IdentityColumn;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;

import ca.jhosek.main.client.LLConstants;
import ca.jhosek.main.client.activity.mainregion.MyCoursesActivity;
import ca.jhosek.main.client.place.MyCoursesPlace;
import ca.jhosek.main.shared.proxy.CourseProxy;

/**
 *  My Courses page - used by both students and instructors
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see MyCoursesView
 * @see MyCoursesViewImpl.ui.xml
 * @see MyCoursesPlace
 * @see MyCoursesActivity
 */
public class MyCoursesViewImpl extends Composite implements MyCoursesView {

//	private static final String VIEW_EDIT_BUTTON = "View Course";

//	private static final String NO_COURSES_FOUND = "No Courses found. Please select 'Add A Course' above.";

	private static final Logger logger = Logger.getLogger( MyCoursesViewImpl.class.getName() );

	private final DateTimeFormat dateFormatter; 

	interface MyClassesViewUiBinder extends UiBinder<Widget, MyCoursesViewImpl> {
	}
	private static MyClassesViewUiBinder uiBinder = GWT
	.create(MyClassesViewUiBinder.class);

	@UiField FlowPanel courseListPanel;
	@UiField Button addCourseButton;	// opens add a course view

	private Presenter presenter;

	private CellTable<CourseProxy> celltable;
	private SimplePager pager;
	private Column<CourseProxy, String> nameColumn;
	private Column<CourseProxy, Date> startColumn;	
	private Column<CourseProxy, Date> endColumn;

	private final LLConstants constants;

	@Inject
	public MyCoursesViewImpl(LLConstants constants) {
		this.constants = constants;
		initWidget(uiBinder.createAndBindUi(this));
		this.dateFormatter = DateTimeFormat.getFormat(constants.mdyDateFormatterPattern());
		
		// create a cell table
		celltable = new CellTable<CourseProxy>();
		// create name column
		nameColumn = new Column<CourseProxy, String>( new TextCell() ) {

			@Override
			public String getValue(CourseProxy object) {
				// 
				return object.getName();
			}
		};
		celltable.addColumn(nameColumn, constants.name());
		
		//---------------------------------------------------
		startColumn = new Column<CourseProxy, Date>( new  DateCell( dateFormatter ) ) {

			@Override
			public Date getValue(CourseProxy object) {
				// 
				return object.getStartDate();
			}

		};
		celltable.addColumn(startColumn, constants.startDate());

		//---------------------------------------------------
		endColumn = new Column<CourseProxy, Date>( new  DateCell( dateFormatter ) ) {

			@Override
			public Date getValue(CourseProxy object) {
				// 
				return object.getEndDate();
			}

		};
		celltable.addColumn(endColumn, constants.endDate());
		//---------------------------------------------------
		// action cell for capturing clicks 
		ActionCell<CourseProxy> actioncell = new ActionCell<CourseProxy> ( constants.viewEditButton(), new ActionCell.Delegate<CourseProxy>() {
			public void execute(CourseProxy object) {
				//	go to view course 
				presenter.viewCourse(object);
			}
		});		
		IdentityColumn<CourseProxy> column = new IdentityColumn<CourseProxy>( actioncell );
		celltable.addColumn( column, "" );  



		// pager
		pager = new SimplePager();
		pager.setDisplay(celltable);

	}

	/* (non-Javadoc)
	 * @see ca.jhosek.main.client.ui.priv.student.InstructorHomeView#clear()
	 */
	public void clear() {
		// 
		courseListPanel.clear();
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.main.client.ui.priv.student.InstructorHomeView#setPresenter(ca.jhosek.main.client.ui.priv.student.InstructorHomeView.Presenter)
	 */
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	public void emptyQueryResults() {
		// 		
		courseListPanel.clear();
		courseListPanel.add( new Label(constants.noCoursesFound()));		

	}

	public void showCourseList(List<CourseProxy> courses) {
		// 
		logger.info( "showCourseList( count = " + courses.size() + " )" );

		ListDataProvider<CourseProxy> dataProvider = new ListDataProvider<CourseProxy>();
		dataProvider.setList(courses);
		dataProvider.addDataDisplay(celltable);

		celltable.setRowCount(courses.size());
		celltable.setVisibleRange(0, 5);
		celltable.setRowData(0, courses);

		courseListPanel.clear();
		if ( courses.size() > 5 ) {
			courseListPanel.add(pager);
		}
		courseListPanel.add(celltable);
	}

	@UiHandler("addCourseButton")
	void onAddCourseButtonClick(ClickEvent event) {
		presenter.addACourse();
	}

}
