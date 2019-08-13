/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.ui.priv.student;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import ca.jhosek.linguaelive.proxy.UserProxy;

/**
 *  Student user start page 
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class StudentStartViewImpl extends Composite implements StudentStartView {

	interface StartViewUiBinder extends UiBinder<Widget, StudentStartViewImpl> {
	}
	private static StartViewUiBinder uiBinder = GWT
	.create(StartViewUiBinder.class);

	@SuppressWarnings("unused")
	private Presenter presenter;

	public StudentStartViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.priv.student.StudentStartView#clear()
	 */
	public void clear() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.priv.student.StudentStartView#setPresenter(ca.jhosek.linguaelive.ui.priv.student.StudentStartView.Presenter)
	 */
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.priv.student.StudentStartView#setUser(ca.jhosek.linguaelive.User)
	 */
	public void setUser(UserProxy user) {
		// TODO Auto-generated method stub

	}


}
