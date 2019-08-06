/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.ui.anon;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * Who Is View 
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class WhoIsView extends Composite  {

	private static WhoIsViewUiBinder uiBinder = GWT
			.create(WhoIsViewUiBinder.class);

	interface WhoIsViewUiBinder extends UiBinder<Widget, WhoIsView> {
	}

	public WhoIsView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
