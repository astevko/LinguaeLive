package ca.jhosek.main.client.activity.mainregion;

import java.util.Date;
import java.util.Set;
import java.util.logging.Logger;

import javax.validation.ConstraintViolation;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

import ca.jhosek.main.client.domain.CurrentState;
import ca.jhosek.main.client.place.IndexPlace;
import ca.jhosek.main.client.place.LoginFormPlace;
import ca.jhosek.main.client.place.NewStudentPlace;
import ca.jhosek.main.client.place.StudentStartPlace;
import ca.jhosek.main.client.ui.anon.NewStudentPanel;
import ca.jhosek.main.client.ui.anon.NewStudentPanelImpl;
import ca.jhosek.main.shared.UserType;
import ca.jhosek.main.shared.proxy.AppRequestFactory;
import ca.jhosek.main.shared.proxy.CourseProxy;
import ca.jhosek.main.shared.proxy.CourseRequestContext;
import ca.jhosek.main.shared.proxy.MemberProxy;
import ca.jhosek.main.shared.proxy.MemberRequestContext;
import ca.jhosek.main.shared.proxy.UserProxy;
import ca.jhosek.main.shared.proxy.UserRequestContext;

/**
 * @author copyright (C) 2011 Andrew Stevko
 *
 * @see NewStudentPlace
 * @see NewStudentPanel
 * @see NewStudentPanelImpl
 * 
 */
public class NewStudentFormActivity extends AbstractActivity implements NewStudentPanel.Presenter {

	// hook in Editor Driver for Request Factory
	public interface Driver extends
	RequestFactoryEditorDriver<UserProxy, NewStudentPanelImpl> {
	}

	private static final Logger logger = Logger.getLogger( NewStudentFormActivity.class.getName() );

	private final PlaceController placeController;
	private final CurrentState currentState;
	private final AppRequestFactory requestFactory;
	private final Driver driver;
	private final EventBus eventBus;
	private final NewStudentPanel view;

	private CourseProxy course;
	private UserProxy user;

	
	public interface Factory {
		NewStudentFormActivity create( String courseInviteCode );
	}

	@Inject
	public NewStudentFormActivity(
			NewStudentPanel view,
			EventBus eventBus,
			PlaceController placeController, 
			CurrentState currentState, 
			AppRequestFactory requestFactory,
			@Assisted String courseInviteCode
		) {
		super();
		this.view = view;
		this.eventBus = eventBus;
		this.placeController = placeController;
		this.currentState = currentState;
		this.requestFactory = requestFactory;
		view.setPresenter(this);
		view.setInviteCode( courseInviteCode );
		driver = createEditorDriver();
	}

	/**
	 * @see ca.jhosek.main.client.ui.anon.NewStudentPanel.Presenter#cancel()
	 */
	
	public void cancel() {
		logger.info("cancel()" );
		// 
		placeController.goTo( new IndexPlace());

	}

	/**
	 * @see ca.jhosek.main.client.ui.anon.NewStudentPanel.Presenter#createNewStudent(ca.jhosek.main.shared.User)
	 */
	@SuppressWarnings("deprecation")
	
	public void createNewStudent() {
		logger.info("createNewStudent()" );

		// push ui content into object
		user.setTimezoneOffset( new Date().getTimezoneOffset() );
		user.setBrowser( CurrentState.getUserAgent() );
		user.setUserType( UserType.STUDENT );

		driver.flush().fire( new Receiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				// flush failed w/ errors
				if (driver.hasErrors()) {
					logger.warning("createNewStudent() onSuccess() with hasErrors() after flush()" );
					Window.alert("errors detected locally in input");
					return;
				}
				// successful flush to object
				UserRequestContext userContext = requestFactory.userRequest();
				// flush object to persistent storage
				userContext.persist( user ).fire( new Receiver<UserProxy>() {

					@Override
					public void onSuccess( UserProxy response) {
						logger.info("createNewStudent() successful flush & persist" );
						// successful save to persistent storage
						user = response;
						// login new user 
						currentState.loginThisUser(response);

						// add student as member of the previously determined course
						MemberRequestContext memberContext = requestFactory.memberRequest();
						memberContext.joinCourse(user, course).to( new Receiver<MemberProxy>() {

							@Override
							public void onSuccess(MemberProxy response) {
								// 
								// ok student is signed up for the course too...
								Window.alert("Thanks for signing up!");						
								
								placeController.goTo( new StudentStartPlace() );

								
							}
						}).fire();
						
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
		// initially show the invite code form
		view.setInviteCodeFormVisibility(true);
		view.setUserFormVisibility(false);

		// display view
		panel.setWidget(view);

	}


	/**
	 * @return an editor driver tied to this class
	 */
	private Driver createEditorDriver() {
		// 
		Driver driver = GWT.create(Driver.class);
		driver.initialize( eventBus, requestFactory, (NewStudentPanelImpl) view);
		return driver;
	}

	/** 
	 * look up invite code before allowing student to sign up
	 * 
	 * @see ca.jhosek.main.client.ui.anon.NewStudentPanel.Presenter#acceptInviteCode(java.lang.String)
	 */
	
	public void acceptInviteCode(final String inviteCode) {
		logger.info("acceptInviteCode( " + inviteCode + " )");
		// 
		CourseRequestContext courseContext = requestFactory.courseRequest();
		courseContext.findInviteCode( inviteCode ).with("owner").to( new Receiver<CourseProxy>() {

			@Override
			public void onSuccess(CourseProxy response) {
				// save response for later use in creating new student 	
				course = response;
				if( course == null ) {
					// bad data
					Window.alert("Invite code '" + inviteCode + "' is not found. Please try again" );
					return;
				} else {
					logger.info("valid invite code found" );
					// course found, switch forms and initialize the editor
					// initialize editor data layer
					UserRequestContext userContext = requestFactory.userRequest();
					user = userContext.create( UserProxy.class );

					// copy school and location into student user
					user.setSchool( course.getOwner().getSchool() );
					user.setLocation( course.getOwner().getLocation() );

					driver.edit(user, userContext);

					view.setInviteCodeFormVisibility(false);
					view.setUserFormVisibility(true);
				}

			}
		}).fire();

	}

}
