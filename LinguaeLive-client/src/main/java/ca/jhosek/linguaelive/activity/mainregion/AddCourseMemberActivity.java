package ca.jhosek.linguaelive.activity.mainregion;

import java.util.Set;
import java.util.logging.Logger;

import javax.validation.ConstraintViolation;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

import ca.jhosek.linguaelive.domain.CurrentState;
import ca.jhosek.linguaelive.place.AddCourseMemberPlace;
import ca.jhosek.linguaelive.place.StudentYourCoursePlace;
import ca.jhosek.linguaelive.ui.priv.student.AddCourseMemberView;
import ca.jhosek.linguaelive.ui.priv.student.AddCourseMemberViewImpl;
import ca.jhosek.linguaelive.proxy.AppRequestFactory;
import ca.jhosek.linguaelive.proxy.MemberProxy;
import ca.jhosek.linguaelive.proxy.MemberRequestContext;

/**
 * add a new course member to the current user
 * or cancel to home page
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see AddCourseMemberView
 * @see AddCourseMemberViewImpl
 * @see AddCourseMemberPlace
 * 
 */
public class AddCourseMemberActivity extends AbstractActivity implements AddCourseMemberView.Presenter {

	private static final Logger logger = Logger.getLogger( AddCourseMemberActivity.class.getName() );

	private final AddCourseMemberView view;
	private final PlaceController placeController;
	private final AppRequestFactory requestFactory;
	private final CurrentState currentState;


	@Inject
	public AddCourseMemberActivity( 
			AddCourseMemberView view, 
			PlaceController placeController,
			AppRequestFactory requestFactory,
			CurrentState currentState,
			EventBus eventBus
	) {
		super();
		this.view = view;
		this.placeController = placeController;
		this.requestFactory = requestFactory;
		this.currentState = currentState;
		view.setPresenter(this);
	}

	public void start(AcceptsOneWidget panel, EventBus eventBus) {

		panel.setWidget(view);

	}

	/***
	 * goto home page on cancel
	 * @see ca.jhosek.linguaelive.ui.priv.instructor.AddEditCourseView.Presenter#cancel()
	 */
	public void cancel() {
		// 
		placeController.goTo( currentState.getHomePlace() );
	}

	public void saveCourseMember( String inviteCode ) {
		//
		logger.info("saveCourseMember()" );
		
		MemberRequestContext addMemberContext = requestFactory.memberRequest();
		Request<MemberProxy> req =  addMemberContext.joinInviteCode( currentState.getLoggedInUser(), inviteCode );
		req.with("course","user");
		req.fire( new Receiver<MemberProxy>() {

			@Override
			public void onSuccess(MemberProxy response) {
				// 
				if( response == null ){
					// failed to create course
					Window.alert("Error processing invite code. Did you get it right?" );
					return;
				}
				logger.info("joinInviteCode success - id: " + response.getId().toString() );				
				placeController.goTo( new StudentYourCoursePlace( response.getCourse() ));
			}

			/* (non-Javadoc)
			 * @see com.google.web.bindery.requestfactory.shared.Receiver#onFailure(com.google.web.bindery.requestfactory.shared.ServerFailure)
			 */
			@Override
			public void onFailure(ServerFailure error) {
				// 
				logger.severe("server error saveCourseMember : " + error.getMessage() );
				Window.alert("server error saveCourseMember : " + error.getMessage() );
			}

			/* (non-Javadoc)
			 * @see com.google.web.bindery.requestfactory.shared.Receiver#onConstraintViolation(java.util.Set)
			 */
			@Override
			public void onConstraintViolation(Set<ConstraintViolation<?>> errors) {
				// 
				StringBuffer msg = new StringBuffer("Incomplete or errors found. Please correct and resubmit." );
				for( ConstraintViolation<?> vio : errors ) {
					msg.append( "\n* " );
					msg.append( vio.getMessage() );
				}
				Window.alert( msg.toString() );
				super.onConstraintViolation(errors);
			}
		});
	}


}
