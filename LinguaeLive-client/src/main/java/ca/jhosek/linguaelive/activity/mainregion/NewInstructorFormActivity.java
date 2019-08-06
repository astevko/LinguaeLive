package ca.jhosek.main.client.activity.mainregion;

import java.util.Date;
import java.util.Set;
import java.util.logging.Logger;

import javax.validation.ConstraintViolation;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

import ca.jhosek.main.client.domain.CurrentState;
import ca.jhosek.main.client.place.AddEditCoursePlace;
import ca.jhosek.main.client.place.IndexPlace;
import ca.jhosek.main.client.place.LoginFormPlace;
import ca.jhosek.main.client.place.NewInstructorPlace;
import ca.jhosek.main.client.ui.anon.NewInstructorPanel;
import ca.jhosek.main.client.ui.anon.NewInstructorPanelImpl;
import ca.jhosek.main.server.email.NewInstructorEmail;
import ca.jhosek.main.shared.UserType;
import ca.jhosek.main.shared.proxy.AppRequestFactory;
import ca.jhosek.main.shared.proxy.UserProxy;
import ca.jhosek.main.shared.proxy.UserRequestContext;

/**
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see NewInstructorFormActivity
 * @see NewInstructorPanel
 * @see NewInstructorPanelImpl
 * @see NewInstructorPlace
 * 
 * @see NewInstructorEmail
 */
public class NewInstructorFormActivity extends AbstractActivity implements NewInstructorPanel.Presenter {

	// hook in Editor Driver for Request Factory
	public interface Driver extends
	RequestFactoryEditorDriver<UserProxy, NewInstructorPanelImpl> {
	}

	private static final Logger logger = Logger.getLogger( NewInstructorFormActivity.class.getName() );
	
	private final PlaceController placeController;
	private final CurrentState currentState;
	private final AppRequestFactory requestFactory;
	private final Driver driver;
	private final NewInstructorPanel view;

	private UserProxy user;
	
	@Inject
	public NewInstructorFormActivity(
			NewInstructorPanel view,
			EventBus eventBus,
			PlaceController placeController, 
			CurrentState currentState, 
			AppRequestFactory requestFactory ) {
		super();
		this.view = view;
		this.placeController = placeController;
		this.currentState = currentState;
		this.requestFactory = requestFactory;
		view.setPresenter(this);
		driver = view.createEditorDriver(eventBus, requestFactory);
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.main.client.ui.anon.NewInstructorPanel.Presenter#cancel()
	 */
	
	public void cancel() {
		logger.info("cancel()" );
		// 
		placeController.goTo( new IndexPlace());

	}

	/* (non-Javadoc)
	 * @see ca.jhosek.main.client.ui.anon.NewInstructorPanel.Presenter#createNewInstructor(ca.jhosek.main.shared.User)
	 */
	@SuppressWarnings("deprecation")
	
	public void createNewInstructor() {
		logger.info("createNewInstructor()" );

		// push ui content into object
		user.setTimezoneOffset( new Date().getTimezoneOffset() );
		user.setBrowser( CurrentState.getUserAgent() );
		// instructor type
		user.setUserType( UserType.INSTRUCTOR );
		
		driver.flush().fire( new Receiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				// flush failed w/ errors
				if (driver.hasErrors()) {
					logger.warning("createNewInstructor() onSuccess() with hasErrors() after flush()" );
					Window.alert("errors detected locally in input");
					return;
				}
				// successful flush to object
				
				UserRequestContext userContext = requestFactory.userRequest();
				
				// flush object to persistent storage
				userContext.persist( user ).fire( new Receiver<UserProxy>() {

					@Override
					public void onSuccess( UserProxy response) {
						logger.info("createNewInstructor() successful flush & persist" );
					// successful save to persistent storage
						user = response;
						// login new user 
						currentState.loginThisUser(response);
						Window.alert("Thanks for signing up! \nYou are now ready to register your first course.");
						placeController.goTo( new AddEditCoursePlace("") );	// updated LL-142

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
					/* (non-Javadoc)
					 * @see com.google.web.bindery.requestfactory.shared.Receiver#onFailure(com.google.web.bindery.requestfactory.shared.ServerFailure)
					 */
					@Override
					public void onFailure(ServerFailure error) {
						// 
						Window.alert("Error registering new user - " + error.getMessage() );
						placeController.goTo( new LoginFormPlace() );
					}
				});
				
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
		
	};

	/**
	 * creates a new UserProxy object
	 * Registers it with the editor view
	 *  
	 * @see com.google.gwt.activity.shared.Activity#start(com.google.gwt.user.client.ui.AcceptsOneWidget, com.google.web.bindery.event.shared.EventBus)
	 */
	
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		// initialize data layer
		UserRequestContext userContext = requestFactory.userRequest();
		this.user = userContext.create( UserProxy.class );
		this.driver.edit(user, userContext);
		// display view
		panel.setWidget(view);

	}



}
