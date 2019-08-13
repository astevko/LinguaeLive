/**
 * 
 */
package ca.jhosek.linguaelive.domain;

import java.io.Serializable;
import java.util.Date;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;

/**
 * a Session invitation from Member1 to Member2, 
 * starts out pending=true and accepted=false
 * 
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 * @see SessionInviteDao
 */
@Entity
public class SessionInvite  extends DatastoreObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2398416359335439934L;

	/**
	 * did member2 accept or decline the invitation
	 * invite accepted or rejected
	 */
	private Boolean		accepted  = false;
	
	/**
	 * date this invite was created
	 */
	private Date		createDate = new Date();
	
	/**
	 * the first member of this session
	 */
	@Ignore
	private
	transient Member member1;
	
	@Index
	private Key<Member> member1Key;
	
	/**
	 * the second member of this session
	 */
	@Ignore
	private
	transient Member member2;

	@Index
	private Key<Member> member2Key;

	/**
	 * invite responded too
	 */
	@Index
	private Boolean		pending = true;
	/**
	 * personal message associated with this invite
	 */
	private String personalMessage = "";

	/**
	 * default used to serialize
	 */
	public SessionInvite() {
		// timestamp to NOW
		createDate=new Date();
	}
	/**
	 * @param member1
	 * @param member2
	 * @param personalMessage
	 * @param startDateTime
	 */
	public SessionInvite(Member member1, Member member2,
			String personalMessage) {
		// timestamp to NOW
		createDate=new Date();
		this.setMember1( member1 );
		this.setMember2( member2 );
		this.personalMessage = personalMessage;
	}
	/**
	 * @return the accepted
	 */
	public Boolean getAccepted() {
		return accepted;
	}
	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * @return the member1
	 * @throws EntityNotFoundException 
	 */
	public Member getMember1() throws EntityNotFoundException {
		return new MemberDao().get( member1Key );
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
		return new MemberDao().get( member2Key );

	}
	/**
	 * @return the member2Key
	 */
	public Key<Member> getMember2Key() {
		return member2Key;
	}
	
	/**
	 * @return the pending
	 */
	public Boolean getPending() {
		return pending;
	}
	
	/**
	 * @return the personalMessage
	 */
	public String getPersonalMessage() {
		return personalMessage;
	}
	/**
	 * @param accepted the accepted to set
	 */
	public void setAccepted(Boolean accepted) {
		this.accepted = accepted;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * @param member1 the member1 to set
	 */
	public void setMember1(Member member1) {
		this.member1Key = new MemberDao().key(member1);
	}
	/**
	 * @param member1Key the member1Key to set
	 */
	public void setMember1Key(Key<Member> member1Key) {
		this.member1Key = member1Key;
	}
	/**
	 * @param member2 the member2 to set
	 */
	public void setMember2(Member member2) {
		this.member2Key = new MemberDao().key(member2);
	}
	/**
	 * @param member2Key the member2Key to set
	 */
	public void setMember2Key(Key<Member> member2Key) {
		this.member2Key = member2Key;
	}
	/**
	 * @param pending the pending to set
	 */
	public void setPending(Boolean pending) {
		this.pending = pending;
	}
	/**
	 * @param personalMessage the personalMessage to set
	 */
	public void setPersonalMessage(String personalMessage) {
		this.personalMessage = personalMessage;
	}
}
