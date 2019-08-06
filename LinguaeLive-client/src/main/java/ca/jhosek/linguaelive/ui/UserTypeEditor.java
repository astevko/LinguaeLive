/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.ui;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

import ca.jhosek.main.shared.UserType;

/**
 * Editor widget for User Type
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class UserTypeEditor extends Composite implements IsEditor<Editor<UserType>>, LeafValueEditor<UserType>, HasValue<UserType> {

	interface UserTypeEditorUiBinder extends UiBinder<Widget, UserTypeEditor> {
	}

	
	// logger boilerplate
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(UserTypeEditor.class
			.getName());

	private static UserTypeEditorUiBinder uiBinder = GWT
	.create(UserTypeEditorUiBinder.class);
	
	@UiField ListBox listBox;
	@UiField Label editLabel;
	@UiField DeckPanel deckPanel;
	@UiField FocusPanel focusPanel;

	private UserType value;

//    private HandlerRegistration subscription;
    
	/**
	 * 
	 */
	@UiConstructor
	public UserTypeEditor( String groupName ) {
		initWidget(uiBinder.createAndBindUi(this));

		deckPanel.showWidget(0);

		focusPanel.addFocusHandler(new FocusHandler() {
			public void onFocus(FocusEvent event) {
				switchToEdit();
			}
		});

		editLabel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				switchToEdit();
			}
		});
		
		for ( UserType e: UserType.class.getEnumConstants()){
			listBox.addItem(e.name());
		}	   

	}

	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.TakesValue#setValue(java.lang.Object)
	 */
	public void setValue(UserType value) {
		this.value = value;
		//        
		if (value==null)
			return;
		// set the value of the label
		editLabel.setText(value.name());
		
//		logger.info( "set UserTypeEditor value to " + value.name() );
		int i = 0;
		for ( UserType e: UserType.class.getEnumConstants()){
			if ( e.equals(value)) {
				listBox.setSelectedIndex(i);
				break;
			}
			i++;
		}	   
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.TakesValue#getValue()
	 */
	public UserType getValue() {
		// get the value of the selected index and convert to user type
		value = UserType.valueOf( listBox.getValue( listBox.getSelectedIndex()) );	
//		logger.info( "UserTypeEditor value is " + value.name() );
		return value;
	}

	public Editor<UserType> asEditor() {
		// 
		return this;
	}

//	@Override
//	public void setDelegate(EditorDelegate<UserType> delegate) {
//		// 
//		if ( subscription != null ) {
//			subscription.removeHandler();
//		}
//		subscription = delegate.subscribe();
//		
//	}
//
//	@Override
//	public void flush() {
//		//
//		getValue();
//	}
//
//	@Override
//	public void onPropertyChange(String... paths) {
//		// no-op		
//	}


	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<UserType> handler) {
		// 
		return addHandler( handler, ValueChangeEvent.getType() );
	}

	public void setValue(UserType value, boolean fireEvents) {
		// 
		setValue(value);
	}


	/**
	 * moves view from label to listBox
	 */
	public void switchToEdit() {
		if(deckPanel.getVisibleWidget() == 1) return;
//		listBox.setText(getValue());
		deckPanel.showWidget(1);
		listBox.setFocus(true);
	}

	public void switchToLabel() {
		if(deckPanel.getVisibleWidget() == 0) return;
		setValue( getValue(), true); // fires events, too
		deckPanel.showWidget(0);
	}

	/**
	 * just like switchToLabel but looses edits
	 */
	public void reset() {
		if(deckPanel.getVisibleWidget() == 0) return;
		// widget
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
}
