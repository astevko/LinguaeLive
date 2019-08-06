package ca.jhosek.main.client.activity.mainregion;

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
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

import ca.jhosek.main.client.domain.CurrentState;
import ca.jhosek.main.client.place.ContactUsPlace;
import ca.jhosek.main.client.ui.anon.ContactUsView;
import ca.jhosek.main.client.ui.anon.ContactUsViewImpl;
import ca.jhosek.main.client.ui.anon.ContactUsViewImpl.Driver;
import ca.jhosek.main.shared.proxy.AppRequestFactory;
import ca.jhosek.main.shared.proxy.ContactUsProxy;
import ca.jhosek.main.shared.proxy.ContactUsRequestContext;
import ca.jhosek.main.shared.proxy.UserProxy;

/**
 * @author copyright (C) 2011 Andrew Stevko
 *
 * @see ContactUsView
 * @see ContactUsViewImpl
 * @see ContactUsPlace
 * @see ContactUsProxy
 * 
 */
public class ContactUsActivity extends AbstractActivity implements ContactUsView.Presenter {

	private static final Logger logger = Logger.getLogger( ContactUsActivity.class.getName() );


	private final ContactUsView view;
	private final PlaceController placeController;
	private final CurrentState currentState;
	private final AppRequestFactory requestFactory;

	private Driver contactUsDriver;

	private ContactUsProxy contactUs;

	@Inject
	public ContactUsActivity( ContactUsView view, 
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
		contactUsDriver = view.createEditorDriver(eventBus, requestFactory);
		ContactUsRequestContext contactUsContext = requestFactory.contactUsRequest();

		contactUs = contactUsContext.create( ContactUsProxy.class );
		// set defaults if available...
		if ( currentState.isLoggedIn() ) {
			UserProxy user = currentState.getLoggedInUser();
			contactUs.setEmailAddress(user.getEmailAddress());
			contactUs.setName( user.getFirstName() + " " + user.getLastName() );
			contactUs.setSchool( user.getSchool() );
			contactUs.setUserId( user.getId() );

		}

		contactUsDriver.edit(contactUs, contactUsContext);

		contactUsContext.submit(contactUs).to( new Receiver<Void>() {

			/* (non-Javadoc)
			 * @see com.google.web.bindery.requestfactory.shared.Receiver#onFailure(com.google.web.bindery.requestfactory.shared.ServerFailure)
			 */
			@Override
			public void onFailure(ServerFailure error) {
				Window.alert("Error connecting to our servers. Please try again." );
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
			@Override
			public void onSuccess(Void response) {
				// 
				Window.alert( "Your information has been submitted to our support team.\nThank you.");
				placeController.goTo( currentState.getHomePlace() );
			}
		}
		);

		panel.setWidget(view);

	}

	/* (non-Javadoc)
	 * @see ca.jhosek.main.client.ui.anon.NewStudentPanel.Presenter#cancel()
	 */

	public void cancel() {
		logger.info("cancel()" );
		// 
		placeController.goTo( currentState.getHomePlace() );
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.main.client.ui.anon.ContactUsView.Presenter#submit()
	 */
	public void submit() {
		// 
		logger.info("submit()");

		RequestContext context = contactUsDriver.flush();

		if ( contactUsDriver.hasErrors() ) {
			Window.alert("All fields are required. Please complete and submit again.");
			return;
		}
		logger.info("submitting contact us information" );
		context.fire();
	}

}
