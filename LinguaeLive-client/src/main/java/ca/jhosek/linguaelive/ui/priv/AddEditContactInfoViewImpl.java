/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.ui.priv;

import java.util.Arrays;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.ui.client.ValueBoxEditorDecorator;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

import ca.jhosek.main.client.activity.mainregion.AddEditContactInfoActivity;
import ca.jhosek.main.client.place.AddEditContactInfoPlace;
import ca.jhosek.main.client.ui.EnumRenderer;
import ca.jhosek.main.shared.ContactInfoType;
import ca.jhosek.main.shared.proxy.ContactInfoProxy;

/**
 * add edit contact info page
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see AddEditContactInfoActivity
 * @see AddEditContactInfoView
 * @see AddEditContactInfoPlace
 * 
 */
public class AddEditContactInfoViewImpl extends Composite implements IsWidget, Editor<ContactInfoProxy>, AddEditContactInfoView {
	interface AddEditContactInfoUiBinder extends UiBinder<Widget, AddEditContactInfoViewImpl> {
	}
	
	public interface Driver extends RequestFactoryEditorDriver<ContactInfoProxy, AddEditContactInfoViewImpl> {
	}

	// logger boilerplate
	private static final Logger logger = Logger.getLogger(AddEditContactInfoViewImpl.class
			.getName());

	static EnumRenderer<ContactInfoType> contactTypeRenderer = new EnumRenderer<ContactInfoType>();
	
	private static AddEditContactInfoUiBinder uiBinder = GWT.create(AddEditContactInfoUiBinder.class);
	
	@UiField ValueBoxEditorDecorator<String> info;
	@UiField(provided=true) ValueListBox<ContactInfoType> type= new ValueListBox<ContactInfoType>( contactTypeRenderer );
	// TODO add preferred check box
	@UiField Button saveButton;	
//	@UiField Button saveMoreButton;
	@UiField Button cancelButton;
	
	
	private Presenter presenter;

	public AddEditContactInfoViewImpl() {
		// initialize language widget values
		type.setAcceptableValues( Arrays.asList( ContactInfoType.values()) );
		initWidget(uiBinder.createAndBindUi(this));
	}

	/**
	 * @return a ContactInfoProxy editor driver, userProxyDriver, tied to this presenter and view
	 */
	public Driver createEditorDriver(EventBus eventBus, RequestFactory requestFactory) {
		// 
		logger.info( "createEditorDriver( AddEditContactInfoViewImpl )");
		Driver driver = GWT.create(Driver.class);
		driver.initialize( eventBus, requestFactory, this );
		return driver;
	}


	/**
	 * save edits and go to my profile
	 * @param event
	 */
	@UiHandler("saveButton")
	void onSaveButtonClick(ClickEvent event) {
		// do some error checking here...
		
		// move content from edit widgets to value
		presenter.save();
	}
	

	/**
	 * save edits and add more
	 * @param event
	 */
//	@UiHandler("saveMoreButton")
//	void onSaveMoreButtonClick(ClickEvent event) {
//		// do some error checking here...
//		
//		// move content from edit widgets to value
//		presenter.saveMore();
//	}
	
	/**
	 * abandon edits and go to my profile
	 * 
	 * @param event
	 */
	@UiHandler("cancelButton")
	void onCancelButtonClick(ClickEvent event) {		
		// move content from edit widgets to value
		presenter.cancel();
	}
	
	
	/**
	 * @param presenter the presenter to set
	 */
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
}
