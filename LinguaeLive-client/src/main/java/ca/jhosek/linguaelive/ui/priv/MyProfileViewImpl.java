/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.ui.priv;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DateLabel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.NumberLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

import ca.jhosek.linguaelive.LLConstants;
import ca.jhosek.linguaelive.activity.mainregion.MyProfileActivity;
import ca.jhosek.linguaelive.place.MyProfilePlace;
import ca.jhosek.linguaelive.ui.LabelEditor;
import ca.jhosek.linguaelive.ui.ViewModule;
import ca.jhosek.linguaelive.ContactInfoType;
import ca.jhosek.linguaelive.proxy.ContactInfoProxy;
import ca.jhosek.linguaelive.proxy.ContactInfoRequestContext;
import ca.jhosek.linguaelive.proxy.UserProxy;

/**
 *  My Profile page 
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * 
 * @see MyProfileView
 * @see MyProfileActivity
 * @see MyProfilePlace
 *
 * @see ViewModule for registration 
 */
public class MyProfileViewImpl extends Composite implements IsWidget, Editor<UserProxy>, MyProfileView {

	private static final String NO_CONTACT_INFO = "No contact info found. Please click 'add' below.";


//	private static final String LOADING_CONTACT_INFO_PLEASE_WAIT_BRIEFLY = "Loading contact info. Please wait briefly...";


	// logger boilerplate
	private static final Logger logger = Logger.getLogger(MyProfileViewImpl.class.getName());


	private static final int DISPLAY_SIZE = 10;

	interface MyProfileViewImplUiBinder extends UiBinder<Widget, MyProfileViewImpl> {
	}
	private static MyProfileViewImplUiBinder uiBinder = GWT
	.create(MyProfileViewImplUiBinder.class);


	@UiField LabelEditor firstName;
	@UiField LabelEditor lastName;
	@UiField Label emailAddress;
	@UiField LabelEditor school;
	@UiField DateLabel createDate;
	@UiField NumberLabel<Integer> timezoneOffset;
	@UiField Label currentUserTime;
	@UiField LabelEditor location;
	@UiField LabelEditor hint;
	@UiField Button saveUserButton;
	@UiField Button cancelEditsButton;
	
	@UiField Button changePasswordButton;


	// contact info list 
	@UiField HTMLPanel contactInfoPanel;
	private ListDataProvider<ContactInfoProxy> listContactInfoProvider;
	private final CellTable<ContactInfoProxy> contactInfoCTable;
	private final SimplePager contactInfoPager;	
	@UiField Button addContactInfoButton;

	/**
	 * link to take user to Quick Start. 
	 */
	@UiField Anchor linkToQuickStart;
	
	private Presenter presenter;


	private final LLConstants constants;

	@Inject
	public MyProfileViewImpl(LLConstants constants) {
		this.constants = constants;
		initWidget(uiBinder.createAndBindUi(this));

		{
			//----------------------------
			// create contact info celltable, cells, selectionModel, Pager
			// INFO
			EditTextCell infoCell = new EditTextCell();
			Column<ContactInfoProxy, String> infoColumn = new Column<ContactInfoProxy, String>( infoCell ) {

				@Override
				public String getValue(ContactInfoProxy contactInfo) {
					// 
					return contactInfo.getInfo();
				}
			};
			// editable cells require a field updater
			infoColumn.setFieldUpdater( new FieldUpdater<ContactInfoProxy, String>() {

				public void update(int index, ContactInfoProxy contactInfo, String value) {
					// 
					// --- save regularly
					ContactInfoRequestContext contactInfoContext = presenter.getRequestFactory().contactInfoRequest();
					ContactInfoProxy editable;
					editable = contactInfoContext.edit( contactInfo );
					editable.setInfo(value);
					contactInfoContext.persist(editable).fire();

					contactInfoCTable.redraw();
					return;
				}
			});
			//-------------
			// PREFERRED
			Column< ContactInfoProxy, Boolean> preferredColumn = new Column<ContactInfoProxy, Boolean>(new CheckboxCell(true, false)) {

				@Override
				public Boolean getValue(ContactInfoProxy contactInfo) {
					// 
					return contactInfo.getPreferred();
				}
			};
			preferredColumn.setFieldUpdater( new FieldUpdater<ContactInfoProxy, Boolean>() {

				public void update(int index, ContactInfoProxy contactInfo, Boolean value) {
					// 
					// --- save regularly
					ContactInfoRequestContext contactInfoContext = presenter.getRequestFactory().contactInfoRequest();
					ContactInfoProxy editable;
					editable = contactInfoContext.edit( contactInfo );
					editable.setPreferred(value);
					contactInfoContext.persist(editable).fire();

					//					contactInfoCTable.redraw();
				}
			});
			//-------------
			// TYPES
			List<String> typeNames = new ArrayList<String>();
			for( ContactInfoType type : ContactInfoType.values() ) {
				typeNames.add(type.toString());
			}
			SelectionCell typeCell= new SelectionCell( typeNames );
			Column< ContactInfoProxy, String > typeColumn = new Column<ContactInfoProxy, String>( typeCell ) {

				@Override
				public String getValue(ContactInfoProxy contactInfo) {
					// 
					return contactInfo.getType().toString();
				}
			};
			typeColumn.setFieldUpdater( new FieldUpdater<ContactInfoProxy, String>() {

				public void update(int index, ContactInfoProxy contactInfo, String value) {
					// 
					// --- save regularly
					ContactInfoRequestContext contactInfoContext = presenter.getRequestFactory().contactInfoRequest();
					ContactInfoProxy editable;
					editable = contactInfoContext.edit( contactInfo );
					editable.setType( ContactInfoType.valueOf(value));
					contactInfoContext.persist(editable).fire();

					//					contactInfoCTable.redraw();
				}
			});

			//----------------------
			// build table 
			contactInfoCTable = new CellTable<ContactInfoProxy>();
			contactInfoCTable.addColumn( typeColumn, "Type" );
			contactInfoCTable.addColumn( infoColumn, "Number/Id" );
			contactInfoCTable.addColumn( preferredColumn, "Preferred" );

			contactInfoCTable.setEmptyTableWidget(new HTML( NO_CONTACT_INFO ));
			
			contactInfoPager= new SimplePager();
			contactInfoPager.setDisplay(contactInfoCTable);
		}

	}


	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.priv.student.InstructorHomeView#setPresenter(ca.jhosek.linguaelive.ui.priv.student.InstructorHomeView.Presenter)
	 */
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@UiHandler("saveUserButton")
	void onSaveUserButtonClick(ClickEvent event) {
		// move content from edit widgets to value
		acceptEdits();
		presenter.saveUserEdits();
	}
	@UiHandler("cancelEditsButton")
	void onCancelEditsButtonClick(ClickEvent event) {
		resetView();
	}

