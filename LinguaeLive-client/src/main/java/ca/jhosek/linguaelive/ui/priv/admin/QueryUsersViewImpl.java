/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.linguaelive.ui.priv.admin;

import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import ca.jhosek.linguaelive.activity.mainregion.QueryUsersActivity;
import ca.jhosek.linguaelive.place.QueryUsersPlace;
import ca.jhosek.linguaelive.proxy.ContactInfoProxy;
import ca.jhosek.linguaelive.proxy.UserProxy;

/**
 *  Admin query users view
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 * @see QueryUsersView
 * @see QueryUsersViewImpl
 * @see QueryUsersActivity
 * @see QueryUsersPlace
 * 
 */
public class QueryUsersViewImpl extends Composite implements QueryUsersView {

	private static final String LIST_PROMPT_DIV_W_STYLE = "<div style=\"font-weight: bold;cursor: pointer;color: #d35e46;\" >";

	private static final int VISIBLE_ROWS_INT = 25;

	private static final Logger logger = Logger.getLogger( QueryUsersViewImpl.class.getName() );
	
	/**
	 * what happens when the user selects the list
	 * @author copyright (C) 2011 Andrew Stevko
	 *
	 */
	private final class SelectionChangeHandler implements
			SelectionChangeEvent.Handler {
		private final SingleSelectionModel<UserProxy> selectionModel;

		private SelectionChangeHandler(
				SingleSelectionModel<UserProxy> selectionModel) {
			this.selectionModel = selectionModel;
		}

		public void onSelectionChange(SelectionChangeEvent event) {
			// 
			UserProxy user = selectionModel.getSelectedObject();
			if( user != null ) {
				userProfilePanel.setVisible(true);
				presenter.showUser(user);
			}
			
		}
	}



	interface QueryUsersViewUiBinder extends UiBinder<Widget, QueryUsersViewImpl> {
	}
	private static QueryUsersViewUiBinder uiBinder = GWT
	.create(QueryUsersViewUiBinder.class);

	@UiField TextBox emailAddressFilter;
	
	@UiField FlowPanel userListPanel;
	@UiField Button executeQueryButton;
	@UiField AdminUserViewerImpl userProfilePanel;

	@UiField HTML userListMessage;
	
	private Presenter presenter;
	
	private final CellList<UserProxy> celllist;
	private final SimplePager pager;

