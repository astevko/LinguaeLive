package ca.jhosek.linguaelive.activity.mainregion;

import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;

import ca.jhosek.linguaelive.domain.CurrentState;
import ca.jhosek.linguaelive.ui.anon.CourseBrowseView;
import ca.jhosek.linguaelive.ui.anon.CourseBrowseViewImpl;
import ca.jhosek.linguaelive.proxy.AppRequestFactory;
import ca.jhosek.linguaelive.proxy.CourseProxy;
import ca.jhosek.linguaelive.proxy.CourseRequestContext;

/**
 * @author copyright (C) 2011 Andrew Stevko
 * @see CourseBrowseView
 * @see CourseBrowseViewImpl
 */
public class CourseBrowseActivity extends AbstractActivity implements CourseBrowseView.Presenter {

	private static final Logger logger = Logger.getLogger( CourseBrowseActivity.class.getName() );


	private final CourseBrowseView view;
	@SuppressWarnings("unused")
	private final PlaceController placeController;
	@SuppressWarnings("unused")
	private final CurrentState currentState;
	private final AppRequestFactory requestFactory;


	@Inject
	public CourseBrowseActivity( CourseBrowseView view, 
			PlaceController placeController, 
			CurrentState currentState, 
			AppRequestFactory requestFactory ) {
		super();
		this.view = view;
		this.placeController = placeController;
		this.currentState = currentState;
		this.requestFactory = requestFactory;
		view.setPresenter(this);
	}


	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		
		panel.setWidget(view);

		CourseRequestContext courseContext = requestFactory.courseRequest();
		// set query
		Request<List<CourseProxy>> request = courseContext.listAll().with("owner");
		// execute query
		request.fire( new Receiver<List<CourseProxy>>(){

			@Override
			public void onSuccess(List<CourseProxy> results) {
				// 
				logger.info("Showing " + results.size() + " courses" 	);
				view.showCourseList( results );
			}
			
		});
		
	}

//	/* (non-Javadoc)
//	 * @see ca.jhosek.linguaelive.ui.anon.NewStudentPanel.Presenter#cancel()
//	 */
//	@Override
//	public void cancel() {
//		logger.info("cancel()" );
//		// 
////		placeController.goTo( currentState.getHomePlace() );
//	}
//
//	/* (non-Javadoc)
//	 * @see ca.jhosek.linguaelive.ui.anon.ContactUsView.Presenter#submit()
//	 */
//	@Override
//	public void submit() {
//		// 
//		logger.info("submit()");
//		
//		RequestContext context = contactUsDriver.flush();
//		
////		if ( !context.isChanged() ) {
////			Window.alert( "Please fill out the form before submitting it.");
////			return;
////		}
//		if ( contactUsDriver.hasErrors() ) {
//			Window.alert("All fields are required. Please complete and submit again.");
//			return;
//		}
//		logger.info("submitting contact us information" );
//		context.fire();
//	}

}
