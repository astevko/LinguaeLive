/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.ui.anon;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * Privacy Statement
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class PrivacyView extends Composite  {

	private static PrivacyViewImplUiBinder uiBinder = GWT
			.create(PrivacyViewImplUiBinder.class);

	interface PrivacyViewImplUiBinder extends UiBinder<Widget, PrivacyView> {
	}

	public PrivacyView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
