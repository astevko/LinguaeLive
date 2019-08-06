/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.activity.menuregion;



import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import ca.jhosek.main.client.place.FaqPlace;
import ca.jhosek.main.client.place.IndexPlace;
import ca.jhosek.main.client.place.InstructorInfoPlace;
import ca.jhosek.main.client.place.StudentInfoPlace;
import ca.jhosek.main.client.place.WhatIsPlace;
import ca.jhosek.main.client.place.WhoIsPlace;
import ca.jhosek.main.client.ui.anon.AnonMenuView;
import ca.jhosek.main.client.ui.anon.AnonMenuViewImpl;

/**
 * menu activity for anon users
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see AnonMenuViewImpl
 * @see AnonMenuView
 */
public class AnonMenuActivity extends AbstractActivity implements AnonMenuView.Presenter {
	private final AnonMenuView view;
	private final PlaceController placeController;
	
	/**
	 * 
	 */
	@Inject
	public AnonMenuActivity( AnonMenuView view, PlaceController placeController ) {
		this.view = view;
		this.placeController = placeController;
		// 
		view.setPresenter( this );
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.activity.shared.Activity#start(com.google.gwt.user.client.ui.AcceptsOneWidget, com.google.web.bindery.event.shared.EventBus)
	 */
	
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(view);
		
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.main.client.ui.anon.AnonMenuView.Presenter#goToWhatIs()
	 */
	
	public void goToWhatIs() {
		placeController.goTo( new WhatIsPlace() );
		
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.main.client.ui.anon.AnonMenuView.Presenter#goToFaq()
	 */
	
	public void goToFaq() {
		placeController.goTo( new FaqPlace() );
		
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.main.client.ui.anon.AnonMenuView.Presenter#goToInstructorInfo()
	 */
	
	public void goToInstructorInfo() {
		placeController.goTo( new InstructorInfoPlace() );
		
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.main.client.ui.anon.AnonMenuView.Presenter#goToStudentInfo()
	 */
	
	public void goToStudentInfo() {
		placeController.goTo( new StudentInfoPlace() );
		
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.main.client.ui.anon.AnonMenuView.Presenter#goToWhoIs()
	 */
	
	public void goToWhoIs() {
		placeController.goTo( new WhoIsPlace() );
		
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.main.client.ui.anon.AnonMenuView.Presenter#goToIndex()
	 */
	
	public void goToIndex() {
		placeController.goTo( new IndexPlace() );
		
	}
}
