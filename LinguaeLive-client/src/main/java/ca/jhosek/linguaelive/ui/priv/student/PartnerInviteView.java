package ca.jhosek.main.client.ui.priv.student;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

import ca.jhosek.main.client.ui.priv.student.PartnerInviteViewImpl.Driver;
import ca.jhosek.main.shared.LanguageType;

/**
 * @author copyright (C) 2011 Andrew Stevko
 *
 * @see PartnerInviteViewImpl
 * 
 */
public interface PartnerInviteView extends IsWidget {

	public void setPresenter(Presenter presenter);

	/**
	 * clear the display
	 */
	public void clear();

	/**
	 * 
	 * @author copyright (C) 2011 Andrew Stevko
	 *
	 */
	public interface Presenter {
		/**
		 * accept or decline - respond to course link request
		 * 
		 * @param accept
		 */
		void respondToSessionInvite( Boolean accept );
		/**
		 * @param sessionLang start the session with this language by logged in user
		 */
//		void startNewSessionWithCurrentInvite(LanguageType sessionLang);
		void gotoMember1Course();
		void gotoMember2Course();
		void startSessionWithCurrentInvite();
		void startOtherSessionWithCurrentInvite();
		/**
		 * used to update page when a session is started.
		 */
		void checkForInprogressSessions();
	}
	
	public Driver 
		createEditorDriver(EventBus eventBus, RequestFactory requestFactory);

	/**
	 * displays/hides buttons
	 * 
	 * @param pending
	 * @param accepted
	 */
	void setViewMode( Boolean sender, Boolean pending, Boolean accepted);
	/**
	 * updates buttons with language names
	 * @param first
	 * @param second
	 */
	void updateLanguageButtons(LanguageType first, LanguageType second);

	void showMember2UserOnlineStatus(String onlineStatus);

	void showMember1UserOnlineStatus(String onlineStatus);
}
