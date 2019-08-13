package ca.jhosek.linguaelive.activity.mainregion;

import java.util.List;
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

import ca.jhosek.linguaelive.activity.ActivityModule;
import ca.jhosek.linguaelive.domain.CurrentState;
import ca.jhosek.linguaelive.place.AddEditContactInfoPlace;
import ca.jhosek.linguaelive.place.ChangePswdPlace;
import ca.jhosek.linguaelive.place.MyProfilePlace;
import ca.jhosek.linguaelive.place.StudentStartPlace;
import ca.jhosek.linguaelive.ui.priv.MyProfileView;
import ca.jhosek.linguaelive.ui.priv.MyProfileViewImpl;
// import ca.jhosek.linguaelive.domain.ContactInfoDao;
import ca.jhosek.linguaelive.proxy.AppRequestFactory;
import ca.jhosek.linguaelive.proxy.ContactInfoProxy;
import ca.jhosek.linguaelive.proxy.ContactInfoRequestContext;
import ca.jhosek.linguaelive.proxy.UserProxy;
import ca.jhosek.linguaelive.proxy.UserRequestContext;

/**
 * Activity related to My Profile View
 * 
 * @see MyProfileView
 * @see MyProfileViewImpl
 * @see MyProfilePlace
 * 
 * @see UserProxy
 * @see ContactInfoProxy
 *
 * @author copyright (C) 2011 Andrew Stevko
 */
public class MyProfileActivity extends AbstractActivity implements MyProfileView.Presenter {

	private static final Logger logger = Logger.getLogger( MyProfileActivity.class.getName() );

	
	/**
	 * Guice/Gin Factory Module Builder
	 * {@link} http://google-guice.googlecode.com/svn/trunk/javadoc/com/google/inject/assistedinject/FactoryModuleBuilder.html
	 * @see FactoryModuleBuilder
	 * @see GinFactoryModuleBuilder
	 *  
	 * @see ActivityModule
	 */
	public interface Factory {
		MyProfileActivity create( String userFocusId );
	}
	
	private final MyProfileView view;
	private final PlaceController placeController;
	private final AppRequestFactory requestFactory;
	private final CurrentState currentState;

	private MyProfileViewImpl.Driver userProxyDriver;

	protected List<ContactInfoProxy> contactInfos;

	/**
	 * the focus for the profile component
	 */
	private String userFocusId = "";
	/**
	 * the focus for the profile component
	 */
	private UserProxy userFocus = null;

	/**
	 * @param currentState
	 * @param requestFactory
	 * @param view
	 * @param placeController
	 */
	@Inject
	public MyProfileActivity(
			CurrentState currentState,
			AppRequestFactory requestFactory,
			MyProfileView view, 
			PlaceController placeController,
			@Assisted String userFocusId ) {
		super();
		// injected
		this.currentState = currentState;
		this.requestFactory = requestFactory;
		this.view = view;
		this.placeController = placeController;
		
		// assisted user focus
		this.userFocusId = userFocusId;
		
		// back link to presenter
		view.setPresenter(this);
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.activity.shared.Activity#start(com.google.gwt.user.client.ui.AcceptsOneWidget, com.google.web.bindery.event.shared.EventBus)
	 */
	
	public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
		// default to logged in user, could already be set
		if (userFocus == null && (userFocusId == null || userFocusId.isEmpty())) {
			userFocus = currentState.getLoggedInUser();
			// set user focus to default user
			setUserFocus(userFocus, panel, eventBus);			
			
		} else if (userFocus == null) {
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
		// this is the focus
		this.userFocus = setUserFocus;
		this.userFocusId = userFocus.getId().toString();
		
		// set up user context 
		UserRequestContext persistUserContext = requestFactory.userRequest();
		persistUserContext.persist( this.userFocus );
		
		// push into editor
		this.userProxyDriver = view.createEditorDriver(eventBus, requestFactory);
		this.userProxyDriver.edit( this.userFocus, persistUserContext );
		
		panel.setWidget(view);
		
		loadContactInfo( this.userFocus );
	}

	public void saveUserEdits() {
		// 
		RequestContext context = userProxyDriver.flush();
		if (!context.isChanged()) {
			Window.alert("No changes were detected." );
			return;
		}
		if (userProxyDriver.hasErrors()) {
			logger.warning("saveUserEdits() onSuccess() with hasErrors() after flush()" );
			Window.alert("Errors detected locally in input");
			return;
		}
		logger.info( "post flush school=" + currentState.getLoggedInUser().getSchool() );
		context.fire( new Receiver<Void>() {

			@Override
			public void onSuccess( Void response) {
				logger.info("saveUserEdits().persist() successful" );
				logger.info("User Updated! ");
//						userProxyDriver.edit( response, userContext );
//						view.resetView();
			}
			/* (non-Javadoc)
			 * @see com.google.web.bindery.requestfactory.shared.Receiver#onFailure(com.google.web.bindery.requestfactory.shared.ServerFailure)
			 */
			@Override
			public void onFailure(ServerFailure error) {
				// TODO - figure out RequestFactory server exceptions
				Window.alert( error.getExceptionType() + "-" + error.getMessage() );
				currentState.refetchProfile();
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
	
	/**
	 * display User's Contact Info 
	 * @param user
	 * 
	 * @see ContactInfoDao
	 */
	private void loadContactInfo( UserProxy user) {
		logger.info("loadContactInfo( " + user.getFirstName() + " " + user.getLastName() + " )");
		// 
		ContactInfoRequestContext contactInfoContext = requestFactory.contactInfoRequest();
		contactInfoContext.findContactInfos(user).fire( new Receiver<List<ContactInfoProxy>>() {

			@Override
			public void onSuccess(List<ContactInfoProxy> response) {
				logger.info("loadContactInfo() fetched  " + response.size() + " ContactInfoProxy" );
				//
				contactInfos = response;
				view.showContactInfo(response);
			}
		});
	}

	
	public void addNewContactInfo() {
		logger.info("addNewContactInfo() " );
		placeController.goTo( new AddEditContactInfoPlace(""));
	}

	
	public void editContactInfo(ContactInfoProxy contactInfo) {
		logger.info("editContactInfo() " );
		placeController.goTo( new AddEditContactInfoPlace( contactInfo ));
		
	}
	
	
	public void gotoQuickStart() {
		placeController.goTo(new StudentStartPlace());
	}

	
	public AppRequestFactory getRequestFactory() {
		// 
		return requestFactory;
	}

	@Override
	public void gotoChangePassword() {
		// open change password form for this user
		placeController.goTo(new ChangePswdPlace(userFocus));
	}
	

	//			/* (non-Javadoc)
	//			 * @see com.google.web.bindery.requestfactory.shared.Receiver#onFailure(com.google.web.bindery.requestfactory.shared.ServerFailure)
	//			 */
	//			@Override
	//			public void onFailure(ServerFailure error) {
	//				// TODO - figure out RequestFactory server exceptions
	//				Window.alert( error.getExceptionType() + "-" + error.getMessage() );
	//			}
	//
	//			/* (non-Javadoc)
	//			 * @see com.google.web.bindery.requestfactory.shared.Receiver#onViolation(java.util.Set)
	//			 */
	//			@Override
	//			public void onViolation(Set<Violation> errors) {
	//				// TODO - figure out RequestFactory server exceptions
	//				Window.alert("errors detected on server");
	//				for( Violation error : errors ){
	//					Window.alert("server says: " + error.getMessage() );
	//				}
	//				logger.warning("saveUserEdits().persist() onViolation() " );
	//				userProxyDriver.setViolations(errors);						
	//			}
//} );
//}
}
