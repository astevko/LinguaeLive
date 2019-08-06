package ca.jhosek.main.client.widgets;

import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.editor.client.adapters.TakesValueEditor;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;

public class EnumInlineLabel< T extends Enum<T>> extends Composite implements IsEditor<LeafValueEditor<T>>, TakesValue<T>{
	private final InlineLabel label;
	
	private T value;
	
	public EnumInlineLabel() {
		// 
		label = new InlineLabel();
		super.initWidget(label);
	}
	
	public T getValue() {
		// 
		return value;
	}

	public void setValue(T value) {
		// 
		this.value = value;
		if ( value != null )
			label.setText(value.name());
		else 
			label.setText("-----");
	}

	public LeafValueEditor<T> asEditor() {
		// 
		return TakesValueEditor.of( this );
	}

}
