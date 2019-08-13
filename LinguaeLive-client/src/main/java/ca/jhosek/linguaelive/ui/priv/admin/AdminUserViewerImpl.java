/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.ui.priv.admin;

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
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

import ca.jhosek.linguaelive.activity.mainregion.QueryUsersActivity;
import ca.jhosek.linguaelive.ui.LabelEditor;
import ca.jhosek.linguaelive.ui.NumberEditor;
import ca.jhosek.linguaelive.ui.UserTypeEditor;
import ca.jhosek.linguaelive.ContactInfoType;
import ca.jhosek.linguaelive.proxy.ContactInfoProxy;
import ca.jhosek.linguaelive.proxy.ContactInfoRequestContext;
import ca.jhosek.linguaelive.proxy.UserProxy;

/**
 * admin display of user information
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see AdminUserView
 * @see QueryUsersActivity
 * @see UserProxy
 *
 */
public class AdminUserViewerImpl extends Composite implements IsWidget, Editor<UserProxy>, AdminUserView {

	private static final String NO_CONTACT_INFO = "No contact info found.";
	private static final String LOADING_CONTACT_INFO_PLEASE_WAIT_BRIEFLY = "Loading contact info. Please wait briefly...";

	// logger boilerplate
	private static final Logger logger = Logger.getLogger(AdminUserViewerImpl.class
			.getName());

	private static AdminUserViewerUiBinder uiBinder = GWT
	.create(AdminUserViewerUiBinder.class);

	interface AdminUserViewerUiBinder extends UiBinder<Widget, AdminUserViewerImpl> {
	}


	@UiField LabelEditor firstName;
	@UiField LabelEditor lastName;
	@UiField LabelEditor emailAddress;
	@UiField LabelEditor school;
	@UiField UserTypeEditor userType;
	@UiField NumberLabel<Long> id;
	@UiField DateLabel createDate;
	@UiField NumberEditor timezoneOffset;
	
	@UiField Label currentUserTime;
	@UiField Label userTimeZone;
	@UiField LabelEditor location;
	@UiField Button saveUserButton;
	@UiField Button cancelEditsButton;
	
	@UiField Button changePasswordButton;
	
	@UiField Button viewOwnedCoursesButton;

	//	@UiField CellTable<ContactInfoProxy> contactInfos;// = new CellTable<ContactInfoProxy>();
	//	@UiField Button addContactInfo;
	//	private Column<ContactInfoProxy,String > infoColumn;

	// contact info list 
	@UiField HTMLPanel contactInfoPanel;
	private ListDataProvider<ContactInfoProxy> listContactInfoProvider;
	private final CellTable<ContactInfoProxy> contactInfoCTable;
	private final SimplePager contactInfoPager;	

	private Presenter presenter;

	/**
	 * @param presenter the presenter to set
	 */
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	public AdminUserViewerImpl() {
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

		@UiHandler("saveUserButton")
		void onSaveUserButtonClick(ClickEvent event) {
			// move content from edit widgets to value
			acceptEdits();
			presenter.saveUserEdits();
		}
		@UiHandler("cancelEditsButton")
		void onCancelEditsButtonClick(ClickEvent event) {
			resetView();
			presenter.resetUser();
		}

		@UiHandler("changePasswordButton")
		void onChangePasswordButtonClick(ClickEvent event) {
			presenter.gotoChangePassword();
		}

		/* (non-Javadoc)
		 * @see ca.jhosek.linguaelive.ui.priv.admin.AdminUserView#acceptEdits()
		 * 
		 * 
		 * resets the user interface to show labels only
		 */
		public void acceptEdits(){
			// LabelEditors 
			firstName.acceptEdits();
			lastName.acceptEdits();
			emailAddress.acceptEdits();
			school.acceptEdits();
			location.acceptEdits();
			userType.acceptEdits();
			timezoneOffset.acceptEdits();
		}

		/* (non-Javadoc)
		 * @see ca.jhosek.linguaelive.ui.priv.admin.AdminUserView#resetView()
		 * 
		 * reset widgets from edit mode
		 */
		public void resetView() {
			// LabelEditors 
			firstName.reset();
			lastName.reset();
			emailAddress.reset();
			school.reset();
			location.reset();		
			userType.reset();
			timezoneOffset.reset();
		}

		public interface Driver extends RequestFactoryEditorDriver<UserProxy, AdminUserViewerImpl> {
		}


		/**
		 * @return a UserProxy editor driver, userProxyDriver, tied to this presenter and view
		 */
		public Driver createEditorDriver(EventBus eventBus, RequestFactory requestFactory) {
			// 
			logger.info( "createEditorDriver( AdminUserViewerImpl )");
			Driver driver = GWT.create(Driver.class);
			driver.initialize( eventBus, requestFactory, this );
			return driver;
		}

		/**
		 * show contact info for this user
		 * 
		 * @see ca.jhosek.linguaelive.ui.priv.MyProfileView#showContactInfo(java.util.List)
		 */
		public void showContactInfo(List<ContactInfoProxy> contactInfos) {
			int displaySize = 5;
			// clear panel
			contactInfoPanel.clear();
			// 
			if (contactInfos == null ) {
				logger.info("missing contact info for user" );
				contactInfoPanel.add( new HTML( LOADING_CONTACT_INFO_PLEASE_WAIT_BRIEFLY));

			} else {
				logger.info("contact info for user = " + contactInfos.size() + " records" );

				listContactInfoProvider = new ListDataProvider<ContactInfoProxy>(contactInfos);
				listContactInfoProvider.addDataDisplay(contactInfoCTable);

				contactInfoCTable.setRowCount( contactInfos.size());
				contactInfoCTable.setVisibleRange(0, displaySize );
				contactInfoCTable.setRowData(0, contactInfos);

				contactInfoPanel.add(contactInfoCTable);
			}

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
