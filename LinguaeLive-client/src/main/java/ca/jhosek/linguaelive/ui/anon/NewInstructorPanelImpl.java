/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.ui.anon;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.ui.client.ValueBoxEditorDecorator;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

import ca.jhosek.linguaelive.LLConstants;
import ca.jhosek.linguaelive.activity.mainregion.NewInstructorFormActivity;
import ca.jhosek.linguaelive.activity.mainregion.NewInstructorFormActivity.Driver;

/**
 * @author copyright (C) 2011 Andrew Stevko
 * @see NewInstructorFormActivity
 *
 */
public class NewInstructorPanelImpl extends Composite implements NewInstructorPanel {

	interface Binder extends UiBinder<Widget, NewInstructorPanelImpl> {
	}
	private static final Binder binder = GWT.create(Binder.class);
	// editor fields
	@UiField ValueBoxEditorDecorator<String> firstName;
	@UiField ValueBoxEditorDecorator<String> lastName;
	@UiField ValueBoxEditorDecorator<String> emailAddress;
	@UiField ValueBoxEditorDecorator<String> password;
	@UiField ValueBoxEditorDecorator<String> location;
	@UiField ValueBoxEditorDecorator<String> school;
	@UiField ValueBoxEditorDecorator<String> hint;
//	@Ignore @UiField UserTypeEditor userType;
	
	@UiField CheckBox acceptsTerms;
	@Ignore @UiField TextBox  emailTextBox;
	
	// field controls used for form validation
	@Ignore	@UiField PasswordTextBox pswd1TextBox;	
	@Ignore	@UiField PasswordTextBox pswd2TextBox;
	
	// actions
	@UiField Button signUpButton;	
	@UiField Button cancelButton;
	
	private Presenter presenter;
	private final LLConstants constants;

	@Inject
	public NewInstructorPanelImpl(LLConstants constants) {
		this.constants = constants;
		initWidget(binder.createAndBindUi(this));
	}


	/**
	 * @return the acceptsTerms
	 */
	public Boolean getAcceptsTerms() {
		return acceptsTerms.getValue();
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.anon.NewInstructorPanel#getEmailAddress()
	 */
	public String getEmailAddress() {
		// 
		return emailTextBox.getValue();
	}


	@UiHandler("cancelButton")
	void onCancelButtonClick(ClickEvent event) {
		presenter.cancel();
	}


	@UiHandler("signUpButton")
	void onSignUpButtonClick(ClickEvent event) {

		// compare passwords
		if ( !pswd1TextBox.getValue().equals( pswd2TextBox.getValue() ))	{
			Window.alert(constants.bothPasswordsMustMatch());
			pswd1TextBox.setFocus(true);

		// accepts terms?	
		} else if ( !acceptsTerms.getValue() ) {
			Window.alert(constants.pleaseAcceptTheTermsOfService());
			acceptsTerms.setFocus(true);
			
		} else {
			presenter.createNewInstructor();
		}
	}


	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.anon.NewInstructorPanel#setPresenter(ca.jhosek.linguaelive.ui.anon.NewInstructorPanel.Presenter)
	 */
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	/**
	 * @return an editor driver tied to this class
	 */
	public Driver createEditorDriver(EventBus eventBus, RequestFactory requestFactory) {
		// 
		Driver driver = GWT.create(Driver.class);
		driver.initialize( eventBus, requestFactory, this);
		return driver;
	}

}
