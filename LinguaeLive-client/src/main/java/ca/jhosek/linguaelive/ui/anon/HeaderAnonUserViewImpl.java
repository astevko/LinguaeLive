/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.ui.anon;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

import ca.jhosek.linguaelive.activity.userregion.HeaderAnonUserActivity;

/**
 * header user region view for anonymous users 
 * 
 * @author copyright (C) 2011,2012,2013 Andrew Stevko
 * 
 * @see HeaderAnonUserView
 * @see HeaderAnonUserActivity
 *
 */
public class HeaderAnonUserViewImpl extends Composite implements HeaderAnonUserView {

	interface HeaderAnonUserViewImplUiBinder extends UiBinder<Widget, HeaderAnonUserViewImpl> {
	}
	
	private static HeaderAnonUserViewImplUiBinder uiBinder = GWT
			.create(HeaderAnonUserViewImplUiBinder.class);
	@UiField Button loginButton;
	@UiField ListBox selectLanguage;

	private Presenter presenter;

	public HeaderAnonUserViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		
		// initialize select language box
		selectLanguage.addItem("Select a language");
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.pub.HeaderLoginView#setPresenter(ca.jhosek.linguaelive.ui.pub.HeaderLoginView.Presenter)
	 */
	public void setPresenter(Presenter presenter) {
		// 
		this.presenter = presenter;
		
		final LocaleInfo currentLocale = LocaleInfo.getCurrentLocale();
		final String[] availableLocales = LocaleInfo.getAvailableLocaleNames();
		for (String availableLocale : availableLocales) {
			// add locales to the box
			final String localeNativeDisplayName = LocaleInfo.getLocaleNativeDisplayName(availableLocale);
			if (localeNativeDisplayName != null) {
				selectLanguage.addItem( localeNativeDisplayName, availableLocale);
				if (currentLocale.getLocaleName().equals(availableLocale)) {
					// select this one...
					selectLanguage.setItemSelected(selectLanguage.getItemCount()-1, true);
				}
			}
		}
	}

	@UiHandler("loginButton")
	void onLoginButtonClick(ClickEvent event) {
		presenter.loginUser();
	}
	@UiHandler("selectLanguage")
	void onSelectLanguage(ChangeEvent  event) {
		presenter.changeLocale(selectLanguage.getValue(selectLanguage.getSelectedIndex()));
	}
}
