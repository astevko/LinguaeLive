/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.ui.priv.admin;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DateLabel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.NumberLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

import ca.jhosek.main.client.ui.LabelEditor;
import ca.jhosek.main.shared.proxy.CourseProxy;

/**
 * admin display of user information
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public class AdminCourseViewImpl extends Composite implements IsWidget, Editor<CourseProxy>, AdminCourseView {
	// logger boilerplate
	private static final Logger logger = Logger.getLogger(AdminCourseViewImpl.class
			.getName());
	
	private static AdminCourseViewerUiBinder uiBinder = GWT
			.create(AdminCourseViewerUiBinder.class);

	interface AdminCourseViewerUiBinder extends UiBinder<Widget, AdminCourseViewImpl> {
	}

	
	@UiField LabelEditor name;
	@UiField LabelEditor description;
	@UiField NumberLabel<Long> estimatedMemberSize;
	@UiField DateLabel startDate;
	@UiField DateLabel endDate;
	@UiField NumberLabel<Long> id;
	@UiField Button saveCourseButton;
	@UiField Button cancelEditsButton;

	private Presenter presenter;
	
	/**
	 * @param presenter the presenter to set
	 */
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	public AdminCourseViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		
//		infoColumn = new Column<ContactInfoProxy, String>( new EditTextCell() ) {
//			
//			@Override
//			public String getValue(ContactInfoProxy object) {
//				// 
//				return object.getInfo();
//			}
//		};
//		contactInfos.addColumn(infoColumn, "phone number" );
	}

	@UiHandler("saveCourseButton")
	void onSaveCourseButtonClick(ClickEvent event) {
		// move content from edit widgets to value
		acceptEdits();
		presenter.saveCourseEdits();
	}
	@UiHandler("cancelEditsButton")
	void onCancelEditsButtonClick(ClickEvent event) {
		resetView();
		presenter.resetCourse();
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.main.client.ui.priv.admin.AdminCourseView#acceptEdits()
	 * 
	 * 
	 * resets the course interface to show labels only
	 */
	public void acceptEdits(){
		// LabelEditors 
		 name.acceptEdits();
		 description.acceptEdits();	
	}
	
	/* (non-Javadoc)
	 * @see ca.jhosek.main.client.ui.priv.admin.AdminCourseView#resetView()
	 * 
	 * reset widgets from edit mode
	 */
	public void resetView() {
		// LabelEditors 
		 name.reset();
		 description.reset();
	}

	public interface Driver extends RequestFactoryEditorDriver<CourseProxy, AdminCourseViewImpl> {
	}
	
	
	/**
	 * @return a CourseProxy editor driver, userProxyDriver, tied to this presenter and view
	 */
	public Driver createEditorDriver(EventBus eventBus, RequestFactory requestFactory) {
		// 
		logger.info( "createEditorDriver( AdminCourseViewerImpl )");
		Driver driver = GWT.create(Driver.class);
		driver.initialize( eventBus, requestFactory, this );
		return driver;
	}


//	/**
//	 * add a contact info object to the list
//	 * @param event
//	 */
//	@UiHandler("addContactInfo")
//	void onAddContactInfoClick(ClickEvent event) {
//		presenter.addContactInfo();
//		contactInfos.redraw();
//	}
}
