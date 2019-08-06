/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.ui.anon;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * Video View 
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class Video2View extends Composite  {

	private static PrivacyViewImplUiBinder uiBinder = GWT
			.create(PrivacyViewImplUiBinder.class);

	interface PrivacyViewImplUiBinder extends UiBinder<Widget, Video2View> {
	}

	public Video2View() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
