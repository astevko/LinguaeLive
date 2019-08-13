/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.ui.anon;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;

import ca.jhosek.linguaelive.LLConstants;
// import ca.jhosek.linguaelive.activity.mainregion.CourseBrowseActivity;
// import ca.jhosek.linguaelive.place.CourseBrowsePlace;
import ca.jhosek.linguaelive.proxy.CourseProxy;

/**
 * course browser
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see CourseBrowseView
 * @see CourseBrowseActivity
 * @see CourseBrowsePlace
 *
 */
public class CourseBrowseViewImpl extends Composite implements IsWidget, CourseBrowseView {
	// logger boilerplate
	private static final Logger logger = Logger.getLogger(CourseBrowseViewImpl.class.getName());

	private final DateTimeFormat dateFormatter; // = DateTimeFormat.getFormat( "dd MMM yyyy"); 

	interface CourseBrowseViewImplUiBinder extends UiBinder<Widget, CourseBrowseViewImpl> {
	}
	private static CourseBrowseViewImplUiBinder uiBinder = GWT.create(CourseBrowseViewImplUiBinder.class);

	@UiField HTMLPanel browserPanel;
	
	@SuppressWarnings("unused")
	private Presenter presenter;
	
	final private CellTable<CourseProxy> celltable;
	final private SimplePager pager;
	final private Column<CourseProxy, String> nameColumn;
	final private Column<CourseProxy, Date>   startColumn;	
	final private Column<CourseProxy, String> targetColumn;
	final private Column<CourseProxy, String> expertColumn;
	final private Column<CourseProxy, String> schoolColumn;
	final private Column<CourseProxy, String> locationColumn;

	@SuppressWarnings("unused")
	private final LLConstants constants;

	
	@Inject
	public CourseBrowseViewImpl(LLConstants constants) {
		this.constants = constants;
		initWidget(uiBinder.createAndBindUi(this));

//		final String dateFormatterPattern = "dd MMM yyyy";
		dateFormatter = DateTimeFormat.getFormat( constants.mdyDateFormatterPattern());
		
		// create a cell table
		celltable = new CellTable<CourseProxy>();
		//----------------------------
		// create name column
		nameColumn = new Column<CourseProxy, String>( new TextCell() ) {

			@Override
			public String getValue(CourseProxy course) {
				// 
				return course.getName();
			}
		};
		celltable.addColumn(nameColumn, constants.name());
		
		//---------------------------------------------------
		startColumn = new Column<CourseProxy, Date>( new  DateCell( dateFormatter ) ) {

			@Override
			public Date getValue(CourseProxy course) {
				// 
				return course.getStartDate();
			}

		};
		celltable.addColumn(startColumn, constants.startDate());
		//----------------------------
		// create target column
		targetColumn = new Column<CourseProxy, String>( new TextCell() ) {

			@Override
			public String getValue(CourseProxy course) {
				// 
				return course.getTargetLanguage().toString();
			}
		};
		celltable.addColumn(targetColumn, constants.targetLanguage());
		//----------------------------
		// create expert column
		expertColumn = new Column<CourseProxy, String>( new TextCell() ) {

			@Override
			public String getValue(CourseProxy course) {
				// 
				return course.getExpertLanguage().toString();
			}
		};
		celltable.addColumn(expertColumn, constants.expertLanguage());
		//----------------------------
		// create school column
		schoolColumn = new Column<CourseProxy, String>( new TextCell() ) {

			@Override
			public String getValue(CourseProxy course) {
				// 
				return course.getSchoolName();
			}
		};
		celltable.addColumn(schoolColumn, constants.school());
		//----------------------------
		// create location column
		locationColumn = new Column<CourseProxy, String>( new TextCell() ) {

			@Override
			public String getValue(CourseProxy course) {
				// 
				return course.getOwner().getLocation();
			}
		};
		celltable.addColumn(locationColumn, constants.location());
		

	    // Add a selection model to handle user selection.
	    final SingleSelectionModel<CourseProxy> selectionModel = new SingleSelectionModel<CourseProxy>();
	    celltable.setSelectionModel(selectionModel);
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	      public void onSelectionChange(SelectionChangeEvent event) {
	    	  CourseProxy selected = selectionModel.getSelectedObject();
	        if (selected != null) {
	          Window.alert("You selected: " + selected.getDescription());
	        }
	      }
	    });
	    
		// pager
		pager = new SimplePager();
		pager.setDisplay(celltable);
	
	}

	public void showCourseList(List<CourseProxy> courses) {
		// 
		logger.info( "showCourseList( count = " + courses.size() + " )" );

		ListDataProvider<CourseProxy> dataProvider = new ListDataProvider<CourseProxy>();
		dataProvider.setList(courses);
		dataProvider.addDataDisplay(celltable);

		celltable.setRowCount(courses.size());
		celltable.setVisibleRange(0, 15);
		celltable.setRowData(0, courses);

		browserPanel.clear();
		if ( courses.size() > 15 ) {
			browserPanel.add(pager);
		}
		browserPanel.add(celltable);
	}
	
	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.anon.ContactUsView#clear()
	 */
	public void clear() {
		//
		browserPanel.clear();
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.anon.ContactUsView#setPresenter(ca.jhosek.linguaelive.ui.anon.ContactUsView.Presenter)
	 */
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;

	}
}
