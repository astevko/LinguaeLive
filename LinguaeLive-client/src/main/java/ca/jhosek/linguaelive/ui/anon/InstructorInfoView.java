/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.ui.anon;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import ca.jhosek.linguaelive.activity.mainregion.InstructorInfoActivity;

/**
 * Instructor Information View
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see InstructorInfoActivity
 *
 */
public class InstructorInfoView extends Composite  {

	public interface Presenter {
		void goToNewInstructorForm();
		void goToBrowseCourse();
	}	
	private static InstructorInfoViewImplUiBinder uiBinder = GWT
	.create(InstructorInfoViewImplUiBinder.class);

	interface InstructorInfoViewImplUiBinder extends UiBinder<Widget, InstructorInfoView> {
	}
	private Presenter presenter;

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	public InstructorInfoView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	@UiHandler("newInstructorButton")
	void onNewInstructorButtonClick(ClickEvent event) {
		presenter.goToNewInstructorForm();
	}
	@UiHandler("browseCoursesButton")
	void onBrowseCourseButtonClick(ClickEvent event) {
		presenter.goToBrowseCourse();
	}
}
