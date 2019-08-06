package ca.jhosek.main.client.activity.mainregion;

import java.util.ArrayList;
import java.util.List;
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

import ca.jhosek.main.client.place.ChangePswdPlace;
import ca.jhosek.main.client.place.QueryUsersPlace;
import ca.jhosek.main.client.ui.priv.admin.AdminUserView;
import ca.jhosek.main.client.ui.priv.admin.AdminUserViewerImpl;
import ca.jhosek.main.client.ui.priv.admin.AdminUserViewerImpl.Driver;
import ca.jhosek.main.client.ui.priv.admin.QueryUsersView;
import ca.jhosek.main.client.ui.priv.admin.QueryUsersViewImpl;
import ca.jhosek.main.server.domain.ContactInfoDao;
import ca.jhosek.main.shared.proxy.AppRequestFactory;
import ca.jhosek.main.shared.proxy.ContactInfoProxy;
import ca.jhosek.main.shared.proxy.ContactInfoRequestContext;
import ca.jhosek.main.shared.proxy.UserProxy;
import ca.jhosek.main.shared.proxy.UserRequestContext;

/**
 * Query Users and put them into a list along with editable region
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 * @see QueryUsersView
 * @see QueryUsersViewImpl
 * @see QueryUsersPlace

 * @see AdminUserViewV
 * @see AdminUserViewerImpl
 */
public class QueryUsersActivity extends AbstractActivity implements QueryUsersView.Presenter, AdminUserView.Presenter {

	private static final Logger logger = Logger.getLogger( QueryUsersActivity.class.getName() );
	

//	public interface Driver extends RequestFactoryEditorDriver<UserProxy, AdminUserViewerImpl> {
//	}
//	
	
	private final QueryUsersView view;
	private PlaceController placeController;
	private final AppRequestFactory requestFactory;
	@SuppressWarnings("unused")
	private final EventBus eventBus;

	private Driver userProxyDriver;
	
	private UserProxy currentUser;
	
	protected List<ContactInfoProxy> contactInfos;
	 

	@Inject
	public QueryUsersActivity( 
			EventBus eventBus,
			QueryUsersView view,		
			AppRequestFactory requestFactory,
			PlaceController placeController
			) {
		super();
		this.eventBus = eventBus;
		this.view = view;
		this.requestFactory = requestFactory;
		this.placeController = placeController;
		view.setPresenter(this);
		AdminUserView userProfilePanel = view.getUserProfilePanel();
		userProfilePanel.setPresenter(this);
		userProxyDriver = userProfilePanel.createEditorDriver( eventBus, requestFactory );
	}

	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(view);
		// init user query
		queryUsers();

	}

	/* (non-Javadoc)
	 * @see ca.jhosek.main.client.ui.priv.admin.QueryUsersView.Presenter#queryUsers()
	 */
	public void queryUsers() {
		logger.info( "executing queryUsers()");
		final String emailAddressFilter = view.getEmailAddressFilter();
		
		// 
		// obtain user context
		UserRequestContext userContext = requestFactory.userRequest();
		// flush object to persistent storage
		
		if (emailAddressFilter.isEmpty()) {
			final Request<List<UserProxy>> q = userContext.findAllUsers();
			q.with("contactInfos").fire( new Receiver<List<UserProxy>>() {

				@Override
				public void onSuccess(List<UserProxy> response) {
					view.clear();
					// 
					// show all users in the list
					if( response.isEmpty() ) {
						logger.info( "no results returned");
						view.emptyQueryResults();
					} else {
						view.showUserList( response );
					}
					
				}
			
			});
		} else {
			final Request<UserProxy> q = userContext.findEmailAddress(emailAddressFilter);
			q.with("contactInfos").fire( new Receiver<UserProxy>() {

				@Override
				public void onSuccess(UserProxy response) {
					view.clear();
					// 
					// show all users in the list
					if( response == null ) {
						logger.info( "no results returned");
						view.emptyQueryResults();
					} else {
						final ArrayList<UserProxy> userList = new ArrayList<UserProxy>();
						userList.add(response);
						view.showUserList( userList );
					}
					
				}
			
			});
		}
		
		
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.main.client.ui.priv.admin.QueryUsersView.Presenter#showUser(java.lang.Long)
	 */
	public void showUser(UserProxy user ) {
		// 
		final UserRequestContext userContext = requestFactory.userRequest();
		this.currentUser = user;
		// persists edit flush fire 
		userContext.persist(currentUser);
		userProxyDriver.edit( user, userContext );
		loadContactInfo(user);
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
	
//	/**
//	 * @return a UserProxy editor driver, userProxyDriver, tied to this presenter and view
//	 */
//	private Driver createEditorDriver() {
//		// 
//		logger.info( "createEditorDriver( AdminUserViewerImpl )");
//		Driver driver = GWT.create(Driver.class);
//		driver.initialize( eventBus, requestFactory, (AdminUserViewerImpl) view.getUserProfilePanel() );
//		return  driver;
//	}

	/* (non-Javadoc)
	 * @see ca.jhosek.main.client.ui.priv.admin.AdminUserView.Presenter#saveUserEdits()
	 */
	public void saveUserEdits() {
		logger.info( "SaveUserEdits() pre-flush, user type=" + currentUser.getUserType().name() +
				" school="  + currentUser.getSchool() );

		// save into proxy user from editor
		userProxyDriver.flush().fire( new Receiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				logger.info( "User Flushed! user type=" + currentUser.getUserType().name() +
						" school="  + currentUser.getSchool() );


				// flush failed w/ errors
				if (userProxyDriver.hasErrors()) {
					logger.warning("saveUserEdits() onSuccess() with hasErrors() after flush()" );
					Window.alert("errors detected locally in input");
					return;
				}
				// successful flush to object
				final UserRequestContext userContext = requestFactory.userRequest();
				// flush object to persistent storage
				userContext.persist( currentUser ).fire( new Receiver<UserProxy>() {

					@Override
					public void onSuccess( UserProxy response) {
						logger.info("saveUserEdits().persist() successful" );
					// successful save to persistent storage
						currentUser = response;
						// login new user 
						logger.info("User Updated! user type=" + response.getUserType().name() +
								" school="  + response.getSchool() );
//						userProxyDriver.edit( response, userContext );
						view.getUserProfilePanel().resetView();
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
	 * @see ca.jhosek.main.client.ui.priv.admin.AdminUserView.Presenter#resetUser()
	 */
	public void resetUser() {
		// 
		logger.info("resetting user");
		UserRequestContext userContext = requestFactory.userRequest();
		userProxyDriver.edit( this.currentUser , userContext );
		
	}

	public AppRequestFactory getRequestFactory() {
		// 
		return requestFactory;
	}

	
//	@Override
//	public void addContactInfo() {
//		logger.info( "executing addContactInfo()");
//		// 
//		UserRequestContext userContext = requestFactory.userRequest();
//		ContactInfoProxy newInfo = userContext.create( ContactInfoProxy.class );
//		newInfo.setType( ContactInfoType.Other);
//		newInfo.setInfo("click to edit");
//		this.currentUser.getContactInfos().add( newInfo );
//	}
	

	@Override
	public void gotoChangePassword() {
		// open change password form for this user
		placeController.goTo(new ChangePswdPlace(currentUser));
	}
	
	@Override
	public void gotoViewOwnedCourses( final UserProxy ownedBy) {
		// open courses page with ownerKey = Key( User, ownedBy.id )
		
		
	}
}
