/**
 * 
 */
package ca.jhosek.linguaelive.ui.priv.student;

import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DateLabel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.NumberLabel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

import ca.jhosek.linguaelive.LLConstants;
import ca.jhosek.linguaelive.activity.mainregion.SessionControlActivity;
import ca.jhosek.linguaelive.place.SessionControlPlace;
import ca.jhosek.linguaelive.widgets.EnumLabel;
import ca.jhosek.linguaelive.widgets.OnlineOfflineIndicator;
import ca.jhosek.linguaelive.ContactInfoType;
import ca.jhosek.linguaelive.LanguageType;
import ca.jhosek.linguaelive.proxy.ContactInfoProxy;
import ca.jhosek.linguaelive.proxy.SessionProxy;

/**
 * Session Control Panel
 * 
 * @author copyright (C) 2011-2014 Andrew Stevko
 * 
 * @see SessionControlView
 * @see SessionControlActivity
 * @see SessionControlPlace
 * @see SessionProxy
 * 
 */
public class SessionControlViewImpl extends Composite implements IsWidget,
		SessionControlView, Editor<SessionProxy> {

	public interface Driver extends
			RequestFactoryEditorDriver<SessionProxy, SessionControlViewImpl> {

	}

	/**
	 * paint contact cell for member 1
	 * 
	 * @author andy
	 * 
	 */
	private class Member1ContactInfoCell extends AbstractCell<ContactInfoProxy> {

		@Override
		public void render(
				final com.google.gwt.cell.client.Cell.Context context,
				final ContactInfoProxy contactInfo, final SafeHtmlBuilder sb) {

			// DO NOT DISPLAY null or default contact info
			if (contactInfo == null) {
				return;
			}
			final String info = contactInfo.getInfo().trim();
			if (info.isEmpty()) {
				return; // nothing there
			}
			if (info.startsWith("enter your")) {
				return; // default only
			}

			sb.appendHtmlConstant("<div><span><b>");
			final ContactInfoType type = contactInfo.getType();
			sb.append(SafeHtmlUtils.fromSafeConstant(type.name()));
			sb.appendHtmlConstant(":</b></span><br /><span>");

			if (type == ContactInfoType.Skype) {
				// paint skype link
				sb.append(SafeHtmlUtils.fromTrustedString("<a href='skype:"
						+ info + "?call'>" + info + "</a>"));
			} else {
				// display phone number
				sb.append(SafeHtmlUtils.fromString(info));
			}
			sb.appendHtmlConstant("</span></div>");
			// output string into log
			logger.info(sb.toString());
		}
	}

	/**
	 * what happens when the user selects the member1's contact info list
	 * 
	 * @author copyright (C) 2011 Andrew Stevko
	 * 
	 */
	private final class Member1ContactInfoCellClickHandler implements
			SelectionChangeEvent.Handler {
		private final SingleSelectionModel<ContactInfoProxy> selectionModel;

		private Member1ContactInfoCellClickHandler(
				final SingleSelectionModel<ContactInfoProxy> selectionModel) {
			this.selectionModel = selectionModel;
		}

		@Override
		public void onSelectionChange(final SelectionChangeEvent event) {
			logger.info("Member1ContactInfoCellClickHandler()");
			//
			final ContactInfoProxy contactInfo = selectionModel
					.getSelectedObject();
			if (contactInfo == null) {
				return;
			}
			// update the record with the user selected contact info type
			// can be used as a record that this was the channel by which the
			// conversation took place.
			presenter.setMember1ContactInfoSelection(contactInfo);
		}
	}

	/**
	 * paint contact cell for member 2
	 * @author Andrew Stevko
	 *
	 */
	private class Member2ContactInfoCell extends AbstractCell<ContactInfoProxy> {

		@Override
		public void render(
				final com.google.gwt.cell.client.Cell.Context context,
				final ContactInfoProxy contactInfo, final SafeHtmlBuilder sb) {

			// DO NOT DISPLAY null or default contact info
			if (contactInfo == null) {
				return;
			}
			final String info = contactInfo.getInfo().trim();
			if (info.isEmpty()) {
				return; // nothing there
			}
			if (info.startsWith("enter your")) {
				return; // default only
			}

			sb.appendHtmlConstant("<div><span><b>");
			final ContactInfoType type = contactInfo.getType();
			sb.append(SafeHtmlUtils.fromSafeConstant(type.name()));
			sb.appendHtmlConstant(":</b></span><br /><span>");

			if (type == ContactInfoType.Skype) {
				// paint skype link
				sb.append(SafeHtmlUtils.fromTrustedString("<a href='skype:"
						+ info + "?call'>" + info + "</a>"));
			} else {
				sb.append(SafeHtmlUtils.fromString(info));
			}
			sb.appendHtmlConstant("</span></div>");
			// output string into log
			logger.info(sb.toString());
		}
	}

	/**
	 * what happens when the user selects the member2's contact info list
	 * 
	 * @author copyright (C) 2011 Andrew Stevko
	 * 
	 */
	private final class Member2ContactInfoCellClickHandler implements
			SelectionChangeEvent.Handler {
		private final SingleSelectionModel<ContactInfoProxy> selectionModel;

		private Member2ContactInfoCellClickHandler(
				final SingleSelectionModel<ContactInfoProxy> selectionModel) {
			this.selectionModel = selectionModel;
		}

		@Override
		public void onSelectionChange(final SelectionChangeEvent event) {
			logger.info("Member2ContactInfoCellClickHandler()");
			//
			final ContactInfoProxy contactInfo = selectionModel
					.getSelectedObject();
			if (contactInfo == null) {
				return;
			}
			// update the record with the user selected contact info type
			// can be used as a record that this was the channel by which the
			// conversation took place.
			presenter.setMember2ContactInfoSelection(contactInfo);
		}
	}

	interface SessionControlUiBinder extends
			UiBinder<Widget, SessionControlViewImpl> {
	}

	// logger boilerplate
	private static final Logger logger = Logger
			.getLogger(SessionControlViewImpl.class.getName());

	// interface
	private static SessionControlUiBinder uiBinder = GWT
			.create(SessionControlUiBinder.class);

	@UiField
	Button cancelSession;
	private final LLConstants constants;

	@UiField
	NumberLabel<Long> durationMinutes;

	@Ignore
	@UiField
	HTML inprogressSession;

	@UiField(provided = true)
	CellList<ContactInfoProxy> member1_contactinfo;
	@Path("member1.user.emailAddress")
	@UiField
	Label member1_user_email;
	// Member 1 details
	@Path("member1.user.firstName")
	@UiField
	InlineLabel member1_user_fname;
	// Member 1 online/offline indicator
	@Path("member1.user.isOnline")
	@UiField(provided = true)
	OnlineOfflineIndicator member1_user_isonline;
	@Path("member1.user.lastName")
	@UiField
	InlineLabel member1_user_lname;
	@Path("member1.user.location")
	@UiField
	Label member1_user_location;

	@Path("member1.user.school")
	@UiField
	Label member1_user_school;

	@Path("member1.user.currentUserTime")
	@UiField
	Label member1_user_time;

	@UiField(provided = true)
	CellList<ContactInfoProxy> member2_contactinfo;
	@Path("member2.user.emailAddress")
	@UiField
	Label member2_user_email;
	@Path("member2.user.firstName")
	@UiField
	InlineLabel member2_user_fname;

	// member2
	@Path("member2.user.isOnline")
	@UiField(provided = true)
	OnlineOfflineIndicator member2_user_isonline;
	@Path("member2.user.lastName")
	@UiField
	InlineLabel member2_user_lname;
	@Path("member2.user.location")
	@UiField
	Label member2_user_location;

	@Path("member2.user.school")
	@UiField
	Label member2_user_school;
	@Path("member2.user.currentUserTime")
	@UiField
	Label member2_user_time;
	private ListDataProvider<String> noteDataProvider;
	@UiField(provided = true)
	CellList<String> notes;
	@UiField(provided = true)
	SimplePager notesPager;

	@Ignore
	@UiField
	TextArea postThisNote;
	private Presenter presenter;

	@UiField
	Button refreshLink;

	@UiField
	EnumLabel<LanguageType> sessionLanguage;

	@UiField
	HTMLPanel start; // start time

	@UiField
	DateLabel startTime;

	@Ignore
	@UiField
	InlineLabel status;

	@UiField
	HTMLPanel stop; // stop time & duration

	// buttons
	@UiField
	Button stopSession;

	@UiField
	DateLabel stopTime;

	@UiField
	Button switchSession;

	/**
	 * initialize display elements/widgets
	 * @param constants inject localized constants 
	 */
	@Inject
	public SessionControlViewImpl(final LLConstants constants) {
		this.constants = constants;
		{ // ------------------------
			// member1 custom selection model, selection handler, list, and
			// pager
			final Member1ContactInfoCell m1InfoCell = new Member1ContactInfoCell();
			final SingleSelectionModel<ContactInfoProxy> m1selectionModel = new SingleSelectionModel<ContactInfoProxy>();
			m1selectionModel
					.addSelectionChangeHandler(new Member1ContactInfoCellClickHandler(
							m1selectionModel));
			member1_contactinfo = new CellList<ContactInfoProxy>(m1InfoCell);
			member1_contactinfo.setSelectionModel(m1selectionModel);
			final SimplePager pager1 = new SimplePager();
			pager1.setDisplay(member1_contactinfo);
			member1_contactinfo.setEmptyListWidget(new HTML(constants
					.noContactInfoFound()));
			member1_user_isonline = new OnlineOfflineIndicator("member1");

			// ------------------------
			// member2 custom selection model, selection handler, list, and
			// pager
			final Member2ContactInfoCell m2InfoCell = new Member2ContactInfoCell();
			final SingleSelectionModel<ContactInfoProxy> m2selectionModel = new SingleSelectionModel<ContactInfoProxy>();
			m2selectionModel
					.addSelectionChangeHandler(new Member2ContactInfoCellClickHandler(
							m2selectionModel));
			member2_contactinfo = new CellList<ContactInfoProxy>(m2InfoCell);
			member2_contactinfo.setSelectionModel(m2selectionModel);
			final SimplePager pager2 = new SimplePager();
			pager2.setDisplay(member2_contactinfo);
			member2_contactinfo.setEmptyListWidget(new HTML(constants
					.noContactInfoFound()));
			member2_user_isonline = new OnlineOfflineIndicator("member2");

			// ------------------------
			// NOTES custom selection model, selection handler, list, and pager
			notes = new CellList<String>(new TextCell());
			notesPager = new SimplePager();
			notesPager.setDisplay(notes);
			notes.setEmptyListWidget(new HTML(constants
					.noNotesHaveBeenEnteredForThisSession()));

		}
		initWidget(uiBinder.createAndBindUi(this));

		// enter key in note field adds text to chat then clears text box
		postThisNote.addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(final KeyPressEvent event) {
				final int code = event.getNativeEvent().getKeyCode();
				if (code == KeyCodes.KEY_ENTER) {
					presenter.addNote(postThisNote.getValue());
					noteDataProvider.getList().add(postThisNote.getValue());
					postThisNote.setValue("");
				}
			}
		});

		clear();
	}

	/**
	 * clear the display
	 * 
	 * @see ca.jhosek.linguaelive.ui.priv.student.SessionControlView#clear()
	 */
	@Override
	public void clear() {
		// display Status Blurb
		status.setText("");

		// display timestamp blocks
		start.setVisible(false);
		stop.setVisible(false);

		stopSession.setVisible(false);

		switchSession.setVisible(false);

		inprogressSession.setVisible(false);
		cancelSession.setVisible(false);

	}

	/**
	 * @return a CourseProxy editor driver, userProxyDriver, tied to this
	 *         presenter and view
	 */
	@Override
	public Driver createEditorDriver(final EventBus eventBus,
			final RequestFactory requestFactory) {
		//
		logger.info("createEditorDriver( SessionInviteViewImpl )");
		final Driver driver = GWT.create(Driver.class);
		driver.initialize(eventBus, requestFactory, this);
		return driver;
	}

	@UiHandler("cancelSession")
	void onCancelButtonClick(final ClickEvent event) {
		logger.info("onCancelButtonClick()");
		presenter.cancelSession();
	}

	@UiHandler("refreshLink")
	void onRefreshLinkClick(final ClickEvent event) {
		logger.info("onRefreshLinkClick()");
		presenter.refresh();
	}

	@UiHandler("stopSession")
	void onStopSessionButtonClick(final ClickEvent event) {
		logger.info("onStopSessionButtonClick()");
		presenter.stopSession();
	}

	@UiHandler("switchSession")
	void onSwitchSessionButtonClick(final ClickEvent event) {
		logger.info("onSwitchSessionButtonClick()");
		presenter.swapLanguages();
	}

	@Override
	public void setPresenter(final Presenter presenter) {
		//
		this.presenter = presenter;
	}

	/**
	 * display only the proper buttons and labels
	 * 
	 * @see ca.jhosek.linguaelive.ui.priv.student.SessionControlView#setViewMode(java.lang.Boolean,
	 *      java.lang.Boolean, ca.jhosek.linguaelive.LanguageType,
	 *      ca.jhosek.linguaelive.LanguageType)
	 */
	@Override
	public void setViewMode(final Boolean isCancelled, final Boolean isStarted,
			final Boolean isActive, final LanguageType sessionLang,
			final LanguageType otherLang, final Boolean isMember1Viewing,
			final Boolean isMember2Viewing, final Boolean canStartStopSession,
			final Boolean isMember1Online, final Boolean isMember2Online) {

		final boolean isMemberViewing = isMember1Viewing || isMember2Viewing;

		logger.info("setViewMode( " + isStarted + " " + isActive + " "
				+ sessionLang.name() + " " + otherLang.name());
		// display Status Blurb
		String statusBlurb;
		if (isCancelled) {
			statusBlurb = constants.sessionCancelled();
		} else if (isStarted) {

			if (isActive) {
				statusBlurb = constants.sessionInProgress(); // "In Progress";

			} else {
				statusBlurb = constants.sessionCompleted(); // "Completed";
			}
		} else {
			statusBlurb = constants.sessionNotStarted(); // "Not started";
		}
		status.setText(statusBlurb);

		// display timestamp blocks
		start.setVisible(isStarted);
		stop.setVisible(isStarted && !isActive);

		stopSession.setVisible(isMemberViewing && (isStarted && isActive)
				&& canStartStopSession && !isCancelled);

		stopSession.setText(constants.stopTiming() + " " + sessionLang.name()
				+ " " + constants.session());

		switchSession.setVisible(isMemberViewing && (isStarted && isActive)
				&& canStartStopSession && !isCancelled);
		// final String switchToTiming = "Switch to Timing ";
		switchSession.setText(constants.switchToTiming() + " "
				+ otherLang.name());

		inprogressSession.setVisible(isMemberViewing && (isStarted && isActive)
				&& !canStartStopSession && !isCancelled);
		cancelSession.setVisible(isMemberViewing && (isStarted && isActive)
				&& !canStartStopSession && !isCancelled);

	}

	/**
	 * display the contact info list for member1
	 * @param contactInfos list of contact info objects
	 * @param selected is any one pre-selected?
	 * @see ca.jhosek.linguaelive.ui.priv.student.SessionControlView#showMember1ContactInfoList(java.util.List, ca.jhosek.linguaelive.proxy.ContactInfoProxy)
	 */
	@Override
	public void showMember1ContactInfoList(
			final List<ContactInfoProxy> contactInfos,
			final ContactInfoProxy selected) {
		logger.info("showContactInfoList( count = " + contactInfos.size()
				+ " )");

		final ListDataProvider<ContactInfoProxy> dataProvider = new ListDataProvider<ContactInfoProxy>();
		dataProvider.setList(contactInfos);
		dataProvider.addDataDisplay(member1_contactinfo);

		member1_contactinfo.setRowCount(contactInfos.size());
		member1_contactinfo.setVisibleRange(0, 5);
		member1_contactinfo.setRowData(0, contactInfos);
		if (selected != null) {
			logger.info("selecting member 1 type=" + selected.getType());
			member1_contactinfo.getSelectionModel().setSelected(selected, true);
		}
	}

	/**
	 * set user1 online status display
	 * 
	 * @param onlineStatus
	 *            ONLINE | OFFLINE | INSESSION | UNKNOWN
	 */
	@Override
	public void showMember1UserOnlineStatus(final String onlineStatus) {
		final boolean isOnline = ("ONLINE".equals(onlineStatus) || "INSESSION"
				.equals(onlineStatus));
		member1_user_isonline.setValue(isOnline);
	}

	/**
	 * display the contact info list for member2
	 * @param contactInfos list of contact info objects
	 * @param selected is any one pre-selected?
	 * @see ca.jhosek.linguaelive.ui.priv.student.SessionControlView#showMember1ContactInfoList(java.util.List, ca.jhosek.linguaelive.proxy.ContactInfoProxy)
	 */
	@Override
	public void showMember2ContactInfoList(
			final List<ContactInfoProxy> contactInfos,
			final ContactInfoProxy selected) {
		logger.info("showContactInfoList( count = " + contactInfos.size()
				+ " )");

		final ListDataProvider<ContactInfoProxy> dataProvider = new ListDataProvider<ContactInfoProxy>();
		dataProvider.setList(contactInfos);
		dataProvider.addDataDisplay(member2_contactinfo);

		member2_contactinfo.setRowCount(contactInfos.size());
		member2_contactinfo.setVisibleRange(0, 5);
		member2_contactinfo.setRowData(0, contactInfos);
		if (selected != null) {
			logger.info("selecting member 2 type=" + selected.getType());
			member2_contactinfo.getSelectionModel().setSelected(selected, true);
		}
	}

	/**
	 * set user2 online status display
	 * 
	 * @param onlineStatus
	 *            ONLINE | OFFLINE | INSESSION | UNKNOWN
	 */
	@Override
	public void showMember2UserOnlineStatus(final String onlineStatus) {
		final boolean isOnline = ("ONLINE".equals(onlineStatus) || "INSESSION"
				.equals(onlineStatus));
		member2_user_isonline.setValue(isOnline);
	}

	/**
	 * refresh the notes datadisplay
	 * 
	 * @see ca.jhosek.linguaelive.ui.priv.student.SessionControlView#showNotes(java.util.List)
	 */
	@Override
	public void showNotes(final List<String> sessisonNotes) {
		noteDataProvider = new ListDataProvider<String>();
		noteDataProvider.setList(sessisonNotes);
		noteDataProvider.addDataDisplay(notes);

		// display the last 5 rows
		final int notesSize = sessisonNotes.size();
		final int firstRow = (notesSize < 5) ? 0 : notesSize - 5;
		notes.setRowCount(notesSize);
		notes.setVisibleRange(firstRow, notesSize);
		notes.setRowData(0, sessisonNotes);
	}
}
