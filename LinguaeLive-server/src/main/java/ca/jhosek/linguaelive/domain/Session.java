/**
 * 
 */
package ca.jhosek.main.server.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;

import ca.jhosek.main.shared.ContactInfoType;
import ca.jhosek.main.shared.LanguageType;
import ca.jhosek.main.shared.proxy.SessionProxy;

/**
 * a session
 * 
 * @author copyright (C) 2011 Andrew Stevko
 * @see SessionProxy
 * @see SessionDao
 */
@Entity
public class Session extends DatastoreObject implements Serializable {

	private static final Logger logger = Logger.getLogger(Session.class
			.getName());

	/**
	 * 
	 */
	private static final long serialVersionUID = -2398416359335439934L;

	@Index
	private Boolean cancelled = false;

	private Long durationMinutes = 0L;

	/**
	 * the first member of this session
	 */
	@Ignore
	private transient Member member1;
	private ContactInfoType member1ChannelType;

	@Index
	private Key<Member> member1Key;

	/**
	 * the second member of this session
	 */
	@Ignore
	private transient Member member2;
	private ContactInfoType member2ChannelType;

	@Index
	private Key<Member> member2Key;
	private Long nextSessionId = 0L;

	private LanguageType otherLanguage = null;

	private LanguageType sessionLanguage = null;
	private List<String> sessionNotes = new ArrayList<String>();

	/**
	 * which member started this: 0 = none 1 = Member1 2 = Member2
	 */
	private Integer startMember = 0;

	@Index
	private Date startTime = null;

	@Index
	private Date stopTime = null;

	/**
	 * default used to serialize
	 */
	public Session() {
		//
	}

	/**
	 * copy constructor specifying the sessionLanguage; otherLanguage is
	 * calculated
	 * 
	 * @param previous
	 * @param sessionLang
	 */
	public Session(final Session previous, final LanguageType sessionLang) {
		this.member1 = previous.member1;
		this.member1Key = previous.member1Key;
		this.member2 = previous.member2;
		this.member2Key = previous.member2Key;
		this.sessionLanguage = sessionLang;
		this.otherLanguage = (previous.sessionLanguage == sessionLang) ? previous
				.getOtherLanguage() : previous.getSessionLanguage();
		this.member1ChannelType = previous.getMember1ChannelType();
		this.member2ChannelType = previous.getMember2ChannelType();
	}

	/**
	 * create a new session from this Invite and a Language.
	 */
	public Session(final SessionInvite invite, final LanguageType sessionLang) {
		final MemberDao memberDao = new MemberDao();
		final CourseDao courseDao = new CourseDao();
		//
		try {
			this.member1 = memberDao.get(invite.getMember1Key());
			this.member1Key = invite.getMember1Key();

			this.member2 = memberDao.get(invite.getMember2Key());
			this.member2Key = invite.getMember2Key();

			final Course course = courseDao.get(member1.getCourseKey());
			if (sessionLang.equals(course.getExpertLanguage())) {
				this.sessionLanguage = course.getExpertLanguage();
				this.otherLanguage = course.getTargetLanguage();
			} else {
				this.sessionLanguage = course.getTargetLanguage();
				this.otherLanguage = course.getExpertLanguage();
			}

		} catch (final EntityNotFoundException e) {
			//
			logger.severe("trying to create new Session with incomplete info from invite id="
					+ invite.getId().toString());
		}
	}

	/**
	 * @param note
	 *            the note to add
	 */
	public void addNote(final String note) {
		this.sessionNotes.add(note);
	}

	/**
	 * @return the cancelled
	 */
	public Boolean getCancelled() {
		return cancelled;
	}

	/**
	 * @return the durationMinutes
	 */
	public Long getDurationMinutes() {
		return durationMinutes;
	}

	/**
	 * @return the member1
	 * @throws EntityNotFoundException
	 */
	public Member getMember1() throws EntityNotFoundException {
		return new MemberDao().get(member1Key);
	}

	/**
	 * @return the member1ChannelType
	 */
	public ContactInfoType getMember1ChannelType() {
		return member1ChannelType;
	}

	/**
	 * @return the member1Key
	 */
	public Key<Member> getMember1Key() {
		return member1Key;
	}

	/**
	 * @return the member2
	 * @throws EntityNotFoundException
	 */
	public Member getMember2() throws EntityNotFoundException {
		return new MemberDao().get(member2Key);

	}

