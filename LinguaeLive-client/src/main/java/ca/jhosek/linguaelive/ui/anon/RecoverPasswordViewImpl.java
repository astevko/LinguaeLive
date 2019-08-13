/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.ui.anon;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import ca.jhosek.linguaelive.LLConstants;
import ca.jhosek.linguaelive.activity.mainregion.RecoverPasswordActivity;
import ca.jhosek.linguaelive.place.RecoverPasswordPlace;
import ca.jhosek.linguaelive.ui.ViewModule;

/**
 * Recover Passwords by sending a hash + url to the user's email. Accept the
 * email + hash @ the url to open a reset password & hint form Will overwrite w/
 * new password and hint
 * 
 * @author copyright (C) 2012 Andrew Stevko
 * 
 * @see ViewModule registered here
 * @see RecoverPasswordView
 * @see RecoverPasswordViewImpl
 * @see RecoverPasswordPlace
 * @see RecoverPasswordActivity
 * 
 */
public class RecoverPasswordViewImpl extends Composite implements
		RecoverPasswordView {

	interface MyViewUiBinder extends UiBinder<Widget, RecoverPasswordViewImpl> {
	}

	private static MyViewUiBinder uiBinder = GWT.create(MyViewUiBinder.class);

	// Panel 1
	@UiField
	TextBox emailAddress;
	@UiField
	TextBox recoveryKey;
	@UiField
	Button recoverButton;

	private Presenter presenter;

	private final LLConstants constants;

	@Inject
	public RecoverPasswordViewImpl(final LLConstants constants) {
		this.constants = constants;
		initWidget(uiBinder.createAndBindUi(this));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ca.jhosek.linguaelive.ui.anon.LostAccountView#clear()
	 */
	@Override
	public void clear() {
		//
		emailAddress.setValue("");
		recoveryKey.setValue("");
	}

	/**
	 * Check the user email & hash and open the reset password panel.
	 * 
	 * @param event
	 */
	@UiHandler("recoverButton")
	void onCheckAndOpenResetPanel(final ClickEvent event) {
		// checkAndOpenResetPanel
		// gather inputs
		final String email = emailAddress.getValue().trim();
		final String hash = recoveryKey.getValue().trim();

		// check inputs
		if (email == null || email.isEmpty() || !email.contains("@")) {
			// final String pleaseEnterAValidEmailAddress =
			// "Please enter a valid email address.";
			Window.alert(constants.pleaseEnterAValidEmailAddress());
			return;

		} else if (hash == null || hash.isEmpty()) {
			// final String pleaseEnterAValidEmailHash =
			// "Please enter a valid email hash.";
			Window.alert(constants.bothPasswordsMustMatch());
			return;

		} else {
			// finally send off checking to the presenter/activity
			presenter.checkOkEmailHash(email, hash);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ca.jhosek.linguaelive.ui.anon.LostAccountView#setPresenter(ca.jhosek.
	 * main.client.ui.anon.LostAccountView.Presenter)
	 */
	@Override
	public void setPresenter(final Presenter presenter) {
		this.presenter = presenter;
	}
}
