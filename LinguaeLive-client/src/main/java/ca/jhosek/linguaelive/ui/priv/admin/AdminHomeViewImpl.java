/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.ui.priv.admin;


import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import ca.jhosek.linguaelive.proxy.UserProxy;

/**
 *  Admin user home page 
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class AdminHomeViewImpl extends Composite implements AdminHomeView {

	interface HomeViewUiBinder extends UiBinder<Widget, AdminHomeViewImpl> {
	}
	private static HomeViewUiBinder uiBinder = GWT
	.create(HomeViewUiBinder.class);

	@SuppressWarnings("unused")
	private Presenter presenter;

	public AdminHomeViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.priv.student.AdminHomeView#clear()
	 */
	public void clear() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.priv.student.AdminHomeView#setPresenter(ca.jhosek.linguaelive.ui.priv.student.AdminHomeView.Presenter)
	 */
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.priv.student.AdminHomeView#setUser(ca.jhosek.linguaelive.User)
	 */
	public void setUser(UserProxy user) {
		// TODO Auto-generated method stub

	}


}
