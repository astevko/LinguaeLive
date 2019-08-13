/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.ui.anon;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

// import ca.jhosek.linguaelive.activity.mainregion.LoginFormActivity;
// import ca.jhosek.linguaelive.place.LoginFormPlace;

/**
 * Login Form View
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 * @see LoginFormView
 * @see LoginFormActivity
 * @see LoginFormPlace
 * 
 */
public class LoginFormViewImpl extends Composite implements LoginFormView {

	/**
	 * UI Binder declaration
	 * @author andy
	 *
	 */
	interface LoginDialogBoxUiBinder extends UiBinder<Widget, LoginFormViewImpl> {}
	/**
	 * UI Binder instance 
	 */
	private static LoginDialogBoxUiBinder uiBinder = GWT.create(LoginDialogBoxUiBinder.class);
	
	@UiField TextBox emailAddress;
	@UiField PasswordTextBox pswd1TextBox;
	@UiField Button loginButton;
	@UiField CheckBox rememberMe;
	@UiField Button cancelButton;

	private Presenter presenter;

	public LoginFormViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("cancelButton")
	public void onCancelClick( ClickEvent event ){
		presenter.cancel();
	}
	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.anon.LoginFormView#clear()
	 */
	public void clear( String defaultEmailAddress ) {
		// 
		emailAddress.setValue( defaultEmailAddress==null ? "" : defaultEmailAddress );
		pswd1TextBox.setValue("");
	}

	/**
	 * @see ca.jhosek.linguaelive.ui.anon.LoginFormView#setPresenter(ca.jhosek.linguaelive.ui.anon.LoginFormView.Presenter)
	 */
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;

	}

//	@UiHandler("forgotPasswordAnchor")
//	void onForgotPasswordAnchorClick(ClickEvent event) {
//		presenter.forgotPassword();
//	}

	@UiHandler("loginButton")
	void onLoginButtonClick(ClickEvent event) {
		onLogin();
	}
	
	/**
	 * pressing enter key on password field submits form
	 * 
	 * @param event
	 */
	@UiHandler("pswd1TextBox")
	void onPasswordTextBoxKeyUp(KeyUpEvent event) {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			onLogin();
		}
	}

	/**
	 * 
	 */
	private void onLogin() {
		// process login
		presenter.loginUser( emailAddress.getValue().trim(), pswd1TextBox.getValue(), rememberMe.getValue() );
	}
}

