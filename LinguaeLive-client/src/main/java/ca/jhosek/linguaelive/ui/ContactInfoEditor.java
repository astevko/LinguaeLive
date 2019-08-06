/**
 * 
 */
package ca.jhosek.main.client.ui;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import ca.jhosek.main.shared.proxy.ContactInfoProxy;

/**
 * Shows the contact info and allows for direct editing
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class ContactInfoEditor extends Composite implements HasValue<ContactInfoProxy>, Editor<ContactInfoProxy> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger( ContactInfoEditor.class.getName());
	
	private static ContactInfoEditorUiBinder uiBinder = GWT
			.create(ContactInfoEditorUiBinder.class);

	interface ContactInfoEditorUiBinder extends
			UiBinder<Widget, ContactInfoEditor> {
	}

	@UiField FocusPanel focusPanel;
	@UiField DeckPanel deckPanel;
	
	@Ignore @UiField Label type;
	@UiField Label info;
	@Ignore @UiField TextBox typeBox;
	@Ignore @UiField TextBox infoBox; 
	
	private ContactInfoProxy contactInfo;
	
	public ContactInfoEditor() {
		initWidget(uiBinder.createAndBindUi(this));


		deckPanel.showWidget(0);

		focusPanel.addFocusHandler(new FocusHandler() {
			public void onFocus(FocusEvent event) {
				switchToEdit();
			}
		});

		info.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				switchToEdit();
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.event.logical.shared.HasValueChangeHandlers#addValueChangeHandler(com.google.gwt.event.logical.shared.ValueChangeHandler)
	 */
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<ContactInfoProxy> handler) {
		// 
		return addHandler( handler, ValueChangeEvent.getType() );
	}
	
	public void switchToEdit() {
		if(deckPanel.getVisibleWidget() == 1) return;
		setValue(getValue());
		deckPanel.showWidget(1);
		infoBox.setFocus(true);
	}

	public void switchToLabel() {
		if(deckPanel.getVisibleWidget() == 0) return;
		setValue(getValue(), true); // fires events, too
		deckPanel.showWidget(0);
	}
	/**
	 * just like switchToLabel but looses edits
	 */
	public void reset() {
		if(deckPanel.getVisibleWidget() == 0) return;
		// reset text box
		setValue( getValue() );
		// setValue(editBox.getText(), true); // fires events, too
		deckPanel.showWidget(0);
	}
	/**
	 * accepts edits and clears ui
	 */
	public void acceptEdits() {
		switchToLabel();
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.HasValue#setValue(java.lang.Object)
	 */
	public void setValue( ContactInfoProxy val ){
		if (val == null ) 
			return;
		
		// save locally
		contactInfo = val;
		
		// update type label & box
		type.setText( val.getType().name() );
		typeBox.setText( val.getType().name() );
		
		// update info label & box
		info.setText(val.getInfo());
		infoBox.setText(val.getInfo());		
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.HasValue#getValue()
	 */
	public ContactInfoProxy getValue() {
		if ( contactInfo == null )
			return null;
		
		// TODO set type
		contactInfo.setInfo( infoBox.getText());
		return contactInfo;
	}

	public void setValue(ContactInfoProxy value, boolean fireEvents) {
		// 
		if( fireEvents ) ValueChangeEvent.fireIfNotEqual(this, getValue(), value );
		setValue( value );
		
	}
}
