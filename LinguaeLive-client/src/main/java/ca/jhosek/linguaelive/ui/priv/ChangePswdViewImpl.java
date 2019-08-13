/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.ui.priv;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

import ca.jhosek.linguaelive.activity.mainregion.ChangePswdActivity;
import ca.jhosek.linguaelive.place.ChangePswdPlace;
import ca.jhosek.linguaelive.ui.ViewModule;
import ca.jhosek.linguaelive.proxy.UserProxy;

/**
 *  Change password page inherited from MyProfileView 
 * 
 * @author copyright (C) 2012 Andrew Stevko
 * 
 * @see ChangePswdView
 * @see ChangePswdActivity
 * @see ChangePswdPlace
 * 
 * @see ViewModule for registration
 * 
 */
public class ChangePswdViewImpl extends Composite implements IsWidget, Editor<UserProxy>, ChangePswdView {


	// logger boilerplate
	private static final Logger logger = Logger.getLogger(ChangePswdViewImpl.class.getName());


	interface ViewImplUiBinder extends UiBinder<Widget, ChangePswdViewImpl> {
	}
	private static ViewImplUiBinder uiBinder = GWT.create(ViewImplUiBinder.class);


	@UiField Label firstName;
	@UiField Label lastName;
	@UiField TextBox password;
	@UiField TextBox hint;
	
	@UiField Button saveUserButton;
	@UiField Button cancelButton;

	private Presenter presenter;

	public ChangePswdViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		
		resetFocus();
	}		


	@Override
	public void resetFocus() {
		// select all
		password.selectAll();
		// set focus 
		password.setFocus(true);
	}


	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.priv.student.InstructorHomeView#setPresenter(ca.jhosek.linguaelive.ui.priv.student.InstructorHomeView.Presenter)
	 */
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@UiHandler("saveUserButton")
	void onSaveUserButtonClick(ClickEvent event) {
		// move content from edit widgets to value
		presenter.saveUserEdits();
	}
	@UiHandler("cancelButton")
	void onCancelButtonClick(ClickEvent event) {
//		resetView();
		presenter.gotoHomePlace();
	}

	
	public interface Driver extends RequestFactoryEditorDriver<UserProxy, ChangePswdViewImpl> {
	}


	/**
	 * @return a UserProxy editor driver, userProxyDriver, tied to this presenter and view
	 */
	public Driver createEditorDriver(EventBus eventBus, RequestFactory requestFactory) {
		// 
		logger.info( "createEditorDriver( ChangePswdViewImpl )");
		Driver driver = GWT.create(Driver.class);
		driver.initialize( eventBus, requestFactory, this );		
		return driver;
	}


	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.priv.ChangePswdView#getNewPassword()
	 */
	@Override
	public String getNewPassword() {
		return password.getValue();
	}

}
