package ca.jhosek.linguaelive.activity.mainregion;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;

import ca.jhosek.linguaelive.domain.CurrentState;
import ca.jhosek.linguaelive.place.InstructorYourCoursePlace;
import ca.jhosek.linguaelive.place.QueryCoursesPlace;
import ca.jhosek.linguaelive.ui.priv.admin.QueryCoursesView;
import ca.jhosek.linguaelive.ui.priv.admin.QueryCoursesViewImpl;
import ca.jhosek.linguaelive.proxy.AppRequestFactory;
import ca.jhosek.linguaelive.proxy.CourseProxy;
import ca.jhosek.linguaelive.proxy.CourseRequestContext;

/**
 * query for courses, display cell table, allow for updates
 * @author copyright (C) 2011, 2012 Andrew Stevko
 * 
 *  @see QueryCoursesPlace
 *  @see QueryCoursesActivity
 *	@see QueryCoursesView
 *	@see QueryCoursesViewImpl
 *
 */

public class QueryCoursesActivity extends AbstractActivity implements QueryCoursesView.Presenter {

	private static final Logger logger = Logger.getLogger( QueryCoursesActivity.class.getName() );
	
	// Injected dependencies here
	private final QueryCoursesView view;
	private final AppRequestFactory requestFactory;
	@SuppressWarnings("unused")
	private final CurrentState currentState;
	private final PlaceController placeController;

	/**
	 * response from the last query; saved across multiple loading of this Activity
	 */
	static protected List<CourseProxy> lastQueryResponse = new ArrayList<CourseProxy>();
	

	@Inject
	public QueryCoursesActivity( 
			QueryCoursesView view,		
			AppRequestFactory requestFactory,
			CurrentState currentState, 
			PlaceController placeController
			) {
		super();
		this.view = view;
		this.requestFactory = requestFactory;
		this.currentState = currentState;
		this.placeController = placeController;
		view.setPresenter(this);
	}

	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(view);

		view.showCourseList( lastQueryResponse );
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.priv.admin.QueryCoursesView.Presenter#queryCourses()
	 */
	public void queryCourses() {
		logger.info( "executing queryCourses()");
		CourseRequestContext courseContext;
		courseContext = requestFactory.courseRequest();
//		// flush object to persistent storage
//		courseContext.listAll().with("owner").fire( 
//				new Receiver<List<CourseProxy>>() {
//
//					@Override
//					public void onSuccess(List<CourseProxy> response) {
//						view.clear();
//						// 
//						// show all courses in the list
//						if( response.isEmpty() ) {
//							logger.info( "no results returned");
//							view.emptyQueryResults();
//						} else {
//							view.showCourseList( response );
//						}
//						
//					}
//		
//		});
		
		// fetch courses that match these.
		courseContext
		.listTheseCourses(view.getTargetLanguage(), view.getExpertLanguage(), view.getWithCourseLinks(), view.getCurrentCourses())
		.with("owner")
		.fire( 
				new Receiver<List<CourseProxy>>() {

					@Override
					public void onSuccess(List<CourseProxy> response) {
						view.clear();
						// 
						// show courses in the list
						if( response.isEmpty() ) {
							logger.info( "no results returned");
							view.emptyQueryResults();
						} else {
							lastQueryResponse = response;
							view.showCourseList( response );
						}
					}
		});
		
	}

	@Override
	public void selectCourse(CourseProxy course) {
		placeController.goTo(new InstructorYourCoursePlace(course));
	}
}