	/**
	 * @return the member2ChannelType
	 */
	public ContactInfoType getMember2ChannelType() {
		return member2ChannelType;
	}

	/**
	 * @return the member2Key
	 */
	public Key<Member> getMember2Key() {
		return member2Key;
	}

	/**
	 * @return the nextSessionId
	 */
	public Long getNextSessionId() {
		return nextSessionId;
	}

	/**
	 * @return the notes
	 */
	public List<String> getNotes() {
		return sessionNotes;
	}

	/**
	 * @return the otherLanguage
	 */
	public LanguageType getOtherLanguage() {
		return otherLanguage;
	}

	/**
	 * @return the sessionLanguage
	 */
	public LanguageType getSessionLanguage() {
		return sessionLanguage;
	}

	/**
	 * @return the startMember
	 */
	public Integer getStartMember() {
		return startMember;
	}

	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @return the stopTime
	 */
	public Date getStopTime() {
		return stopTime;
	}

	/**
	 * isActive means... not canceled and has a start time and does not have a
	 * stop time
	 * 
	 * @param session
	 * @return is the session active
	 */
	boolean isActive() {

		return (!this.cancelled && this.startTime != null && this.stopTime == null);
	}

	/**
	 * @param cancelled
	 *            the cancelled to set
	 */
	public void setCancelled(final Boolean cancelled) {
		this.cancelled = cancelled;
	}

	/**
	 * @param durationMinutes
	 *            the durationMinutes to set
	 */
	public void setDurationMinutes(final Long durationMinutes) {
		this.durationMinutes = durationMinutes;
	}

	/**
	 * @param member1
	 *            the member1 to set
	 */
	public void setMember1(final Member member1) {
		this.member1Key = new MemberDao().key(member1);
	}

	/**
	 * @param member1ChannelType
	 *            the member1ChannelType to set
	 */
	public void setMember1ChannelType(final ContactInfoType member1ChannelType) {
		this.member1ChannelType = member1ChannelType;
	}

	/**
	 * @param member1Key
	 *            the member1Key to set
	 */
	public void setMember1Key(final Key<Member> member1Key) {
		this.member1Key = member1Key;
	}

	/**
	 * @param member2
	 *            the member2 to set
	 */
	public void setMember2(final Member member2) {
		this.member2Key = new MemberDao().key(member2);
	}

	/**
	 * @param member2ChannelType
	 *            the member2ChannelType to set
	 */
	public void setMember2ChannelType(final ContactInfoType member2ChannelType) {
		this.member2ChannelType = member2ChannelType;
	}

	/**
	 * @param member2Key
	 *            the member2Key to set
	 */
	public void setMember2Key(final Key<Member> member2Key) {
		this.member2Key = member2Key;
	}

	public void setNextSession(final Session session) {
		//
		this.nextSessionId = session.getId();
	}

	/**
	 * @param nextSessionId
	 *            the nextSessionId to set
	 */
	public void setNextSessionId(final Long nextSessionId) {
		this.nextSessionId = nextSessionId;
	}

	/**
	 * @param notes
	 *            the notes to set
	 */
	public void setNotes(final List<String> notes) {
		this.sessionNotes = notes;
	}

	/**
	 * @param otherLanguage
	 *            the otherLanguage to set
	 */
	public void setOtherLanguage(final LanguageType otherLanguage) {
		this.otherLanguage = otherLanguage;
	}

	/**
	 * @param sessionLanguage
	 *            the sessionLanguage to set
	 */
	public void setSessionLanguage(final LanguageType sessionLanguage) {
		this.sessionLanguage = sessionLanguage;
	}

	/**
	 * @param startMember
	 *            the startMember to set
	 */
	public void setStartMember(final Integer startMember) {
		this.startMember = startMember;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(final Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * @param stopTime
	 *            the stopTime to set
	 */
	public void setStopTime(final Date stopTime) {
		if (this.stopTime != null) {
			// session already stopped.
			return;
		}
		this.stopTime = stopTime;

		final long durationMsec = this.stopTime.getTime()
				- this.startTime.getTime();
		final long durationMin = durationMsec / (1000 * 60);
		logger.info("stopSession() durationmin=" + durationMin);
		this.setDurationMinutes(durationMin);

	}
}
