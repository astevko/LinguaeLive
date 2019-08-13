package ca.jhosek.linguaelive.activity.mainregion;

import java.util.Set;
import java.util.logging.Logger;

import javax.validation.ConstraintViolation;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

import ca.jhosek.linguaelive.domain.CurrentState;
import ca.jhosek.linguaelive.place.AddEditContactInfoPlace;
import ca.jhosek.linguaelive.place.MyProfilePlace;
import ca.jhosek.linguaelive.ui.priv.AddEditContactInfoView;
import ca.jhosek.linguaelive.ui.priv.AddEditContactInfoViewImpl;
import ca.jhosek.linguaelive.ui.priv.AddEditContactInfoViewImpl.Driver;
// import ca.jhosek.linguaelive.domain.ContactInfoDao;
import ca.jhosek.linguaelive.proxy.AppRequestFactory;
import ca.jhosek.linguaelive.proxy.ContactInfoProxy;
import ca.jhosek.linguaelive.proxy.ContactInfoRequestContext;

/**
 * add/edit new contact info 
 * or cancel to my profile page
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see AddEditContactInfoView
 * @see AddEditContactInfoViewImpl
 * @see AddEditContactInfoPlace
 * @see ContactInfoDao
 * 
 */
public class AddEditContactInfoActivity extends AbstractActivity implements AddEditContactInfoView.Presenter {

	private static final Logger logger = Logger.getLogger( AddEditContactInfoActivity.class.getName() );

	/**
	 * Guice/Gin Factory Module Builder
	 * {@link} http://google-guice.googlecode.com/svn/trunk/javadoc/com/google/inject/assistedinject/FactoryModuleBuilder.html
	 * @see FactoryModuleBuilder
	 * @see GinFactoryModuleBuilder
	 *  
	 */
	public interface Factory {
		AddEditContactInfoActivity create( String contactInfoId );
	}


	private final AddEditContactInfoView view;
	private final PlaceController placeController;
	private final AppRequestFactory requestFactory;
	private final CurrentState currentState;

	private Driver editorDriver;

	private ContactInfoProxy addThisContactInfo = null;
	private final String token;
	private ContactInfoRequestContext contactInfoContext;

	@Inject
	public AddEditContactInfoActivity( 
			AddEditContactInfoView view, 
			PlaceController placeController,
			AppRequestFactory requestFactory,
			CurrentState currentState,
			Driver driver,
			EventBus eventBus,
			@Assisted String contactInfoId
	) {
		super();
		this.view = view;
		this.placeController = placeController;
		this.requestFactory = requestFactory;
		this.currentState = currentState;
		view.setPresenter(this);
		this.editorDriver = view.createEditorDriver( eventBus, requestFactory);

		this.token = contactInfoId;
	}

	/** 
	 * start this activity
	 * 
	 * @see com.google.gwt.activity.shared.Activity#start(com.google.gwt.user.client.ui.AcceptsOneWidget, com.google.web.bindery.event.shared.EventBus)
	 */
	
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		// obtain editor context
		contactInfoContext = requestFactory.contactInfoRequest();

