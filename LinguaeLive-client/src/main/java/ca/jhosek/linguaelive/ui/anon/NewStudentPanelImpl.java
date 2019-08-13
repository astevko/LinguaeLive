/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.ui.anon;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.ui.client.ValueBoxEditorDecorator;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import ca.jhosek.linguaelive.LLConstants;
import ca.jhosek.linguaelive.activity.mainregion.NewStudentFormActivity;

/**
 * New Student Sign up 
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 * @see NewStudentFormActivity
 * @see NewStudentPanel
 * 
 */
public class NewStudentPanelImpl extends Composite implements NewStudentPanel {

	// logger boilerplate
	private static final Logger logger = Logger.getLogger(NewStudentPanelImpl.class
			.getName());

	
	interface Binder extends UiBinder<Widget, NewStudentPanelImpl> {
	}
	private static final Binder binder = GWT.create(Binder.class);
	
	
	//--------- course invite form
	@UiField HTMLPanel courseForm;
	@Ignore	@UiField TextBox courseInviteCode;
	@UiField Button checkInviteCode;

	//--------- user form
	@UiField HTMLPanel userForm;
	// editor fields
	@UiField ValueBoxEditorDecorator<String> firstName;
	@UiField ValueBoxEditorDecorator<String> lastName;
	@UiField ValueBoxEditorDecorator<String> emailAddress;
	@UiField ValueBoxEditorDecorator<String> password;
	@UiField ValueBoxEditorDecorator<String> hint;
//	@UiField ValueBoxEditorDecorator<String> location;
//	@UiField ValueBoxEditorDecorator<String> school;
	
	@UiField CheckBox acceptsTerms;
	// field controls used for form validation
	@Ignore	@UiField PasswordTextBox pswd1TextBox;	
	@Ignore	@UiField PasswordTextBox pswd2TextBox;
	
	// actions
	@UiField Button signUpButton;
	@UiField Button cancelButton;
	
	private Presenter presenter;


	/**
	 * static strings
	 */
	private final LLConstants constants;

	@Inject
	public NewStudentPanelImpl(LLConstants constants) {
		this.constants = constants;
		initWidget(binder.createAndBindUi(this));
	}


	/**
	 * @return the acceptsTerms
	 */
	public Boolean getAcceptsTerms() {
		return acceptsTerms.getValue();
	}

	/** 
	 * save the presenter so that we can trigger actions
	 * @see ca.jhosek.linguaelive.ui.anon.NewStudentPanel#setPresenter(ca.jhosek.linguaelive.ui.anon.NewStudentPanel.Presenter)
	 */
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@UiHandler("checkInviteCode")
	void onCheckInviteCodeButtonClick(ClickEvent event) {
		logger.info( "onCheckInviteCodeButtonClick()");
		presenter.acceptInviteCode( courseInviteCode.getValue().toUpperCase() );
	}

	@UiHandler("cancelButton")
	void onCancelButtonClick(ClickEvent event) {
		presenter.cancel();
	}


	@UiHandler("signUpButton")
	void onSignUpButtonClick(ClickEvent event) {
		logger.info( "onSignUpButton()");
		// compare passwords
		if ( !pswd1TextBox.getValue().equals( pswd2TextBox.getValue() ))	{
//			final String bothPasswordsMustMatch = "Both passwords must match";
			Window.alert(constants.bothPasswordsMustMatch());
			pswd1TextBox.setFocus(true);

		// accepts terms?	
		} else if ( !acceptsTerms.getValue() ) {
//			final String pleaseAcceptTheTermsOfService = "Please accept the terms of service";
			Window.alert(constants.pleaseAcceptTheTermsOfService());
			acceptsTerms.setFocus(true);
			
		} else {
			presenter.createNewStudent();
		}
	}


	/**
	 * show/hide the user form
	 * 
	 * @see ca.jhosek.linguaelive.ui.anon.NewStudentPanel#setUserFormVisibility(boolean)
	 */
	public void setUserFormVisibility(boolean showUserForm) {
		//
		userForm.setVisible(showUserForm);
	}


	/**
	 * show/hide invite code form
	 * 
	 * @see ca.jhosek.linguaelive.ui.anon.NewStudentPanel#setInviteCodeFormVisibility(boolean)
	 */
	public void setInviteCodeFormVisibility(boolean showInviteCodeForm) {
		// 
		courseForm.setVisible( showInviteCodeForm ); 		
	}


	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.anon.NewStudentPanel#setInviteCode(java.lang.String)
	 */
	public void setInviteCode(String courseInviteCode) {
		// 
		this.courseInviteCode.setValue(courseInviteCode);
		
	}


}
