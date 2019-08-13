package ca.jhosek.linguaelive.ui.priv.student;

import java.util.List;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

import ca.jhosek.linguaelive.activity.mainregion.SessionControlActivity;
import ca.jhosek.linguaelive.place.SessionControlPlace;
import ca.jhosek.linguaelive.ui.priv.student.SessionControlViewImpl.Driver;
import ca.jhosek.linguaelive.LanguageType;
import ca.jhosek.linguaelive.proxy.ContactInfoProxy;

/**
 * @author copyright (C) 2011 Andrew Stevko
 *
 * @see SessionControlPlace
 * @see SessionControlViewImpl
 * @see SessionControlActivity
 */
public interface SessionControlView extends IsWidget {

	public void setPresenter(Presenter presenter);

	/**
	 * clear the display
	 */
	public void clear();

	/**
	 * session control presenter 
	 * @author copyright (C) 2011 Andrew Stevko
	 *
	 */
	public interface Presenter {
		/**
		 * stop the sesion
		 */
		void stopSession();

		void swapLanguages();

		void startNewSession( LanguageType sessionLang );
		
		void refresh();
		
		void addNote( String note );

		// member 1 selects this
		void setMember1ContactInfoSelection(ContactInfoProxy contactInfo);
		
		// member 2 selects this
		void setMember2ContactInfoSelection(ContactInfoProxy contactInfo);

		// cancel this session
		void cancelSession();
	}
	
	
	public Driver 
		createEditorDriver(EventBus eventBus, RequestFactory requestFactory);

	/**
	 * displays/hides buttons
	 * 
	 * @param isStarted
	 * @param isActive
	 * @param sessionLang
	 * @param otherLang
	 * @param isMember1Viewing
	 * @param isMember2Viewing
	 */
	void setViewMode( Boolean isCancelled, Boolean isStarted, Boolean isActive, LanguageType sessionLang, LanguageType otherLang, Boolean isMember1Viewing, Boolean isMember2Viewing, Boolean canStartStopSession , Boolean isMember1Online, Boolean isMember2Online ) ;

	void showMember1ContactInfoList(List<ContactInfoProxy> contactInfos, ContactInfoProxy selected);

	void showMember2ContactInfoList(List<ContactInfoProxy> contactInfos, ContactInfoProxy selected);

	public void showNotes(List<String> notes);

	void showMember2UserOnlineStatus(String onlineStatus);

	void showMember1UserOnlineStatus(String onlineStatus);	
}
