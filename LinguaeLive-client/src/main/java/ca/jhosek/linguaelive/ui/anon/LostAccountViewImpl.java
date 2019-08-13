/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.ui.anon;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
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
// import ca.jhosek.linguaelive.activity.mainregion.LostAccountActivity;
// import ca.jhosek.linguaelive.place.LostAccountPlace;

/**
 * @author copyright (C) 2011 Andrew Stevko
 *
 * @see LostAccountView
 * @see LostAccountViewImpl
 * @see LostAccountPlace
 * @see LostAccountActivity
 * 
 */
public class LostAccountViewImpl extends Composite implements LostAccountView {

	interface LostAccountViewUiBinder extends UiBinder<Widget, LostAccountViewImpl> {
	}
	private static LostAccountViewUiBinder  uiBinder = GWT
	.create(LostAccountViewUiBinder.class);

	@UiField TextBox emailAddress;
	@UiField Button recoverButton;
	@UiField Button cancelButton;

	private Presenter presenter;

	private final LLConstants constants;

	@Inject
	public LostAccountViewImpl(LLConstants constants) {
		this.constants = constants;
		initWidget(uiBinder.createAndBindUi(this));

		// 
		emailAddress.addKeyPressHandler( new KeyPressHandler() {
			
			public void onKeyPress(KeyPressEvent event) {
				// 
				if ( event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER ) {
					// enter key pressed submits form
					submitRequest();
				}
			}
		});
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.anon.LostAccountView#clear()
	 */
	public void clear() {
		// 
		emailAddress.setValue("");
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.anon.LostAccountView#setPresenter(ca.jhosek.linguaelive.ui.anon.LostAccountView.Presenter)
	 */
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;

	}

	@UiHandler("cancelButton")
	void onCancelButtonClick(ClickEvent event) {
		clear();
		presenter.cancel();
	}

	@UiHandler("recoverButton")
	void onRecoverPasswordAnchorClick(ClickEvent event) {
		submitRequest();
	}

	private void submitRequest() {
		// 
		String email = emailAddress.getValue();
		if ( email == null || email.isEmpty() || !email.contains("@") ){
//			final String pleaseEnterAValidEmailAddress = "Please enter a valid email address.";
			Window.alert(constants.pleaseEnterAValidEmailAddress());
			return;
		}
		// process recovery request
		presenter.recoverPassword( emailAddress.getValue() );
	}	
}
