/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.widgets;

import java.util.logging.Logger;

import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.editor.ui.client.adapters.HasTextEditor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.inject.Inject;

/**
 * shows combined Label and TextBox
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class EditLabel extends Composite implements IsEditor<LeafValueEditor<String>>, HasText {
	// logger boilerplate
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(EditLabel.class.getName());
	
	private FlowPanel panel;
	private Label label;
	private TextBox textbox;
	private boolean editing = false;

	@Inject
	public EditLabel() {
		
		label = new Label("");
		panel.add( label );
		initWidget(panel);
		
	}

//	/* (non-Javadoc)
//	 * @see com.google.gwt.user.client.TakesValue#setValue(java.lang.Object)
//	 */
//	@Override
//	public void setValue(String value) {
//		// 
//		label.setText(value);
//	}
//
//	/* (non-Javadoc)
//	 * @see com.google.gwt.user.client.TakesValue#getValue()
//	 */
//	@Override
//	public String getValue() {
//		// 
//		return (editing)?textbox.getValue():label.getText();
//
//	}

	/* (non-Javadoc)
	 * @see com.google.gwt.editor.client.IsEditor#asEditor()
	 */
	public LeafValueEditor<String> asEditor() {
		// 
		return HasTextEditor.of(this);
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.HasText#getText()
	 */
	public String getText() {
		// 
		return (editing)?textbox.getText():label.getText();
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.HasText#setText(java.lang.String)
	 */
	public void setText(String text) {
		// 
		label.setText(text);
	}


}
