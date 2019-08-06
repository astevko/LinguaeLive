package ca.jhosek.main.client.activity.mainregion;

import java.util.Set;
import java.util.logging.Logger;

import javax.validation.ConstraintViolation;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

import ca.jhosek.main.client.activity.ActivityModule;
import ca.jhosek.main.client.domain.CurrentState;
import ca.jhosek.main.client.mvp.MainRegionActivityMapper;
import ca.jhosek.main.client.place.ChangePswdPlace;
import ca.jhosek.main.client.place.MyProfilePlace;
import ca.jhosek.main.client.ui.priv.ChangePswdView;
import ca.jhosek.main.client.ui.priv.ChangePswdViewImpl;
import ca.jhosek.main.shared.proxy.AppRequestFactory;
import ca.jhosek.main.shared.proxy.ContactInfoProxy;
import ca.jhosek.main.shared.proxy.UserProxy;
import ca.jhosek.main.shared.proxy.UserRequestContext;

/**
 * Activity related to My Profile View
 * 
 * @see ChangePswdView
 * @see ChangePswdViewImpl
 * @see ChangePswdPlace
 * 
 * @see UserProxy
 * @see ContactInfoProxy
 * 
 * @see MainRegionActivityMapper
 * @see ActivityModule 
 *
 * @author copyright (C) 2011,2012 Andrew Stevko
 */
public class ChangePswdActivity extends AbstractActivity implements ChangePswdView.Presenter {

	private static final Logger logger = Logger.getLogger( ChangePswdActivity.class.getName() );

	/**
	 * Guice/Gin Factory Module Builder
	 * {@link} http://google-guice.googlecode.com/svn/trunk/javadoc/com/google/inject/assistedinject/FactoryModuleBuilder.html
	 * @see FactoryModuleBuilder
	 * @see GinFactoryModuleBuilder
	 *  
	 * @see ActivityModule
	 */
	public interface Factory {
		ChangePswdActivity create( UserProxy userFocus );
	}
	
	private final ChangePswdView view;
	private final PlaceController placeController;
	private final AppRequestFactory requestFactory;
	private final CurrentState currentState;

	private ChangePswdViewImpl.Driver userProxyDriver;

	/**
	 * the focus for the profile component
	 */
	private String userFocusId = "";
	/**
	 * the focus for the profile component
	 */
	private UserProxy userFocus = null;
	/**
	 * 
	 */
	private String doNotSetThisPassword;
	
	/**
	 * @param currentState
	 * @param requestFactory
	 * @param view
	 * @param placeController
	 */
	@Inject
	public ChangePswdActivity(
			CurrentState currentState,
			AppRequestFactory requestFactory,
			ChangePswdView view, 
			PlaceController placeController,
			@Assisted UserProxy userFocus ) {
		super();
		this.currentState = currentState;
		this.requestFactory = requestFactory;
		this.view = view;
		this.placeController = placeController;
		
		// assisted user focus
		this.userFocus = userFocus;
		
		// back link to presenter
		view.setPresenter(this);
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.activity.shared.Activity#start(com.google.gwt.user.client.ui.AcceptsOneWidget, com.google.web.bindery.event.shared.EventBus)
	 */	
	/* (non-Javadoc)
	 * @see com.google.gwt.activity.shared.Activity#start(com.google.gwt.user.client.ui.AcceptsOneWidget, com.google.gwt.event.shared.EventBus)
	 */
	@Override
	public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
		
		// default to logged in user, could already be set
		if (userFocus == null && (userFocusId == null || userFocusId.isEmpty())) {
			userFocus = currentState.getLoggedInUser();
			// set user focus to default user
			setUserFocus(userFocus, panel, eventBus);			
			
		} else if (userFocus == null && !userFocusId.isEmpty()) {
			// look up the user id 
			UserRequestContext getUserContext = requestFactory.userRequest();
			getUserContext.findUser(Long.valueOf(userFocusId)).fire(new Receiver<UserProxy>() {

				@Override
				public void onSuccess(UserProxy response) {
					// save it
					setUserFocus(response, panel, eventBus);
				}
			});
		} else {
			// set user focus to this.
			setUserFocus(userFocus, panel, eventBus);			
		}
	}

	/**
	 * @param setUserFocus
	 * @param panel
	 * @param eventBus
	 */
	protected void setUserFocus(UserProxy setUserFocus, AcceptsOneWidget panel, EventBus eventBus) {
		userProxyDriver = view.createEditorDriver(eventBus, requestFactory);
		userFocus = setUserFocus; // currentState.getLoggedInUser();
		// set up user context
		UserRequestContext userContext = requestFactory.userRequest();
		// mark it as editing
		userContext.persistPassword( userFocus );
		// push into editor
		userProxyDriver.edit( userFocus, userContext );
		panel.setWidget(view);
		
		// prevent scrambling the  password - hash of a hash
		doNotSetThisPassword = setUserFocus.getPassword();
		
		view.resetFocus();
	}
	
	
	/* (non-Javadoc)
	 * @see ca.jhosek.main.client.ui.priv.ChangePswdView.Presenter#saveUserEdits()
	 */
	@Override
	public void saveUserEdits() {
		// 
		RequestContext context = userProxyDriver.flush();
		if (!context.isChanged() ||
				// block saving hashed password back thus corrupting it 
				doNotSetThisPassword.equals(view.getNewPassword())) {
			Window.alert("No changes were detected." );
			return;
		}		
		if (userProxyDriver.hasErrors()) {
			logger.warning("saveUserEdits() onSuccess() with hasErrors() after flush()" );
			Window.alert("Errors detected locally in input");
			return;
		}
		logger.info( "post change password for user id " + userFocus.getId() );
		
		context.fire( new Receiver<Void>() {

			@Override
			public void onSuccess( Void response) {
				logger.info("saveUserEdits().persist() successful" );
				logger.info("User Updated! ");
//						userProxyDriver.edit( response, userContext );
//						view.resetView();
				placeController.goTo(new MyProfilePlace(/*TODO: add user here */));
			}
			/* (non-Javadoc)
			 * @see com.google.web.bindery.requestfactory.shared.Receiver#onFailure(com.google.web.bindery.requestfactory.shared.ServerFailure)
			 */
			@Override
			public void onFailure(ServerFailure error) {
				// TODO - figure out RequestFactory server exceptions
				Window.alert( error.getExceptionType() + "-" + error.getMessage() );
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
	
	public AppRequestFactory getRequestFactory() {
		// 
		return requestFactory;
	}

	@Override
	public void gotoHomePlace() {
		placeController.goTo(new MyProfilePlace(userFocus.getId().toString()));
	}
}
