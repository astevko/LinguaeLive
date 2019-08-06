/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.ui.anon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

import ca.jhosek.main.client.LLConstants;
import ca.jhosek.main.client.activity.mainregion.ContactUsActivity;
import ca.jhosek.main.client.place.ContactUsPlace;
import ca.jhosek.main.shared.proxy.ContactUsProxy;

/**
 * contact us form
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see ContactUsActivity
 * @see ContactUsView
 * @see ContactUsPlace
 * @see ContactUsProxy
 *
 */
public class ContactUsViewImpl extends Composite implements IsWidget, Editor<ContactUsProxy>, ContactUsView {
	// logger boilerplate
	private static final Logger logger = Logger.getLogger(ContactUsViewImpl.class.getName());

	interface LoginDialogBoxUiBinder extends UiBinder<Widget, ContactUsViewImpl> {
	}
	private static LoginDialogBoxUiBinder uiBinder = GWT
	.create(LoginDialogBoxUiBinder.class);

	public interface Driver extends RequestFactoryEditorDriver<ContactUsProxy, ContactUsViewImpl> {
	}
	
	/**
	 * list of accept
	 */
	private final ArrayList<String> values;
	
	@UiField(provided=true) 
	ValueListBox<String> topic = new ValueListBox<String>( new Renderer<String>() {

		public String render(String object) {
			// 
			if ( object == null ) {
				return constants.contactUsReason(); // "Please select a reason for contact";
			}
			return object;
		}

		public void render(String object, Appendable appendable)
				throws IOException {
			// 
			appendable.append(object);
		}

	}) ;
	@UiField TextArea messageBody;
	@UiField TextBox name;
	@UiField TextBox emailAddress;
	
	@UiField Button submitButton;
	@UiField Button cancelButton;

	private Presenter presenter;

	private final LLConstants constants;

	@Inject
	public ContactUsViewImpl(LLConstants constants) {
		this.constants = constants;
		initWidget(uiBinder.createAndBindUi(this));
		
		values = new ArrayList<String>( 
				Arrays.asList(
						constants.contactUsReason1(),
						constants.contactUsReason2(),
						constants.contactUsReason3(),
						constants.contactUsReason4(),
						constants.contactUsReason5(),
						constants.contactUsReason6()
				) ); 
		topic.setAcceptableValues(values);
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.main.client.ui.anon.ContactUsView#clear()
	 */
	public void clear() {
		// 
		emailAddress.setValue("");
		messageBody.setValue("");
		name.setValue("");
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.main.client.ui.anon.ContactUsView#setPresenter(ca.jhosek.main.client.ui.anon.ContactUsView.Presenter)
	 */
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;

	}

	@UiHandler("cancelButton")
	void onCancelButtonClick(ClickEvent event) {
		clear();
		presenter.cancel();
	}
	
	@UiHandler("submitButton")
	void onSubmitButtonClick(ClickEvent event) {
		// 
		presenter.submit();
	}

	public Driver createEditorDriver(EventBus eventBus,
			RequestFactory requestFactory) {
		logger.info( "ContactUsViewImpl.createEditorDriver()");
		Driver driver = GWT.create( Driver.class );
		driver.initialize( eventBus, requestFactory, this);
		return driver;
	}
}

