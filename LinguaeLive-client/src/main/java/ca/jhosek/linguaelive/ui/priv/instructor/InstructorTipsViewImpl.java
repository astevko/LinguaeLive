/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.ui.priv.instructor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import ca.jhosek.main.client.activity.mainregion.InstructorTipsActivity;
import ca.jhosek.main.client.place.InstructorTipsPlace;
import ca.jhosek.main.shared.proxy.UserProxy;

/**
 *  Instructor user home page 
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see InstructorTipsPlace
 * @see InstructorTipsActivity
 */
public class InstructorTipsViewImpl extends Composite implements InstructorTipsView {

	interface InstructorTipsViewUiBinder extends UiBinder<Widget, InstructorTipsViewImpl> {
	}
	private static InstructorTipsViewUiBinder uiBinder = GWT
	.create(InstructorTipsViewUiBinder.class);

	@SuppressWarnings("unused")
	private Presenter presenter;

	public InstructorTipsViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.main.client.ui.priv.student.InstructorHomeView#clear()
	 */
	public void clear() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.jhosek.main.client.ui.priv.student.InstructorHomeView#setPresenter(ca.jhosek.main.client.ui.priv.student.InstructorHomeView.Presenter)
	 */
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.main.client.ui.priv.student.InstructorHomeView#setUser(ca.jhosek.main.shared.User)
	 */
	public void setUser(UserProxy user) {
		// TODO Auto-generated method stub

	}


}