		panel.setWidget(view);
		if ( token == null || token.isEmpty() ) {
			// create new 
			addThisContactInfo = contactInfoContext.create( ContactInfoProxy.class );

			// fixup bean
			addThisContactInfo.setOwner( currentState.getLoggedInUser());
			addThisContactInfo.setPreferred(false);

			editorDriver.edit( addThisContactInfo, contactInfoContext );
			contactInfoContext.persist( addThisContactInfo );

		} else {
			// edit existing
			final Long contactInfoId = Long.valueOf(token);
			Request<ContactInfoProxy> fetchRequest = contactInfoContext.findContactInfo(contactInfoId);
			fetchRequest.to( new Receiver<ContactInfoProxy>() {

				
				public void onSuccess(ContactInfoProxy response) {
					// 
					addThisContactInfo = response;
					if ( response == null ) {
						logger.severe("contact info id not found:" + token );
						Window.alert("Error - Contact info id not found");
						placeController.goTo( new MyProfilePlace());

					} else {
						contactInfoContext = requestFactory.contactInfoRequest();						
						editorDriver.edit( addThisContactInfo, contactInfoContext );
						contactInfoContext.persist( addThisContactInfo );
					}
				}
			}).fire();
		}
	}

	/** 
	 * save ContactInfo and goto MyProfile page
	 *  
	 * @see ca.jhosek.linguaelive.ui.priv.AddEditContactInfoView.Presenter#save()
	 */
	public void save() {
		// 
		logger.info("Saving contact info edits");
		saveContactInfo(  new MyProfilePlace( ) );
	}
	
	
	private void saveContactInfo( final Place goToPlace ) {
		// 
		logger.info("Saving contact info edits");

		RequestContext context = editorDriver.flush();

		// Check for errors
		if (editorDriver.hasErrors()) {
			// 
			// flush failed w/ errors
			logger.warning("saveContactInfo() onSuccess() with hasErrors() after flush()" );
			StringBuilder errorDesc = new StringBuilder( "Errors found while saving the contact info. ");
			for ( EditorError error : editorDriver.getErrors() ) {
				errorDesc.append(error.getMessage());
				errorDesc.append(" ");						
			}

			Window.alert( errorDesc.toString() 	);
//			reloadEditor();
			return;
		}

		context.fire( new Receiver<Void>() {

			/* (non-Javadoc)
			 * @see com.google.web.bindery.requestfactory.shared.Receiver#onFailure(com.google.web.bindery.requestfactory.shared.ServerFailure)
			 */
			@Override
			public void onFailure(ServerFailure error) {
				// 
				Window.alert("Server failed to save due to error:" + error.getExceptionType() + " " + error.getMessage());
				super.onFailure(error);
			}

			/* (non-Javadoc)
			 * @see com.google.web.bindery.requestfactory.shared.Receiver#onConstraintViolation(java.util.Set)
			 */
			@Override
			public void onConstraintViolation(Set<ConstraintViolation<?>> errors) {
				// 
				for( ConstraintViolation<?> vio : errors )
					Window.alert("Failed to save due to error:" + vio.getMessage());
				super.onConstraintViolation(errors);
			}

			/* (non-Javadoc)
			 * @see com.google.web.bindery.requestfactory.shared.Receiver#onSuccess(java.lang.Object)
			 */
			@Override
			public void onSuccess(Void response) {
				// successful save to persistent storage
				Window.alert("Saved!" );
				// goto my profile view
				placeController.goTo( goToPlace );
			}
		});
	}

//	protected void reloadEditor() {
//		// obtain editor context
//		ContactInfoRequestContext contactInfoContext = requestFactory.contactInfoRequest();
//		// set query
//		ContactInfoProxy newContactInfo = contactInfoContext.create( ContactInfoProxy.class );
//		if ( addThisContactInfo != null ){
//			clone(addThisContactInfo, newContactInfo );
//		}
//		addThisContactInfo = newContactInfo; 
//		editorDriver.edit( addThisContactInfo, contactInfoContext );						
//	}
//
//	/**
//	 * clone the ContactInfo cause we have a AutoBean Frozen error
//	 * 
//	 * @param cloneFrom
//	 * @param cloneInto
//	 */
//	private void clone(ContactInfoProxy cloneFrom, ContactInfoProxy cloneInto ) {
//		// 
//		cloneInto.setInfo(  cloneFrom.getInfo() );
//		cloneInto.setOwner( cloneFrom.getOwner());
//		cloneInto.setPreferred( cloneFrom.getPreferred());
//		cloneInto.setType(  cloneFrom.getType() );
//	}

	/***
	 * goto my profile on cancel
	 * @see ca.jhosek.linguaelive.ui.priv.AddEditContactInfoView.Presenter#cancel()
	 */
	public void cancel() {
		// 
		placeController.goTo( new MyProfilePlace() );
	}


//	/**
//	 * save and add more contact info
//	 * @see ca.jhosek.linguaelive.ui.priv.AddEditContactInfoView.Presenter#saveMore()
//	 */
//	@Override
//	public void saveMore() {
//		// 
//		logger.info("Saving contact info edits & refreshing to add more");
//		saveContactInfo( new AddEditContactInfoPlace("") );
//	}
}
