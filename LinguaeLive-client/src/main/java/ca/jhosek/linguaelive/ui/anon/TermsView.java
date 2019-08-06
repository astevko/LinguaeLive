/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.ui.anon;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * Terms Statement
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class TermsView extends Composite  {

	private static TermsViewImplUiBinder uiBinder = GWT
			.create(TermsViewImplUiBinder.class);

	interface TermsViewImplUiBinder extends UiBinder<Widget, TermsView> {
	}

	public TermsView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
