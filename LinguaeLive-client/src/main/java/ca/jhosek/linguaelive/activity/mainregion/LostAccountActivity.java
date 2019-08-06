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
import com.google.web.bindery.requestfactory.shared.ServerFailure;

import ca.jhosek.main.client.activity.ActivityModule;
import ca.jhosek.main.client.place.IndexPlace;
import ca.jhosek.main.client.place.LostAccountPlace;
import ca.jhosek.main.client.ui.anon.LostAccountView;
import ca.jhosek.main.client.ui.anon.LostAccountViewImpl;
import ca.jhosek.main.shared.proxy.AppRequestFactory;
import ca.jhosek.main.shared.proxy.UserRequestContext;

/**
 * used to recover passwords
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see ActivityModule
 * @see LostAccountView
 * @see LostAccountViewImpl
 * @see LostAccountPlace
 * 
 */
public class LostAccountActivity extends AbstractActivity implements
		LostAccountView.Presenter {

	private static final Logger logger = Logger
			.getLogger(LostAccountActivity.class.getName());

	private final LostAccountView view;
	private final PlaceController placeController;
	private final AppRequestFactory requestFactory;

	@Inject
	public LostAccountActivity(final LostAccountView view,
			final PlaceController placeController,
			final AppRequestFactory requestFactory) {
		super();
		this.view = view;
		this.placeController = placeController;
		this.requestFactory = requestFactory;
		view.setPresenter(this);
	}

	@Override
	public void cancel() {
		logger.info("cancel()");
		//
		placeController.goTo(new IndexPlace());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ca.jhosek.main.client.ui.anon.NewStudentPanel.Presenter#cancel()
	 */

	@Override
	public void recoverPassword(final String emailAddress) {
		logger.info("recoverPassword()");

		final UserRequestContext userContext = requestFactory.userRequest();
		// call server with email address
		userContext.recoverPassword(emailAddress).fire(new Receiver<Void>() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see com.google.web.bindery.requestfactory.shared.Receiver#
			 * onConstraintViolation(java.util.Set)
			 */
			@Override
			public void onConstraintViolation(
					final Set<ConstraintViolation<?>> errors) {
				//
				final StringBuffer msg = new StringBuffer(
						"Incomplete or errors found. Please correct and resubmit.");
				for (final ConstraintViolation<?> vio : errors) {
					msg.append("\n* ");
					msg.append(vio.getMessage());
				}
				Window.alert(msg.toString());
				super.onConstraintViolation(errors);
			}

			/**
			 * @see com.google.web.bindery.requestfactory.shared.Receiver#onFailure(com.google.web.bindery.requestfactory.shared.ServerFailure)
			 */
			@Override
			public void onFailure(final ServerFailure error) {
				// Otherwise, show ConstraintViolations in the UI
				logger.warning("recoverPassword() onFailure() called after recoverPassword()");
				Window.alert("Password recovery failure. Please try again.");
			}

			@Override
			public void onSuccess(final Void response) {
				logger.info("recoverPassword() successful recoverPassword()");
				// advise user
				Window.alert("A password recovery email was sent to your mailbox.");
				//
				placeController.goTo(new IndexPlace());

			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ca.jhosek.main.client.ui.anon.LostAccountView.Presenter#recoverPassword
	 * (java.lang.String, java.lang.String)
	 */

	@Override
	public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
		view.clear();
		panel.setWidget(view);

	}
}
