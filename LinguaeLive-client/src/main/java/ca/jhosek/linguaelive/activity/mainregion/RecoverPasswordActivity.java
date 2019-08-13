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
import com.google.web.bindery.requestfactory.shared.ServerFailure;

import ca.jhosek.linguaelive.activity.ActivityModule;
import ca.jhosek.linguaelive.place.ChangePswdPlace;
import ca.jhosek.linguaelive.place.IndexPlace;
import ca.jhosek.linguaelive.place.RecoverPasswordPlace;
import ca.jhosek.linguaelive.ui.anon.RecoverPasswordView;
import ca.jhosek.linguaelive.ui.anon.RecoverPasswordViewImpl;
import ca.jhosek.linguaelive.proxy.AppRequestFactory;
import ca.jhosek.linguaelive.proxy.UserProxy;
import ca.jhosek.linguaelive.proxy.UserRequestContext;

/**
 * used to reset passwords 
 * 
 * @author copyright (C) 2012 Andrew Stevko
 * 
 * @see ActivityModule  registered here
 * @see RecoverPasswordView
 * @see RecoverPasswordViewImpl
 * @see RecoverPasswordPlace
 * @see UserRequestContext  // used to identify & manipulate 
 */
public class RecoverPasswordActivity extends AbstractActivity implements RecoverPasswordView.Presenter {

	private static final Logger logger = Logger.getLogger( RecoverPasswordActivity.class.getName() );


	private final RecoverPasswordView view;
	private final PlaceController placeController;
	private final AppRequestFactory requestFactory;	

	@Inject
	public RecoverPasswordActivity( 
			RecoverPasswordView view, 
			PlaceController placeController,
			AppRequestFactory requestFactory ) {
		super();
		this.view = view;
		this.placeController = placeController;
		this.requestFactory = requestFactory;
		view.setPresenter(this);
	}


	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		view.clear();
		panel.setWidget(view);

	}

	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.anon.NewStudentPanel.Presenter#cancel()
	 */

	public void cancel() {
		logger.info("cancel()" );
		// 
		placeController.goTo( new IndexPlace());

	}
	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.anon.RecoverPasswordView.Presenter#checkOkEmailHash(java.lang.String, java.lang.String)
	 */
	@Override
	public void checkOkEmailHash(final String email, final String hash) {
		logger.info("checkOkEmailHash(String " +
				email +
				", String " +
				hash +
				")" );

		UserRequestContext userContext = requestFactory.userRequest();
		// call server with email address
		userContext.checkOkEmailHash(email, hash).fire( new Receiver<UserProxy>() {

			@Override
			public void onSuccess( final UserProxy distressedUser) {
				logger.info("found the user and verified the user's hash" );
				// save bonified user reference for later use??
				// bonifiedUser = distressedUser;
				
				// DO NOT LOGIN USER - OPENS BACK DOOR 
				// currentState.loginThisUser(distressedUser);
				
				//---------
				// switch to change password page w/ for this user
				final ChangePswdPlace changePasswordPlace = new ChangePswdPlace();
				changePasswordPlace.setUser(distressedUser);
				placeController.goTo( changePasswordPlace );
			}


			/**
			 * @see com.google.web.bindery.requestfactory.shared.Receiver#onFailure(com.google.web.bindery.requestfactory.shared.ServerFailure)
			 */
			@Override
			public void onFailure(ServerFailure error) {
				// Otherwise, show ConstraintViolations in the UI
				logger.warning("recoverPassword() onFailure() called after recoverPassword()" );
				Window.alert("Password recovery failed. Please try again." );
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
