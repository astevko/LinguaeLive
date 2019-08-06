/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.ui.priv;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import ca.jhosek.main.shared.proxy.UserProxy;

/**
 * How we display the current logged in user
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class HeaderUserViewImpl extends Composite implements HeaderUserView {

	private static HeaderLoggedInViewImplUiBinder uiBinder = GWT
			.create(HeaderLoggedInViewImplUiBinder.class);
	
	@UiField Button logoutButton;
	@UiField Label userNameLabel;
	@UiField Label userSchoolLabel;
	private Presenter presenter;

	interface HeaderLoggedInViewImplUiBinder extends
			UiBinder<Widget, HeaderUserViewImpl > {
	}

	/**
	 * Because this class has a default constructor, it can
	 * be used as a binder template. In other words, it can be used in other
	 * *.ui.xml files as follows:
	 * <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 *   xmlns:g="urn:import:**user's package**">
	 *  <g:**UserClassName**>Hello!</g:**UserClassName>
	 * </ui:UiBinder>
	 * Note that depending on the widget that is used, it may be necessary to
	 * implement HasHTML instead of HasText.
	 */
	public HeaderUserViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.main.client.ui.HeaderLoggedInView#setPresenter(ca.jhosek.main.client.ui.HeaderLoggedInView.Presenter)
	 */
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		// 
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.main.client.ui.HeaderLoggedInView#setUser(ca.jhosek.main.shared.User)
	 */
	public void setUser(UserProxy user) {
		// 
		if ( user != null ) {
			userNameLabel.setText( user.getFirstName() + " " + user.getLastName() );
			userSchoolLabel.setText(user.getSchool());
		} else {
			userNameLabel.setText( "" );
			userSchoolLabel.setText( "" );
		}
	
	}

	@UiHandler("logoutButton")
	void onLogoutButtonClick(ClickEvent event) {
		presenter.logoutUser();
	}
}
