package ca.jhosek.linguaelive.activity.mainregion;

import java.util.Set;
import java.util.logging.Logger;

import javax.validation.ConstraintViolation;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

import ca.jhosek.linguaelive.domain.CurrentState;
import ca.jhosek.linguaelive.place.IndexPlace;
import ca.jhosek.linguaelive.place.LoginFormPlace;
import ca.jhosek.linguaelive.place.LostAccountPlace;
import ca.jhosek.linguaelive.ui.anon.LoginFormView;
import ca.jhosek.linguaelive.ui.anon.LoginFormViewImpl;
import ca.jhosek.linguaelive.proxy.AppRequestFactory;
import ca.jhosek.linguaelive.proxy.UserProxy;
import ca.jhosek.linguaelive.proxy.UserRequestContext;

/**
 * Login form 
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see LoginFormView
 * @see LoginFormViewImpl
 * @see LoginFormPlace
 * 
 */
public class LoginFormActivity extends AbstractActivity implements LoginFormView.Presenter {

	private static final Logger logger = Logger.getLogger( LoginFormActivity.class.getName() );


	private final LoginFormView view;
	private final PlaceController placeController;
	private final CurrentState currentState;
	private final AppRequestFactory requestFactory;	

	private final EventBus eventBus;
	
	@Inject
	public LoginFormActivity(
			LoginFormView view,
			EventBus eventBus,
			PlaceController placeController, 
			CurrentState currentState, 
			AppRequestFactory requestFactory ) {
		super();
		this.view = view;
		this.eventBus = eventBus;
		this.placeController = placeController;
		this.currentState = currentState;
		this.requestFactory = requestFactory;
		view.setPresenter(this);
	}

	
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		view.clear( currentState.getCookieEmailAddress() );
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
	 * @see ca.jhosek.linguaelive.ui.anon.LoginFormView.Presenter#loginUser(java.lang.String, java.lang.String)
	 */
	
	public void loginUser(final String emailAddress, final String password, final boolean rememberMe ) {
		logger.info("loginUser( <" + emailAddress + "> <" + password +	"> )" );

		// push ui content into object
		//user.setBrowser( CurrentState.getUserAgent() );
		// flush failed w/ errors
		// successful flush to object
		UserRequestContext userContext = requestFactory.userRequest();
		// flush object to persistent storage
		userContext.loginEmailAddress( emailAddress, password ).fire( new Receiver<UserProxy>() {

			/**
			 * handle server side exception
			 * @see com.google.web.bindery.requestfactory.shared.Receiver#onFailure(com.google.web.bindery.requestfactory.shared.ServerFailure)
			 */
			
			public void onFailure(ServerFailure error) {
				// failed to login
				Window.alert("Login failed for '" + emailAddress +
						"', please try again.");
				
			}
			
			/**
			 * success
			 * @see com.google.web.bindery.requestfactory.shared.Receiver#onSuccess(java.lang.Object)
			 */
			@Override
			public void onSuccess( UserProxy response) {
				logger.info("loginUser() successful loginEmailAddress()" );
				// login new user 
				currentState.loginThisUser(response);
								
				// if remember me then set cookie on local browser
				currentState.rememberUserLogin( rememberMe );
				Place forwardLoginTo = currentState.getForwardLoginTo();
				//	
				if ( forwardLoginTo == null ) {
					placeController.goTo(currentState.getHomePlace());
				
				} else {
					// user came into site via deep link - send them to it
					logger.info("forwarding to previous place - " + forwardLoginTo.toString() );
					// placeController.goTo(forwardLoginTo);
					// force a load of this page
					eventBus.fireEvent(new PlaceChangeEvent( forwardLoginTo ));

				}

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

	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.anon.LoginFormView.Presenter#loginViaFacebook()
	 */
	
	public void loginViaFacebook() {
		// 
		Window.alert("Under Construction" );
		placeController.goTo( new IndexPlace() );


	}

	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.anon.LoginFormView.Presenter#forgotPassword()
	 */
	
	public void forgotPassword() {
		// 
		placeController.goTo( new LostAccountPlace() );

	}


}