	@UiHandler("changePasswordButton")
	void onChangePasswordButtonClick(ClickEvent event) {
		presenter.gotoChangePassword();
	}
	
	@UiHandler("linkToQuickStart")
	void onQuickStgartClick(ClickEvent event) {
		presenter.gotoQuickStart();
	}
	
	public interface Driver extends RequestFactoryEditorDriver<UserProxy, MyProfileViewImpl> {
	}


	/**
	 * @return a UserProxy editor driver, userProxyDriver, tied to this presenter and view
	 */
	public Driver createEditorDriver(EventBus eventBus, RequestFactory requestFactory) {
		// 
		logger.info( "createEditorDriver( MyProfileViewImpl )");
		Driver driver = GWT.create(Driver.class);
		driver.initialize( eventBus, requestFactory, this );		
		return driver;
	}


	public void resetView() {
		// 
		logger.info( "resetView() called");
		firstName.reset();
		lastName.reset();
		school.reset();
		location.reset();
		hint.reset();
	}


	public void acceptEdits() {
		//
		logger.info( "accepting edits" );
		firstName.acceptEdits();
		lastName.acceptEdits();
		school.acceptEdits();
		location.acceptEdits();
		hint.acceptEdits();
	}


	/**
	 * show contact info for this user
	 * 
	 * @see ca.jhosek.linguaelive.ui.priv.MyProfileView#showContactInfo(java.util.List)
	 */
	public void showContactInfo(List<ContactInfoProxy> contactInfos) {
		// clear panel
		contactInfoPanel.clear();
		// 
		if (contactInfos == null ) {
			logger.info("missing contact info for user" );
			contactInfoPanel.add( new HTML( constants.loadingContactInfo() ));

		} else {
			logger.info("contact info for user = " + contactInfos.size() + " records" );

			listContactInfoProvider = new ListDataProvider<ContactInfoProxy>(contactInfos);
			listContactInfoProvider.addDataDisplay(contactInfoCTable);

			contactInfoCTable.setRowCount( contactInfos.size());
			contactInfoCTable.setVisibleRange(0, DISPLAY_SIZE);
			contactInfoCTable.setRowData(0, contactInfos);

			contactInfoPanel.add(contactInfoCTable);
		}

	}

	//	/**
	//	 * 
	//	 * display a contact info record
	//	 * 
	//	 * @author copyright (C) 2011 Andrew Stevko
	//	 * 
	//	 */
	//	private static class ContactInfoCell extends AbstractCell<ContactInfoProxy> {
	//
	//		@Override
	//		public void render(com.google.gwt.cell.client.Cell.Context context,
	//				ContactInfoProxy contactInfo, SafeHtmlBuilder sb) {
	//			// 
	//			if( contactInfo==null	) return;
	//			sb.appendHtmlConstant("<div class=\"{style.listPrompt}\" >" );
	//			sb.append( SafeHtmlUtils.fromTrustedString( contactInfo.getType().name() ));
	//			sb.append( ' ' );			
	//			sb.append( SafeHtmlUtils.fromString( contactInfo.getInfo() ));
	//			if ( contactInfo.getPreferred() ) {
	//				// annotate preferred contact info
	//				sb.append( SafeHtmlUtils.fromTrustedString(" * " ));
	//			}
	//			sb.appendHtmlConstant("</div>" );						
	//		}
	//
	//	}
	//
	//	/**
	//	 * what happens when the contact info is selected
	//	 * @author copyright (C) 2011 Andrew Stevko
	//	 *
	//	 */
	//	private final class ContactInfoSelectionHandler implements
	//	SelectionChangeEvent.Handler {
	//		private final SingleSelectionModel<ContactInfoProxy> selectionModel;
	//
	//		private ContactInfoSelectionHandler(
	//				SingleSelectionModel<ContactInfoProxy> selectionModel) {
	//			this.selectionModel = selectionModel;
	//		}
	//
	//		@Override
	//		public void onSelectionChange(SelectionChangeEvent event) {
	//			// 
	//			ContactInfoProxy contactInfo = selectionModel.getSelectedObject();
	//			if( contactInfo != null ) {
	//				presenter.editContactInfo(contactInfo);
	//			}
	//
	//		}
	//	}

	/**
	 * user clicked add contact info button
	 * @param event
	 */
	@UiHandler("addContactInfoButton")
	void onAddContactinfoButtonClick(ClickEvent event) {
		presenter.addNewContactInfo();
	}
}
