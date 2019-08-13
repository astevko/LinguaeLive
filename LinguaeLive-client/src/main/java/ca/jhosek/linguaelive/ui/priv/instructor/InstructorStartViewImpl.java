/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.ui.priv.instructor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import ca.jhosek.linguaelive.proxy.UserProxy;

/**
 *  Instructor user start page 
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class InstructorStartViewImpl extends Composite implements InstructorStartView {

	interface StartViewUiBinder extends UiBinder<Widget, InstructorStartViewImpl> {
	}
	private static StartViewUiBinder uiBinder = GWT
	.create(StartViewUiBinder.class);

	@SuppressWarnings("unused")
	private Presenter presenter;

	public InstructorStartViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.priv.student.InstructorStartView#clear()
	 */
	public void clear() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.priv.student.InstructorStartView#setPresenter(ca.jhosek.linguaelive.ui.priv.student.InstructorStartView.Presenter)
	 */
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.priv.student.InstructorStartView#setUser(ca.jhosek.linguaelive.User)
	 */
	public void setUser(UserProxy user) {
		// TODO Auto-generated method stub

	}


}