	public QueryUsersViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		{
			// create a cell list of users
			UserProxyCell cell = new UserProxyCell();
			celllist = new CellList<UserProxy>( cell );
			
			final SingleSelectionModel<UserProxy> selectionModel = new SingleSelectionModel<UserProxy>();
			celllist.setSelectionModel(selectionModel);
			selectionModel.addSelectionChangeHandler( new SelectionChangeHandler(selectionModel) );
			
			pager = new SimplePager();
			pager.setDisplay(celllist);
		}
		userProfilePanel.setVisible(false);
		
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.priv.student.AdminHomeView#clear()
	 */
	public void clear() {
		// 
		emailAddressFilter.setValue("");
		userListPanel.clear();
		userProfilePanel.setVisible(false);
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.priv.student.AdminHomeView#setPresenter(ca.jhosek.linguaelive.ui.priv.student.AdminHomeView.Presenter)
	 */
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	/* (non-Javadoc)
	 * @see ca.jhosek.linguaelive.ui.priv.admin.QueryUsersView#emptyQueryResults()
	 */
	public void emptyQueryResults() {
		//
		userListPanel.clear();
		final String noUsersFound = "No users found.";
		userListPanel.add( new Label(noUsersFound));		
	}

//	/* (non-Javadoc)
//	 * @see ca.jhosek.linguaelive.ui.priv.admin.QueryUsersView#addQueryResult(java.lang.String, java.lang.Long)
//	 */
//	@Override
//	public void addQueryResult(final String name, final Long id) {
//		// make safe name 
//		SafeHtmlBuilder nameBuilder = new SafeHtmlBuilder();
//		nameBuilder.appendEscaped(name);
//		
//		HTML nameLabel = new HTML(nameBuilder.toSafeHtml().asString());
//		nameLabel.addClickHandler( new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				// click will show user info in the right panel 
//				presenter.showUser(id);
//			}
//		});
//		userListPanel.add(nameLabel);
//		userListPanel.add( new HTML("<br />"));
//	}
	public void showUserList( List<UserProxy> users ){
		logger.info( "showUserList( count = " + users.size() + " )" );

		final String selectAUser = "Select a user";
		userListMessage.setText(selectAUser );
		
		ListDataProvider<UserProxy> dataProvider = new ListDataProvider<UserProxy>();
		dataProvider.setList(users);
		dataProvider.addDataDisplay(celllist);
		
		celllist.setRowCount(users.size());
		celllist.setVisibleRange(0, VISIBLE_ROWS_INT);
		celllist.setRowData(0, users);

		userListPanel.clear();		
		userListPanel.add(celllist);
		userListPanel.add(pager);
		
	}
	
//	/* (non-Javadoc)
//	 * @see ca.jhosek.linguaelive.ui.priv.admin.QueryUsersView#getAdminUserViewer()
//	 */
//	@Override
//	public Editor<UserProxy> getAdminUserViewer() {
//		// 
//		return userProfilePanel;
//	}


//	/* 
//	 * show user information
//	 * 
//	 * (non-Javadoc)
//	 * @see ca.jhosek.linguaelive.ui.priv.admin.QueryUsersView#showUser(ca.jhosek.linguaelive.UserProxy)
//	 */
//	@Override
//	public void showUser(UserProxy user) {
//		
//		// 
//		timezoneOffset.setText( user.getTimezoneOffset().toString() + " minutes" );
//		currentUserTime.setText( getUserLocalTime( user.getTimezoneOffset() ).toString() );
//		// 
//		userProfilePanel.setVisible(true);
//	}
//
//	/**
//	 * @param timezoneOffset
//	 * @return
//	 */
//	@SuppressWarnings("deprecation")
//	private Date getUserLocalTime(Integer timezoneOffset ) {
//		// 
//		Date now = new Date();
//		
//	 	int thisUsersOffset = now.getTimezoneOffset();
//	 	int timezoneDifference = thisUsersOffset - timezoneOffset;
//	 	// set the minutes to handle the timezone offset
//	 	now.setMinutes(timezoneDifference);
//	 			
//		return now;
//	}

	
	@UiHandler("executeQueryButton")
	void onExecuteQueryButtonClick(ClickEvent event) {
		presenter.queryUsers();
	}
	
	/**
	 * used for display of the list of users on the left side cell list
	 * 
	 * @author copyright (C) 2011 Andrew Stevko
	 *
	 */
	private static class UserProxyCell extends AbstractCell<UserProxy> {

		/* (non-Javadoc)
		 * @see com.google.gwt.cell.client.AbstractCell#render(com.google.gwt.cell.client.Cell.Context, java.lang.Object, com.google.gwt.safehtml.shared.SafeHtmlBuilder)
		 */
		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				UserProxy user, SafeHtmlBuilder sb) {
			// 
			if ( user == null ) 
				return;
			// concat the display
			sb.appendHtmlConstant(LIST_PROMPT_DIV_W_STYLE );
			sb.append( SafeHtmlUtils.fromString(user.getFirstName() + " " + user.getLastName() + ", " + user.getSchool()) );
			sb.appendHtmlConstant("</div>" );						
		}
		
	}
	
	
	
	/**
	 * @return the userProfilePanel
	 */
	public AdminUserViewerImpl getUserProfilePanel() {
		return userProfilePanel;
	}

	public void showContactInfo(List<ContactInfoProxy> response) {
		// 
		userProfilePanel.showContactInfo(response);
	}

	@Override
	public String getEmailAddressFilter() {
		return emailAddressFilter.getValue();
	}


}
