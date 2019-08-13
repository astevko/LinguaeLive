package ca.jhosek.linguaelive.proxy;

import java.util.Date;
import java.util.List;

import com.google.web.bindery.requestfactory.shared.ProxyForName;

// import ca.jhosek.linguaelive.domain.ObjectifyLocator;
// import ca.jhosek.linguaelive.domain.Session;
// import ca.jhosek.linguaelive.domain.SessionDao;
import ca.jhosek.linguaelive.ContactInfoType;
import ca.jhosek.linguaelive.LanguageType;

/**
 * A lab session between two student class members
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 * @see Session
 * @see SessionDao
 */
@ProxyForName( value="ca.jhosek.linguaelive.domain.Session", locator="ca.jhosek.linguaelive.domain.ObjectifyLocator")
public interface SessionProxy extends DatastoreObjectProxy {
	/**
	 * @return the durationMinutes
	 */
	public Long getDurationMinutes();
	/**
	 * @return the member1Key
	 */
	public MemberProxy getMember1();
	/**
	 * @return the member2Key
	 */
	public MemberProxy getMember2();
	/**
	 * @return the startTime
	 */
	public Date getStartTime();
	/**
	 * @return the stopTime
	 */
	public Date getStopTime();
	/**
	 * @param durationMinutes the durationMinutes to set
	 */
	public void setDurationMinutes(Long durationMinutes);
	/**
	 * @param member1Key the member1Key to set
	 */
	public void setMember1(MemberProxy member1);
	/**
	 * @param member2Key the member2Key to set
	 */
	public void setMember2(MemberProxy member2);
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Date startTime);
	/**
	 * @param stopTime the stopTime to set
	 */
	public void setStopTime(Date stopTime);


	/**
	 * @param sessionLanguage the sessionLanguage to set
	 */
	public void setSessionLanguage(LanguageType sessionLanguage);
	/**
	 * @return the sessionLanguage
	 */
	public LanguageType getSessionLanguage();
	/**
	 * @param notes the notes to set
	 */
	public void setNotes(List<String> notes);
	/**
	 * @return the notes
	 */
	public List<String> getNotes();
	/**
	 * @param otherLanguage the otherLanguage to set
	 */
	public void setOtherLanguage(LanguageType otherLanguage);
	/**
	 * @return the otherLanguage
	 */
	public LanguageType getOtherLanguage();

	/**
	 * @param member1ChannelType the member1ChannelType to set
	 */
	public void setMember1ChannelType(ContactInfoType member1ChannelType);
	/**
	 * @return the member1ChannelType
	 */
	public ContactInfoType getMember1ChannelType();
	/**
	 * @param member2ChannelType the member2ChannelType to set
	 */
	public void setMember2ChannelType(ContactInfoType member2ChannelType);
	/**
	 * @return the member2ChannelType
	 */
	public ContactInfoType getMember2ChannelType();
	
	/**
	 * @return the startMember
	 */
	public Integer getStartMember();
	/**
	 * @param startMember the startMember to set
	 */
	public void setStartMember(Integer startMember);
	
	/**
	 * @return the id for the next session
	 */
	public Long getNextSessionId();

	/**
	 * @param cancelled the cancelled to set
	 */
	public void setCancelled(Boolean cancelled);
	/**
	 * @return the cancelled
	 */
	public Boolean getCancelled();
}
