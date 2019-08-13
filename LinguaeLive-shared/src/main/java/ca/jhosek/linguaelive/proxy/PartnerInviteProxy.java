package ca.jhosek.linguaelive.proxy;

import java.util.Date;

// import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.web.bindery.requestfactory.shared.ProxyForName;

// import ca.jhosek.linguaelive.domain.ObjectifyLocator;
// import ca.jhosek.linguaelive.domain.SessionInvite;

/**
 * a session/partner invitation service proxy
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
@ProxyForName( value="ca.jhosek.linguaelive.domain.SessionInvite", locator="ca.jhosek.linguaelive.domain.ObjectifyLocator")
public interface PartnerInviteProxy extends DatastoreObjectProxy {
	/**
	 * @return the accepted
	 */
	public Boolean getAccepted();
	/**
	 * @return the createDate
	 */
	public Date getCreateDate();
	/**
	 * @return the member1
	 * @throws EntityNotFoundException 
	 */
	public MemberProxy getMember1() ;
	/**
	 * @return the member2
	 * @throws EntityNotFoundException 
	 */
	public MemberProxy getMember2();
	/**
	 * @return the pending
	 */
	public Boolean getPending();
	
	/**
	 * @return the personalMessage
	 */
	public String getPersonalMessage();
	/**
	 * @param accepted the accepted to set
	 */
	public void setAccepted(Boolean accepted);
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate);
	/**
	 * @param member1 the member1 to set
	 */
	public void setMember1(MemberProxy member1);
	/**
	 * @param member2 the member2 to set
	 */
	public void setMember2(MemberProxy member2);
	/**
	 * @param pending the pending to set
	 */
	public void setPending(Boolean pending);
	/**
	 * @param personalMessage the personalMessage to set
	 */
	public void setPersonalMessage(String personalMessage);
}
