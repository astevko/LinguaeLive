/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.ui.priv.admin;


import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import ca.jhosek.main.shared.proxy.UserProxy;

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
	 * @see ca.jhosek.main.client.ui.priv.student.AdminHomeView#clear()
	 */
	public void clear() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.jhosek.main.client.ui.priv.student.AdminHomeView#setPresenter(ca.jhosek.main.client.ui.priv.student.AdminHomeView.Presenter)
	 */
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.main.client.ui.priv.student.AdminHomeView#setUser(ca.jhosek.main.shared.User)
	 */
	public void setUser(UserProxy user) {
		// TODO Auto-generated method stub

	}


}
